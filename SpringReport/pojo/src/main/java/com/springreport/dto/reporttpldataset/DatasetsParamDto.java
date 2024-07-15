package com.springreport.dto.reporttpldataset;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
* <p>Title: DatasetsParamDto</p>
* <p>Description: 数据集参数用实体类</p>
* @author caiyang
* @date 2020年3月20日
*/
@Data
public class DatasetsParamDto implements Serializable{

	/**
	* @Feilds:datasetId 数据集id
	* @author caiyang
	*/  
	private Long datasetId;
	
	/**
	* @Feilds:datasetName 数据集名称
	* @author caiyang
	*/  
	private String datasetName;
	
	/**
	* @Feilds:datasourceId 数据库id
	* @author caiyang
	*/  
	private Long datasourceId;
	
	/**
	* @Feilds:params 参数
	* @author caiyang
	*/  
	private List<ReportParamDto> params;
}
