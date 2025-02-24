package com.springreport.enums;

import com.springreport.base.BaseCharEnum;

/**  
 * @ClassName: LuckySheetEnum
 * @Description: luckysheet属性枚举类
 * @author caiyang
 * @date 2022-02-01 12:39:06 
*/  
public enum LuckySheetPropsEnum implements BaseCharEnum {

	R {
		public String getCode() {
			return "r";
		}
	
		public String getName() {
			return "单元格横坐标";
		}
	},
	C {
		public String getCode() {
			return "c";
		}
	
		public String getName() {
			return "单元格纵坐标";
		}
	},
	CELLCONFIG{
		public String getCode() {
			return "v";
		}
	
		public String getName() {
			return "单元格配置";
		}
	},
	CELLVALUE{
		public String getCode() {
			return "v";
		}
	
		public String getName() {
			return "单元格值";
		}
	},
	POSTIL{
		public String getCode() {
			return "ps";
		}
	
		public String getName() {
			return "批注";
		}
	},
	CELLVALUEM{
		public String getCode() {
			return "m";
		}
	
		public String getName() {
			return "单元格值";
		}
	},
	COORDINATECONNECTOR{
		public String getCode() {
			return "_";
		}
	
		public String getName() {
			return "坐标连接符";
		}
	},
	CELLEXTEND{
		public String getCode() {
			return "cellExtend";
		}
	
		public String getName() {
			return "扩展方向";
		}
	},
	AGGREGATETYPE{
		public String getCode() {
			return "aggregateType";
		}
	
		public String getName() {
			return "聚合方式";
		}
	},
	CELLFUNCTION{
		public String getCode() {
			return "cellFunction";
		}
	
		public String getName() {
			return "汇总方式";
		}
	},
	DIGIT{
		public String getCode() {
			return "digit";
		}
	
		public String getName() {
			return "小数位数";
		}
	},
	THRESHOLD{
		public String getCode() {
			return "threshold";
		}
	
		public String getName() {
			return "预警阈值";
		}
	},
	WARNINGCOLOR{
		public String getCode() {
			return "warningColor";
		}
	
		public String getName() {
			return "预警颜色";
		}
	},
	WARNINGCONTENT{
		public String getCode() {
			return "warningContent";
		}
	
		public String getName() {
			return "预警内容";
		}
	},
	WARNING{
		public String getCode() {
			return "warning";
		}
	
		public String getName() {
			return "是否预警";
		}
	},
	WARNINGRULES{
		public String getCode() {
			return "warningRules";
		}
	
		public String getName() {
			return "预警规则";
		}
	},
	GROUPSUMMARYDEPENDENCYR{
		public String getCode() {
			return "groupSummaryDependencyr";
		}
	
		public String getName() {
			return "分组依赖行号";
		}
	},
	GROUPSUMMARYDEPENDENCYC{
		public String getCode() {
			return "groupSummaryDependencyc";
		}
	
		public String getName() {
			return "分组依赖列号";
		}
	},
	MERGECELLS{
		public String getCode() {
			return "mc";
		}
	
		public String getName() {
			return "合并单元格";
		}
	},
	ROWSPAN{
		public String getCode() {
			return "rs";
		}
	
		public String getName() {
			return "合并行数";
		}
	},
	COLSPAN{
		public String getCode() {
			return "cs";
		}
	
		public String getName() {
			return "合并列数";
		}
	},
	FUNCTION{
		public String getCode() {
			return "f";
		}
	
		public String getName() {
			return "函数配置";
		}
	},
	LINKADDRESS{
		public String getCode() {
			return "linkAddress";
		}
	
		public String getName() {
			return "超链接";
		}
	},
	LINKTYPE{
		public String getCode() {
			return "linkType";
		}
	
		public String getName() {
			return "超链接类型";
		}
	},
	LINKTOOLTIP{
		public String getCode() {
			return "linkTooltip";
		}
	
		public String getName() {
			return "超链接提示";
		}
	},
	ROWLEN{
		public String getCode() {
			return "rowlen";
		}
	
		public String getName() {
			return "行高";
		}
	},
	COLUMNLEN{
		public String getCode() {
			return "columnlen";
		}
	
		public String getName() {
			return "列宽";
		}
	},
	MERGECONFIG{
		public String getCode() {
			return "merge";
		}
	
		public String getName() {
			return "合并单元格配置";
		}
	},
	BACKGROUND{
		public String getCode() {
			return "bg";
		}
	
		public String getName() {
			return "单元格背景颜色";
		}
	},
	FONTFAMILY{
		public String getCode() {
			return "ff";
		}
	
		public String getName() {
			return "单元格字体";
		}
	},
	FONTCOLOR{
		public String getCode() {
			return "fc";
		}
	
		public String getName() {
			return "单元格字体颜色";
		}
	},
	FONTSIZE{
		public String getCode() {
			return "fs";
		}
	
		public String getName() {
			return "单元格字体大小";
		}
	},
	BOLD{
		public String getCode() {
			return "bl";
		}
	
		public String getName() {
			return "单元格字体加粗";
		}
	},
	ITALIC{
		public String getCode() {
			return "it";
		}
	
		public String getName() {
			return "单元格字体斜体";
		}
	},
	CANCELLINE{
		public String getCode() {
			return "cl";
		}
	
		public String getName() {
			return "单元格字体删除线";
		}
	},
	UNDERLINE{
		public String getCode() {
			return "un";
		}
	
		public String getName() {
			return "单元格字体下划线";
		}
	},
	HORIZONTALTYPE{
		public String getCode() {
			return "ht";
		}
	
		public String getName() {
			return "水平对齐";
		}
	},
	VERTICALTYPE{
		public String getCode() {
			return "vt";
		}
	
		public String getName() {
			return "垂直对齐";
		}
	},
	DATAFROM{
		public String getCode() {
			return "dataFrom";
		}
	
		public String getName() {
			return "数据来源";
		}
	},
	ISGROUPMERGE{
		public String getCode() {
			return "isGroupMerge";
		}
	
		public String getName() {
			return "分组单元格是否合一";
		}
	},
	CELLTYPE{
		public String getCode() {
			return "ct";
		}
	
		public String getName() {
			return "单元格格式";
		}
	},
	TYPE{
		public String getCode() {
			return "t";
		}
	
		public String getName() {
			return "单元格值格式";
		}
	},
	INLINESTR{
		public String getCode() {
			return "inlineStr";
		}
	
		public String getName() {
			return "单元格值格式内联字符串";
		}
	},
	STRING{
		public String getCode() {
			return "s";
		}
	
		public String getName() {
			return "单元格值格式纯字符串";
		}
	},
	BORDERINFO{
		public String getCode() {
			return "borderInfo";
		}
	
		public String getName() {
			return "边框信息";
		}
	},
	BORDERRANGE{
		public String getCode() {
			return "range";
		}
	
		public String getName() {
			return "边框范围";
		}
	},
	BORDERCOLUMNRANGE{
		public String getCode() {
			return "column";
		}
	
		public String getName() {
			return "边框列范围";
		}
	},
	BORDERROWRANGE{
		public String getCode() {
			return "row";
		}
	
		public String getName() {
			return "边框行范围";
		}
	},
	BORDERTYPE{
		public String getCode() {
			return "borderType";
		}
	
		public String getName() {
			return "边框类型";
		}
	},
	BORDERCOLOR{
		public String getCode() {
			return "color";
		}
	
		public String getName() {
			return "边框颜色";
		}
	},
	RANGETYPE{
		public String getCode() {
			return "rangeType";
		}
	
		public String getName() {
			return "范围类型";
		}
	},
	RANGETYPECELL{
		public String getCode() {
			return "cell";
		}
	
		public String getName() {
			return "单元格类型";
		}
	},
	RANGECOLOR{
		public String getCode() {
			return "color";
		}
	
		public String getName() {
			return "边框颜色";
		}
	},
	RANGESTYLE{
		public String getCode() {
			return "style";
		}
	
		public String getName() {
			return "边框格式";
		}
	},
	TEXTWRAPMODE{
		public String getCode() {
			return "tb";
		}
	
		public String getName() {
			return "换行方式";
		}
	},
	RANGECELLVALUE{
		public String getCode() {
			return "value";
		}
	
		public String getName() {
			return "单元格边框类型值";
		}
	},
	CELLFORMAT{
		public String getCode() {
			return "fa";
		}
	
		public String getName() {
			return "单元格格式";
		}
	},
	ISDICT{
		public String getCode() {
			return "isDict";
		}
	
		public String getName() {
			return "是否是数据字典";
		}
	},
	DATASOURCEID{
		public String getCode() {
			return "datasourceId";
		}
	
		public String getName() {
			return "数据源id";
		}
	},
	DICTTYPE{
		public String getCode() {
			return "dictType";
		}
	
		public String getName() {
			return "字典类型";
		}
	},
	GROUPPROPERTY{
		public String getCode() {
			return "groupProperty";
		}
	
		public String getName() {
			return "分组属性";
		}
	},
	COLHIDDEN{
		public String getCode() {
			return "colhidden";
		}
	
		public String getName() {
			return "隐藏列";
		}
	},
	ROWHIDDEN{
		public String getCode() {
			return "rowhidden";
		}
	
		public String getName() {
			return "隐藏行";
		}
	},
	CELLCONDITIONS{
		public String getCode() {
			return "cellconditions";
		}
	
		public String getName() {
			return "单元格过滤条件";
		}
	},
	FILTERTYPE{
		public String getCode() {
			return "filterType";
		}
	
		public String getName() {
			return "过滤条件类型";
		}
	},
	CELLHIDDENCONDITIONS{
		public String getCode() {
			return "cellHiddenConditions";
		}
	
		public String getName() {
			return "单元格隐藏条件";
		}
	},
	CELLHIDDENTYPE{
		public String getCode() {
			return "hiddenType";
		}
	
		public String getName() {
			return "单元格隐藏条件类型";
		}
	},
	SERIESNAME{
		public String getCode() {
			return "seriesName";
		}
	
		public String getName() {
			return "图表系列名称";
		}
	},
	ISDRILL{
		public String getCode() {
			return "isDrill";
		}
	
		public String getName() {
			return "是否下钻";
		}
	},
	DRILLID{
		public String getCode() {
			return "drillId";
		}
	
		public String getName() {
			return "下钻报表id";
		}
	},
	DRILLATTRS{
		public String getCode() {
			return "drillAttrs";
		}
	
		public String getName() {
			return "下钻参数属性";
		}
	},
	DRILLPARAMS{
		public String getCode() {
			return "drillParams";
		}
	
		public String getName() {
			return "下钻参数";
		}
	},
	UNITTRANSFER{
		public String getCode() {
			return "unitTransfer";
		}
	
		public String getName() {
			return "是否单位转换";
		}
	},
	TRANSFERTYPE{
		public String getCode() {
			return "transferType";
		}
	
		public String getName() {
			return "转换类型";
		}
	},
	MULTIPLE{
		public String getCode() {
			return "multiple";
		}
	
		public String getName() {
			return "倍数";
		}
	},
	ISSUBTOTAL{
		public String getCode() {
			return "isSubtotal";
		}
	
		public String getName() {
			return "是否追加小计";
		}
	},
	SUBTOTALCELLS{
		public String getCode() {
			return "subTotalCells";
		}
	
		public String getName() {
			return "小计单元格";
		}
	},
	SUBTOTALCALC{
		public String getCode() {
			return "subTotalCalc";
		}
	
		public String getName() {
			return "分组小计链";
		}
	},
	SUBTOTALATTRS{
		public String getCode() {
			return "subTotalAttrs";
		}
	
		public String getName() {
			return "小计属性";
		}
	},
	SUB{
		public String getCode() {
			return "sub";
		}
	
		public String getName() {
			return "下标";
		}
	},
	SUP{
		public String getCode() {
			return "sup";
		}
	
		public String getName() {
			return "上标";
		}
	},
	CELLFILLTYPE{
		public String getCode() {
			return "cellFillType";
		}
	
		public String getName() {
			return "数据填充方式";
		}
	},
	LINESPACE{
		public String getCode() {
			return "ls";
		}
	
		public String getName() {
			return "换行行间距";
		}
	},
	
}
