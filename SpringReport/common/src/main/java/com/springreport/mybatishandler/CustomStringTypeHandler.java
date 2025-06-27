package com.springreport.mybatishandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.StringTypeHandler;

import com.springreport.util.StringUtil;

/**  
 * @ClassName: CustomStringTypeHandler
 * @Description: 自定义handler
 * @author caiyang
 * @date 2020-08-25 06:44:32 
*/  
public class CustomStringTypeHandler extends StringTypeHandler{

	@Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
        throws SQLException {
      ps.setString(i, parameter);
    }
}
