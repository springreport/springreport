package com.springreport.websocket;

import org.springframework.context.annotation.Bean;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.springreport.util.IpAndPortUtil;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{

	/**
     * 注入ServerEndpointExporter，
     * 这个bean会自动注册使用了@ServerEndpoint注解声明的Websocket endpoint
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
    
//    @Bean
//    public ServletServerContainerFactoryBean createWebSocketContainer(){
//        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
//        container.setMaxSessionIdleTimeout(10L);
//        return container;
//    }
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //1.注册WebSocket
        //设置websocket的地址
        String websocket_url = "/springReport/api/coedit/websocket/luckysheet";
        //注册Handler
        registry.addHandler(getMyWebSocketHandler(), websocket_url).
                //注册Interceptor
                addInterceptors(getMyWebSocketInterceptor())
                //配置*代表允许所有的ip进行调用
                .setAllowedOrigins("*");
        //2.注册SockJS，提供SockJS支持(主要是兼容ie8)
        //设置sockjs的地址a
        String sockjs_url = "/sockjs/luckysheet";
        //注册Handler
        registry.addHandler(getMyWebSocketHandler(), sockjs_url).
                //注册Interceptor
                addInterceptors(getMyWebSocketInterceptor())
                //配置*代表允许所有的ip进行调用
                .setAllowedOrigins("*").withSockJS();
        

        //获取系统ip
        MyWebSocketHandler.ipAndPort= IpAndPortUtil.getIpAddressAndPort();

    }

    @Bean
    public MyWebSocketHandler getMyWebSocketHandler(){
        return new MyWebSocketHandler();
    }

    @Bean
    public MyWebSocketInterceptor getMyWebSocketInterceptor(){
        return new MyWebSocketInterceptor();
    }
}
