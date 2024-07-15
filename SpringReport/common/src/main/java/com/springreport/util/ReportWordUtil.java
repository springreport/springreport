package com.springreport.util;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TableWidthType;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import com.alibaba.fastjson.JSONObject;
import com.springreport.constants.Constants;
import com.springreport.enums.LuckySheetPropsEnum;

/**  
 * @ClassName: ReportWordUtil
 * @Description: 报表导出word工具类
 * @author caiyang
 * @date 2022-05-31 07:48:59 
*/  
public class ReportWordUtil {

	public static void export(HttpServletResponse response) {
		XWPFDocument doc = new XWPFDocument();// 创建Word文件
		//创建一个6行8列的表格
		XWPFTable table1 = doc.createTable(6, 8);
	    //列宽自适应
	    table1.setWidthType(TableWidthType.AUTO);
	    // 获取到刚刚插入的行 第一行，下标为0
	    XWPFTableRow row1 = table1.getRow(0);
	    // 设置单元格内容
	    row1.getCell(0).setText("序号");
	    row1.getCell(1).setText("指标名称");
	    row1.getCell(2).setText("单位");
	    row1.getCell(3).setText("数值");
	    row1.getCell(4).setText("序号");
	    row1.getCell(5).setText("指标名称");
	    row1.getCell(6).setText("单位");
	    row1.getCell(7).setText("数值");
	    XWPFTableRow row2 = table1.getRow(1);
	    row2.getCell(0).setText("1");
	    row2.getCell(1).setText("核心面积");
	    row2.getCell(2).setText("亩");
	    row2.getCell(3).setText("getKernel");
	    row2.getCell(4).setText("6");
	    row2.getCell(5).setText("当年总产值");
	    row2.getCell(6).setText("万元");
	    row2.getCell(7).setText("getOverallIndustry");
	    XWPFTableRow row3 = table1.getRow(2);
	    row3.getCell(0).setText("2");
	    row3.getCell(1).setText("其中：建设用地");
	    row3.getCell(2).setText("亩");
	    row3.getCell(3).setText("getConstruction");
	    row3.getCell(4).setText("7");
	    row3.getCell(5).setText("第一产业产值");
	    row3.getCell(6).setText("万元");
	    row3.getCell(7).setText("getOneIndustry");
	    XWPFTableRow row4 = table1.getRow(3);
	    row4.getCell(0).setText("3");
	    row4.getCell(1).setText("示范区面积");
	    row4.getCell(2).setText("亩");
	    row4.getCell(3).setText("getDemonstrate");
	    row4.getCell(4).setText("8");
	    row4.getCell(5).setText("第二产业产值");
	    row4.getCell(6).setText("万元");
	    row4.getCell(7).setText("getTwoIndustry");
	    XWPFTableRow row5 = table1.getRow(4);
	    row5.getCell(0).setText("4");
	    row5.getCell(1).setText("辐射区面积");
	    row5.getCell(2).setText("亩");
	    row5.getCell(3).setText("getRadiation");
	    row5.getCell(4).setText("9");
	    row5.getCell(5).setText("第三产业产值");
	    row5.getCell(6).setText("万元");
	    row5.getCell(7).setText("getThreeIndustry");
	    XWPFTableRow row6 = table1.getRow(5);
	    row6.getCell(0).setText("5");
	    row6.getCell(1).setText("核心区从业人员");
	    row6.getCell(2).setText("人");
	    row6.getCell(3).setText("getPractitioner");
	    doc.setTable(0, table1);

	    try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.addHeader("Content-Disposition", "attachment;filename=" +URLEncoder.encode("年度总结", "UTF-8") + ".docx");
            //excel导出的路径和名称
            OutputStream out = response.getOutputStream();
            doc.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

	}
	
	/**  
	 * @MethodName: export
	 * @Description: 导出word
	 * @author caiyang
	 * @param map 行，列和数据
	 * @param httpServletResponse
	 * @param filename 文件名
	 * @date 2022-05-31 08:29:47 
	 */  
	public static void export(List<Map<String, Object>> cellDatas,Map<String, Integer> maxXAndY,Map<String, Map<String, Object>> hyperlinks,
			List<Object> borderInfos,Map<String, Object> rowlen,Map<String, Object> columnlen,HttpServletResponse httpServletResponse,
			String filename,JSONObject frozen)
	{
		int maxX = maxXAndY.get("maxX") + 1;
		int maxY = maxXAndY.get("maxY") + 1;
		XWPFDocument doc = new XWPFDocument();// 创建Word文件
		//创建一个maxX行maxY列的表格
		XWPFTable table1 = doc.createTable(maxX, maxY);
		table1.removeBorders();
		CTTblPr tblPr = table1.getCTTbl().getTblPr();
		tblPr.getTblW().setType(STTblWidth.PCT);
		tblPr.getTblW().setW(new BigInteger("5000"));
		 //列宽自适应
//	    table1.setWidthType(TableWidthType.DXA);
//	    table1.setWidth(7000);
	    setWordCellValue(cellDatas,table1);
		doc.setTable(0, table1);
		try {
			//设置文件名编码格式
	        filename = URLEncoder.encode(filename, "UTF-8");
			httpServletResponse.setCharacterEncoding("UTF-8");
			httpServletResponse.setHeader("content-Type", "application/vnd.ms-excel");
			httpServletResponse.addHeader("Content-Disposition", "attachment;filename=" +filename + ".docx");
		    httpServletResponse.addHeader("filename", filename + ".docx");
            //excel导出的路径和名称
            OutputStream out = httpServletResponse.getOutputStream();
            doc.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/**  
	 * @MethodName: setWordCellValue
	 * @Description: 向创建的word表格单元格中填充值
	 * @author caiyang
	 * @param cellDatas
	 * @param table 
	 * @return void
	 * @date 2022-06-01 08:30:54 
	 */  
	private static void setWordCellValue(List<Map<String, Object>> cellDatas,XWPFTable table)
	{
		Map<Integer, XWPFTableRow > map = new HashMap<>();
		XWPFTableRow xWPFTableRow = null;
		for (int i = 0; i < cellDatas.size(); i++) {
			Map<String, Object> cellData = cellDatas.get(i);
			int rowIndex = Integer.valueOf(String.valueOf(cellData.get(LuckySheetPropsEnum.R.getCode())));
			int colIndex = Integer.valueOf(String.valueOf(cellData.get(LuckySheetPropsEnum.C.getCode())));
			if(map.get(rowIndex) == null)
			{
				xWPFTableRow = table.getRow(rowIndex);
				map.put(rowIndex, xWPFTableRow);
			}else {
				xWPFTableRow = map.get(rowIndex);
			}
			Map<String, Object> cellValue = (Map<String, Object>) cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode());
			Map<String, Object> cellType = (Map<String, Object>) cellValue.get(LuckySheetPropsEnum.CELLTYPE.getCode());
			String t = "s";
			if(cellType != null) {
				t = String.valueOf(cellType.get(LuckySheetPropsEnum.TYPE.getCode()));
			}
			//解析单元格样式
			Map<String, Object> cellConfig = (Map<String, Object>) cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode());
			Map<String, Object> styleMap = LuckySheetCellUtil.getCellStyleMap(cellConfig,true);
			XWPFTableCell cell = xWPFTableRow.getCell(colIndex);
			XWPFParagraph XWPFParagraph = cell.addParagraph();
			//对齐方式
			String horizontal = String.valueOf(styleMap.get("horizontal"));
			switch (horizontal) {
			case "0":
				XWPFParagraph.setAlignment(ParagraphAlignment.CENTER);
				break;
			case "1":
				XWPFParagraph.setAlignment(ParagraphAlignment.LEFT);
				break;
			case "2":
				XWPFParagraph.setAlignment(ParagraphAlignment.RIGHT);
				break;
			}
			String vertical = String.valueOf(styleMap.get("vertical"));
			switch (vertical) {
			case "0":
				XWPFParagraph.setVerticalAlignment(TextAlignment.CENTER);
				break;
			case "1":
				XWPFParagraph.setVerticalAlignment(TextAlignment.TOP);
				break;
			case "2":
				XWPFParagraph.setVerticalAlignment(TextAlignment.BOTTOM);
				break;
			}
			XWPFRun XWPFRun = XWPFParagraph.createRun();
			//字体设置
			String fontName = String.valueOf(styleMap.get("fontName"));
			XWPFRun.setFontFamily(fontName);
			//是否加粗
			boolean bold = (boolean) styleMap.get("bold");
			XWPFRun.setBold(bold);
			//是否斜体
			boolean italic = (boolean) styleMap.get("italic");
			XWPFRun.setItalic(italic);
			//删除线
			boolean strikeOut = (boolean) styleMap.get("strikeOut");
			XWPFRun.setStrikeThrough(strikeOut);
			//下划线
			byte underLine = (byte) styleMap.get("underLine");
			if(Font.U_SINGLE == underLine)
			{
				XWPFRun.setUnderline(UnderlinePatterns.SINGLE);
			}else {
				XWPFRun.setUnderline(UnderlinePatterns.NONE);
			}
			int[] fontColor =  (int[]) styleMap.get("fontColor");
			String color = StringUtil.rgb2Hex(fontColor[0],fontColor[1],fontColor[2]);
			XWPFRun.setColor(color);
			//字体大小
			String fontSize = String.valueOf(styleMap.get("fontSize"));
			XWPFRun.setFontSize(Integer.valueOf(fontSize));
			XWPFParagraph.setWordWrapped(true);
			cell.setParagraph(XWPFParagraph);
			if(LuckySheetPropsEnum.INLINESTR.getCode().equals(t))
			{
				List<Map<String, Object>> list = (List<Map<String, Object>>) cellType.get(LuckySheetPropsEnum.STRING.getCode());
				if(list.get(0).get(LuckySheetPropsEnum.CELLVALUE.getCode()) != null)
				{
					String text = String.valueOf(list.get(0).get(LuckySheetPropsEnum.CELLVALUE.getCode()));
					if(text.contains("\r\n"))
					{
						String[] splitStrings = text.split("\r\n");
						for(String te : splitStrings){
							XWPFRun.setText(te.trim());
							XWPFRun.addBreak();
						}
					}else {
						XWPFRun.setText(text);
					}
				}else {
					XWPFRun.setText("");
				}
			}else {
				if(cellValue.get(LuckySheetPropsEnum.CELLVALUE.getCode()) != null)
				{
					String text = String.valueOf(cellValue.get(LuckySheetPropsEnum.CELLVALUE.getCode()));
					if(text.contains("\r\n"))
					{
						String[] splitStrings = text.split("\r\n");
						for(String te : splitStrings){
							XWPFRun.setText(te.trim());
							XWPFRun.addBreak();
						}
					}else {
						XWPFRun.setText(text);
					}
				}else {
					XWPFRun.setText("");
				}
			}
			Map<String, Object> mergeCell = (Map<String, Object>) cellValue.get(LuckySheetPropsEnum.MERGECELLS.getCode());
			if(mergeCell != null) {
				if(mergeCell.containsKey(LuckySheetPropsEnum.ROWSPAN.getCode()) && mergeCell.containsKey(LuckySheetPropsEnum.COLSPAN.getCode()))
				{
					int firstRow = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.R.getCode())));
					int lastRow = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.R.getCode()))) + Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.ROWSPAN.getCode()))) - 1;
					int firstCol = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.C.getCode())));
					int lastCol = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.C.getCode()))) + Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.COLSPAN.getCode()))) - 1;
					for (int j = firstCol; j <= lastCol; j++) {
						mergeCellsVertically(table,j,firstRow,lastRow);
					}
					for (int j = firstRow; j <= lastRow; j++) {
						mergeCellsHorizontal(table, j, firstCol, lastCol);
					}
					
				}
			}
		}
	}
	
	/**
	 * word单元格列合并
	 * @param table 表格
	 * @param row 合并列所在行
	 * @param startCell 开始列
	 * @param endCell 结束列
	 * @date 2020年4月8日 下午4:43:54
	 */
	public static void mergeCellsHorizontal(XWPFTable table, int row, int startCell, int endCell) {
		for (int i = startCell; i <= endCell; i++) {
			XWPFTableCell cell = table.getRow(row).getCell(i);
			if (i == startCell) {
				// The first merged cell is set with RESTART merge value  
				cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
			} else {
				// Cells which join (merge) the first one, are set with CONTINUE  
				cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
			}
		}
	}

	/**
	 * word单元格行合并
	 * @param table 表格
	 * @param col 合并行所在列
	 * @param fromRow 开始行
	 * @param toRow 结束行
	 * @date 2020年4月8日 下午4:46:18
	 */
	public static void mergeCellsVertically(XWPFTable table, int col, int startRow, int endRow) {
		for (int i = startRow; i <= endRow; i++) {
			XWPFTableCell cell = table.getRow(i).getCell(col);
			if (i == startRow) {
				// The first merged cell is set with RESTART merge value  
				cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
			} else {
				// Cells which join (merge) the first one, are set with CONTINUE  
				cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
			}
		}
	}
	
}
