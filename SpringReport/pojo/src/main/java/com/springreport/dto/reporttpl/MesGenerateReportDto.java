package com.springreport.dto.reporttpl;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.springreport.enums.YesNoEnum;

import lombok.Data;

/**  
 * @ClassName: GenerateReportDto
 * @Description: 生成报表用参数实体类
 * @author caiyang
 * @date 2021-05-26 06:00:17 
*/  
@Data
public class MesGenerateReportDto {

	/**  
	 * @Fields tplId : 模板id
	 * @author caiyang
	 * @date 2021-05-26 06:00:08 
	 */ 
	private Long tplId;
	
	/**
	* @Feilds:searchData 动态参数
	* @author caiyang
	*/  
	private List<Map<String, Object>> searchData;
	
	/**  
	 * @Fields pagination : 分页参数
	 * @author caiyang
	 * @date 2023-01-16 02:19:47 
	 */  
	private Map<String, Integer> pagination;
	
	/**
	 *	 每页显示的条数
	 */
	private Integer pageSize = 500;

	/**
	 * 	当前页数
	 */
	private int currentPage = 1;
	
	/**
	 * 	偏移量
	 */
	private int offSet = 1;
	
	/**  
	 * @Fields dataType : 请求来源 1预览数据 2导出全部数据 3导出当前页数据，默认为1
	 * @author caiyang
	 * @date 2021-06-10 05:56:00 
	 */ 
	private Integer source = 1;
	
	/**  
	 * @Fields password : 密码
	 * @author caiyang
	 * @date 2022-07-05 06:16:24 
	 */  
	private String password;
	

	public int getOffSet() {
	    return (currentPage-1)*pageSize;
	}
	
	/**  
	 * @Fields isValidateRole : 是否需要校验角色权限，1是 2否 默认2
	 * @author caiyang
	 * @date 2021-06-23 06:40:46 
	 */ 
	private Integer isValidateRole = YesNoEnum.NO.getCode();
	
	/**  
	 * @Fields roleId : 角色id，校验时需要传入角色id
	 * @author caiyang
	 * @date 2021-06-23 06:41:05 
	 */ 
	private Long roleId;
	
	/**  
	 * @Fields isPreview : 是否是预览
	 * @author caiyang
	 * @date 2021-09-02 06:30:18 
	 */ 
	private Integer isPreview = YesNoEnum.YES.getCode();
	
	/**  
	 * @Fields isPatination : 是否分页查询
	 * @author caiyang
	 * @date 2021-12-16 07:25:07 
	 */ 
	private boolean isPatination = false;
	
	/**  
	 * @Fields tplName : 文档名称
	 * @author caiyang
	 * @date 2023-02-17 11:18:24 
	 */  
	private String tplName;
	
	/**  
	 * @Fields sheetIndex : sheet index
	 * @author caiyang
	 * @date 2023-04-03 01:48:24 
	 */  
	private String sheetIndex;
	
	/**  
	 * @Fields pdfType : 横向还是纵向pdf 1纵向 2横向 默认1
	 * @author caiyang
	 * @date 2023-04-06 04:40:20 
	 */  
	private Integer pdfType = 1;
	
	/**  
	 * @Fields isCustomerPage : 是否指定页数 1是 2否
	 * @author caiyang
	 * @date 2023-05-19 07:55:32 
	 */  
	private Integer isCustomerPage = 2;
	
	/**  
	 * @Fields startPage : 起始页
	 * @author caiyang
	 * @date 2023-05-19 07:55:58 
	 */  
	private Integer startPage;
	
	/**  
	 * @Fields endPage : 结束页
	 * @author caiyang
	 * @date 2023-05-19 07:56:17 
	 */  
	private Integer endPage;
	
	/**  
	 * @Fields isMobile : 是否是手机端
	 * @author caiyang
	 * @date 2023-06-29 07:48:30 
	 */  
	private Integer isMobile = YesNoEnum.NO.getCode();
	
	/**  
	 * @Fields chartsBase64 : chart的base64数据
	 * @author caiyang
	 * @date 2023-07-03 09:53:02 
	 */  
	private JSONObject chartsBase64;
	
	/**  
	 * @Fields taskId : 任务id
	 * @author caiyang
	 * @date 2023-07-29 09:56:07 
	 */  
	private Long taskId;
	
	/**  
	 * @Fields exportType : 导出类型 1excel  2pdf 3excel和pdf
	 * @author caiyang
	 * @date 2023-07-29 09:56:23 
	 */  
	private int exportType = 1;
	
	/**  
	 * @Fields sendEmail : 发送邮件
	 * @author caiyang
	 * @date 2023-07-29 03:51:09 
	 */  
	private String sendEmail;
	
	/**  
	 * @Fields jobName : 任务名称
	 * @author caiyang
	 * @date 2023-07-29 03:54:22 
	 */  
	private String jobName;
	
	/**  
	 * @Fields fileId : 页面加载时分配一个文件名，防止每次查询都生成一个新的文件(doc用)
	 * @author caiyang
	 * @date 2024-05-09 04:50:05 
	 */  
	private String fileId;
	
	/**  
	 * @Fields apiHeaders : api请求所需要的url中的动态header
	 * @author caiyang
	 * @date 2024-05-09 08:26:24 
	 */  
	private JSONObject apiHeaders;
	
	/**  
	 * @Fields isLarge : 是否是大量数据导出 默认否
	 * @author caiyang
	 * @date 2025-03-15 08:54:20 
	 */  
	private boolean isLarge = false;
	
	/**  
	 * @Fields isTask : 是否定时任务
	 * @author caiyang
	 * @date 2025年4月11日17:38:48
	 */  
	private Integer isTask = YesNoEnum.NO.getCode();
	
	/**  
	 * @Fields activeSheet : 当前选中的sheet
	 * @author caiyang
	 * @date 2025-05-07 01:48:03 
	 */  
	private String activeSheet;
}
