package com.springreport.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFLineProperties;
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.AxisCrossBetween;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.BarDirection;
import org.apache.poi.xddf.usermodel.chart.BarGrouping;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.RadarStyle;
import org.apache.poi.xddf.usermodel.chart.XDDFAreaChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFBarChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFDoughnutChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFLineChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFPieChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFRadarChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTHoleSize;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springreport.enums.YesNoEnum;

/**  
 * @ClassName: ExcelChartUtil
 * @Description: excel导出图表工具类
 * @author caiyang
 * @date 2024-02-09 06:05:10 
*/ 
public class ExcelChartUtil {
	
	/**  
	 * @MethodName: createLineChart
	 * @Description: 折线图
	 * @author caiyang
	 * @param sheet
	 * @param chartCell
	 * @param chartOptions void
	 * @date 2024-02-17 09:09:21 
	 */ 
	public static void createLineChart(XSSFSheet sheet,JSONObject chartCell,JSONObject chartOptions,boolean smooth,boolean showLabel,int isCoedit) {
		//创建一个画布
		XSSFDrawing drawing = sheet.createDrawingPatriarch();
		int r = chartCell.getIntValue("r");
		int rs = chartCell.getIntValue("rs");
		int c = chartCell.getIntValue("c");
		int cs = chartCell.getIntValue("cs");
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,c,r,c+cs,r+rs);
		//创建一个chart对象
		XSSFChart chart = drawing.createChart(anchor);
		chart.setPlotOnlyVisibleCells(false);
		//标题
		String title = getTitle(chartOptions);
		if(StringUtil.isNotEmpty(title))
		{
			chart.setTitleText(title);
			//标题覆盖
			chart.setTitleOverlay(false);
		}
		List<JSONArray> chartData = groupChartData(chartOptions);
		if(ListUtil.isEmpty(chartData)) {
			return;
		}
		JSONObject chartLegend = getLegend(chartOptions,chartData);
		boolean show = chartLegend.getBooleanValue("show");
		if(show)
		{
			XDDFChartLegend legend = chart.getOrAddLegend();
			String position = chartLegend.getString("position");
			switch (position) {
			case "top":
				legend.setPosition(LegendPosition.TOP);
				break;
			case "left":
				legend.setPosition(LegendPosition.LEFT);
				break;
			case "right-top":
				legend.setPosition(LegendPosition.TOP_RIGHT);
				break;
			case "right":
				legend.setPosition(LegendPosition.RIGHT);
				break;
			case "bottom":
				legend.setPosition(LegendPosition.BOTTOM);
				break;
			default:
				legend.setPosition(LegendPosition.BOTTOM);
				break;
			}
		}
		JSONObject axis = getAxisData(chartOptions,"column",chartData);
//		//分类轴标(X轴),标题位置
		XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
		//值(Y轴)轴,标题位置
		XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
		JSONArray axisData = axis.getJSONArray("data");
		XDDFCategoryDataSource category = XDDFDataSourcesFactory.fromArray(axisData.toArray(new String[axisData.size()]));
		List<XDDFNumericalDataSource<Double>> datas = getSeriesArea(sheet, chartOptions, axisData.size(),isCoedit);
		if(ListUtil.isNotEmpty(datas))
		{
			JSONArray legendData = chartLegend.getJSONArray("data");
			XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
			for (int i = 0; i < datas.size(); i++) {
				XDDFLineChartData.Series series = (XDDFLineChartData.Series) data.addSeries(category, datas.get(i));
				series.setSmooth(false);
				if(ListUtil.isNotEmpty(legendData) && i < legendData.size())
				{
					series.setTitle(legendData.getString(i), null);
					series.setSmooth(smooth);
					if(showLabel)
					{
						series.setShowLeaderLines(true);
						chart.getCTChart().getPlotArea().getLineChartArray(0).getSerArray(i).getDLbls().addNewShowVal().setVal(true);
			            chart.getCTChart().getPlotArea().getLineChartArray(0).getSerArray(i).getDLbls().addNewShowLegendKey().setVal(false);
			            chart.getCTChart().getPlotArea().getLineChartArray(0).getSerArray(i).getDLbls().addNewShowCatName().setVal(false);
			            chart.getCTChart().getPlotArea().getLineChartArray(0).getSerArray(i).getDLbls().addNewShowSerName().setVal(false);
					}
				}else {
					series.setTitle("系列"+i, null);
				}
			}
			chart.plot(data);
			fixChart(chart);
		}
	}
	
	/**  
	 * @MethodName: createAreaChart
	 * @Description: 面积图
	 * @author caiyang
	 * @param sheet
	 * @param chartCell
	 * @param chartOptions void
	 * @date 2024-02-18 06:39:43 
	 */ 
	public static void createAreaChart(XSSFSheet sheet,JSONObject chartCell,JSONObject chartOptions,int isCoedit) {
		//创建一个画布
		XSSFDrawing drawing = sheet.createDrawingPatriarch();
		int r = chartCell.getIntValue("r");
		int rs = chartCell.getIntValue("rs");
		int c = chartCell.getIntValue("c");
		int cs = chartCell.getIntValue("cs");
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,c,r,c+cs,r+rs);
		//创建一个chart对象
		XSSFChart chart = drawing.createChart(anchor);
		chart.setPlotOnlyVisibleCells(false);
		//标题
		String title = getTitle(chartOptions);
		if(StringUtil.isNotEmpty(title))
		{
			chart.setTitleText(title);
			//标题覆盖
			chart.setTitleOverlay(false);
		}
		List<JSONArray> chartData = groupChartData(chartOptions);
		JSONObject chartLegend = getLegend(chartOptions,chartData);
		boolean show = chartLegend.getBooleanValue("show");
		if(show)
		{
			XDDFChartLegend legend = chart.getOrAddLegend();
			String position = chartLegend.getString("position");
			switch (position) {
			case "top":
				legend.setPosition(LegendPosition.TOP);
				break;
			case "left":
				legend.setPosition(LegendPosition.LEFT);
				break;
			case "right-top":
				legend.setPosition(LegendPosition.TOP_RIGHT);
				break;
			case "right":
				legend.setPosition(LegendPosition.RIGHT);
				break;
			case "bottom":
				legend.setPosition(LegendPosition.BOTTOM);
				break;
			default:
				legend.setPosition(LegendPosition.BOTTOM);
				break;
			}
		}
		JSONObject axis = getAxisData(chartOptions,"column",chartData);
		//分类轴标(X轴),标题位置
		XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
		//值(Y轴)轴,标题位置
		XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
		JSONArray axisData = axis.getJSONArray("data");
		XDDFCategoryDataSource category = XDDFDataSourcesFactory.fromArray(axisData.toArray(new String[axisData.size()]));
		List<XDDFNumericalDataSource<Double>> datas = getSeriesArea(sheet, chartOptions, axisData.size(),isCoedit);
		if(ListUtil.isNotEmpty(datas))
		{
			JSONArray legendData = chartLegend.getJSONArray("data");
			XDDFAreaChartData data = (XDDFAreaChartData) chart.createData(ChartTypes.AREA, bottomAxis, leftAxis);
			for (int i = 0; i < datas.size(); i++) {
				XDDFAreaChartData.Series series = (XDDFAreaChartData.Series) data.addSeries(category, datas.get(i));
				if(ListUtil.isNotEmpty(legendData) && i < legendData.size())
				{
					series.setTitle(legendData.getString(i), null);
				}else {
					series.setTitle("系列"+i, null);
				}
			}
			chart.plot(data);
			fixChart(chart);
		}
	}
	
	 /**
     * 柱状图
     * @param sheet
     * @param chartPosition
     * @param pieChart
     */
    public static void createBar(XSSFSheet sheet,JSONObject chartCell,JSONObject chartOptions,String type,boolean stacked,int isCoedit){
		//创建一个画布
		XSSFDrawing drawing = sheet.createDrawingPatriarch();
		int r = chartCell.getIntValue("r");
		int rs = chartCell.getIntValue("rs");
		int c = chartCell.getIntValue("c");
		int cs = chartCell.getIntValue("cs");
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,c,r,c+cs,r+rs);
		//创建一个chart对象
		XSSFChart chart = drawing.createChart(anchor);
		chart.setPlotOnlyVisibleCells(false);
		//标题
		String title = getTitle(chartOptions);
		if(StringUtil.isNotEmpty(title))
		{
			chart.setTitleText(title);
			//标题覆盖
			chart.setTitleOverlay(false);
		}
		List<JSONArray> chartData = groupChartData(chartOptions);
		if(ListUtil.isEmpty(chartData)) {
			return;
		}
		JSONObject chartLegend = getLegend(chartOptions,chartData);
		boolean show = chartLegend.getBooleanValue("show");
		if(show)
		{
			XDDFChartLegend legend = chart.getOrAddLegend();
			String position = chartLegend.getString("position");
			switch (position) {
			case "top":
				legend.setPosition(LegendPosition.TOP);
				break;
			case "left":
				legend.setPosition(LegendPosition.LEFT);
				break;
			case "right-top":
				legend.setPosition(LegendPosition.TOP_RIGHT);
				break;
			case "right":
				legend.setPosition(LegendPosition.RIGHT);
				break;
			case "bottom":
				legend.setPosition(LegendPosition.BOTTOM);
				break;
			default:
				legend.setPosition(LegendPosition.BOTTOM);
				break;
			}
		}
		JSONObject axis = getAxisData(chartOptions,type,chartData);
//		//分类轴标(X轴),标题位置
		XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
		//值(Y轴)轴,标题位置
		XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
		leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
		JSONArray axisData = axis.getJSONArray("data");
		XDDFCategoryDataSource category = XDDFDataSourcesFactory.fromArray(axisData.toArray(new String[axisData.size()]));
		List<XDDFNumericalDataSource<Double>> datas = getSeriesArea(sheet, chartOptions, axisData.size(),isCoedit);
		if(ListUtil.isNotEmpty(datas))
		{
			JSONArray legendData = chartLegend.getJSONArray("data");
			XDDFBarChartData data = (XDDFBarChartData) chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
			// 是否设置不同的颜色 false就行
			data.setVaryColors(false);
			if("column".equals(type))
			{
				data.setBarDirection(BarDirection.COL);
			}else {
				data.setBarDirection(BarDirection.BAR);
			}
			if(stacked)
			{
				data.setBarGrouping(BarGrouping.STACKED); // 设置堆叠样式	
				data.setOverlap((byte)100);
			}
			for (int i = 0; i < datas.size(); i++) {
				XDDFBarChartData.Series series = (XDDFBarChartData.Series) data.addSeries(category, datas.get(i));
				if(ListUtil.isNotEmpty(legendData) && i < legendData.size())
				{
					series.setTitle(legendData.getString(i), null);
//					series.setShowLeaderLines(true);
//					chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(i).getDLbls().addNewShowVal().setVal(true);
//		            chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(i).getDLbls().addNewShowLegendKey().setVal(false);
//		            chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(i).getDLbls().addNewShowCatName().setVal(false);
//		            chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(i).getDLbls().addNewShowSerName().setVal(false);
				}else {
					series.setTitle("系列"+i, null);
				}
			}
			chart.plot(data);
			fixChart(chart);
		}
    }
    
    /**  
     * @MethodName: createPie
     * @Description: 饼图
     * @author caiyang
     * @param sheet
     * @param chartCell
     * @param chartOptions
     * @param type void
     * @date 2024-02-19 07:47:32 
     */ 
    public static void createPie(XSSFSheet sheet,JSONObject chartCell,JSONObject chartOptions,int isCoedit){
    	//创建一个画布
    	XSSFDrawing drawing = sheet.createDrawingPatriarch();
		int r = chartCell.getIntValue("r");
		int rs = chartCell.getIntValue("rs");
		int c = chartCell.getIntValue("c");
		int cs = chartCell.getIntValue("cs");
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,c,r,c+cs,r+rs);
		//创建一个chart对象
		XSSFChart chart = drawing.createChart(anchor);
		chart.setPlotOnlyVisibleCells(false);
		//标题
		String title = getTitle(chartOptions);
		if(StringUtil.isNotEmpty(title))
		{
			chart.setTitleText(title);
			//标题覆盖
			chart.setTitleOverlay(false);
		}
		List<JSONArray> chartData = groupChartData(chartOptions);
		if(ListUtil.isEmpty(chartData)) {
			return;
		}
		JSONObject chartLegend = getLegend(chartOptions,chartData);
		boolean show = chartLegend.getBooleanValue("show");
		if(show)
		{
			XDDFChartLegend legend = chart.getOrAddLegend();
			String position = chartLegend.getString("position");
			switch (position) {
			case "top":
				legend.setPosition(LegendPosition.TOP);
				break;
			case "left":
				legend.setPosition(LegendPosition.LEFT);
				break;
			case "right-top":
				legend.setPosition(LegendPosition.TOP_RIGHT);
				break;
			case "right":
				legend.setPosition(LegendPosition.RIGHT);
				break;
			case "bottom":
				legend.setPosition(LegendPosition.BOTTOM);
				break;
			default:
				legend.setPosition(LegendPosition.BOTTOM);
				break;
			}
		}
		boolean rangeConfigCheck = chartOptions.getBooleanValue("rangeConfigCheck");
		JSONArray names = getAxesData(chartData,rangeConfigCheck);
		XDDFCategoryDataSource category = XDDFDataSourcesFactory.fromArray(names.toArray(new String[names.size()]));
		List<XDDFNumericalDataSource<Double>> datas = getSeriesArea(sheet, chartOptions, names.size(),isCoedit);
		if(ListUtil.isNotEmpty(datas)) {
			XDDFPieChartData data = (XDDFPieChartData)chart.createData(ChartTypes.PIE, null, null);
			XDDFPieChartData.Series series = (XDDFPieChartData.Series)data.addSeries(category, datas.get(0));
			chart.plot(data);
			CTDLbls dLbls = chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).addNewDLbls();
	        dLbls.addNewShowVal().setVal(false);
	        dLbls.addNewShowLegendKey().setVal(false);
	        dLbls.addNewShowCatName().setVal(true);// 类别名称
	        dLbls.addNewShowSerName().setVal(false);
	        dLbls.addNewShowPercent().setVal(true);// 百分比
	        dLbls.addNewShowLeaderLines().setVal(true);// 引导线
	        dLbls.setSeparator("\n");// 分隔符为分行符
		}
    }
    
    /**  
     * @MethodName: createDoughnut
     * @Description: 环形图
     * @author caiyang
     * @param sheet
     * @param chartCell
     * @param chartOptions void
     * @date 2024-02-20 04:42:34 
     */ 
    public static void createDoughnut(XSSFSheet sheet,JSONObject chartCell,JSONObject chartOptions,int isCoedit){
    	//创建一个画布
    	XSSFDrawing drawing = sheet.createDrawingPatriarch();
		int r = chartCell.getIntValue("r");
		int rs = chartCell.getIntValue("rs");
		int c = chartCell.getIntValue("c");
		int cs = chartCell.getIntValue("cs");
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,c,r,c+cs,r+rs);
		//创建一个chart对象
		XSSFChart chart = drawing.createChart(anchor);
		chart.setPlotOnlyVisibleCells(false);
		//标题
		String title = getTitle(chartOptions);
		if(StringUtil.isNotEmpty(title))
		{
			chart.setTitleText(title);
			//标题覆盖
			chart.setTitleOverlay(false);
		}
		List<JSONArray> chartData = groupChartData(chartOptions);
		if(ListUtil.isEmpty(chartData)) {
			return;
		}
		JSONObject chartLegend = getLegend(chartOptions,chartData);
		boolean show = chartLegend.getBooleanValue("show");
		if(show)
		{
			XDDFChartLegend legend = chart.getOrAddLegend();
			String position = chartLegend.getString("position");
			switch (position) {
			case "top":
				legend.setPosition(LegendPosition.TOP);
				break;
			case "left":
				legend.setPosition(LegendPosition.LEFT);
				break;
			case "right-top":
				legend.setPosition(LegendPosition.TOP_RIGHT);
				break;
			case "right":
				legend.setPosition(LegendPosition.RIGHT);
				break;
			case "bottom":
				legend.setPosition(LegendPosition.BOTTOM);
				break;
			default:
				legend.setPosition(LegendPosition.BOTTOM);
				break;
			}
		}
		boolean rangeConfigCheck = chartOptions.getBooleanValue("rangeConfigCheck");
		JSONArray names = getAxesData(chartData,rangeConfigCheck);
		XDDFCategoryDataSource category = XDDFDataSourcesFactory.fromArray(names.toArray(new String[names.size()]));
		List<XDDFNumericalDataSource<Double>> datas = getSeriesArea(sheet, chartOptions, names.size(),isCoedit);
		if(ListUtil.isNotEmpty(datas)) {
			XDDFDoughnutChartData data = (XDDFDoughnutChartData)chart.createData(ChartTypes.DOUGHNUT, null, null);
			XDDFDoughnutChartData.Series series = (XDDFDoughnutChartData.Series)data.addSeries(category, datas.get(0));
			chart.plot(data);
			CTDLbls dLbls = chart.getCTChart().getPlotArea().getDoughnutChartArray(0).getSerArray(0).addNewDLbls();
	        dLbls.addNewShowVal().setVal(false);
	        dLbls.addNewShowLegendKey().setVal(false);
	        dLbls.addNewShowCatName().setVal(true);// 类别名称
	        dLbls.addNewShowSerName().setVal(false);
	        dLbls.addNewShowPercent().setVal(true);// 百分比
	        dLbls.addNewShowLeaderLines().setVal(true);// 引导线
	        dLbls.setSeparator("\n");// 分隔符为分行符
	        CTHoleSize holeSize = chart.getCTChart().getPlotArea().getDoughnutChartArray(0).addNewHoleSize();
	        holeSize.setVal((short) 50);
		}
    }
    
    /**  
     * @MethodName: createRadar
     * @Description: 雷达图
     * @author caiyang
     * @param sheet
     * @param chartCell
     * @param chartOptions void
     * @date 2024-02-20 06:57:24 
     */ 
    public static void createRadar(XSSFSheet sheet,JSONObject chartCell,JSONObject chartOptions,int isCoedit){
    	//创建一个画布
    	XSSFDrawing drawing = sheet.createDrawingPatriarch();
		int r = chartCell.getIntValue("r");
		int rs = chartCell.getIntValue("rs");
		int c = chartCell.getIntValue("c");
		int cs = chartCell.getIntValue("cs");
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,c,r,c+cs,r+rs);
		//创建一个chart对象
		XSSFChart chart = drawing.createChart(anchor);
		chart.setPlotOnlyVisibleCells(false);
		//标题
		String title = getTitle(chartOptions);
		if(StringUtil.isNotEmpty(title))
		{
			chart.setTitleText(title);
			//标题覆盖
			chart.setTitleOverlay(false);
		}
		List<JSONArray> chartData = groupChartData(chartOptions);
		if(ListUtil.isEmpty(chartData)) {
			return;
		}
		JSONObject chartLegend = getLegend(chartOptions,chartData);
		boolean show = chartLegend.getBooleanValue("show");
		if(show)
		{
			XDDFChartLegend legend = chart.getOrAddLegend();
			String position = chartLegend.getString("position");
			switch (position) {
			case "top":
				legend.setPosition(LegendPosition.TOP);
				break;
			case "left":
				legend.setPosition(LegendPosition.LEFT);
				break;
			case "right-top":
				legend.setPosition(LegendPosition.TOP_RIGHT);
				break;
			case "right":
				legend.setPosition(LegendPosition.RIGHT);
				break;
			case "bottom":
				legend.setPosition(LegendPosition.BOTTOM);
				break;
			default:
				legend.setPosition(LegendPosition.BOTTOM);
				break;
			}
		}
		//分类轴标(X轴),标题位置
		XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
		//值(Y轴)轴,标题位置
		XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
		boolean rangeConfigCheck = chartOptions.getBooleanValue("rangeConfigCheck");
		JSONArray indicator = getAxesData(chartData,rangeConfigCheck);
		JSONObject pieSeries = getLegend(chartOptions, chartData);
		JSONArray names = pieSeries.getJSONArray("data");
		XDDFCategoryDataSource category = XDDFDataSourcesFactory.fromArray(indicator.toArray(new String[indicator.size()]));
		List<XDDFNumericalDataSource<Double>> datas = getSeriesArea(sheet, chartOptions, indicator.size(),isCoedit);
		if(ListUtil.isNotEmpty(datas)) {
			// 网格线
			XDDFShapeProperties yGridProperties = leftAxis.getOrAddMajorGridProperties();
			XDDFLineProperties yGridLine = new XDDFLineProperties();
			yGridLine.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(new byte[] {(byte)228,(byte)231,(byte)237})));
			yGridLine.setWidth(0.5);
			yGridProperties.setLineProperties(yGridLine);
			XDDFRadarChartData data = (XDDFRadarChartData) chart.createData(ChartTypes.RADAR, bottomAxis, leftAxis);
			data.setStyle(RadarStyle.MARKER);
			for (int i = 0; i < datas.size(); i++) {
				XDDFRadarChartData.Series series = (XDDFRadarChartData.Series) data.addSeries(category, datas.get(i));
				if(ListUtil.isNotEmpty(names) && i < names.size())
				{
					series.setTitle(names.getString(i), null);
				}else {
					series.setTitle("系列"+(i+1), null);
				}
			}
			chart.plot(data);
//			CTDLbls dLbls = chart.getCTChart().getPlotArea().getRadarChartArray(0).getSerArray(0).addNewDLbls();
//	        dLbls.addNewShowVal().setVal(true);
//	        dLbls.addNewShowLegendKey().setVal(false);
//	        dLbls.addNewShowCatName().setVal(false);// 类别名称
//	        dLbls.addNewShowSerName().setVal(false);
//	        dLbls.addNewShowPercent().setVal(false);// 百分比
//	        dLbls.addNewShowLeaderLines().setVal(true);// 引导线
			fixChart(chart);
		}
    }
    
    /**  
     * @MethodName: getTitle
     * @Description: 获取标题
     * @author caiyang
     * @param chartOptions
     * @return String
     * @date 2024-02-17 06:14:24 
     */ 
    public static String getTitle(JSONObject chartOptions) {
    	String result = "";
		JSONObject defaultOption = chartOptions.getJSONObject("defaultOption");
		if(defaultOption != null)
		{
			JSONObject spec = defaultOption.getJSONObject("spec");
			if(spec != null) {
				JSONObject title = spec.getJSONObject("title");
				if(title != null)
				{
					boolean show = title.getBooleanValue("visible");
					if(show) {
						result = title.getString("text");
					}
				}
			}
			
		}
		return result;
	}
    
    public static boolean getTitleShow(JSONObject chartOptions) {
    	boolean result = false;
    	JSONObject defaultOption = chartOptions.getJSONObject("defaultOption");
		if(defaultOption != null)
		{
			JSONObject spec = defaultOption.getJSONObject("spec");
			if(spec != null) {
				JSONObject title = spec.getJSONObject("title");
				if(title != null)
				{
					result = title.getBooleanValue("visible");
				}
			}
			
		}
    	return result;
    }
    
    /**  
     * @MethodName: getLegend
     * @Description: 获取图例信息
     * @author caiyang
     * @param chartOptions
     * @return JSONObject
     * @date 2024-02-17 06:32:58 
     */ 
    public static JSONObject getLegend(JSONObject chartOptions,List<JSONArray> chartDatas)
    {
    	JSONObject result = new JSONObject();
    	result.put("show", false);
    	JSONObject defaultOption = chartOptions.getJSONObject("defaultOption");
    	boolean rangeConfigCheck = chartOptions.getBooleanValue("rangeConfigCheck");
		if(defaultOption != null)
		{
			JSONObject spec = defaultOption.getJSONObject("spec");
			if(spec != null) {
				JSONObject legend = spec.getJSONObject("legends");
				if(legend != null)
				{
					boolean show = legend.getBooleanValue("visible");
					result.put("show", show);
					JSONArray legendData = getLegendData(chartDatas,rangeConfigCheck);
					result.put("data", legendData);
					String positionValue = legend.getString("position");
					result.put("position", positionValue);
					String direction = legend.getString("orient");
					result.put("direction", direction);
				}
			}
		}
		return result;
    }
    
    public static JSONObject getAxisData(JSONObject chartOptions,String type,List<JSONArray> chartData){
    	JSONObject result = new JSONObject();
    	result.put("show", true);
    	boolean rangeConfigCheck = chartOptions.getBooleanValue("rangeConfigCheck");
    	JSONArray data = getAxesData(chartData,rangeConfigCheck);
    	result.put("data", data);
		return result;
    }
    
    private static List<XDDFNumericalDataSource<Double>> getSeriesArea(XSSFSheet sheet,JSONObject chartOptions,int axisDataSize,int isCoedit){
    	List<XDDFNumericalDataSource<Double>> result = new ArrayList<>();
    	JSONArray rangeArray = chartOptions.getJSONArray("rangeArray");
    	boolean rangeConfigCheck = chartOptions.getBooleanValue("rangeConfigCheck");// transpose(switch row/column)
    	JSONObject rangeSplitArray = chartOptions.getJSONObject("rangeSplitArray");
    	String type = rangeSplitArray.getString("type");
    	if(ListUtil.isNotEmpty(rangeArray))
    	{
    		JSONObject rangeObj = rangeArray.getJSONObject(0);
    		JSONArray row = rangeObj.getJSONArray("row");
    		JSONArray column = rangeObj.getJSONArray("column");
    		if(rangeConfigCheck)
    		{
    			int str = row.getIntValue(0);
    			int edr = row.getIntValue(1);
    			int stc = column.getIntValue(0);
    			int edc = column.getIntValue(1);
    			if(YesNoEnum.YES.getCode().intValue() == 1) {
    				if("normal".equals(type)) {
    					str = str + 1;
    					stc = stc + 1;
    				}else if("leftright".equals(type)) {
    					stc = stc + 1;
    				}else if("topbottom".equals(type)) {
    					str = str + 1;
    				}
    			}
    			if(axisDataSize != 0 && (edc-stc+1)%axisDataSize==0) {
    				int multiple = (edc-stc+1)/axisDataSize;
    				XDDFNumericalDataSource<Double> area = null;
    				for (int t = 1; t <= multiple; t++) {
    					edc = axisDataSize + stc - 1;
    					for (int i = str; i <= edr; i++) {
            				area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(i, i, stc, edc));
            				result.add(area);
        				}
    					stc = edc;
					}
    			}else {
    				if(axisDataSize < (edc-stc+1))
        			{
        				edc = stc+axisDataSize-1;
        			}
        			XDDFNumericalDataSource<Double> area = null;
        			for (int i = str; i <= edr; i++) {
        				area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(i, i, stc, edc));
        				result.add(area);
    				}
    			}
    		}else {
    			int str = row.getIntValue(0);
    			int edr = row.getIntValue(1);
    			int stc = column.getIntValue(0);
    			int edc = column.getIntValue(1);
    			if(YesNoEnum.YES.getCode().intValue() == 1) {
    				if("normal".equals(type)) {
    					str = str + 1;
    					stc = stc + 1;
    				}else if("leftright".equals(type)) {
    					stc = stc + 1;
    				}else if("topbottom".equals(type)) {
    					str = str + 1;
    				}
    			}
    			if(axisDataSize != 0 && (edr-str+1)%axisDataSize==0) {
    				int multiple = (edr-str+1)/axisDataSize;
    				XDDFNumericalDataSource<Double> area = null;
    				for (int t = 1; t <= multiple; t++) {
    					edr = str+axisDataSize-1;
            			for (int i = stc; i <= edc; i++) {
            				area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(str, edr, i, i));
            				result.add(area);
        				}
            			str = edr+1;
    				}
    			}else {
    				if(axisDataSize < (edr-str+1))
        			{
        				edr = str+axisDataSize-1;
        			}
    				XDDFNumericalDataSource<Double> area = null;
        			for (int i = stc; i <= edc; i++) {
        				area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(str, edr, i, i));
        				result.add(area);
    				}
    			}
    		}
    	}
    	return result;
    }
    
    /**
     * 修复图表
     */
    private static void fixChart(XSSFChart chart) {
        CTPlotArea plotArea = chart.getCTChart().getPlotArea();
        List<CTCatAx> catAxList = plotArea.getCatAxList();
        for (int i = 0; i < catAxList.size(); i++) {
            catAxList.get(i).getNumFmt().setFormatCode("General");
            CTTextBody txPr = catAxList.get(i).addNewTxPr();
            txPr.addNewBodyPr();
            txPr.addNewP().addNewPPr().addNewDefRPr();
        }
        List<CTValAx> valAxList = plotArea.getValAxList();
        for (int i = 0; i < valAxList.size(); i++) {
            valAxList.get(i).addNewNumFmt().setFormatCode("General");
            CTTextBody txPr = valAxList.get(i).addNewTxPr();
            txPr.addNewBodyPr();
            txPr.addNewP().addNewPPr().addNewDefRPr();
        }
    }
    
    public static List<JSONArray> groupChartData(JSONObject chartOptions) {
    	List<JSONArray> result = new ArrayList<>();
    	if(chartOptions.getJSONObject("defaultOption") == null || chartOptions.getJSONObject("defaultOption").getJSONObject("spec") == null) {
    		return result;
    	}
    	JSONArray values = chartOptions.getJSONObject("defaultOption").getJSONObject("spec").getJSONObject("data").getJSONArray("values");
    	boolean rangeConfigCheck = chartOptions.getBooleanValue("rangeConfigCheck");
    	if(ListUtil.isNotEmpty(values)) {
    		Map<String, JSONArray> dataMap = new LinkedHashMap<>();
    		for (int i = 0; i < values.size(); i++) {
    			JSONArray rowList = null;
				JSONObject data = values.getJSONObject(i);
				String key = "";
				if(rangeConfigCheck) {
					key = data.getString("seriesField");
				}else {
					key = data.getString("type");
				}
				if(dataMap.containsKey(key)) {
					rowList = dataMap.get(key);
				}else {
					rowList = new JSONArray();
					dataMap.put(key, rowList);
				}
				rowList.add(data);
			}
    		Iterator<Entry<String, JSONArray>> entries = dataMap.entrySet().iterator();
    		while(entries.hasNext()){
    			result.add(entries.next().getValue());
			}
    	}
    	return result;
    }
    
    private static JSONArray getLegendData(List<JSONArray> chartData,boolean rangeConfigCheck) {
    	JSONArray result = new JSONArray();
    	if(rangeConfigCheck) {
    		if(ListUtil.isNotEmpty(chartData)) {
        		for (int i = 0; i < chartData.size(); i++) {
        			result.add(chartData.get(i).getJSONObject(0).getString("seriesField"));
    			}
        	}
    	}else {
    		if(ListUtil.isNotEmpty(chartData)) {
        		JSONArray datas = chartData.get(0);
        		for (int i = 0; i < datas.size(); i++) {
        			result.add(datas.getJSONObject(i).getString("seriesField"));
    			}
        	}
    	}
    	
    	return result;
    }
    
    public static JSONArray getAxesData(List<JSONArray> chartData,boolean rangeConfigCheck) {
    	JSONArray result = new JSONArray();
    	if(rangeConfigCheck) {
    		if(ListUtil.isNotEmpty(chartData)) {
        		JSONArray datas = chartData.get(0);
        		for (int i = 0; i < datas.size(); i++) {
        			result.add(datas.getJSONObject(i).getString("type"));
    			}
        	}
    	}else {
    		if(ListUtil.isNotEmpty(chartData)) {
        		for (int i = 0; i < chartData.size(); i++) {
        			result.add(chartData.get(i).getJSONObject(0).getString("type"));
    			}
        	}
    	}
    	return result;
    }
}
