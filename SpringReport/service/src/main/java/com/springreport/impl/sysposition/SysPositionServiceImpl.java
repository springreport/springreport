package com.springreport.impl.sysposition;

import com.springreport.entity.sysposition.SysPosition;
import com.springreport.mapper.sysposition.SysPositionMapper;
import com.springreport.api.sysposition.ISysPositionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

 /**  
* @Description: SysPosition服务实现
* @author 
* @date 2022-06-24 10:54:59
* @version V1.0  
 */
@Service
public class SysPositionServiceImpl extends ServiceImpl<SysPositionMapper, SysPosition> implements ISysPositionService {
  
	/** 
	* @Title: tablePagingQuery 
	* @Description: 表格分页查询
	* @param @param model
	* @return BaseEntity 
	* @author 
	* @throws 
	*/ 
	@Override
	public void tablePagingQuery(SysPosition model) {
		QueryWrapper<SysPosition> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("area_code", "100000");
		SysPosition china = this.getOne(queryWrapper,false);
		List<String> notFound = new ArrayList<>();
//		notFound.add("import geo" + china.getAreaCode() + " from '../static/map/"+china.getAreaCode()+".json'");
//		notFound.add("echarts.registerMap(" + "'geo"+china.getAreaCode() +"'"+ ", geo"+china.getAreaCode()+")");
		notFound.add("{value:'"+china.getAreaCode()+"',label:'"+china.getAreaName()+"'},");
		queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("level", 1);
		queryWrapper.eq("area_index", china.getAreaCode());
		List<SysPosition> provinces = this.baseMapper.selectList(queryWrapper);
		QueryWrapper<SysPosition> cityQueryWrapper = null;
		for (int i = 0; i < provinces.size(); i++) {
//			notFound.add("import geo" + provinces.get(i).getAreaCode() + " from '../static/map/"+provinces.get(i).getAreaCode()+".json'");
//			notFound.add("echarts.registerMap(" + "'geo"+provinces.get(i).getAreaCode() +"'"+ ", geo"+provinces.get(i).getAreaCode()+")");
			notFound.add("{value:'"+provinces.get(i).getAreaCode()+"',label:'"+provinces.get(i).getAreaName()+"'},");
			cityQueryWrapper = new QueryWrapper<>();
			cityQueryWrapper.eq("level", 2);
			cityQueryWrapper.like("area_index", provinces.get(i).getAreaCode());
//			notFound.add(china.getAreaCode()+":"+"{"+provinces.get(i).getAreaName()+":"+provinces.get(i).getAreaName()+"},");
//			List<SysPosition> cities = this.baseMapper.selectList(cityQueryWrapper);
//			for (int j = 0; j < cities.size(); j++) {
//				notFound.add("{value:'"+cities.get(j).getAreaCode()+"',label:'"+cities.get(j).getAreaName()+"'},");
//				notFound.add("import geo" + cities.get(j).getAreaCode() + " from '../static/map/"+cities.get(j).getAreaCode()+".json'");
//				notFound.add("echarts.registerMap(" + "'geo"+cities.get(j).getAreaCode() +"'"+ ", geo"+cities.get(j).getAreaCode()+")");
//			}
		}
		for (int i = 0; i < provinces.size(); i++) {
			cityQueryWrapper = new QueryWrapper<>();
			cityQueryWrapper.eq("level", 2);
			cityQueryWrapper.like("area_index", provinces.get(i).getAreaCode());
			List<SysPosition> cities = this.baseMapper.selectList(cityQueryWrapper);
//			StringBuilder sb = new StringBuilder();
//			sb.append(provinces.get(i).getAreaCode()).append(":{");
			for (int j = 0; j < cities.size(); j++) {
				notFound.add("{value:'"+cities.get(j).getAreaCode()+"',label:'"+cities.get(j).getAreaName()+"'},");
			}
//			sb.append("}");
//			notFound.add(sb.toString());
			
		}
		
		for (int i = 0; i < notFound.size(); i++) {
			System.out.println(notFound.get(i));
			
		}
	}

}