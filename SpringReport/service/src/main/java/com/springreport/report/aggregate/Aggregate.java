package com.springreport.report.aggregate;


/**  
 * @ClassName: Aggregate
 * @Description: 数据聚合
 * @author caiyang
 * @date 2021-05-27 04:53:27 
*/  
public abstract class Aggregate<T,S,M,N,K> {

	/**
	*<p>Title: aggregate</p>
	*<p>Description: 数据聚合</p>
	* @author caiyang
	* @param reportCell 当前单元格
	* @param bindData 前一个单元格关联的数据
	* @return
	*/
	public abstract S aggregate(T t,S s,M m,N n,K k);
}
