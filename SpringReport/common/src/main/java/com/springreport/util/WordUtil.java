package com.springreport.util;

import java.math.BigInteger;
import java.util.regex.Pattern;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TableRowAlign;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFAbstractNum;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFNumbering;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPBdr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLayoutType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblLayoutType;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.deepoove.poi.data.Texts;
import com.deepoove.poi.data.style.Style;
import com.deepoove.poi.util.TableTools;
import com.springreport.enums.TitleLevelEnum;

public class WordUtil {

	/**  
	 * @MethodName: addCustomHeadingStyle
	 * @Description: 添加自定义标题
	 * @author caiyang
	 * @param doc
	 * @param strStyleId
	 * @param headingLevel void
	 * @date 2024-05-03 08:15:40 
	 */ 
	public static void addCustomHeadingStyle(XWPFDocument doc,String strStyleId, int headingLevel) {
        CTStyle ctStyle = CTStyle.Factory.newInstance();
        ctStyle.setStyleId(strStyleId);

        CTString styleName = CTString.Factory.newInstance();
        styleName.setVal(strStyleId);
        ctStyle.setName(styleName);

        CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
        indentNumber.setVal(BigInteger.valueOf(headingLevel));

        // lower number > style is more prominent in the formats bar
        ctStyle.setUiPriority(indentNumber);

        CTOnOff onoffnull = CTOnOff.Factory.newInstance();
        ctStyle.setUnhideWhenUsed(onoffnull);

        // style shows up in the formats bar
//        ctStyle.setQFormat(onoffnull);

        // style defines a heading of the given level
        CTPPrGeneral ctpPrGeneral = CTPPrGeneral.Factory.newInstance();
        ctpPrGeneral.setOutlineLvl(indentNumber);
        ctStyle.setPPr(ctpPrGeneral);

        XWPFStyle style = new XWPFStyle(ctStyle);

        // is a null op if already defined
        XWPFStyles styles = doc.createStyles();

        style.setType(STStyleType.PARAGRAPH);
        styles.addStyle(style);
	}
	
    /**  
     * @MethodName: setPaperSize
     * @Description: 设置纸张大小
     * @author caiyang
     * @param document
     * @param height
     * @param width void
     * @date 2024-05-04 08:00:40 
     */ 
    public static void setPaperSize(XWPFDocument document , int height, int width,String pagerDirection){
        CTBody body = document.getDocument().getBody();
        CTSectPr sectPr = body.isSetSectPr() ? body.getSectPr() : body.addNewSectPr();
        CTPageSz pageSize = sectPr.addNewPgSz();
        int w = (int) (width * 20 / 1.33445);
        int h = (int) (height * 20 / 1.33445);
        if("horizontal".equals(pagerDirection)){  
        	//横版
        	pageSize.setW(BigInteger.valueOf(h));
            pageSize.setH(BigInteger.valueOf(w));
            pageSize.setOrient(STPageOrientation.LANDSCAPE);
        }else{
        	pageSize.setW(BigInteger.valueOf(w));
            pageSize.setH(BigInteger.valueOf(h));
            pageSize.setOrient(STPageOrientation.PORTRAIT);
        }
        
    }
    
    /**  
     * @MethodName: addTitleParagraph
     * @Description: 添加标题
     * @author caiyang
     * @param document
     * @param content void
     * @date 2024-05-04 08:47:51 
     */ 
    public static void addTitleParagraph(XWPFParagraph paragraph,JSONObject content) {
    	String level = content.getString("level");
    	String headStyle = "";
    	if(TitleLevelEnum.FIRST.getCode().equals(level)) {
    		headStyle = TitleLevelEnum.FIRST.getName();
    	}else if(TitleLevelEnum.SECOND.getCode().equals(level)) {
    		headStyle = TitleLevelEnum.SECOND.getName();
    	}else if(TitleLevelEnum.THIRD.getCode().equals(level)) {
    		headStyle = TitleLevelEnum.THIRD.getName();
    	}else if(TitleLevelEnum.FOURTH.getCode().equals(level)) {
    		headStyle = TitleLevelEnum.FOURTH.getName();
    	}else if(TitleLevelEnum.FIFTH.getCode().equals(level)) {
    		headStyle = TitleLevelEnum.FIFTH.getName();
    	}else if(TitleLevelEnum.SIXTH.getCode().equals(level)) {
    		headStyle = TitleLevelEnum.SIXTH.getName();
    	}
    	JSONArray valueList = content.getJSONArray("valueList");
    	if(ListUtil.isNotEmpty(valueList)) {
    		for (int i = 0; i < valueList.size(); i++) {
    			addParagraph(paragraph,valueList.getJSONObject(i),headStyle);
			}
    	}
    	
    }
    
    /**  
     * @MethodName: addParagraph
     * @Description: 添加段落
     * @author caiyang
     * @param document
     * @param content void
     * @date 2024-05-04 08:48:31 
     */ 
    public static void addParagraph(XWPFParagraph paragraph,JSONObject content,String titleStyle) {
    	XWPFRun run = paragraph.createRun();
    	setRunText(run,content,"text");
    	String rowFlex = content.getString("rowFlex");//对齐方式
    	Float rowMargin = content.getFloat("rowMargin");//行间距
    	if(StringUtil.isNotEmpty(rowFlex)) {
    		switch (rowFlex) {
			case "left":
				paragraph.setAlignment(ParagraphAlignment.LEFT);
				break;
			case "center":
				paragraph.setAlignment(ParagraphAlignment.CENTER);
				break;
			case "right":
				paragraph.setAlignment(ParagraphAlignment.RIGHT);
				break;
			case "alignment":
				paragraph.setAlignment(ParagraphAlignment.BOTH);
				break;
			default:
				paragraph.setAlignment(ParagraphAlignment.LEFT);
				break;
			}
    	}else {
    		paragraph.setAlignment(ParagraphAlignment.LEFT);
    	}
    	if(rowMargin != null) {
    		paragraph.setSpacingBetween(rowMargin,LineSpacingRule.AUTO);
    	}
    	if(StringUtil.isNotEmpty(titleStyle)) {
    		paragraph.setStyle(titleStyle);
    	}
    	
    }
    
    private static void setRunText(XWPFRun run,JSONObject content,String type) {
    	String value = content.getString("value");//内容
    	String font = content.getString("font");//字体
     	Double fontSize = content.getDouble("size");//字体大小
      	Boolean bold = content.getBoolean("bold");//是否加粗
    	String color = content.getString("color");//字体颜色
    	Boolean italic = content.getBoolean("italic");//是否斜体
    	String highlight = content.getString("highlight");//高亮
    	Boolean underline = content.getBoolean("underline");//是否下划线
    	Boolean strikeout = content.getBoolean("strikeout");//是否删除线
    	if(value.contains("\n")) {
    		if("\n".equals(value)) {
    			run.setText("");
    		}else {
    			String[] textes = value.split("\n");
    			for (int i = 0; i < textes.length; i++) {
    				run.setText(textes[i]);
    				if(i != textes.length - 1) {
    					run.addBreak();	
    				}
    			}
    			if(value.endsWith("\n")) {
        			run.addBreak();	
        		}
    		}
    		
    	}else {
    		run.setText(value);
    	}
    	switch (type) {
			case "sup":
				run.setSubscript(VerticalAlign.SUPERSCRIPT);
				break;
			case "sub":
				run.setSubscript(VerticalAlign.SUBSCRIPT);
			default:
				break;
		}
    	
    	if(StringUtil.isNotEmpty(font)) {
    		run.setFontFamily(font);
    	}else {
    		run.setFontFamily("微软雅黑");
    	}
    	if(fontSize != null) {
    		run.setFontSize(fontSize/1.33445);
    	}else {
    		run.setFontSize(16/1.33445);
    	}
    	if(bold != null) {
    		run.setBold(bold);
    	}
    	if(StringUtil.isNotEmpty(color)) {
    		if(color.startsWith("#")) {
    			run.setColor(color.replaceAll("#", ""));
    		}else if(color.startsWith("rgb")) {
    			try {
					int[] rgbs = StringUtil.rgbStringToRgb(color);
					color = StringUtil.rgb2Hex(rgbs[0], rgbs[1], rgbs[2]);
					run.setColor(color);
				} catch (Exception e) {
					
				}
    		}
    		
    	}
    	if(italic != null) {
    		run.setItalic(italic);
    	}
    	if(StringUtil.isNotEmpty(highlight)) {
    		if(highlight.startsWith("#")) {
    			run.setTextHighlightColor(highlight.replaceAll("#", ""));
    		}else if(highlight.startsWith("rgb")) {
    			try {
					int[] rgbs = StringUtil.rgbStringToRgb(highlight);
					highlight = StringUtil.rgb2Hex(rgbs[0], rgbs[1], rgbs[2]);
					run.setTextHighlightColor(highlight);
				} catch (Exception e) {
					
				}
    		}
    	}
    	if(underline != null && underline) {
    		JSONObject textDecoration = content.getJSONObject("textDecoration");
    		if(textDecoration == null) {
    			run.setUnderline(UnderlinePatterns.SINGLE);
    		}else {
    			String style = textDecoration.getString("style");
    			switch (style) {
				case "solid":
					run.setUnderline(UnderlinePatterns.SINGLE);
					break;
				case "double":
					run.setUnderline(UnderlinePatterns.DOUBLE);
					break;
				case "dashed":
					run.setUnderline(UnderlinePatterns.DASH);
					break;
				case "dotted":
					run.setUnderline(UnderlinePatterns.DOT_DASH);
					break;
				case "wavy":
					run.setUnderline(UnderlinePatterns.WAVE);
					break;
				default:
					run.setUnderline(UnderlinePatterns.NONE);
					break;
				}
    		}
    	}else {
    		run.setUnderline(UnderlinePatterns.NONE);
    	}
    	if(strikeout != null) {
    		run.setStrikeThrough(strikeout);
    	}
    }
    
    /**  
     * @MethodName: addTab
     * @Description: 添加tab
     * @author caiyang
     * @param paragraph void
     * @date 2024-05-05 07:49:55 
     */ 
    public static void addTab(XWPFParagraph paragraph,XWPFRun run ) {
    	if(run == null) {
    		run = paragraph.createRun();	
    	}
//    	run.addTab();
    	run.setText("       ");
		CTText ctText = run.getCTR().getTArray(0);
		ctText.setSpace(SpaceAttribute.Space.PRESERVE);
    }
    
    /**  
     * @MethodName: addTable
     * @Description: 添加表格
     * @author caiyang
     * @param doc void
     * @date 2024-05-05 07:50:03 
     */ 
    public static void addTable(XWPFDocument doc,JSONObject tableData) {
    	int rows = 1;//行数
    	int cols = 1;//列数
    	JSONArray trList = tableData.getJSONArray("trList");
    	rows = trList.size();
    	JSONArray colgroup = tableData.getJSONArray("colgroup");
    	cols = colgroup.size();
    	String rowFlex = tableData.getString("rowFlex");
    	XWPFTable xwpfTable = doc.createTable(rows,cols);
    	if(StringUtil.isNotEmpty(rowFlex)) {
    		switch (rowFlex) {
			case "left":
				xwpfTable.setTableAlignment(TableRowAlign.LEFT);
				break;
			case "center":
				xwpfTable.setTableAlignment(TableRowAlign.CENTER);
				break;
			case "right":
				xwpfTable.setTableAlignment(TableRowAlign.RIGHT);
				break;
			default:
				xwpfTable.setTableAlignment(TableRowAlign.LEFT);
				break;
			}
    	}
    	CTTbl ttbl = xwpfTable.getCTTbl(); 
	    CTTblGrid tblGrid = ttbl.addNewTblGrid();
    	for (int i = 0; i < cols; i++) {
    		CTTblGridCol gridCol = tblGrid.addNewGridCol();
    		gridCol.setW(new BigInteger(String.valueOf(colgroup.getJSONObject(i).getBigDecimal("width").intValue()*16)));  
    		for (int j = 0; j < rows; j++) {
    			xwpfTable.getRow(j).getCell(i).setWidth(String.valueOf(colgroup.getJSONObject(i).getBigDecimal("width").intValue()*16));
			}
		}
    	for (int i = 0; i < rows; i++) {
    		xwpfTable.getRow(i).setHeight(42*15);
//    		if(i == 0) {
//    			xwpfTable.getRow(i).setHeight(42*15);
//    		}else {
//    			int height = trList.getJSONObject(i).getIntValue("height");
//        		float multiple = height/42;
//        		if(multiple > 5) {
//        			xwpfTable.getRow(i).setHeight(height*15);
//        		}else {
//        			xwpfTable.getRow(i).setHeight(42*15);
//        		}	
//    		}
		}
    	setTableWidthFixed(xwpfTable,true);
    	JSONObject mergeObj = new JSONObject();
    	mergeObj.put("isMerge", true);
    	for (int i = 0; i < rows; i++) {
    		JSONArray tdList = trList.getJSONObject(i).getJSONArray("tdList");
    		for (int j = 0; j < tdList.size(); j++) {
				JSONObject cellInfo = tdList.getJSONObject(j);
				boolean isMerge = cellInfo.getBooleanValue("isMerge");
				if(isMerge) {
					continue;
				}else {
					int rowspan = cellInfo.getIntValue("rowspan");
					int colspan = cellInfo.getIntValue("colspan");
					if(rowspan > 1 || colspan > 1) {
						for (int k = 0; k < rowspan; k++) {
							for (int k2 = 0; k2 < colspan; k2++) {
								int r = i+k;
								int c = j+k2;
								if(r == i) {
									if(k2!=0) {
										if(c >= tdList.size()) {
											tdList.add(mergeObj);
										}else {
											tdList.add(c, mergeObj);
										}
									}
								}else {
									if(c >= trList.getJSONObject(r).getJSONArray("tdList").size()) {
										trList.getJSONObject(r).getJSONArray("tdList").add(mergeObj);
									}else {
										trList.getJSONObject(r).getJSONArray("tdList").add(c, mergeObj);
									}
								}
							}
						}
					}
				}
			}
		}
    	for (int i = 0; i < trList.size(); i++) {
    		JSONArray tdList = trList.getJSONObject(i).getJSONArray("tdList");
    		for (int j = 0; j < tdList.size(); j++) {
    			JSONObject cellInfo = tdList.getJSONObject(j);
    			boolean isMerge = cellInfo.getBooleanValue("isMerge");
				if(isMerge) {
					continue;
				}else {
					setCellValue(xwpfTable.getRow(i).getCell(j),cellInfo);
				}
    		}
    	}
    	for (int i = 0; i < trList.size(); i++) {
			JSONArray tdList = trList.getJSONObject(i).getJSONArray("tdList");
			for (int j = 0; j < tdList.size(); j++) {
				JSONObject cellInfo = tdList.getJSONObject(j);
				boolean isMerge = cellInfo.getBooleanValue("isMerge");
				if(isMerge) {
					continue;
				}
				int rowspan = cellInfo.getIntValue("rowspan");
				int colspan = cellInfo.getIntValue("colspan");
				if(rowspan > 1) {
					for (int k = 0; k < colspan; k++) {
						TableTools.mergeCellsVertically(xwpfTable, j+k, i, i+rowspan-1);
					}
				}
				
			}
		}
//    	for (int i = 0; i < trList.size(); i++) {
//			JSONArray tdList = trList.getJSONObject(i).getJSONArray("tdList");
//			for (int j = 0; j < tdList.size(); j++) {
//				JSONObject cellInfo = tdList.getJSONObject(j);
//				boolean isMerge = cellInfo.getBooleanValue("isMerge");
//				if(isMerge) {
//					continue;
//				}
//				int rowspan = cellInfo.getIntValue("rowspan");
//				int colspan = cellInfo.getIntValue("colspan");
//				if(colspan > 1) {
//					for (int k = 0; k < rowspan; k++) {
//						TableTools.mergeCellsHorizonal(xwpfTable, i+k, j, j+colspan-1);
//					}
//				}
//			}
//		}
    	for (int i = trList.size()-1; i >= 0; i--) {
    		JSONArray tdList = trList.getJSONObject(i).getJSONArray("tdList");
    		for (int j = tdList.size()-1; j >= 0; j--) {
    			JSONObject cellInfo = tdList.getJSONObject(j);
    			boolean isMerge = cellInfo.getBooleanValue("isMerge");
				if(isMerge) {
					continue;
				}
				int rowspan = cellInfo.getIntValue("rowspan");
				int colspan = cellInfo.getIntValue("colspan");
				if(colspan > 1) {
					for (int k = 0; k < rowspan; k++) {
						TableTools.mergeCellsHorizonal(xwpfTable, i+k, j, j+colspan-1);
					}
				}
    		}
    	}
    }
    
	/**
     * 是否允许表格自动重调单元格宽度 对应【表格属性-表格-选项-自动重调尺寸以适应内容】
     * */
    public static void setTableWidthFixed(XWPFTable table,boolean isFixed){
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CTTblLayoutType tblLayout = tblPr.isSetTblLayout() ? tblPr.getTblLayout() : tblPr.addNewTblLayout();
        if(isFixed) {
        	tblLayout.setType(STTblLayoutType.FIXED);
        }
    }
    
    private static void setCellValue(XWPFTableCell cell,JSONObject cellInfo) {
    	if(cell == null) {
    		return;
    	}
    	JSONArray values = cellInfo.getJSONArray("value");
    	String verticalAlign = cellInfo.getString("verticalAlign");
    	if(ListUtil.isNotEmpty(values)) {
    		CTP ctp = CTP.Factory.newInstance();
            XWPFParagraph paragraph = new XWPFParagraph(ctp, cell);
            String alignment = "";
            Float pRowMargin = 1f;
            for (int i = 0; i < values.size(); i++) {
            	JSONObject content = values.getJSONObject(i);
            	String value = content.getString("value");//内容
            	String type = content.getString("type");
            	String font = content.getString("font");//字体
            	Double fontSize = content.getDouble("size");//字体大小
            	Boolean bold = content.getBoolean("bold");//是否加粗
            	String color = content.getString("color");//字体颜色
            	Boolean italic = content.getBoolean("italic");//是否斜体
            	String highlight = content.getString("highlight");//高亮
            	Boolean underline = content.getBoolean("underline");//是否下划线
            	Boolean strikeout = content.getBoolean("strikeout");//是否删除线
            	String rowFlex = content.getString("rowFlex");//对齐方式
            	Float rowMargin = content.getFloat("rowMargin");//行间距
            	XWPFRun run = paragraph.createRun();
            	if("tab".equals(type)) {
            		addTab(paragraph,run);
            	}else {
            		if(value.contains("\n")) {
                		if("\n".equals(value)) {
                			run.addBreak();
                		}else {
                			String[] textes = value.split("\n");
                			for (int t = 0; t < textes.length; t++) {
                				run.setText(textes[t]);
                				run.addBreak();	
                			}
                		}
                	}else {
                		run.setText(value);
                	}
            	}
            	if(StringUtil.isNotEmpty(font)) {
            		run.setFontFamily(font);
            	}else {
            		run.setFontFamily("微软雅黑");
            	}
            	if(fontSize != null) {
            		run.setFontSize(fontSize/1.33445);
            	}else {
            		run.setFontSize(16/1.33445);
            	}
            	if(bold != null) {
            		run.setBold(bold);
            	}
            	if(StringUtil.isNotEmpty(color)) {
            		if(color.startsWith("#")) {
            			run.setColor(color.replaceAll("#", ""));
            		}else if(color.startsWith("rgb")) {
            			try {
        					int[] rgbs = StringUtil.rgbStringToRgb(color);
        					color = StringUtil.rgb2Hex(rgbs[0], rgbs[1], rgbs[2]);
        					run.setColor(color);
        				} catch (Exception e) {
        					
        				}
            		}
            		
            	}
            	if(italic != null) {
            		run.setItalic(italic);
            	}
            	if(StringUtil.isNotEmpty(highlight)) {
            		if(highlight.startsWith("#")) {
            			run.setTextHighlightColor(highlight.replaceAll("#", ""));
            		}else if(highlight.startsWith("rgb")) {
            			try {
        					int[] rgbs = StringUtil.rgbStringToRgb(highlight);
        					highlight = StringUtil.rgb2Hex(rgbs[0], rgbs[1], rgbs[2]);
        					run.setTextHighlightColor(highlight);
        				} catch (Exception e) {
        					
        				}
            		}
            	}
            	if(underline != null) {
            		JSONObject textDecoration = content.getJSONObject("textDecoration");
            		if(textDecoration == null) {
            			run.setUnderline(UnderlinePatterns.SINGLE);
            		}else {
            			String style = textDecoration.getString("style");
            			switch (style) {
        				case "solid":
        					run.setUnderline(UnderlinePatterns.SINGLE);
        					break;
        				case "double":
        					run.setUnderline(UnderlinePatterns.DOUBLE);
        					break;
        				case "dashed":
        					run.setUnderline(UnderlinePatterns.DASH);
        					break;
        				case "dotted":
        					run.setUnderline(UnderlinePatterns.DOT_DASH);
        					break;
        				case "wavy":
        					run.setUnderline(UnderlinePatterns.WAVE);
        					break;
        				default:
        					run.setUnderline(UnderlinePatterns.NONE);
        					break;
        				}
            		}
            	}else {
            		run.setUnderline(UnderlinePatterns.NONE);
            	}
            	if(strikeout != null) {
            		run.setStrikeThrough(strikeout);
            	}
            	if(StringUtil.isNotEmpty(rowFlex)) {
            		alignment = rowFlex;
            	}
            	if(rowMargin != null) {
            		pRowMargin = rowMargin;
            	}
			}
            if(StringUtil.isNotEmpty(alignment)) {
        		switch (alignment) {
    			case "left":
    				paragraph.setAlignment(ParagraphAlignment.LEFT);
    				break;
    			case "center":
    				paragraph.setAlignment(ParagraphAlignment.CENTER);
    				break;
    			case "right":
    				paragraph.setAlignment(ParagraphAlignment.RIGHT);
    				break;
    			case "alignment":
    				paragraph.setAlignment(ParagraphAlignment.BOTH);
    				break;
    			default:
    				paragraph.setAlignment(ParagraphAlignment.LEFT);
    				break;
    			}
        	}
            if(pRowMargin != null) {
            	paragraph.setSpacingBetween(pRowMargin,LineSpacingRule.AUTO);
        	}
            cell.setParagraph(paragraph);
            if(StringUtil.isNotEmpty(verticalAlign)) {
            	switch (verticalAlign) {
				case "top":
					cell.setVerticalAlignment(XWPFVertAlign.TOP);
					break;
				case "middle":
					cell.setVerticalAlignment(XWPFVertAlign.CENTER);
					break;
				case "bottom":
					cell.setVerticalAlignment(XWPFVertAlign.BOTTOM);
					break;
				default:
					break;
				}
            }
            
    	}
    }
    
    /**  
     * @MethodName: addSubSupScript
     * @Description: 添加上下标
     * @author caiyang
     * @param paragraph
     * @param content
     * @param type sup：上标 sub：下标
     * @date 2024-05-06 08:42:04 
     */ 
    public static void addSubSupScript(XWPFParagraph paragraph,JSONObject content,String type) {
    	XWPFRun run = paragraph.createRun();
    	setRunText(run,content,type);
    }
    
    /**  
     * @MethodName: addSeparator
     * @Description: 添加分割线
     * @author caiyang
     * @param paragraph
     * @param content void
     * @date 2024-05-06 01:37:00 
     */ 
    public static void addSeparator(XWPFParagraph paragraph,JSONObject content) {
    	JSONArray dashArray = content.getJSONArray("dashArray");
        CTP ctp = paragraph.getCTP();    
        CTPPr pr = ctp.isSetPPr() ? ctp.getPPr() : ctp.addNewPPr();    
        CTPBdr border = pr.isSetPBdr() ? pr.getPBdr() : pr.addNewPBdr();    
        CTBorder ct =  border.isSetBottom() ? border.getBottom() : border.addNewBottom();    
        if(ListUtil.isEmpty(dashArray)) {
        	ct.setVal(STBorder.Enum.forInt(3));
        }else {
        	int value = dashArray.getIntValue(0);
        	switch (value) {
			case 3:
				ct.setVal(STBorder.Enum.forInt(6));
				break;
			case 4:
				ct.setVal(STBorder.Enum.forInt(7));
				break;
			case 7:
				ct.setVal(STBorder.Enum.forInt(8));
				break;
			case 6:
				ct.setVal(STBorder.Enum.forInt(9));
				break;
			default:
				ct.setVal(STBorder.Enum.forInt(3));
				break;
			}
        }
        ct.setSz(BigInteger.valueOf(4));    
        ct.setSpace(BigInteger.ZERO);    
        ct.setColor("auto");
    }
    
    /**  
     * @MethodName: addList
     * @Description: 添加列表
     * @author caiyang
     * @param paragraph
     * @param content void
     * @date 2024-05-06 01:36:56 
     */ 
    public static void addList(XWPFDocument document,JSONObject content) {
    	String listStyle = content.getString("listStyle");
    	JSONArray valueList = content.getJSONArray("valueList");
    	if(ListUtil.isNotEmpty(valueList)) {
    		BigInteger numID = getNewDecimalNumberingId(document, BigInteger.valueOf(IdWorker.getId()),listStyle);
    		XWPFParagraph paragraph = null;
//    		paragraph.setNumID(numID);
    		XWPFRun run = null;
    		for (int i = 0; i < valueList.size(); i++) {
    			JSONObject valueObj = valueList.getJSONObject(i);
				String value = valueObj.getString("value");
				String type = valueObj.getString("type") == null?"":valueObj.getString("type");
				switch (type) {
				case "superscript":
					type = "sup";
					break;
				case "subscript":
					type = "sub";
					break;
				default:
					type = "text";
					break;
				}
				String[] values = value.split("\n");
				for (int j = 0; j < values.length; j++) {
					if(StringUtil.isNotEmpty(values[j])) {
						if(paragraph == null) {
							paragraph = document.createParagraph();
							paragraph.setNumID(numID);
							paragraph.setNumILvl(BigInteger.valueOf(0));
						}else if(j != 0) {
							paragraph = document.createParagraph();
							paragraph.setNumID(numID);
							paragraph.setNumILvl(BigInteger.valueOf(0));
						}
						run = paragraph.createRun();  
						valueObj.put("value", values[j]);
						setRunText(run,valueObj,type);
					}
				}
			}
    	}
    }
    
	private static BigInteger getNewDecimalNumberingId(XWPFDocument document, BigInteger abstractNumID,String listStyle) {
		CTAbstractNum cTAbstractNum = CTAbstractNum.Factory.newInstance();
		cTAbstractNum.setAbstractNumId(abstractNumID);

		CTLvl cTLvl = cTAbstractNum.addNewLvl();
		cTLvl.setIlvl(BigInteger.valueOf(0)); // set indent level 0
		switch (listStyle) {
		case "decimal":
			cTLvl.addNewNumFmt().setVal(STNumberFormat.DECIMAL);
			cTLvl.addNewLvlText().setVal("%1.");
			break;
		case "checkbox":
			cTLvl.addNewLvlText().setVal("■");
			break;
		case "disc":
			cTLvl.addNewLvlText().setVal("●");
			break;
		case "circle":
			cTLvl.addNewLvlText().setVal("○");
			break;
		case "square":
			cTLvl.addNewLvlText().setVal("□");
			break;
		default:
			break;
		}
		cTLvl.addNewStart().setVal(BigInteger.valueOf(1));

		XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);

		XWPFNumbering numbering = document.createNumbering();

		abstractNumID = numbering.addAbstractNum(abstractNum);

		BigInteger numID = numbering.addNum(abstractNumID);

		return numID;
	}
	
	public static void addWatermark(XWPFDocument doc, String markStr,String fontColor,int fontSize) {
		XWPFHeaderFooterPolicy headerFooterPolicy = doc.getHeaderFooterPolicy();
		if (headerFooterPolicy == null) {
			headerFooterPolicy = doc.createHeaderFooterPolicy();
		}
		headerFooterPolicy.createWatermark(markStr);
		XWPFHeader header = headerFooterPolicy.getHeader(XWPFHeaderFooterPolicy.DEFAULT);
		XWPFParagraph  paragraph = header.getParagraphArray(0);
        org.apache.xmlbeans.XmlObject[] xmlobjects = paragraph.getCTP().getRArray(0).getPictArray(0).selectChildren(
                new javax.xml.namespace.QName("urn:schemas-microsoft-com:vml", "shape"));
        if (xmlobjects.length > 0) {
            com.microsoft.schemas.vml.CTShape ctshape = (com.microsoft.schemas.vml.CTShape)xmlobjects[0];
            ctshape.setFillcolor(fontColor);//设置水印的颜色
            ctshape.setStyle(getWaterMarkStyle(ctshape.getStyle(), fontSize) + ";rotation:315");
        }
	}
	
	 /**
     * 设置水印格式
     * word
     *
     * @param styleStr
     * @param height
     * @return
     */
    public static String getWaterMarkStyle(String styleStr, double height) {
        Pattern p = Pattern.compile(";");
        String[] strs = p.split(styleStr);
        for (String str : strs) {
            if (str.startsWith("height:")) {
                String heightStr = "height:" + height + "pt";
                styleStr = styleStr.replace(str, heightStr);
                break;
            }
        }
        return styleStr;
    }
}
