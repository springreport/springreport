package com.springreport.base;

import com.springreport.constants.StatusCode;
import com.springreport.enums.MsgLevelEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**  
 * @ClassName: BaseEntity
 * @Description: 基底实体类
 * @author caiyang
 * @date 2020-05-29 10:56:13 
*/  
@Data
public class BaseEntity implements Serializable{

	/**
     * 	执行状态
     * 200:执行成功
     * 	其他:异常
     */
	@TableField(exist = false)
    private String statusCode = StatusCode.SUCCESS;

    /**
     * 	消息内容
     */
	@TableField(exist = false)
    private String statusMsg = "";
	
	/**
	 * 	消息级别
	 * info
	 * warn
	 * error
	 */
	@TableField(exist = false)
	private String msgLevel	= MsgLevelEnum.SUCCESS.getCode();
	
	/**
     * 排序字符串
     * <p>在数据库的XML文件中，通过${}直接写入原文</p>
     */
	@TableField(exist = false)
    private String orderSql = StringUtils.EMPTY;
	
	@TableField(exist = false)
	private List<?> children;

}
