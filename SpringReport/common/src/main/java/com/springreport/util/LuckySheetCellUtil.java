package com.springreport.util;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springreport.constants.Constants;
import com.springreport.enums.BorderTypeEnum;
import com.springreport.enums.CellFormatEnum;
import com.springreport.enums.FunctionExpressEnum;
import com.springreport.enums.LuckySheetPropsEnum;
import com.springreport.enums.TplTypeEnum;
import com.springreport.enums.YesNoEnum;

public class LuckySheetCellUtil {

	private XSSFWorkbook wb = null;

    private XSSFSheet sheet = null;

    /**
     * @param wb
     * @param sheet
     */
    public LuckySheetCellUtil(XSSFWorkbook wb, XSSFSheet sheet) {
        this.wb = wb;
        this.sheet = sheet;
    }
    
    private static Map<String, String> numberFormat = new HashMap<>();//数字格式
    
    static{
    	numberFormat.put("##0.00","1");
    	numberFormat.put("0","1");
    	numberFormat.put("0.00","1");
    	numberFormat.put("#,##0","1");
    	numberFormat.put("#,##0.00","1");
    	numberFormat.put("#,##0_);(#,##0)","1");
    	numberFormat.put("#,##0_);[Red](#,##0)","1");
    	numberFormat.put("#,##0.00_);(#,##0.00)","1");
    	numberFormat.put("#0.00%","1");
    	numberFormat.put("0%","1");
    	numberFormat.put("0.00%","1");
	}
    
    /**  
   	 * @Title: createCells
   	 * @Description: 创建单元格
   	 * @param sheet
   	 * @param maxX
   	 * @param maxY
   	 * @author caiyang
   	 * @date 2021-06-09 05:00:13 
   	 */ 
   	public void createCells(int maxX,int maxY,Map<String, Object> rowlen,JSONObject rowhidden)
   	{
   		if(rowlen == null)
   		{
   			rowlen = new HashMap<String, Object>();
   		}
   		for (int i = 0; i <= maxX; i++) {
   			sheet.createRow(i);
   		}
   	}
   	
   	public void setRowHeight(int maxX,Map<String, Object> rowlen,JSONObject rowhidden,Map<String, Object> wrapText) {
   		if(rowlen == null)
   		{
   			rowlen = new HashMap<String, Object>();
   		}
   		for (int i = 0; i <= maxX; i++) {
   			XSSFRow row = sheet.getRow(i);
   			if(rowhidden != null && !rowhidden.isEmpty())
   			{
   				if(rowhidden.get(i+"") != null && rowhidden.getIntValue(i+"") == 0)
   				{
   					row.setZeroHeight(true);
   				}
   			}
   			if(!wrapText.containsKey(i+"")) {
   				if(rowlen.get(String.valueOf(i)) != null)
   	   			{
   	   				try {
   	   					row.setHeightInPoints(Float.parseFloat(rowlen.get(String.valueOf(String.valueOf(i))) + "")*3/4);//行高px值
   					} catch (Exception e) {
   						row.setHeightInPoints(sheet.getDefaultRowHeightInPoints());//默认行高
   					}
   	   			}
   			}
   		}
   	}
   	
   	
   	/**  
	 * @Title: setCellValues
	 * @Description: 单元格赋值
	 * @param sheet
	 * @param cellValues
	 * @author caiyang
	 * @date 2021-06-09 05:01:36 
	 */ 
	public void setCellValues(List<Map<String, Object>> cellDatas,Map<String, Map<String, Object>> hyperlinks,List<Object> borderInfos,Map<String, String> unProtectCells,JSONObject merge,Integer isCoedit,
			JSONObject dataVerification,JSONArray xxbtCells,boolean isPdfStream,JSONArray barCodeCells,JSONArray qrCodeCells,Map<String, Object> wrapText,
			List<String> noViewAuthCells)
	{
		JSONObject cellBorders = this.getCellBorderInfo(borderInfos);
		if(cellDatas != null && cellDatas.size() > 0)
		{
			JSONObject cellBorder = null;
			JSONObject cellColSpan = new JSONObject();
			Map<String, XSSFCellStyle> cellStyleMap = new HashMap<String, XSSFCellStyle>();
			for (int i = 0; i < cellDatas.size(); i++) {
				Map<String, Object> cellData = cellDatas.get(i);
				Map<String, Object> cellConfig = (Map<String, Object>) cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode());
				Map<String, Object> cellValue = (Map<String, Object>) cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode());
				if(cellConfig == null) {
					cellConfig = new HashMap<String, Object>();
				}
				if(cellValue == null) {
					cellValue = new HashMap<String, Object>();
				}
				Cell cell = this.getCell(Integer.valueOf(String.valueOf(cellData.get(LuckySheetPropsEnum.R.getCode()))), Integer.valueOf(String.valueOf(cellData.get(LuckySheetPropsEnum.C.getCode()))));
				if(YesNoEnum.NO.getCode().intValue() == isCoedit.intValue())
				{
					Map<String, Object> mergeCell = (Map<String, Object>) cellValue.get(LuckySheetPropsEnum.MERGECELLS.getCode());
					if(mergeCell != null)
					{
						if(mergeCell.containsKey(LuckySheetPropsEnum.ROWSPAN.getCode()) && mergeCell.containsKey(LuckySheetPropsEnum.COLSPAN.getCode()))
						{
							int firstRow = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.R.getCode())));
							int lastRow = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.R.getCode()))) + Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.ROWSPAN.getCode()))) - 1;
							int firstCol = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.C.getCode())));
							int lastCol = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.C.getCode()))) + Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.COLSPAN.getCode()))) - 1;
							this.mergeCell(firstRow, lastRow, firstCol, lastCol);
							cellColSpan.put(firstRow+"_"+firstCol, lastCol-firstCol+1);
						}
					}
				}
				
				if(!isPdfStream && cellConfig != null && cellConfig.get("xxbt") != null && "1".equals(String.valueOf(cellConfig.get("xxbt")))
						&& cellConfig.get("v") != null && String.valueOf(cellConfig.get("v")).contains("|") && String.valueOf(cellConfig.get("v")).split("\\|").length<5)
				{
					xxbtCells.add(cellData);
				}else {
					if(cellConfig != null && cellConfig.get("xxbt") != null && "1".equals(String.valueOf(cellConfig.get("xxbt")))
							&& cellConfig.get("v") != null && String.valueOf(cellConfig.get("v")).contains("|") && String.valueOf(cellConfig.get("v")).split("\\|").length<5)
					{
						String v = String.valueOf(cellConfig.get("v"));
						String[] splits = v.split("\\|");
						StringBuilder sb = new StringBuilder(); 
						if(splits.length == 2)
						{
							sb.append(splits[1]).append("\n").append("\n").append(splits[0]);
						}else if(splits.length == 3) {
							sb.append(splits[2]).append("\n").append("\n").append(splits[1]).append("\n").append("\n").append(splits[0]);
						}else if(splits.length == 4) {
							sb.append(splits[3]).append("\n").append("\n").append(splits[2]).append("\n").append("\n").append(splits[1]).append("\n").append("\n").append(splits[0]);
						}
						cellConfig.put("v", sb.toString());
					}
					int r = cell.getAddress().getRow();
					int c = cell.getAddress().getColumn();
					String key = r+"_"+c;
					if(noViewAuthCells!=null && noViewAuthCells.contains(key)) {
						continue;
					}
					if(cellStyleMap.size()<64000)
					{//单元格格式如果创建超过64000个会报异常
						boolean isLock = true;
						if(unProtectCells != null && unProtectCells.containsKey(key))
						{
							isLock = false;
						}
						String borderType = "border-none";
						if(!StringUtil.isEmptyMap(cellBorders))
						{
							cellBorder = cellBorders.getJSONObject(key);
							if(cellBorder != null)
							{
								borderType = cellBorder.getString("borderType");
								cellBorders.remove(key);
							}
						}
						CellStyle cellStyle = this.getCellStyle(cellData,cellStyleMap,isLock,borderType,wrapText);
						cell.setCellStyle(cellStyle);
					}
					Map<String, Object> cellType = (Map<String, Object>) cellValue.get(LuckySheetPropsEnum.CELLTYPE.getCode());
					String t = "s";
					if(cellType != null) {
						t = String.valueOf(cellType.get(LuckySheetPropsEnum.TYPE.getCode()));
					}
 					String f = String.valueOf(cellValue.get(LuckySheetPropsEnum.FUNCTION.getCode()) == null ? "" : cellValue.get(LuckySheetPropsEnum.FUNCTION.getCode()));
					if(StringUtil.isNotEmpty(f))
					{
						if((f.toLowerCase()).contains((FunctionExpressEnum.NOW.getCode().toLowerCase())))
						{
							cell.setCellValue(String.valueOf(cellValue.get(LuckySheetPropsEnum.CELLVALUE.getCode())));
						}else {
							cell.setCellFormula(f.substring(1));
						}
					}else {
						if(LuckySheetPropsEnum.INLINESTR.getCode().equals(t))
						{
							List<Map<String, Object>> list = (List<Map<String, Object>>) cellType.get(LuckySheetPropsEnum.STRING.getCode());
							String value = "";
							JSONArray richStyles = new JSONArray();
							JSONObject richStyle = null;
							int start = 0;
							for (int j = 0; j < list.size(); j++) {
								if(list.get(j).get(LuckySheetPropsEnum.CELLVALUE.getCode()) != null)
								{
									richStyle = new JSONObject();
									Map<String, Object> richCellStyleMap = this.getRichCellStyleMap(list.get(j));
									value = value + String.valueOf(list.get(j).get(LuckySheetPropsEnum.CELLVALUE.getCode()));
									richStyle.put("start", start);
									richStyle.put("end", start+String.valueOf(list.get(j).get(LuckySheetPropsEnum.CELLVALUE.getCode())).length());
									richStyle.put("styleMap", richCellStyleMap);
									richStyles.add(richStyle);
									start = start + String.valueOf(list.get(j).get(LuckySheetPropsEnum.CELLVALUE.getCode())).length();
								}
							}
							XSSFRichTextString xssfRichTextString = new XSSFRichTextString(value);
							if(!ListUtil.isEmpty(richStyles))
							{
								for (int j = 0; j < richStyles.size(); j++) {
									int str = richStyles.getJSONObject(j).getIntValue("start");
									int end = richStyles.getJSONObject(j).getIntValue("end");
									Map<String, Object> styleMap = (Map<String, Object>) richStyles.getJSONObject(j).get("styleMap");
									XSSFFont font = this.getRichCellFont(styleMap);
									xssfRichTextString.applyFont(str, end, font);
								}
							}
							cell.setCellValue(xssfRichTextString);
						}else {
							if(cellValue.get(LuckySheetPropsEnum.CELLVALUE.getCode()) != null)
							{
//								if(CheckUtil.isNumber(String.valueOf(cellValue.get(LuckySheetPropsEnum.CELLVALUE.getCode()))) 
//									&& !CheckUtil.isIDCardNo(String.valueOf(cellValue.get(LuckySheetPropsEnum.CELLVALUE.getCode()))) && String.valueOf(cellValue.get(LuckySheetPropsEnum.CELLVALUE.getCode())).length() <= 12)
//								{
//									cell.setCellValue(Double.parseDouble(String.valueOf(cellValue.get(LuckySheetPropsEnum.CELLVALUE.getCode()))));
//								}else {
//									
//								}
								XSSFRichTextString richTextString = this.getCheckBoxValue(dataVerification, String.valueOf(cellValue.get(LuckySheetPropsEnum.CELLVALUE.getCode())), key);
								if(richTextString == null)
								{
									JSONObject ct = JSONObject.parseObject(JSON.toJSONString(cellConfig.get(LuckySheetPropsEnum.CELLTYPE.getCode())));
									String dataFormat = null;
									if(ct != null) {
										dataFormat = ct.getString(LuckySheetPropsEnum.CELLFORMAT.getCode());
									}
									if("General".equals(dataFormat)) {
										if(CheckUtil.isNumeric(String.valueOf(cellValue.get(LuckySheetPropsEnum.CELLVALUE.getCode())))) {
											cell.setCellValue(Double.parseDouble(String.valueOf(cellValue.get(LuckySheetPropsEnum.CELLVALUE.getCode()))));
										}else {
											cell.setCellValue(String.valueOf(cellValue.get(LuckySheetPropsEnum.CELLVALUE.getCode())));
										}
									}else if(numberFormat.containsKey(dataFormat)) {
										if(StringUtil.isNotEmpty(String.valueOf(cellValue.get(LuckySheetPropsEnum.CELLVALUE.getCode())))) {
											try {
												cell.setCellValue(Double.parseDouble(String.valueOf(cellValue.get(LuckySheetPropsEnum.CELLVALUE.getCode()))));
											} catch (Exception e) {
												cell.setCellValue(String.valueOf(cellValue.get(LuckySheetPropsEnum.CELLVALUE.getCode())));
											}
											
										}else {
											cell.setCellValue("");
										}
									}else {
										cell.setCellValue(String.valueOf(cellValue.get(LuckySheetPropsEnum.CELLVALUE.getCode())));
									}
								}else {
									cell.setCellValue(richTextString);
								}
							}else {
								cell.setCellValue("");
							}
						}
					}
					if(cell.getCellType() == CellType.STRING ) {
						String cellValueString = cell.getStringCellValue();
						if(wrapText.containsKey(r+"_"+cell.getColumnIndex())) {
							if(StringUtil.isNotEmpty(cellValueString)) {
								wrapText.put(r+"_"+cell.getColumnIndex(), cellValueString.length());
								wrapText.put("row_"+r,cell.getColumnIndex());
								String maxKey = "maxrow_" + r;
								if(wrapText.containsKey(maxKey)) {
									if(cellValueString.length()>String.valueOf(wrapText.get(maxKey)).length()) {
										wrapText.put(maxKey, cellValueString);
										if(cellColSpan.containsKey(r+"_"+cell.getColumnIndex())) {
											wrapText.put(maxKey+"_colspan", cellColSpan.get(r+"_"+cell.getColumnIndex()));
										}
									}
								}else {
									wrapText.put(maxKey, cellValueString);
									if(cellColSpan.containsKey(r+"_"+cell.getColumnIndex())) {
										wrapText.put(maxKey+"_colspan", cellColSpan.get(r+"_"+cell.getColumnIndex()));
									}
								}
							}
						}
//						else {
//							XSSFFont font = wb.getFontAt(cell.getCellStyle().getFontIndex());
//							if(StringUtil.isNotEmpty(cellValueString)) {
//								wrapText.put(r+"", cellValueString.length());
//								wrapText.put(r+"_value", cellValueString.length()*font.getFontHeightInPoints());
//								wrapText.put("row_"+r,cell.getColumnIndex());
//							}
//						}
					}
				}
				
				if(cellValue.get(LuckySheetPropsEnum.POSTIL.getCode()) != null)
				{
					JSONObject ps = JSONObject.parseObject(JSONObject.toJSONString(cellValue.get("ps")));
					ClientAnchor anchor = new XSSFClientAnchor();
		            // 关键修改
		            anchor.setDx1(0);
		            anchor.setDx2(0);
		            anchor.setDy1(0);
		            anchor.setDy2(0);
		            anchor.setCol1(cell.getColumnIndex());
		            anchor.setRow1(cell.getRowIndex());
		            anchor.setCol2(cell.getColumnIndex() + 5);
		            anchor.setRow2(cell.getRowIndex() + 6);
		            // 结束
		            Drawing drawing = sheet.createDrawingPatriarch();
		            Comment comment = drawing.createCellComment(anchor);
		            // 输入批注信息
		            comment.setString(new XSSFRichTextString(ps.getString("value")));
		            // 将批注添加到单元格对象中
		            cell.setCellComment(comment);
				}
				if(hyperlinks != null && hyperlinks.containsKey(cellData.get(LuckySheetPropsEnum.R.getCode())+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+cellData.get(LuckySheetPropsEnum.C.getCode())))
				{
					Map<String, Object> link = hyperlinks.get(cellData.get(LuckySheetPropsEnum.R.getCode())+LuckySheetPropsEnum.COORDINATECONNECTOR.getCode()+cellData.get(LuckySheetPropsEnum.C.getCode()));
					CreationHelper createHelper = wb.getCreationHelper();
					XSSFHyperlink  XSSFHyperlink = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.URL);
					XSSFHyperlink.setAddress(String.valueOf(link.get(LuckySheetPropsEnum.LINKADDRESS.getCode())));
					cell.setHyperlink(XSSFHyperlink);
				}
				String ff = String.valueOf(cellConfig.get("ff"));//字体
				if(barCodeCells != null && StringUtil.isNotEmpty(ff) && ff.contains("barCode128")) {
					barCodeCells.add(cellData);
				}else if(qrCodeCells != null && StringUtil.isNotEmpty(ff) && ff.contains("qrCode")) {
					qrCodeCells.add(cellData);
				}
				
			}
			
		}
		this.setCellBorder(cellBorders);
		if(YesNoEnum.YES.getCode().intValue() == isCoedit.intValue())
		{
			if(!StringUtil.isEmptyMap(merge))
			{
				Set<String> set = merge.keySet();
				for (String k : set) {
					 JSONObject obj = merge.getJSONObject(k);
					 int r = obj.getIntValue("r");
					 int c = obj.getIntValue("c");
					 int rs = obj.getIntValue("rs");
					 int cs = obj.getIntValue("cs");
					 this.mergeCell(r, r+rs-1, c, c+cs-1);
				}
			}
		}
	}
	
	private void setCellBorder(JSONObject jsonObject)
	{
		if(!StringUtil.isEmptyMap(jsonObject))
		{
			Map<String, XSSFCellStyle> borderMap = new HashMap<>();
			 for(Map.Entry entry : jsonObject.entrySet()){
				 String mapKey = (String) entry.getKey();
				 JSONObject mapValue = (JSONObject) entry.getValue();
				 int r = Integer.parseInt(mapKey.split("_")[0]);
				 int c = Integer.parseInt(mapKey.split("_")[1]);
				 Cell cell = this.getCell(r,c);
				 String borderType = mapValue.getString("borderType");
				 if(BorderTypeEnum.BORDERALL.getCode().equals(borderType))
					{
					 	XSSFCellStyle cellStyle = borderMap.get(BorderTypeEnum.BORDERALL.getCode());
					 	if(cellStyle == null)
					 	{
					 		cellStyle = (XSSFCellStyle) wb.createCellStyle();
					 	}
						cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
						cellStyle.setBorderLeft(BorderStyle.THIN); //左边框
						cellStyle.setBorderRight(BorderStyle.THIN); //右边框
						cellStyle.setBorderTop(BorderStyle.THIN); //上边框
						borderMap.put(BorderTypeEnum.BORDERALL.getCode(), cellStyle);
						cell.setCellStyle(cellStyle);
					}else {
						XSSFCellStyle cellStyle = (XSSFCellStyle) wb.createCellStyle();
						if(borderType.contains(BorderTypeEnum.BORDERBOTTOM.getCode()))
						{
							cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
						}
						if(borderType.contains(BorderTypeEnum.BORDERLEFT.getCode()))
						{
							cellStyle.setBorderLeft(BorderStyle.THIN); //下边框
						}
						if(borderType.contains(BorderTypeEnum.BORDERRIGHT.getCode()))
						{
							cellStyle.setBorderRight(BorderStyle.THIN); //下边框
						}
						if(borderType.contains(BorderTypeEnum.BORDERTOP.getCode()))
						{
							cellStyle.setBorderTop(BorderStyle.THIN); //下边框
						}
						cell.setCellStyle(cellStyle);
					}
			 }
		}
	}
	
	private void setCellBorder(List<Object> borderInfos)
	{
		if(!ListUtil.isEmpty(borderInfos))
		{
			for (int i = 0; i < borderInfos.size(); i++) {
				Map<String, Object> border = (Map<String, Object>) borderInfos.get(i);
				String rangeType = (String)border.get(LuckySheetPropsEnum.RANGETYPE.getCode());
				if(LuckySheetPropsEnum.BORDERRANGE.getCode().equals(rangeType))
				{
					String borderType = (String) border.get(LuckySheetPropsEnum.BORDERTYPE.getCode());
					String borderColor = (String) border.get(LuckySheetPropsEnum.BORDERCOLOR.getCode());
					if("#000".equals(borderColor))
					{
						borderColor = "#000000";
					}
					XSSFColor xssfColor = null;
					try {
						int[] rgb = StringUtil.hexToRgb(borderColor);
						xssfColor = new XSSFColor(new Color(rgb[0], rgb[1], rgb[2]),new DefaultIndexedColorMap());
					} catch (Exception e) {
						xssfColor = new XSSFColor(new Color(0, 0, 0),new DefaultIndexedColorMap());
					}
					List<Map<String, Object>> ranges = (List<Map<String, Object>>) border.get(LuckySheetPropsEnum.BORDERRANGE.getCode());
					for (int j = 0; j < ranges.size(); j++) {
						Map<String, Object> range = ranges.get(j);
						List<Integer> rows = (List<Integer>) range.get(LuckySheetPropsEnum.BORDERROWRANGE.getCode());
						List<Integer> cols = (List<Integer>) range.get(LuckySheetPropsEnum.BORDERCOLUMNRANGE.getCode());
						int rs = rows.get(1) - rows.get(0) + 1;
						int cs = cols.get(1) - cols.get(0) + 1;
						for (int k = 0; k < rs; k++) {
							for (int k2 = 0; k2 < cs; k2++) {
								Cell cell = this.getCell(rows.get(0)+k,cols.get(0)+k2);
								XSSFCellStyle cellStyle = (XSSFCellStyle) cell.getCellStyle();
								if(BorderTypeEnum.BORDERALL.getCode().equals(borderType))
								{//全边框
									cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
									cellStyle.setBottomBorderColor(xssfColor);
									cellStyle.setBorderLeft(BorderStyle.THIN); //左边框
									cellStyle.setLeftBorderColor(xssfColor);
									cellStyle.setBorderRight(BorderStyle.THIN); //右边框
									cellStyle.setRightBorderColor(xssfColor);
									cellStyle.setBorderTop(BorderStyle.THIN); //上边框
									cellStyle.setTopBorderColor(xssfColor);
								}else
								{
									if(BorderTypeEnum.BORDERBOTTOM.getCode().equals(borderType))
									{
										cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
										cellStyle.setBottomBorderColor(xssfColor);
									} 
									if(BorderTypeEnum.BORDERLEFT.getCode().equals(borderType))
									{
										cellStyle.setBorderLeft(BorderStyle.THIN); //左边框
										cellStyle.setLeftBorderColor(xssfColor);
									} 
									if(BorderTypeEnum.BORDERRIGHT.getCode().equals(borderType))
									{
										cellStyle.setBorderRight(BorderStyle.THIN); //右边框
										cellStyle.setRightBorderColor(xssfColor);
									} 
									if(BorderTypeEnum.BORDERTOP.getCode().equals(borderType))
									{
										cellStyle.setBorderTop(BorderStyle.THIN); //上边框
										cellStyle.setTopBorderColor(xssfColor);
									}
								}
							}
						}
					}
				}else {
					Map<String, Object> value = (Map<String, Object>) border.get(LuckySheetPropsEnum.RANGECELLVALUE.getCode());
					int r = (int) value.get("row_index");
					int c = (int) value.get("col_index");
					Cell cell = this.getCell(r,c);
					CellStyle cellStyle = cell.getCellStyle();
					Object rightBorder = value.get("r");
					if(rightBorder != null)
					{
						cellStyle.setBorderRight(BorderStyle.THIN); //右边框
					}
					Object leftBorder = value.get("l");
					if(leftBorder != null)
					{
						cellStyle.setBorderLeft(BorderStyle.THIN); //左边框
					}
					Object topBorder = value.get("t");
					if(topBorder != null)
					{
						cellStyle.setBorderTop(BorderStyle.THIN); //上边框
					}
					Object bottomBorder = value.get("b");
					if(bottomBorder != null)
					{
						cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
					}
				}
				
			}
		}
	}
	 
   	/**  
	 * @Title: getCell
	 * @Description: 根据坐标获取单元格
	 * @param rowIndex
	 * @param colIndex
	 * @return
	 * @author caiyang
	 * @date 2021-06-09 06:21:39 
	 */ 
	public  Cell getCell(int rowIndex, int colIndex) {
		XSSFRow row = sheet.getRow(rowIndex);
		if(row == null) {
			row = sheet.createRow(rowIndex);
		}
		Cell cell = row.getCell(colIndex);
		if(cell == null)
		{
			cell = row.createCell(colIndex);
		}
		return cell;
	}
	
	/**  
	 * @Title: mergeCell
	 * @Description: 合并单元格
	 * @param firstRow
	 * @param lastRow
	 * @param firstCol
	 * @param lastCol
	 * @author caiyang
	 * @date 2021-06-09 06:25:39 
	 */ 
	public void mergeCell(int firstRow, int lastRow, int firstCol, int lastCol) {
		if(firstRow != lastRow || firstCol != lastCol)
		{
			CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
			sheet.addMergedRegion(region);
		}
	}
	
	private CellStyle getCellStyle(Map<String, Object> cellData,Map<String, XSSFCellStyle> cellStyleMap,boolean isLock,String borderType,Map<String, Object> wrapText) {
		Map<String, Object> cellConfig = (Map<String, Object>) cellData.get(LuckySheetPropsEnum.CELLCONFIG.getCode());
		Map<String, Object> styleMap = getCellStyleMap(cellConfig,isLock);
		styleMap.put("borderType", borderType);
		String md5Key = Md5Util.generateMd5(JSONObject.toJSONString(styleMap));
		XSSFCellStyle cellStyle = cellStyleMap.get(md5Key);
		int r = (int) cellData.get("r");
		int c = (int) cellData.get("c");
		String tb = String.valueOf(styleMap.get("tb"));
		if("2".equals(tb)) {
			if(!wrapText.containsKey(r+"")) {
				wrapText.put(r+"_"+c, 0);
			}
			if(styleMap.containsKey("ls")) {
				wrapText.put(r+"_"+c+"_ls", (Integer) styleMap.get("ls"));
			}else {
				wrapText.put(r+"_"+c+"_ls", 0);
			}
		}
		if(cellStyle != null)
		{
			return cellStyle;
		}else {
			cellStyle = (XSSFCellStyle) wb.createCellStyle();
			String fa = String.valueOf(styleMap.get("dataFormat")==null?"":styleMap.get("dataFormat"));
			if("##0.00".equals(fa)) {
				fa = "#,##0.00";
			}else if("#0.00%".equals(fa)) {
				fa = "0.00%";
			}
			DataFormat format = wb.createDataFormat();
			cellStyle.setDataFormat(StringUtil.isNullOrEmpty(fa)?format.getFormat("General"):format.getFormat(fa));
			XSSFFont font = (XSSFFont) wb.createFont();
			//字体设置
			String fontName = String.valueOf(styleMap.get("fontName"));
			font.setFontName(fontName);
			//是否加粗
			boolean bold = (boolean) styleMap.get("bold");
			font.setBold(bold);
			//是否斜体
			boolean italic = (boolean) styleMap.get("italic");
			font.setItalic(italic);
			//删除线
			boolean strikeOut = (boolean) styleMap.get("strikeOut");
			font.setStrikeout(strikeOut);
			//下划线
			byte underLine = (byte) styleMap.get("underLine");
			font.setUnderline(underLine);
			//字体颜色
			int[] fontColor =  (int[]) styleMap.get("fontColor");
			if(fontColor != null)
			{
				font.setColor(new XSSFColor(new Color(fontColor[0], fontColor[1], fontColor[2]),new DefaultIndexedColorMap()));
			}
			//背景颜色
			if(styleMap.get("background") != null)
			{
				int[] backgroundXSSFColor = (int[]) styleMap.get("background");
				cellStyle.setFillForegroundColor(new XSSFColor(new Color(backgroundXSSFColor[0], backgroundXSSFColor[1], backgroundXSSFColor[2]),new DefaultIndexedColorMap()));
				cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			}
			//对齐方式
			String horizontal = String.valueOf(styleMap.get("horizontal"));
			switch (horizontal) {
			case "0":
				cellStyle.setAlignment(HorizontalAlignment.CENTER);
				break;
			case "1":
				cellStyle.setAlignment(HorizontalAlignment.LEFT);
				break;
			case "2":
				cellStyle.setAlignment(HorizontalAlignment.RIGHT);
				break;
			}
			String vertical = String.valueOf(styleMap.get("vertical"));
			switch (vertical) {
			case "0":
				cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				break;
			case "1":
				cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
				break;
			case "2":
				cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
				break;
			}
			if(styleMap.get("tr") != null)
			{
				short tr = (short) styleMap.get("tr");
				cellStyle.setRotation(tr);
			}
			//字体大小
			String fontSize = String.valueOf(styleMap.get("fontSize"));
			font.setFontHeightInPoints(Short.parseShort(fontSize));
			if("2".equals(tb)) {
				cellStyle.setWrapText(true);
			}else if("1".equals(tb)) {
				cellStyle.setWrapText(false);
			}else if("0".equals(tb)) {
				cellStyle.setAlignment(HorizontalAlignment.FILL);
			}
			
			cellStyle.setFont(font);
			cellStyle.setLocked(isLock);
			if(BorderTypeEnum.BORDERALL.getCode().equals(borderType))
			{
				cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
				cellStyle.setBorderLeft(BorderStyle.THIN); //左边框
				cellStyle.setBorderRight(BorderStyle.THIN); //右边框
				cellStyle.setBorderTop(BorderStyle.THIN); //上边框
			}else {
				if(borderType.contains(BorderTypeEnum.BORDERBOTTOM.getCode()))
				{
					cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
				}
				if(borderType.contains(BorderTypeEnum.BORDERLEFT.getCode()))
				{
					cellStyle.setBorderLeft(BorderStyle.THIN); //下边框
				}
				if(borderType.contains(BorderTypeEnum.BORDERRIGHT.getCode()))
				{
					cellStyle.setBorderRight(BorderStyle.THIN); //下边框
				}
				if(borderType.contains(BorderTypeEnum.BORDERTOP.getCode()))
				{
					cellStyle.setBorderTop(BorderStyle.THIN); //下边框
				}
			}
			cellStyleMap.put(md5Key, cellStyle);
			return cellStyle;
		}
	}
	
	/**  
	 * @Title: getCellStyleMap
	 * @Description: 单元格样式map
	 * @param cellConfig
	 * @return
	 * @author caiyang
	 * @date 2022-03-30 02:08:41 
	 */ 
	public static Map<String, Object> getCellStyleMap(Map<String, Object> cellConfig,boolean isLock)
	{
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		//字体
		if(cellConfig == null) {
			cellConfig = new HashMap<>();
		}
		if(cellConfig.containsKey(LuckySheetPropsEnum.FONTFAMILY.getCode()))
		{
			result.put("fontName", cellConfig.get(LuckySheetPropsEnum.FONTFAMILY.getCode()));
		}else {
			result.put("fontName", Constants.DEFAULT_FONT_FAMILY);
		}
		//是否加粗
		if(cellConfig.containsKey(LuckySheetPropsEnum.BOLD.getCode()))
		{
			String bold = String.valueOf(cellConfig.get((LuckySheetPropsEnum.BOLD.getCode())));
			if("1".equals(bold))
			{
				result.put("bold", true);
			}else {
				result.put("bold", false);
			}
		}else {
			result.put("bold", false);
		}
		//是否斜体
		if(cellConfig.containsKey(LuckySheetPropsEnum.ITALIC.getCode()))
		{
			String italic = String.valueOf(cellConfig.get((LuckySheetPropsEnum.ITALIC.getCode())));
			if("1".equals(italic))
			{
				result.put("italic", true);
			}else {
				result.put("italic", false);
			}
		}else {
			result.put("italic", false);
		}
		//删除线
		if(cellConfig.containsKey(LuckySheetPropsEnum.CANCELLINE.getCode()))
		{
			String cancleLine = String.valueOf(cellConfig.get((LuckySheetPropsEnum.CANCELLINE.getCode())));
			if("1".equals(cancleLine)) {
				result.put("strikeOut", true);
			}else {
				result.put("strikeOut", false);
			}
		}else {
			result.put("strikeOut", false);
		}
		//下划线
		if(cellConfig.containsKey(LuckySheetPropsEnum.UNDERLINE.getCode()))
		{
			String underLine = String.valueOf(cellConfig.get((LuckySheetPropsEnum.UNDERLINE.getCode())));
			if("1".equals(underLine))
			{
				result.put("underLine", Font.U_SINGLE);
			}else {
				result.put("underLine", Font.U_NONE);
			}
		}else {
			result.put("underLine", Font.U_NONE);
		}
		//字体颜色
		if(cellConfig.containsKey(LuckySheetPropsEnum.FONTCOLOR.getCode()))
		{
			String fontColor = String.valueOf(cellConfig.get(LuckySheetPropsEnum.FONTCOLOR.getCode()));
			if(fontColor.contains("rgb")) {
				int[] rgb = StringUtil.rgbStringToRgb(fontColor);
				result.put("fontColor", rgb);
			}else {
				if(!"null".equals(fontColor))
				{
					int[] rgb = StringUtil.hexToRgb(fontColor);
					result.put("fontColor", rgb);
				}
			}
		}else {
			int[] rgb = new int[3];
			rgb[0] = 0;
			rgb[1] = 0;
			rgb[2] = 0;
			result.put("fontColor", rgb);
		}
		//背景颜色
		if(cellConfig.containsKey(LuckySheetPropsEnum.BACKGROUND.getCode()))
		{
			Object object = cellConfig.get(LuckySheetPropsEnum.BACKGROUND.getCode());
			if(object != null) {
				String background = String.valueOf(object);
				try {
					int[] rgb = StringUtil.hexToRgb(background);
					result.put("background", rgb);
				} catch (Exception e) {
					result.put("background",null);
				}
			}else {
				result.put("background", null);
			}
		}else {
			result.put("background",null);
		}
		//对齐方式
		if(cellConfig.containsKey(LuckySheetPropsEnum.HORIZONTALTYPE.getCode()))
		{//水平对齐 0 居中、1 左、2右
			result.put("horizontal", cellConfig.get(LuckySheetPropsEnum.HORIZONTALTYPE.getCode()));
		}else {
			result.put("horizontal", "1");
		}
		if(cellConfig.containsKey(LuckySheetPropsEnum.VERTICALTYPE.getCode()))
		{//垂直对齐 0 中间、1 上、2下
			result.put("vertical", cellConfig.get(LuckySheetPropsEnum.VERTICALTYPE.getCode()));
		}else {
			result.put("vertical", "0");
		}
		//字体大小
		if(cellConfig.containsKey(LuckySheetPropsEnum.FONTSIZE.getCode()))
		{
			result.put("fontSize", cellConfig.get(LuckySheetPropsEnum.FONTSIZE.getCode()));
		}else {
			result.put("fontSize", Constants.DEFAULT_FONT_SIZE);
		}
		if(cellConfig.containsKey("tr"))
		{//字体倾斜
			String tr = String.valueOf(cellConfig.get("tr"));
			switch (tr) {
			case "1":
				result.put("tr", new Short("45"));
				break;
			case "2":
				result.put("tr", new Short("135"));
				break;
			case "3":
				result.put("tr", new Short("225"));
				break;
			case "4":
				result.put("tr", new Short("90"));
				break;
			case "5":
				result.put("tr", new Short("180"));
				break;
			default:
				result.put("tr", new Short("0"));
				break;
			}
		}
		if(cellConfig.containsKey(LuckySheetPropsEnum.TEXTWRAPMODE.getCode())) {
			String tb = String.valueOf(cellConfig.get(LuckySheetPropsEnum.TEXTWRAPMODE.getCode()));
			result.put("tb", tb);
		}
		if(cellConfig.containsKey(LuckySheetPropsEnum.LINESPACE.getCode())) {
			int ls = Integer.parseInt(String.valueOf(cellConfig.get(LuckySheetPropsEnum.LINESPACE.getCode())));
			result.put("ls", ls);
		}
		result.put("isLock", isLock);
		try {
			JSONObject ct = JSONObject.parseObject(JSON.toJSONString(cellConfig.get(LuckySheetPropsEnum.CELLTYPE.getCode())));
			String dataFormat = ct.getString(LuckySheetPropsEnum.CELLFORMAT.getCode());
			result.put("dataFormat", dataFormat);
		} catch (Exception e) {
		}
		
		return result;
		
	}
	
	/**  
	 * @Title: getCellStyleMap
	 * @Description: 单元格样式map
	 * @param cellConfig
	 * @return
	 * @author caiyang
	 * @date 2022-03-30 02:08:41 
	 */ 
	public static Map<String, Object> getRichCellStyleMap(Map<String, Object> cellConfig)
	{
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		//字体
		if(cellConfig.containsKey(LuckySheetPropsEnum.FONTFAMILY.getCode()))
		{
			result.put("fontName", cellConfig.get(LuckySheetPropsEnum.FONTFAMILY.getCode()));
		}else {
			result.put("fontName", Constants.DEFAULT_FONT_FAMILY);
		}
		//是否加粗
		if(cellConfig.containsKey(LuckySheetPropsEnum.BOLD.getCode()))
		{
			String bold = String.valueOf(cellConfig.get((LuckySheetPropsEnum.BOLD.getCode())));
			if("1".equals(bold))
			{
				result.put("bold", true);
			}else {
				result.put("bold", false);
			}
		}else {
			result.put("bold", false);
		}
		//是否斜体
		if(cellConfig.containsKey(LuckySheetPropsEnum.ITALIC.getCode()))
		{
			String italic = String.valueOf(cellConfig.get((LuckySheetPropsEnum.ITALIC.getCode())));
			if("1".equals(italic))
			{
				result.put("italic", true);
			}else {
				result.put("italic", false);
			}
		}else {
			result.put("italic", false);
		}
		//删除线
		if(cellConfig.containsKey(LuckySheetPropsEnum.CANCELLINE.getCode()))
		{
			String cancleLine = String.valueOf(cellConfig.get((LuckySheetPropsEnum.CANCELLINE.getCode())));
			if("1".equals(cancleLine)) {
				result.put("strikeOut", true);
			}else {
				result.put("strikeOut", false);
			}
		}else {
			result.put("strikeOut", false);
		}
		//下划线
		if(cellConfig.containsKey(LuckySheetPropsEnum.UNDERLINE.getCode()))
		{
			String underLine = String.valueOf(cellConfig.get((LuckySheetPropsEnum.UNDERLINE.getCode())));
			if("1".equals(underLine))
			{
				result.put("underLine", Font.U_SINGLE);
			}else {
				result.put("underLine", Font.U_NONE);
			}
		}else {
			result.put("underLine", Font.U_NONE);
		}
		//字体颜色
		if(cellConfig.containsKey(LuckySheetPropsEnum.FONTCOLOR.getCode()))
		{
			String fontColor = String.valueOf(cellConfig.get(LuckySheetPropsEnum.FONTCOLOR.getCode()));
			if(fontColor.contains("rgb")) {
				int[] rgb = StringUtil.rgbStringToRgb(fontColor);
				result.put("fontColor", rgb);
			}else {
				if(!"null".equals(fontColor))
				{
					int[] rgb = StringUtil.hexToRgb(fontColor);
					result.put("fontColor", rgb);
				}
			}
		}else {
			int[] rgb = new int[3];
			rgb[0] = 0;
			rgb[1] = 0;
			rgb[2] = 0;
			result.put("fontColor", rgb);
		}
		//字体大小
		if(cellConfig.containsKey(LuckySheetPropsEnum.FONTSIZE.getCode()))
		{
			result.put("fontSize", cellConfig.get(LuckySheetPropsEnum.FONTSIZE.getCode()));
		}else {
			result.put("fontSize", Constants.DEFAULT_FONT_SIZE);
		}
		//上标
		if(cellConfig.containsKey(LuckySheetPropsEnum.SUB.getCode()))
		{
			int sub = (int) cellConfig.get(LuckySheetPropsEnum.SUB.getCode());
			result.put("sub", sub);
		}else {
			result.put("sub", 0);
		}
		if(cellConfig.containsKey(LuckySheetPropsEnum.SUP.getCode()))
		{
			int sup = (int) cellConfig.get(LuckySheetPropsEnum.SUP.getCode());
			result.put("sup", sup);
		}else {
			result.put("sup", 0);
		}
		return result;
	}
	
	private XSSFFont getRichCellFont(Map<String, Object> styleMap) {
		XSSFFont font = (XSSFFont) wb.createFont();
		//字体设置
		String fontName = String.valueOf(styleMap.get("fontName"));
		font.setFontName(fontName);
		//是否加粗
		boolean bold = (boolean) styleMap.get("bold");
		font.setBold(bold);
		//是否斜体
		boolean italic = (boolean) styleMap.get("italic");
		font.setItalic(italic);
		//删除线
		boolean strikeOut = (boolean) styleMap.get("strikeOut");
		font.setStrikeout(strikeOut);
		//下划线
		byte underLine = (byte) styleMap.get("underLine");
		font.setUnderline(underLine);
		//字体颜色
		int[] fontColor =  (int[]) styleMap.get("fontColor");
		if(fontColor != null)
		{
			font.setColor(new XSSFColor(new Color(fontColor[0], fontColor[1], fontColor[2]),new DefaultIndexedColorMap()));
		}
		int sub = (int) styleMap.get("sub");
		if(sub == 1)
		{
			font.setTypeOffset(XSSFFont.SS_SUB);
		}
		int sup = (int) styleMap.get("sup");
		if(sup == 1)
		{
			font.setTypeOffset(XSSFFont.SS_SUPER);
		}
		return font;
	}
	
	 public void createPicRowAndCol(int x,int y,int maxX,int maxY) {
	    if(x>maxX)
	    {
	    	for (int i = 0; i <= (x-maxX); i++) {
	    		XSSFRow row = sheet.createRow(i+maxX+1);
	    		for (int j = 0; j <= y; j++) {
	   				row.createCell(j);
	   			}
			}
	    }
	    for (int i = 0; i <= maxX; i++) {
			if(y>maxY)
			{
				XSSFRow row = sheet.getRow(i);
				for (int j = 0; j < (y-maxY); j++) {
					row.createCell(j+maxY+1);
				}
			}
		}
	   
	 }
	 
	 private XSSFRichTextString getCheckBoxValue(JSONObject dataVerification,String cellValue,String key)
	 {
		 XSSFRichTextString richTextString = null;
		 if(!StringUtil.isEmptyMap(dataVerification))
		 {
			 if(dataVerification.containsKey(key))
			 {
				 JSONObject data = dataVerification.getJSONObject(key);
				 String type = data.getString("type");
				 if("checkbox".equals(type))
				 {
					 boolean checked = data.getBooleanValue("checked");
					 if(checked)
					 {
						 richTextString = new XSSFRichTextString("☑ "+ cellValue); 
					 }else {
						 richTextString = new XSSFRichTextString("□ "+ cellValue); 
					 }
				 }
			 }
		 }
		 return richTextString;
	 }
	 
	 private JSONObject getCellBorderInfo(List<Object> borderInfos) {
		 JSONObject result = new JSONObject();
		 if(!ListUtil.isEmpty(borderInfos))
		 {
			 for (int i = 0; i < borderInfos.size(); i++) {
				Map<String, Object> border = (Map<String, Object>) borderInfos.get(i);
				String rangeType = (String)border.get(LuckySheetPropsEnum.RANGETYPE.getCode());
				if(LuckySheetPropsEnum.BORDERRANGE.getCode().equals(rangeType))
				{
					String borderType = (String) border.get(LuckySheetPropsEnum.BORDERTYPE.getCode());	
					List<Map<String, Object>> ranges = (List<Map<String, Object>>) border.get(LuckySheetPropsEnum.BORDERRANGE.getCode());
					JSONObject cell = null;
					for (int j = 0; j < ranges.size(); j++) {
						Map<String, Object> range = ranges.get(j);
						List<Integer> rows = (List<Integer>) range.get(LuckySheetPropsEnum.BORDERROWRANGE.getCode());
						List<Integer> cols = (List<Integer>) range.get(LuckySheetPropsEnum.BORDERCOLUMNRANGE.getCode());
						int rs = rows.get(1) - rows.get(0) + 1;
						int cs = cols.get(1) - cols.get(0) + 1;
						for (int k = 0; k < rs; k++) {
							for (int k2 = 0; k2 < cs; k2++) {
								String key = (rows.get(0)+k)+"_"+(cols.get(0)+k2);
								if(result.containsKey(key))
								{
									if("border-none".equals(borderType))
									{
										result.remove(key);
									}else {
										cell = result.getJSONObject(key);
										String cellBorderType = cell.getString("borderType");
										if("border-all".equals(borderType))
										{
											cell.put("borderType", "border-all");
										}else {
											if(!cellBorderType.contains("border-all") && !cellBorderType.contains(borderType))
											{
												if(StringUtil.isNotEmpty(cellBorderType))
												{
													cellBorderType = cellBorderType + "," + borderType;
												}else {
													cellBorderType = borderType;
												}
												cell.put("borderType", cellBorderType);
											}
										}
									}
								}else {
									if(!"border-none".equals(borderType)) {
										cell = new JSONObject();
										cell.put("borderType", borderType);
										result.put(key, cell);
									}
								}
							}
						}
					}
				}else if("cell".equals(rangeType)) {
					Map<String, Object> value = (Map<String, Object>) border.get("value");
					Object r = value.get("row_index");
					Object c = value.get("col_index");
					String key = r+"_"+c;
					if(value.get("l") != null && value.get("r") != null && value.get("t") != null && value.get("b") != null)
					{
						JSONObject cell = new JSONObject();
						cell.put("borderType", "border-all");
						result.put(key, cell);
					}else {
						String cellBorderType = "";
						if(value.get("l") != null)
						{
							if(StringUtil.isNotEmpty(cellBorderType))
							{
								cellBorderType = cellBorderType + "," + "border-left";
							}else {
								cellBorderType = "border-left";
							}
						}
						if(value.get("r") != null)
						{
							if(StringUtil.isNotEmpty(cellBorderType))
							{
								cellBorderType = cellBorderType + "," + "border-right";
							}else {
								cellBorderType = "border-right";
							}
						}
						if(value.get("t") != null)
						{
							if(StringUtil.isNotEmpty(cellBorderType))
							{
								cellBorderType = cellBorderType + "," + "border-top";
							}else {
								cellBorderType = "border-top";
							}
						}
						if(value.get("b") != null)
						{
							if(StringUtil.isNotEmpty(cellBorderType))
							{
								cellBorderType = cellBorderType + "," + "border-bottom";
							}else {
								cellBorderType = "border-bottom";
							}
						}
						if(StringUtil.isNotEmpty(cellBorderType))
						{
							JSONObject cell = new JSONObject();
							cell.put("borderType", cellBorderType);
							result.put(key, cell);
						}else {
							JSONObject cell = new JSONObject();
							cell.put("borderType", "border-none");
							result.put(key, cell);
						}
					}
				}
			 }
		 }
		 return result;
	 }
}
