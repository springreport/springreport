package com.springreport.dto.reporttpl;

import com.springreport.enums.ShareTypeEnum;
import com.springreport.enums.YesNoEnum;

import lombok.Data;

/**  
 * @ClassName: ShareDto
 * @Description: 分享用参数实体类
 * @author caiyang
 * @date 2023-06-25 01:55:59 
*/  
@Data
public class ShareDto {

	/**  
	 * @Fields tplId : 模板id
	 * @author caiyang
	 * @date 2023-06-25 02:17:55 
	 */  
	private Long tplId;
	
	/**  
	 * @Fields shareTime : 分享有效时长
	 * @author caiyang
	 * @date 2023-06-25 01:57:29 
	 */  
	private Long shareTime;
	
	/**  
	 * @Fields shareType : 分享类型 1 pc 2 h5
	 * @author caiyang
	 * @date 2023-06-25 01:58:02 
	 */  
	private Integer shareType = ShareTypeEnum.PC.getCode();
	
	/**  
	 * @Fields shareMsg : 分享链接信息
	 * @author caiyang
	 * @date 2023-06-25 02:02:04 
	 */  
	private String shareMsg;
	
	/**  
	 * @Fields tplType : 报表类型 1展示报表 2填报报表
	 * @author caiyang
	 * @date 2023-06-26 02:27:10 
	 */  
	private Integer tplType = 1;
	
	/**  
	 * @Fields allowReport : 填报报表是否允许上报数据 1是 2否
	 * @author caiyang
	 * @date 2023-06-26 06:22:36 
	 */  
	private Integer allowReport = YesNoEnum.NO.getCode();
	
	/**  
	 * @Fields thirdPartyType : 第三方
	 * @author caiyang
	 * @date 2025-01-09 09:22:32 
	 */  
	private String thirdPartyType;
	
	/**  
	 * @Fields isShareForever : 分享是否永久有效
	 * @author caiyang
	 * @date 2025-07-19 06:59:08 
	 */  
	private Integer isShareForever = YesNoEnum.NO.getCode();
}
