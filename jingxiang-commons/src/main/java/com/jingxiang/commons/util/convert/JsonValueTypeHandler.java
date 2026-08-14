package com.jingxiang.commons.util.convert;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MyBatis JSON 值转换：兼容对象 {@code {...}} 与数组 {@code [...]}。
 */
public class JsonValueTypeHandler extends BaseTypeHandler<Object> {

    /**
     * Json 编码，对象 ==> Json 字符串
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType)
            throws SQLException {
        String jsonString = JSON.toJSONString(parameter, JSONWriter.Feature.WriteEnumUsingToString);
        if (jdbcType == null) {
            ps.setObject(i, jsonString);
        } else {
            ps.setObject(i, jsonString, jdbcType.TYPE_CODE);
        }
    }

    /**
     * Json 解码，Json 字符串 ==> 对象或数组
     */
    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJson(rs.getString(columnName));
    }

    /**
     * Json 解码，Json 字符串 ==> 对象或数组
     */
    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJson(rs.getString(columnIndex));
    }

    /**
     * Json 解码，Json 字符串 ==> 对象或数组
     */
    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJson(cs.getString(columnIndex));
    }

    private static Object parseJson(String result) {
        if (result == null || result.isBlank()) {
            return null;
        }
        return JSON.parse(result);
    }
}
