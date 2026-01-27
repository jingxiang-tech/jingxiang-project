package com.inbyte.commons.util;

import com.alibaba.fastjson2.JSON;
import com.inbyte.commons.model.dto.R;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 易思网络
 * Web工具类
 *
 * @author chenjw
 * @date 2017年03月28日
 */
public class WebUtil {

    private static String FORWARD_REQUEST_URI = "jakarta.servlet.forward.request_uri";

    private static final Logger log = LoggerFactory.getLogger(WebUtil.class);

    /**
     * 400错误消息
     */
    private static final String BAD_REQUEST_JSON_STRING = "{\"status\":400,\"msg\":\"%s\"}";
//    public static String getRequestLog(ServletRequest request) {
//        HttpServletRequest req = (HttpServletRequest) request;
//        String method = req.getMethod();
//        if ("post".equals(method)) {
//            return "请求IP：" + IpUtil.getWanIpAddress() +
//                    " 访问地址：" + req.getRequestURI() +
//                    " 请求参数：" + JSON.toJSONString(req.getParameterMap()).replaceAll("\\[|]", "");
//        } else {
//            String queryString = req.getQueryString() == null ? "" : "?" + req.getQueryString();
//            return "请求IP：" + IpUtil.getWanIpAddress() +
//                    " 访问地址：" + req.getRequestURI() + queryString;
//        }
//    }

    //    private static final String GET = "get";
    private static final String ERROR = "error";
    private static final String JSON_DEFAULT = "{}";
    private static final String DEFAULT_VALUE = "-";

//    /**
//     * 每个字段前加1个空格, 用于日志解析
//     */
//    private static final String Client_IP = " clientIp:";
//    private static final String Url = " url:";
//    private static final String Request_Method = " requestMethod:";
//    private static final String Query_String = " queryStr:";
//    private static final String Form_Param = " formParam:";
//    private static final String Request_Body = " requestBody:";


    /**
     * 获取当前线程request
     *
     * @return
     */
    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
    }

    /**
     * 获取当前线程request
     *
     * @return
     */
    public static HttpServletResponse getCurrentResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getResponse();
    }

    /**
     * 获取当前请求指定header值
     *
     * @return
     */
    public static String getHeader(String headerName) {
        return getCurrentRequest().getHeader(headerName);
    }

    /**
     * 获取当前请求指定Attribute值
     *
     * @param attributeName
     * @return
     */

    public static Object getAttribute(String attributeName) {
        return getCurrentRequest().getAttribute(attributeName);
    }

    /**
     * 获取请求URI
     *
     * @return
     */
    public static String getRequestUri() {
        HttpServletRequest request = getCurrentRequest();
        // 获取原生请求地址, 404时候不会getRequestURL不会得到error
        String requestUri = request.getRequestURI();
        if (requestUri.contains(ERROR)) {
            requestUri = request.getAttribute(FORWARD_REQUEST_URI).toString();
        }
        return requestUri;
    }

    /**
     * 获取请求信息-不包括JSON Body
     *
     * @return
     */
    public static String getRequestInfo() {
        HttpServletRequest request = getCurrentRequest();
        StringBuilder sb = new StringBuilder();
        sb.append("url:").append(request.getRequestURL());
        sb.append(" requestMethod:").append(request.getMethod());
        sb.append(" queryString:").append(defaultIfEmpty(request.getQueryString()));
        sb.append(" requestBody:").append(defaultIfEmpty(JSON.toJSONString(request.getParameterMap())));
        return sb.toString();
    }

//    /**
//     * 获取请求信息-包括JSON Body
//     *
//     * @param joinPoint
//     * @return
//     */
//    public static String getRequestInfo(JoinPoint joinPoint) {
//        HttpServletRequest request = getCurrentRequest();
//        StringBuilder httpParam = getRequestInfo();
//        httpParam.append(Request_Body).append(getJSONParam(joinPoint));
//        if (joinPoint == null) {
//            return Json_Default;
//        }
//        boolean nonJsonParam = Get.equals(request.getMethod()) ? true : false;
//        if (nonJsonParam) {
//            return Json_Default;
//        }
//        httpParam.append(Request_Body).append(getJSONParam(joinPoint));
//        return httpParam.toString();
//    }

    /**
     * 获取RequestBody参数
     *
     * @param joinPoint
     * @return
     */
    private static String getJsonParam(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                if (parameterAnnotations[i][j] instanceof RequestBody) {
                    return JSON.toJSONString(joinPoint.getArgs()[i]);
                }
            }
        }
        return JSON_DEFAULT;
    }

    private static String defaultIfEmpty(String str) {
        if (StringUtil.isEmpty(str)) {
            return DEFAULT_VALUE;
        }
        return str;
    }

    /**
     * 通过 HttpServletResponse 输出Result数据结果
     * 响应头 application/json; charset=UTF-8"
     *
     * @param r
     */
    public static void writeResult(R r) {
        writeBody(r.toString());
    }

    /**
     * 通过 HttpServletResponse 输出Result数据结果
     * 响应头 application/json; charset=UTF-8"
     *
     * @param message
     */
    public static void write400Message(String message) {
        writeBody(String.format(BAD_REQUEST_JSON_STRING, message));
    }

    /**
     * 输出消息内容
     * 通过 HttpServletResponse 输出Result数据结果
     * 响应头 application/json; charset=UTF-8"
     *
     * @param responseBody
     */
    public static void writeBody(String responseBody) {
        writeBody(HttpStatus.OK, responseBody);
    }

    /**
     * 输出消息内容
     * 通过 HttpServletResponse 输出Result数据结果
     * 响应头 application/json; charset=UTF-8"
     *
     * @param r
     */
    public static void writeBody(HttpStatusCode httpStatusCode, R r) {
        writeBody(httpStatusCode, JSON.toJSONString(r));
    }

    /**
     * 输出消息内容
     * 通过 HttpServletResponse 输出Result数据结果
     * 响应头 application/json; charset=UTF-8"
     *
     * @param responseBody
     */
    public static void writeBody(HttpStatusCode httpStatusCode, String responseBody) {
        HttpServletResponse response = getCurrentResponse();
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(httpStatusCode.value());
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(responseBody);
        } catch (IOException e) {
            log.error("WebUtil 输出 400 responseBody 数据异常", e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 获取 request body
     */
    public static String getRequestBodyString(HttpServletRequest request) {
        ServletInputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String s;
            //读取回调请求体
            while ((s = bufferedReader.readLine()) != null) {
                stringBuffer.append(s);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            log.error("IO 操作异常", e);
            throw new RuntimeException("IO 流操作异常");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("关闭输入流异常", e);
                    throw new RuntimeException("IO 流操作异常");
                }
            }
        }
    }
}
