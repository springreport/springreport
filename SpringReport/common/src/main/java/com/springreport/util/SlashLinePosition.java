package com.springreport.util;

import lombok.Data;

@Data
public class SlashLinePosition {

	public SlashLinePosition(int startX,int startY,int endX,int endY,int r,int c,int endr,int endc)
	{
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.r = r;
		this.c = c;
		this.endr = endr;
		this.endc = endc;
	}
	
	/**  
	 * @Fields startX : 斜线起点x轴坐标
	 * @author caiyang
	 * @date 2023-11-20 08:13:52 
	 */  
	private int startX;
	
	/**  
	 * @Fields startY : 斜线起点y轴坐标
	 * @author caiyang
	 * @date 2023-11-20 08:14:28 
	 */  
	private int startY;
	
	/**  
	 * @Fields endX : 斜线结束点x轴坐标
	 * @author caiyang
	 * @date 2023-11-20 08:14:48 
	 */  
	private int endX;
	
	/**  
	 * @Fields endY : 斜线结束点y轴坐标
	 * @author caiyang
	 * @date 2023-11-20 08:15:06 
	 */  
	private int endY;
	
	/**  
	 * @Fields r : 起点单元格横坐标
	 * @author caiyang
	 * @date 2023-11-21 11:43:42 
	 */  
	private int r;
	
	/**  
	 * @Fields c : 起点单元格纵坐标
	 * @author caiyang
	 * @date 2023-11-21 11:44:07 
	 */  
	private int c;
	
	/**  
	 * @Fields rs : 行高
	 * @author caiyang
	 * @date 2023-11-21 11:44:29 
	 */  
	private int endr;
	
	/**  
	 * @Fields cs : 列宽
	 * @author caiyang
	 * @date 2023-11-21 11:44:40 
	 */  
	private int endc;
	
}
