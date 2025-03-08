/*   
 * Copyright (c) 2016-2020 canaanQd. All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * canaanQd. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with canaanQd.   
 *   
 */ 

package com.springreport.enums;

import com.springreport.base.BaseCharEnum;

/** 
* @ClassName: RedisPrefixEnum 
* @Description: redis缓存前缀
* @author joseph
* @date 2022年10月13日17:45:44
*  
*/
public enum RedisPrefixEnum implements BaseCharEnum{

	DATASETCOLUMN {
		public String getCode() {
			return "datasetColumn:";
		}

		public String getName() {
			return "数据集字段";
		}
	},
	REPORTDATAVERSION {
		public String getCode() {
			return "reportDataVersion:";
		}

		public String getName() {
			return "上报数据版本号";
		}
	},
	UPDATEREPORT {
		public String getCode() {
			return "updateReport:";
		}

		public String getName() {
			return "在线文档更新";
		}
	},
	UPDATEONLINEVERSION {
		public String getCode() {
			return "updateOnlineVersion:";
		}

		public String getName() {
			return "更新在线文档版本号";
		}
	},
	ONLINEUPDATEVALUE{
		public String getCode() {
			return "onlineUpdateValue:";
		}

		public String getName() {
			return "在线文档更新值";
		}
	},
	ONLINEUPDATEFORMAT{
		public String getCode() {
			return "onlineUpdateFormat:";
		}

		public String getName() {
			return "在线文档更新格式";
		}
	},
	ONLINEUPDATEMERGE{
		public String getCode() {
			return "onlineUpdateMerge:";
		}

		public String getName() {
			return "在线文档合并单元格更新";
		}
	},
	ONLINEUPDATEBORDERINFO{
		public String getCode() {
			return "onlineUpdateBorderInfo:";
		}

		public String getName() {
			return "在线文档边框更新";
		}
	},
	ONLINEUPDATEROWLEN{
		public String getCode() {
			return "onlineUpdateRowlen:";
		}

		public String getName() {
			return "在线文档行高更新";
		}
	},
	ONLINEUPDATECOLUMNLEN{
		public String getCode() {
			return "onlineUpdateColumnlen:";
		}

		public String getName() {
			return "在线文档列宽更新";
		}
	},
	ONLINEUPDATESHEETNAME{
		public String getCode() {
			return "onlineUpdateSheetName:";
		}

		public String getName() {
			return "在线文档sheet名称更新";
		}
	},
	ROWCOLUMNOPERATE{
		public String getCode() {
			return "rowColumnOperate:";
		}

		public String getName() {
			return "行和列新增删除操作";
		}
	},
	SHAREREPORT{
		public String getCode() {
			return "shareReport:";
		}

		public String getName() {
			return "分享报表链接";
		}
	},
	COEDIT{
		public String getCode() {
			return "coedit:";
		}

		public String getName() {
			return "协同编辑";
		}
	},
	DOCOMENTDATA{
		public String getCode() {
			return "documentData:";
		}

		public String getName() {
			return "协同编辑文档数据";
		}
	},
	DOCOMENTCELLDATA{
		public String getCode() {
			return "documentCellData:";
		}

		public String getName() {
			return "协同编辑文档单元格数据";
		}
	},
	ACTIVEDOCUMENT{
		public String getCode() {
			return "activeDocument:";
		}

		public String getName() {
			return "当前激活的协同编辑文档";
		}
	},
	DOCUMENTCACHEFLAG{
		public String getCode() {
			return "documentCacheFlag:";
		}

		public String getName() {
			return "协同编辑文档缓存标志";
		}
	},
	MQRETRYTIMES{
		public String getCode() {
			return "mqRetryTimes:";
		}

		public String getName() {
			return "mq失败重试次数";
		}
	},
	SEQUENCE{
		public String getCode() {
			return "sequence:";
		}

		public String getName() {
			return "序列号";
		}
	},
	REPORTDICT{
		public String getCode() {
			return "reportDict:";
		}

		public String getName() {
			return "报表数据字典";
		}
	},
	COEDITSHAREMODE{
		public String getCode() {
			return "coeditShareMode:";
		}

		public String getName() {
			return "协同文档分享模式";
		}
	},
	COEDITUSERS{
		public String getCode() {
			return "coeditUsers:";
		}

		public String getName() {
			return "协同编辑用户列表";
		}
	},
	REPORTTPLSHEETTEMPCACHE{
		public String getCode() {
			return "reportTplSheetTempCache:";
		}

		public String getName() {
			return "报表模板sheet页临时缓存";
		}
	},
	REPORTTPLTEMPCACHE{
		public String getCode() {
			return "reportTplTempCache:";
		}

		public String getName() {
			return "报表模板临时缓存";
		}
	},
	REPORTCELLDATATEMPCACHE{
		public String getCode() {
			return "reportCelldataTempCache:";
		}

		public String getName() {
			return "填报报表单元格数据临时缓存";
		}
	},
	ATTACHMENTCACHE{
		public String getCode() {
			return "attachmentCache:";
		}

		public String getName() {
			return "附件查看临时缓存";
		}
	},
	THIRDPARTYTOKEN{
		public String getCode() {
			return "thirdPartyToken:";
		}

		public String getName() {
			return "第三方token缓存";
		}
	},
	ONLYOFFICEDOCKEY{
		public String getCode() {
			return "onlyofficeDocKey:";
		}

		public String getName() {
			return "onlyoffice文档key";
		}
	},
	ONLYOFFICELOCKKEY{
		public String getCode() {
			return "onlyofficeLockKey:";
		}

		public String getName() {
			return "onlyoffice锁key";
		}
	}
}
