package com.springreport.base;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.deepoove.poi.data.RowRenderData;

import lombok.Data;

@Data
public class ServerTableData {

    /**
     *  携带表格中真实数据
     */
    private List<RowRenderData> serverDataList;

    /**
     * 携带要分组的信息
     */
    private List<Map<String, Object>> groupDataList;

    
    private Map<Integer, List<WordTableMerge>> mergeInfos;
}

