package com.springreport.enums;

import com.springreport.base.BaseCharEnum;

/**  
 * @ClassName: ComponentTypeEnum
 * @Description: 组件类型枚举类
 * @author caiyang
 * @date 2021-08-02 04:57:07 
*/  
public enum ComponentTypeEnum implements BaseCharEnum{

	PANEL {
		public String getCode() {
			return "panel";
		}

		public String getName() {
			return "画布";
		}
	},
	TEXT {
		public String getCode() {
			return "text";
		}

		public String getName() {
			return "文本";
		}
	},
	PICTURE {
		public String getCode() {
			return "picture";
		}

		public String getName() {
			return "图片";
		}
	},
	HISTOGRAM {
		public String getCode() {
			return "histogram";
		}

		public String getName() {
			return "柱状图";
		}
	},
	LINECHART {
		public String getCode() {
			return "lineChart";
		}

		public String getName() {
			return "折线图";
		}
	},
	PIECHART {
		public String getCode() {
			return "pieChart";
		}

		public String getName() {
			return "饼图";
		}
	},
	TABLE {
		public String getCode() {
			return "table";
		}

		public String getName() {
			return "表格";
		}
	},
	DATE {
		public String getCode() {
			return "date";
		}

		public String getName() {
			return "日期";
		}
	},
	MAP {
		public String getCode() {
			return "map";
		}

		public String getName() {
			return "地图";
		}
	},
	MAP3D {
		public String getCode() {
			return "map3d";
		}

		public String getName() {
			return "3d地图";
		}
	},
	PIE3DCHART {
		public String getCode() {
			return "pie3dChart";
		}

		public String getName() {
			return "3d饼图";
		}
	},
	GAUGE{
		public String getCode() {
			return "gauge";
		}

		public String getName() {
			return "仪表盘";
		}
	},
	TAB{
		public String getCode() {
			return "tab";
		}

		public String getName() {
			return "轮播组件";
		}
	},
	VIDEO{
		public String getCode() {
			return "video";
		}

		public String getName() {
			return "视频组件";
		}
	},
	HISTOGRAMLINECHART{
		public String getCode() {
			return "histogramLineChart";
		}

		public String getName() {
			return "折柱图";
		}
	},
	MAPSCATTER{
		public String getCode() {
			return "mapScatter";
		}

		public String getName() {
			return "散点地图";
		}
	},
	MAPMIGRATE{
		public String getCode() {
			return "mapMigrate";
		}

		public String getName() {
			return "迁徙地图";
		}
	},
	DELETE {
		public String getCode() {
			return "delete";
		}

		public String getName() {
			return "删除的组件";
		}
	}
}
