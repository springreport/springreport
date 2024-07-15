package com.springreport.base;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;

/**  
 * @ClassName: PageEntity
 * @Description: 分页参数用基底实体类
 * @author caiyang
 * @date 2020-05-29 11:04:00 
*/  
@Data
public class PageEntity extends BaseEntity{

	/**
	 *	 每页显示的条数
	 */
	@TableField(exist = false)
	private Integer pageSize = 10;

	/**
	 * 	当前页数
	 */
	@TableField(exist = false)
	private int currentPage = 1;
	
	/**
	 * 	偏移量
	 */
	@TableField(exist = false)
	private int offSet = 1;

	/**
	 * 	表格数据行
	 */
	@TableField(exist = false)
	private List<?> data;

	/**
	 * 总条数
	 */
	@TableField(exist = false)
	private Long total = (long) 0;
	
	 public int getOffSet() {
	      return (currentPage-1)*pageSize;
	 }

}
