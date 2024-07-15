package com.springreport.base;

import lombok.Data;

@Data
public class EsDataSourceConfig {

	private Long id;
	// 用户名
	private String username;
	// 密码
	private String password;
	// 连接地址
	private String url;

    public EsDataSourceConfig(final Long id,final String username,
                            final String password,final String url) 
    {
        this.id = id;
        this.username = username;
        this.url = url;
    }
}
