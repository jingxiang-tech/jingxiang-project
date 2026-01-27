package com.inbyte.commons.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期工具
 */
public class DateUtil {

    private static final DateTimeFormatter Date_Pattern_CN = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
    private static final DateTimeFormatter DateTime_Pattern_CN = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分");
    private static final DateTimeFormatter Date_Pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DateTime_Pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * 格式化日期到指定格式
     * 
     * @param localDate
     *            需要格式化的日期
     * @return 返回格式的字符串
     */
    public static String formatCn(LocalDate localDate) {
        return localDate.format(Date_Pattern_CN);
    }

    /**
     * 格式化日期到指定格式
     *
     * @return 返回格式的字符串
     */
    public static String format(LocalDate localDate) {
        return LocalDate.now().format(Date_Pattern);
    }

    /**
     * 格式化日期到指定格式
     *
     * @param localDateTime
     *            需要格式化的日期
     * @return 返回格式的字符串
     */
    public static String formatCn(LocalDateTime localDateTime) {
        return localDateTime.format(DateTime_Pattern_CN);
    }

    /**
     * 格式化日期到指定格式
     *
     * @return 返回格式的字符串
     */
    public static String format(LocalDateTime localDateTime) {
        return localDateTime.format(DateTime_Pattern);
    }

    /**
     * 格式化日期到指定格式
     *
     * @return 返回格式的字符串
     */
    public static String todayCn() {
        return formatCn(LocalDate.now());
    }

    /**
     * 格式化日期到指定格式
     *
     * @return 返回格式的字符串
     */
    public static String today() {
        return format(LocalDate.now());
    }

    /**
     * 格式化日期到指定格式
     *
     * @return 返回格式的字符串
     */
    public static String nowStr() {
        return format(LocalDateTime.now());
    }


    public static void main(String[] args) {
        System.out.println(todayCn());
    }
}
