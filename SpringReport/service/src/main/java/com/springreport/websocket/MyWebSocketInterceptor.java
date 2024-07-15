package com.springreport.websocket;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.springreport.dto.coedit.WSUserModel;
import com.springreport.util.StringUtil;
import com.springreport.util.UrlUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MyWebSocketInterceptor extends HttpSessionHandshakeInterceptor{

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        //解决The extension [x-webkit-deflate-frame] is not supported问题
//        if (request.getHeaders().containsKey("Sec-WebSocket-Extensions")) {
//            request.getHeaders().set("Sec-WebSocket-Extensions", "permessage-deflate");
//        }

        //websocket系统启动连接程序，启动的时候就会把他的session值传过来，放入到websocketsession（websocket的一个内置服务器）里面
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        HttpSession session=servletRequest.getServletRequest().getSession(false);
        log.error("beforeHandshake--session:{}",session);
        //用户token
        String token =getParam(servletRequest,WSUserModel.USER_TOKEN);
        //文档id
        String gridKey = getParam(servletRequest,WSUserModel.USER_GRIDKEY);
        if (null!=token && null!=gridKey) {
        	//验证用户信息
        	String gridKeys = UrlUtils.urlDecode(gridKey);
        	String _checkStr=check(gridKeys);
            log.error("link gridKey:{};userToken:{};check:{}",gridKeys,token,_checkStr);
        	if(_checkStr.length()>0){
        	    return false;
        	}
            attributes.put(WSUserModel.USER_TOKEN, token);
            attributes.put(WSUserModel.USER_GRIDKEY, gridKey);
            attributes.put(WSUserModel.VISITOR_CODE, StringUtil.getCode());
        } else {
            return false;
        }
        return true;
    }


    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {
    	log.error("exception:{}",exception);
    }


    private String getParam(ServletServerHttpRequest servletRequest, String key){
        String value=servletRequest.getServletRequest().getParameter(key);
        if(value!=null){
            value=value.trim();
            if(value.length()>0){
                return  value;
            }
        }
        return null;
    }

    /**
     * /修改、读取ajax 如果_ObjectId为空，使用id
     * @param _id
     * @return
     */
    private String check(String _id){
        //此处校验代码
        return "";
    }


}
