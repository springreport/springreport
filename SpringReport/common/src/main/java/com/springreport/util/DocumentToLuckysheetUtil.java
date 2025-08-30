package com.springreport.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.SheetVisibility;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFColorScaleFormatting;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFConditionalFormatting;
import org.apache.poi.xssf.usermodel.XSSFConditionalFormattingRule;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.opencsv.CSVReader;
import com.springreport.constants.Constants;

import cn.hutool.poi.excel.ExcelDateUtil;

/**  
 * @ClassName: DocumentToLuckysheetUtil
 * @Description: 文档转成luckysheet格式数据工具类
 * @author caiyang
 * @date 2023-11-09 06:37:07 
*/ 
public class DocumentToLuckysheetUtil {

	/**  
	 * @MethodName: xlsx2Luckysheet
	 * @Description: xlsx转成luckysheet
	 * @author caiyang
	 * @param file void
	 * @throws IOException 
	 * @throws EncryptedDocumentException 
	 * @date 2023-11-09 06:37:58 
	 */ 
	public static JSONArray xlsx2Luckysheet(MultipartFile file) throws Exception {
		 XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(file.getInputStream());
		 JSONArray result = parseWorkBook(workbook);
		 return result;
	}
	
	public static JSONArray xlsx2Luckysheet(InputStream inputStream) throws Exception {
		XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(inputStream);
		JSONArray result = parseWorkBook(workbook);
		return result;
	}
	
	private static JSONArray parseWorkBook(XSSFWorkbook workbook) throws Exception {
		 JSONArray result = new JSONArray();
		int sheetsNum = workbook.getNumberOfSheets();
		 XSSFSheet sheet = null;
		 FormulaEvaluator formulaEvaluator = new XSSFFormulaEvaluator(workbook);
		 for (int i = 0; i < sheetsNum; i++) {
			 JSONObject sheetData = new JSONObject();
			 JSONObject config = new JSONObject();//配置信息
			 JSONArray cellDatas = new JSONArray();
			 JSONObject configRowhidden = new JSONObject();//隐藏行信息
			 JSONObject configColhidden = new JSONObject();//隐藏列信息
			 JSONObject configMerge = new JSONObject();//合并单元格信息
			 JSONArray configBorderInfo = new JSONArray();//边框信息
			 JSONObject configRowlen = new JSONObject();//行高信息
			 JSONObject configColumnlen = new JSONObject();//列宽信息
			 JSONArray calcChain = new JSONArray();//公式链
			 JSONObject dataVerification = new JSONObject();//数据验证项
			 JSONObject authority = null;//单元格保护信息
			 JSONObject hyperlinks = new JSONObject();//超链接信息
			 JSONObject frozen = new JSONObject();//冻结信息
			 JSONObject filterSelect = new JSONObject();//筛选信息
			 JSONArray images = new JSONArray();//图片信息，图片返回的是byte字节流，需要自己在业务层进行处理，可以上传到服务器，也可以转成luckysheet可以识别的格式
			 sheet = workbook.getSheetAt(i);
			 String sheetIndex = "Sheet"+UUIDUtil.getUUID();
			 String sheetName = sheet.getSheetName();
			 //获取合并单元格信息
			 List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
			 JSONObject subMerges = new JSONObject();//合并单元格中非第一个单元格集合
			 JSONObject mergeFirstCellBorder = new JSONObject();//合并单元格中第一个单元格的边框信息
			 int rowEnd = sheet.getLastRowNum();
			 if(!ListUtil.isEmpty(mergedRegions))
			 {
				 for (CellRangeAddress mergedRegion : mergedRegions) {
					 int firstRow = mergedRegion.getFirstRow();
					 int lastRow = mergedRegion.getLastRow();
					 int firstColumn = mergedRegion.getFirstColumn();
					 int lastColumn = mergedRegion.getLastColumn();
					 JSONObject merge = new JSONObject();
					 merge.put("r", firstRow);
					 merge.put("c", firstColumn);
					 merge.put("rs", lastRow - firstRow + 1);
					 merge.put("cs", lastColumn - firstColumn + 1);
					 configMerge.put(firstRow+"_"+firstColumn, merge);
					 for (int j = firstRow; j <= lastRow; j++) {
						for (int k = firstColumn; k <= lastColumn; k++) {
							if(j != firstRow || k != firstColumn)
							{
								subMerges.put(j+"_"+k, firstRow+"_"+firstColumn);
							}
						}
					 }
				 }
			 }
			//获取数据验证项
			 List<DataValidation> list = ReadExcelDataValidaton.getDataValidations(sheet);
			 if(!ListUtil.isEmpty(list))
			 {
				 for (int j = 0; j < list.size(); j++) {
					 DataValidation dataValidation = list.get(j);
					 DataValidationConstraint dataValidationConstraint = dataValidation.getValidationConstraint();
					 int valudationType = dataValidationConstraint.getValidationType();
					 String forumla1 = dataValidationConstraint.getFormula1();
					 String forumla2 = dataValidationConstraint.getFormula2();
					 Integer operator = dataValidationConstraint.getOperator();
					 CellRangeAddressList cellRangeAddressList = dataValidation.getRegions();
			         CellRangeAddress[] cellRangeAddresses = cellRangeAddressList.getCellRangeAddresses();
			         for (int t = 0; t < cellRangeAddresses.length; t++) {
			        	CellRangeAddress cellRangeAddress = cellRangeAddresses[t];
			        	int firstRow = cellRangeAddress.getFirstRow();
			        	int lastRow = cellRangeAddress.getLastRow();
			        	int firstColumn = cellRangeAddress.getFirstColumn();
			        	int lastColumn = cellRangeAddress.getLastColumn();
			        	List<String> cells = SheetUtil.getRangeCells(firstRow, lastRow>rowEnd?rowEnd:lastRow, firstColumn, lastColumn);
			        	if(!ListUtil.isEmpty(cells))
			        	{
			        		for (String cell : cells) {
			        			if(valudationType == 3)
								{//下拉选项
			        				JSONObject dataVerify = JSONObject.parseObject(Constants.DROPDOWN);
			        				dataVerify.put("value1", StringUtil.isNotEmpty(forumla1)?forumla1.replaceAll("\"", ""):null);
									dataVerification.put(cell, dataVerify); 
								}else if(valudationType == 1 || valudationType == 2 || valudationType == 6) {
			        				//整数或者小数或者字符串长度
			        				JSONObject dataVerify = JSONObject.parseObject(Constants.DATAVERIFY);
			        				if(valudationType == 1)
			        				{
			        					dataVerify.put("type", "number_integer");
			        				}else if(valudationType == 2)
			        				{
			        					dataVerify.put("type", "number_decimal");
			        				}else if(valudationType == 3)
			        				{
			        					dataVerify.put("type", "text_length");
			        				}
			        				switch (operator) {
									case 0:
										//介于
										dataVerify.put("type2", "bw");
										break;
									case 1:
										//未介于
										dataVerify.put("type2", "nb");
										break;
									case 2:
										//等于
										dataVerify.put("type2", "eq");
										break;
									case 3:
										//不等于
										dataVerify.put("type2", "ne");
										break;
									case 4:
										//大于
										dataVerify.put("type2", "gt");
										break;
									case 5:
										//小于
										dataVerify.put("type2", "lt");
										break;
									case 6:
										//大于或等于
										dataVerify.put("type2", "gte");
										break;
									case 7:
										//小于或等于
										dataVerify.put("type2", "lte");
										break;
									default:
										break;
									}
			        				dataVerify.put("value1", StringUtil.isNotEmpty(forumla1)?forumla1.replaceAll("\"", ""):null);
			        				dataVerify.put("value2", StringUtil.isNotEmpty(forumla2)?forumla2.replaceAll("\"", ""):null);
									dataVerification.put(cell, dataVerify); 
			        			}else if(valudationType == 4) {
			        				//日期
			        				JSONObject dataVerify = JSONObject.parseObject(Constants.DATAVERIFY);
			        				dataVerify.put("type", "date");
			        				switch (operator) {
									case 0:
										//介于
										dataVerify.put("type2", "bw");
										break;
									case 1:
										//未介于
										dataVerify.put("type2", "nb");
										break;
									case 2:
										//等于
										dataVerify.put("type2", "eq");
										break;
									case 3:
										//不等于
										dataVerify.put("type2", "ne");
										break;
									case 4:
										//大于 晚于
										dataVerify.put("type2", "af");
										break;
									case 5:
										//小于  早于
										dataVerify.put("type2", "bf");
										break;
									case 6:
										//大于或等于 不早于
										dataVerify.put("type2", "nbf");
										break;
									case 7:
										//小于或等于 不晚于
										dataVerify.put("type2", "naf");
										break;
									default:
										break;
									}
			        				dataVerify.put("value1", StringUtil.isNotEmpty(forumla1)?DateUtil.ExcelDoubleToDate(forumla1):null);
			        				dataVerify.put("value2", StringUtil.isNotEmpty(forumla2)?DateUtil.ExcelDoubleToDate(forumla2):null);
									dataVerification.put(cell, dataVerify); 
			        			}
							}
			        	}
			         }
				}
			 }
			 //获取批注信息
			 Map<CellAddress, ? extends Comment> cellComments = sheet.getCellComments();
			 JSONObject comments = new JSONObject();
			 if(!cellComments.isEmpty())
			 {
				 JSONObject comment = null;
				 for (CellAddress cellAddress : cellComments.keySet()) {
					 comment = JSONObject.parseObject(Constants.COMMENT);
					 int c = cellAddress.getColumn();
					 int r = cellAddress.getRow();
					 XSSFComment cellComment = sheet.getCellComment(cellAddress);
					 String commentString = cellComment.getString().getString();
					 comment.put("value", commentString);
					 comment.put("isshow", cellComment.isVisible());
					 comments.put(r+"_"+c, comment);
				 } 
			 }
			 //获取工作表保护信息
			 if(sheet.getProtect())
			 {
				 Map<Integer, List<Integer>> colUnProtectCells = new HashMap<>();//每一列被保护的单元格
				 for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
					 Row row = sheet.getRow(j);
					 if(row != null)
					 {
						int columns = row.getLastCellNum();
						for (int k = 0; k < columns; k++) {
							Cell cell = row.getCell(k);
							if(cell != null)
							{
								CellStyle cellStyle = cell.getCellStyle();
								if(cellStyle != null)
								{
									if(!cellStyle.getLocked()) {
										if(colUnProtectCells.get(k) == null)
										{
											colUnProtectCells.put(k, new ArrayList<>());
										}
										colUnProtectCells.get(k).add(j);
									}
								}
							}
							
						}
					 }
				 }
				 if(!colUnProtectCells.isEmpty())
				 {
					 authority = JSONObject.parseObject(Constants.AUTHORITY);
					 JSONArray allowRangeList = new JSONArray();
					 int flag = 0;
					 for(Map.Entry entry : colUnProtectCells.entrySet()){
						 Integer key = (Integer) entry.getKey();
						 List<Integer> value = (List<Integer>) entry.getValue();
						 String column = SheetUtil.excelColIndexToStr(key+1);//获取列名
						 List<List<Integer>> serialNumList = ListUtil.getSerialNumList(value);
						 for (int j = 0; j < serialNumList.size(); j++) {
							JSONObject unProtectRange = JSONObject.parseObject(Constants.UNPROTECTCONFIG);
							int start = serialNumList.get(j).get(0)+1;
							int end = serialNumList.get(j).get(serialNumList.get(j).size() - 1)+1;
							unProtectRange.put("name", "Default"+flag);
							if(start == end)
							{
								unProtectRange.put("sqref", "$"+column+"$"+start);
							}else {
								unProtectRange.put("sqref", "$"+column+"$"+start+":"+"$"+column+"$"+end);
							}
							flag = flag + 1;
							allowRangeList.add(unProtectRange);
						 }
					 }
					 authority.put("allowRangeList", allowRangeList);
				 }
			 }
			//获取超链接
			 List<? extends Hyperlink> hyperlinkList = sheet.getHyperlinkList();
			 if(!ListUtil.isEmpty(hyperlinkList))
			 {
				 JSONObject link = null;
				 for (Hyperlink hyperlink : hyperlinkList) {
					 XSSFHyperlink xhyperlink = (XSSFHyperlink) hyperlink;
					 int startr = xhyperlink.getFirstRow();
					 int startc = xhyperlink.getFirstColumn();
					 int endr = xhyperlink.getLastRow();
					 int endc =xhyperlink.getLastColumn();
					 link = new JSONObject();
					 link.put("linkType", "external");
					 link.put("linkAddress", xhyperlink.getAddress());
					 link.put("linkTooltip", "");
					 for (int j = startr; j <= endr; j++) {
						for (int k = startc; k <= endc; k++) {
							hyperlinks.put(j+"_"+k, link);
						}
					}
				 } 
			 }
			 //获取冻结
			 if(sheet.getPaneInformation() != null && sheet.getPaneInformation().isFreezePane())
			 {
				 short row = sheet.getPaneInformation().getHorizontalSplitPosition();
				 short column = sheet.getPaneInformation().getVerticalSplitPosition();
				 
				 if(row == 1 && column ==0)
				 {//冻结首行
					 frozen.put("type", "row");
				 }else if(row == 0 && column == 1)
				 {//冻结首列
					 frozen.put("type", "column");
				 }else if(row == 1 && column == 1) {
					 //冻结首行A列
					 frozen.put("type", "both");
				 }else if(row == 0 && column > 1)
				 {//冻结列到选区
					 frozen.put("type", "rangeColumn");
					 JSONObject range = new JSONObject();
					 range.put("row_focus", 0);
					 range.put("column_focus", column-1);
					 frozen.put("range", range);
				 }else if(row > 1 && column == 0) {
					 //冻结行到选区
					 frozen.put("type", "rangeRow");
					 JSONObject range = new JSONObject();
					 range.put("row_focus", row-1);
					 range.put("column_focus", 0);
					 frozen.put("range", range);
				 }
				 else if((row >= 1 && column>1) || (row>1 && column >= 1))
				 {//冻结行列到选区
					 frozen.put("type", "rangeBoth");
					 JSONObject range = new JSONObject();
					 range.put("row_focus", row-1);
					 range.put("column_focus", column-1);
					 frozen.put("range", range);
				 }
			 }
			 //获取筛选信息
			 boolean isAutoFilter = sheet.getCTWorksheet().isSetAutoFilter();
			 if(isAutoFilter) {
				 CTAutoFilter autoFilter = sheet.getCTWorksheet().getAutoFilter();
				 String filterRange = autoFilter.getRef();
				 String firstCell = filterRange.split(":")[0];
				 String endCell = filterRange.split(":")[1];
				 int[] firstCellCoor = SheetUtil.convertFromExcelCoordinate(firstCell);
				 int[] endCellCoor = SheetUtil.convertFromExcelCoordinate(endCell);
				 JSONArray row = new JSONArray();
				 row.add(firstCellCoor[0]-1);
				 row.add(endCellCoor[0]-1);
				 filterSelect.put("row", row);
				 JSONArray column = new JSONArray();
				 column.add(firstCellCoor[1]-1);
				 column.add(endCellCoor[1]-1);
				 filterSelect.put("column", column);
			 }
			 //获取条件格式
			 JSONArray luckysheetConditionformatSave = new JSONArray();//条件格式
			 int formatNum = sheet.getSheetConditionalFormatting().getNumConditionalFormattings();
			 if(formatNum > 0) {
				 for (int j = 0; j < formatNum; j++) {
					 XSSFConditionalFormatting conditionalFormatting = sheet.getSheetConditionalFormatting().getConditionalFormattingAt(j);
					 int ruleNum = conditionalFormatting.getNumberOfRules();
					 if(ruleNum > 0) {
						 CellRangeAddress[] cellRangeAddress = conditionalFormatting.getFormattingRanges();
						 JSONArray cellrange = new JSONArray();
						 for (int k = 0; k < cellRangeAddress.length; k++) {
							JSONObject range = new JSONObject();
							JSONArray row = new JSONArray();
							JSONArray column = new JSONArray();
							row.add(cellRangeAddress[k].getFirstRow());
							row.add(cellRangeAddress[k].getLastRow());
							column.add(cellRangeAddress[k].getFirstColumn());
							column.add(cellRangeAddress[k].getLastColumn());
							range.put("row", row);
							range.put("column", column);
							cellrange.add(range);
						 }
						 JSONArray conditionFormats = new JSONArray();
						 for (int k = 0; k < ruleNum; k++) {
							 XSSFConditionalFormattingRule rule = conditionalFormatting.getRule(k);
							 if(rule.getPatternFormatting() == null) {
								 continue;
							 }
							 Class<?> clazz = rule.getClass();
							 Field field = clazz.getDeclaredField("_cfRule");
							 field.setAccessible(true);
							 CTCfRule cfRule = (CTCfRule)field.get(rule);
							 String ruleType = cfRule.getType().toString();
							 if("cellIs".equals(ruleType) || "containsText".equals(ruleType) || "duplicateValues".equals(ruleType) 
								|| "top10".equals(ruleType) || "aboveAverage".equals(ruleType)){
								 JSONObject cellConditionFormat = JSON.parseObject(Constants.CELL_RULE);
								 String operator = "";
								 if(cfRule.getOperator() == null) {
									 operator = ruleType;
								 }else {
									 operator = cfRule.getOperator().toString();
								 }
								 if("greaterThan".equals(operator) || "lessThan".equals(operator) || "equal".equals(operator) 
								 || "between".equals(operator) || "containsText".equals(operator) || "duplicateValues".equals(operator)
								 || "top10".equals(operator) || "aboveAverage".equals(operator)) {
									 String cellColor = "#ffffff";
									 if(rule.getPatternFormatting() != null) {
										 try {
											 cellColor = "#" + rule.getPatternFormatting().getFillBackgroundColorColor().getARGBHex().substring(2);
										} catch (Exception e) {
											// TODO: handle exception
										}
									 }
									 String textColor = "#000000";
									 if(rule.getFontFormatting() != null) {
										 try {
											 textColor = "#" + rule.getFontFormatting().getFontColor().getARGBHex().substring(2);
										} catch (Exception e) {
											// TODO: handle exception
										}
									 }
									 if("greaterThan".equals(operator)) {
										 String conditionValue = rule.getFormula1();
										 cellConditionFormat.put("conditionName", "greaterThan");
										 cellConditionFormat.getJSONArray("conditionValue").set(0, conditionValue);
									 }else if("lessThan".equals(operator)) {
										 String conditionValue = rule.getFormula1();
										 cellConditionFormat.put("conditionName", "lessThan");
										 cellConditionFormat.getJSONArray("conditionValue").set(0, conditionValue);
									 }else if("equal".equals(operator)) {
										 String conditionValue = rule.getFormula1();
										 cellConditionFormat.put("conditionName", "equal");
										 cellConditionFormat.getJSONArray("conditionValue").set(0, conditionValue);
									 }else if("between".equals(operator)) {
										 String conditionValue = rule.getFormula1();
										 cellConditionFormat.put("conditionName", "betweenness");
										 cellConditionFormat.getJSONArray("conditionValue").set(0, conditionValue);
										 cellConditionFormat.getJSONArray("conditionValue").add(rule.getFormula2());
									 }else if("containsText".equals(operator)) {
										 String conditionValue = cfRule.getText();
										 cellConditionFormat.put("conditionName", "textContains");
										 cellConditionFormat.getJSONArray("conditionValue").set(0, conditionValue);
									 }else if("duplicateValues".equals(operator)) {
										 cellConditionFormat.put("conditionName", "duplicateValue");
										 cellConditionFormat.getJSONArray("conditionValue").set(0, "0");
									 }else if("top10".equals(operator)) {
										 String conditionValue = String.valueOf(cfRule.getRank());
										 if(cfRule.getBottom()) {
											 if(cfRule.getPercent()) {
												 cellConditionFormat.put("conditionName", "last10%"); 
											 }else {
												 cellConditionFormat.put("conditionName", "last10"); 
											 }
										 }else {
											 if(cfRule.getPercent()) {
												 cellConditionFormat.put("conditionName", "top10%"); 
											 }else {
												 cellConditionFormat.put("conditionName", "top10"); 
											 }
										 }
										 cellConditionFormat.getJSONArray("conditionValue").set(0, conditionValue);
									 }else if("aboveAverage".equals(operator)) {
										 if(cfRule.getAboveAverage()) {
											 cellConditionFormat.put("conditionName", "aboveAverage");
											 cellConditionFormat.getJSONArray("conditionValue").set(0, "aboveAverage");
										 }else {
											 cellConditionFormat.put("conditionName", "SubAverage"); 
											 cellConditionFormat.getJSONArray("conditionValue").set(0, "SubAverage");
										 }
										
									 }
									 cellConditionFormat.getJSONObject("format").put("textColor", textColor);
									 cellConditionFormat.getJSONObject("format").put("cellColor", cellColor);
									 cellConditionFormat.put("cellrange", cellrange);
									 conditionFormats.add(cellConditionFormat); 
								 }
							 }else if("dataBar".equals(ruleType)) {
								 JSONObject dataBarConditionFormat = JSON.parseObject(Constants.DATA_BAR_RULE);
								 String color = "#" + rule.getDataBarFormatting().getColor().getARGBHex().substring(2);
								 dataBarConditionFormat.getJSONArray("format").set(0, color);
								 dataBarConditionFormat.put("cellrange", cellrange);
								 conditionFormats.add(dataBarConditionFormat); 
							 }else if("colorScale".equals(ruleType)) {
								 JSONObject colorScaleConditionFormat = JSON.parseObject(Constants.COLORGRADATION_RULE);
								 XSSFColorScaleFormatting colorScaleFormatting = rule.getColorScaleFormatting();
								 int numControlPoints = colorScaleFormatting.getNumControlPoints();
								 JSONArray format = new JSONArray();
								 for (int l = numControlPoints-1; l >= 0; l--) {
									String color =  "#" + colorScaleFormatting.getColors()[l].getARGBHex().substring(2);
									int[] rgbs = StringUtil.hexToRgb(color);
									format.add("rgb("+rgbs[0]+","+rgbs[1]+","+rgbs[2]+")");
								 }
								 colorScaleConditionFormat.put("cellrange", cellrange);
								 colorScaleConditionFormat.put("format", format);
								 conditionFormats.add(colorScaleConditionFormat); 
							 }else if("iconSet".equals(ruleType)) {
								 //部分图标集不支持：3个三角形、3个星形、5个框
								 JSONObject iconSetConditionFormat = JSON.parseObject(Constants.ICON_SET_RULE);
								 String iconSet = cfRule.getIconSet().getIconSet().toString();
								 JSONObject format = new JSONObject();
								 if("3Arrows".equals(iconSet)) {//三向箭头 彩色
									 format.put("len", "3");
									 format.put("top", "0");
									 format.put("leftMin", "0");
								 }else if("3ArrowsGray".equals(iconSet)) {//三向箭头 灰色
									 format.put("len", "3");
									 format.put("top", "0");
									 format.put("leftMin", "5");
								 }else if("4ArrowsGray".equals(iconSet)) {//四向箭头 灰色
									 format.put("len", "4");
									 format.put("top", "1");
									 format.put("leftMin", "5");
								 }else if("4Arrows".equals(iconSet)) {//四向箭头 彩色
									 format.put("len", "4");
									 format.put("top", "2");
									 format.put("leftMin", "0");
								 }else if("5ArrowsGray".equals(iconSet)) {//五向箭头 灰色
									 format.put("len", "5");
									 format.put("top", "2");
									 format.put("leftMin", "5");
								 }else if("5Arrows".equals(iconSet)) {//五向箭头 彩色
									 format.put("len", "5");
									 format.put("top", "3");
									 format.put("leftMin", "0");
								 }else if("3TrafficLights1".equals(iconSet)) {//三色交通灯 无边框
									 format.put("len", "3");
									 format.put("top", "4");
									 format.put("leftMin", "0");
								 }else if("3TrafficLights2".equals(iconSet)) {//三色交通灯 有边框
									 format.put("len", "3");
									 format.put("top", "4");
									 format.put("leftMin", "5");
								 }else if("3Signs".equals(iconSet)) {//三标志
									 format.put("len", "3");
									 format.put("top", "5");
									 format.put("leftMin", "0");
								 }else if("4TrafficLights".equals(iconSet)) {//四色交通灯
									 format.put("len", "5");
									 format.put("top", "5");
									 format.put("leftMin", "5");
								 }else if("4RedToBlack".equals(iconSet)) {//绿-红-黑渐变
									 format.put("len", "4");
									 format.put("top", "6");
									 format.put("leftMin", "0");
								 }else if("3Symbols".equals(iconSet)) {//三个符号 有圆圈
									 format.put("len", "3");
									 format.put("top", "7");
									 format.put("leftMin", "0");
								 }else if("3Symbols2".equals(iconSet)) {//三个符号 无圆圈
									 format.put("len", "3");
									 format.put("top", "7");
									 format.put("leftMin", "5");
								 }else if("3Flags".equals(iconSet)) {//三色旗
									 format.put("len", "3");
									 format.put("top", "8");
									 format.put("leftMin", "0");
								 }else if("4Rating".equals(iconSet)) {//四等级
									 format.put("len", "4");
									 format.put("top", "9");
									 format.put("leftMin", "5");
								 }else if("5Quarters".equals(iconSet)) {//五象限图
									 format.put("len", "5");
									 format.put("top", "10");
									 format.put("leftMin", "0");
								 }else if("5Rating".equals(iconSet)) {//五等级
									 format.put("len", "5");
									 format.put("top", "10");
									 format.put("leftMin", "5");
								 }
								 iconSetConditionFormat.put("cellrange", cellrange);
								 iconSetConditionFormat.put("format", format);
								 conditionFormats.add(iconSetConditionFormat); 
							 }
						}
						luckysheetConditionformatSave.addAll(conditionFormats);
					 }
				}
			 }
            int maxColumn = -1;
            HashMap<String, Object> cellBorders = new LinkedHashMap<>();
            for (int rowNum = 0; rowNum <= rowEnd; rowNum++) {
           	 XSSFRow row = (XSSFRow) sheet.getRow(rowNum);
           	 if(row == null)
           	 {
           		 continue;
           	 }
           	 if(row.getZeroHeight())
           	 {//是否是隐藏行
           		 configRowhidden.put(String.valueOf(rowNum), 0);
           	 }else {
           		 int rowHeight = (int) Math.ceil(row.getHeightInPoints());
           		 if(rowHeight != 15)
           		 {
           			 configRowlen.put(String.valueOf(rowNum), new Long(Math.round(row.getHeightInPoints()/0.75)).intValue());
           		 }
           	 }
           	 for (int j = 0; j < row.getLastCellNum(); j++) {
           		 if(j > maxColumn)
           		 {
           			 if(sheet.isColumnHidden(j))
           			 {
           				 configColhidden.put(String.valueOf(j), 0);
           			 }else {
           				 int columnWidth = Math.round(sheet.getColumnWidthInPixels(j));
           				 if(columnWidth != 56)
           				 {
           					 configColumnlen.put(String.valueOf(j), new Long(Math.round(sheet.getColumnWidthInPixels(j)/0.875)).intValue());
           				 }
           			 }
           			 maxColumn = j;
           		 }
           		 XSSFCell cell = row.getCell(j);
           		 if(cell != null)
           		 {
           			 if(!subMerges.containsKey(rowNum+"_"+j))
           			 {
           				 JSONObject cellInfo = new JSONObject();//单元格信息
           				 cellInfo.put("r", rowNum);
           				 cellInfo.put("c", j);
           				 cellInfo.put("id", IdWorker.getId());
                   		 JSONObject v = new JSONObject();
                   		 JSONObject ct = new JSONObject();
                   		 ct.put("t", "g");
                   		 ct.put("fa", "General");
                   		 v.put("ct", ct);
                   		 cellInfo.put("v", v);
               			 Object value = null;
               			 Object mValue = null;
               			 XSSFRichTextString xSSFRichTextString = null;
                   		 if (cell.getCellType() == CellType.NUMERIC) {
                   			 value = cell.getNumericCellValue();
                                DataFormatter formatter = new DataFormatter();
                                final CellStyle cellStyle = cell.getCellStyle();
                                short format = cell.getCellStyle().getDataFormat();
                                if(ExcelDateUtil.isDateFormat(cell))
                                {
                               	 cell.getNumericCellValue();
                               	 SimpleDateFormat sdf = null;
                               	 if (format == 20 || format == 32) {  
                               		 sdf = new SimpleDateFormat("HH:mm");  
                               	 }else if(format == 14)
                               	 {
                               		 sdf = new SimpleDateFormat("yyyy/MM/dd"); 
                               	 }else if(format == 31)
                               	 {
                               		 sdf = new SimpleDateFormat("yyyy年MM月dd日"); 
                               	 }else if(format == 58)
                               	 {
                               		 sdf = new SimpleDateFormat("M月d日"); 
                               	 }else if(format == 179)
                               	 {
                               		 sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm"); 
                               	 }else if(format == 180)
                               	 {
                               		 sdf = new SimpleDateFormat("yyyy-MM-dd"); 
                               	 }else if(format == 181)
                               	 {
                               		 sdf = new SimpleDateFormat("HH:mm:ss"); 
                               	 }else {
                               		 sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
                               	 }
                               	 value = sdf.format(cell.getDateCellValue());// 日期
                               	 mValue = value;
                                }else {
                                 value = cell.getNumericCellValue();
                               	 mValue = formatter.formatRawCellContents(cell.getNumericCellValue(), cellStyle.getDataFormat(), cellStyle.getDataFormatString()); 
                                }
                                String dataformatString = cellStyle.getDataFormatString();
                                ct.put("fa", dataformatString);
                   		 }else if (cell.getCellType() == CellType.FORMULA) {
                   			 String dataformatString = "General";
                   			 try {
                   				 CellValue evaluate = formulaEvaluator.evaluate(cell);
                       			 final CellStyle cellStyle = cell.getCellStyle();
                       			 dataformatString = cellStyle.getDataFormatString();
                       			 value = evaluate.formatAsString();
                       			 mValue = value;
								} catch (Exception e) {
									e.printStackTrace();
								}
                   			 v.put("f", "="+cell.getCellFormula());
               				 JSONObject calc = new JSONObject();
               				 calc.put("r", rowNum);
               				 calc.put("c", j);
               				 calc.put("index", sheetIndex);
               				 calcChain.add(calc);
               				 ct.put("fa", dataformatString);
                   		 }
                   		 else if (cell.getCellType() == CellType.STRING) {
               				 xSSFRichTextString = cell.getRichStringCellValue();
               				 if(xSSFRichTextString.numFormattingRuns() == 0)
               				 {
               					 value = cell.getStringCellValue();
               					 mValue = cell.getStringCellValue();
               					 xSSFRichTextString = null;
               				 }
               				 ct.put("t", "s");
                       		 ct.put("fa", "@");
                            }else if (cell.getCellType() == CellType.BOOLEAN) {
                           	 value = cell.getBooleanCellValue();
                           	 mValue = cell.getBooleanCellValue();
                            } else if (cell.getCellType() == CellType.ERROR) {
                           	 value = cell.getErrorCellValue();
                           	 mValue = cell.getErrorCellValue();
                            }
               			 if(value != null)
               			 {
               				 v.put("v", value);
               				 v.put("m", String.valueOf(mValue));
               			 }
               			 if(cell.getCellStyle().getFillForegroundColorColor() != null)
               			 {
               				 String bg = cell.getCellStyle().getFillForegroundColorColor().getARGBHex();//背景颜色
                   			 if(StringUtil.isNotEmpty(bg))
                   			 {
                   				 bg = "#"+bg.substring(2);
                   				 v.put("bg", bg);
                   			 } 
               			 }
               			 if(xSSFRichTextString == null)
               			 {
                   			 XSSFFont font = workbook.getFontAt(cell.getCellStyle().getFontIndex());
                   			 JSONObject fontStyle = getFontStyle(font);
                   			 v.putAll(fontStyle);
               			 }else {
               				 String cellVal = xSSFRichTextString.getString();
               				 JSONArray s = new JSONArray();
               				 int size = xSSFRichTextString.numFormattingRuns();// 该富文本使用的格式的数量
               				 ct.put("t", "inlineStr");
               				 int subLength = 0;
               				 for (int t = 0; t < size; t++) {
               					 XSSFFont font = xSSFRichTextString.getFontOfFormattingRun(t);
               					 JSONObject fontStyle = getFontStyle(font);
               					 fontStyle.put("v", cellVal.substring(subLength, (subLength + xSSFRichTextString.getLengthOfFormattingRun(t))>cellVal.length()?cellVal.length():(subLength + xSSFRichTextString.getLengthOfFormattingRun(t))));
               					 subLength = subLength + xSSFRichTextString.getLengthOfFormattingRun(t);
               					 s.add(fontStyle);
               				 }
               				 ct.put("s", s);
               			 }
               			 VerticalAlignment verticalAlignment =  cell.getCellStyle().getVerticalAlignment();
               			 if(verticalAlignment != null)
               			 {
               				 if(verticalAlignment.getCode() == VerticalAlignment.BOTTOM.getCode())
               				 {
               					 v.put("vt", "2");
               				 } else if(verticalAlignment.getCode() == VerticalAlignment.TOP.getCode()) {
               					 v.put("vt", "1");
               				 }else if(verticalAlignment.getCode() == VerticalAlignment.CENTER.getCode()) {
               					 v.put("vt", "0");
               				 }
               			 }else {
               				 v.put("vt", "0");
               			 }
               			 HorizontalAlignment horizontalAlignment = cell.getCellStyle().getAlignment();
               			 if(horizontalAlignment != null)
               			 {
               				 if(horizontalAlignment.getCode() == HorizontalAlignment.CENTER.getCode())
               				 {
               					 v.put("ht", "0");
               				 }else if(horizontalAlignment.getCode() == HorizontalAlignment.LEFT.getCode())
               				 {
               					 v.put("ht", "1");
               				 }else if(horizontalAlignment.getCode() == HorizontalAlignment.RIGHT.getCode())
               				 {
               					 v.put("ht", "2");
               				 }
               			 }else {
               				 v.put("ht", "1");
               			 }
               			 if(cell.getCellStyle().getWrapText())
               			 {
               				 v.put("tb", "2");
               			 }else {
               				 v.put("tb", "0"); 
               			 }
               			 if(comments.containsKey(rowNum + "_" + j))
               			 {
               				 v.put("ps", comments.get(rowNum + "_" + j));
               			 }
               			 XSSFCell lastMergeCell = null;
               			 if(configMerge.containsKey(rowNum + "_" + j))
               			 {
               				 JSONObject merge = configMerge.getJSONObject(rowNum + "_" + j);
               				 if(merge != null) {
               					 int rs = merge.getIntValue("rs");
               					 int cs = merge.getIntValue("cs");
               					 int r = rowNum+rs-1;
               					 int c = j+cs-1;
               					 XSSFRow row1 = (XSSFRow) sheet.getRow(r);
               					 if(row1 != null) {
               						 lastMergeCell = row1.getCell(c);
               					 }
               				 }
               			 }
               			 if(lastMergeCell != null)
               			 {
               				 getColumnBorder(cell.getCellStyle(),rowNum,j,cellBorders,lastMergeCell.getCellStyle(),mergeFirstCellBorder);
               			 }else {
               				 getColumnBorder(cell.getCellStyle(),rowNum,j,cellBorders,null,mergeFirstCellBorder);
               			 }
               			 short rotation = cell.getCellStyle().getRotation();
               			 switch (rotation) {
							 case 45:
								 v.put("tr", "1"); 
								break;
							 case 135:
								 v.put("tr", "2"); 
								break;
							 case 255:
								 v.put("tr", "3"); 
								break;
							 case 90:
								 v.put("tr", "4"); 
								break;
							 case 180:
								 v.put("tr", "5"); 
								break;
							 default:
								break;
							}
               			cellDatas.add(cellInfo);
           			 }else {
           				 String rowCol = subMerges.getString(rowNum+"_"+j);
           				 if(mergeFirstCellBorder.containsKey(rowCol))
           				 {
           					 JSONArray columnBorder = null;
           					 if(cellBorders.containsKey(j+""))
           					 {
           						columnBorder = (JSONArray) cellBorders.get(j+"");
           					 }else {
           						columnBorder = new JSONArray();
           						cellBorders.put(j+"", columnBorder);
           					 } 
           					 JSONObject cellBorder = JSON.parseObject(JSON.toJSONString(mergeFirstCellBorder.getJSONObject(rowCol)));
           					 cellBorder.put("r", rowNum);
           					 cellBorder.put("c", j);
           					 if(cellBorder.containsKey("cellBorder")) {
           						 JSONArray range = new JSONArray();
               					 JSONArray columnRange = new JSONArray();
               					 columnRange.add(j);
               					 columnRange.add(j);
               					 JSONArray rowRange = new JSONArray();
               					 rowRange.add(rowNum);
               					 rowRange.add(rowNum);
               					 JSONObject rangeObj = new JSONObject();
               					 rangeObj.put("column", columnRange);
               					 rangeObj.put("row", rowRange);
               					 range.add(rangeObj);
               					 for (int k = 0; k < cellBorder.getJSONArray("cellBorder").size(); k++) {
               						 cellBorder.getJSONArray("cellBorder").getJSONObject(k).put("range", range);
   								 }
           					 }
           					 columnBorder.add(cellBorder);
           				 }else {
           					 getColumnBorder(cell.getCellStyle(),rowNum,j,cellBorders,null,mergeFirstCellBorder); 
           				 }
           			 }
           		 }else {
           			 getColumnBorder(null,rowNum,j,cellBorders,null,mergeFirstCellBorder);
           		 }
           		 
				}
            }
            getSheetBorderInfo(configBorderInfo,cellBorders);
            //获取所有图片
			 XSSFDrawing xSSFDrawing = sheet.getDrawingPatriarch();
			 if(xSSFDrawing != null)
			 {
				 try {
					 List<XSSFShape> shapes = xSSFDrawing.getShapes();
					 if(!ListUtil.isEmpty(shapes))
					 {
						 for (XSSFShape shape : shapes) {
							 if(!(shape instanceof XSSFPicture))
							 {
								 continue;
							 }
							 XSSFPicture pic = (XSSFPicture) shape; 
							 XSSFClientAnchor anchor = (XSSFClientAnchor) shape.getAnchor();
				             int str = anchor.getRow1();
				             int edr = anchor.getRow2();
				             int stc = anchor.getCol1();
				             int edc = anchor.getCol2();
				             int dx1 = anchor.getDx1();
				             int dx2 = anchor.getDx2();
				             int dy1 = anchor.getDy1();
				             int dy2 = anchor.getDy2();
				             double top = LuckysheetUtil.calculateTop(configRowlen, str,configRowhidden)+dy1/Units.EMU_PER_PIXEL-2;
				 			 double left = LuckysheetUtil.calculateLeft(configColumnlen, stc,configColhidden)+dx1/Units.EMU_PER_PIXEL-2;
				 			 Object width = null;
				 			 if(edc>stc) {
				 				width = LuckysheetUtil.calculateWidth(configColumnlen, stc, edc-stc)-dx1/Units.EMU_PER_PIXEL+dx2/Units.EMU_PER_PIXEL;;
				 			 }else {
				 				width = (dx2-dx1)/Units.EMU_PER_PIXEL;
				 			 }
//				 			 Object width = (dx2-dx1)/Units.EMU_PER_PIXEL;
				 			 Object height = null;
				 			 if(edr>str) {
				 				height = LuckysheetUtil.calculateHeight(configRowlen, str, edr-str)-dy1/Units.EMU_PER_PIXEL+dy2/Units.EMU_PER_PIXEL; 
				 			 }else {
				 				height =  (dy2-dy1)/Units.EMU_PER_PIXEL;
				 			 }
				             XSSFPictureData xSSFPictureData = pic.getPictureData();
				             JSONObject imgInfo = JSONObject.parseObject(Constants.DEFAULT_IMG_INFO);
				 			 imgInfo.getJSONObject("default").put("top", top);
				 			 imgInfo.getJSONObject("default").put("left", left);
				 			 imgInfo.getJSONObject("default").put("width", width);
				 			 imgInfo.getJSONObject("default").put("height", height);
				 			 imgInfo.getJSONObject("crop").put("width", width);
				 			 imgInfo.getJSONObject("crop").put("height", height);
				 			 String imgType =  xSSFPictureData.getPackagePart().getContentTypeDetails().getSubType();
				 			 imgInfo.put("imgType", imgType);
				 			 imgInfo.put("imgBytes", xSSFPictureData.getData());//图片数据，需要在业务中单独处理，可以上传到服务器，也可以转成luckysheet可以识别的图片格式，使用src属性返回
				 			 images.add(imgInfo);
						 }
					 }
				} catch (Exception e) {
					e.printStackTrace();
				}
			 }
            SheetVisibility sheetVisibility = workbook.getSheetVisibility(i);
            sheetVisibility.name();
            config.put("merge", configMerge);
			 config.put("rowlen", configRowlen);
			 config.put("columnlen", configColumnlen);
			 config.put("rowhidden", configRowhidden);
			 config.put("colhidden", configColhidden);
			 config.put("borderInfo", configBorderInfo);
			 config.put("authority", authority);
			 sheetData.put("config", config);
			 sheetData.put("name", sheetName);
			 sheetData.put("index", sheetIndex);
			 sheetData.put("status", 0);
			 sheetData.put("order", i);
			 sheetData.put("hide", "VISIBLE".equals(sheetVisibility.name())?0:1);
			 sheetData.put("isPivotTable", false);
			 if(rowEnd > Constants.DEFAULT_SHEET_ROW)
			 {
				 sheetData.put("row", rowEnd); 
			 }else {
				 sheetData.put("row", Constants.DEFAULT_SHEET_ROW); 
			 }
			 if(maxColumn > Constants.DEFAULT_SHEET_COLUMN)
			 {
				 sheetData.put("column", maxColumn+1); 
			 }else {
				 sheetData.put("column", Constants.DEFAULT_SHEET_COLUMN); 
			 }
			 sheetData.put("celldata", cellDatas); 
			 sheetData.put("calcChain", calcChain); 
			 sheetData.put("frozen", frozen); 
			 sheetData.put("dataVerification", dataVerification);
			 sheetData.put("images", images);
			 sheetData.put("hyperlink", hyperlinks);
			 sheetData.put("filter_select", filterSelect);
			 sheetData.put("luckysheet_conditionformat_save", luckysheetConditionformatSave);
			 result.add(sheetData);
		 }
		 return result;
	}
	
	//获取文档中所有sheet页的名称
	public static List<String> getSheetsName(MultipartFile file) throws EncryptedDocumentException, IOException
	{
		List<String> result = new ArrayList<>();
		XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(file.getInputStream());
		int sheetsNum = workbook.getNumberOfSheets();
		XSSFSheet sheet = null;
		for (int i = 0; i < sheetsNum; i++) {
			sheet = workbook.getSheetAt(i);
			String sheetName = sheet.getSheetName();
			result.add(sheetName);
		}
		return result;
	}
	
	private static JSONObject getFontStyle(XSSFFont font) {
		JSONObject v = new JSONObject();
		if(font == null)
		{
			return v;
		}
		XSSFColor color = (XSSFColor)font.getXSSFColor();
        if(color != null)
        {//字体颜色
       	 	String fc = color.getARGBHex();
       	 	if(StringUtil.isNotEmpty(fc))
       	 	{
	       	 	fc = "#"+fc.substring(2);
	       	 	v.put("fc", fc);
       	 	}
        }
		 String ff = font.getFontName();//字体名称
		 if(StringUtil.isNotEmpty(ff))
		 {//字体名称
			 if(Constants.LUCKYSHEET_FONTS.contains(ff))
			 {
				 v.put("ff", ff); 
			 }else {
				 v.put("ff", Constants.DEFAULT_LUCKYSHEET_FONT); 
			 }
		 }
		 boolean isBold = font.getBold();
		 if(isBold)
		 {//粗体
			 v.put("bl", 1);
		 }
		 boolean italic = font.getItalic();
		 if(italic)
		 {//斜体
			 v.put("it", 1);
		 }
		 byte underline = font.getUnderline();
        boolean isUnderline = FontUnderline.SINGLE.getByteValue() == underline;
        if(isUnderline)
        {//下划线
       	 v.put("un", 1);
        }
        boolean strikeout = font.getStrikeout();
        if(strikeout)
        {//删除线
       	 v.put("cl", 1);
        }
        short fontSize = font.getFontHeightInPoints();
        v.put("fs", fontSize);
        return v;
	}
	
	/**  
	 * @MethodName: getColumnBorder
	 * @Description: 以列为单位存储每个单元格的边框信息
	 * @author caiyang
	 * @param cellStyle
	 * @param r
	 * @param c
	 * @param borderObject void
	 * @date 2023-11-12 08:39:40 
	 */ 
	private static void getColumnBorder(XSSFCellStyle cellStyle,int r,int c,HashMap<String, Object> cellBorders,XSSFCellStyle lastMergeCellStyle,JSONObject mergeFirstCellBorder) {
		JSONArray columnBorder = null;
		if(cellBorders.containsKey(c+""))
		{
			columnBorder = (JSONArray) cellBorders.get(c+"");
		}else {
			columnBorder = new JSONArray();
			cellBorders.put(c+"", columnBorder);
		}
		JSONObject cellBorder = getCellBorder(cellStyle, r, c,lastMergeCellStyle);
		if(lastMergeCellStyle != null) {
			mergeFirstCellBorder.put(r+"_"+c, cellBorder);
		}
		columnBorder.add(cellBorder);
	}
	
	//获取边框类型
	private static JSONObject getCellBorder(XSSFCellStyle cellStyle,int r,int c,XSSFCellStyle lastMergeCellStyle){
		JSONObject result = new JSONObject();
		result.put("r", r);
		result.put("c", c);
		if(cellStyle == null)
		{
			result.put("type", "border-none");
			return result;
		}
		JSONArray cellBorder = new JSONArray();
		BorderStyle topBorderStyle = cellStyle.getBorderTop();
		BorderStyle bottomBorderStyle = lastMergeCellStyle != null?lastMergeCellStyle.getBorderBottom():cellStyle.getBorderBottom();
		BorderStyle leftBorderStyle = cellStyle.getBorderLeft();
		BorderStyle rightBorderStyle = lastMergeCellStyle != null?lastMergeCellStyle.getBorderRight():cellStyle.getBorderRight();
		if(topBorderStyle != null && topBorderStyle.getCode() != BorderStyle.NONE.getCode() && bottomBorderStyle != null && bottomBorderStyle.getCode() != BorderStyle.NONE.getCode()
			&& leftBorderStyle != null && leftBorderStyle.getCode() != BorderStyle.NONE.getCode() && rightBorderStyle != null && rightBorderStyle.getCode() != BorderStyle.NONE.getCode())
		{
			JSONObject border = new JSONObject();
			border.put("borderType", "border-all");
			border.put("rangeType", "range");
			border.put("style", "1");
			border.put("color", "#000");
			JSONArray range = new JSONArray();
			JSONArray column = new JSONArray();
			column.add(c);
			column.add(c);
			JSONArray row = new JSONArray();
			row.add(r);
			row.add(r);
			JSONObject rangeObj = new JSONObject();
			rangeObj.put("column", column);
			rangeObj.put("row", row);
			range.add(rangeObj);
			border.put("range", range);
			cellBorder.add(border);
			result.put("type", "border-all");
			result.put("cellBorder", cellBorder);
		}else if(topBorderStyle == null && bottomBorderStyle == null && leftBorderStyle == null && rightBorderStyle == null)
		{
			result.put("type", "border-none");
		}else if(topBorderStyle != null && topBorderStyle.getCode() == BorderStyle.NONE.getCode() && bottomBorderStyle != null && bottomBorderStyle.getCode() == BorderStyle.NONE.getCode()
				&& leftBorderStyle != null && leftBorderStyle.getCode() == BorderStyle.NONE.getCode() && rightBorderStyle != null && rightBorderStyle.getCode() == BorderStyle.NONE.getCode()){
			result.put("type", "border-none");
		}else {
			if(topBorderStyle != null && topBorderStyle.getCode() != BorderStyle.NONE.getCode())
			{
				JSONObject border = new JSONObject();
				border.put("borderType", "border-top");
				border.put("rangeType", "range");
				border.put("style", "1");
				if(cellStyle.getTopBorderXSSFColor() != null)
				{
					String borderColor = cellStyle.getTopBorderXSSFColor().getARGBHex();
					if(StringUtil.isNotEmpty(borderColor))
					{
						borderColor = "#"+borderColor.substring(2);
						border.put("color", borderColor);
					}else {
						border.put("color", "#000");	
					}
				}else {
					border.put("color", "#000");
				}
				JSONArray range = new JSONArray();
				JSONArray column = new JSONArray();
				column.add(c);
				column.add(c);
				JSONArray row = new JSONArray();
				row.add(r);
				row.add(r);
				JSONObject rangeObj = new JSONObject();
				rangeObj.put("column", column);
				rangeObj.put("row", row);
				range.add(rangeObj);
				border.put("range", range);
				cellBorder.add(border);
			}
			if(bottomBorderStyle != null && bottomBorderStyle.getCode() != BorderStyle.NONE.getCode())
			{
				JSONObject border = new JSONObject();
				border.put("borderType", "border-bottom");
				border.put("rangeType", "range");
				border.put("style", "1");
				if(cellStyle.getBottomBorderXSSFColor() != null)
				{
					String borderColor = cellStyle.getBottomBorderXSSFColor().getARGBHex();
					if(StringUtil.isNotEmpty(borderColor))
					{
						borderColor = "#"+borderColor.substring(2);
						border.put("color", borderColor);
					}else {
						border.put("color", "#000");
					}
				}else {
					border.put("color", "#000");
				}
				JSONArray range = new JSONArray();
				JSONArray column = new JSONArray();
				column.add(c);
				column.add(c);
				JSONArray row = new JSONArray();
				row.add(r);
				row.add(r);
				JSONObject rangeObj = new JSONObject();
				rangeObj.put("column", column);
				rangeObj.put("row", row);
				range.add(rangeObj);
				border.put("range", range);
				cellBorder.add(border);
			}
			if(leftBorderStyle != null && leftBorderStyle.getCode() != BorderStyle.NONE.getCode())
			{
				JSONObject border = new JSONObject();
				border.put("borderType", "border-left");
				border.put("rangeType", "range");
				border.put("style", "1");
				if(cellStyle.getLeftBorderXSSFColor() != null)
				{
					String borderColor = cellStyle.getLeftBorderXSSFColor().getARGBHex();
					if(StringUtil.isNotEmpty(borderColor))
					{
						borderColor = "#"+borderColor.substring(2);
						border.put("color", borderColor);
					}else {
						border.put("color", "#000");
					}
				}else {
					border.put("color", "#000");
				}
				JSONArray range = new JSONArray();
				JSONArray column = new JSONArray();
				column.add(c);
				column.add(c);
				JSONArray row = new JSONArray();
				row.add(r);
				row.add(r);
				JSONObject rangeObj = new JSONObject();
				rangeObj.put("column", column);
				rangeObj.put("row", row);
				range.add(rangeObj);
				border.put("range", range);
				cellBorder.add(border);
			}
			if(rightBorderStyle != null && rightBorderStyle.getCode() != BorderStyle.NONE.getCode())
			{
				JSONObject border = new JSONObject();
				border.put("borderType", "border-right");
				border.put("rangeType", "range");
				border.put("style", "1");
				if(cellStyle.getRightBorderXSSFColor() != null)
				{
					String borderColor = cellStyle.getRightBorderXSSFColor().getARGBHex();
					if(StringUtil.isNotEmpty(borderColor))
					{
						borderColor = "#"+borderColor.substring(2);
						border.put("color", borderColor);
					}else {
						border.put("color", "#000");
					}
				}else {
					border.put("color", "#000");
				}
				JSONArray range = new JSONArray();
				JSONArray column = new JSONArray();
				column.add(c);
				column.add(c);
				JSONArray row = new JSONArray();
				row.add(r);
				row.add(r);
				JSONObject rangeObj = new JSONObject();
				rangeObj.put("column", column);
				rangeObj.put("row", row);
				range.add(rangeObj);
				border.put("range", range);
				cellBorder.add(border);
			}
			result.put("type", "border-other");
			result.put("cellBorder", cellBorder);
		}
		return result;
	}
	
	
	private static void getSheetBorderInfo(JSONArray configBorderInfo,HashMap<String, Object> cellBorders)
	{
		for (Map.Entry entry : cellBorders.entrySet()) {
			String key = (String) entry.getKey();
			JSONArray columnBorder = (JSONArray) entry.getValue();
			if(!ListUtil.isEmpty(columnBorder))
			{
				Integer str = null;
				Integer edr = null;
				String borderType = "";
				JSONArray tempBorders = null;
				for (int i = 0; i < columnBorder.size(); i++) {
					JSONObject cellBorder = columnBorder.getJSONObject(i);
					String cellBorderType = cellBorder.getString("type");
					int r = cellBorder.getIntValue("r");
					JSONArray cellBorderInfo = cellBorder.getJSONArray("cellBorder");
					if(StringUtil.isNullOrEmpty(borderType))
					{
						str = r;
						edr = r;
						borderType = cellBorderType;
						tempBorders = new JSONArray();
						if(!ListUtil.isEmpty(cellBorderInfo))
						{
							tempBorders.addAll(cellBorderInfo);
						}
					}else {
						if(cellBorderType.equals(borderType) && (r-edr==1))
						{
							edr = r;
							if(!ListUtil.isEmpty(cellBorderInfo))
							{
								tempBorders.addAll(cellBorderInfo);
							}
						}else {
							if("border-all".equals(borderType))
							{
								JSONObject border = tempBorders.getJSONObject(0);
								JSONArray range = new JSONArray();
								JSONArray column = new JSONArray();
								column.add(Integer.parseInt(key));
								column.add(Integer.parseInt(key));
								JSONArray row = new JSONArray();
								row.add(str);
								row.add(edr);
								JSONObject rangeObj = new JSONObject();
								rangeObj.put("column", column);
								rangeObj.put("row", row);
								range.add(rangeObj);
								border.put("range", range);
								configBorderInfo.add(border);
							}else if("border-other".equals(borderType)) {
								configBorderInfo.addAll(tempBorders);
							}
							str = r;
							edr = r;
							borderType = cellBorderType;
							tempBorders = new JSONArray();
							if(!ListUtil.isEmpty(cellBorderInfo))
							{
								tempBorders.addAll(cellBorderInfo);
							}
						}
					}
					if(i == columnBorder.size()-1)
					{
						if("border-all".equals(borderType))
						{
							JSONObject border = tempBorders.getJSONObject(0);
							JSONArray range = new JSONArray();
							JSONArray column = new JSONArray();
							column.add(Integer.parseInt(key));
							column.add(Integer.parseInt(key));
							JSONArray row = new JSONArray();
							row.add(str);
							row.add(edr);
							JSONObject rangeObj = new JSONObject();
							rangeObj.put("column", column);
							rangeObj.put("row", row);
							range.add(rangeObj);
							border.put("range", range);
							configBorderInfo.add(border);
						}else if("border-other".equals(borderType)) {
							configBorderInfo.addAll(tempBorders);
						}
					}
				}
			}
		}
	}
	
	/**  
	 * @MethodName: csv2Luckysheet
	 * @Description: csv转luckysheet
	 * @author caiyang
	 * @param file
	 * @return
	 * @throws Exception JSONArray
	 * @date 2024-05-21 03:56:16 
	 */ 
	public static JSONArray csv2Luckysheet(MultipartFile file) throws Exception {
		JSONArray result = parseCsv(file.getInputStream(),file.getOriginalFilename());
		return result;
	}
	
	public static JSONArray csv2Luckysheet(InputStream inputStream,String fileName) throws Exception {
		JSONArray result = parseCsv(inputStream,fileName);
		return result;
	}
	
	private static JSONArray parseCsv(InputStream inputStream,String fileName) throws Exception{
		JSONArray result = new JSONArray();
		List<String[]> rows = new ArrayList<>();
		CSVReader reader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		String[] rowData;
        while ((rowData = reader.readNext()) != null) {
            rows.add(rowData);
        }
        reader.close();
        if(ListUtil.isNotEmpty(rows)) {
        	JSONObject sheetData = new JSONObject();
        	JSONObject config = new JSONObject();//配置信息
        	JSONArray cellDatas = new JSONArray();
        	String sheetIndex = "Sheet"+UUIDUtil.getUUID();
			String sheetName = fileName.replace(".csv", "");
			int maxColumn = -1;
        	for (int i = 0; i < rows.size(); i++) {
        		rowData = rows.get(i);
        		for (int j = 0; j < rowData.length; j++) {
	        		JSONObject cellInfo = new JSONObject();//单元格信息
		   			cellInfo.put("r", i);
		   			cellInfo.put("c", j);
		   			cellInfo.put("id", IdWorker.getId());
		           	JSONObject v = new JSONObject();
		           	JSONObject ct = new JSONObject();
		           	ct.put("t", "s");
		           	ct.put("fa", "@");
		           	v.put("ct", ct);
		           	v.put("v", rowData[j]);
	    			v.put("m", rowData[j]);
		           	cellInfo.put("v", v);
		           	cellDatas.add(cellInfo);
		           	if(j>maxColumn) {
		           		maxColumn = j;	
		           	}
				}
			}
        	sheetData.put("celldata", cellDatas); 
        	sheetData.put("config", config);
			sheetData.put("name", sheetName);
			sheetData.put("index", sheetIndex);
			sheetData.put("status", 0);
			sheetData.put("order", 0);
			sheetData.put("hide", 0);
			sheetData.put("isPivotTable", false);
			if ((rows.size()) > Constants.DEFAULT_SHEET_ROW) {
				sheetData.put("row", rows.size());
			} else {
				sheetData.put("row", Constants.DEFAULT_SHEET_ROW);
			}
			if (maxColumn > Constants.DEFAULT_SHEET_COLUMN) {
				sheetData.put("column", maxColumn+1);
			} else {
				sheetData.put("column", Constants.DEFAULT_SHEET_COLUMN);
			}
			result.add(sheetData);
        }
		return result;
	}
}
