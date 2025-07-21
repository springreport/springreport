package com.springreport.base;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.springreport.enums.YesNoEnum;

import lombok.Data;

/** 
* @ClassName: UserInfoDto 
* @Description: 用户登陆信息存储用实体类
* @author joseph
* @date 2019年9月16日 下午4:56:10 
*  
*/
@Data
public class UserInfoDto implements Serializable{

	/** 
	* @Fields token : 登陆标识
	* @author joseph
	*/ 
	private String token;
	
	/**
	* @Fields userName : 用户名
	* @author caiyang
	*/  
	
	private String userName;
	/**
	* @Fields userId 用户表主键userId
	* @author caiyang
	*/  
	private Long userId;
	
	/**
	* @Fields userRealName 用户真实姓名
	* @author caiyang
	*/  
	private String userRealName;
	
	/**
	* @Fields roleId 角色id
	* @author caiyang
	*/  
	private Long roleId;
	
	/**
	* @Fields roleName 角色名称
	* @author caiyang
	*/  
	private String roleName;
	
	/**
	* @Feilds:isAdmin是否管理员 1是 2否
	* @author caiyang
	*/  
	private Integer isAdmin;
	
	/**
	* @Feilds:apis 角色对应的api权限
	* @author caiyang
	*/  
	private List<String> apis;
	
	/**  
	 * @Fields expireDate : 过期时间
	 * @author caiyang
	 * @date 2021-06-18 08:01:13 
	 */ 
	private Date expireDate;
	
	/**  
	 * @Fields merchantMode : 是否是多租户模式
	 * @author caiyang
	 * @date 2023-12-22 06:17:32 
	 */  
	private Integer merchantMode = YesNoEnum.NO.getCode();
	
	
	/**  
	 * @Fields merchantNo : 商户号
	 * @author caiyang
	 * @date 2023-12-22 08:23:41 
	 */  
	private String merchantNo;
	
	/**  
	 * @Fields isSystemMerchant : 是否是系统商户
	 * @author caiyang
	 * @date 2023-12-23 09:36:28 
	 */  
	private Integer isSystemMerchant;
	
	/**  
	 * @Fields deptId : 部门id
	 * @author caiyang
	 * @date 2024-08-14 11:35:54 
	 */  
	private Long deptId;
	
	/**  
	 * @Fields isShareToken : 是否是分享链接的token
	 * @author caiyang
	 * @date 2025-07-20 07:49:33 
	 */  
	private Integer isShareToken = YesNoEnum.NO.getCode();
	
}
