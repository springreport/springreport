
package com.springreport.security.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import com.springreport.constants.Constants;
import com.springreport.security.filter.JWTFilter;
import com.springreport.shiro.ShiroMethodInterceptor;
import com.springreport.util.RedisUtil;


/**
 * @ClassName: ShiroConfig
 * @Description: shiro配置
 * @Author 蔡阳
 * @DateTime 2019年10月24日13:45:04
 */
@Configuration
public class ShiroConfig {
	
	@Value("${authentic.enabale}")
    private boolean authenticEnabale;
	
	@Autowired
	private RedisUtil redisUtil;
	
	
	@Bean(name = "shiroFilterFactoryBean")
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) throws Exception {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		if(authenticEnabale)
		{
			// 添加自己的过滤器并且取名为jwt
			Map<String, Filter> filterMap = new HashMap<>();
			filterMap.put(Constants.JWT_STRING, new JWTFilter(redisUtil));
			shiroFilterFactoryBean.setFilters(filterMap);
			shiroFilterFactoryBean.setSecurityManager(securityManager);
//			shiroFilterFactoryBean.setUnauthorizedUrl("/api/common/unnauthorized");//未授权跳转url
			Map<String, String> map = new LinkedHashMap<>();
			map.put("/springReport/api/login/doLogin", Constants.PUBLIC_STRING);      // 允许匿名访问
			map.put("/springReport/api/common/upload", Constants.PUBLIC_STRING);      // 允许匿名访问
			map.put("/springReport/api/screenWebsocket/**", Constants.PUBLIC_STRING);      // 允许匿名访问
			map.put("/images/**", Constants.PUBLIC_STRING);      // 允许匿名访问
			map.put("/springReport/api/common/printTestRespose", Constants.PUBLIC_STRING);      // 允许匿名访问
//			map.put("/springReport/api/screenTpl/getScreenDesign", Constants.PUBLIC_STRING); 
			map.put("/springReport/api/reportTplDataset/getShareReportDatasetsParam", Constants.PUBLIC_STRING); 
			map.put("/springReport/api/reportTpl/previewShareLuckysheetReportData", Constants.PUBLIC_STRING); 
			map.put("/springReport/api/reportTpl/previewShareLuckysheetFormsReportData", Constants.PUBLIC_STRING); 
			map.put("/springReport/api/common/shareUpload", Constants.PUBLIC_STRING); 
			map.put("/springReport/api/reportTpl/shareReportData", Constants.PUBLIC_STRING); 
			map.put("/springReport/api/reportDatasourceDictData/getShareDictDatas", Constants.PUBLIC_STRING); 
			map.put("/springReport/api/luckysheetReportFormsHis/getShareTableList", Constants.PUBLIC_STRING); 
			map.put("/springReport/api/reportTpl/shareLuckySheetExportExcel", Constants.PUBLIC_STRING); 
			map.put("/springReport/api/reportTpl/getShareSheetPdf", Constants.PUBLIC_STRING); 
			map.put("/springReport/api/reportTpl/getShareDetail", Constants.PUBLIC_STRING); 
			map.put("/springReport/api/reportTpl/getMobileShareReport", Constants.PUBLIC_STRING); 
			map.put("/springReport/api/coedit/load", Constants.PUBLIC_STRING); 
			map.put("/springReport/api/coedit/loadsheet", Constants.PUBLIC_STRING); 
			map.put("/springReport/api/coedit/websocket/luckysheet", Constants.PUBLIC_STRING); 
			map.put("/springReport/api/reportTpl/getShareSheetPdfStream", Constants.PUBLIC_STRING); 
			map.put("/springReport/api/login/getMerchantMode", Constants.PUBLIC_STRING); 
			map.put("/springReport/api/docOnlyOffice/callback", Constants.PUBLIC_STRING); 
			
	        map.put("/**", Constants.JWT_STRING); 
//			shiroFilterFactoryBean.setFilterChainDefinitionMap(chainDefinitionSectionMetaSource.getObject());
	        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
			return shiroFilterFactoryBean;
		}else {
			Map<String, Filter> filterMap = new HashMap<>();
			filterMap.put(Constants.JWT_STRING, new JWTFilter(redisUtil));
			shiroFilterFactoryBean.setFilters(filterMap);
			shiroFilterFactoryBean.setSecurityManager(securityManager);
			Map<String, String> map = new LinkedHashMap<>();
	        map.put("/springReport/**", Constants.PUBLIC_STRING); 
//			shiroFilterFactoryBean.setFilterChainDefinitionMap(chainDefinitionSectionMetaSource.getObject());
	        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
			return shiroFilterFactoryBean;
		}
		
	}
	
	@Bean //(name = "securityManager")
	public DefaultWebSecurityManager  getDefaultSecurityManager(@Qualifier("myRelam") MyRealm myRealm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(myRealm);
		return securityManager;
	}
	
	@Bean(name="myRelam")
	public MyRealm getMyRealm() {
		return new MyRealm();
	}
	
	 /**
     * 下面的代码是添加注解支持
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @Primary
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        advisor.setAdvice(new ShiroMethodInterceptor().skipPremissionHandler());
        return advisor;
    }
    
}
