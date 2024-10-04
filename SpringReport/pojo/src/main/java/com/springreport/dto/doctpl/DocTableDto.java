package com.springreport.dto.doctpl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

@Data
public class DocTableDto {

	private String value = "";
	
	private String type = "table";
	
	private List<DocTableRowDto> trList;
	
	private int width;
	
	private int height;
	
	private List<JSONObject> colgroup = new ArrayList<>();
}
