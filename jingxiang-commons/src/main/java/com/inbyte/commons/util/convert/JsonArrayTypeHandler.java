package com.inbyte.commons.util.convert;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONWriter;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mybatis JSONArray 数据转换处理类
 * @author chenjw
 * @date 2023/3/2
 */
public class JsonArrayTypeHandler extends BaseTypeHandler<JSONArray> {

	public static final JSONArray EMPTY_JSON_ARRAY = new JSONArray();

	/**
	 * Json编码，对象 ==> Json字符串
	 */
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, JSONArray parameter, JdbcType jdbcType) throws SQLException {
		String value = parameter.toJSONString(JSONWriter.Feature.WriteEnumUsingToString);
		if (jdbcType == null) {
			ps.setObject(i, value);
		} else {
			ps.setObject(i, value, jdbcType.TYPE_CODE);
		}
	}

	/**
	 * Json解码，Json字符串 ==> 对象
	 */
	@Override
	public JSONArray getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String result = rs.getString(columnName);
		return result == null ? EMPTY_JSON_ARRAY : JSON.parseArray(result);
	}

	/**
	 * Json解码，Json字符串 ==> 对象
	 */
	@Override
	public JSONArray getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String result = rs.getString(columnIndex);
		return result == null ? EMPTY_JSON_ARRAY : JSON.parseArray(result);
	}

	/**
	 * Json解码，Json字符串 ==> 对象
	 */
	@Override
	public JSONArray getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String result = cs.getString(columnIndex);
		return result == null ? EMPTY_JSON_ARRAY : JSON.parseArray(result);
	}
}
