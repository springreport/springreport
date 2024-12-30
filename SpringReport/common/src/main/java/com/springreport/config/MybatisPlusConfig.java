package com.springreport.config;

import java.util.Properties;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;

/**  
 * @ClassName: MybatisPlusConfig
 * @Description: mybatis-plus配置
 * @author caiyang
 * @date 2020-05-29 10:31:35 
*/  
@EnableTransactionManagement
@Configuration
@MapperScan("com.springreport.mapper.*")
public class MybatisPlusConfig {
	
    /** 
    * @Title: paginationInterceptor 
    * @Description: 分页插件
    * @param @return 设定文件 
    * @return PaginationInterceptor 返回类型 
    * @throws 
    */ 
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        return new MybatisPlusInterceptor();
    }
    
    
    /**  
     * @Title: globalConfig
     * @Description: 自动填充功能
     * @return
     * @author caiyang
     * @date 2020-05-29 10:31:51 
     */ 
    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(new MetaHandler());
        return globalConfig;
    }
    
    @Bean
    ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return new ConfigurationCustomizer() {
			@Override
			public void customize(MybatisConfiguration configuration) {
//				configuration.addInterceptor(new QueryInterceptor());
				configuration.setObjectWrapperFactory(new MapWrapperFactory());
			}
        };
    }
    
    @Bean
	 public DatabaseIdProvider getDatabaseIdProvider() {
	        DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
	        Properties p = new Properties();
	        p.setProperty("MySQL", "mysql");
//	        p.setProperty("PostgreSQL", "postgresql");
//	        p.setProperty("DM DBMS", "dm8");
//	        p.setProperty("Oracle", "oracle");
//	        p.setProperty("SQL Server", "sqlserver");
//	        p.setProperty("Kingbase", "kingbase8");
	        databaseIdProvider.setProperties(p);
	        return databaseIdProvider;
	 }
}
