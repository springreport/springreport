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
* @ClassName: MqTypeEnums 
* @Description: 消息队列类型
* @author caiyang
* @date 2023年9月13日15:53:25
*  
*/
public enum MqTypeEnums implements BaseCharEnum{

	INSERTCELLDATA {
		public String getCode() {
			return "insertCellData";
		}

		public String getName() {
			return "新增单元格数据";
		}
	},
	UPDATECELLDATA {
		public String getCode() {
			return "updateCellData";
		}

		public String getName() {
			return "更新单元格数据";
		}
	},
	UPDATEBORDERINFO {
		public String getCode() {
			return "updateBorderInfo";
		}

		public String getName() {
			return "更新边框信息";
		}
	},
	UPDATEROWHIDDEN {
		public String getCode() {
			return "updateRowhidden";
		}

		public String getName() {
			return "更新隐藏行";
		}
	},
	UPDATEROWLEN {
		public String getCode() {
			return "updateRowLen";
		}

		public String getName() {
			return "更新行高信息";
		}
	},
	UPDATECOLLEN {
		public String getCode() {
			return "updateColLen";
		}

		public String getName() {
			return "更新列宽信息";
		}
	},
	UPDATECOLHIDDEN {
		public String getCode() {
			return "updateColhidden";
		}

		public String getName() {
			return "更新隐藏列";
		}
	},
	UPDATEFILTERROWHIDDEN {
		public String getCode() {
			return "updateFilterRowhidden";
		}

		public String getName() {
			return "更新过滤隐藏行";
		}
	},
	UPDATECONFIG {
		public String getCode() {
			return "updateConfig";
		}

		public String getName() {
			return "更新sheet配置信息";
		}
	},
	UPDATEHYPERLINK {
		public String getCode() {
			return "updateHyperline";
		}

		public String getName() {
			return "更新超链接信息";
		}
	},
	UPDATEIMAGES {
		public String getCode() {
			return "updateImages";
		}

		public String getName() {
			return "更新图片信息";
		}
	},
	UPDATEFROZEN {
		public String getCode() {
			return "updateFrozen";
		}

		public String getName() {
			return "更新冻结信息";
		}
	},
	UPDATEFILTERSELECT {
		public String getCode() {
			return "updateFilterSelect";
		}

		public String getName() {
			return "添加筛选";
		}
	},
	UPDATEFILTER {
		public String getCode() {
			return "updateFilter";
		}

		public String getName() {
			return "更新筛选条件";
		}
	},
	UPDATEDATAVERIFICATION {
		public String getCode() {
			return "updateDataVerification";
		}

		public String getName() {
			return "更新数据验证信息";
		}
	},
	CLEARFILTER {
		public String getCode() {
			return "clearFilter";
		}

		public String getName() {
			return "清除筛选";
		}
	},
	UPDATECALCCHAIN {
		public String getCode() {
			return "updateCalcChain";
		}

		public String getName() {
			return "更新公式链信息";
		}
	},
	UPDATELUCKYSHEETALTERNATEFORMATSAVE {
		public String getCode() {
			return "updateLuckysheetAlternateformatSave";
		}

		public String getName() {
			return "更新交替颜色";
		}
	},
	UPDATELUCKYSHEETCONDITIONFORMATSAVE {
		public String getCode() {
			return "updateLuckysheetConditionformatSave";
		}

		public String getName() {
			return "更新条件格式信息";
		}
	},
	ADDFC {
		public String getCode() {
			return "addFc";
		}

		public String getName() {
			return "新增公式链配置";
		}
	},
	DELFC {
		public String getCode() {
			return "delFc";
		}

		public String getName() {
			return "删除公式链配置";
		}
	},
	BATCHINSERTCELLDATA {
		public String getCode() {
			return "batchInsertCellData";
		}

		public String getName() {
			return "批量新增单元格数据";
		}
	},
	BATCHUPDATECELLDATA {
		public String getCode() {
			return "batchUpdateCellData";
		}

		public String getName() {
			return "批量更新单元格数据";
		}
	},
	BATCHDELETECELLDATA {
		public String getCode() {
			return "batchDeleteCellData";
		}

		public String getName() {
			return "批量删除单元格数据";
		}
	},
	DELETEROWCOLS {
		public String getCode() {
			return "deleteRowCols";
		}

		public String getName() {
			return "删除行列";
		}
	},
	ADDROWCOLS {
		public String getCode() {
			return "addRowCols";
		}

		public String getName() {
			return "新增行列";
		}
	},
	UPDATEROW {
		public String getCode() {
			return "updateRow";
		}

		public String getName() {
			return "更新行数";
		}
	},
	UPDATECOLUMN {
		public String getCode() {
			return "updateColumn";
		}

		public String getName() {
			return "更新列数";
		}
	},
	UPDATENAME {
		public String getCode() {
			return "updateName";
		}

		public String getName() {
			return "更新sheet名称";
		}
	},
	UPDATECOLOR {
		public String getCode() {
			return "updateColor";
		}

		public String getName() {
			return "更新sheet颜色";
		}
	},
	UPDATEHIDESTATUS {
		public String getCode() {
			return "updateHideStatus";
		}

		public String getName() {
			return "更新隐藏状态";
		}
	},
	UPDATEORDER{
		public String getCode() {
			return "updateOrder";
		}

		public String getName() {
			return "更新sheet顺序";
		}
	},
	DELSHEET{
		public String getCode() {
			return "delSheet";
		}

		public String getName() {
			return "删除sheet";
		}
	},
	DELSHEETCELL{
		public String getCode() {
			return "delSheetCell";
		}

		public String getName() {
			return "删除sheet单元格数据";
		}
	},
	ADDSHEET{
		public String getCode() {
			return "addSheet";
		}

		public String getName() {
			return "新增sheet";
		}
	},
	COPYSHEET{
		public String getCode() {
			return "copySheet";
		}

		public String getName() {
			return "复制sheet";
		}
	},
	INITSHEETCONFIG{
		public String getCode() {
			return "initSheetConfig";
		}

		public String getName() {
			return "初始化配置信息";
		}
	},
	DELDOC{
		public String getCode() {
			return "delDoc";
		}

		public String getName() {
			return "删除文档";
		}
	},
	INSERTCHART{
		public String getCode() {
			return "insertChart";
		}

		public String getName() {
			return "插入图表";
		}
	},
	MOVECHART{
		public String getCode() {
			return "moveChart";
		}

		public String getName() {
			return "移动图表位置或者改变图表大小";
		}
	},
	CHANGECHARTRANGE{
		public String getCode() {
			return "changeChartRange";
		}

		public String getName() {
			return "改变图表选区";
		}
	},
	UPDATECHART{
		public String getCode() {
			return "updateChart";
		}

		public String getName() {
			return "修改图表设置";
		}
	},
	DELETECHART{
		public String getCode() {
			return "deleteChart";
		}

		public String getName() {
			return "删除图表";
		}
	},
}
