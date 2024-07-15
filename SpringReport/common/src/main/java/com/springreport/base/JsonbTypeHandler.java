package com.springreport.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**  
 * @ClassName: JsonbTypeHandler
 * @Description: PostgreSql jsonb 数据处理器
 * @author caiyang
 * @date 2023-09-01 01:32:27 
*/ 
@MappedTypes({Object.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class JsonbTypeHandler extends AbstractJsonTypeHandler<Object> {
    private static final PGobject jsonObject = new PGobject();
    private final Class<?> type;

    public JsonbTypeHandler(Class<?> type) {
        this.type = type;
    }

    /**
     * 重写设置参数
     * @param ps
     * @param i
     * @param parameter
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        if (ps != null) {
            jsonObject.setType("jsonb");
            jsonObject.setValue(JSON.toJSONString(parameter));
            ps.setObject(i, jsonObject);
        }
    }

    /**
     * 根据列名，获取可以为空的结果
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object v = rs.getObject(columnName);
        return toFill(v);
    }

    /**
     * 根据列索引，获取可以为空的结果
     * @param rs
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object v = rs.getObject(columnIndex);
        return toFill(v);
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object v = cs.getObject(columnIndex);
        return toFill(v);
    }

    @Override
	public Object parse(String json) {
        return JSON.parseObject(json, this.type);
    }

    /**
     * 必须将 v 转成 PGObject 处理
     * @param v
     * @return
     */
    private Object toFill(Object v) {
        if (v != null && v instanceof PGobject) {
            PGobject p = (PGobject) v;
            String pv = p.getValue();
            if (StringUtils.isNotEmpty(pv) && ("jsonb".equals(p.getType()) || "json".equals(p.getType()))) {
                return parse(p.getValue());
            }
        }
        return v;
    }

    @Override
    public String toJson(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty);
    }
}
