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
 * @ClassName: UpdateOnlineTypeEnum
 * @Description: 更新在线文档类型
 * @author caiyang
 * @date 2023-02-07 04:44:27 
*/  
public enum UpdateOnlineTypeEnum implements BaseCharEnum{

	UPDATECELL {
		public String getCode() {
			return "updateCell";
		}
		public String getName() {
			return "更新单元格";
		}
	},
	UPDATECELLFORMAT {
		public String getCode() {
			return "updateCellFormat";
		}
		public String getName() {
			return "更新单元格格式";
		}
	},
	UPDATECELLFORMATOTHER {
		public String getCode() {
			return "updateCellFormatOther";
		}
		public String getName() {
			return "更新单元格其他格式";
		}
	},
	UPDATEMERGE {
		public String getCode() {
			return "updateMerge";
		}
		public String getName() {
			return "更新单元格合并信息";
		}
	},
	UPDATEBORDER{
		public String getCode() {
			return "updateBorder";
		}
		public String getName() {
			return "更新边框信息";
		}
	},
	UPDATEROWLEN{
		public String getCode() {
			return "updateRowlen";
		}
		public String getName() {
			return "更新行高";
		}
	},
	UPDATECOLUMNLEN{
		public String getCode() {
			return "updateColumnlen";
		}
		public String getName() {
			return "更新列宽";
		}
	},
	CREATESHEET{
		public String getCode() {
			return "createSheet";
		}
		public String getName() {
			return "新增sheet";
		}
	},
	DELETESHEET{
		public String getCode() {
			return "deleteSheet";
		}
		public String getName() {
			return "删除sheet";
		}
	},
	CHANGESHEETNAME{
		public String getCode() {
			return "changeSheetName";
		}
		public String getName() {
			return "修改sheet名称";
		}
	},
	ROWCOLUMNINSERT{
		public String getCode() {
			return "rowColumnInsert";
		}
		public String getName() {
			return "行和列新增操作";
		}
	},
	ROWCOLUMNDEL{
		public String getCode() {
			return "rowColumnDel";
		}
		public String getName() {
			return "行和列删除操作";
		}
	},
}
