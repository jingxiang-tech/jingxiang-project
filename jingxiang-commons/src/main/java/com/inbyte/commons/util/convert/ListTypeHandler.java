package com.inbyte.commons.util.convert;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mybatis List 数据转换处理类
 *
 * @author chenjw
 * @date 2023/3/2
 */
public class ListTypeHandler extends BaseTypeHandler<List> {

    public static final List EMPTY_JSON_ARRAY = new ArrayList();

    /**
     * Json编码，对象 ==> Json字符串
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List parameter, JdbcType jdbcType) throws SQLException {
//		String value = parameter.toString();
//		if (jdbcType == null) {
//			ps.setObject(i, value);
//		} else {
//			ps.setObject(i, value, jdbcType.TYPE_CODE);
//		}
        ps.setObject(i, JSON.toJSONString(parameter, JSONWriter.Feature.WriteEnumUsingToString));
    }

    /**
     * Json解码，Json字符串 ==> 对象
     */
    @Override
    public List getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        return result == null ? EMPTY_JSON_ARRAY : JSON.parseArray(result);
    }

    /**
     * Json解码，Json字符串 ==> 对象
     */
    @Override
    public List getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);
        return result == null ? EMPTY_JSON_ARRAY : JSON.parseArray(result);
    }

    /**
     * Json解码，Json字符串 ==> 对象
     */
    @Override
    public List getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String result = cs.getString(columnIndex);
        return result == null ? EMPTY_JSON_ARRAY : JSON.parseArray(result);
    }
}
