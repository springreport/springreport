package com.springreport.api.sysrolesheet;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springreport.entity.sysrolesheet.SysRoleSheet;
import com.springreport.base.BaseEntity;
import com.springreport.base.PageEntity;
import com.springreport.dto.report.TreeDto;
import com.springreport.dto.sysrole.MesAuthDto;

 /**  
* @Description: SysRoleSheet服务接口
* @author 
* @date 2022-11-08 03:27:24
* @version V1.0  
 */
public interface ISysRoleSheetService extends IService<SysRoleSheet> {
	
	/**  
	 * @Title: getReportTree
	 * @Description: 获取报表树
	 * @param model
	 * @return
	 * @author caiyang
	 * @date 2022年7月6日07:57:58
	 */ 
	TreeDto getReportTree(SysRoleSheet model);
	
	/**  
	 * @Title: authed
	 * @Description: 报表角色授权
	 * @param mesAuthDto
	 * @return
	 * @author caiyang
	 * @date 2022年7月6日07:57:53
	 */ 
	BaseEntity authed(MesAuthDto mesAuthDto);
}
