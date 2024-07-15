package com.springreport.base;

import lombok.Data;

@Data
public class InfluxDbDataSourceConfig {

	private Long id;
	// 用户名
	private String username;
	// 密码
	private String password;
	// 连接地址
	private String openurl;
	// 数据库
	private String database;
	// 保留策略
	private String retentionPolicy;

    public InfluxDbDataSourceConfig(final Long id,final String username,
                            final String password,final String openurl, 
                            final String database) {
        this.id = id;
        this.username = username;
        this.openurl = openurl;
        this.password = password;
        this.database = database;
    }
}
