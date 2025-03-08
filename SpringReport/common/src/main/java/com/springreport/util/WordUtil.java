package com.springreport.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
import org.apache.poi.xddf.usermodel.chart.XDDFLineChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFPieChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TableRowAlign;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFAbstractNum;
import org.apache.poi.xwpf.usermodel.XWPFChart;
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
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPBdr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLayoutType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblLayoutType;
import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.deepoove.poi.util.TableTools;
import com.springreport.base.DocChartSettingDto;
import com.springreport.base.MergeColDto;
import com.springreport.enums.TitleLevelEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.excel2pdf.BarCodeUtil;
import com.springreport.excel2pdf.QRCodeUtil;

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
    
    public static void setPaperMargins(XWPFDocument document,JSONArray margins) {
    	if(ListUtil.isNotEmpty(margins)) {
    		// 获取CTSectPr，如果没有则创建
            CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
            CTPageMar pageMar = sectPr.addNewPgMar();
     
            // 设置上页边距为500 (Twips, 1 Twip = 1/20 点)
            pageMar.setTop(BigInteger.valueOf((long) (margins.getLongValue(0)*14.4)));
            // 设置下页边距为500 (Twips)
            pageMar.setBottom(BigInteger.valueOf((long) (margins.getLongValue(2)*14.4)));
            // 设置左页边距为720 (Twips)
            pageMar.setLeft(BigInteger.valueOf((long) (margins.getLongValue(3)*14.4)));
            // 设置右页边距为720 (Twips)
            pageMar.setRight(BigInteger.valueOf((long) (margins.getLongValue(1)*14.4)));
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
    			addParagraph(paragraph,valueList.getJSONObject(i),headStyle,false);
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
    public static void addParagraph(XWPFParagraph paragraph,JSONObject content,String titleStyle,boolean ignoreStartn) {
    	XWPFRun run = paragraph.createRun();
    	setRunText(run,content,"text",ignoreStartn);
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
    		setSingleLineSpacing(paragraph,rowMargin);
    	}
    	if(StringUtil.isNotEmpty(titleStyle)) {
    		paragraph.setStyle(titleStyle);
    	}
    	
    }
    
    public static void setSingleLineSpacing(XWPFParagraph paragraph,Float rowMargin) {
        CTP ctp = paragraph.getCTP();
        CTPPr ppr = ctp.isSetPPr() ? ctp.getPPr() : ctp.addNewPPr();
        CTSpacing spacing = ppr.isSetSpacing()? ppr.getSpacing() : ppr.addNewSpacing();
        spacing.setAfter(BigInteger.valueOf(0));
        spacing.setBefore(BigInteger.valueOf(0));
        //注意设置行距类型为 EXACT
        spacing.setLineRule(STLineSpacingRule.EXACT);
        //1磅数是20
        spacing.setLine(BigInteger.valueOf((long) (rowMargin*350)));
    }


    
    private static void setRunText(XWPFRun run,JSONObject content,String type,boolean ignoreStartn) {
    	String value = content.getString("value");//内容
    	String font = content.getString("font");//字体
     	Double fontSize = content.getDouble("size");//字体大小
      	Boolean bold = content.getBoolean("bold");//是否加粗
    	String color = content.getString("color");//字体颜色
    	Boolean italic = content.getBoolean("italic");//是否斜体
    	String highlight = content.getString("highlight");//高亮
    	Boolean underline = content.getBoolean("underline");//是否下划线
    	Boolean strikeout = content.getBoolean("strikeout");//是否删除线
    	if(ignoreStartn) {
    		if(StringUtil.isNotEmpty(value) && value.startsWith("\n")) {
    			value = value.replaceFirst("\n", "");
    		}
    	}
    	if(value.contains("\n")) {
    		if("\n".equals(value)) {
    			run.setText("");
    		}else {
    			String[] textes = (value+" ").split("\n");
    			for (int i = 0; i < textes.length; i++) {
    				if(i == textes.length - 1) {
    					int lastIndex = textes[i].lastIndexOf(" ");
    					if (lastIndex != -1) {
    						textes[i] = textes[i].substring(0, lastIndex);
    					}
    				}
    				run.setText(textes[i]);
    				if(i != textes.length - 1) {
    					run.addBreak();	
    				}
    			}
//    			if(value.endsWith("\n")) {
//        			run.addBreak();	
//        		}
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
					e.printStackTrace();
				}
    		}
    		
    	}
    	if(italic != null) {
    		run.setItalic(italic);
    	}
    	if(StringUtil.isNotEmpty(highlight)) {
    		run.setTextHighlightColor(getHighlightName(highlight));
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
					run.setUnderline(UnderlinePatterns.SINGLE);
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
     * @throws Exception 
     * @date 2024-05-05 07:50:03 
     */ 
    public static int addTable(XWPFDocument doc,JSONObject tableData,JSONObject docTplChartsObj,JSONObject docTplCodesObj,Map<String, Object> dynamicData,boolean isTemplate,int abstractNumID) throws Exception {
    	int rows = 1;//行数
    	int cols = 1;//列数
    	JSONArray trList = tableData.getJSONArray("trList");
    	rows = trList.size();
    	JSONArray colgroup = tableData.getJSONArray("colgroup");
    	cols = colgroup.size();
    	String rowFlex = tableData.getString("rowFlex");
    	XWPFTable xwpfTable = doc.createTable(rows,cols);
    	String borderType = tableData.getString("borderType");
    	if("empty".equals(borderType)) {
    		//无边框设置
    		CTTblPr tblPr = xwpfTable.getCTTbl().getTblPr();
        	if (tblPr == null) tblPr = xwpfTable.getCTTbl().addNewTblPr();
        	CTTblBorders tblBorders = tblPr.addNewTblBorders();
        	tblBorders.addNewTop().setVal(STBorder.NONE);
        	tblBorders.addNewLeft().setVal(STBorder.NONE);
        	tblBorders.addNewBottom().setVal(STBorder.NONE);
        	tblBorders.addNewRight().setVal(STBorder.NONE);
        	tblBorders.addNewInsideH().setVal(STBorder.NONE);
        	tblBorders.addNewInsideV().setVal(STBorder.NONE);
    	}
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
    	Map<Integer, Boolean> isDynamicRow = new HashMap<>();
    	for (int i = 0; i < trList.size(); i++) {
    		JSONArray tdList = trList.getJSONObject(i).getJSONArray("tdList");
    		for (int j = 0; j < tdList.size(); j++) {
    			JSONObject cellInfo = tdList.getJSONObject(j);
    			boolean isMerge = cellInfo.getBooleanValue("isMerge");
				if(isMerge) {
					continue;
				}else {
					abstractNumID = setCellValue(xwpfTable.getRow(i).getCell(j),cellInfo,docTplChartsObj,docTplCodesObj,dynamicData,doc,isTemplate,abstractNumID);
				}
				if(!isDynamicRow.containsKey(i)) {
					String plainText = getCellPlainText(cellInfo);
					if(StringUtil.isNotEmpty(plainText)) {
						if((plainText.contains("{{") && plainText.contains("}}")) || (plainText.contains("[") && plainText.contains("]"))) {
							isDynamicRow.put(i, true);
						}
					}
				}
    		}
    	}
    	for (int i = 0; i < cols; i++) {
    		CTTblGridCol gridCol = tblGrid.addNewGridCol();
    		gridCol.setW(new BigInteger(String.valueOf(colgroup.getJSONObject(i).getBigDecimal("width").intValue()*16)));  
    		for (int j = 0; j < rows; j++) {
    			xwpfTable.getRow(j).getCell(i).setWidth(String.valueOf(colgroup.getJSONObject(i).getBigDecimal("width").intValue()*16));
			}
		}
    	for (int i = 0; i < rows; i++) {
    		int height = trList.getJSONObject(i).getIntValue("height");
    		if(isDynamicRow.containsKey(i)) {
            	float multiple = height/42;
            	xwpfTable.getRow(i).setHeight(42*15);
//            	if(multiple > 6) {
//            		xwpfTable.getRow(i).setHeight(height*15);
//            	}else {
//            		xwpfTable.getRow(i).setHeight(42*15);
//            	}
            	xwpfTable.getRow(i).setHeight(42*15);
    		}else {
    			xwpfTable.getRow(i).setHeight(height*15);
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
    	List<MergeColDto> mergeCellsHorizonal = new ArrayList<MergeColDto>();
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
						MergeColDto mergeColDto = new MergeColDto();
						mergeColDto.setRow(i+k);
						mergeColDto.setFrom(j);
						mergeColDto.setTo(j+colspan-1);
						mergeCellsHorizonal.add(mergeColDto);
					}
				}
    		}
    	}
    	if(ListUtil.isNotEmpty(mergeCellsHorizonal)) {
    		Map<Integer, List<MergeColDto>> groupBy = mergeCellsHorizonal.stream().collect(Collectors.groupingBy(MergeColDto::getRow));
    		for (List<MergeColDto> value : groupBy.values()) {
    			value.sort(Comparator.comparing(MergeColDto::getFrom).reversed());
    			for (int i = 0; i < value.size(); i++) {
    				TableTools.mergeCellsHorizonal(xwpfTable, value.get(i).getRow(), value.get(i).getFrom(),value.get(i).getTo());
				}
    		}
    	}
    	return abstractNumID;
    }
    
    public static String getCellPlainText(JSONObject cellInfo) {
    	StringBuffer sb = new StringBuffer();
    	JSONArray values = cellInfo.getJSONArray("value");
    	if(ListUtil.isNotEmpty(values)) {
    		for (int i = 0; i < values.size(); i++) {
    			JSONObject content = values.getJSONObject(i);
            	String value = content.getString("value");//内容
            	sb.append(value);
			}
    	}
    	return sb.toString();
    }
    
	/**
     * 是否允许表格自动重调单元格宽度 对应【表格属性-表格-选项-自动重调尺寸以适应内容】
     * */
    public static void setTableWidthFixed(XWPFTable table,boolean isFixed){
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CTTblLayoutType tblLayout = tblPr.isSetTblLayout() ? tblPr.getTblLayout() : tblPr.addNewTblLayout();
        if(isFixed) {
        	tblLayout.setType(STTblLayoutType.AUTOFIT);
        }
    }
    
    private static int setCellValue(XWPFTableCell cell,JSONObject cellInfo,JSONObject docTplChartsObj,JSONObject docTplCodesObj,Map<String, Object> dynamicData,XWPFDocument doc,boolean isTemplate,int abstractNumID) throws Exception {
    	if(cell == null) {
    		return abstractNumID;
    	}
    	String backgroundColor = cellInfo.getString("backgroundColor");
    	if(StringUtil.isNotEmpty(backgroundColor)) {
    		cell.setColor(backgroundColor.replaceFirst("#", ""));
    	}
    	JSONArray values = cellInfo.getJSONArray("value");
    	String verticalAlign = cellInfo.getString("verticalAlign");
    	if(ListUtil.isNotEmpty(values)) {
            XWPFParagraph paragraph = null;
            Float pRowMargin = 1f;
            String lastType = "";
            for (int i = 0; i < values.size(); i++) {
            	JSONObject content = values.getJSONObject(i);
            	String type = content.getString("type") == null?"":content.getString("type");
            	switch (type) {
				case "":
					if(content.getString("value").startsWith("\n") || 
							(!type.equals(lastType) && !"tab".equals(lastType) 
									&& !"superscript".equals(lastType) 
									&& !"subscript".equals(lastType)
									&& !"separator".equals(lastType)
									&& !"hyperlink".equals(lastType))) {
						content.put("value", content.getString("value").replaceFirst("\n", ""));
						if(i == 0) {
							paragraph = cell.getParagraphs().get(0);
						}else {
							paragraph = cell.addParagraph();
						}
					}
					if("separator".equals(lastType)) {
						String value = content.getString("value");
						if(StringUtil.isNotEmpty(value) && value.startsWith("\n")) {
							content.put("value", value.replaceFirst("\n", ""));
						}
					}
					if(paragraph == null) {
						if(i == 0) {
							paragraph = cell.getParagraphs().get(0);
						}else {
							paragraph = cell.addParagraph();
						}
					}
					WordUtil.addParagraph(paragraph,content, null,false);
					break;
				case "superscript":
					if(paragraph == null) {
						if(i == 0) {
							paragraph = cell.getParagraphs().get(0);
						}else {
							paragraph = cell.addParagraph();
						}
					}
					WordUtil.addSubSupScript(paragraph, content, "sup");
					break;
				case "subscript":
					if(paragraph == null) {
						if(i == 0) {
							paragraph = cell.getParagraphs().get(0);
						}else {
							paragraph = cell.addParagraph();
						}
					}
					WordUtil.addSubSupScript(paragraph, content, "sub");
					break;
				case "hyperlink":
					if(paragraph == null) {
						if(i == 0) {
							paragraph = cell.getParagraphs().get(0);
						}else {
							paragraph = cell.addParagraph();
						}
					}
					WordUtil.addHyperlink(paragraph, content);
					break;
				case "list":
					abstractNumID = WordUtil.addCellList(doc, content, abstractNumID, cell);
					break;
				case "image":
					if(paragraph == null) {
						if(i == 0) {
							paragraph = cell.getParagraphs().get(0);
						}else {
							paragraph = cell.addParagraph();
						}
					}
					WordUtil.addImage(paragraph, content);
					break;
				default:
					break;
				}
            	lastType = type;
        	}
            if(pRowMargin != null && paragraph != null) {
            	paragraph.setSpacingBetween(pRowMargin,LineSpacingRule.AUTO);
        	}
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
    	return abstractNumID;
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
    	setRunText(run,content,type,false);
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
		     case 1:
		        ct.setVal(STBorder.Enum.forInt(6));
		        break;
			case 3:
				ct.setVal(STBorder.Enum.forInt(22));
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
    
    public static void addHyperlink(XWPFParagraph paragraph,JSONObject content) {
    	JSONArray valueList = content.getJSONArray("valueList");
    	String url = content.getString("url");
    	if(ListUtil.isNotEmpty(valueList)) {
    		XWPFRun run = null;
    		for (int i = 0; i < valueList.size(); i++) {
    			JSONObject valueObj = valueList.getJSONObject(i);
    			String value = valueObj.getString("value");
    			run = paragraph.createHyperlinkRun(url);
    			valueObj.put("value", value);
    			setRunText(run,valueObj,"text",false);
    		}
    	}
    }
    
    public static void addPageBreak(XWPFParagraph paragraph) {
    	paragraph.createRun().addBreak(BreakType.PAGE);
    }
    
    /**  
     * @MethodName: addList
     * @Description: 添加列表
     * @author caiyang
     * @param paragraph
     * @param content void
     * @date 2024-05-06 01:36:56 
     */ 
    public static int addList(XWPFDocument document,JSONObject content,int abstractNumID) {
    	String listStyle = content.getString("listStyle");
    	JSONArray valueList = content.getJSONArray("valueList");
    	if(ListUtil.isNotEmpty(valueList)) {
    		BigInteger numID = getNewDecimalNumberingId(document, BigInteger.valueOf(abstractNumID),listStyle);
    		abstractNumID = abstractNumID + 1;
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
				if("\n".equals(value)) {
					if(paragraph == null) {
						paragraph = document.createParagraph();
						paragraph.setNumID(numID);
						paragraph.setNumILvl(BigInteger.valueOf(0));
					}else {
						paragraph = document.createParagraph();
						paragraph.setNumID(numID);
						paragraph.setNumILvl(BigInteger.valueOf(0));
					}
					run = paragraph.createRun();  
					valueObj.put("value", value);
					setRunText(run,valueObj,type,false);
				}else {
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
							setRunText(run,valueObj,type,false);
						}
					}
				}
				
			}
    	}
    	return abstractNumID;
    }
    
    /**  
     * @MethodName: addList
     * @Description: 添加列表
     * @author caiyang
     * @param paragraph
     * @param content void
     * @date 2024-05-06 01:36:56 
     */ 
    public static int addCellList(XWPFDocument document,JSONObject content,int abstractNumID,XWPFTableCell cell) {
    	String listStyle = content.getString("listStyle");
    	JSONArray valueList = content.getJSONArray("valueList");
    	if(ListUtil.isNotEmpty(valueList)) {
    		BigInteger numID = getNewDecimalNumberingId(document, BigInteger.valueOf(abstractNumID),listStyle);
    		abstractNumID = abstractNumID + 1;
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
				if("\n".equals(value)) {
					if(ListUtil.isEmpty(cell.getParagraphs().get(0).getRuns())) {
						paragraph = cell.getParagraphs().get(0);
					}else {
						paragraph = cell.addParagraph();
					}
					paragraph.setNumID(numID);
					paragraph.setNumILvl(BigInteger.valueOf(0));
					run = paragraph.createRun();  
					valueObj.put("value", value);
					setRunText(run,valueObj,type,false);
				}else {
					String[] values = value.split("\n");
					for (int j = 0; j < values.length; j++) {
						if(StringUtil.isNotEmpty(values[j])) {
							if(ListUtil.isEmpty(cell.getParagraphs().get(0).getRuns())) {
								paragraph = cell.getParagraphs().get(0);
							}else {
								paragraph = cell.addParagraph();
							}
							paragraph.setNumID(numID);
							paragraph.setNumILvl(BigInteger.valueOf(0));
							run = paragraph.createRun();  
							valueObj.put("value", values[j]);
							setRunText(run,valueObj,type,false);
						}
					}
				}
				
			}
    	}
    	return abstractNumID;
    }
    
    /**  
     * @MethodName: addImage
     * @Description: 添加图片
     * @author caiyang
     * @param document
     * @param content void
     * @date 2024-08-07 11:18:34 
     */ 
    public static void addImage(XWPFParagraph paragraph,JSONObject content) {
    	InputStream in;
    	try {
    		BufferedImage image;
    		URL url = new URL(content.getString("value"));
    		in = url.openStream();
    		image = ImageIO.read(url);
    		XWPFRun run = paragraph.createRun();
    		run.addPicture(in, org.apache.poi.xwpf.usermodel.Document.PICTURE_TYPE_PNG, "",
    		Units.pixelToEMU(content.getIntValue("width")), Units.pixelToEMU(content.getIntValue("height")));
    		String rowFlex = content.getString("rowFlex");//对齐方式
			if (StringUtil.isNotEmpty(rowFlex)) {
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
			} else {
				paragraph.setAlignment(ParagraphAlignment.LEFT);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
    
    public static void addImage(XWPFParagraph paragraph,JSONObject content,byte[] codeByte) {
    	InputStream in;
    	try {
    		in = new ByteArrayInputStream(codeByte);
    		XWPFRun run = paragraph.createRun();
    		run.addPicture(in, org.apache.poi.xwpf.usermodel.Document.PICTURE_TYPE_PNG, "",
    		Units.pixelToEMU(content.getIntValue("width")), Units.pixelToEMU(content.getIntValue("height")));
    		String rowFlex = content.getString("rowFlex");//对齐方式
			if (StringUtil.isNotEmpty(rowFlex)) {
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
			} else {
				paragraph.setAlignment(ParagraphAlignment.LEFT);
			}
		} catch (Exception e) {
			// TODO: handle exception
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
			cTLvl.addNewNumFmt().setVal(STNumberFormat.BULLET);
			cTLvl.addNewLvlText().setVal("■");
			break;
		case "disc":
			cTLvl.addNewNumFmt().setVal(STNumberFormat.BULLET);
			cTLvl.addNewLvlText().setVal("●");
			break;
		case "circle":
			cTLvl.addNewNumFmt().setVal(STNumberFormat.BULLET);
			cTLvl.addNewLvlText().setVal("○");
			break;
		case "square":
			cTLvl.addNewNumFmt().setVal(STNumberFormat.BULLET);
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
    
    /**  
     * @MethodName: addChart
     * @Description: 添加图表
     * @author caiyang
     * @param paragraph
     * @param content void
     * @throws Exception 
     * @date 2024-08-08 10:11:54 
     */ 
    public static void addChart(XWPFDocument document,XWPFParagraph paragraph,JSONObject content,Map<String, Object> dynamicData,boolean isTemplate,DocChartSettingDto docChartSettingDto) throws Exception {
    	switch (docChartSettingDto.getChartType()) {
		case "pie":
			addPieChart(document,paragraph, content, dynamicData,isTemplate,docChartSettingDto);
			break;
		case "histogram":
			addHistogramChart(document,paragraph, content, dynamicData,isTemplate,docChartSettingDto,false);
			break;
		case "horizontalHistogram":
			addHistogramChart(document,paragraph, content, dynamicData,isTemplate,docChartSettingDto,true);
			break;
		case "line":
			addLineChart(document,paragraph, content, dynamicData,isTemplate,docChartSettingDto);
			break;
		default:
			break;
		}
    	String rowFlex = content.getString("rowFlex");//对齐方式
    	if (StringUtil.isNotEmpty(rowFlex)) {
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
		} else {
			paragraph.setAlignment(ParagraphAlignment.LEFT);
		}
    }
    
    /**  
     * @MethodName: addPieChart
     * @Description: 添加饼图
     * @author caiyang void
     * @throws IOException 
     * @throws InvalidFormatException 
     * @date 2024-08-08 10:16:17 
     */ 
    private static void addPieChart(XWPFDocument document,XWPFParagraph paragraph,JSONObject content,Map<String, Object> dynamicData,boolean isTemplate,DocChartSettingDto docChartSettingDto) throws Exception {
    	XWPFRun xwpfRun = paragraph.createRun();
    	//1.创建chart图表对象
    	XWPFChart chart = document.createChart(xwpfRun,Units.pixelToEMU(content.getIntValue("width")), Units.pixelToEMU(content.getIntValue("height")));
    	// 2.图表相关设置
    	if(docChartSettingDto.getShowChartName().intValue() == YesNoEnum.YES.getCode().intValue()) {
    		chart.setTitleText(docChartSettingDto.getChartName()); // 图表标题
    	}
        chart.setTitleOverlay(false); // 图例是否覆盖标题
        // 3.图例设置
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.BOTTOM); // 图例位置:上下左右
        // 4.X轴(分类轴)相关设置:饼图中的图例显示
        String[] xAxisData = null;
        List<Map<String, Object>> datas = null;
        if(isTemplate) {
        	xAxisData = new String[] {
                    "oxygen","silicon","aluminum","iron","calcium","sodium",
                    "potassium","others",
            };
        }else {
        	datas = (List<Map<String, Object>>) dynamicData.get(docChartSettingDto.getDatasetName());
        	String categoryField = docChartSettingDto.getCategoryField();
        	List<String> categories = new ArrayList<>();
        	for (int i = 0; i < datas.size(); i++) {
        		Object obj = datas.get(i).get(categoryField);
        		categories.add(String.valueOf(obj));
			}
        	xAxisData = categories.stream().toArray(String[]::new);
        }
        
        XDDFCategoryDataSource xAxisSource = XDDFDataSourcesFactory.fromArray(xAxisData); // 设置分类数据

        // 5.Y轴(值轴)相关设置:饼图中的圆形显示
        Double[] yAxisData = null;
        if(isTemplate) {
        	yAxisData = new Double[]{
            		46.60, 27.72, 8.13, 5.0, 3.63, 2.83,
                    2.59, 3.5
            };
        }else {
        	String valueField = JSON.parseArray(docChartSettingDto.getValueField()).getString(0);
        	List<Double> values = new ArrayList<>();
        	for (int i = 0; i < datas.size(); i++) {
        		Object obj = datas.get(i).get(valueField);
        		values.add(Double.parseDouble(String.valueOf(obj)));
        	}
        	yAxisData = values.stream().toArray(Double[]::new);
        }
        XDDFNumericalDataSource<Double> yAxisSource = XDDFDataSourcesFactory.fromArray(yAxisData); // 设置值数据
        // 6.创建饼图对象,饼状图不需要X,Y轴,只需要数据集即可
        XDDFPieChartData pieChart = (XDDFPieChartData) chart.createData(ChartTypes.PIE, null, null);
        // 7.加载饼图数据集
        XDDFPieChartData.Series pieSeries = (XDDFPieChartData.Series) pieChart.addSeries(xAxisSource, yAxisSource);
//        pieSeries.setTitle("", null); // 系列提示标题
        CTPieSer ctPieSer = pieSeries.getCTPieSer();
        showCateName(ctPieSer, false);
        showVal(ctPieSer, true);
        showPercent(ctPieSer, false);
        showLegendKey(ctPieSer, false);
        showSerName(ctPieSer, false);
        // 8.绘制饼图
        chart.plot(pieChart);
        // 9.设置替换文字，格式是{{数据集名称}}，根据该内容给chart动态赋值
        xwpfRun.getCTR().getDrawingArray(0).getInlineArray(0).getDocPr().setDescr("{{"+docChartSettingDto.getDatasetName()+"_"+docChartSettingDto.getChartType()+"}}");
    }
    
    /**  
     * @MethodName: addHistogramChart
     * @Description: 柱状图
     * @author caiyang
     * @param document
     * @param paragraph
     * @param content
     * @param dynamicData
     * @param isTemplate
     * @param docChartSettingDto
     * @throws Exception void
     * @date 2024-08-10 08:16:03 
     */ 
    public static void addHistogramChart(XWPFDocument document,XWPFParagraph paragraph,JSONObject content,Map<String, Object> dynamicData,boolean isTemplate,DocChartSettingDto docChartSettingDto,boolean isBar) throws Exception{
    	XWPFRun xwpfRun = paragraph.createRun();
    	//创建chart图表对象
    	XWPFChart chart = document.createChart(xwpfRun,Units.pixelToEMU(content.getIntValue("width")), Units.pixelToEMU(content.getIntValue("height")));
    	//图表相关设置
    	if(docChartSettingDto.getShowChartName().intValue() == YesNoEnum.YES.getCode().intValue()) {
    		chart.setTitleText(docChartSettingDto.getChartName()); // 图表标题
    	}
    	chart.setTitleOverlay(false); // 图例是否覆盖标题
    	
        //x轴 y轴设置
        XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT);
        yAxis.setCrosses(AxisCrosses.AUTO_ZERO);
        yAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
        
        String[] categories = null;//x轴标签数据
        String[] seriesData = null;//分类数据
        XDDFDataSource<String> categoryDS = null;
        List<XDDFNumericalDataSource<Double>> valueDS = new ArrayList<>();
        //数据处理
        if(isTemplate) {
        	if(isBar) {
        		categories = new String[] { "北京", "天津", "重庆","深圳", "广州", "山东" };
            	seriesData = new String[] { };
        	}else {
        		categories = new String[] { "Nail polish", "Eyebrow pencil", "Rouge" };
            	seriesData = new String[] { "Africa", "EU", "China", "USA" };
        	}
        	
			
			Double[] valuesA = new Double[] { 4229d, 3932d, 5221d };
			if(isBar) {
				valuesA = new Double[] { 3080d, 2880d, 880d, 780d, 680d, 580d };
			}
			Double[] valuesB = new Double[] { 4376d, 3987d, 3574d };
			Double[] valuesC = new Double[] { 3054d, 5067d, 7004d };
			Double[] valuesD = new Double[] { 12814d, 13012d, 11624d };
			categoryDS = XDDFDataSourcesFactory.fromArray(categories);
		    
		    XDDFNumericalDataSource<Double> s = XDDFDataSourcesFactory.fromArray(valuesA);
            valueDS.add(s);
            if(!isBar) {
            	XDDFNumericalDataSource<Double> s1 = XDDFDataSourcesFactory.fromArray(valuesB);
                valueDS.add(s1);
                XDDFNumericalDataSource<Double> s2 = XDDFDataSourcesFactory.fromArray(valuesC);
                valueDS.add(s2);
                XDDFNumericalDataSource<Double> s3 = XDDFDataSourcesFactory.fromArray(valuesD);
                valueDS.add(s3);
            }
        }else {
        	Map<String, Object> dataMap = processDatas(dynamicData, docChartSettingDto);
        	categoryDS = XDDFDataSourcesFactory.fromArray((String[]) dataMap.get("categories"));
        	valueDS = (List<XDDFNumericalDataSource<Double>>) dataMap.get("valueDS");
        	seriesData = (String[]) dataMap.get("seriesData");
        	
        }
        XDDFChartData data = chart.createData(ChartTypes.BAR, xAxis, yAxis);
        if(isBar) {
        	 ((XDDFBarChartData) data).setBarDirection(BarDirection.BAR);
        }else {
        	 ((XDDFBarChartData) data).setBarDirection(BarDirection.COL);
        }
        int i = 0;
        for (XDDFNumericalDataSource<Double> value : valueDS) {
            XDDFChartData.Series series = data.addSeries(categoryDS, value);
            if(valueDS.size() > 1) {
            	series.setTitle(seriesData[i], null);
            }else {
            	series.setTitle("", null);
            }
            chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(i).addNewDLbls().addNewShowVal().setVal(true);
            chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(i).getDLbls().addNewShowLegendKey().setVal(false);
            chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(i).getDLbls().addNewShowCatName().setVal(false);
            chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(i).getDLbls().addNewShowSerName().setVal(false);
            i++;
        }
        // 图例设置
        if(valueDS.size() > 1) {
        	XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.BOTTOM); // 图例位置:上下左右
        }else {
        	((XDDFBarChartData) data).setVaryColors(false);
        }
        chart.plot(data);
        // 设置替换文字，格式是{{数据集名称}}，根据该内容给chart动态赋值
        xwpfRun.getCTR().getDrawingArray(0).getInlineArray(0).getDocPr().setDescr("{{"+docChartSettingDto.getDatasetName()+"_"+docChartSettingDto.getChartType()+"}}");
    }
    
    public static void addLineChart(XWPFDocument document,XWPFParagraph paragraph,JSONObject content,Map<String, Object> dynamicData,boolean isTemplate,DocChartSettingDto docChartSettingDto) throws Exception{
    	XWPFRun xwpfRun = paragraph.createRun();
    	//创建chart图表对象
    	XWPFChart chart = document.createChart(xwpfRun,Units.pixelToEMU(content.getIntValue("width")), Units.pixelToEMU(content.getIntValue("height")));
    	//图表相关设置
    	if(docChartSettingDto.getShowChartName().intValue() == YesNoEnum.YES.getCode().intValue()) {
    		chart.setTitleText(docChartSettingDto.getChartName()); // 图表标题
    	}
    	chart.setTitleOverlay(false); // 图例是否覆盖标题
    	
        //x轴 y轴设置
        XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT);
        yAxis.setCrosses(AxisCrosses.AUTO_ZERO);
        yAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
        
        String[] categories = null;//x轴标签数据
        String[] seriesData = null;//分类数据
        XDDFDataSource<String> categoryDS = null;
        List<XDDFNumericalDataSource<Double>> valueDS = new ArrayList<>();
        //数据处理
        if(isTemplate) {
        	categories = new String[] { "2:00", "4:00", "6:00","8:00", "10:00", "12:00", "14:00", "16:00","18:00" };
            seriesData = new String[] { };
        	
			
			Double[] valuesA = new Double[] { 8d, 9d, 11d, 14d, 16d, 17d, 17d, 16d, 15d };
			categoryDS = XDDFDataSourcesFactory.fromArray(categories);
		    
		    XDDFNumericalDataSource<Double> s = XDDFDataSourcesFactory.fromArray(valuesA);
            valueDS.add(s);
        }else {
        	Map<String, Object> dataMap = processDatas(dynamicData, docChartSettingDto);
        	categoryDS = XDDFDataSourcesFactory.fromArray((String[]) dataMap.get("categories"));
        	valueDS = (List<XDDFNumericalDataSource<Double>>) dataMap.get("valueDS");
        	seriesData = (String[]) dataMap.get("seriesData");
        	
        }
        XDDFChartData data = chart.createData(ChartTypes.LINE, xAxis, yAxis);
        int i = 0;
        for (XDDFNumericalDataSource<Double> value : valueDS) {
            XDDFChartData.Series series = data.addSeries(categoryDS, value);
            if(valueDS.size() > 1) {
            	series.setTitle(seriesData[i], null);
            }else {
            	series.setTitle("", null);
            }
            chart.getCTChart().getPlotArea().getLineChartArray(0).getSerArray(i).addNewDLbls().addNewShowVal().setVal(true);
            chart.getCTChart().getPlotArea().getLineChartArray(0).getSerArray(i).getDLbls().addNewShowLegendKey().setVal(false);
            chart.getCTChart().getPlotArea().getLineChartArray(0).getSerArray(i).getDLbls().addNewShowCatName().setVal(false);
            chart.getCTChart().getPlotArea().getLineChartArray(0).getSerArray(i).getDLbls().addNewShowSerName().setVal(false);
            i++;
        }
        // 图例设置
        if(valueDS.size() > 1) {
        	XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.BOTTOM); // 图例位置:上下左右
        }else {
        	((XDDFLineChartData) data).setVaryColors(false);
        }
        chart.plot(data);
        // 设置替换文字，格式是{{数据集名称}}，根据该内容给chart动态赋值
        xwpfRun.getCTR().getDrawingArray(0).getInlineArray(0).getDocPr().setDescr("{{"+docChartSettingDto.getDatasetName()+"_"+docChartSettingDto.getChartType()+"}}");
    }
    
    private static Map<String, Object> processDatas(Map<String, Object> dynamicData,DocChartSettingDto docChartSettingDto){
    	Map<String, Object> result = new HashMap<String, Object>();
    	List<Map<String, Object>> datas = (List<Map<String, Object>>) dynamicData.get(docChartSettingDto.getDatasetName());
    	List<JSONArray> groupDatas = groupDatas(datas, docChartSettingDto);
    	List<String> categories = new ArrayList<>();//x轴标签数据
    	List<String> seriesData = new ArrayList<>();//分类数据
        List<XDDFNumericalDataSource<Double>> valueDS = new ArrayList<>();
        Map<Integer, List<Double>> valuesMap = new LinkedHashMap<>();
    	if(ListUtil.isNotEmpty(groupDatas)) {
    		for (int i = 0; i < groupDatas.size(); i++) {
    			categories.add(groupDatas.get(i).getJSONObject(0).getString("type"));
    			for (int j = 0; j < groupDatas.get(i).size(); j++) {
    				if(i == 0) {
    					seriesData.add(groupDatas.get(i).getJSONObject(j).getString("seriesField"));
    				}
    				List<Double> values = null;
					if(!valuesMap.containsKey(j)) {
						 values = new ArrayList<>();
						 valuesMap.put(j, values);
					}else {
						values = valuesMap.get(j);
					}
					values.add(groupDatas.get(i).getJSONObject(j).getDoubleValue("value"));
				}
    		}
    	}
    	valuesMap.forEach((key,value) -> {
    		XDDFNumericalDataSource<Double> s = XDDFDataSourcesFactory.fromArray(value.stream().toArray(Double[]::new));
    		valueDS.add(s);
        });
    	result.put("categories", categories.stream().toArray(String[]::new));
    	result.put("seriesData", seriesData.stream().toArray(String[]::new));
    	result.put("valueDS", valueDS);
    	return result;
    }
    
    private static List<JSONArray> groupDatas(List<Map<String, Object>> datas,DocChartSettingDto docChartSettingDto){
    	JSONArray valueField = JSON.parseArray(docChartSettingDto.getValueField());
    	if(valueField == null) {
			valueField = new JSONArray();
		}
    	JSONArray seriesField = JSON.parseArray(docChartSettingDto.getSeriesField());
    	if(seriesField == null) {
    		seriesField = new JSONArray();
		}
    	boolean flag = false;
		if(seriesField.size()>=valueField.size()) {
			flag = true;
		}
		JSONArray chartDatas = new JSONArray();
		if(ListUtil.isNotEmpty(datas)) {
			if(ListUtil.isNotEmpty(valueField)) {
				for (int i = 0; i < valueField.size(); i++) {
					for (int j = 0; j < datas.size(); j++) {
						JSONObject chartData = new JSONObject();
						chartData.put("value", datas.get(j).get(valueField.getString(i)));
						chartData.put("type", datas.get(j).get(docChartSettingDto.getCategoryField()));
						if(flag) {
							chartData.put("seriesField", datas.get(j).get(seriesField.getString(i)));
						}else {
							chartData.put("seriesField", "系列"+(i+1));
						}
						chartDatas.add(chartData);
					}
				}
			}
		}
		List<JSONArray> result = new ArrayList<>();
		if(ListUtil.isNotEmpty(chartDatas)) {
			Map<String, JSONArray> dataMap = new LinkedHashMap<>();
    		for (int i = 0; i < chartDatas.size(); i++) {
    			JSONArray rowList = null;
    			JSONObject data = chartDatas.getJSONObject(i);
    			String key = String.valueOf(data.get("type"));
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
    
    private static void showCateName(CTPieSer series, boolean val) {
    	if(series.getDLbls() == null) {
    		series.addNewDLbls();
    	}
 	   if (series.getDLbls().isSetShowCatName()) {
 	     series.getDLbls().getShowCatName().setVal(val);
 	   } else {
 	     series.getDLbls().addNewShowCatName().setVal(val);
 	   }
 	 }

 	 // 控制值是否显示
    private static void showVal(CTPieSer series, boolean val) {
 		if(series.getDLbls() == null) {
    		series.addNewDLbls();
    	}
 	   if (series.getDLbls().isSetShowVal()) {
 	     series.getDLbls().getShowVal().setVal(val);
 	   } else {
 	     series.getDLbls().addNewShowVal().setVal(val);
 	   }
 	 }
 	 
    private static void showPercent(CTPieSer series, boolean val) {
 		if(series.getDLbls() == null) {
    		series.addNewDLbls();
    	}
 		   if (series.getDLbls().isSetShowPercent()) {
 		     series.getDLbls().getShowPercent().setVal(val);
 		   } else {
 		     series.getDLbls().addNewShowPercent().setVal(val);
 		   }
 		 }

 	 // 控制值系列名称是否显示
    private static void showSerName(CTPieSer series, boolean val) {
 		if(series.getDLbls() == null) {
    		series.addNewDLbls();
    	}
 	   if (series.getDLbls().isSetShowSerName()) {
 	     series.getDLbls().getShowSerName().setVal(val);
 	   } else {
 	     series.getDLbls().addNewShowSerName().setVal(val);
 	   }
 	 }

 	 // 控制图例标识是否显示
    private static void showLegendKey(CTPieSer series, boolean val) {
 	   if (series.getDLbls().isSetShowLegendKey()) {
 	     series.getDLbls().getShowLegendKey().setVal(val);
 	   } else {
 	     series.getDLbls().addNewShowLegendKey().setVal(val);
 	   }
 	 }
    
	/**
	 * @param color
	 * @return
	 * @date 2020年4月7日 下午7:16:39
	 */
	private static String getHighlightName(String color) {
		color = color.replaceAll("\\s+", "");
		if ("#FFFF00".equals(color)) {
			return "yellow";//1-黄色
		} else if ("#00FF00".equals(color)) {
			return "green";//2-绿色
		} else if ("#00FFFF".equals(color)) {
			return "cyan";//3-青色
		} else if ("#FF00FF".equals(color)) {
			return "magenta";//4-粉红色
		} else if ("#0000FF".equals(color)) {
			return "blue";//5-蓝色
		} else if ("#FF0000".equals(color)) {
			return "red";//6-红色
		} else if ("#000080".equals(color)) {
			return "darkBlue";//7-深蓝色
		} else if ("#008080".equals(color)) {
			return "darkCyan";//8-深青色
		} else if ("#008000".equals(color)) {
			return "darkGreen";//9-深绿色
		} else if ("#800080".equals(color)) {
			return "darkMagenta";//10-深粉红色，紫色
		} else if ("#800000".equals(color)) {
			return "darkRed";//11-深红色
		} else if ("#808000".equals(color)) {
			return "darkYellow";//12-深黄色
		} else if ("#808080".equals(color)) {
			return "darkGray";//13-深灰色
		} else if ("#C0C0C0".equals(color)) {
			return "lightGray";//14-浅灰色
		} else if ("#000000".equals(color)) {
			return "black";//15-黑色
		} else {
			return "none";//无色
		}
	}
	
	public static String getHighlightByName(String name) {
		if ("yellow".equals(name)) {
			return "#FFFF00";//1-黄色
		} else if ("green".equals(name)) {
			return "#00FF00";//2-绿色
		} else if ("cyan".equals(name)) {
			return "#00FFFF";//3-青色
		} else if ("magenta".equals(name)) {
			return "#FF00FF";//4-粉红色
		} else if ("blue".equals(name)) {
			return "#0000FF";//5-蓝色
		} else if ("red".equals(name)) {
			return "#FF0000";//6-红色
		} else if ("darkBlue".equals(name)) {
			return "#000080";//7-深蓝色
		} else if ("darkCyan".equals(name)) {
			return "#008080";//8-深青色
		} else if ("darkGreen".equals(name)) {
			return "#008000";//9-深绿色
		} else if ("darkMagenta".equals(name)) {
			return "#800080";//10-深粉红色，紫色
		} else if ("#800000".equals(name)) {
			return "darkRed";//11-深红色
		} else if ("darkYellow".equals(name)) {
			return "#808000";//12-深黄色
		} else if ("darkGray".equals(name)) {
			return "#808080";//13-深灰色
		} else if ("lightGray".equals(name)) {
			return "#C0C0C0";//14-浅灰色
		} else if ("black".equals(name)) {
			return "#000000";//15-黑色
		} else {
			return "";//无色
		}
	}
}
