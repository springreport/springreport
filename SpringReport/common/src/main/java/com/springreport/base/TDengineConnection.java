package com.springreport.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.Data;

@Data
public class TDengineConnection {
	// 用户名
	private String username;
	// 密码
	private String password;
	// 连接地址
	private String jdbcUrl;
	/**  
	 * @Fields connection : 数据库连接
	 * @author caiyang
	 * @date 2023-05-16 07:51:11 
	 */  
	private Connection connection;
	
	public TDengineConnection(String url,String username,String password) throws Exception {
		this.jdbcUrl = url;
		this.username = username;
		this.password = password;
		buildConnection();
	}
	
	public void buildConnection() throws Exception {
		if(connection == null || connection.isClosed())
		{
			 Class.forName("com.taosdata.jdbc.TSDBDriver");
			 Connection conn = DriverManager.getConnection(this.jdbcUrl,this.username,this.password);
			 this.connection = conn;
		}
	}
}
