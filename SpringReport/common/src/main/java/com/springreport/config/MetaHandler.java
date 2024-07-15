package com.springreport.config;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springreport.base.UserInfoDto;
import com.springreport.enums.RequestHeaderEnums;
import com.springreport.util.JWTUtil;
import com.springreport.util.StringUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

/**  
 * @ClassName: MetaHandler
 * @Description: 公共字段写入
 * @author caiyang
 * @date 2020-05-29 10:30:23 
*/  
@Component
public class MetaHandler implements MetaObjectHandler{

	@Autowired
	private HttpServletRequest httpServletRequest;
	
	/**  
	 * @Title: insertFill
	 * @Description: 新增时写入创建时间和更新时间
	 * @param metaObject 
	 * @see com.baomidou.mybatisplus.core.handlers.MetaObjectHandler#insertFill(org.apache.ibatis.reflection.MetaObject) 
	 * @author caiyang
	 * @date 2020-05-29 10:30:36 
	 */
	@Override
	public void insertFill(MetaObject metaObject) {
		if (metaObject.hasGetter("createTime")) {
			this.setFieldValByName("createTime", new Date(), metaObject);
		}
		if (metaObject.hasGetter("updateTime")) {
			this.setFieldValByName("updateTime", new Date(), metaObject);
		}
		try {
			//如果有creator或者creatorName字段，自动填充
			if (metaObject.hasGetter("creator") ) {
				UserInfoDto userInfoDto = new UserInfoDto();
				String authorization = httpServletRequest.getHeader(RequestHeaderEnums.Authorization.getCode());//获取header信息
				if (!StringUtil.isNullOrEmpty(authorization)) {
					userInfoDto = JWTUtil.getUserInfo(authorization);
					if (metaObject.hasGetter("creator") && userInfoDto != null) {
						this.setFieldValByName("creator", userInfoDto.getUserId(), metaObject);
					}
					if (metaObject.hasGetter("updateBy"))
			    	{
			    		this.setFieldValByName("updateBy", userInfoDto.getUserId(), metaObject);
			    	}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**  
	 * @Title: updateFill
	 * @Description: 更新是写入更新时间
	 * @param metaObject 
	 * @see com.baomidou.mybatisplus.core.handlers.MetaObjectHandler#updateFill(org.apache.ibatis.reflection.MetaObject) 
	 * @author caiyang
	 * @date 2020-05-29 10:30:58 
	 */
	@Override
	public void updateFill(MetaObject metaObject) {
		if (metaObject.hasGetter("updateTime")) {
			this.setFieldValByName("updateTime", new Date(), metaObject);
		}
		try {
			//如果有creator或者creatorName字段，自动填充
			if (metaObject.hasGetter("updater") ) {
				UserInfoDto userInfoDto = new UserInfoDto();
				String authorization = httpServletRequest.getHeader(RequestHeaderEnums.Authorization.getCode());//获取header信息
				if (!StringUtil.isNullOrEmpty(authorization)) {
					userInfoDto = JWTUtil.getUserInfo(authorization);
					if (metaObject.hasGetter("updater"))
			    	{
			    		this.setFieldValByName("updater", userInfoDto.getUserId(), metaObject);
			    	}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
	}

}
