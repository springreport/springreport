package com.springreport;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.Units;
import org.apache.poi.xddf.usermodel.chart.AxisCrossBetween;
import org.apache.poi.xddf.usermodel.chart.AxisCrosses;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.BarDirection;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFBarChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFPieChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.ChartSingleSeriesRenderData;
import com.deepoove.poi.data.Charts;
import com.github.pagehelper.util.StringUtil;
import com.springreport.util.AsposeUtil;

import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer;
public class ChartTest {

	public static void main(String[] args) throws Exception {
		System.err.println(com.springreport.util.StringUtil.getFileNameFromUrl("https://www.springreport.vip/images/chart/pie.png?t=1723023618316").split("\\.")[0]);
//	      Map<String, Object> map = new HashMap<>();
//	      ChartSingleSeriesRenderData demo1 = Charts.ofPie("123", new String[]{"第一季度", "第二季度", "第三季度", "第四季度"})
//	              .series("", new Number[]{0.3, 0.3, 0.2, 0.2})
//	              .create();
//	      map.put("demo1", demo1);
//	      XWPFTemplate.compile("D:\\software\\chart\\charttest2.docx").render(map).writeToFile("D:\\software\\chart\\charttest.docx");
		XWPFDocument document = new XWPFDocument();
		XWPFChart chart = document.createChart(14 * Units.EMU_PER_CENTIMETER, 10 * Units.EMU_PER_CENTIMETER);
		// 显示在饼图上面的标题
		chart.setTitleText("测试");
		// 设置标题是否覆盖图表
		chart.setTitleOverlay(false);
		// 分类数据源，即各个专业
		XDDFCategoryDataSource categoryDS = XDDFDataSourcesFactory.fromArray(new String[]{"英语", "数学", "计算机", "财经"});
		// 值数据源，即各个专业的选修人数
		XDDFNumericalDataSource<Number> valueDS = XDDFDataSourcesFactory.fromArray(new Integer[]{10, 20, 30, 50});

		// 将数据源绑定到饼图上
		XDDFChartData data = chart.createData(ChartTypes.PIE3D, null, null);
		XDDFChartData.Series series = data.addSeries(categoryDS, valueDS);

		// 为了在饼图上显示百分比等信息，需要调用下面的方法
		series.setShowLeaderLines(true);
		// 图例
		XDDFChartLegend legend = chart.getOrAddLegend();
		// 图例的位置显示在图的正下方
		legend.setPosition(LegendPosition.BOTTOM);
		// 让每个分类的颜色区分开来
		data.setVaryColors(true);
		// 绘制饼图
		chart.plot(data);
        FileOutputStream fos = new FileOutputStream("D:\\software\\chart\\charttest2.docx");
        document.write(fos); // 导出word

        // 11、关闭流r
        fos.close();
        document.close();
		AsposeUtil.wordToPdf("D:\\software\\chart\\charttest2.docx", "D:\\software\\chart\\1790359896949010433.pdf");
	}
	
	
	static CellReference setTitleInDataSheet(XWPFChart chart, String title, int column) throws Exception {
		XSSFWorkbook workbook = chart.getWorkbook();
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow row = sheet.getRow(0);
		if (row == null)
			row = sheet.createRow(0);
		XSSFCell cell = row.getCell(column);
		if (cell == null)
			cell = row.createCell(column);
		cell.setCellValue(title);
		return new CellReference(sheet.getSheetName(), 0, column, true, true);
	}

	
//	 public static void main(String[] args) throws Exception {
//		 
////		 XWPFDocument document = null;
////		 document = new XWPFDocument(POIXMLDocument.openPackage("D:\\software\\chart\\chart.docx"));
////		 document.getParagraphs();
////		 List<XWPFChart> charts = document.getCharts();
////		 System.err.println();
//	        // 1、创建word文档对象
//	        XWPFDocument document = new XWPFDocument();
//	        // 2、创建chart图表对象,抛出异常
//	        XWPFParagraph paragraph = document.createParagraph();
//	        XWPFRun xwpfRun = paragraph.createRun();
////	        CTR crt = xwpfRun.getCTR();
////	        crt.getDrawingArray();
////	        crt.addNewDrawing().addNewAnchor();
//	        XWPFChart chart = document.createChart(xwpfRun,15 * Units.EMU_PER_CENTIMETER, 10 * Units.EMU_PER_CENTIMETER);
//	        // 3、图表相关设置
//	        chart.setTitleText("使用POI创建的饼图"); // 图表标题
//	        chart.setTitleOverlay(false); // 图例是否覆盖标题
//	        XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
//	        XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT);
//	        yAxis.setCrosses(AxisCrosses.AUTO_ZERO);
//	        yAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
//	        
//	        String[] categories = new String[] { "Lang 1", "Lang 2", "Lang 3" };
//			Double[] valuesA = new Double[] { 10d, 20d, 30d };
//			Double[] valuesB = new Double[] { 15d, 25d, 35d };
//
//	        XDDFDataSource<String> categoryDS = XDDFDataSourcesFactory.fromArray(categories);
//	        List<XDDFNumericalDataSource<Double>> valueDS = new ArrayList<>();
////	        List<SerieData> seriesData = chartData.getSeries();
////	        for (Double seriesDatum : valuesA) {
////	            XDDFNumericalDataSource<Number> s = XDDFDataSourcesFactory.fromArray(new Double[] { seriesDatum });
////	            valueDS.add(s);
////	        }
//	        XDDFNumericalDataSource<Double> s = XDDFDataSourcesFactory.fromArray(valuesA);
//            valueDS.add(s);
//            XDDFNumericalDataSource<Double> s1 = XDDFDataSourcesFactory.fromArray(valuesB);
//            valueDS.add(s1);
//	        // create chart data
//	        XDDFChartData data = chart.createData(ChartTypes.BAR, xAxis, yAxis);
//	        ((XDDFBarChartData) data).setBarDirection(BarDirection.COL);
//	        ((XDDFBarChartData) data).setGapWidth(500);
//
//	        int i = 0;
////	        data.setVaryColors(seriesData.size() > 1);
//	        for (XDDFNumericalDataSource<Double> value : valueDS) {
//	            XDDFChartData.Series series = data.addSeries(categoryDS, value);
//	            series.setTitle("", null);
////	            solidFillSeries(series, seriesData.get(i).getColor());
//	            i++;
//	        }
//	        
//	        // 4、图例设置
//	        XDDFChartLegend legend = chart.getOrAddLegend();
//	        legend.setPosition(LegendPosition.TOP); // 图例位置:上下左右
//
////	        // 5、X轴(分类轴)相关设置:饼图中的图例显示
////	        String[] xAxisData = new String[] {
////	                "oxygen","silicon","aluminum","iron","calcium","sodium",
////	                "potassium","others",
////	        };
////	        XDDFCategoryDataSource xAxisSource = XDDFDataSourcesFactory.fromArray(xAxisData); // 设置分类数据
////
////	        // 6、Y轴(值轴)相关设置:饼图中的圆形显示
////	        Double[] yAxisData = new Double[]{
////	        		46.60, 27.72, 18.13, 15.0, 13.63, 12.83,
////	                12.59, 13.5
////	        };
////	        XDDFNumericalDataSource<Double> yAxisSource = XDDFDataSourcesFactory.fromArray(yAxisData); // 设置值数据
////	        // 7、创建饼图对象,饼状图不需要X,Y轴,只需要数据集即可
////	        XDDFPieChartData pieChart = (XDDFPieChartData) chart.createData(ChartTypes.PIE, null, null);
////	        // 8、加载饼图数据集
////	        XDDFPieChartData.Series pieSeries = (XDDFPieChartData.Series) pieChart.addSeries(xAxisSource, yAxisSource);
////	        pieSeries.setTitle("", null); // 系列提示标题
////	        pieSeries.setShowLeaderLines(true);
////	        CTPieSer ctPieSer = pieSeries.getCTPieSer();
////	        showCateName(ctPieSer, false);
////	        showVal(ctPieSer, true);
////	        showPercent(ctPieSer, false);
//////	        showLegendKey(ctPieSer, false);
//////	        showSerName(ctPieSer, false);
////	        // 9、绘制饼图
////	        chart.plot(pieChart);
////	        xwpfRun.getCTR().getDrawingArray(0).getInlineArray(0).getDocPr().setDescr("{{demo1}}");
//	        chart.plot(data);
//	        // 10、输出到word文档
//	        FileOutputStream fos = new FileOutputStream("D:\\software\\chart\\charttest2.docx");
//	        document.write(fos); // 导出word
//
//	        // 11、关闭流r
//	        fos.close();
//	        document.close();
//	    }
	 
	// 控制分类名称是否显示
	 public static void showCateName(CTPieSer series, boolean val) {
	   if (series.getDLbls().isSetShowCatName()) {
	     series.getDLbls().getShowCatName().setVal(val);
	   } else {
	     series.getDLbls().addNewShowCatName().setVal(val);
	   }
	 }

	 // 控制值是否显示
	 public static void showVal(CTPieSer series, boolean val) {
	   if (series.getDLbls().isSetShowVal()) {
	     series.getDLbls().getShowVal().setVal(val);
	   } else {
	     series.getDLbls().addNewShowVal().setVal(val);
	   }
	 }
	 
	 public static void showPercent(CTPieSer series, boolean val) {
		   if (series.getDLbls().isSetShowPercent()) {
		     series.getDLbls().getShowPercent().setVal(val);
		   } else {
		     series.getDLbls().addNewShowPercent().setVal(val);
		   }
		 }

	 // 控制值系列名称是否显示
	 public static void showSerName(CTPieSer series, boolean val) {
	   if (series.getDLbls().isSetShowSerName()) {
	     series.getDLbls().getShowSerName().setVal(val);
	   } else {
	     series.getDLbls().addNewShowSerName().setVal(val);
	   }
	 }

	 // 控制图例标识是否显示
	 public static void showLegendKey(CTPieSer series, boolean val) {
	   if (series.getDLbls().isSetShowLegendKey()) {
	     series.getDLbls().getShowLegendKey().setVal(val);
	   } else {
	     series.getDLbls().addNewShowLegendKey().setVal(val);
	   }
	 }

}
