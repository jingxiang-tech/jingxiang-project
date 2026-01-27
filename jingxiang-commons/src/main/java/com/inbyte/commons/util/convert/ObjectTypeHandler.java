package com.inbyte.commons.util.convert;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mybatis Object 数据转换处理类
 * @author chenjw
 * @date 2023/3/2
 */
public class ObjectTypeHandler extends BaseTypeHandler<Object> {

	/**
	 * Json编码，对象 ==> Json字符串
	 */
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
		String jsonString = JSON.toJSONString(parameter, JSONWriter.Feature.WriteEnumUsingToString);
		if (jdbcType == null) {
			ps.setObject(i, jsonString);
		} else {
			ps.setObject(i, jsonString, jdbcType.TYPE_CODE);
		}
	}

	/**
	 * Json解码，Json字符串 ==> 对象
	 */
	@Override
	public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String result = rs.getString(columnName);
		return result == null ? null : JSON.parseObject(result);
	}

	/**
	 * Json解码，Json字符串 ==> 对象
	 */
	@Override
	public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String result = rs.getString(columnIndex);
		return result == null ? null : JSON.parseObject(result);
	}

	/**
	 * Json解码，Json字符串 ==> 对象
	 */
	@Override
	public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String result = cs.getString(columnIndex);
		return result == null ? null : JSON.parseObject(result);
	}
}
