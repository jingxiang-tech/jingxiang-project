package com.inbyte.commons.util;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 星期日期转换工具类
 */
public class WeekdayConverter {

    public static String convert(List<Integer> weekdays) {
        List<String> readableWeekdays = weekdays.stream()
                .map(WeekdayConverter::convertToWeekday)
                .collect(Collectors.toList());
        return String.join("、", readableWeekdays);
    }

    public static String convert(Integer weekday) {
        return convertToWeekday(weekday);
    }

    public static String convert(LocalDate localDate) {
        return convert(localDate.getDayOfWeek().getValue());
    }

    private static String convertToWeekday(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1: return "周一";
            case 2: return "周二";
            case 3: return "周三";
            case 4: return "周四";
            case 5: return "周五";
            case 6: return "周六";
            case 7: return "周日";
            default: return "未知";
        }
    }

}