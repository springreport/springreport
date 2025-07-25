package com.springreport.api.screentpl;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.reporttype.ReportType;
import com.springreport.entity.screentpl.ScreenTpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.base.UserInfoDto;
import com.springreport.dto.reporttpl.ShareDto;
import com.springreport.dto.screentpl.MesScreenTplDto;
import com.springreport.dto.screentpl.SaveScreenTplDto;
import com.springreport.dto.screentpl.ScreenTplDto;
import com.springreport.dto.screentpl.ScreenTplTreeDto;

 /**  
* @Description: ScreenTpl服务接口
* @author 
* @date 2021-08-02 07:01:17
* @version V1.0  
 */
public interface IScreenTplService extends IService<ScreenTpl> {

	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @throws 
	*/ 
	PageEntity tablePagingQuery(ScreenTpl model);
	
	List<ScreenTplTreeDto> getChildren(ScreenTpl model);

	/**
	*<p>Title: getDetail</p>
	*<p>Description: 获取详情</p>
	* @author caiyang
	* @param id
	* @return
	*/
	BaseEntity getDetail(Long id);
	
	/**
	*<p>Title: insert</p>
	*<p>Description: 新增数据</p>
	* @author caiyang
	* @param model
	* @return
	*/
	BaseEntity insert(MesScreenTplDto mesScreenTplDto);
	
	/**
	*<p>Title: update</p>
	*<p>Description: 更新数据</p>
	* @author caiyang
	* @param model
	*/
	BaseEntity update(MesScreenTplDto mesScreenTplDto);
	
	/**
	*<p>Title: delete</p>
	*<p>Description: 单条删除数据</p>
	* @author caiyang
	* @param model
	* @return
	*/
	BaseEntity delete(Long id);
	
	/**
	*<p>Title: deleteBatch</p>
	*<p>Description: 批量删除数据</p>
	* @author caiyang
	* @param list
	* @return
	*/
	BaseEntity deleteBatch(List<Long> ids);
	
	/**  
	 * @Title: getScreenDesign
	 * @Description: 获取大屏设计详情
	 * @param screenTpl
	 * @return
	 * @author caiyang
	 * @date 2021-08-02 11:39:06 
	 */ 
	ScreenTplDto getScreenDesign(ScreenTpl screenTpl);
	
	/**  
	 * @Title: saveScreenDesign
	 * @Description: 保存大屏设计
	 * @param screenTplDto
	 * @author caiyang
	 * @throws JsonProcessingException 
	 * @date 2021-08-02 04:38:41 
	 */ 
	BaseEntity saveScreenDesign(SaveScreenTplDto saveScreenTplDto) throws JsonProcessingException;
	
	/**  
	 * @Title: getScreens
	 * @Description: 获取所有的大屏
	 * @return
	 * @author caiyang
	 * @date 2021-08-30 07:35:50 
	 */ 
	List<ScreenTpl> getScreens(ScreenTpl screenTpl);
	
	/**  
	 * @MethodName: copyScreen
	 * @Description: 复制大屏
	 * @author caiyang
	 * @param screenTpl
	 * @return BaseEntity
	 * @date 2025-04-04 12:51:12 
	 */ 
	BaseEntity copyScreen(ScreenTpl screenTpl);
	
	/**  
	 * @MethodName: getShareUrl
	 * @Description: 获取大屏分享链接
	 * @author caiyang
	 * @param shareDto
	 * @param userInfoDto
	 * @return BaseEntity
	 * @date 2025-07-19 07:11:28 
	 */ 
	ShareDto getShareUrl(ShareDto shareDto,UserInfoDto userInfoDto);
	
}
