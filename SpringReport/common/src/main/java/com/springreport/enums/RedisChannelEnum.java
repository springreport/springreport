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
 * @ClassName: QueueChannelEnum
 * @Description: 消息队列频道名称
 * @author caiyang
 * @date 2023-02-07 04:36:31 
*/  
public enum RedisChannelEnum implements BaseCharEnum{

	UPDATEONLINEREPORT {
		public String getCode() {
			return "updateOnlineReportChannel";
		}

		public String getName() {
			return "编辑在线文档频道";
		}
	},
	SCREENREFSHCHANNEL{
		public String getCode() {
			return "screenRefreshChannel";
		}

		public String getName() {
			return "大屏刷新频道";
		}
	},
	SCREENCOMPONENTREFSHCHANNEL{
		public String getCode() {
			return "screenComponentRefreshChannel";
		}

		public String getName() {
			return "大屏组件刷新频道";
		}
	},
	COEDITDOCUMENT {
		public String getCode() {
			return "coeditChannel";
		}

		public String getName() {
			return "编辑在线文档频道";
		}
	},
}
