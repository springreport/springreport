//package com.springreport.config;
//
//import java.util.List;
//
//import javax.annotation.PostConstruct;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.context.annotation.Configuration;
//
//import com.springreport.interceptor.QueryInterceptor;
//import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
//
//
//@Configuration
//@AutoConfigureAfter(PageHelperAutoConfiguration.class)
//public class QueryHelperAutoConfiguration {
//
//	@Autowired
//    private List<SqlSessionFactory> sqlSessionFactoryList;
//	
//	@PostConstruct
//	 public void addPageInterceptor() {
//	    QueryInterceptor queryInterceptor = new QueryInterceptor();
//	    for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
////            sqlSessionFactory.getConfiguration().addInterceptor(queryInterceptor);
//        }
//	 }
//}
