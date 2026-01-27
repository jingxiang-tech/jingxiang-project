package com.inbyte.component.common.basic;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.inbyte.commons.util.SpringContextUtil;
import com.inbyte.component.common.basic.dao.CommonSystemConfigMapper;

import java.math.BigDecimal;

/**
 * 系统配置工具类
 *
 * @author chenjw
 * @date 20240312
 */
public class SystemConfigUtil {

    /**
     * 获取系统配置值
     *
     * @param key
     * @return
     */
    public static String getValue(String key) {
        return SpringContextUtil.getBean(CommonSystemConfigMapper.class).getValue(key);
    }

    /**
     * 获取系统配置值 String
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return getValue(key);
    }

    /**
     * 获取系统配置值 Integer 类型
     *
     * @param key
     * @return
     */
    public static Integer getInteger(String key) {
        return Integer.valueOf(getString(key));
    }

    /**
     * 获取系统配置值 Long 类型
     *
     * @param key
     * @return
     */
    public static Long getLong(String key) {
        return Long.valueOf(getString(key));
    }

    /**
     * 获取系统配置值 Double 类型
     *
     * @param key
     * @return
     */
    public static Double getDouble(String key) {
        return Double.valueOf(getString(key));
    }

    /**
     * 获取系统配置值 BigDecimal 类型
     *
     * @param key
     * @return
     */
    public static BigDecimal getBigDecimal(String key) {
        return new BigDecimal(getString(key));
    }

    /**
     * 获取系统配置值 JSONObject 类型
     *
     * @param key
     * @return
     */
    public static JSONObject getJSONObject(String key) {
        return JSONObject.parseObject(getString(key));
    }

    /**
     * 获取系统配置值 JSONArray 类型
     *
     * @param key
     * @return
     */
    public static JSONArray getJSONArray(String key) {
        return JSONArray.parseArray(getString(key));
    }


}
