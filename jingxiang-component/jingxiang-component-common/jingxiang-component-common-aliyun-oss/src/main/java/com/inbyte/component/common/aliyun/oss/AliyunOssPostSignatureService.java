package com.inbyte.component.common.aliyun.oss;

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.inbyte.commons.exception.BizException;
import com.inbyte.commons.model.dto.R;
import com.inbyte.component.common.aliyun.oss.dao.ObjectStorageMapper;
import com.inbyte.component.common.aliyun.oss.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 阿里云OSS POST签名服务
 * 用于Web端服务端签名直传
 *
 * @author chenjw
 * @date 2024/12/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliyunOssPostSignatureService {

    /**
     * STS临时凭证有效期（秒）- 固定值，默认3600秒（1小时）
     */
    private static final Long DURATION_SECONDS = 3600L;

    /**
     * 签名版本
     */
    private static final String SIGNATURE_VERSION = "OSS4-HMAC-SHA256";

    /**
     * OSS回调成功响应
     */
    private static final AliyunOssCallbackDto CALLBACK_SUCCESS = new AliyunOssCallbackDto("OK");

    /**
     * OSS回调验证失败响应
     */
    private static final AliyunOssCallbackDto CALLBACK_VERIFY_FAILED = new AliyunOssCallbackDto("verify not ok");

    private final AliyunOssProperties aliyunOssProperties;

    private final ObjectStorageMapper objectStorageMapper;

    @Value("${inbyte.app.server}")
    private String appServer;

    /**
     * 获取POST签名
     * 用于Web端服务端签名直传
     *
     * @param param 参数
     * @return POST签名信息
     */
    public R<AliyunOssPostSignatureDto> getPostSignature(AliYunOssStsTokenParam param) {
        // 获取STS临时凭证
        AssumeRoleResponse.Credentials credentials;
        try {
            credentials = getStsCredentials();
        } catch (Exception e) {
            log.error("获取STS临时凭证失败", e);
            throw BizException.error("获取STS临时凭证失败");
        }

        String accessKeyId = credentials.getAccessKeyId();
        String accessKeySecret = credentials.getAccessKeySecret();
        String securityToken = credentials.getSecurityToken();

        // 获取当前UTC时间
        ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);

        // 获取日期，格式为 yyyyMMdd
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String date = now.format(dateFormatter);

        // 获取x-oss-date，格式为 yyyyMMddTHHmmssZ
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
        String xOssDate = now.format(dateTimeFormatter);

        // 构建x-oss-credential
        String xOssCredential = String.format("%s/%s/%s/oss/aliyun_v4_request",
                accessKeyId, date, aliyunOssProperties.getRegion());

        // 构建上传目录前缀
        String uploadPath = buildUploadDir(param);

        // 创建Policy
        String policy = createPolicy(xOssCredential, xOssDate, securityToken, uploadPath);

        // 计算签名
        String signature = calculateSignature(policy, accessKeySecret, date,
                aliyunOssProperties.getRegion());

        // host格式: http://bucketname.oss-region.aliyuncs.com
        String host = "https://" + aliyunOssProperties.getBucketName() + "." + aliyunOssProperties.getEndpoint();

        InbyteObjectStoragePo inbyteObjectStoragePo = InbyteObjectStoragePo.builder()
                .mctNo(param.getMctNo())
                .url(host + "/" + uploadPath)
                .moduleName(param.getModuleName())
                .fileName(param.getFileName())
                .fileType(param.getFileType())
                .createTime(LocalDateTime.now())
                .creator(param.getOperator())
                .build();
        objectStorageMapper.insert(inbyteObjectStoragePo);

        // 生成回调配置
        String callback = createCallback(inbyteObjectStoragePo.getObjectId());

        AliyunOssPostSignatureDto signatureDto = AliyunOssPostSignatureDto.builder()
                .ossSignatureVersion(SIGNATURE_VERSION)
                .policy(policy)
                .xOssCredential(xOssCredential)
                .xOssDate(xOssDate)
                .signature(signature)
                .securityToken(securityToken)
                .uploadPath(uploadPath)
                .host(host)
                .callback(callback)
                .build();

        return R.ok(signatureDto);
    }

    /**
     * 获取STS临时凭证
     *
     * @return STS凭证
     */
    private AssumeRoleResponse.Credentials getStsCredentials() throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(aliyunOssProperties.getRegion(),
                aliyunOssProperties.getAccessKeyId(), aliyunOssProperties.getAccessKeySecret());
        DefaultAcsClient stsClient = new DefaultAcsClient(profile);

        // 构建AssumeRole请求
        AssumeRoleRequest request = new AssumeRoleRequest();
        request.setSysMethod(MethodType.POST);
        request.setRoleArn(aliyunOssProperties.getRoleArn());
        request.setRoleSessionName(aliyunOssProperties.getRoleSessionName());
        request.setDurationSeconds(DURATION_SECONDS);

        // 获取STS临时凭证
        AssumeRoleResponse response = stsClient.getAcsResponse(request);
        return response.getCredentials();
    }

    /**
     * 构建上传目录前缀
     *
     * @param param 参数
     * @return 上传目录前缀
     */
    private String buildUploadDir(AliYunOssStsTokenParam param) {
        // 文件目录格式: 商户空间/商户号/年/月/日/
        ZonedDateTime now = ZonedDateTime.now();
        String uploadDir = String.format("mct-space/%s/%d/%d/%d-%s",
                param.getMctNo(),
                now.getYear(),
                now.getMonthValue(),
                new Random().nextInt(10000000),
                param.getFileName());
        return uploadDir.replace("//", "/");
    }

    /**
     * 生成过期时间
     *
     * @param seconds 有效时长（秒）
     * @return ISO8601 时间字符串，如："2014-12-01T12:00:00.000Z"
     */
    private String generateExpiration(long seconds) {
        long now = Instant.now().getEpochSecond();
        long expirationTime = now + seconds;
        Instant instant = Instant.ofEpochSecond(expirationTime);
        ZoneId zone = ZoneOffset.UTC;
        ZonedDateTime zonedDateTime = instant.atZone(zone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return zonedDateTime.format(formatter);
    }

    /**
     * 创建Policy
     *
     * @param xOssCredential 凭证
     * @param xOssDate       日期
     * @param securityToken  安全令牌
     * @param uploadDir      上传目录前缀
     * @return Base64编码的Policy字符串
     */
    private String createPolicy(String xOssCredential, String xOssDate,
                                String securityToken, String uploadDir) {
        Map<String, Object> policy = new HashMap<>();
        policy.put("expiration", generateExpiration(DURATION_SECONDS));

        List<Object> conditions = new ArrayList<>();

        // Bucket条件
        Map<String, String> bucketCondition = new HashMap<>();
        bucketCondition.put("bucket", aliyunOssProperties.getBucketName());
        conditions.add(bucketCondition);

        // 安全令牌条件
        Map<String, String> securityTokenCondition = new HashMap<>();
        securityTokenCondition.put("x-oss-security-token", securityToken);
        conditions.add(securityTokenCondition);

        // 签名版本条件
        Map<String, String> signatureVersionCondition = new HashMap<>();
        signatureVersionCondition.put("x-oss-signature-version", SIGNATURE_VERSION);
        conditions.add(signatureVersionCondition);

        // 凭证条件
        Map<String, String> credentialCondition = new HashMap<>();
        credentialCondition.put("x-oss-credential", xOssCredential);
        conditions.add(credentialCondition);

        // 日期条件
        Map<String, String> dateCondition = new HashMap<>();
        dateCondition.put("x-oss-date", xOssDate);
        conditions.add(dateCondition);

        // 文件大小限制 (1字节 - 10MB)
        conditions.add(Arrays.asList("content-length-range", 1, 10240000));

        // 成功状态码
        conditions.add(Arrays.asList("eq", "$success_action_status", "200"));

        // 文件前缀限制
        conditions.add(Arrays.asList("starts-with", "$key", uploadDir));

        policy.put("conditions", conditions);

        // 将Policy转换为JSON字符串
        String jsonPolicy = JSONObject.toJSONString(policy);

        // Base64编码，明确使用UTF-8编码
        return BinaryUtil.toBase64String(jsonPolicy.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 计算HMAC-SHA256签名
     *
     * @param key  密钥
     * @param data 数据
     * @return HMAC-SHA256结果
     */
    private byte[] hmacsha256(byte[] key, String data) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate HMAC-SHA256", e);
        }
    }

    /**
     * 计算签名
     *
     * @param stringToSign    Base64编码的Policy字符串
     * @param accessKeySecret 访问密钥
     * @param date            日期 (yyyyMMdd)
     * @param region          地域
     * @return 签名字符串（十六进制）
     */
    private String calculateSignature(String stringToSign, String accessKeySecret,
                                      String date, String region) {
        // 步骤1: 计算dateKey
        byte[] dateKey = hmacsha256(("aliyun_v4" + accessKeySecret).getBytes(), date);

        // 步骤2: 计算dateRegionKey
        byte[] dateRegionKey = hmacsha256(dateKey, region);

        // 步骤3: 计算dateRegionServiceKey
        byte[] dateRegionServiceKey = hmacsha256(dateRegionKey, "oss");

        // 步骤4: 计算signingKey
        byte[] signingKey = hmacsha256(dateRegionServiceKey, "aliyun_v4_request");

        // 步骤5: 计算最终签名
        byte[] result = hmacsha256(signingKey, stringToSign);
        return BinaryUtil.toHex(result);
    }

    /**
     * 创建上传回调配置
     * 回调配置会被Base64编码后返回给前端
     *
     * @param objectId 对象存储ID，用于回调时更新上传状态
     * @return Base64编码的回调配置字符串
     */
    private String createCallback(Integer objectId) {
        if (appServer == null || appServer.isEmpty()) {
            log.warn("回调服务器地址未配置，将不返回回调配置");
            return null;
        }

        // 步骤5：设置回调。
        JSONObject jasonCallback = new JSONObject();
        jasonCallback.put("callbackUrl", appServer + "/api/aliyun/oss/callback");
        // TODO 待优化增加回调参数 https://help.aliyun.com/zh/oss/developer-reference/callback#3efd8ac8c0l4f
        jasonCallback.put("callbackBody","object=${object}&" +
                "size=${size}&" +
                "mimeType=${mimeType}&" +
                "height=${imageInfo.height}&" +
                "width=${imageInfo.width}&" +
                "objectId=" + objectId);
        jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");

        // Base64编码回调配置，明确使用UTF-8编码
        return BinaryUtil.toBase64String(jasonCallback.toString().getBytes(StandardCharsets.UTF_8));
    }

//    /**
//     * 回调通知 Post请求
//     *
//     * OSS上传文件成功后，会向应用服务器发送POST回调请求
//     * 应用服务器需要在5秒内返回JSON响应，HTTP状态码200表示成功
//     *
//     * 参考文档：https://help.aliyun.com/zh/oss/developer-reference/callback
//     *
//     * https://help.aliyun.com/zh/oss/user-guide/python-1?spm=a2c4g.11186623.0.i12
//     *
//     * @param request HTTP请求
//     * @return 回调响应结果，Spring Boot会自动序列化为JSON返回给OSS
//     *         成功返回：{"Status": "OK"}
//     *         失败返回：{"Status": "verify not ok"}
//     */
//    public AliyunOssCallbackDto callback(HttpServletRequest request) {
//        try {
//            request.setCharacterEncoding("UTF-8");
//            String ossCallbackBody = WebUtil.getRequestBodyString(request);
//            log.info("阿里云 OSS 回调参数:{}", ossCallbackBody);
//            boolean ret = VerifyOSSCallbackRequest(request, ossCallbackBody);
//            log.info("verify result : " + ret);
//
//            if (!ret) {
//                log.warn("OSS回调验证失败");
//                return CALLBACK_VERIFY_FAILED;
//            }
//
//            String decode = URLDecoder.decode(ossCallbackBody, "UTF-8");
//            JSONObject json = StringUtil.strToJson(decode);
//            String object = json.getString("object");
//            Integer objectId = json.getInteger("objectId");
//            String fileName = object.substring(object.lastIndexOf("/") + 1);
//            String mimeType = json.getString("mimeType");
//            Integer height = json.getInteger("height");
//            Integer width = json.getInteger("width");
//            Integer size = json.getInteger("size");
//
//            InbyteObjectStoragePo inbyteObjectStoragePo = InbyteObjectStoragePo.builder()
//                    .objectId(objectId)
//                    .fileName(fileName)
//                    .mimeType(mimeType)
//                    .height(height)
//                    .width(width)
//                    .size(size)
//                    .uploaded(WhetherDict.Yes.code)
//                    .updateTime(LocalDateTime.now())
//                    .build();
//            objectStorageMapper.updateById(inbyteObjectStoragePo);
//
//            log.info("OSS回调处理成功, objectId: {}, fileName: {}", objectId, fileName);
//            return CALLBACK_SUCCESS;
//        } catch (IOException e) {
//            log.error("阿里云OSS回调异常:", e);
//            return CALLBACK_VERIFY_FAILED;
//        }
//    }
//
//
//    /**
//     * 验证上传回调的Request
//     *
//     * @param request
//     * @param ossCallbackBody
//     * @return
//     * @throws NumberFormatException
//     * @throws IOException
//     */
//    protected boolean VerifyOSSCallbackRequest(HttpServletRequest request, String ossCallbackBody)
//            throws NumberFormatException, IOException {
//        // 检查必要的header
//        String autorizationInput = request.getHeader("Authorization");
//        String pubKeyInput = request.getHeader("x-oss-pub-key-url");
//
//
//        try {
//            byte[] authorization = BinaryUtil.fromBase64String(autorizationInput);
//            byte[] pubKey = BinaryUtil.fromBase64String(pubKeyInput);
//            String pubKeyAddr = new String(pubKey, StandardCharsets.UTF_8);
//
//            if (!pubKeyAddr.startsWith("http://gosspublic.alicdn.com/")
//                    && !pubKeyAddr.startsWith("https://gosspublic.alicdn.com/")) {
//                log.warn("pub key addr must be oss address: {}", pubKeyAddr);
//                return false;
//            }
//
//            String retString = executeGet(pubKeyAddr);
//            if (retString == null || retString.isEmpty()) {
//                log.warn("Failed to get public key from: {}", pubKeyAddr);
//                return false;
//            }
//
//            // 清理公钥字符串
//            retString = retString.replace("-----BEGIN PUBLIC KEY-----", "");
//            retString = retString.replace("-----END PUBLIC KEY-----", "");
//            retString = retString.replaceAll("\\s", ""); // 移除所有空白字符
//
//            String queryString = request.getQueryString();
//            String uri = request.getRequestURI();
//            String decodeUri = java.net.URLDecoder.decode(uri, "UTF-8");
//            String authStr = decodeUri;
//            if (queryString != null && !queryString.isEmpty()) {
//                authStr += "?" + queryString;
//            }
//            authStr += "\n" + ossCallbackBody;
//
//            log.debug("验证字符串: {}", authStr);
//            boolean ret = doCheck(authStr, authorization, retString);
//            return ret;
//        } catch (Exception e) {
//            log.error("验证OSS回调请求时发生异常", e);
//            return false;
//        }
//    }
//
//    /**
//     * 验证RSA
//     *
//     * @param content
//     * @param sign
//     * @param publicKey
//     * @return
//     */
//    public static boolean doCheck(String content, byte[] sign, String publicKey) {
//        try {
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            byte[] encodedKey = BinaryUtil.fromBase64String(publicKey);
//            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
//            // 阿里云OSS回调验证使用SHA1withRSA算法，不是MD5withRSA
//            java.security.Signature signature = java.security.Signature.getInstance("SHA1withRSA");
//            signature.initVerify(pubKey);
//            signature.update(content.getBytes(StandardCharsets.UTF_8));
//            boolean bverify = signature.verify(sign);
//            return bverify;
//
//        } catch (Exception e) {
//            log.error("RSA验证失败", e);
//        }
//
//        return false;
//    }
//
//    /**
//     * 获取public key
//     *
//     * @param url
//     * @return
//     */
//    private String executeGet(String url) {
//        BufferedReader in = null;
//
//        String content = null;
//        try {
//            // 定义HttpClient
//            @SuppressWarnings("resource")
//            DefaultHttpClient client = new DefaultHttpClient();
//            // 实例化HTTP方法
//            HttpGet request = new HttpGet();
//            request.setURI(new URI(url));
//            CloseableHttpResponse response = client.execute(request);
//
//            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
//            StringBuffer sb = new StringBuffer("");
//            String line = "";
//            String NL = System.getProperty("line.separator");
//            while ((line = in.readLine()) != null) {
//                sb.append(line + NL);
//            }
//            in.close();
//            content = sb.toString();
//        } catch (Exception e) {
//            log.error("阿里云OSS, GET请求错误", e);
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();// 最后要关闭BufferedReader
//                } catch (Exception e) {
//                    log.error("阿里云OSS, 关闭流", e);
//                }
//            }
//        }
//        return content;
//    }


}
