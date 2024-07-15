package com.springreport.report.calculate;



/**
* <p>Title: Calculate</p>
* <p>Description: 计算数据</p>
* @author caiyang
* @date 2021年6月11日18:35:04
*/
public abstract class Calculate<T> {

	/**
	*<p>Title: calculate</p>
	*<p>Description: 计算数据</p>
	* @author caiyang
	* @param datas
	* @return
	*/
	public abstract String calculate(T bindData);
}
