package com.springreport.base;


import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.springreport.constants.StatusCode;
import com.springreport.exception.BizException;




/**   
 * @ClassName:  BaseController   
 * @Description:基底controller，controller统一处理 
 * @author: caiyang 
 * @date:   2020年5月29日17:23:53
 *      
 */
public class BaseController {

	@Autowired
	protected HttpServletRequest httpServletRequest;
	@Autowired
	protected HttpServletResponse httpServletResponse;
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	/**   
	 * @Title: exception   
	 * @Description: 异常统一处理 
	 * @param: @param e
	 * @param: @return      
	 * @return: Object      
	 * @throws   
	 */
	@ExceptionHandler
	public Object exception(Exception e)
	{
		String currentUrl = httpServletRequest.getServletPath();//当前请求的url
		logger.error("请求" + currentUrl + "调用结束，调用过程出现异常，异常类型"+e.getClass().toString()+"，异常内容：", e);
		if (e instanceof BizException)
		{//自定义异常
			BizException bizException = (BizException)e;
			return Response.error(bizException.getCode(), bizException.getMessage(),currentUrl);
		}else if(e instanceof UnauthorizedException)
		{
			return Response.error(StatusCode.AUTH_FAILURE, "访问权限不足。",currentUrl);
		}else if(e instanceof DuplicateKeyException)
		{
			return Response.error(StatusCode.AUTH_FAILURE, e.getMessage(),currentUrl);
		}
		else {
			StackTraceElement[] elements = e.getStackTrace();
			StackTraceElement element = null;
			if(elements != null && elements.length>0)
			{
				element = elements[0];
			}
			String errMsg = "";
			if(e instanceof NullPointerException)
			{
				if(element != null)
				{
					errMsg = "空指针异常，异常文件："+element.getFileName()+"，异常方法："+element.getMethodName()+"，异常代码行数："+element.getLineNumber();
				}else {
					errMsg = "空指针异常。";
				}
			}
			else if(e instanceof ClassCastException)
			{
				if(element != null)
				{
					errMsg = "类型转换异常，异常文件："+element.getFileName()+"，异常方法："+element.getMethodName()+"，异常代码行数："+element.getLineNumber();
				}else {
					errMsg = "类型转换异常。";
				}
			}
			else if(e instanceof NegativeArraySizeException)
			{
				if(element != null)
				{
					errMsg = "数组下标不能为负数，异常文件："+element.getFileName()+"，异常方法："+element.getMethodName()+"，异常代码行数："+element.getLineNumber();
				}else {
					errMsg = "数组下标不能为负数。";
				}
			}
			else if(e instanceof ArrayIndexOutOfBoundsException )
			{
				if(element != null)
				{
					errMsg = "数组越界异常，异常文件："+element.getFileName()+"，异常方法："+element.getMethodName()+"，异常代码行数："+element.getLineNumber();
				}else {
					errMsg = "数组越界异常。";
				}
			}
			else if(e instanceof IndexOutOfBoundsException)
			{
				if(element != null)
				{
					errMsg = "数组越界异常，异常文件："+element.getFileName()+"，异常方法："+element.getMethodName()+"，异常代码行数："+element.getLineNumber();
				}else {
					errMsg = "数组越界异常。";
				}
			}
			else if(e instanceof NumberFormatException  )
			{
				if(element != null)
				{
					errMsg = "字符串转换为数字异常，异常文件："+element.getFileName()+"，异常方法："+element.getMethodName()+"，异常代码行数："+element.getLineNumber();
				}else {
					errMsg = "字符串转换为数字异常。";
				}
			}
			else if(e instanceof SQLException)
			{
				if(element != null)
				{
					errMsg = "操作数据库异常，异常文件："+element.getFileName()+"，异常方法："+element.getMethodName()+"，异常代码行数："+element.getLineNumber();
				}else {
					errMsg = "操作数据库异常。";
				}
			}
			else if(e instanceof IOException)
			{
				if(element != null)
				{
					errMsg = "输入输出异常，异常文件："+element.getFileName()+"，异常方法："+element.getMethodName()+"，异常代码行数："+element.getLineNumber();
				}else {
					errMsg = "输入输出异常。";
				}
			}
			else if(e instanceof NoSuchMethodException)
			{
				if(element != null)
				{
					errMsg = "方法找不到异常，异常文件："+element.getFileName()+"，异常方法："+element.getMethodName()+"，异常代码行数："+element.getLineNumber();
				}else {
					errMsg = "方法找不到异常。";
				}
			}
			else if(e instanceof ArrayStoreException)
			{
				if(element != null)
				{
					errMsg = "数组存储异常，异常文件："+element.getFileName()+"，异常方法："+element.getMethodName()+"，异常代码行数："+element.getLineNumber();
				}else {
					errMsg = "数组存储异常。";
				}
			}
			else if(e instanceof CloneNotSupportedException )
			{
				if(element != null)
				{
					errMsg = "不支持克隆异常，异常文件："+element.getFileName()+"，异常方法："+element.getMethodName()+"，异常代码行数："+element.getLineNumber();
				}else {
					errMsg = "不支持克隆异常。";
				}
			}
			else if(e instanceof StringIndexOutOfBoundsException  )
			{
				if(element != null)
				{
					errMsg = "字符串索引越界异常，异常文件："+element.getFileName()+"，异常方法："+element.getMethodName()+"，异常代码行数："+element.getLineNumber();
				}else {
					errMsg = "字符串索引越界异常。";
				}
			}else if(e instanceof SQLSyntaxErrorException)
			{
				errMsg = "sql语句解析错误，错误信息："+e.getMessage();
			}
			else {
				if(element != null)
				{
					errMsg = "系统异常，异常文件："+element.getFileName()+"，异常方法："+element.getMethodName()+"，异常代码行数："+element.getLineNumber();
				}else {
					errMsg = "系统异常，请与管理员联系。";
				}
			}
			return Response.error(StatusCode.FAILURE,errMsg,currentUrl);
		}
	}

}
