package com.inbyte.commons.util;

import com.alibaba.fastjson2.JSONObject;

import java.math.BigDecimal;
import java.util.Iterator;

/**
 * 字符串工具
 * @author chenjw
 * @date 2016-6-6
 */
public class StringUtil {


    public static String leftPad(String str, int size) {
        return leftPad(str, size, ' ');
    }

    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        } else {
            int pads = size - str.length();
            if (pads <= 0) {
                return str;
            } else {
                return pads > 8192 ? leftPad(str, size, String.valueOf(padChar)) : repeat(padChar, pads).concat(str);
            }
        }
    }

    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        } else {
            if (isEmpty(padStr)) {
                padStr = " ";
            }

            int padLen = padStr.length();
            int strLen = str.length();
            int pads = size - strLen;
            if (pads <= 0) {
                return str;
            } else if (padLen == 1 && pads <= 8192) {
                return leftPad(str, size, padStr.charAt(0));
            } else if (pads == padLen) {
                return padStr.concat(str);
            } else if (pads < padLen) {
                return padStr.substring(0, pads).concat(str);
            } else {
                char[] padding = new char[pads];
                char[] padChars = padStr.toCharArray();

                for (int i = 0; i < pads; ++i) {
                    padding[i] = padChars[i % padLen];
                }

                return (new String(padding)).concat(str);
            }
        }
    }

    /**
     * 是否空字符串或 null
     *
     * @param cs
     * @return
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 非空字符串
     *
     * @param cs
     * @return
     */
    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static String repeat(char ch, int repeat) {
        if (repeat <= 0) {
            return "";
        } else {
            char[] buf = new char[repeat];

            for (int i = repeat - 1; i >= 0; --i) {
                buf[i] = ch;
            }

            return new String(buf);
        }
    }

    public static String repeat(String str, int repeat) {
        if (str == null) {
            return null;
        } else if (repeat <= 0) {
            return "";
        } else {
            int inputLength = str.length();
            if (repeat != 1 && inputLength != 0) {
                if (inputLength == 1 && repeat <= 8192) {
                    return repeat(str.charAt(0), repeat);
                } else {
                    int outputLength = inputLength * repeat;
                    switch (inputLength) {
                        case 1:
                            return repeat(str.charAt(0), repeat);
                        case 2:
                            char ch0 = str.charAt(0);
                            char ch1 = str.charAt(1);
                            char[] output2 = new char[outputLength];

                            for (int i = repeat * 2 - 2; i >= 0; --i) {
                                output2[i] = ch0;
                                output2[i + 1] = ch1;
                                --i;
                            }

                            return new String(output2);
                        default:
                            StringBuilder buf = new StringBuilder(outputLength);

                            for (int i = 0; i < repeat; ++i) {
                                buf.append(str);
                            }

                            return buf.toString();
                    }
                }
            } else {
                return str;
            }
        }
    }

    public static String repeat(String str, String separator, int repeat) {
        if (str != null && separator != null) {
            String result = repeat(str + separator, repeat);
            return removeEnd(result, separator);
        } else {
            return repeat(str, repeat);
        }
    }


    public static String removeEnd(String str, String remove) {
        if (!isEmpty(str) && !isEmpty(remove)) {
            return str.endsWith(remove) ? str.substring(0, str.length() - remove.length()) : str;
        } else {
            return str;
        }
    }

    /**
     * "&"符号连接的字符串转JSON
     *
     * @return
     */
    public static JSONObject strToJson(String str) {
        return strToJson(str, "&");
    }

    /**
     * "&"符号连接的字符串转JSON
     *
     * @return
     */
    public static JSONObject strToJson(String str, String jointMark) {
        JSONObject json = new JSONObject();
        if (StringUtil.isEmpty(str)) {
            return json;
        }
        String[] arr;
        for (String s : str.split(jointMark)) {
            arr = s.split("=");
            try {
                json.put(arr[0], arr[1]);
            } catch (Exception e) {
                continue;
            }
        }
        return json;
    }

    /**
     * JSON转String “&”拼接
     *
     * @param json
     * @return
     */
    public static String jsonToStr(JSONObject json) {
        if (json == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = json.keySet().iterator();
        String key;
        while (iterator.hasNext()) {
            key = iterator.next();
            sb.append(key).append("=").append(json.getString(key)).append("&");
        }
        if (sb.length() > 0) {
            return sb.deleteCharAt(sb.length() - 1).toString();
        }
        return "";
    }

    /**
     * JSON转String 符号拼接
     *
     * @param json
     * @return
     */
    public static String jsonToStr(JSONObject json, String jointMark) {
        if (json == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = json.keySet().iterator();
        String key;
        while (iterator.hasNext()) {
            key = iterator.next();
            sb.append(jointMark).append(key).append(jointMark).append(json.getString(key));
        }
        return sb.toString();
    }

    public static String defaultIfEmpty(String str, String defaultValue) {
        return str == null || "".equals(str) ? defaultValue : str;
    }

    public static String defaultIfEmpty(String str) {
        return defaultIfEmpty(str, "");
    }

    /**
     * 去除最后一个英文逗号
     * @param str
     * @return
     */
    public static String removeLastComma(String str) {
        return removeLastStr(str, ",");
    }

    /**
     * 去除String文末字符
     * @param str
     * @param strToRemove
     * @return
     */
    public static String removeLastStr(String str, String strToRemove) {
        if (str != null && str.endsWith(strToRemove)) {
            return str.substring(0, str.length() - strToRemove.length());
        }
        return str;
    }


    /**
     * 序列化BigDecimal
     *
     * 小数后为0，返回整数
     * @param number
     * @return
     */
    public static String formatBigDecimal(BigDecimal number) {
        if (number == null) {
            return "0";
        }
        // 去掉末尾的0并去掉小数点（如果是整数）
        number = number.stripTrailingZeros();

        // 判断是否是整数
        if (number.scale() <= 0) {
            return number.toPlainString(); // 整数部分
        } else {
            return number.toString(); // 有小数部分，保留小数
        }
    }

    public static boolean isNotBlankOrEmpty(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof String) {
            return ((String) obj).length() > 0;
        }
        if (obj.getClass().isArray()) {
            return java.lang.reflect.Array.getLength(obj) > 0;
        }
        if (obj instanceof java.util.Collection) {
            return !((java.util.Collection<?>) obj).isEmpty();
        }
        if (obj instanceof java.util.Map) {
            return !((java.util.Map<?, ?>) obj).isEmpty();
        }
        return true;
    }

    /**
     * 添加非空字符串
     *
     * @param sb
     * @param prefix
     * @param value
     */
    public static void appendIfNotEmpty(StringBuilder sb, String prefix, String value) {
        if (!StringUtil.isEmpty(value)) {
            sb.append(prefix).append(value).append("\n");
        }
    }
}
