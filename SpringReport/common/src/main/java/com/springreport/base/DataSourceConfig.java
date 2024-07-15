package com.springreport.base;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class DataSourceConfig {

	private final Long id;
    private final String driverClass;
    private final String jdbcUrl;
    private final String user;
    private final String password;
    private final Map<String, Object> options;

    public DataSourceConfig(final Long id, final String driverClass, final String jdbcUrl, final String user,
                            final String password,
                            final String queryerClass, final String dbPoolClass) {
        this(id, driverClass, jdbcUrl, user, password, new HashMap<>(3));
    }

    public DataSourceConfig(final Long id, final String driverClass, final String jdbcUrl, final String user,
                            final String password,
                            final Map<String, Object> options) {
        this.id = id;
        this.driverClass = driverClass;
        this.jdbcUrl = jdbcUrl;
        this.user = user;
        this.password = password;
        this.options = options;
    }
}
