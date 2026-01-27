package com.inbyte.commons.util;

import com.inbyte.commons.model.dto.R;

import jakarta.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ip地址解析工具
 *
 * @author chenjw
 * @date 2018年8月7日
 */
public class IpUtil {

    private static final String UNKNOWN_STR = "unknown";
    private static final String IPV_4_PREFIX_V_1 = "000000000000000000000000";
    private static final String IPV_4_PREFIX_V_2 = "00000000000000000000ffff";
    private static final String LOCAL_IP_V4 = "127.0.0.1";
    private static final String LOCAL_IP_V6 = "0:0:0:0:0:0:0:1";

    /**
     * 最大IP位
     */
    private static int Max_Ip_Segment = 255;

    /**
     * IP匹配Pattern
     */
    private static final Pattern Ip_Pattern = Pattern.compile("^.*?\\.(\\d+)$");

    /**
     * 获取广域网IP
     *
     * @return
     */
    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public static String getWanIpAddress() {
        HttpServletRequest httpServletRequest = WebUtil.getCurrentRequest();
        String ipAddress = httpServletRequest.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN_STR.equalsIgnoreCase(ipAddress)) {
            ipAddress = httpServletRequest.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN_STR.equalsIgnoreCase(ipAddress)) {
            ipAddress = httpServletRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN_STR.equalsIgnoreCase(ipAddress)) {
            ipAddress = httpServletRequest.getRemoteAddr();
            if (LOCAL_IP_V4.equals(ipAddress) || LOCAL_IP_V6.equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况, 第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * 获取局域网IP
     *
     * @return
     */
    public static String getLanIpAddress() {
        return WebUtil.getCurrentRequest().getRemoteHost();
    }
    /**
     * 16进制IPv4和V6转可读字符串
     * @param hexIp
     * @return
     */
//    public static String hexIpToHumanRead(String hexIp) {
//        if (hexIp != null && hexIp.isEmpty()) {
//            return hexIp;
//        }
//        if (hexIp.length() != 32) {
//            return hexIp;
//        }
//        if (hexIp.startsWith(Ipv4_Prefix_V1) || hexIp.startsWith(Ipv4_Prefix_V2)) {
//            long lon = Long.parseLong(hexIp.substring(24), 16);
//
//            int[] b = new int[4];
//            b[0] = (int) ((lon >> 24) & 0xff);
//            b[1] = (int) ((lon >> 16) & 0xff);
//            b[2] = (int) ((lon >> 8) & 0xff);
//            b[3] = (int) (lon & 0xff);
//
//            return b[0] + "." + b[1] + "." + b[2]+ "." + b[3];
//        } else {
//            return hexIp.replaceAll(".{2}", ":$0").substring(1);
//        }
//    }

//    /**
//     * 16进制IPv4和V6转可读字符串
//     *
//     * @param hexIp
//     * @return
//     */
//    @SuppressWarnings("AlibabaUndefineMagicConstant")
//    public static String hexIpToHumanRead(String hexIp) {
//        if (StringUtils.hasLength(hexIp)) {
//            return hexIp;
//        }
//        if (hexIp.length() != 32) {
//            return hexIp;
//        }
//        if (hexIp.startsWith(IPV_4_PREFIX_V_1) || hexIp.startsWith(IPV_4_PREFIX_V_2)) {
//            long lon = Long.parseLong(hexIp.substring(24), 16);
//
//            int[] b = new int[4];
//            b[0] = (int) ((lon >> 24) & 0xff);
//            b[1] = (int) ((lon >> 16) & 0xff);
//            b[2] = (int) ((lon >> 8) & 0xff);
//            b[3] = (int) (lon & 0xff);
//
//            return b[0] + "." + b[1] + "." + b[2] + "." + b[3];
//        } else {
//            return hexIp.replaceAll(".{2}", ":$0").substring(1);
//        }
//    }

    /**
     * 获取最后一个IP
     *
     * @param ip
     * @param num
     * @return
     */
    public static R lastIp(String ip, int num) {
        String[] split = ip.split("\\.");
        boolean legalIP = Integer.valueOf(split[0]) > 0 && Integer.valueOf(split[0]) <= 255
                && Integer.valueOf(split[1]) >= 0 && Integer.valueOf(split[1]) <= 255
                && Integer.valueOf(split[2]) >= 0 && Integer.valueOf(split[2]) <= 255
                && Integer.valueOf(split[3]) > 0 && Integer.valueOf(split[3]) <= 255;
        if (!legalIP) {
            return R.fail("IP格式不正确");
        }

        Matcher matcher = Ip_Pattern.matcher(ip);
        matcher.find();
        int ipEndSegment = Integer.parseInt(matcher.group(1));
        int ipEnd = ipEndSegment + num - 1;
        if (ipEnd > Max_Ip_Segment) {
            return R.fail("Ip尾端超过255, 请重新选择起始IP");
        }

        int i = ip.lastIndexOf(".");
        String virtualIpEnd = ip.substring(0, i + 1) + ipEnd;
        return R.ok("请求成功", virtualIpEnd);
    }
}
