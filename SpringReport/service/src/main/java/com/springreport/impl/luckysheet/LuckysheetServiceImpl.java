package com.springreport.impl.luckysheet;

import com.springreport.entity.luckysheet.Luckysheet;
import com.springreport.mapper.luckysheet.LuckysheetMapper;
import com.springreport.api.luckysheet.ILuckysheetService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.springreport.util.MessageUtil;
import com.github.pagehelper.PageHelper;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.springreport.enums.DelFlagEnum;

 /**  
* @Description: Luckysheet服务实现
* @author 
* @date 2023-08-12 05:57:21
* @version V1.0  
 */
@Service
public class LuckysheetServiceImpl extends ServiceImpl<LuckysheetMapper, Luckysheet> implements ILuckysheetService {

	/**  
	 * @MethodName: saveBatch
	 * @Description: 批量保存
	 * @author caiyang
	 * @param models
	 * @see com.springreport.api.luckysheet.ILuckysheetService#saveBatch(java.util.List)
	 * @date 2023-08-30 09:59:17 
	 */
	@Override
	@Async
	public void saveBatchDatas(List<Luckysheet> models) {
		this.saveBatch(models);
	}

	/**  
	 * @MethodName: getOneButJsonData
	 * @Description: 获取一条数据除了json_data字段，如果json_data字段太大查询太慢
	 * @author caiyang
	 * @param jsonObject
	 * @return
	 * @see com.springreport.api.luckysheet.ILuckysheetService#getOneButJsonData(com.alibaba.fastjson.JSONObject)
	 * @date 2023-09-10 09:01:24 
	 */
	@Override
	public Luckysheet getOneButJsonData(JSONObject jsonObject) {
		Luckysheet result = this.baseMapper.getOneButJsonData(jsonObject);
		return result;
	}
	
	
	
}