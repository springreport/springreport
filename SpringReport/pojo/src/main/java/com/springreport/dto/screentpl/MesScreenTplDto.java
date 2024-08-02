package com.springreport.dto.screentpl;

import java.util.List;

import com.springreport.entity.screentpl.ScreenTpl;

import lombok.Data;

/**  
 * @ClassName: MesScreenTplDto
 * @Description: 新增更新大屏模板用实体类
 * @author caiyang
 * @date 2021-08-08 06:31:24 
*/  
@Data
public class MesScreenTplDto extends ScreenTpl{

	/**
	 * 	报表数据源
	 */
	private List<Long> dataSource;
}
