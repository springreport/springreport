package com.springreport.dto.coedit;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import com.springreport.util.JWTUtil;
import com.springreport.util.StringUtil;

import java.util.Hashtable;

/**
 * @author Administrator
 */
@Data
public class WSUserModel {

    /**
     * 用户key
     */
    public static final String USER_TOKEN="Authorization";
    /**
     * 文档key
     */
    public static final String USER_GRIDKEY="g";
    
    /**  
     * @Fields VISITOR : 游客编码
     * @author caiyang
     * @date 2023-08-20 09:41:24 
     */  
    public static final String VISITOR_CODE="visitorCode";

    /**
     * ws-session
     */
    private WebSocketSession ws;
    /**
     * session id
     */
    private String id;
    /**
     * 接收token
     */
    private String token;
    /**
     * 文档id
     */
    private String gridKey;
    /**
     * 连接的用户名
     */
    private String userName;
    
    /**  
     * @Fields userId : 用户id
     * @author caiyang
     * @date 2024-01-01 08:18:04 
     */  
    private Long userId;

    public WSUserModel(WebSocketSession ws){
    	if(ws != null)
    	{
    		this.id=ws.getId();
            this.userName="游客_"+ws.getAttributes().get(VISITOR_CODE).toString();;
            if(ws.getAttributes().get(USER_TOKEN)!=null && !"null".equals(ws.getAttributes().get(USER_TOKEN)) && !"undefined".equals(ws.getAttributes().get(USER_TOKEN))){
                this.token=ws.getAttributes().get(USER_TOKEN).toString();
                String userName = JWTUtil.getUsername(this.token);
                if(StringUtil.isNotEmpty(userName))
                {
                	this.userName = userName;
                }
                Long userId = JWTUtil.getUserId(this.token);
                if(userId != null)
                {
                	this.userId = userId;
                }
            }else{
                this.token="i";
            }
            if(ws.getAttributes().get(USER_GRIDKEY)!=null){
                this.gridKey=ws.getAttributes().get(USER_GRIDKEY).toString();
            }else{
                this.gridKey="1";
            }
            this.ws=ws;
    	}
        
    }


    /**
     * 外层key gridKey（文档id），内层key session ID（用户id）
     * @param maps
     * @param wm
     */
    public static void webSocketMapAdd(Hashtable<String,Hashtable<String,WSUserModel>> maps,WSUserModel wm){
        if(maps.containsKey(wm.getGridKey())){
            maps.get(wm.getGridKey()).put(wm.getId(),wm);
        }else{
            Hashtable<String,WSUserModel> _map=new Hashtable<String,WSUserModel>();
            _map.put(wm.getId(),wm);
            maps.put(wm.getGridKey(),_map);
        }
    }
    public static void webSocketMapRemove(Hashtable<String,Hashtable<String,WSUserModel>> maps, WSUserModel wm){
        if(maps.containsKey(wm.getGridKey())){
            if(maps.get(wm.getGridKey())!=null){
                Hashtable<String,WSUserModel> _map=maps.get(wm.getGridKey());
                if(_map!=null && _map.containsKey(wm.getId())){
                    _map.remove(wm.getId());
                    wm = null;
                }
            }
        }
    }

}
