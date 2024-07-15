package com.springreport.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	public static void createLineChart(XSSFSheet sheet,JSONObject chartCell,JSONObject chartOptions,boolean smooth,boolean showLabel) {
		//创建一个画布
		XSSFDrawing drawing = sheet.createDrawingPatriarch();
		int r = chartCell.getIntValue("r");
		int rs = chartCell.getIntValue("rs");
		int c = chartCell.getIntValue("c");
		int cs = chartCell.getIntValue("cs");
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,c,r,c+cs,r+rs);
		//创建一个chart对象
		XSSFChart chart = drawing.createChart(anchor);
		//标题
		String title = getTitle(chartOptions);
		if(StringUtil.isNotEmpty(title))
		{
			chart.setTitleText(title);
			//标题覆盖
			chart.setTitleOverlay(false);
		}
		JSONObject chartLegend = getLegend(chartOptions);
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
		JSONObject axis = getAxisData(chartOptions,"column");
//		//分类轴标(X轴),标题位置
		XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
		//值(Y轴)轴,标题位置
		XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
		JSONArray axisData = axis.getJSONArray("data");
		XDDFCategoryDataSource category = XDDFDataSourcesFactory.fromArray(axisData.toArray(new String[axisData.size()]));
		List<XDDFNumericalDataSource<Double>> datas = getSeriesArea(sheet, chartOptions, axisData.size());
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
	public static void createAreaChart(XSSFSheet sheet,JSONObject chartCell,JSONObject chartOptions) {
		//创建一个画布
		XSSFDrawing drawing = sheet.createDrawingPatriarch();
		int r = chartCell.getIntValue("r");
		int rs = chartCell.getIntValue("rs");
		int c = chartCell.getIntValue("c");
		int cs = chartCell.getIntValue("cs");
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,c,r,c+cs,r+rs);
		//创建一个chart对象
		XSSFChart chart = drawing.createChart(anchor);
		//标题
		String title = getTitle(chartOptions);
		if(StringUtil.isNotEmpty(title))
		{
			chart.setTitleText(title);
			//标题覆盖
			chart.setTitleOverlay(false);
		}
		JSONObject chartLegend = getLegend(chartOptions);
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
		JSONObject axis = getAxisData(chartOptions,"column");
		//分类轴标(X轴),标题位置
		XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
		//值(Y轴)轴,标题位置
		XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
		JSONArray axisData = axis.getJSONArray("data");
		XDDFCategoryDataSource category = XDDFDataSourcesFactory.fromArray(axisData.toArray(new String[axisData.size()]));
		List<XDDFNumericalDataSource<Double>> datas = getSeriesArea(sheet, chartOptions, axisData.size());
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
    public static void createBar(XSSFSheet sheet,JSONObject chartCell,JSONObject chartOptions,String type,boolean stacked){
		//创建一个画布
		XSSFDrawing drawing = sheet.createDrawingPatriarch();
		int r = chartCell.getIntValue("r");
		int rs = chartCell.getIntValue("rs");
		int c = chartCell.getIntValue("c");
		int cs = chartCell.getIntValue("cs");
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,c,r,c+cs,r+rs);
		//创建一个chart对象
		XSSFChart chart = drawing.createChart(anchor);
		//标题
		String title = getTitle(chartOptions);
		if(StringUtil.isNotEmpty(title))
		{
			chart.setTitleText(title);
			//标题覆盖
			chart.setTitleOverlay(false);
		}
		JSONObject chartLegend = getLegend(chartOptions);
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
		JSONObject axis = getAxisData(chartOptions,type);
//		//分类轴标(X轴),标题位置
		XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
		//值(Y轴)轴,标题位置
		XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
		leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
		JSONArray axisData = axis.getJSONArray("data");
		XDDFCategoryDataSource category = XDDFDataSourcesFactory.fromArray(axisData.toArray(new String[axisData.size()]));
		List<XDDFNumericalDataSource<Double>> datas = getSeriesArea(sheet, chartOptions, axisData.size());
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
    public static void createPie(XSSFSheet sheet,JSONObject chartCell,JSONObject chartOptions){
    	//创建一个画布
    	XSSFDrawing drawing = sheet.createDrawingPatriarch();
		int r = chartCell.getIntValue("r");
		int rs = chartCell.getIntValue("rs");
		int c = chartCell.getIntValue("c");
		int cs = chartCell.getIntValue("cs");
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,c,r,c+cs,r+rs);
		//创建一个chart对象
		XSSFChart chart = drawing.createChart(anchor);
		//标题
		String title = getTitle(chartOptions);
		if(StringUtil.isNotEmpty(title))
		{
			chart.setTitleText(title);
			//标题覆盖
			chart.setTitleOverlay(false);
		}
		JSONObject chartLegend = getLegend(chartOptions);
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
		JSONObject pieSeries = getPieSeries(chartOptions);
		JSONArray names = pieSeries.getJSONArray("data");
		XDDFCategoryDataSource category = XDDFDataSourcesFactory.fromArray(names.toArray(new String[names.size()]));
		List<XDDFNumericalDataSource<Double>> datas = getSeriesArea(sheet, chartOptions, names.size());
		if(ListUtil.isNotEmpty(datas)) {
			XDDFPieChartData data = (XDDFPieChartData)chart.createData(ChartTypes.PIE, null, null);
			XDDFPieChartData.Series series = (XDDFPieChartData.Series)data.addSeries(category, datas.get(0));
			series.setTitle(pieSeries.getString("name"), null);
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
    public static void createDoughnut(XSSFSheet sheet,JSONObject chartCell,JSONObject chartOptions){
    	//创建一个画布
    	XSSFDrawing drawing = sheet.createDrawingPatriarch();
		int r = chartCell.getIntValue("r");
		int rs = chartCell.getIntValue("rs");
		int c = chartCell.getIntValue("c");
		int cs = chartCell.getIntValue("cs");
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,c,r,c+cs,r+rs);
		//创建一个chart对象
		XSSFChart chart = drawing.createChart(anchor);
		//标题
		String title = getTitle(chartOptions);
		if(StringUtil.isNotEmpty(title))
		{
			chart.setTitleText(title);
			//标题覆盖
			chart.setTitleOverlay(false);
		}
		JSONObject chartLegend = getLegend(chartOptions);
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
		JSONObject pieSeries = getPieSeries(chartOptions);
		JSONArray names = pieSeries.getJSONArray("data");
		XDDFCategoryDataSource category = XDDFDataSourcesFactory.fromArray(names.toArray(new String[names.size()]));
		List<XDDFNumericalDataSource<Double>> datas = getSeriesArea(sheet, chartOptions, names.size());
		if(ListUtil.isNotEmpty(datas)) {
			XDDFDoughnutChartData data = (XDDFDoughnutChartData)chart.createData(ChartTypes.DOUGHNUT, null, null);
			XDDFDoughnutChartData.Series series = (XDDFDoughnutChartData.Series)data.addSeries(category, datas.get(0));
			series.setTitle(pieSeries.getString("name"), null);
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
    public static void createRadar(XSSFSheet sheet,JSONObject chartCell,JSONObject chartOptions){
    	//创建一个画布
    	XSSFDrawing drawing = sheet.createDrawingPatriarch();
		int r = chartCell.getIntValue("r");
		int rs = chartCell.getIntValue("rs");
		int c = chartCell.getIntValue("c");
		int cs = chartCell.getIntValue("cs");
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,c,r,c+cs,r+rs);
		//创建一个chart对象
		XSSFChart chart = drawing.createChart(anchor);
		//标题
		String title = getTitle(chartOptions);
		if(StringUtil.isNotEmpty(title))
		{
			chart.setTitleText(title);
			//标题覆盖
			chart.setTitleOverlay(false);
		}
		JSONObject chartLegend = getLegend(chartOptions);
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
		JSONArray indicator = getIndicator(chartOptions);
		JSONObject pieSeries = getPieSeries(chartOptions);
		JSONArray names = pieSeries.getJSONArray("data");
		XDDFCategoryDataSource category = XDDFDataSourcesFactory.fromArray(indicator.toArray(new String[indicator.size()]));
		List<XDDFNumericalDataSource<Double>> datas = getSeriesArea(sheet, chartOptions, indicator.size());
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
					series.setTitle("系列"+i, null);
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
    private static String getTitle(JSONObject chartOptions) {
    	String result = "";
		JSONObject defaultOption = chartOptions.getJSONObject("defaultOption");
		if(defaultOption != null)
		{
			JSONObject title = defaultOption.getJSONObject("title");
			if(title != null)
			{
				boolean show = title.getBooleanValue("show");
				if(show) {
					result = title.getString("text");
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
    private static JSONObject getLegend(JSONObject chartOptions)
    {
    	JSONObject result = new JSONObject();
    	result.put("show", false);
    	JSONObject defaultOption = chartOptions.getJSONObject("defaultOption");
		if(defaultOption != null)
		{
			JSONObject legend = defaultOption.getJSONObject("legend");
			if(legend != null)
			{
				boolean show = legend.getBooleanValue("show");
				result.put("show", show);
				JSONArray legendData = legend.getJSONArray("data");
				result.put("data", legendData);
				JSONObject position = legend.getJSONObject("position");
				if(position != null)
				{
					String positionValue = position.getString("value");
					switch (positionValue) {
					case "left-top":
						positionValue = "top";
						break;
					case "left-middle":
						positionValue = "left";
						break;
					case "left-bottom":
						positionValue = "left";
						break;
					case "right-top":
						positionValue = "right-top";
						break;
					case "right-middle":
						positionValue = "right";
						break;
					case "right-bottom":
						positionValue = "right";
						break;
					case "center-top":
						positionValue = "top";
						break;
					case "center-middle":
						positionValue = "bottom";
						break;
					case "center-bottom":
						positionValue = "bottom";
						break;
					default:
						positionValue = "bottom";
						break;
					}
					result.put("position", positionValue);
					String direction = position.getString("direction");
					result.put("direction", direction);
				}
			}
		}
		return result;
    }
    
    private static JSONObject getPieSeries(JSONObject chartOptions) {
    	JSONObject result = new JSONObject();
    	JSONArray dataNames = new JSONArray();
    	JSONObject defaultOption = chartOptions.getJSONObject("defaultOption");
    	String seriesName = "";
    	if(defaultOption != null) {
    		JSONArray series = defaultOption.getJSONArray("series");
        	if(ListUtil.isNotEmpty(series))
        	{
        		JSONObject seriesObj = series.getJSONObject(0);
        		JSONArray data = seriesObj.getJSONArray("data");
        		seriesName = seriesObj.getString("name");
        		if(ListUtil.isNotEmpty(data))
        		{
        			for (int i = 0; i < data.size(); i++) {
    					String name = data.getJSONObject(i).getString("name");
    					dataNames.add(name);
    				}
        		}
        	}
    	}
    	result.put("data", dataNames);
    	result.put("name", seriesName);
    	return result;
    }
    
    private static JSONObject getAxisData(JSONObject chartOptions,String type){
    	JSONObject result = new JSONObject();
    	result.put("show", false);
    	JSONObject defaultOption = chartOptions.getJSONObject("defaultOption");
		if(defaultOption != null)
		{
			JSONObject axis = defaultOption.getJSONObject("axis");
			if(axis != null)
			{
				JSONObject xAxisDown = null;
				if("column".equals(type))
				{
					xAxisDown = axis.getJSONObject("xAxisDown");
				}else {
					xAxisDown = axis.getJSONObject("yAxisLeft");
				}
				if(xAxisDown != null)
				{
					boolean show = xAxisDown.getBooleanValue("show");
					JSONArray data = xAxisDown.getJSONArray("data");
					result.put("show", show);
					result.put("data", data);
				}
			}
		}
		return result;
    }
    
    private static List<XDDFNumericalDataSource<Double>> getSeriesArea(XSSFSheet sheet,JSONObject chartOptions,int axisDataSize){
    	List<XDDFNumericalDataSource<Double>> result = new ArrayList<>();
    	JSONArray rangeArray = chartOptions.getJSONArray("rangeArray");
    	boolean rangeConfigCheck = chartOptions.getBooleanValue("rangeConfigCheck");// transpose(switch row/column)
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
    			if(axisDataSize < (edc-stc+1))
    			{
    				edc = stc+axisDataSize-1;
    			}
    			XDDFNumericalDataSource<Double> area = null;
    			for (int i = str; i <= edr; i++) {
    				area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(i, i, stc, edc));
    				result.add(area);
				}
    		}else {
    			int str = row.getIntValue(0);
    			int edr = row.getIntValue(1);
    			int stc = column.getIntValue(0);
    			int edc = column.getIntValue(1);
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
    	return result;
    }
    
    private static JSONArray getIndicator(JSONObject chartOptions) {
    	JSONArray result = new JSONArray();
    	JSONObject defaultOption = chartOptions.getJSONObject("defaultOption");
		if(defaultOption != null)
		{
			JSONObject radar = defaultOption.getJSONObject("radar");
			if(radar != null) {
				JSONArray indicator = radar.getJSONArray("indicator");
				if(ListUtil.isNotEmpty(indicator))
				{
					for (int i = 0; i < indicator.size(); i++) {
						String name = indicator.getJSONObject(i).getString("name");
						result.add(name);
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
}
