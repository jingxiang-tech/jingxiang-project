package com.jingxiang.commons.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

/**
 * JSON 字段名 snake_case → camelCase 转换（用于第三方响应代理归一化）。
 */
public final class JsonKeyNamingUtil {

    private JsonKeyNamingUtil() {
    }

    public static JSONObject snakeKeysToCamel(JSONObject source) {
        if (source == null) {
            return null;
        }
        JSONObject target = new JSONObject(source.size());
        for (String key : source.keySet()) {
            target.put(snakeToCamel(key), convertValue(source.get(key)));
        }
        return target;
    }

    private static Object convertValue(Object value) {
        if (value instanceof JSONObject jsonObject) {
            return snakeKeysToCamel(jsonObject);
        }
        if (value instanceof JSONArray jsonArray) {
            JSONArray out = new JSONArray(jsonArray.size());
            for (Object item : jsonArray) {
                out.add(convertValue(item));
            }
            return out;
        }
        return value;
    }

    private static String snakeToCamel(String key) {
        if (key == null || !key.contains("_")) {
            return key;
        }
        StringBuilder sb = new StringBuilder(key.length());
        boolean upperNext = false;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (c == '_') {
                upperNext = true;
            } else if (upperNext) {
                sb.append(Character.toUpperCase(c));
                upperNext = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
