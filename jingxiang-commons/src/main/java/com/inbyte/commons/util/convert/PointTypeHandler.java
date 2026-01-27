package com.inbyte.commons.util.convert;

import com.inbyte.commons.model.dto.Point;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * 经维度坐标类型处理器
 *
 */
@MappedTypes({Point.class})
public class PointTypeHandler extends BaseTypeHandler<Point> {

    PointConverter converter = new PointConverter();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Point parameter, JdbcType jdbcType) throws SQLException {
        ps.setBytes(i, converter.to(parameter));
    }

    @Override
    public Point getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return converter.from(rs.getBytes(columnName));
    }

    @Override
    public Point getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return converter.from(rs.getBytes(columnIndex));
    }

    @Override
    public Point getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return converter.from(cs.getBytes(columnIndex));
    }
}