package com.springreport.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springreport.excel2pdf.ImageInfo;

public class JFreeChartUtil {

	private static StandardChartTheme createChartTheme(String fontName) {
		StandardChartTheme standardChartTheme = new StandardChartTheme("demo");
	    standardChartTheme.setExtraLargeFont(new Font(fontName, Font.BOLD, 20));
	    standardChartTheme.setRegularFont(new Font(fontName, Font.PLAIN, 15));
	    standardChartTheme.setLargeFont(new Font(fontName, Font.PLAIN, 15));
	    return standardChartTheme;
	}
	
	/**  
	 * @MethodName: createPieChart
	 * @Description: 生成饼图
	 * @author caiyang
	 * @param title
	 * @param dataset
	 * @param width
	 * @param height
	 * @return
	 * @throws IOException 
	 * @return byte[]
	 * @date 2023-04-21 06:31:39 
	 */  
	public static byte[] createPieChart(String title,PieDataset dataset,int width,int height,String type,JSONArray legend) throws IOException
	{
		try {
			StandardChartTheme standardChartTheme = createChartTheme("宋体");
		    ChartFactory.setChartTheme(standardChartTheme);
			JFreeChart chart = ChartFactory.createPieChart(title, dataset, true, true, false);
			// 设置抗锯齿，防止字体显示不清楚
	        chart.setTextAntiAlias(false);
	        PiePlot plot = (PiePlot) chart.getPlot();
	        //边框线为白色
	        plot.setOutlinePaint(Color.white);
	        //连接线类型为直线
	        plot.setLabelLinkStyle(PieLabelLinkStyle.QUAD_CURVE);
	        //设置Label字体
	        plot.setLabelFont(new java.awt.Font("微软雅黑", java.awt.Font.BOLD, 12));
	        //设置legend字体
	        chart.getLegend().setItemFont(new java.awt.Font("微软雅黑", java.awt.Font.BOLD, 12));
	        // 图片中显示百分比:默认方式
	        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(StandardPieToolTipGenerator.DEFAULT_TOOLTIP_FORMAT));
	        // 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
	        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}:{1}",
	                NumberFormat.getNumberInstance(),
	                new DecimalFormat("0.00%")));
	        // 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例
	        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}:{2}"));
	        // 指定图片的透明度(0.0-1.0)
	        plot.setForegroundAlpha(1.0f);
	        // 指定显示的饼图上圆形(false)还椭圆形(true)
	        plot.setCircular(true);
	        //设置图表背景透明度
	        plot.setBackgroundAlpha(0f);
	        if("split".equals(type))
	        {
	        	for (int i = 0; i < legend.size(); i++) {
	        		plot.setExplodePercent(legend.getString(i), 0.1);
	            }
	        }
	        // 设置背景色为白色
	        chart.setBackgroundPaint(Color.WHITE);
	        // 设置标注无边框
	        chart.getLegend().setFrame(new BlockBorder(Color.WHITE));
	        
	        BufferedImage bufferedImage = chart.createBufferedImage(width, height,
	              BufferedImage.TYPE_INT_RGB,null);
	        ByteArrayOutputStream os = new ByteArrayOutputStream();
	        ImageIO.write(bufferedImage, "jpeg", os);
	        return os.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] createRingChart(String title,PieDataset dataset,int width,int height) throws IOException
	{
		try {
			StandardChartTheme standardChartTheme = createChartTheme("宋体");
		    ChartFactory.setChartTheme(standardChartTheme);
			JFreeChart chart = ChartFactory.createRingChart(title, dataset, true, true, false);
			// 设置抗锯齿，防止字体显示不清楚
	        chart.setTextAntiAlias(false);
	        RingPlot plot = (RingPlot) chart.getPlot();
	        //边框线为白色
	        plot.setOutlinePaint(Color.white);
	        //连接线类型为直线
	        plot.setLabelLinkStyle(PieLabelLinkStyle.QUAD_CURVE);
	        //设置Label字体
	        plot.setLabelFont(new java.awt.Font("微软雅黑", java.awt.Font.BOLD, 12));
	        //设置legend字体
	        chart.getLegend().setItemFont(new java.awt.Font("微软雅黑", java.awt.Font.BOLD, 12));
	        // 图片中显示百分比:默认方式
	        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(StandardPieToolTipGenerator.DEFAULT_TOOLTIP_FORMAT));
	        // 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
	        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}:{1}",
	                NumberFormat.getNumberInstance(),
	                new DecimalFormat("0.00%")));
	        // 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例
	        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}:{2}"));
	        // 指定图片的透明度(0.0-1.0)
	        plot.setForegroundAlpha(1.0f);
	        // 指定显示的饼图上圆形(false)还椭圆形(true)
	        plot.setCircular(true);
	        //设置图表背景透明度
	        plot.setBackgroundAlpha(0f);
	        // 设置背景色为白色
	        chart.setBackgroundPaint(Color.WHITE);
	        // 设置标注无边框
	        chart.getLegend().setFrame(new BlockBorder(Color.WHITE));
	        
	        BufferedImage bufferedImage = chart.createBufferedImage(width, height,
	              BufferedImage.TYPE_INT_RGB,null);
	        ByteArrayOutputStream os = new ByteArrayOutputStream();
	        ImageIO.write(bufferedImage, "jpeg", os);
	        return os.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**  
	 * @MethodName: getPieChartDataset
	 * @Description: 获取饼图数据
	 * @author caiyang
	 * @param jsonObject
	 * @return 
	 * @return PieDataset
	 * @date 2023-04-21 05:43:07 
	 */  
	public static JSONObject getPieChartDataset(JSONObject chartOptions,List<JSONArray> chartData) {
		JSONObject result = new JSONObject();
		DefaultPieDataset dataset = new DefaultPieDataset();
		boolean rangeConfigCheck = chartOptions.getBooleanValue("rangeConfigCheck");
		JSONArray legend = ExcelChartUtil.getAxesData(chartData,rangeConfigCheck);
		try {
			if(!ListUtil.isEmpty(chartData))
			{
				if(rangeConfigCheck) {
					JSONArray datas = chartData.get(0);
					for (int i = 0; i < datas.size(); i++) {
						JSONObject data = datas.getJSONObject(i);
						String name = data.getString("type");
						String value = data.getString("value");
						dataset.setValue(name, Double.parseDouble(value));
					}
				}else {
					for (int i = 0; i < chartData.size(); i++) {
						JSONObject data = chartData.get(i).getJSONObject(0);
						String name = data.getString("type");
						String value = data.getString("value");
						dataset.setValue(name, Double.parseDouble(value));
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			dataset = null;
		}
		result.put("dataSet", dataset);
		result.put("legend", legend);
		return result;
	}
	
	public static byte[] createLineChart(String title,DefaultCategoryDataset dataset,int width,int height) throws IOException {
		try {
			StandardChartTheme standardChartTheme = createChartTheme("宋体");
		    ChartFactory.setChartTheme(standardChartTheme);
		    // 创建柱状图.标题,X坐标,Y坐标,数据集合,orientation,是否显示legend,是否显示tooltip,是否使用url链接
		    JFreeChart chart = ChartFactory.createLineChart(title, "", "", dataset, PlotOrientation.VERTICAL,true, true, true);
		    // 设置抗锯齿，防止字体显示不清楚
		    setXYStyle(chart);
		    CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();
	        // 获得renderer
	        LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryPlot.getRenderer();
	        lineandshaperenderer.setDefaultShapesVisible(true);
	        lineandshaperenderer.setDefaultLinesVisible(true);
	        lineandshaperenderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
	        lineandshaperenderer.setDefaultItemLabelsVisible(true);
	        lineandshaperenderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE1, TextAnchor.BASELINE_CENTER));
	        BufferedImage bufferedImage = chart.createBufferedImage(width, height,
	                BufferedImage.TYPE_INT_RGB,null);
	          ByteArrayOutputStream os = new ByteArrayOutputStream();
	          ImageIO.write(bufferedImage, "jpeg", os);
	          return os.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static byte[] createAreaChart(String title,DefaultCategoryDataset dataset,int width,int height) throws IOException {
		try {
			StandardChartTheme standardChartTheme = createChartTheme("宋体");
		    ChartFactory.setChartTheme(standardChartTheme);
		    // 创建柱状图.标题,X坐标,Y坐标,数据集合,orientation,是否显示legend,是否显示tooltip,是否使用url链接
		    JFreeChart chart = ChartFactory.createAreaChart(title, "", "", dataset, PlotOrientation.VERTICAL,true, true, true);
		    // 设置抗锯齿，防止字体显示不清楚
		    setXYStyle(chart);
		    CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();
	        // 获得renderer
		    AreaRenderer areaRenderer = (AreaRenderer) categoryPlot.getRenderer();
		    areaRenderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		    areaRenderer.setDefaultItemLabelsVisible(true);
		    areaRenderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE1, TextAnchor.BASELINE_CENTER));
	        BufferedImage bufferedImage = chart.createBufferedImage(width, height,
	                BufferedImage.TYPE_INT_RGB,null);
	          ByteArrayOutputStream os = new ByteArrayOutputStream();
	          ImageIO.write(bufferedImage, "jpeg", os);
	          return os.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static byte[] creteBarChart(String title,DefaultCategoryDataset dataset,int width,int height,String type) {
		try {
			StandardChartTheme standardChartTheme = createChartTheme("宋体");
		    ChartFactory.setChartTheme(standardChartTheme);
			JFreeChart chart = ChartFactory.createBarChart(
	                title,
	                "", // 目录轴的显示标签
	                "", // 数值轴的显示标签
	                dataset, // 数据集
	                "column".equals(type)?PlotOrientation.VERTICAL:PlotOrientation.HORIZONTAL, // 图表方向
	                true, // 是否显示图例，对于简单的柱状图必须为false
	                true, // 是否生成提示工具
	                false); // 是否生成url链接
			setXYStyle(chart);
			CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();
	        BarRenderer barRenderer = (BarRenderer) categoryPlot.getRenderer();
	        barRenderer.setDefaultItemLabelsVisible(true);
	        barRenderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator(StandardCategoryItemLabelGenerator.DEFAULT_LABEL_FORMAT_STRING,
                    NumberFormat.getInstance()));
	        barRenderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.BASELINE_CENTER));
	        barRenderer.setDefaultItemLabelsVisible(true);
	        BufferedImage bufferedImage = chart.createBufferedImage(width, height,
	                BufferedImage.TYPE_INT_RGB,null);
	          ByteArrayOutputStream os = new ByteArrayOutputStream();
	          ImageIO.write(bufferedImage, "jpeg", os);
	          return os.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**  
	 * @MethodName: createStackedBar
	 * @Description: 创建堆叠柱状图
	 * @author caiyang
	 * @param title
	 * @param dataset
	 * @param width
	 * @param height
	 * @param type
	 * @return 
	 * @return byte[]
	 * @throws IOException 
	 * @date 2023-04-23 11:23:43 
	 */  
	public static byte[] createStackedBar(String title,DefaultCategoryDataset dataset,int width,int height,String type) throws IOException {
		StandardChartTheme standardChartTheme = createChartTheme("宋体");
	    ChartFactory.setChartTheme(standardChartTheme);
		JFreeChart chart = ChartFactory.createStackedBarChart(
                title,
                "", // 目录轴的显示标签
                "", // 数值轴的显示标签
                dataset, // 数据集
                "column".equals(type)?PlotOrientation.VERTICAL:PlotOrientation.HORIZONTAL, // 图表方向
                true, // 是否显示图例，对于简单的柱状图必须为false
                true, // 是否生成提示工具
                false); // 是否生成url链接
		setXYStyle(chart);
		CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();
        BarRenderer barRenderer = (BarRenderer) categoryPlot.getRenderer();
        barRenderer.setDefaultItemLabelsVisible(true);
        barRenderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator(StandardCategoryItemLabelGenerator.DEFAULT_LABEL_FORMAT_STRING,
                NumberFormat.getInstance()));
        barRenderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.BASELINE_CENTER));
        barRenderer.setDefaultItemLabelsVisible(true);
        BufferedImage bufferedImage = chart.createBufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB,null);
          ByteArrayOutputStream os = new ByteArrayOutputStream();
          ImageIO.write(bufferedImage, "jpeg", os);
          return os.toByteArray();
	}
	
	public static DefaultCategoryDataset getCategoryDataset(JSONObject chartOptions,List<JSONArray> chartData) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		try {
			JSONObject chartLegend = ExcelChartUtil.getLegend(chartOptions,chartData);
			JSONArray legendData = chartLegend.getJSONArray("data");
			boolean rangeConfigCheck = chartOptions.getBooleanValue("rangeConfigCheck");
			if(ListUtil.isNotEmpty(chartData)) {
				for (int i = 0; i < chartData.size(); i++) {
					JSONArray datas = chartData.get(i);
					for (int j = 0; j < datas.size(); j++) {
						String name = datas.getJSONObject(j).getString("type");
						if(rangeConfigCheck) {
							dataset.addValue(datas.getJSONObject(j).getDoubleValue("value"), legendData.getString(i), name);
						}else {
							dataset.addValue(datas.getJSONObject(j).getDoubleValue("value"), legendData.getString(j), name);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			dataset = null;
		}
		
		return dataset;
	}
	
	private static void setXYStyle(JFreeChart chart) {
		// 设置抗锯齿，防止字体显示不清楚
        chart.setTextAntiAlias(false);
        chart.setBackgroundPaint(Color.WHITE);
        chart.getLegend().setItemFont(new java.awt.Font("微软雅黑", java.awt.Font.BOLD, 12));
        Font font = new Font("宋体", Font.BOLD, 12);
        chart.getTitle().setFont(font);
        chart.setBackgroundPaint(Color.WHITE);
        // 配置字体（解决中文乱码的通用方法）
        // X轴
        Font xfont = new Font("微软雅黑", Font.BOLD, 12);
        // Y轴
        Font yfont = new Font("微软雅黑", Font.BOLD, 12);
        // 图片标题
        Font titleFont = new Font("微软雅黑", Font.BOLD, 12);
		CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot();
		categoryPlot.setOutlinePaint(Color.white);
		categoryPlot.getDomainAxis().setLabelFont(xfont);
	    categoryPlot.getDomainAxis().setLabelFont(xfont);
	    categoryPlot.getRangeAxis().setLabelFont(yfont);
	    chart.getTitle().setFont(titleFont);
	    categoryPlot.setBackgroundPaint(Color.WHITE);
	    // x轴 // 分类轴网格是否可见
	    categoryPlot.setDomainGridlinesVisible(true);
	    // y轴 //数据轴网格是否可见
	    categoryPlot.setRangeGridlinesVisible(true);
	    // 设置网格竖线颜色
	    categoryPlot.setDomainGridlinePaint(Color.LIGHT_GRAY);
	    // 设置网格横线颜色
	    categoryPlot.setRangeGridlinePaint(Color.LIGHT_GRAY);
	    // 没有数据时显示的文字说明
	    categoryPlot.setNoDataMessage("没有数据显示");
	    // 设置曲线图与xy轴的距离
	    categoryPlot.setAxisOffset(new RectangleInsets(0d, 0d, 0d, 0d));
	    // 设置面板字体
	    Font labelFont = new Font("微软雅黑", Font.BOLD, 12);
	    // 取得Y轴
	    NumberAxis rangeAxis = (NumberAxis) categoryPlot.getRangeAxis();
	    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	    rangeAxis.setAutoRangeIncludesZero(true);
	    rangeAxis.setUpperMargin(0.20);
	    rangeAxis.setLabelAngle(Math.PI / 2.0);
	    // 取得X轴
	    CategoryAxis categoryAxis = (CategoryAxis) categoryPlot.getDomainAxis();
	    // 设置X轴坐标上的文字
	    categoryAxis.setTickLabelFont(labelFont);
	    // 设置X轴的标题文字
	    categoryAxis.setLabelFont(labelFont);
	    // 设置距离图片左端距离
	    categoryAxis.setLowerMargin(0.0);
	    // 设置距离图片右端距离
	    categoryAxis.setUpperMargin(0.0);
	}
	
	
	/**  
	 * @MethodName: getRadarDataset
	 * @Description: 获取雷达图的数据集
	 * @author caiyang
	 * @param defaultOption
	 * @return DefaultCategoryDataset
	 * @date 2023-07-02 03:50:42 
	 */ 
	public static JSONObject getRadarDataset(JSONObject chartOptions,List<JSONArray> chartData)
	{
		JSONObject result = new JSONObject();
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		boolean rangeConfigCheck = chartOptions.getBooleanValue("rangeConfigCheck");
		JSONObject chartLegend = ExcelChartUtil.getLegend(chartOptions,chartData);
		JSONArray legend = chartLegend.getJSONArray("data");
		float maxValue = 0;
		if(ListUtil.isNotEmpty(chartData)) {
			for (int i = 0; i < chartData.size(); i++) {
				JSONArray datas = chartData.get(i);
				for (int j = 0; j < datas.size(); j++) {
					String name = datas.getJSONObject(j).getString("type");
					if(rangeConfigCheck) {
						dataset.addValue(datas.getJSONObject(j).getFloatValue("value"), legend.getString(i), name);
					}else {
						dataset.addValue(datas.getJSONObject(j).getFloatValue("value"), legend.getString(j), name);
					}
					if(datas.getJSONObject(j).getDoubleValue("value") > maxValue) {
						maxValue = datas.getJSONObject(j).getFloatValue("value");
					}
				}
			}
		}
//		if(!ListUtil.isEmpty(datas))
//		{
//			for (int i = 0; i < datas.size(); i++) {
//				JSONObject data = datas.getJSONObject(i);
//				String groupName = data.getString("name");
//				JSONArray values = data.getJSONArray("value");
//				if(!ListUtil.isEmpty(values) && values.size() == indicator.size())
//				{
//					for (int j = 0; j < values.size(); j++) {
//						if(values.getFloatValue(j) > maxValue)
//						{
//							maxValue = values.getFloatValue(j);
//						}
//						dataset.addValue(values.getFloatValue(j), groupName, indicator.getJSONObject(j).getString("name"));
//					}
//					
//				}
//			}
//		}
		result.put("dataSet", dataset);
		result.put("maxValue", maxValue);
		return result;
	}
}
