package com.springreport.util;

import lombok.Data;

@Data
public class SlashLineText {

	public SlashLineText(int startX,int startY,int endX,int endY,String content,
			int r,int c,int rs,int cs)
	{
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.content = content;
		this.r = r;
		this.c = c;
		this.rs = rs;
		this.cs = cs;
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
	 * @Fields content : 显示的内容
	 * @author caiyang
	 * @date 2023-11-21 11:07:30 
	 */  
	private String content;
	
	
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
	private int rs;
	
	/**  
	 * @Fields cs : 列宽
	 * @author caiyang
	 * @date 2023-11-21 11:44:40 
	 */  
	private int cs;
	
}
