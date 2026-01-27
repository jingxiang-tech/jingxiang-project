package com.inbyte.commons.util;

/**
 * 时间工具类
 *
 * @author chenjw
 * @date 2024年7月26日
 */
public class TimeUtil {

    /**
     * 分钟数格式化
     *
     * @param minutes
     * @return
     */
    public static String format(int minutes) {
        if (minutes == 0) {
            return "0分钟";
        } else if (minutes % 1440 == 0) {
            int days = minutes / 1440;
            return days + "天";
        } else {
            int hours = minutes / 60;
            int remainingMinutes = minutes % 60;

            if (hours == 0) {
                return remainingMinutes + "分钟";
            } else if (remainingMinutes == 0) {
                return hours + "小时";
            } else {
                return hours + "小时" + remainingMinutes + "分钟";
            }
        }
    }

}