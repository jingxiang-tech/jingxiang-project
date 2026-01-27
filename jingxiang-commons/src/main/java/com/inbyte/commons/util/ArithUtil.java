package com.inbyte.commons.util;

import com.inbyte.commons.exception.InbyteException;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 四则运算工具类
 * 易思网络
 *
 * 提供基本的四则运算和比较操作。
 *
 * @author chenjw
 * @date 2016年06月30日
 */
public class ArithUtil {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * 将 null 转换为默认值 BigDecimal.ZERO
     *
     * @param value 数值
     * @return BigDecimal 值
     */
    private static BigDecimal nullToZero(Number value) {
        return value == null ? BigDecimal.ZERO : new BigDecimal(value.toString());
    }

    /**
     * 加法运算，支持多种数值类型
     *
     * @param numbers 可变参数的数值
     * @return 和
     */
    public static BigDecimal add(Number... numbers) {
        BigDecimal result = BigDecimal.ZERO;
        for (Number number : numbers) {
            result = result.add(nullToZero(number));
        }
        return result.setScale(SCALE, ROUNDING_MODE);
    }

    public static Integer add(Integer addend, Integer summand) {
        return nullToZero(addend).intValue() + nullToZero(summand).intValue();
    }

    /**
     * 减法运算，支持多种数值类型
     *
     * @param minuend 被减数
     * @param subtrahend 可变参数的减数
     * @return 差
     */
    public static BigDecimal subtract(Number minuend, Number... subtrahend) {
        BigDecimal result = nullToZero(minuend);
        for (Number number : subtrahend) {
            result = result.subtract(nullToZero(number));
        }
        return result.setScale(SCALE, ROUNDING_MODE);
    }

    /**
     * 乘法运算，支持多种数值类型
     *
     * @param numbers 可变参数的数值
     * @return 积
     */
    public static BigDecimal multiply(Number... numbers) {
        BigDecimal result = BigDecimal.ONE;
        for (Number number : numbers) {
            result = result.multiply(nullToZero(number));
        }
        return result.setScale(SCALE, ROUNDING_MODE);
    }

    /**
     * 除法运算，支持多种数值类型
     *
     * @param dividend 被除数
     * @param divisor 除数
     * @return 商
     * @throws InbyteException 如果除数为 null 或零
     */
    public static BigDecimal divide(Number dividend, Number divisor) {
        BigDecimal divisorBD = nullToZero(divisor);
        if (divisorBD.equals(BigDecimal.ZERO)) {
            throw InbyteException.error("divisor can not be null or zero");
        }
        return nullToZero(dividend).divide(divisorBD, SCALE, ROUNDING_MODE);
    }

    /**
     * 判断第一个 BigDecimal 是否大于第二个 BigDecimal。
     *
     * @param num1 要比较的第一个 BigDecimal
     * @param num2 要比较的第二个 BigDecimal
     * @return 如果 num1 大于 num2，则返回 true，否则返回 false
     */
    public static boolean isGreaterThan(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) > 0;
    }

    /**
     * 判断第一个 BigDecimal 是否大于零
     *
     * @param num1 要比较的第一个 BigDecimal
     * @return 如果 num1 大于 num2，则返回 true，否则返回 false
     */
    public static boolean isGreaterThanZero(BigDecimal num1) {
        return num1 != null && num1.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 判断第一个 BigDecimal 是否小于第二个 BigDecimal。
     *
     * @param num1 要比较的第一个 BigDecimal
     * @param num2 要比较的第二个 BigDecimal
     * @return 如果 num1 小于 num2，则返回 true，否则返回 false
     */
    public static boolean isLessThan(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) < 0;
    }

    /**
     * 判断两个 BigDecimal 是否相等。
     *
     * @param num1 要比较的第一个 BigDecimal
     * @param num2 要比较的第二个 BigDecimal
     * @return 如果 num1 等于 num2，则返回 true，否则返回 false
     */
    public static boolean isEqual(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) == 0;
    }
    public static boolean isZero(BigDecimal num1) {
        return num1 == null || BigDecimal.ZERO.compareTo(num1) == 0;
    }
    public static boolean isNullOrZero(BigDecimal num) {
        return num == null || BigDecimal.ZERO.compareTo(num) == 0;
    }

    /**
     * 判断第一个 BigDecimal 是否大于或等于第二个 BigDecimal。
     *
     * @param num1 要比较的第一个 BigDecimal
     * @param num2 要比较的第二个 BigDecimal
     * @return 如果 num1 大于或等于 num2，则返回 true，否则返回 false
     */
    public static boolean isGreaterThanOrEqualTo(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) >= 0;
    }

    /**
     * 判断第一个 BigDecimal 是否小于或等于第二个 BigDecimal。
     *
     * @param num1 要比较的第一个 BigDecimal
     * @param num2 要比较的第二个 BigDecimal
     * @return 如果 num1 小于或等于 num2，则返回 true，否则返回 false
     */
    public static boolean isLessThanOrEqualTo(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) <= 0;
    }
}
