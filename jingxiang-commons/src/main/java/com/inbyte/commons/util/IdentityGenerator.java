package com.inbyte.commons.util;

import com.inbyte.commons.model.enums.OrderTypeEnum;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class IdentityGenerator {

    public static AtomicInteger ORDER_SERIAL_NO = new AtomicInteger();
    public static AtomicInteger Verify_Code_SERIALNO = new AtomicInteger();

    private static final Map<String, String> MAP = new HashMap<>();

    /**
     * 邀请码生成器, 算法原理：<br/>
     * 1) 获取id: 1127738 <br/>
     * 2) 使用自定义进制转为：gpm6 <br/>
     * 3) 转为字符串, 并在后面加'o'字符：gpm6o <br/>
     * 4）在后面随机产生若干个随机数字字符：gpm6o7 <br/>
     * 转为自定义进制后就不会出现o这个字符, 然后在后面加个'o', 这样就能确定唯一性。最后在后面产生一些随机字符进行补全。<br/>
     *
     */

    /**
     * 自定义进制(0,1没有加入,容易与o,l混淆)
     */
    private static final char[] r = new char[]{'q', 'w', 'e', '8', 'a', 's', '2', 'd', 'z', 'x', '9', 'c', '7', 'p', '5', 'i', 'k', '3', 'm', 'j', 'u', 'f', 'r', '4', 'v', 'y', 'l', 't', 'n', '6', 'b', 'g', 'h'};

    /**
     * (不能与自定义进制有重复)
     */
    private static final char b = 'o';

    /**
     * 进制长度
     */
    private static final int binLen = r.length;

    /**
     * 序列最小长度
     */
    private static final int s = 6;

    static {
        MAP.put("00", "0");
        MAP.put("01", "1");
        MAP.put("02", "2");
        MAP.put("03", "3");
        MAP.put("04", "4");
        MAP.put("05", "5");
        MAP.put("06", "6");
        MAP.put("07", "7");
        MAP.put("08", "8");
        MAP.put("09", "9");
        MAP.put("10", "A");
        MAP.put("11", "B");
        MAP.put("12", "C");
        MAP.put("13", "D");
        MAP.put("14", "E");
        MAP.put("15", "F");
        MAP.put("16", "G");
        MAP.put("17", "H");
        MAP.put("18", "I");
        MAP.put("19", "G");
        MAP.put("20", "K");
        MAP.put("21", "L");
        MAP.put("22", "M");
        MAP.put("23", "N");
        MAP.put("24", "O");
        MAP.put("25", "P");
        MAP.put("26", "Q");
        MAP.put("27", "R");
        MAP.put("28", "S");
        MAP.put("29", "T");
        MAP.put("30", "U");
        MAP.put("31", "V");
        MAP.put("32", "W");
        MAP.put("33", "X");
        MAP.put("34", "Y");
        MAP.put("35", "Z");
        MAP.put("36", "a");
        MAP.put("37", "b");
        MAP.put("38", "c");
        MAP.put("39", "d");
        MAP.put("40", "e");
        MAP.put("41", "f");
        MAP.put("42", "g");
        MAP.put("43", "h");
        MAP.put("44", "i");
        MAP.put("45", "g");
        MAP.put("46", "k");
        MAP.put("47", "l");
        MAP.put("48", "m");
        MAP.put("49", "n");
        MAP.put("50", "o");
        MAP.put("51", "p");
        MAP.put("52", "q");
        MAP.put("53", "r");
        MAP.put("54", "s");
        MAP.put("55", "t");
        MAP.put("56", "u");
        MAP.put("57", "v");
        MAP.put("58", "w");
        MAP.put("59", "x");
        MAP.put("60", "y");
        MAP.put("61", "z");
    }

    /**
     * 根据ID生成六位随机码
     *
     * @param id ID
     * @return 随机码
     */
    public static String toSerialCode(long id) {
        char[] buf = new char[32];
        int charPos = 32;

        while ((id / binLen) > 0) {
            int ind = (int) (id % binLen);
            // System.out.println(num + "-->" + ind);
            buf[--charPos] = r[ind];
            id /= binLen;
        }
        buf[--charPos] = r[(int) (id % binLen)];
        // System.out.println(num + "-->" + num % binLen);
        String str = new String(buf, charPos, (32 - charPos));
        // 不够长度的自动随机补全
        if (str.length() < s) {
            StringBuilder sb = new StringBuilder();
            sb.append(b);
            Random rnd = new Random();
            for (int i = 1; i < s - str.length(); i++) {
                sb.append(r[rnd.nextInt(binLen)]);
            }
            str += sb.toString();
        }
        return str;
    }

    /**
     * 生成订单编号
     *
     * @return
     */
    public static String generateOrderNo(OrderTypeEnum orderTypeCode) {
        int serialNo = ORDER_SERIAL_NO.incrementAndGet();
        if (serialNo == 999) {
            ORDER_SERIAL_NO.set(0);
        }

        Date date = new Date();
        String yy = new SimpleDateFormat("yy").format(date);
        String mm = new SimpleDateFormat("MM").format(date);
        String dd = new SimpleDateFormat("dd").format(date);
        String hh = new SimpleDateFormat("HH").format(date);
        String minute = new SimpleDateFormat("mm").format(date);
        String ss = new SimpleDateFormat("ss").format(date);
        String sss = new SimpleDateFormat("SSS").format(date);

        List<String> list = new ArrayList<>();
        list.add(yy);
        list.add(mm);
        list.add(dd);
        list.add(hh);
        list.add(minute);
        list.add(ss);
        list.add(sss);
        list.add(StringUtil.leftPad(String.valueOf(serialNo), 3, "0"));

        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        // 前三位订单列表编码
        sb.append(orderTypeCode.code);
        list.forEach(sb::append);
        return sb.toString();
    }

    /**
     * 生成退款编号
     *
     * @return
     */
    public static String generateRefundNo() {
        int serialNo = ORDER_SERIAL_NO.incrementAndGet();
        if (serialNo == 999) {
            ORDER_SERIAL_NO.set(0);
        }

        Date date = new Date();
        String yy = new SimpleDateFormat("yy").format(date);
        String mm = new SimpleDateFormat("MM").format(date);
        String dd = new SimpleDateFormat("dd").format(date);
        String hh = new SimpleDateFormat("HH").format(date);
        String minute = new SimpleDateFormat("mm").format(date);
        String ss = new SimpleDateFormat("ss").format(date);
        String sss = new SimpleDateFormat("SSS").format(date);

        List<String> list = new ArrayList<>();
        list.add(yy);
        list.add(mm);
        list.add(dd);
        list.add(hh);
        list.add(minute);
        list.add(ss);
        list.add(sss);
        list.add(StringUtil.leftPad(String.valueOf(serialNo), 3, "0"));

        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        sb.append("RFD");
        // 前二位订单列表编码
//        sb.append(orderTypeCode);
        list.forEach(sb::append);
        return sb.toString();
    }

//    /**
//     * 生成二维码值
//     *
//     * @return
//     */
//    public static String generateVerifyCode(int verifyCodeTypeDictCode) {
//        int verifySerialNo = Verify_Code_SERIALNO.incrementAndGet();
//        if (verifySerialNo == 999) {
//            Verify_Code_SERIALNO.set(0);
//        }
//        Date date = new Date();
//        String yy = new SimpleDateFormat("yy").format(date);
//        String mm = new SimpleDateFormat("MM").format(date);
//        String dd = new SimpleDateFormat("dd").format(date);
//        String hh = new SimpleDateFormat("HH").format(date);
//        String minute = new SimpleDateFormat("mm").format(date);
//        String ss = new SimpleDateFormat("ss").format(date);
//        String sss = new SimpleDateFormat("SSS").format(date);
//
//        List<String> list = new ArrayList<>();
//        list.add(yy);
//        list.add(mm);
//        list.add(dd);
//        list.add(hh);
//        list.add(minute);
//        list.add(ss);
//        list.add(sss);
//        list.add(StringUtil.leftPad(String.valueOf(verifySerialNo), 3, "0"));
//        Collections.shuffle(list);
//
//        StringBuilder sb = new StringBuilder();
//        sb.append(verifyCodeTypeDictCode);
//        list.forEach(sb::append);
//        // 前二位订单列表编码
//        return sb.toString();
//    }

    /**
     * 生成指定长度数字验证码
     *
     * @return
     */
    public static String generateRandomDigitalCode(int length) {
        StringBuilder validateCode = new StringBuilder();
        for (int i = 0; i < length; i++) {
            validateCode.append((int) (Math.random() * 10));
        }
        return validateCode.toString();
    }

    /**
     * 生成 6 位数字验证码
     *
     * @return
     */
    public static String generateRandomDigitalCode() {
        return generateRandomDigitalCode(6);
    }

    /**
     * 生成订单编号
     * @return
     */
    public static String generatePaymentNo(int paymentTypeDictCode) {
        int serialNo = ORDER_SERIAL_NO.incrementAndGet();
        if (serialNo == 999) {
            ORDER_SERIAL_NO.set(0);
        }

        Date date = new Date();
        String yy = new SimpleDateFormat("yy").format(date);
        String mm = new SimpleDateFormat("MM").format(date);
        String dd = new SimpleDateFormat("dd").format(date);
        String hh = new SimpleDateFormat("HH").format(date);
        String minute = new SimpleDateFormat("mm").format(date);
        String ss = new SimpleDateFormat("ss").format(date);
        String sss = new SimpleDateFormat("SSS").format(date);

        List<String> list = new ArrayList<>();
        list.add(yy);
        list.add(mm);
        list.add(dd);
        list.add(hh);
        list.add(minute);
        list.add(ss);
        list.add(sss);
        list.add(StringUtil.leftPad(String.valueOf(serialNo), 3, "0"));

        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        // 前二位订单列表编码
        sb.append(paymentTypeDictCode);
        list.forEach(sb::append);
        return sb.toString();
    }

    private static String get(String key) {
        String val = MAP.get(key);
        if (val == null) {
            val = key;
        }
        return val;
    }

    public static void main(String[] args) {
//        System.out.println(generateOrderNo("1"));
    }
}
