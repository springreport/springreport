//package com.springreport.base;
//
//import java.io.Serializable;
//
//import com.springreport.constants.StatusCode;
//import com.springreport.enums.MsgLevelEnum;
//
//import lombok.Data;
//
///**  
// * @ClassName: NoConvertResponse
// * @Description: 统一数据返回对象(不需要经过fastjsonconvert)
// * @param <T>
// * @author caiyang
// * @date 2020年9月5日13:16:00
//*/ 
//@Data
//public class NoConvertResponse implements Serializable{
//
//	private String code = StatusCode.SUCCESS;
//	
//
//	/**
//	 * 提示信息
//	 */
//	private String message;
//	
//	/**
//	 * 消息类型
//	 */
//	private String msgLevel = MsgLevelEnum.SUCCESS.getCode();
//	
//	/**
//	 * 返回业务数据
//	 */
//	private Object responseData;
//	
//	/**  
//	 * @Fields url : 请求路径
//	 * @author caiyang
//	 * @date 2020年5月9日 
//	 */ 
//	private String url;
//	
//	/**  
//	 * @Fields newToken : 新生成的token
//	 * @author caiyang
//	 * @date 2020-06-08 05:54:44 
//	 */ 
//	private String newToken;
//	
//	
//	public NoConvertResponse() {
//		
//    }
//	public NoConvertResponse(String statusCode,String msg, Object data, String url,String msgLevel) {
//		this.code = statusCode;
//        this.message = msg;
//        this.responseData = data;
//        this.url = url;
//        this.msgLevel = msgLevel;
//    }
//	/**  
//	 * @Title: success
//	 * @Description: 请求成功返回，只返回数据不带返回消息
//	 * @param data
//	 * @return
//	 * @author caiyang
//	 * @date 2020年5月15日 
//	 */ 
//	public static NoConvertResponse success(Object data) {
//        return new NoConvertResponse(StatusCode.SUCCESS,null, data, "",MsgLevelEnum.SUCCESS.getCode());
//	}
//	
//	/**  
//	 * @Title: success
//	 * @Description: 请求成功返回，不带数据只返回通知消息
//	 * @param msg
//	 * @return
//	 * @author caiyang
//	 * @date 2020年5月15日 
//	 */ 
//	public static NoConvertResponse success(String msg) {
//        return new NoConvertResponse(StatusCode.SUCCESS,msg, null, "",MsgLevelEnum.SUCCESS.getCode());
//	}
//	
//	/**  
//	 * @Title: success
//	 * @Description: 请求成功返回，带数据和消息返回
//	 * @param data
//	 * @param message
//	 * @return
//	 * @author caiyang
//	 * @date 2020年5月15日 
//	 */ 
//	public static NoConvertResponse success(Object data,String message) {
//        return new NoConvertResponse(StatusCode.SUCCESS,message, data, "",MsgLevelEnum.SUCCESS.getCode());
//	}
//	
//	/**  
//	 * @Title: error
//	 * @Description: 请求失败返回，只返回错误码,错误消息和数据
//	 * @param errorCode
//	 * @param errorMsg
//	 * @return
//	 * @author caiyang
//	 * @date 2020年5月15日 
//	 */ 
//	public static NoConvertResponse error(String errorCode,String errorMsg,Object data) {
//        return new NoConvertResponse(errorCode,errorMsg, data, "",MsgLevelEnum.ERROR.getCode());
//	}
//	
//	/**  
//	 * @Title: error
//	 * @Description: 请求失败返回，只返回错误码和错误消息
//	 * @param errorCode
//	 * @param errorMsg
//	 * @return
//	 * @author caiyang
//	 * @date 2020年5月15日 
//	 */ 
//	public static NoConvertResponse error(String errorCode,String errorMsg) {
//        return new NoConvertResponse(errorCode,errorMsg, null, "",MsgLevelEnum.ERROR.getCode());
//	}
//	
//	/**  
//	 * @Title: error
//	 * @Description: 请求失败返回，返回错误码，请求消息和请求的url
//	 * @param errorCode
//	 * @param errorMsg
//	 * @param url
//	 * @return
//	 * @author caiyang
//	 * @date 2020年5月15日 
//	 */ 
//	public static NoConvertResponse error(String errorCode,String errorMsg,String url) {
//        return new NoConvertResponse(errorCode,errorMsg, null, url,MsgLevelEnum.ERROR.getCode());
//	}
//
//}
