package com.springreport.base;

import lombok.Data;

@Data
public class TDengineConfig {

	private Long id;
	// 用户名
	private String username;
	// 密码
	private String password;
	// 连接地址
	private String jdbcUrl;

    public TDengineConfig(final Long id,final String username,final String password,final String jdbcUrl) {
        this.id = id;
        this.username = username;
        this.jdbcUrl = jdbcUrl;
        this.password = password;
    }
}
