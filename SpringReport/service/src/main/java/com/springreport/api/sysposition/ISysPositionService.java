package com.springreport.api.sysposition;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.sysposition.SysPosition;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;

 /**  
* @Description: SysPosition服务接口
* @author 
* @date 2022-06-24 10:54:59
* @version V1.0  
 */
public interface ISysPositionService extends IService<SysPosition> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	void tablePagingQuery(SysPosition model);

}
