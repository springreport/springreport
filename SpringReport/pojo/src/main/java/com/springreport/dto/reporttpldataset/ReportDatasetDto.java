package com.springreport.dto.reporttpldataset;

import java.util.List;
import java.util.Map;

import com.springreport.entity.reporttpldataset.ReportTplDataset;

import lombok.Data;

@Data
public class ReportDatasetDto extends ReportTplDataset{

	/**
	* @Feilds:columns 数据列
	* @author caiyang
	*/  
	private List<Map<String, Object>> columns;
}
