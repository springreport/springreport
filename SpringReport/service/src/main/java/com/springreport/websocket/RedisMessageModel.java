package com.springreport.websocket;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * redis消息
 * @author Administrator
 */
@Data
public class RedisMessageModel {

    /**
     * 节点端口与ip （或者其他唯一标示）
     */
    private String ipandport;
    /**
     * 文档id
     */
    private String gridkey;
    /**
     * 内容
     */
    private Object content;
    
    private boolean sendSelf;

    public RedisMessageModel(String _ipandport,String _gridkey,Object _content,boolean sendSelf){
        this.ipandport=_ipandport;
        this.gridkey=_gridkey;
        this.content=_content;
        this.sendSelf=sendSelf;
    }
    public RedisMessageModel(JSONObject jsonObject){
        if(jsonObject!=null) {
            if (jsonObject.containsKey("ipandport")) {
                this.ipandport = jsonObject.getString("ipandport");
            }
            if (jsonObject.containsKey("gridkey")) {
                this.gridkey = jsonObject.getString("gridkey");
            }
            if (jsonObject.containsKey("content")) {
                this.content = jsonObject.get("content");
            }
            if (jsonObject.containsKey("sendSelf")) {
                this.sendSelf = jsonObject.getBooleanValue("sendSelf");
            }
        }
    }

//    public JSONObject toDBObject(){
//        JSONObject jsonObject=new JSONObject();
//        jsonObject.put("ipandport",ipandport);
//        jsonObject.put("gridkey",gridkey);
//        jsonObject.put("content",content);
//        jsonObject.put("sendSelf",sendSelf);
//        return jsonObject;
//    }

//    @Override
//    public String toString() {
//        return "RedisMessageModel{" +
//                "ipandport='" + ipandport + '\'' +
//                ", gridkey='" + gridkey + '\'' +
//                ", content='" + content + '\'' +
//                ", sendSelf='" + sendSelf + '\'' +
//                '}';
//    }
}
