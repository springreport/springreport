package com.springreport.base;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

/**  
 * @ClassName: MesExportExcel
 * @Description: 导出excel用实体类
 * @author caiyang
 * @date 2022-10-21 05:07:39 
*/  
@Data
public class MesExportExcel {

	/**  
	 * @Fields password : 密码
	 * @author caiyang
	 * @date 2022-10-21 05:08:06 
	 */  
	private String password;
	
	/**  
	 * @Fields sheetConfigs : sheet页配置信息
	 * @author caiyang
	 * @date 2022-10-21 05:16:46 
	 */  
	private List<MesSheetConfig> sheetConfigs;
	
	/**  
	 * @Fields chartsBase64 : chart的base64数据
	 * @author caiyang
	 * @date 2023-07-03 09:53:02 
	 */  
	private JSONObject chartsBase64;
	
	/**  
	 * @Fields imageInfos : 计算位置后的图片信息
	 * @author caiyang
	 * @date 2024-07-13 09:54:54 
	 */  
	private Map<String, Map<String, Object>> imageInfos;
	
	/**  
	 * @Fields backImages : 背景图片，通过插入图片添加的图片
	 * @author caiyang
	 * @date 2024-07-13 07:49:10 
	 */  
	private Map<String, String> backImages;
	
}
