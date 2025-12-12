package com.springreport.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.ColorScaleFormatting;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.ConditionFilterType;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.ExtendedColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FontFormatting;
import org.apache.poi.ss.usermodel.IconMultiStateFormatting.IconSet;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.ShapeTypes;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.streaming.SXSSFDrawing;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFConditionalFormattingRule;
import org.apache.poi.xssf.usermodel.XSSFDataBarFormatting;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSheetConditionalFormatting;
import org.apache.poi.xssf.usermodel.XSSFSimpleShape;
import org.apache.poi.xssf.usermodel.XSSFTextBox;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.PieDataset;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfType;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springreport.base.CellValueDto;
import com.springreport.base.MesExportExcel;
import com.springreport.enums.LuckySheetPropsEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.excel2pdf.BarCodeUtil;
import com.springreport.excel2pdf.QRCodeUtil;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.http.HttpUtil;

/**  
 * @ClassName: ReportExcelUtil
 * @Description: 报表导出excel工具类
 * @author caiyang
 * @date 2021-06-09 07:18:57 
*/  
public class ReportExcelUtil {
	
	/**  
	 * @Fields defaultRowHeight : 默认行高
	 * @author caiyang
	 * @date 2022-06-07 09:13:49 
	 */  
	private final static short defaultRowHeight = 20;
	
	/**  
	 * @Fields defaultColWidth : 默认列宽
	 * @author caiyang
	 * @date 2022-06-07 09:13:59 
	 */  
	private final static short defaultColWidth = 73;

	public static void export(Map<String, Object> map,HttpServletResponse httpServletResponse, String sheetname, String filename) throws Exception
	{
		List<CellValueDto> cellValues = (List<CellValueDto>) map.get("cellValues");
		Map<String, Integer> maxXAndY = (Map<String, Integer>) map.get("maxXAndY");
		int maxX = maxXAndY.get("maxX");
		int maxY = maxXAndY.get("maxY");
		httpServletResponse.setContentType("octets/stream");
    	//设置文件名编码格式
        filename = URLEncoder.encode(filename, "UTF-8");
        httpServletResponse.addHeader("Content-Disposition", "attachment;filename=" +filename + ".xlsx");
        httpServletResponse.addHeader("filename", filename + ".xlsx");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        SXSSFWorkbook wb = new SXSSFWorkbook();
        SXSSFSheet sheet = wb.createSheet(sheetname);
        sheet.setRandomAccessWindowSize(-1);
        CellUtil cellUtil = new CellUtil(wb, sheet);
        cellUtil.createCells(maxX, maxY);
        cellUtil.setCellValues(cellValues);
        try {
        	wb.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
	}
	
	/**  
	 * @MethodName: export
	 * @Description: 导出excel文件
	 * @author caiyang
	 * @param mesExportExcel
	 * @param filename
	 * @param password
	 * @param httpServletResponse 
	 * @return void
	 * @date 2022-10-21 05:23:20 
	 */  
	public static void export(MesExportExcel mesExportExcel,String filename,String password,HttpServletResponse httpServletResponse) throws Exception
	{
		httpServletResponse.setContentType("octets/stream");
    	//设置文件名编码格式
        filename = URLEncoder.encode(filename, "UTF-8");
        httpServletResponse.addHeader("Content-Disposition", "attachment;filename=" +filename + ".xlsx");
        httpServletResponse.addHeader("filename", filename + ".xlsx");
        XSSFWorkbook wb = new XSSFWorkbook();
        if(!ListUtil.isEmpty(mesExportExcel.getSheetConfigs()))
        {
        	for (int i = 0; i < mesExportExcel.getSheetConfigs().size(); i++) {
        		List<Map<String, Object>> cellDatas = mesExportExcel.getSheetConfigs().get(i).getCellDatas();
        		Map<String, Integer> maxXAndY = mesExportExcel.getSheetConfigs().get(i).getMaxXAndY();
        		Map<String, Map<String, Object>> hyperlinks = mesExportExcel.getSheetConfigs().get(i).getHyperlinks();
        		List<Object> borderInfos = mesExportExcel.getSheetConfigs().get(i).getBorderInfos();
        		Map<String, Object> rowlen = mesExportExcel.getSheetConfigs().get(i).getRowlen();
        		Map<String, Object> columnlen = mesExportExcel.getSheetConfigs().get(i).getColumnlen();
        		JSONObject frozen = mesExportExcel.getSheetConfigs().get(i).getFrozen();
        		JSONObject images = mesExportExcel.getSheetConfigs().get(i).getBase64Images();
        		List<JSONObject> imageDatas = mesExportExcel.getSheetConfigs().get(i).getImageDatas();
        		String sheetname = mesExportExcel.getSheetConfigs().get(i).getSheetname();
        		JSONObject colhidden = mesExportExcel.getSheetConfigs().get(i).getColhidden();
        		JSONObject rowhidden = mesExportExcel.getSheetConfigs().get(i).getRowhidden();
        		JSONObject dataVerification = mesExportExcel.getSheetConfigs().get(i).getDataVerification();
        		JSONObject authority = mesExportExcel.getSheetConfigs().get(i).getAuthority();
        		JSONObject filter = mesExportExcel.getSheetConfigs().get(i).getFilter();
        		JSONArray xxbtCells = new JSONArray();//斜线表头单元格
        		XSSFSheet sheet = wb.createSheet(sheetname);
        		List<String> noViewAuthCells = mesExportExcel.getSheetConfigs().get(i).getNoViewAuthCells();
        		if(!StringUtil.isEmptyMap(filter))
        		{
        			String filterRange = getFilterRange(filter);
        			CellRangeAddress c = CellRangeAddress.valueOf(filterRange);
            		sheet.setAutoFilter(c);
        		}
        		
        		sheet.setForceFormulaRecalculation(true);
        		Map<String, String> unProtectCells = null;
        		if(authority != null && authority.getIntValue("sheet") == 1)
        		{
        			unProtectCells = getUnProtectCells(authority);
        		}
        		if(colhidden != null && !colhidden.isEmpty())
        		{
        			for(String key:colhidden.keySet())
        			{
        				int value = colhidden.getIntValue(key);
        				if(value == 0)
        				{
        					int column = Integer.parseInt(key);
        					sheet.setColumnHidden(column, true);
        				}
        				
        			}
        		}
        		if(frozen != null)
                {
                	String frozenType = frozen.getString("type");
                	int row = 0;
                	int column = 0;
                	if(StringUtil.isNotEmpty(frozenType))
                	{
        	        	switch (frozenType) {
        				case "row":
        					sheet.createFreezePane(0, 1);
        					break;
        				case "column":
        					sheet.createFreezePane(1, 0);
        					break;
        				case "both":
        					sheet.createFreezePane(1, 1);
        					break;
        				case "rangeRow":
        					row = frozen.getJSONObject("range").getIntValue("row_focus");
        					sheet.createFreezePane(0, row+1);
        					break;
        				case "rangeColumn":
        					column = frozen.getJSONObject("range").getIntValue("column_focus");
        					sheet.createFreezePane(column+1,0);
        				case "rangeBoth":
        					row = frozen.getJSONObject("range").getIntValue("row_focus");
        					column = frozen.getJSONObject("range").getIntValue("column_focus");
        					sheet.createFreezePane(column+1, row+1);
        				default:
        					break;
        				}
                	}
                }
//        		sheet.setRandomAccessWindowSize(-1);
                LuckySheetCellUtil cellUtil = new LuckySheetCellUtil(wb, sheet);
                cellUtil.createCells(maxXAndY.get("maxX"), maxXAndY.get("maxY"),rowlen,rowhidden);
                if(columnlen != null)
                {
                	for (Map.Entry<String, Object> entry : columnlen.entrySet()) {
                		BigDecimal wid = new BigDecimal(String.valueOf(entry.getValue()));
                		BigDecimal excleWid=new BigDecimal(32);
                		sheet.setColumnWidth(Integer.parseInt(String.valueOf(entry.getKey())), wid.multiply(excleWid).setScale(0,BigDecimal.ROUND_HALF_UP).intValue());//列宽px值
                	}
                }
                JSONArray barCodeCells = new JSONArray();//条形码单元格
                JSONArray qrCodeCells = new JSONArray();//二维码单元格
                Map<String, Object> wrapText = new HashMap<>();
                cellUtil.setCellValues(cellDatas,hyperlinks,borderInfos,unProtectCells,mesExportExcel.getSheetConfigs().get(i).getMerge(),mesExportExcel.getSheetConfigs().get(i).getIsCoedit(), dataVerification,xxbtCells,false,barCodeCells,qrCodeCells,wrapText,noViewAuthCells);
                cellUtil.setRowHeight(maxXAndY.get("maxX"), rowlen, rowhidden, wrapText);
                cellUtil.setWrapTextRowHeight(maxXAndY.get("maxX"), rowlen, rowhidden, wrapText);
                JSONObject rowlenObj = JSONObject.parseObject(JSONObject.toJSONString(rowlen));
                JSONObject columnlenObj = JSONObject.parseObject(JSONObject.toJSONString(columnlen));
                setImages(wb,sheet,images,columnlenObj,rowlenObj,rowhidden,colhidden,mesExportExcel.getImageInfos(),mesExportExcel.getBackImages());
                insertUrlImg(wb,sheet,imageDatas);
                insertChart(wb, sheet, mesExportExcel.getSheetConfigs().get(i).getChart(),mesExportExcel.getSheetConfigs().get(i).getChartCells(),mesExportExcel.getSheetConfigs().get(i).getIsCoedit(),rowlenObj,rowhidden,columnlenObj,colhidden);
//                insertBase64Chart(wb, sheet, mesExportExcel.getSheetConfigs().get(i).getChart(),mesExportExcel.getSheetConfigs().get(i).getChartCells(),cellUtil,mesExportExcel.getChartsBase64());
                addDataVerification(sheet, dataVerification);
                if(authority != null && authority.getIntValue("sheet") == 1)
        		{
        			sheet.protectSheet(StringUtil.isNotEmpty(authority.getString("password"))?authority.getString("password"):"");
        		}
                if(!ListUtil.isEmpty(xxbtCells))
                {
                	List<SlashLinePosition> slashes = new ArrayList<>();
                	List<SlashLineText> slashTexts = new ArrayList<>();
                	for (int j = 0; j < xxbtCells.size(); j++) {
						JSONObject cellData = xxbtCells.getJSONObject(j);
						int r = cellData.getIntValue("r");
						int c = cellData.getIntValue("c");
						int rs = 1;
						int cs = 1;
						String cellValue = "";
						if(cellData.getJSONObject("v") != null)
						{
							JSONObject v = cellData.getJSONObject("v");
							if(v.containsKey("mc"))
							{
								rs = v.getJSONObject("mc").getInteger("rs") != null?v.getJSONObject("mc").getInteger("rs"):1;
								cs = v.getJSONObject("mc").getInteger("cs") != null?v.getJSONObject("mc").getInteger("cs"):1;
							}
							cellValue = v.getString("v");
						}
						getSlashLinePositionXlsx(sheet,r,c,rs,cs,cellValue,slashes,slashTexts);
					}
                	drawLineXlsx(sheet,slashes,slashTexts);
                }
                if(ListUtil.isNotEmpty(barCodeCells)) {
                	for (int j = 0; j < barCodeCells.size(); j++) {
                		JSONObject cellValue = barCodeCells.getJSONObject(j).getJSONObject(LuckySheetPropsEnum.CELLCONFIG.getCode());
                		JSONObject mergeCell = cellValue.getJSONObject(LuckySheetPropsEnum.MERGECELLS.getCode());
                		if(StringUtil.isNotEmpty(cellValue.getString("v"))) {
                			byte[] barCodeByte = BarCodeUtil.generateBarcodeImage(cellValue.getString("v"), 200, 100);
                    		if(mergeCell != null) {
                    			if(mergeCell.containsKey(LuckySheetPropsEnum.ROWSPAN.getCode()) && mergeCell.containsKey(LuckySheetPropsEnum.COLSPAN.getCode())) {
                    				int firstRow = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.R.getCode())));
        							int lastRow = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.R.getCode()))) + Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.ROWSPAN.getCode()))) - 1;
        							int firstCol = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.C.getCode())));
        							int lastCol = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.C.getCode()))) + Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.COLSPAN.getCode()))) - 1;
        							ReportExcelUtil.insertBarcode(wb, sheet, barCodeByte, firstRow, lastRow, firstCol, lastCol);
                    			}
                    		}else {
                    			int r = barCodeCells.getJSONObject(j).getIntValue("r");
    							int c = barCodeCells.getJSONObject(j).getIntValue("c");
    							ReportExcelUtil.insertBarcode(wb, sheet, barCodeByte, r, r, c, c);
                    		}
                		}
					}
                }
                
                if(ListUtil.isNotEmpty(qrCodeCells)) {
                	for (int j = 0; j < qrCodeCells.size(); j++) {
                		JSONObject cellValue = qrCodeCells.getJSONObject(j).getJSONObject(LuckySheetPropsEnum.CELLCONFIG.getCode());
                		JSONObject mergeCell = cellValue.getJSONObject(LuckySheetPropsEnum.MERGECELLS.getCode());
                		if(StringUtil.isNotEmpty(cellValue.getString("v"))) {
                			byte[] barCodeByte = QRCodeUtil.generateQRCodeImage(cellValue.getString("v"), 256, 256);
                    		if(mergeCell != null) {
                    			if(mergeCell.containsKey(LuckySheetPropsEnum.ROWSPAN.getCode()) && mergeCell.containsKey(LuckySheetPropsEnum.COLSPAN.getCode())) {
                    				int firstRow = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.R.getCode())));
        							int lastRow = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.R.getCode()))) + Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.ROWSPAN.getCode()))) - 1;
        							int firstCol = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.C.getCode())));
        							int lastCol = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.C.getCode()))) + Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.COLSPAN.getCode()))) - 1;
        							ReportExcelUtil.insertBarcode(wb, sheet, barCodeByte, firstRow, lastRow, firstCol, lastCol);
                    			}
                    		}else {
                    			int r = qrCodeCells.getJSONObject(j).getIntValue("r");
    							int c = qrCodeCells.getJSONObject(j).getIntValue("c");
    							ReportExcelUtil.insertBarcode(wb, sheet, barCodeByte, r, r, c, c);
                    		}
                		}
					}
                }
                processSheetConditionFormat(sheet, mesExportExcel.getSheetConfigs().get(i).getLuckysheetConditionformatSave());
        	}
        }
        if(StringUtil.isNotEmpty(password))
        {
        	ZipSecureFile.setMinInflateRatio(-1.0d);
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
     	    wb.write(baos);
     	    baos.flush();
     	    ByteArrayInputStream workbookInput = new ByteArrayInputStream(baos.toByteArray());
     	   try (POIFSFileSystem fs = new POIFSFileSystem()) {
   	      	EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
   	          Encryptor enc = info.getEncryptor();
   	          enc.confirmPassword(password);
   	          try (OPCPackage opc = OPCPackage.open(workbookInput); OutputStream os = enc.getDataStream(fs)) {
   	              opc.save(os);
   	          } catch (Exception e) {
   	              e.printStackTrace();
   	          }
   	          httpServletResponse.reset();
   	          httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=utf-8");
   	          httpServletResponse.addHeader("Content-Disposition", "attachment;filename=" +filename + ".xlsx");
   	          httpServletResponse.addHeader("filename", filename + ".xlsx");
   	          fs.writeFilesystem(httpServletResponse.getOutputStream());
   	      }
        }else {
        	 ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        	try {
            	wb.write(outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	
	/**  
	 * @MethodName: largeExport
	 * @Description: 大量数据导出
	 * @author caiyang
	 * @param mesExportExcel
	 * @param filename
	 * @param password
	 * @param httpServletResponse
	 * @throws Exception void
	 * @date 2025-03-15 03:36:51 
	 */ 
	public static void largeExport(MesExportExcel mesExportExcel,String filename,String password,HttpServletResponse httpServletResponse) 
			throws Exception{
		httpServletResponse.setContentType("octets/stream");
    	//设置文件名编码格式
        filename = URLEncoder.encode(filename, "UTF-8");
        httpServletResponse.addHeader("Content-Disposition", "attachment;filename=" +filename + ".xlsx");
        httpServletResponse.addHeader("filename", filename + ".xlsx");
        SXSSFWorkbook wb = new SXSSFWorkbook();
        if(!ListUtil.isEmpty(mesExportExcel.getSheetConfigs())) {
        	for (int i = 0; i < mesExportExcel.getSheetConfigs().size(); i++) {
        		List<Map<String, Object>> cellDatas = mesExportExcel.getSheetConfigs().get(i).getCellDatas();
        		Map<String, Integer> maxXAndY = mesExportExcel.getSheetConfigs().get(i).getMaxXAndY();
        		Map<String, Map<String, Object>> hyperlinks = mesExportExcel.getSheetConfigs().get(i).getHyperlinks();
        		List<Object> borderInfos = mesExportExcel.getSheetConfigs().get(i).getBorderInfos();
        		Map<String, Object> rowlen = mesExportExcel.getSheetConfigs().get(i).getRowlen();
        		Map<String, Object> columnlen = mesExportExcel.getSheetConfigs().get(i).getColumnlen();
        		JSONObject frozen = mesExportExcel.getSheetConfigs().get(i).getFrozen();
        		JSONObject images = mesExportExcel.getSheetConfigs().get(i).getBase64Images();
        		List<JSONObject> imageDatas = mesExportExcel.getSheetConfigs().get(i).getImageDatas();
        		String sheetname = mesExportExcel.getSheetConfigs().get(i).getSheetname();
        		JSONObject colhidden = mesExportExcel.getSheetConfigs().get(i).getColhidden();
        		JSONObject rowhidden = mesExportExcel.getSheetConfigs().get(i).getRowhidden();
        		JSONObject dataVerification = mesExportExcel.getSheetConfigs().get(i).getDataVerification();
        		JSONObject authority = mesExportExcel.getSheetConfigs().get(i).getAuthority();
        		JSONObject filter = mesExportExcel.getSheetConfigs().get(i).getFilter();
        		JSONArray xxbtCells = new JSONArray();//斜线表头单元格
        		SXSSFSheet sheet = wb.createSheet(sheetname);
        		List<String> noViewAuthCells = mesExportExcel.getSheetConfigs().get(i).getNoViewAuthCells();
        		if(!StringUtil.isEmptyMap(filter))
        		{
        			String filterRange = getFilterRange(filter);
        			CellRangeAddress c = CellRangeAddress.valueOf(filterRange);
            		sheet.setAutoFilter(c);
        		}
        		sheet.setForceFormulaRecalculation(true);
        		sheet.setRandomAccessWindowSize(-1);
        		Map<String, String> unProtectCells = null;
        		if(authority != null && authority.getIntValue("sheet") == 1)
        		{
        			unProtectCells = getUnProtectCells(authority);
        		}
        		if(colhidden != null && !colhidden.isEmpty())
        		{
        			for(String key:colhidden.keySet())
        			{
        				int value = colhidden.getIntValue(key);
        				if(value == 0)
        				{
        					int column = Integer.parseInt(key);
        					sheet.setColumnHidden(column, true);
        				}
        				
        			}
        		}
        		if(frozen != null)
                {
                	String frozenType = frozen.getString("type");
                	int row = 0;
                	int column = 0;
                	if(StringUtil.isNotEmpty(frozenType))
                	{
        	        	switch (frozenType) {
        				case "row":
        					sheet.createFreezePane(0, 1);
        					break;
        				case "column":
        					sheet.createFreezePane(1, 0);
        					break;
        				case "both":
        					sheet.createFreezePane(1, 1);
        					break;
        				case "rangeRow":
        					row = frozen.getJSONObject("range").getIntValue("row_focus");
        					sheet.createFreezePane(0, row+1);
        					break;
        				case "rangeColumn":
        					column = frozen.getJSONObject("range").getIntValue("column_focus");
        					sheet.createFreezePane(column+1,0);
        				case "rangeBoth":
        					row = frozen.getJSONObject("range").getIntValue("row_focus");
        					column = frozen.getJSONObject("range").getIntValue("column_focus");
        					sheet.createFreezePane(column+1, row+1);
        				default:
        					break;
        				}
                	}
                }
        		LuckySheetCellUtil2 cellUtil = new LuckySheetCellUtil2(wb, sheet);
        		cellUtil.createCells(maxXAndY.get("maxX"), maxXAndY.get("maxY"),rowlen,rowhidden);
        		if(columnlen != null)
                {
                	for (Map.Entry<String, Object> entry : columnlen.entrySet()) {
                		BigDecimal wid = new BigDecimal(String.valueOf(entry.getValue()));
                		BigDecimal excleWid=new BigDecimal(32);
                		sheet.setColumnWidth(Integer.parseInt(String.valueOf(entry.getKey())), wid.multiply(excleWid).setScale(0,BigDecimal.ROUND_HALF_UP).intValue());//列宽px值
                	}
                }
        		JSONArray barCodeCells = new JSONArray();//条形码单元格
                JSONArray qrCodeCells = new JSONArray();//二维码单元格
                Map<String, Integer> wrapText = new HashMap<>();
                cellUtil.setCellValues(cellDatas,hyperlinks,borderInfos,unProtectCells,mesExportExcel.getSheetConfigs().get(i).getMerge(),mesExportExcel.getSheetConfigs().get(i).getIsCoedit(), dataVerification,xxbtCells,false,barCodeCells,qrCodeCells,wrapText,noViewAuthCells);
                cellUtil.setRowHeight(maxXAndY.get("maxX"), rowlen, rowhidden, wrapText);
                JSONObject rowlenObj = JSONObject.parseObject(JSONObject.toJSONString(rowlen));
                JSONObject columnlenObj = JSONObject.parseObject(JSONObject.toJSONString(columnlen));
                setImages(wb,sheet,images,columnlenObj,rowlenObj,rowhidden,colhidden,mesExportExcel.getImageInfos(),mesExportExcel.getBackImages());
                insertUrlImg(wb,sheet,imageDatas);
//                insertChart(wb, sheet, mesExportExcel.getSheetConfigs().get(i).getChart(),mesExportExcel.getSheetConfigs().get(i).getChartCells(),mesExportExcel.getSheetConfigs().get(i).getIsCoedit(),rowlenObj,rowhidden,columnlenObj,colhidden);
                addDataVerification(sheet, dataVerification);
                if(authority != null && authority.getIntValue("sheet") == 1)
        		{
        			sheet.protectSheet(StringUtil.isNotEmpty(authority.getString("password"))?authority.getString("password"):"");
        		}
                if(!ListUtil.isEmpty(xxbtCells))
                {
                	List<SlashLinePosition> slashes = new ArrayList<>();
                	List<SlashLineText> slashTexts = new ArrayList<>();
                	for (int j = 0; j < xxbtCells.size(); j++) {
						JSONObject cellData = xxbtCells.getJSONObject(j);
						int r = cellData.getIntValue("r");
						int c = cellData.getIntValue("c");
						int rs = 1;
						int cs = 1;
						String cellValue = "";
						if(cellData.getJSONObject("v") != null)
						{
							JSONObject v = cellData.getJSONObject("v");
							if(v.containsKey("mc"))
							{
								rs = v.getJSONObject("mc").getInteger("rs") != null?v.getJSONObject("mc").getInteger("rs"):1;
								cs = v.getJSONObject("mc").getInteger("cs") != null?v.getJSONObject("mc").getInteger("cs"):1;
							}
							cellValue = v.getString("v");
						}
						getSlashLinePositionXlsx2(sheet,r,c,rs,cs,cellValue,slashes,slashTexts);
					}
//                	drawLineXlsx(sheet,slashes,slashTexts);
                }
                if(ListUtil.isNotEmpty(barCodeCells)) {
                	for (int j = 0; j < barCodeCells.size(); j++) {
                		JSONObject cellValue = barCodeCells.getJSONObject(j).getJSONObject(LuckySheetPropsEnum.CELLCONFIG.getCode());
                		JSONObject mergeCell = cellValue.getJSONObject(LuckySheetPropsEnum.MERGECELLS.getCode());
                		if(StringUtil.isNotEmpty(cellValue.getString("v"))) {
                			byte[] barCodeByte = BarCodeUtil.generateBarcodeImage(cellValue.getString("v"), 200, 100);
                    		if(mergeCell != null) {
                    			if(mergeCell.containsKey(LuckySheetPropsEnum.ROWSPAN.getCode()) && mergeCell.containsKey(LuckySheetPropsEnum.COLSPAN.getCode())) {
                    				int firstRow = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.R.getCode())));
        							int lastRow = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.R.getCode()))) + Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.ROWSPAN.getCode()))) - 1;
        							int firstCol = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.C.getCode())));
        							int lastCol = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.C.getCode()))) + Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.COLSPAN.getCode()))) - 1;
        							ReportExcelUtil.insertBarcode(wb, sheet, barCodeByte, firstRow, lastRow, firstCol, lastCol);
                    			}
                    		}else {
                    			int r = barCodeCells.getJSONObject(j).getIntValue("r");
    							int c = barCodeCells.getJSONObject(j).getIntValue("c");
    							ReportExcelUtil.insertBarcode(wb, sheet, barCodeByte, r, r, c, c);
                    		}
                		}
					}
                }
                if(ListUtil.isNotEmpty(qrCodeCells)) {
                	for (int j = 0; j < qrCodeCells.size(); j++) {
                		JSONObject cellValue = qrCodeCells.getJSONObject(j).getJSONObject(LuckySheetPropsEnum.CELLCONFIG.getCode());
                		JSONObject mergeCell = cellValue.getJSONObject(LuckySheetPropsEnum.MERGECELLS.getCode());
                		if(StringUtil.isNotEmpty(cellValue.getString("v"))) {
                			byte[] barCodeByte = QRCodeUtil.generateQRCodeImage(cellValue.getString("v"), 256, 256);
                    		if(mergeCell != null) {
                    			if(mergeCell.containsKey(LuckySheetPropsEnum.ROWSPAN.getCode()) && mergeCell.containsKey(LuckySheetPropsEnum.COLSPAN.getCode())) {
                    				int firstRow = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.R.getCode())));
        							int lastRow = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.R.getCode()))) + Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.ROWSPAN.getCode()))) - 1;
        							int firstCol = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.C.getCode())));
        							int lastCol = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.C.getCode()))) + Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.COLSPAN.getCode()))) - 1;
        							ReportExcelUtil.insertBarcode(wb, sheet, barCodeByte, firstRow, lastRow, firstCol, lastCol);
                    			}
                    		}else {
                    			int r = qrCodeCells.getJSONObject(j).getIntValue("r");
    							int c = qrCodeCells.getJSONObject(j).getIntValue("c");
    							ReportExcelUtil.insertBarcode(wb, sheet, barCodeByte, r, r, c, c);
                    		}
                		}
					}
                }
                processSheetConditionFormat(sheet, mesExportExcel.getSheetConfigs().get(i).getLuckysheetConditionformatSave());
        	}
        }
        if(StringUtil.isNotEmpty(password))
        {
        	ZipSecureFile.setMinInflateRatio(-1.0d);
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
     	    wb.write(baos);
     	    baos.flush();
     	    ByteArrayInputStream workbookInput = new ByteArrayInputStream(baos.toByteArray());
     	   try (POIFSFileSystem fs = new POIFSFileSystem()) {
   	      	EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
   	          Encryptor enc = info.getEncryptor();
   	          enc.confirmPassword(password);
   	          try (OPCPackage opc = OPCPackage.open(workbookInput); OutputStream os = enc.getDataStream(fs)) {
   	              opc.save(os);
   	          } catch (Exception e) {
   	              e.printStackTrace();
   	          }
   	          httpServletResponse.reset();
   	          httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=utf-8");
   	          httpServletResponse.addHeader("Content-Disposition", "attachment;filename=" +filename + ".xlsx");
   	          httpServletResponse.addHeader("filename", filename + ".xlsx");
   	          fs.writeFilesystem(httpServletResponse.getOutputStream());
   	      }
        }else {
        	 ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        	try {
            	wb.write(outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	
	/**  
	 * @MethodName: processSheetConditionFormat
	 * @Description: 条件格式处理
	 * @author caiyang
	 * @param sheet
	 * @param conditionFormats void
	 * @throws Exception 
	 * @date 2024-08-19 01:16:54 
	 */ 
	private static void processSheetConditionFormat(XSSFSheet sheet,JSONArray conditionFormats) throws Exception {
		if(ListUtil.isNotEmpty(conditionFormats)) {
			for (int i = 0; i < conditionFormats.size(); i++) {
				String type = conditionFormats.getJSONObject(i).getString("type");
				if("default".equals(type)) {
					String conditionName = conditionFormats.getJSONObject(i).getString("conditionName");
					JSONArray conditionValue = conditionFormats.getJSONObject(i).getJSONArray("conditionValue");
					JSONObject format = conditionFormats.getJSONObject(i).getJSONObject("format");
					for (int j = 0; j < conditionFormats.getJSONObject(i).getJSONArray("cellrange").size(); j++) {
						JSONArray row = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("row");
						JSONArray column = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("column");
						ConditionalFormattingRule rule = null;
						if("lessThan".equals(conditionName)) {
							rule = sheet.getSheetConditionalFormatting().createConditionalFormattingRule(ComparisonOperator.LT, conditionValue.getString(0));
						}else if("greaterThan".equals(conditionName)) {
							rule = sheet.getSheetConditionalFormatting().createConditionalFormattingRule(ComparisonOperator.GT, conditionValue.getString(0));
						}else if("equal".equals(conditionName)) {
							rule = sheet.getSheetConditionalFormatting().createConditionalFormattingRule(ComparisonOperator.EQUAL, conditionValue.getString(0));
						}else if("betweenness".equals(conditionName)) {
							rule = sheet.getSheetConditionalFormatting().createConditionalFormattingRule(ComparisonOperator.BETWEEN, conditionValue.getString(0),conditionValue.getString(1));
						}else if("textContains".equals(conditionName)) {
							//字符串包含
							String col = SheetUtil.excelColIndexToStr(column.getIntValue(0)+1);
							String ruleStr = "NOT(ISERROR(SEARCH(\"" + conditionValue.getString(0) + "\","+(col+(row.getIntValue(0)+1))+")))";
							rule = sheet.getSheetConditionalFormatting().createConditionalFormattingRule(ruleStr);
						}else if("occurrenceDate".equals(conditionName)) {
							//发生日期，暂不支持 TODO
						}else if("duplicateValue".equals(conditionName)) {
							//重复值
							rule = createConditionalFormattingRuleDuplicate(sheet.getSheetConditionalFormatting());
						}else if("top10".equals(conditionName) || "top10%".equals(conditionName) || "last10%".equals(conditionName)
							|| "last10".equals(conditionName) || "AboveAverage".equals(conditionName) || "SubAverage".equals(conditionName)) {
							rule = createConditionalFormattingRuleRank(sheet.getSheetConditionalFormatting(),conditionName,conditionValue);
						}
						if(rule != null) {
							String cellColor = format.getString("cellColor");
							int[] rgb = null;
							if(cellColor.contains("rgb")) {
								rgb = StringUtil.rgbStringToRgb(cellColor);
							}else {
								rgb = StringUtil.hexToRgb(cellColor);
							}
							PatternFormatting patternFormatting = rule.createPatternFormatting();
							patternFormatting.setFillBackgroundColor(new XSSFColor(new Color(rgb[0], rgb[1], rgb[2]),new DefaultIndexedColorMap()));
							FontFormatting fontFormatting = rule.createFontFormatting();
							String textColor = format.getString("textColor");
							if(textColor.contains("rgb")) {
								rgb = StringUtil.rgbStringToRgb(textColor);
							}else {
								rgb = StringUtil.hexToRgb(textColor);
							}
							fontFormatting.setFontColor(new XSSFColor(new Color(rgb[0], rgb[1], rgb[2]),new DefaultIndexedColorMap()));
							CellRangeAddress[] regions = {
								new CellRangeAddress(row.getIntValue(0), row.getIntValue(1), column.getIntValue(0), column.getIntValue(1))
							};
							sheet.getSheetConditionalFormatting().addConditionalFormatting(regions,rule);
						}
					}
					
				}else if("dataBar".equals(type)) {
					//数据条
					JSONArray format = conditionFormats.getJSONObject(i).getJSONArray("format");
					int[] rgb = null;
					if(format.getString(0).contains("rgb")) {
						rgb = StringUtil.rgbStringToRgb(format.getString(0));
					}else {
						rgb = StringUtil.hexToRgb(format.getString(0));
					}
					ConditionalFormattingRule rule = sheet.getSheetConditionalFormatting().createConditionalFormattingRule(new XSSFColor(new Color(rgb[0], rgb[1], rgb[2]),new DefaultIndexedColorMap()));
					XSSFDataBarFormatting dataBarFormatting = (XSSFDataBarFormatting) rule.getDataBarFormatting();
					CellRangeAddress[] regions = new CellRangeAddress[conditionFormats.getJSONObject(i).getJSONArray("cellrange").size()];
					for (int j = 0; j < conditionFormats.getJSONObject(i).getJSONArray("cellrange").size(); j++) {
						JSONArray row = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("row");
						JSONArray column = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("column");
						regions[j] = new CellRangeAddress(row.getIntValue(0), row.getIntValue(1), column.getIntValue(0), column.getIntValue(1));
					}
					sheet.getSheetConditionalFormatting().addConditionalFormatting(regions,rule);
					
				}else if("colorGradation".equals(type)) {
					//色阶
					JSONArray format = conditionFormats.getJSONObject(i).getJSONArray("format");
					ConditionalFormattingRule rule = sheet.getSheetConditionalFormatting().createConditionalFormattingColorScaleRule();
					ColorScaleFormatting colorScaleFormatting = rule.getColorScaleFormatting();
					colorScaleFormatting.setNumControlPoints(format.size());//设置几色阶
					if(format.size() ==3) {//三色阶
						//设置最小色阶
						colorScaleFormatting.getThresholds()[0].setRangeType(org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType.MIN);
						// 设置两个颜色递进的形式 此处为百分比
						colorScaleFormatting.getThresholds()[1].setRangeType(org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType.PERCENTILE);
						colorScaleFormatting.getThresholds()[1].setValue(50d);
						// 设置最大色阶
						colorScaleFormatting.getThresholds()[2].setRangeType(org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType.MAX);
						String color = format.getString(2);
						if(color.contains("rgb")) {
							int[] rgb = StringUtil.rgbStringToRgb(color);
							color = StringUtil.rgb2Hex(rgb[0], rgb[1], rgb[2]);
						}
						((ExtendedColor)colorScaleFormatting.getColors()[0]).setARGBHex(color.replaceAll("#", ""));
						color = format.getString(1);
						if(color.contains("rgb")) {
							int[] rgb = StringUtil.rgbStringToRgb(color);
							color = StringUtil.rgb2Hex(rgb[0], rgb[1], rgb[2]);
						}
						((ExtendedColor)colorScaleFormatting.getColors()[1]).setARGBHex(color.replaceAll("#", ""));
						color = format.getString(0);
						if(color.contains("rgb")) {
							int[] rgb = StringUtil.rgbStringToRgb(color);
							color = StringUtil.rgb2Hex(rgb[0], rgb[1], rgb[2]);
						}
						((ExtendedColor)colorScaleFormatting.getColors()[2]).setARGBHex(color.replaceAll("#", ""));
					}else if(format.size() ==2) {
						//设置最小色阶
						colorScaleFormatting.getThresholds()[0].setRangeType(org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType.MIN);
						// 设置最大色阶
						colorScaleFormatting.getThresholds()[1].setRangeType(org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType.MAX);
						String color = format.getString(1);
						if(color.contains("rgb")) {
							int[] rgb = StringUtil.rgbStringToRgb(color);
							color = StringUtil.rgb2Hex(rgb[0], rgb[1], rgb[2]);
						}
						((ExtendedColor)colorScaleFormatting.getColors()[0]).setARGBHex(color.replaceAll("#", ""));
						color = format.getString(0);
						if(color.contains("rgb")) {
							int[] rgb = StringUtil.rgbStringToRgb(color);
							color = StringUtil.rgb2Hex(rgb[0], rgb[1], rgb[2]);
						}
						((ExtendedColor)colorScaleFormatting.getColors()[1]).setARGBHex(color.replaceAll("#", ""));
					}
					CellRangeAddress[] regions = new CellRangeAddress[conditionFormats.getJSONObject(i).getJSONArray("cellrange").size()];
					for (int j = 0; j < conditionFormats.getJSONObject(i).getJSONArray("cellrange").size(); j++) {
						JSONArray row = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("row");
						JSONArray column = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("column");
						regions[j] = new CellRangeAddress(row.getIntValue(0), row.getIntValue(1), column.getIntValue(0), column.getIntValue(1));
					}
					
					sheet.getSheetConditionalFormatting().addConditionalFormatting(regions,rule);
				}else if("icons".equals(type)) {
					//图标集
					JSONObject format = conditionFormats.getJSONObject(i).getJSONObject("format");
					ConditionalFormattingRule rule = createConditionalFormattingRuleIconSets(sheet.getSheetConditionalFormatting(),format);
					CellRangeAddress[] regions = new CellRangeAddress[conditionFormats.getJSONObject(i).getJSONArray("cellrange").size()];
					for (int j = 0; j < conditionFormats.getJSONObject(i).getJSONArray("cellrange").size(); j++) {
						JSONArray row = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("row");
						JSONArray column = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("column");
						regions[j] = new CellRangeAddress(row.getIntValue(0), row.getIntValue(1), column.getIntValue(0), column.getIntValue(1));
					}
					sheet.getSheetConditionalFormatting().addConditionalFormatting(regions,rule);
				}
			}
		}
	}
	
	private static void processSheetConditionFormat(SXSSFSheet sheet,JSONArray conditionFormats) throws Exception {
		if(ListUtil.isNotEmpty(conditionFormats)) {
			for (int i = 0; i < conditionFormats.size(); i++) {
				String type = conditionFormats.getJSONObject(i).getString("type");
				if("default".equals(type)) {
					String conditionName = conditionFormats.getJSONObject(i).getString("conditionName");
					JSONArray conditionValue = conditionFormats.getJSONObject(i).getJSONArray("conditionValue");
					JSONObject format = conditionFormats.getJSONObject(i).getJSONObject("format");
					for (int j = 0; j < conditionFormats.getJSONObject(i).getJSONArray("cellrange").size(); j++) {
						JSONArray row = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("row");
						JSONArray column = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("column");
						ConditionalFormattingRule rule = null;
						if("lessThan".equals(conditionName)) {
							rule = sheet.getSheetConditionalFormatting().createConditionalFormattingRule(ComparisonOperator.LT, conditionValue.getString(0));
						}else if("greaterThan".equals(conditionName)) {
							rule = sheet.getSheetConditionalFormatting().createConditionalFormattingRule(ComparisonOperator.GT, conditionValue.getString(0));
						}else if("equal".equals(conditionName)) {
							rule = sheet.getSheetConditionalFormatting().createConditionalFormattingRule(ComparisonOperator.EQUAL, conditionValue.getString(0));
						}else if("betweenness".equals(conditionName)) {
							rule = sheet.getSheetConditionalFormatting().createConditionalFormattingRule(ComparisonOperator.BETWEEN, conditionValue.getString(0),conditionValue.getString(1));
						}else if("textContains".equals(conditionName)) {
							//字符串包含
							String col = SheetUtil.excelColIndexToStr(column.getIntValue(0)+1);
							String ruleStr = "NOT(ISERROR(SEARCH(\"" + conditionValue.getString(0) + "\","+(col+(row.getIntValue(0)+1))+")))";
							rule = sheet.getSheetConditionalFormatting().createConditionalFormattingRule(ruleStr);
						}else if("occurrenceDate".equals(conditionName)) {
							//发生日期，暂不支持 TODO
						}else if("duplicateValue".equals(conditionName)) {
							//重复值
							rule = createConditionalFormattingRuleDuplicate(sheet.getSheetConditionalFormatting());
						}else if("top10".equals(conditionName) || "top10%".equals(conditionName) || "last10%".equals(conditionName)
							|| "last10".equals(conditionName) || "AboveAverage".equals(conditionName) || "SubAverage".equals(conditionName)) {
							rule = createConditionalFormattingRuleRank(sheet.getSheetConditionalFormatting(),conditionName,conditionValue);
						}
						if(rule != null) {
							String cellColor = format.getString("cellColor");
							int[] rgb = null;
							if(cellColor.contains("rgb")) {
								rgb = StringUtil.rgbStringToRgb(cellColor);
							}else {
								rgb = StringUtil.hexToRgb(cellColor);
							}
							PatternFormatting patternFormatting = rule.createPatternFormatting();
							patternFormatting.setFillBackgroundColor(new XSSFColor(new Color(rgb[0], rgb[1], rgb[2]),new DefaultIndexedColorMap()));
							FontFormatting fontFormatting = rule.createFontFormatting();
							String textColor = format.getString("textColor");
							if(textColor.contains("rgb")) {
								rgb = StringUtil.rgbStringToRgb(textColor);
							}else {
								rgb = StringUtil.hexToRgb(textColor);
							}
							fontFormatting.setFontColor(new XSSFColor(new Color(rgb[0], rgb[1], rgb[2]),new DefaultIndexedColorMap()));
							CellRangeAddress[] regions = {
								new CellRangeAddress(row.getIntValue(0), row.getIntValue(1), column.getIntValue(0), column.getIntValue(1))
							};
							sheet.getSheetConditionalFormatting().addConditionalFormatting(regions,rule);
						}
					}
					
				}else if("dataBar".equals(type)) {
					//数据条
					JSONArray format = conditionFormats.getJSONObject(i).getJSONArray("format");
					int[] rgb = null;
					if(format.getString(0).contains("rgb")) {
						rgb = StringUtil.rgbStringToRgb(format.getString(0));
					}else {
						rgb = StringUtil.hexToRgb(format.getString(0));
					}
					ConditionalFormattingRule rule = sheet.getSheetConditionalFormatting().createConditionalFormattingRule(new XSSFColor(new Color(rgb[0], rgb[1], rgb[2]),new DefaultIndexedColorMap()));
					XSSFDataBarFormatting dataBarFormatting = (XSSFDataBarFormatting) rule.getDataBarFormatting();
					CellRangeAddress[] regions = new CellRangeAddress[conditionFormats.getJSONObject(i).getJSONArray("cellrange").size()];
					for (int j = 0; j < conditionFormats.getJSONObject(i).getJSONArray("cellrange").size(); j++) {
						JSONArray row = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("row");
						JSONArray column = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("column");
						regions[j] = new CellRangeAddress(row.getIntValue(0), row.getIntValue(1), column.getIntValue(0), column.getIntValue(1));
					}
					sheet.getSheetConditionalFormatting().addConditionalFormatting(regions,rule);
					
				}else if("colorGradation".equals(type)) {
					//色阶
					JSONArray format = conditionFormats.getJSONObject(i).getJSONArray("format");
					ConditionalFormattingRule rule = sheet.getSheetConditionalFormatting().createConditionalFormattingColorScaleRule();
					ColorScaleFormatting colorScaleFormatting = rule.getColorScaleFormatting();
					colorScaleFormatting.setNumControlPoints(format.size());//设置几色阶
					if(format.size() ==3) {//三色阶
						//设置最小色阶
						colorScaleFormatting.getThresholds()[0].setRangeType(org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType.MIN);
						// 设置两个颜色递进的形式 此处为百分比
						colorScaleFormatting.getThresholds()[1].setRangeType(org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType.PERCENTILE);
						colorScaleFormatting.getThresholds()[1].setValue(50d);
						// 设置最大色阶
						colorScaleFormatting.getThresholds()[2].setRangeType(org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType.MAX);
						String color = format.getString(2);
						if(color.contains("rgb")) {
							int[] rgb = StringUtil.rgbStringToRgb(color);
							color = StringUtil.rgb2Hex(rgb[0], rgb[1], rgb[2]);
						}
						((ExtendedColor)colorScaleFormatting.getColors()[0]).setARGBHex(color.replaceAll("#", ""));
						color = format.getString(1);
						if(color.contains("rgb")) {
							int[] rgb = StringUtil.rgbStringToRgb(color);
							color = StringUtil.rgb2Hex(rgb[0], rgb[1], rgb[2]);
						}
						((ExtendedColor)colorScaleFormatting.getColors()[1]).setARGBHex(color.replaceAll("#", ""));
						color = format.getString(0);
						if(color.contains("rgb")) {
							int[] rgb = StringUtil.rgbStringToRgb(color);
							color = StringUtil.rgb2Hex(rgb[0], rgb[1], rgb[2]);
						}
						((ExtendedColor)colorScaleFormatting.getColors()[2]).setARGBHex(color.replaceAll("#", ""));
					}else if(format.size() ==2) {
						//设置最小色阶
						colorScaleFormatting.getThresholds()[0].setRangeType(org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType.MIN);
						// 设置最大色阶
						colorScaleFormatting.getThresholds()[1].setRangeType(org.apache.poi.ss.usermodel.ConditionalFormattingThreshold.RangeType.MAX);
						String color = format.getString(1);
						if(color.contains("rgb")) {
							int[] rgb = StringUtil.rgbStringToRgb(color);
							color = StringUtil.rgb2Hex(rgb[0], rgb[1], rgb[2]);
						}
						((ExtendedColor)colorScaleFormatting.getColors()[0]).setARGBHex(color.replaceAll("#", ""));
						color = format.getString(0);
						if(color.contains("rgb")) {
							int[] rgb = StringUtil.rgbStringToRgb(color);
							color = StringUtil.rgb2Hex(rgb[0], rgb[1], rgb[2]);
						}
						((ExtendedColor)colorScaleFormatting.getColors()[1]).setARGBHex(color.replaceAll("#", ""));
					}
					CellRangeAddress[] regions = new CellRangeAddress[conditionFormats.getJSONObject(i).getJSONArray("cellrange").size()];
					for (int j = 0; j < conditionFormats.getJSONObject(i).getJSONArray("cellrange").size(); j++) {
						JSONArray row = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("row");
						JSONArray column = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("column");
						regions[j] = new CellRangeAddress(row.getIntValue(0), row.getIntValue(1), column.getIntValue(0), column.getIntValue(1));
					}
					
					sheet.getSheetConditionalFormatting().addConditionalFormatting(regions,rule);
				}else if("icons".equals(type)) {
					//图标集
					JSONObject format = conditionFormats.getJSONObject(i).getJSONObject("format");
					ConditionalFormattingRule rule = createConditionalFormattingRuleIconSets(sheet.getSheetConditionalFormatting(),format);
					CellRangeAddress[] regions = new CellRangeAddress[conditionFormats.getJSONObject(i).getJSONArray("cellrange").size()];
					for (int j = 0; j < conditionFormats.getJSONObject(i).getJSONArray("cellrange").size(); j++) {
						JSONArray row = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("row");
						JSONArray column = conditionFormats.getJSONObject(i).getJSONArray("cellrange").getJSONObject(j).getJSONArray("column");
						regions[j] = new CellRangeAddress(row.getIntValue(0), row.getIntValue(1), column.getIntValue(0), column.getIntValue(1));
					}
					sheet.getSheetConditionalFormatting().addConditionalFormatting(regions,rule);
				}
			}
		}
	}
	
	private static void getSlashLinePositionXlsx(XSSFSheet xssfSheet,int rowIndex,int colIndex,int rs,int cs,String v,List<SlashLinePosition> slashes
			,List<SlashLineText> slashTexts){
		int height = 0;
		int width = 0;
		Map<Integer, Integer> rowHeights = new LinkedHashMap<Integer, Integer>();
		Map<Integer, Integer> colWidths = new LinkedHashMap<Integer, Integer>();
		for (int i = 0; i < rs; i++) {
			int rowHeight = xssfSheet.getRow(rowIndex+i).getHeight()*700;
			rowHeights.put(rowIndex+i, rowHeight);
			height = height + rowHeight;
		}
		for (int i = 0; i < cs; i++) {
			int colWidth = xssfSheet.getColumnWidth(colIndex+i)*300;
			colWidths.put(colIndex+i, colWidth);
			width = width + colWidth;
		}
		String[] splits = v.split("\\|");
		int lineCount = v.split("\\|").length - 1;
		if(lineCount == 1)
		{
			SlashLinePosition slp1 = new SlashLinePosition(0, 0, width, height,rowIndex,colIndex,rowIndex+rs-1,colIndex+cs-1);
			slashes.add(slp1);
			int startx = 0;
			int starty = 0;
			if(rs > 1)
			{
				starty = 0;
			}else {
				starty = height/2;
			}
			SlashLineText slashLineText1 = new SlashLineText(startx, starty, colWidths.get(colIndex), rowHeights.get(rowIndex+rs-1), splits[0], rowIndex+rs-1, colIndex, rs, cs);
			slashTexts.add(slashLineText1);
			startx = 0;
			starty = 0;
			if(cs > 1)
			{
				startx = 0;
			}else {
				startx = width/2;
			}
			SlashLineText slashLineText2 = new SlashLineText(startx, starty, colWidths.get(colIndex+cs-1), rowHeights.get(rowIndex), splits[1], rowIndex, colIndex+cs-1, rs, cs);
			slashTexts.add(slashLineText2);
		}else if(lineCount == 2)
		{
			int rsMid = (int)Math.ceil(rs/2.0);
			int csMid = (int)Math.floor(cs/2.0);
			SlashLinePosition slp1 = new SlashLinePosition(0, 0, cs>1?getWidth(colWidths, colIndex, csMid):width/2, height,rowIndex,colIndex,rowIndex+rs-1,cs>1?colIndex+csMid-1:colIndex);
			slashes.add(slp1);
			int startx = 0;
			int starty = 0;
			if(rs > 1)
			{
				starty = 0;
			}else {
				starty = height/2;
			}
			SlashLineText slashLineText1 = new SlashLineText(0, starty, getWidth(colWidths, colIndex, csMid), rowHeights.get(rowIndex+rs-1), splits[0], rowIndex+rs-1, colIndex, rs, cs);
			slashTexts.add(slashLineText1);
			SlashLinePosition slp2 = new SlashLinePosition(0, 0, width, rs>1?getHeight(rowHeights, rowIndex, rsMid):height/2,rowIndex,colIndex,rs>1?rowIndex+rsMid-1:rowIndex,colIndex+cs-1);
			slashes.add(slp2);
			if(cs > 1)
			{
				startx = 0;
			}else {
				startx = width/2;
			}
			if(rs > 1)
			{
				starty = 0;
			}else {
				starty = height/2;
			}
			SlashLineText slashLineText2 = new SlashLineText(startx, starty, colWidths.get(colIndex+cs-1), rowHeights.get(rowIndex+rs-1), splits[1], rowIndex+rs-1,colIndex+cs-1, rs, cs);
			slashTexts.add(slashLineText2);
			startx = 0;
			starty = 0;
			if(cs > 1)
			{
				startx = 0;
			}else {
				startx = width/2;
			}
			SlashLineText slashLineText3 = new SlashLineText(startx, 0, colWidths.get(colIndex+cs-1), rowHeights.get(rowIndex), splits[2], rowIndex, colIndex+cs-1, rs, cs);
			slashTexts.add(slashLineText3);
		}else if(lineCount == 3) {
			int rsMid = (int)Math.floor(rs/2.0);
			int csMid = (int)Math.floor(cs/2.0);
			SlashLinePosition slp1 = new SlashLinePosition(0, 0, cs>1?getWidth(colWidths, colIndex, csMid):width/2, height,rowIndex,colIndex,rowIndex+rs-1,cs>1?colIndex+csMid-1:colIndex);
			slashes.add(slp1);
			int startx = 0;
			int starty = 0;
			if(rs > 1)
			{
				starty = 0;
			}else {
				starty = height/2;
			}
			SlashLineText slashLineText1 = new SlashLineText(0, starty, getWidth(colWidths, colIndex, csMid), rowHeights.get(rowIndex+rs-1), splits[0], rowIndex+rs-1, colIndex, rs, cs);
			slashTexts.add(slashLineText1);
			SlashLinePosition slp2 = new SlashLinePosition(0, 0, width, height,rowIndex,colIndex,rowIndex+rs-1,colIndex+cs-1);
			slashes.add(slp2);
			startx = 0;
			starty = 0;
			if(cs > 1)
			{
				startx = 0;
			}else {
				startx = colWidths.get(colIndex)/2;
			}
			if(rs > 1)
			{
				starty = rowHeights.get(rowIndex+rs-1)*1/3;
			}else {
				starty = rowHeights.get(rowIndex)*3/5;
			}
			SlashLineText slashLineText2 = new SlashLineText(startx, starty, colWidths.get(colIndex+cs-1), rowHeights.get(rowIndex+rs-1), splits[1], rowIndex+rs-1, colIndex+cs-1, rs, cs);
			slashTexts.add(slashLineText2);
			SlashLinePosition slp3 = new SlashLinePosition(0, 0, width, rs>1?getHeight(rowHeights, rowIndex, rsMid):height/2,rowIndex,colIndex,rs>1?rowIndex+rsMid-1:rowIndex,colIndex+cs-1);
			slashes.add(slp3);
			startx = 0;
			starty = 0;
			if(cs > 1)
			{
				startx = colWidths.get(colIndex+cs-1)*1/3;
			}else {
				startx = colWidths.get(colIndex)*3/5;
			}
			if(rs > 1)
			{
				starty = 0;
			}else {
				starty = rowHeights.get(rowIndex)*2/5;
			}
			SlashLineText slashLineText3 = new SlashLineText(startx, starty, colWidths.get(colIndex+cs-1), rowHeights.get(rowIndex+rs-1), splits[2], rowIndex+rs-1, colIndex+cs-1, rs, cs);
			slashTexts.add(slashLineText3);
			startx = 0;
			starty = 0;
			if(cs > 1)
			{
				startx = 0;
			}else {
				startx = width/2;
			}
			SlashLineText slashLineText4 = new SlashLineText(startx, 0, colWidths.get(colIndex+cs-1), rowHeights.get(rowIndex), splits[3], rowIndex, colIndex+cs-1, rs, cs);
			slashTexts.add(slashLineText4);
		}
	}
	
	private static void getSlashLinePositionXlsx2(SXSSFSheet xssfSheet,int rowIndex,int colIndex,int rs,int cs,String v,List<SlashLinePosition> slashes
			,List<SlashLineText> slashTexts){
		int height = 0;
		int width = 0;
		Map<Integer, Integer> rowHeights = new LinkedHashMap<Integer, Integer>();
		Map<Integer, Integer> colWidths = new LinkedHashMap<Integer, Integer>();
		for (int i = 0; i < rs; i++) {
			int rowHeight = xssfSheet.getRow(rowIndex+i).getHeight()*700;
			rowHeights.put(rowIndex+i, rowHeight);
			height = height + rowHeight;
		}
		for (int i = 0; i < cs; i++) {
			int colWidth = xssfSheet.getColumnWidth(colIndex+i)*300;
			colWidths.put(colIndex+i, colWidth);
			width = width + colWidth;
		}
		String[] splits = v.split("\\|");
		int lineCount = v.split("\\|").length - 1;
		if(lineCount == 1)
		{
			SlashLinePosition slp1 = new SlashLinePosition(0, 0, width, height,rowIndex,colIndex,rowIndex+rs-1,colIndex+cs-1);
			slashes.add(slp1);
			int startx = 0;
			int starty = 0;
			if(rs > 1)
			{
				starty = 0;
			}else {
				starty = height/2;
			}
			SlashLineText slashLineText1 = new SlashLineText(startx, starty, colWidths.get(colIndex), rowHeights.get(rowIndex+rs-1), splits[0], rowIndex+rs-1, colIndex, rs, cs);
			slashTexts.add(slashLineText1);
			startx = 0;
			starty = 0;
			if(cs > 1)
			{
				startx = 0;
			}else {
				startx = width/2;
			}
			SlashLineText slashLineText2 = new SlashLineText(startx, starty, colWidths.get(colIndex+cs-1), rowHeights.get(rowIndex), splits[1], rowIndex, colIndex+cs-1, rs, cs);
			slashTexts.add(slashLineText2);
		}else if(lineCount == 2)
		{
			int rsMid = (int)Math.ceil(rs/2.0);
			int csMid = (int)Math.floor(cs/2.0);
			SlashLinePosition slp1 = new SlashLinePosition(0, 0, cs>1?getWidth(colWidths, colIndex, csMid):width/2, height,rowIndex,colIndex,rowIndex+rs-1,cs>1?colIndex+csMid-1:colIndex);
			slashes.add(slp1);
			int startx = 0;
			int starty = 0;
			if(rs > 1)
			{
				starty = 0;
			}else {
				starty = height/2;
			}
			SlashLineText slashLineText1 = new SlashLineText(0, starty, getWidth(colWidths, colIndex, csMid), rowHeights.get(rowIndex+rs-1), splits[0], rowIndex+rs-1, colIndex, rs, cs);
			slashTexts.add(slashLineText1);
			SlashLinePosition slp2 = new SlashLinePosition(0, 0, width, rs>1?getHeight(rowHeights, rowIndex, rsMid):height/2,rowIndex,colIndex,rs>1?rowIndex+rsMid-1:rowIndex,colIndex+cs-1);
			slashes.add(slp2);
			if(cs > 1)
			{
				startx = 0;
			}else {
				startx = width/2;
			}
			if(rs > 1)
			{
				starty = 0;
			}else {
				starty = height/2;
			}
			SlashLineText slashLineText2 = new SlashLineText(startx, starty, colWidths.get(colIndex+cs-1), rowHeights.get(rowIndex+rs-1), splits[1], rowIndex+rs-1,colIndex+cs-1, rs, cs);
			slashTexts.add(slashLineText2);
			startx = 0;
			starty = 0;
			if(cs > 1)
			{
				startx = 0;
			}else {
				startx = width/2;
			}
			SlashLineText slashLineText3 = new SlashLineText(startx, 0, colWidths.get(colIndex+cs-1), rowHeights.get(rowIndex), splits[2], rowIndex, colIndex+cs-1, rs, cs);
			slashTexts.add(slashLineText3);
		}else if(lineCount == 3) {
			int rsMid = (int)Math.floor(rs/2.0);
			int csMid = (int)Math.floor(cs/2.0);
			SlashLinePosition slp1 = new SlashLinePosition(0, 0, cs>1?getWidth(colWidths, colIndex, csMid):width/2, height,rowIndex,colIndex,rowIndex+rs-1,cs>1?colIndex+csMid-1:colIndex);
			slashes.add(slp1);
			int startx = 0;
			int starty = 0;
			if(rs > 1)
			{
				starty = 0;
			}else {
				starty = height/2;
			}
			SlashLineText slashLineText1 = new SlashLineText(0, starty, getWidth(colWidths, colIndex, csMid), rowHeights.get(rowIndex+rs-1), splits[0], rowIndex+rs-1, colIndex, rs, cs);
			slashTexts.add(slashLineText1);
			SlashLinePosition slp2 = new SlashLinePosition(0, 0, width, height,rowIndex,colIndex,rowIndex+rs-1,colIndex+cs-1);
			slashes.add(slp2);
			startx = 0;
			starty = 0;
			if(cs > 1)
			{
				startx = 0;
			}else {
				startx = colWidths.get(colIndex)/2;
			}
			if(rs > 1)
			{
				starty = rowHeights.get(rowIndex+rs-1)*1/3;
			}else {
				starty = rowHeights.get(rowIndex)*3/5;
			}
			SlashLineText slashLineText2 = new SlashLineText(startx, starty, colWidths.get(colIndex+cs-1), rowHeights.get(rowIndex+rs-1), splits[1], rowIndex+rs-1, colIndex+cs-1, rs, cs);
			slashTexts.add(slashLineText2);
			SlashLinePosition slp3 = new SlashLinePosition(0, 0, width, rs>1?getHeight(rowHeights, rowIndex, rsMid):height/2,rowIndex,colIndex,rs>1?rowIndex+rsMid-1:rowIndex,colIndex+cs-1);
			slashes.add(slp3);
			startx = 0;
			starty = 0;
			if(cs > 1)
			{
				startx = colWidths.get(colIndex+cs-1)*1/3;
			}else {
				startx = colWidths.get(colIndex)*3/5;
			}
			if(rs > 1)
			{
				starty = 0;
			}else {
				starty = rowHeights.get(rowIndex)*2/5;
			}
			SlashLineText slashLineText3 = new SlashLineText(startx, starty, colWidths.get(colIndex+cs-1), rowHeights.get(rowIndex+rs-1), splits[2], rowIndex+rs-1, colIndex+cs-1, rs, cs);
			slashTexts.add(slashLineText3);
			startx = 0;
			starty = 0;
			if(cs > 1)
			{
				startx = 0;
			}else {
				startx = width/2;
			}
			SlashLineText slashLineText4 = new SlashLineText(startx, 0, colWidths.get(colIndex+cs-1), rowHeights.get(rowIndex), splits[3], rowIndex, colIndex+cs-1, rs, cs);
			slashTexts.add(slashLineText4);
		}
	}
	
	private static int getHeight(Map<Integer, Integer> rowHeights,int r,int rs) {
		int height = rowHeights.get(r);
		if(rs > 0) {
			for (int i = 1; i < rs; i++) {
				height = height + rowHeights.get(r+i);
			}
		}
		return height;
	}
	
	private static int getWidth(Map<Integer, Integer> colWidths,int c,int cs) {
		int width = colWidths.get(c);
		if(cs > 0) {
			for (int i = 1; i < cs; i++) {
				width = width + colWidths.get(c+i);
			}
		}
		return width;
	}
	
	private static void drawLineXlsx(XSSFSheet xssfSheet,List<SlashLinePosition> slashes,
			List<SlashLineText> slashLineTexts) {
		XSSFDrawing xssfDrawing  = xssfSheet.createDrawingPatriarch();
		for(SlashLinePosition slp:slashes)
		{
			XSSFClientAnchor xssfClientAnchor = new XSSFClientAnchor(slp.getStartX(), slp.getStartY(), slp.getEndX(), slp.getEndY(), slp.getC(), slp.getR(), slp.getEndc(), slp.getEndr());
			XSSFSimpleShape shape = xssfDrawing.createSimpleShape(xssfClientAnchor);
			shape.setShapeType(ShapeTypes.LINE);
			shape.setLineWidth(0.5);
			shape.setLineStyle(0);
			shape.setLineStyleColor(0, 0, 0);
		}
		for(SlashLineText slt:slashLineTexts) {
			XSSFClientAnchor createAnchor = xssfDrawing.createAnchor(slt.getStartX(), slt.getStartY(), slt.getEndX(), slt.getEndY(), slt.getC(), slt.getR(), slt.getC(), slt.getR());
			XSSFTextBox tb1 = xssfDrawing.createTextbox(createAnchor);
			XSSFRichTextString richTextString = new XSSFRichTextString(slt.getContent());
			tb1.setText(richTextString);   
		}
	}
	
	/**  
	 * @MethodName: getExcelStream
	 * @Description: 获取excel文件流
	 * @author caiyang
	 * @param mesExportExcel
	 * @param filename
	 * @param password
	 * @param httpServletResponse 
	 * @return void
	 * @date 2022-10-21 05:23:20 
	 */  
	public static ByteArrayInputStream getExcelStream(MesExportExcel mesExportExcel,String filename,String password,int type) throws Exception
	{
        XSSFWorkbook wb = new XSSFWorkbook();
        if(!ListUtil.isEmpty(mesExportExcel.getSheetConfigs()))
        {
        	for (int i = 0; i < mesExportExcel.getSheetConfigs().size(); i++) {
        		List<Map<String, Object>> cellDatas = mesExportExcel.getSheetConfigs().get(i).getCellDatas();
        		Map<String, Integer> maxXAndY = mesExportExcel.getSheetConfigs().get(i).getMaxXAndY();
        		Map<String, Map<String, Object>> hyperlinks = mesExportExcel.getSheetConfigs().get(i).getHyperlinks();
        		List<Object> borderInfos = mesExportExcel.getSheetConfigs().get(i).getBorderInfos();
        		Map<String, Object> rowlen = mesExportExcel.getSheetConfigs().get(i).getRowlen();
        		Map<String, Object> columnlen = mesExportExcel.getSheetConfigs().get(i).getColumnlen();
        		JSONObject frozen = mesExportExcel.getSheetConfigs().get(i).getFrozen();
        		JSONObject images = mesExportExcel.getSheetConfigs().get(i).getBase64Images();
        		JSONObject dataVerification = mesExportExcel.getSheetConfigs().get(i).getDataVerification();
        		List<JSONObject> imageDatas = mesExportExcel.getSheetConfigs().get(i).getImageDatas();
        		String sheetname = mesExportExcel.getSheetConfigs().get(i).getSheetname();
        		JSONObject colhidden = mesExportExcel.getSheetConfigs().get(i).getColhidden();
        		JSONObject rowhidden = mesExportExcel.getSheetConfigs().get(i).getRowhidden();
        		JSONArray xxbtCells = new JSONArray();//斜线表头单元格
        		XSSFSheet sheet = wb.createSheet(sheetname);
        		List<String> noViewAuthCells = mesExportExcel.getSheetConfigs().get(i).getNoViewAuthCells();
        		sheet.setForceFormulaRecalculation(true);
        		if(colhidden != null && !colhidden.isEmpty())
        		{
        			for(String key:colhidden.keySet())
        			{
        				int value = colhidden.getIntValue(key);
        				if(value == 0)
        				{
        					int column = Integer.parseInt(key);
        					sheet.setColumnHidden(column, true);
        				}
        				
        			}
        		}
        		if(frozen != null)
                {
                	String frozenType = frozen.getString("type");
                	int row = 0;
                	int column = 0;
                	if(StringUtil.isNotEmpty(frozenType))
                	{
        	        	switch (frozenType) {
        				case "row":
        					sheet.createFreezePane(0, 1);
        					break;
        				case "column":
        					sheet.createFreezePane(1, 0);
        					break;
        				case "both":
        					sheet.createFreezePane(1, 1);
        					break;
        				case "rangeRow":
        					row = frozen.getJSONObject("range").getIntValue("row_focus");
        					sheet.createFreezePane(0, row+1);
        					break;
        				case "rangeColumn":
        					column = frozen.getJSONObject("range").getIntValue("column_focus");
        					sheet.createFreezePane(column+1,0);
        				case "rangeBoth":
        					row = frozen.getJSONObject("range").getIntValue("row_focus");
        					column = frozen.getJSONObject("range").getIntValue("column_focus");
        					sheet.createFreezePane(column+1, row+1);
        				default:
        					break;
        				}
                	}
                }
//        		sheet.setRandomAccessWindowSize(-1);
                LuckySheetCellUtil cellUtil = new LuckySheetCellUtil(wb, sheet);
                cellUtil.createCells(maxXAndY.get("maxX"), maxXAndY.get("maxY"),rowlen,rowhidden);
                if(columnlen != null)
                {
                	for (Map.Entry<String, Object> entry : columnlen.entrySet()) {
                		BigDecimal wid = new BigDecimal(String.valueOf(entry.getValue()));
                		BigDecimal excleWid=new BigDecimal(35);
                		sheet.setColumnWidth(Integer.parseInt(String.valueOf(entry.getKey())), wid.multiply(excleWid).setScale(0,BigDecimal.ROUND_HALF_UP).intValue());//列宽px值
                	}
                }
                JSONArray barCodeCells = new JSONArray();//条形码单元格
                JSONArray qrCodeCells = new JSONArray();//二维码单元格
                Map<String, Object> wrapText = new HashMap<>();
                mesExportExcel.getSheetConfigs().get(i).setWrapText(wrapText);
                cellUtil.setCellValues(cellDatas,hyperlinks,borderInfos,null,mesExportExcel.getSheetConfigs().get(i).getMerge(),mesExportExcel.getSheetConfigs().get(i).getIsCoedit(), dataVerification,xxbtCells,true,barCodeCells,qrCodeCells,wrapText,noViewAuthCells);
                cellUtil.setRowHeight(maxXAndY.get("maxX"), rowlen, rowhidden, wrapText);
                JSONObject rowlenObj = JSONObject.parseObject(JSONObject.toJSONString(rowlen));
                JSONObject columnlenObj = JSONObject.parseObject(JSONObject.toJSONString(columnlen));
                setImages(wb,sheet,images,columnlenObj,rowlenObj,rowhidden,colhidden,mesExportExcel.getImageInfos(),mesExportExcel.getBackImages());
                insertUrlImg(wb,sheet,imageDatas);
                if(type == 1) {
                	insertChart(wb, sheet, mesExportExcel.getSheetConfigs().get(i).getChart(),mesExportExcel.getSheetConfigs().get(i).getChartCells(),cellUtil);
                }else {
                	insertChart(wb, sheet, mesExportExcel.getSheetConfigs().get(i).getChart(),mesExportExcel.getSheetConfigs().get(i).getChartCells(),mesExportExcel.getSheetConfigs().get(i).getIsCoedit(),rowlenObj,rowhidden,columnlenObj,colhidden);
                }
//                insertChart(wb, sheet, mesExportExcel.getSheetConfigs().get(i).getChart(),mesExportExcel.getSheetConfigs().get(i).getChartCells());
//                insertChart(wb, sheet, mesExportExcel.getSheetConfigs().get(i).getChart(),mesExportExcel.getSheetConfigs().get(i).getChartCells(),cellUtil);
//                insertBase64Chart(wb, sheet, mesExportExcel.getSheetConfigs().get(i).getChart(),mesExportExcel.getSheetConfigs().get(i).getChartCells(),cellUtil,mesExportExcel.getChartsBase64());
                if(ListUtil.isNotEmpty(barCodeCells)) {
                	for (int j = 0; j < barCodeCells.size(); j++) {
                		JSONObject cellValue = barCodeCells.getJSONObject(j).getJSONObject(LuckySheetPropsEnum.CELLCONFIG.getCode());
                		JSONObject mergeCell = cellValue.getJSONObject(LuckySheetPropsEnum.MERGECELLS.getCode());
                		if(StringUtil.isNotEmpty(cellValue.getString("v"))) {
                			byte[] barCodeByte = BarCodeUtil.generateBarcodeImage(cellValue.getString("v"), 200, 100);
                    		if(mergeCell != null) {
                    			if(mergeCell.containsKey(LuckySheetPropsEnum.ROWSPAN.getCode()) && mergeCell.containsKey(LuckySheetPropsEnum.COLSPAN.getCode())) {
                    				int firstRow = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.R.getCode())));
        							int lastRow = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.R.getCode()))) + Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.ROWSPAN.getCode()))) - 1;
        							int firstCol = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.C.getCode())));
        							int lastCol = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.C.getCode()))) + Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.COLSPAN.getCode()))) - 1;
        							ReportExcelUtil.insertBarcode(wb, sheet, barCodeByte, firstRow, lastRow, firstCol, lastCol);
                    			}
                    		}else {
                    			int r = barCodeCells.getJSONObject(j).getIntValue("r");
    							int c = barCodeCells.getJSONObject(j).getIntValue("c");
    							ReportExcelUtil.insertBarcode(wb, sheet, barCodeByte, r, r, c, c);
                    		}
                		}
                		
					}
                }
                if(ListUtil.isNotEmpty(qrCodeCells)) {
                	for (int j = 0; j < qrCodeCells.size(); j++) {
                		JSONObject cellValue = qrCodeCells.getJSONObject(j).getJSONObject(LuckySheetPropsEnum.CELLCONFIG.getCode());
                		JSONObject mergeCell = cellValue.getJSONObject(LuckySheetPropsEnum.MERGECELLS.getCode());
                		if(StringUtil.isNotEmpty(cellValue.getString("v"))) {
                			byte[] barCodeByte = QRCodeUtil.generateQRCodeImage(cellValue.getString("v"), 256, 256);
                    		if(mergeCell != null) {
                    			if(mergeCell.containsKey(LuckySheetPropsEnum.ROWSPAN.getCode()) && mergeCell.containsKey(LuckySheetPropsEnum.COLSPAN.getCode())) {
                    				int firstRow = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.R.getCode())));
        							int lastRow = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.R.getCode()))) + Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.ROWSPAN.getCode()))) - 1;
        							int firstCol = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.C.getCode())));
        							int lastCol = Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.C.getCode()))) + Integer.valueOf(String.valueOf(mergeCell.get(LuckySheetPropsEnum.COLSPAN.getCode()))) - 1;
        							ReportExcelUtil.insertBarcode(wb, sheet, barCodeByte, firstRow, lastRow, firstCol, lastCol);
                    			}
                    		}else {
                    			int r = qrCodeCells.getJSONObject(j).getIntValue("r");
    							int c = qrCodeCells.getJSONObject(j).getIntValue("c");
    							ReportExcelUtil.insertBarcode(wb, sheet, barCodeByte, r, r, c, c);
                    		}
                		}
					}
                }
                processSheetConditionFormat(sheet, mesExportExcel.getSheetConfigs().get(i).getLuckysheetConditionformatSave());
        	}
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
 	    wb.write(baos);
 	    baos.flush();
 	    ZipSecureFile.setMinInflateRatio(-1.0d);
 	    ByteArrayInputStream workbookInput = new ByteArrayInputStream(baos.toByteArray());
 	    return workbookInput;
	}
	
	
	private static void setImages(XSSFWorkbook wb,XSSFSheet sheet,JSONObject images,JSONObject columnlenObject,JSONObject rowlenObject,JSONObject rowhidden,JSONObject colhidden,Map<String, Map<String, Object>> imageInfos,Map<String, String> backImages) {
		//图片插入
        if (images != null){
        	Map<String, Object> map = images.getInnerMap();
        	for(Map.Entry<String, Object> entry : map.entrySet()) {
        		XSSFDrawing patriarch = sheet.createDrawingPatriarch();
        		 //图片信息
                JSONObject iamgeData = (JSONObject) entry.getValue();
                Integer zIndex = iamgeData.getInteger("zIndex");
                if(zIndex == null) {
                	zIndex = 0;
                }
                 //图片的位置宽 高 距离左 距离右
                JSONObject imageDefault = ((JSONObject) iamgeData.get("default"));
                Map<String, Object> colrowMap = getColRowMap(imageDefault,columnlenObject, rowlenObject,rowhidden,colhidden);
                colrowMap.put("width", imageDefault.getFloat("width"));
                colrowMap.put("height", imageDefault.getFloat("height"));
                XSSFClientAnchor anchor = new XSSFClientAnchor((int)colrowMap.get("dx1"), (int)colrowMap.get("dy1"), (int)colrowMap.get("dx2"), (int)colrowMap.get("dy2"), (int)colrowMap.get("col1"), (int)colrowMap.get("row1"), (int)colrowMap.get("col2"), (int)colrowMap.get("row2"));
                anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);
                byte[] decoderBytes = new byte[0];
                boolean flag = true;
                if (iamgeData.get("src") != null) {
                	if(StringUtil.isImgUrl(String.valueOf(iamgeData.get("src"))))
                	{
                		decoderBytes = HttpUtil.downloadBytes(String.valueOf(iamgeData.get("src")));
    				    flag = iamgeData.get("src").toString().contains(".png");
                	}else {
                		decoderBytes = Base64Decoder.decode(iamgeData.get("src").toString().split(";base64,")[1]);
    				    flag = iamgeData.get("src").toString().split(";base64,")[0].contains("png");
                	}
                 	if(imageInfos != null) {
                		colrowMap.put("pictureBytes", decoderBytes);
                		colrowMap.put("zIndex", zIndex);
                    	imageInfos.put(entry.getKey(), colrowMap);	
                    }
				}
                if(backImages != null) {
                	backImages.put((int)colrowMap.get("row1")+"_"+(int)colrowMap.get("col1"), "1");
                }
                Picture picture = null;
                if (flag) {
                	picture = patriarch.createPicture(anchor, wb.addPicture(decoderBytes, HSSFWorkbook.PICTURE_TYPE_PNG));
                } else {
                	picture = patriarch.createPicture(anchor, wb.addPicture(decoderBytes, HSSFWorkbook.PICTURE_TYPE_JPEG));
                }
//                picture.resize(0.96, 0.82);
        	}
        }
	}
	
	private static void setImages(SXSSFWorkbook wb,SXSSFSheet sheet,JSONObject images,JSONObject columnlenObject,JSONObject rowlenObject,JSONObject rowhidden,JSONObject colhidden,Map<String, Map<String, Object>> imageInfos,Map<String, String> backImages) {
		//图片插入
        if (images != null){
        	Map<String, Object> map = images.getInnerMap();
        	for(Map.Entry<String, Object> entry : map.entrySet()) {
        		SXSSFDrawing patriarch = sheet.createDrawingPatriarch();
        		 //图片信息
                JSONObject iamgeData = (JSONObject) entry.getValue();
                 //图片的位置宽 高 距离左 距离右
                JSONObject imageDefault = ((JSONObject) iamgeData.get("default"));
                Map<String, Object> colrowMap = getColRowMap(imageDefault,columnlenObject, rowlenObject,rowhidden,colhidden);
                
                XSSFClientAnchor anchor = new XSSFClientAnchor((int)colrowMap.get("dx1"), (int)colrowMap.get("dy1"), (int)colrowMap.get("dx2"), (int)colrowMap.get("dy2"), (int)colrowMap.get("col1"), (int)colrowMap.get("row1"), (int)colrowMap.get("col2"), (int)colrowMap.get("row2"));
                anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);
                byte[] decoderBytes = new byte[0];
                boolean flag = true;
                if (iamgeData.get("src") != null) {
                	if(StringUtil.isImgUrl(String.valueOf(iamgeData.get("src"))))
                	{
                		decoderBytes = HttpUtil.downloadBytes(String.valueOf(iamgeData.get("src")));
    				    flag = iamgeData.get("src").toString().contains(".png");
                	}else {
                		decoderBytes = Base64Decoder.decode(iamgeData.get("src").toString().split(";base64,")[1]);
    				    flag = iamgeData.get("src").toString().split(";base64,")[0].contains("png");
                	}
                 	if(imageInfos != null) {
                		colrowMap.put("pictureBytes", decoderBytes);
                    	imageInfos.put(entry.getKey(), colrowMap);	
                    }
				}
                if(backImages != null) {
                	backImages.put((int)colrowMap.get("row1")+"_"+(int)colrowMap.get("col1"), "1");
                }
                Picture picture = null;
                if (flag) {
                	picture = patriarch.createPicture(anchor, wb.addPicture(decoderBytes, HSSFWorkbook.PICTURE_TYPE_PNG));
                } else {
                	picture = patriarch.createPicture(anchor, wb.addPicture(decoderBytes, HSSFWorkbook.PICTURE_TYPE_JPEG));
                }
//                picture.resize(0.96, 0.82);
        	}
        }
	}
	
	public static void insertChart(XSSFWorkbook wb,XSSFSheet sheet,JSONArray jsonArray,JSONObject chartCells,LuckySheetCellUtil cellUtil) throws IOException
	{
		if(!ListUtil.isEmpty(jsonArray))
		{
			XSSFDrawing patriarch = sheet.createDrawingPatriarch();
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String chartId = jsonObject.getString("chart_id");
				JSONObject chartOptions = jsonObject.getJSONObject("chartOptions");
				if(chartOptions.getJSONObject("defaultOption") == null || chartOptions.getJSONObject("defaultOption").getJSONObject("spec") == null) {
					continue;
				}
				JSONObject defaultOption = chartOptions.getJSONObject("defaultOption");
				String chartAllType = chartOptions.getString("chartAllType");
				JSONObject chartCell = chartCells.getJSONObject(chartId);
				if(chartCell != null)
				{
					int r = chartCell.getIntValue("r");
					int rs = chartCell.getIntValue("rs");
					int c = chartCell.getIntValue("c");
					int cs = chartCell.getIntValue("cs");
					cellUtil.mergeCell(r,r+rs-1,c,c+cs-1);
					XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0,c,r,c+cs,r+rs);
					anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);
					boolean showTitle = ExcelChartUtil.getTitleShow(chartOptions);
					String title = ExcelChartUtil.getTitle(chartOptions);
					List<JSONArray> chartData = ExcelChartUtil.groupChartData(chartOptions);
					if(chartAllType.contains("pie"))
					{//饼图
						JSONObject pieObject = JFreeChartUtil.getPieChartDataset(chartOptions,chartData);
						PieDataset pieDataset = (PieDataset) pieObject.get("dataSet");
						JSONArray legend = pieObject.getJSONArray("legend");
						double innerRadius = defaultOption.getJSONObject("spec").getDoubleValue("innerRadius");
						if(pieDataset != null)
						{
							byte[] chartBytes = null;
							if(innerRadius > 0) {
								chartBytes = JFreeChartUtil.createRingChart(showTitle?title:"", pieDataset, jsonObject.getIntValue("offsetWidth"), jsonObject.getIntValue("offsetHeight"));
							}else {
								String type = chartAllType.split("\\|")[2];
								chartBytes = JFreeChartUtil.createPieChart(showTitle?title:"", pieDataset, jsonObject.getIntValue("offsetWidth"), jsonObject.getIntValue("offsetHeight"),type,legend);
							}
							if(chartBytes != null)
							{
								patriarch.createPicture(anchor, wb.addPicture(chartBytes, HSSFWorkbook.PICTURE_TYPE_JPEG));
							}
						}
					}else if(chartAllType.contains("line")) {
						DefaultCategoryDataset dataset = JFreeChartUtil.getCategoryDataset(chartOptions,chartData);
						byte[] chartBytes = JFreeChartUtil.createLineChart(showTitle?title:"", dataset, jsonObject.getIntValue("offsetWidth"), jsonObject.getIntValue("offsetHeight"));
						if(chartBytes != null)
						{
							patriarch.createPicture(anchor, wb.addPicture(chartBytes, HSSFWorkbook.PICTURE_TYPE_JPEG));
						}
					}
					else if(chartAllType.contains("area")) {
						DefaultCategoryDataset dataset = JFreeChartUtil.getCategoryDataset(chartOptions,chartData);
						byte[] chartBytes = JFreeChartUtil.createAreaChart(showTitle?title:"", dataset, jsonObject.getIntValue("offsetWidth"), jsonObject.getIntValue("offsetHeight"));
						if(chartBytes != null)
						{
							patriarch.createPicture(anchor, wb.addPicture(chartBytes, HSSFWorkbook.PICTURE_TYPE_JPEG));
						}
					}
					else if(chartAllType.contains("column")) {
						DefaultCategoryDataset dataset = JFreeChartUtil.getCategoryDataset(chartOptions,chartData);
						byte[] chartBytes = null;
						if(chartAllType.contains("stack"))
						{
							chartBytes = JFreeChartUtil.createStackedBar(showTitle?title:"", dataset, jsonObject.getIntValue("offsetWidth"), jsonObject.getIntValue("offsetHeight"),"column");
						}else {
							chartBytes = JFreeChartUtil.creteBarChart(showTitle?title:"", dataset, jsonObject.getIntValue("width"), jsonObject.getIntValue("offsetHeight"),"column");
						}
						if(chartBytes != null)
						{
							patriarch.createPicture(anchor, wb.addPicture(chartBytes, HSSFWorkbook.PICTURE_TYPE_JPEG));
						}
					}else if(chartAllType.contains("bar")) {
						DefaultCategoryDataset dataset = JFreeChartUtil.getCategoryDataset(chartOptions,chartData);
						byte[] chartBytes = null;
						if(chartAllType.contains("stack"))
						{
							chartBytes = JFreeChartUtil.createStackedBar(showTitle?title:"", dataset, jsonObject.getIntValue("offsetWidth"), jsonObject.getIntValue("offsetHeight"),"bar");
						}else {
							chartBytes = JFreeChartUtil.creteBarChart(showTitle?title:"", dataset, jsonObject.getIntValue("offsetWidth"), jsonObject.getIntValue("offsetHeight"),"bar");
						}
						if(chartBytes != null)
						{
							patriarch.createPicture(anchor, wb.addPicture(chartBytes, HSSFWorkbook.PICTURE_TYPE_JPEG));
						}
					}else if(chartAllType.contains("radar")) {
						JSONObject radarData = JFreeChartUtil.getRadarDataset(chartOptions,chartData);
						DefaultCategoryDataset dataset = (DefaultCategoryDataset) radarData.get("dataSet");
						float maxValue = (float) radarData.get("maxValue");
						byte[] chartBytes = null;
						CalibrationSpiderWebPlot plot = new CalibrationSpiderWebPlot(dataset,maxValue,true);
						plot.setOutlinePaint(Color.white);
					    //设置Label字体
					    plot.setLabelFont(new java.awt.Font("微软雅黑", java.awt.Font.BOLD, 12));
					    plot.setOutlineVisible(true);
					    plot.setBackgroundAlpha(0f);
						Font f = new Font("宋体",Font.BOLD,12);
						JFreeChart chart = new JFreeChart(showTitle?title:"", f, plot, false);
						LegendTitle legendtitle = new LegendTitle(plot);
				        legendtitle.setPosition(RectangleEdge.BOTTOM);
				        chart.addSubtitle(legendtitle);
				        chart.setBackgroundPaint(Color.WHITE);
						BufferedImage bufferedImage = chart.createBufferedImage(jsonObject.getIntValue("offsetWidth"), jsonObject.getIntValue("offsetHeight"),
					                BufferedImage.TYPE_INT_RGB,null);
					    ByteArrayOutputStream os = new ByteArrayOutputStream();
					    ImageIO.write(bufferedImage, "jpeg", os);
					    chartBytes = os.toByteArray();
					    if(chartBytes != null)
					    {
					    	patriarch.createPicture(anchor, wb.addPicture(chartBytes, HSSFWorkbook.PICTURE_TYPE_JPEG));
						}
					}
				}
				
			}
             
		}
	}
	
	public static void insertBase64Chart(XSSFWorkbook wb,XSSFSheet sheet,JSONArray jsonArray,JSONObject chartCells,LuckySheetCellUtil cellUtil,JSONObject chartsBase64) {
		if(!ListUtil.isEmpty(jsonArray) && chartsBase64 != null)
		{
			XSSFDrawing patriarch = sheet.createDrawingPatriarch();
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String chartId = jsonObject.getString("chart_id");
				JSONObject chartCell = chartCells.getJSONObject(chartId);
				String base64 = chartsBase64.getString(chartId);
				if(chartCell != null && StringUtil.isNotEmpty(base64))
				{
					int r = chartCell.getIntValue("r");
					int rs = chartCell.getIntValue("rs");
					int c = chartCell.getIntValue("c");
					int cs = chartCell.getIntValue("cs");
					cellUtil.mergeCell(r,r+rs-1,c,c+cs-1);
					XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0,c,r,c+cs,r+rs);
					anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);
					byte[] chartBytes = StringUtil.convertBase64ToByte(base64);
					patriarch.createPicture(anchor, wb.addPicture(chartBytes, HSSFWorkbook.PICTURE_TYPE_JPEG));
				}
			}
		}
	}
	
	/**
     * 获取图片位置
     * dx1：起始单元格的x偏移量，如例子中的255表示直线起始位置距A1单元格左侧的距离；
     * dy1：起始单元格的y偏移量，如例子中的125表示直线起始位置距A1单元格上侧的距离；
     * dx2：终止单元格的x偏移量，如例子中的1023表示直线起始位置距C3单元格左侧的距离；
     * dy2：终止单元格的y偏移量，如例子中的150表示直线起始位置距C3单元格上侧的距离；
     * col1：起始单元格列序号，从0开始计算；竖
     * row1：起始单元格行序号，从0开始计算，如例子中col1=0,row1=0就表示起始单元格为A1；横
     * col2：终止单元格列序号，从0开始计算；
     * row2：终止单元格行序号，从0开始计算，如例子中col2=2,row2=2就表示起始单元格为C3；
     * @param imageDefault
     * @param defaultRowHeight
     * @param defaultColWidth
     * @param columnlenObject
     * @param rowlenObject
     */
    private static Map<String, Object> getColRowMap(JSONObject imageDefault,JSONObject columnlenObject, JSONObject rowlenObject,JSONObject rowhidden,JSONObject colhidden){
    	Map<String, Object> map =new HashMap<>();
    	double left =  Double.parseDouble(String.valueOf(imageDefault.get("left")));
        double top =  Double.parseDouble(String.valueOf(imageDefault.get("top")));
        double width =  Double.parseDouble(String.valueOf(imageDefault.get("width")));
        double height =  Double.parseDouble(String.valueOf(imageDefault.get("height")));
        JSONObject strObj = LuckysheetUtil.calculateRows(top, rowlenObject, rowhidden);
        JSONObject edrObj = LuckysheetUtil.calculateRows(top+height, rowlenObject, rowhidden);
        JSONObject stcObj = LuckysheetUtil.calculateCols(left, columnlenObject, colhidden);
        JSONObject edcObj = LuckysheetUtil.calculateCols(left+width, columnlenObject, colhidden);
        int row1 = strObj.getIntValue("r");
        BigDecimal dy1 = strObj.getBigDecimal("dy");
        int row2 = edrObj.getIntValue("r");
        BigDecimal dy2 = edrObj.getBigDecimal("dy");
        int col1 = stcObj.getIntValue("c");
        BigDecimal dx1 = stcObj.getBigDecimal("dx").subtract(new BigDecimal(2));
        int col2 = edcObj.getIntValue("c");
        BigDecimal dx2 = edcObj.getBigDecimal("dx");
        map.put("dx1",dx1.multiply(new BigDecimal(Units.EMU_PER_PIXEL)).setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
        map.put("dy1",dy1.multiply(new BigDecimal(Units.EMU_PER_PIXEL)).setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
        map.put("dx2",dx2.multiply(new BigDecimal(Units.EMU_PER_PIXEL)).setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
        map.put("dy2",dy2.multiply(new BigDecimal(Units.EMU_PER_PIXEL)).setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
        map.put("col1",col1);
        map.put("row1",row1);
        map.put("col2",col2);
        map.put("row2",row2);
        map.put("dy1Percent",strObj.get("percent"));
        map.put("dy2Percent",edrObj.get("percent"));
        map.put("dx1Percent",stcObj.get("percent"));
        map.put("dx2Percent",edcObj.get("percent"));
        return map;
    }
    
    public static void insertUrlImg(XSSFWorkbook workbook,XSSFSheet sheet, List<JSONObject> imgDatas) {
		if (imgDatas == null || imgDatas.isEmpty()) return;

		Drawing<?> drawing = sheet.getDrawingPatriarch();
		if (drawing == null) {
			drawing = sheet.createDrawingPatriarch();
		}

		for (JSONObject img : imgDatas) {
			try {
				String url = img.getJSONObject("imgInfo").getString("src");
				int r = img.getIntValue("r");
				int c = img.getIntValue("c");

				int mergeCellWidth = img.getJSONObject("imgInfo").getJSONObject("crop").getIntValue("width");
				int mergeCellHeight = img.getJSONObject("imgInfo").getJSONObject("crop").getIntValue("height");
				int endR = r;
				int endC = c;
				if (img.getIntValue("isMerge") == YesNoEnum.YES.getCode().intValue()) {
					endR += img.getIntValue("rowSpan") - 1;
					endC += img.getIntValue("colSpan") - 1;
				}

				byte[] imgBytes = HttpUtil.downloadBytes(url);
				if (imgBytes == null || imgBytes.length == 0) continue;

				XSSFClientAnchor anchor = createCustomAnchor(workbook, sheet, imgBytes, c, mergeCellWidth, r, mergeCellHeight, endC, endR);
				int pictureIndex = workbook.addPicture(imgBytes, Workbook.PICTURE_TYPE_PNG);
				drawing.createPicture(anchor, pictureIndex);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}}
    
    private static XSSFClientAnchor createCustomAnchor (XSSFWorkbook wb, XSSFSheet sheet,byte[] imgBytes,
    		int col1, int mergeCellWidth, int row1, int mergeCellHeight, int colEnd, int rowEnd) throws IOException {
    			final float POINT_TO_PIXEL = 96f / 72f; // 行高：pt -> px

    			// 1) 目标区宽度/高度（像素）
    			double totalWidthPx = 0d;
    			for (int c = col1; c <= colEnd; c++) {
    				totalWidthPx += sheet.getColumnWidthInPixels(c);    // 精准列宽（像素）
    			}
    			double totalHeightPx = 0d;
    			for (int r = row1; r <= rowEnd; r++) {
    				float hPt = (sheet.getRow(r) != null) ? sheet.getRow(r).getHeightInPoints() : sheet.getDefaultRowHeightInPoints();
    				totalHeightPx += hPt * POINT_TO_PIXEL;
    			}

    			// 2) 图片尺寸 & 居中缩放
    			BufferedImage bi = ImageIO.read(new ByteArrayInputStream(imgBytes));
    			if (bi == null) return null;
    			int imgW = bi.getWidth(), imgH = bi.getHeight();

    			double scale = Math.min(totalWidthPx / imgW, totalHeightPx / imgH);
    			int scaledW = (int)Math.round(imgW * scale);
    			int scaledH = (int)Math.round(imgH * scale);

    			int offsetX = (int)Math.floor((totalWidthPx  - scaledW) / 2.0); // 居中：左右留白相等
    			int offsetY = (int)Math.floor((totalHeightPx - scaledH) / 2.0);

    			// 3) 把“区域内像素偏移 → 起点列/行 + dx1/dy1”
    			int startCol = col1, dx1 = 0;
    			double wSum = 0d;
    			for (int c = col1; c <= colEnd; c++) {
    				double w = sheet.getColumnWidthInPixels(c);
    				if (wSum + w > offsetX) {
    					startCol = c;
    					dx1 = Units.pixelToEMU((int)Math.round(offsetX - wSum + 2));
    					break;
    				}
    				wSum += w;
    			}

    			int startRow = row1, dy1 = 0;
    			double hSum = 0d;
    			for (int r = row1; r <= rowEnd; r++) {
    				double h = ((sheet.getRow(r) != null) ? sheet.getRow(r).getHeightInPoints() : sheet.getDefaultRowHeightInPoints()) * POINT_TO_PIXEL;
    				if (hSum + h > offsetY) {
    					startRow = r;
    					dy1 = Units.pixelToEMU((int)Math.round(offsetY - hSum + 2));
    					break;
    				}
    				hSum += h;
    			}

    			// 4) 把“右下角像素位置 → 终点列/行 + dx2/dy2”
    			int endCol = col1, dx2 = 0;
    			wSum = 0d;
    			int rightX = offsetX + scaledW;
    			for (int c = col1; c <= colEnd; c++) {
    				double w = sheet.getColumnWidthInPixels(c);
    				if (wSum + w >= rightX) {
    					endCol = c;
    					dx2 = Units.pixelToEMU((int)Math.round(rightX - wSum));
    					break;
    				}
    				wSum += w;
    			}

    			int endRow = row1, dy2 = 0;
    			hSum = 0d;
    			int bottomY = offsetY + scaledH;
    			for (int r = row1; r <= rowEnd; r++) {
    				double h = ((sheet.getRow(r) != null) ? sheet.getRow(r).getHeightInPoints() : sheet.getDefaultRowHeightInPoints()) * POINT_TO_PIXEL;
    				if (hSum + h >= bottomY) {
    					endRow = r;
    					dy2 = Units.pixelToEMU((int)Math.round(bottomY - hSum));
    					break;
    				}
    				hSum += h;
    			}

    			XSSFClientAnchor anchor = new XSSFClientAnchor(
    					dx1, dy1, dx2, dy2,
    					(short) startCol, startRow,
    					(short) endCol,   endRow
    			);
    			anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
    			return anchor;
    		}
    
    public static void insertUrlImg(SXSSFWorkbook workbook,SXSSFSheet sheet, List<JSONObject> imgDatas) {
    	if(!ListUtil.isEmpty(imgDatas))
    	{
    		for (int i = 0; i < imgDatas.size(); i++) {
    			JSONObject img = imgDatas.get(i);
    			String url = img.getJSONObject("imgInfo").getString("src");
    			int r = img.getIntValue("r");//横坐标
				int c = img.getIntValue("c");//纵坐标
				int isMerge = img.getIntValue("isMerge");
				int endR = img.getIntValue("r");
				int endC = img.getIntValue("c");
				if(YesNoEnum.YES.getCode().intValue() == isMerge)
				{
					int rowSpan = img.getIntValue("rowSpan");
					int colSpan = img.getIntValue("colSpan"); 
					endR = endR + rowSpan - 1;
					endC = endC + colSpan - 1;
				}
				byte[] bytes = HttpUtil.downloadBytes(url);
				insertImg(workbook, sheet, bytes, r, endR, c, endC);
			}
    	}
    }
    
    /**
     * 插入图片
     *
     * @param workbook      文档对象
     * @param sheet         sheet页对象
     * @param picture       图片二进制流数组
     * @param beginRowIndex 开始行号
     * @param endRowIndex   结束行号
     * @param beginColIndex 开始列号
     * @param endColIndex   结束列号
     */
    public static void insertImg(XSSFWorkbook workbook, XSSFSheet sheet, byte[] picture, int beginRowIndex, int endRowIndex
            , int beginColIndex, int endColIndex) {
        //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
        Drawing drawing = sheet.getDrawingPatriarch();
        if (drawing == null) {
            drawing = sheet.createDrawingPatriarch();
        }
        //anchor主要用于设置图片的属性
        ClientAnchor anchor = null;
        anchor = new XSSFClientAnchor(0, 0, 255, 255, (short) beginColIndex, beginRowIndex, (short) (endColIndex + 1), endRowIndex + 1);
        //插入图片
        drawing.createPicture(anchor, workbook.addPicture(picture, Workbook.PICTURE_TYPE_JPEG));
    }
    
    public static void insertImg(SXSSFWorkbook workbook, SXSSFSheet sheet, byte[] picture, int beginRowIndex, int endRowIndex
            , int beginColIndex, int endColIndex) {
        //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
        Drawing drawing = sheet.getDrawingPatriarch();
        if (drawing == null) {
            drawing = sheet.createDrawingPatriarch();
        }
        //anchor主要用于设置图片的属性
        ClientAnchor anchor = null;
        anchor = new XSSFClientAnchor(0, 0, 255, 255, (short) beginColIndex, beginRowIndex, (short) (endColIndex + 1), endRowIndex + 1);
        //插入图片
        drawing.createPicture(anchor, workbook.addPicture(picture, Workbook.PICTURE_TYPE_JPEG));
    }
    
    public static void insertBarcode(XSSFWorkbook workbook, XSSFSheet sheet, byte[] picture, int beginRowIndex, int endRowIndex
            , int beginColIndex, int endColIndex) {
        //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
        Drawing drawing = sheet.getDrawingPatriarch();
        if (drawing == null) {
            drawing = sheet.createDrawingPatriarch();
        }
        //anchor主要用于设置图片的属性
        ClientAnchor anchor = null;
        anchor = new XSSFClientAnchor(0, 0, 255, 255, (short) beginColIndex, beginRowIndex, (short) (endColIndex + 1), endRowIndex + 1);
        anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);
        anchor.setDx1(Units.EMU_PER_PIXEL * 5);
        anchor.setDx2(Units.EMU_PER_PIXEL *(-5));
        anchor.setDy1(Units.EMU_PER_PIXEL *5);
        anchor.setDy2(Units.EMU_PER_PIXEL *(-5));
        //插入图片
        drawing.createPicture(anchor, workbook.addPicture(picture, Workbook.PICTURE_TYPE_PNG));
    }
    public static void insertBarcode(SXSSFWorkbook workbook, SXSSFSheet sheet, byte[] picture, int beginRowIndex, int endRowIndex
            , int beginColIndex, int endColIndex) {
        //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
        Drawing drawing = sheet.getDrawingPatriarch();
        if (drawing == null) {
            drawing = sheet.createDrawingPatriarch();
        }
        //anchor主要用于设置图片的属性
        ClientAnchor anchor = null;
        anchor = new XSSFClientAnchor(0, 0, 255, 255, (short) beginColIndex, beginRowIndex, (short) (endColIndex + 1), endRowIndex + 1);
        anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);
        anchor.setDx1(Units.EMU_PER_PIXEL * 5);
        anchor.setDx2(Units.EMU_PER_PIXEL *(-5));
        anchor.setDy1(Units.EMU_PER_PIXEL *5);
        anchor.setDy2(Units.EMU_PER_PIXEL *(-5));
        //插入图片
        drawing.createPicture(anchor, workbook.addPicture(picture, Workbook.PICTURE_TYPE_PNG));
    }
    
    /**  
     * @MethodName: addDataVerification
     * @Description: 添加数据校验项
     * @author caiyang 
     * @return void
     * @date 2023-05-06 08:37:01 
     */  
    private static void addDataVerification(XSSFSheet sheet,JSONObject dataVerification) {
    	if(dataVerification != null && !dataVerification.isEmpty())
    	{
    		DataValidationHelper helper = sheet.getDataValidationHelper();
    		for(Map.Entry entry : dataVerification.entrySet()){
    			String key = (String) entry.getKey();
    			int r = Integer.parseInt(key.split("_")[0]);
    			int c = Integer.parseInt(key.split("_")[1]);
    			JSONObject value = (JSONObject) entry.getValue();
    			String type = value.getString("type");
    			String value1 = value.getString("value1");
    			String value2 = value.getString("value2");
    			DataValidationConstraint constraint = null;
    			if("dropdown".equals(type))
    			{//下拉
    				if(value1.contains("$"))
    				{
    					constraint = helper.createFormulaListConstraint(value1);
    				}else {
    					constraint = helper.createExplicitListConstraint(value1.split(","));
    				}
    				
    			}else if("number_integer".equals(type))
    			{//整数
    				String type2 = value.getString("type2");
    				int operate = getNumberOperateType(type2);
    				constraint = helper.createIntegerConstraint(operate, value1, value2);
    			}else if("number_decimal".equals(type))
    			{//小数
    				String type2 = value.getString("type2");
    				int operate = getNumberOperateType(type2);
    				constraint = helper.createDecimalConstraint(operate, value1, value2);
    			}else if("text_length".equals(type))
    			{//长度
    				String type2 = value.getString("type2");
    				int operate = getNumberOperateType(type2);
    				constraint = helper.createTextLengthConstraint(operate, value1, value2);
    			}else if("date".equals(type)) {
    				//日期
    				String type2 = value.getString("type2");
    				int operate = getDateOperateType(type2);
    				constraint = helper.createDateConstraint(operate, value1, value2, DateUtil.FORMAT_LONOGRAM);
    			}
    			CellRangeAddressList addressList = new CellRangeAddressList(r,r,c,c);
    			if(constraint != null)
    			{
    				DataValidation validation = helper.createValidation(constraint,addressList);
        			validation.setSuppressDropDownArrow(true);
                    validation.setShowErrorBox(true);
                    sheet.addValidationData(validation);
    			}
    			
    		}
    	}
    }
    
    private static void addDataVerification(SXSSFSheet sheet,JSONObject dataVerification) {
    	if(dataVerification != null && !dataVerification.isEmpty())
    	{
    		DataValidationHelper helper = sheet.getDataValidationHelper();
    		for(Map.Entry entry : dataVerification.entrySet()){
    			String key = (String) entry.getKey();
    			int r = Integer.parseInt(key.split("_")[0]);
    			int c = Integer.parseInt(key.split("_")[1]);
    			JSONObject value = (JSONObject) entry.getValue();
    			String type = value.getString("type");
    			String value1 = value.getString("value1");
    			String value2 = value.getString("value2");
    			DataValidationConstraint constraint = null;
    			if("dropdown".equals(type))
    			{//下拉
    				if(value1.contains("$"))
    				{
    					constraint = helper.createFormulaListConstraint(value1);
    				}else {
    					constraint = helper.createExplicitListConstraint(value1.split(","));
    				}
    				
    			}else if("number_integer".equals(type))
    			{//整数
    				String type2 = value.getString("type2");
    				int operate = getNumberOperateType(type2);
    				constraint = helper.createIntegerConstraint(operate, value1, value2);
    			}else if("number_decimal".equals(type))
    			{//小数
    				String type2 = value.getString("type2");
    				int operate = getNumberOperateType(type2);
    				constraint = helper.createDecimalConstraint(operate, value1, value2);
    			}else if("text_length".equals(type))
    			{//长度
    				String type2 = value.getString("type2");
    				int operate = getNumberOperateType(type2);
    				constraint = helper.createTextLengthConstraint(operate, value1, value2);
    			}else if("date".equals(type)) {
    				//日期
    				String type2 = value.getString("type2");
    				int operate = getDateOperateType(type2);
    				constraint = helper.createDateConstraint(operate, value1, value2, DateUtil.FORMAT_LONOGRAM);
    			}
    			CellRangeAddressList addressList = new CellRangeAddressList(r,r,c,c);
    			if(constraint != null)
    			{
    				DataValidation validation = helper.createValidation(constraint,addressList);
        			validation.setSuppressDropDownArrow(true);
                    validation.setShowErrorBox(true);
                    sheet.addValidationData(validation);
    			}
    			
    		}
    	}
    }
    
    private static int getNumberOperateType(String type) {
    	int operate = 0;
    	switch (type) {
			case "bw":
				operate = 0;
				break;
			case "nb":
				operate = 1;
				break;
			case "eq":
				operate = 2;
				break;
			case "ne":
				operate = 3;
				break;
			case "gt":
				operate = 4;
			case "lt":
				operate = 5;
				break;
			case "gte":
				operate = 6;
				break;
			case "lte":
				operate = 7;
				break;
			default:
				operate = 0;
				break;
		}
    	return operate;
    }
    
    private static int getDateOperateType(String type) {
    	int operate = 0;
    	switch (type) {
			case "bw":
				operate = 0;
				break;
			case "nb":
				operate = 1;
				break;
			case "eq":
				operate = 2;
				break;
			case "ne":
				operate = 3;
				break;
			case "af":
				operate = 4;
			case "bf":
				operate = 5;
				break;
			case "nbf":
				operate = 6;
				break;
			case "naf":
				operate = 7;
				break;
			default:
				operate = 0;
				break;
		}
    	return operate;
    }
    
    /**  
     * @MethodName: getUnProtectCells
     * @Description: 获取不受保护的单元格
     * @author caiyang
     * @param authority
     * @return 
     * @return Map<String,String>
     * @date 2023-05-06 03:01:08 
     */  
    private static Map<String, String> getUnProtectCells(JSONObject authority){
    	Map<String, String> result = null;
    	JSONArray allowRangeList = authority.getJSONArray("allowRangeList");
    	if(!ListUtil.isEmpty(allowRangeList))
    	{
    		result = new HashMap<>();
    		for (int i = 0; i < allowRangeList.size(); i++) {
				String sqref = allowRangeList.getJSONObject(i).getString("sqref");
				int c = 0;
				int r = 0;
				int endc = 0;
				int endr = 0;
				if(sqref.contains(":"))
				{
					String[] sqrefs = sqref.split(":");
					c = SheetUtil.excelColStrToNum(SheetUtil.getColumnFlag(sqrefs[0].replaceAll("\\$", "")))-1;
					r = SheetUtil.getRowNum(sqrefs[0].replaceAll("\\$", ""))-1;
					endc = SheetUtil.excelColStrToNum(SheetUtil.getColumnFlag(sqrefs[1].replaceAll("\\$", "")))-1;
					endr = SheetUtil.getRowNum(sqrefs[1].replaceAll("\\$", ""))-1;
				}else {
					c = SheetUtil.excelColStrToNum(SheetUtil.getColumnFlag(sqref.replaceAll("\\$", "")))-1;
					r = SheetUtil.getRowNum(sqref.replaceAll("\\$", ""))-1;
					endc = c;
					endr = r;
				}
				for (int j = r; j <= endr; j++) {
					for (int j2 = c; j2 <= endc; j2++) {
						result.put(j+"_"+j2, j+"_"+j2);
					}
				}
			}
    	}
    	return result;
    }
    
    /**  
     * @MethodName: getFilterRange
     * @Description: 获取筛选条件范围
     * @author caiyang
     * @param filter
     * @return String
     * @date 2023-09-04 11:02:40 
     */ 
    private static String getFilterRange(JSONObject filter)
    {
    	JSONArray row = filter.getJSONArray("row");
    	JSONArray column = filter.getJSONArray("column");
    	int startr = row.getIntValue(0);
    	int endr = row.getIntValue(1);
    	int startc = column.getIntValue(0);
    	int endc = column.getIntValue(1);
    	String startcName = SheetUtil.excelColIndexToStr(startc+1);
    	String endcName = SheetUtil.excelColIndexToStr(endc+1);
    	String result = startcName+(startr+1)+":"+endcName+(endr+1);
    	return result;
    }
    
    public static void insertChart(XSSFWorkbook wb,XSSFSheet sheet,JSONArray jsonArray,JSONObject chartCells,int isCoedit,JSONObject rowlen,JSONObject rowhidden,JSONObject columnlen,JSONObject colhidden) throws IOException
	{
		if(!ListUtil.isEmpty(jsonArray))
		{
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String chartId = jsonObject.getString("chart_id");
				JSONObject chartOptions = jsonObject.getJSONObject("chartOptions");
				JSONObject defaultOption = chartOptions.getJSONObject("defaultOption");
				JSONArray values = chartOptions.getJSONObject("defaultOption").getJSONObject("spec").getJSONObject("data").getJSONArray("values");
				if(ListUtil.isEmpty(values)) {
					continue;
				}
				String chartAllType = chartOptions.getString("chartAllType");
				JSONObject chartCell = null;
				int r = 0;
				int rs = 1;
				int c = 0;
				int cs = 1;
				if(YesNoEnum.YES.getCode().intValue() == isCoedit) {
					double top = jsonObject.getDoubleValue("top");
					double left = jsonObject.getDoubleValue("left");
					double height = jsonObject.getDoubleValue("height");
					double width = jsonObject.getDoubleValue("width");
					JSONObject strObj = LuckysheetUtil.calculateRows(top, rowlen, rowhidden);
				    JSONObject edrObj = LuckysheetUtil.calculateRows(top+height, rowlen, rowhidden);
				    JSONObject stcObj = LuckysheetUtil.calculateCols(left, columnlen, colhidden);
				    JSONObject edcObj = LuckysheetUtil.calculateCols(left+width, columnlen, colhidden);
					r = strObj.getIntValue("r");
					rs = edrObj.getIntValue("r") - strObj.getIntValue("r") + 1;
					c = stcObj.getIntValue("c");
					cs = edcObj.getIntValue("c") - stcObj.getIntValue("c") + 1;
					chartCell = new JSONObject();
					chartCell.put("r", r);
					chartCell.put("rs", rs);
					chartCell.put("c", c);
					chartCell.put("cs", cs);
				}else {
					chartCell = chartCells.getJSONObject(chartId);
					if(chartCell != null)
					{
						r = chartCell.getIntValue("r");
						rs = chartCell.getIntValue("rs");
						c = chartCell.getIntValue("c");
						cs = chartCell.getIntValue("cs");
					}
				}
				XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0,c,r,c+cs,r+rs);
				anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);
//				boolean showTitle = defaultOption.getJSONObject("title").getBooleanValue("show");
//				String title = defaultOption.getJSONObject("title").getString("text");
				if(chartAllType.contains("pie"))
				{//饼图
					double innerRadius = defaultOption.getJSONObject("spec").getDoubleValue("innerRadius");
					if(innerRadius>0) {
						ExcelChartUtil.createDoughnut(sheet, chartCell, chartOptions,isCoedit);
					}else {
						ExcelChartUtil.createPie(sheet, chartCell, chartOptions,isCoedit);
					}
				}else if(chartAllType.contains("line")) {
					boolean smooth = false;
					boolean showLabel = false;
					if(chartAllType.contains("smooth")) {
						smooth = true;
					}
					if(chartAllType.contains("label")) {
						showLabel = true;
					}
					ExcelChartUtil.createLineChart(sheet, chartCell, chartOptions,smooth,showLabel,isCoedit);
				}
				else if(chartAllType.contains("area")) {
					ExcelChartUtil.createAreaChart(sheet, chartCell, chartOptions,isCoedit);
				}
				else if(chartAllType.contains("column")) {
					ExcelChartUtil.createBar(sheet, chartCell, chartOptions, "column", chartAllType.contains("stack"),isCoedit);
				}else if(chartAllType.contains("bar")) {
					ExcelChartUtil.createBar(sheet, chartCell, chartOptions, "bar", chartAllType.contains("stack"),isCoedit);
				}else if(chartAllType.contains("radar")) {
					ExcelChartUtil.createRadar(sheet, chartCell, chartOptions,isCoedit);
				}
				
			}
		}
	}
    private static XSSFConditionalFormattingRule createConditionalFormattingRuleDuplicate(SheetConditionalFormatting sheetConditionalFormatting) throws Exception {
    	  Field _sheet = XSSFSheetConditionalFormatting.class.getDeclaredField("_sheet");
    	  _sheet.setAccessible(true);
    	  XSSFSheet sheet = (XSSFSheet)_sheet.get(sheetConditionalFormatting);
    	  Constructor constructor = XSSFConditionalFormattingRule.class.getDeclaredConstructor(XSSFSheet.class);
    	  constructor.setAccessible(true);
    	  XSSFConditionalFormattingRule rule = (XSSFConditionalFormattingRule)constructor.newInstance(sheet);
    	  Field _cfRule = XSSFConditionalFormattingRule.class.getDeclaredField("_cfRule");
    	  _cfRule.setAccessible(true);
    	  CTCfRule cfRule = (CTCfRule)_cfRule.get(rule);
    	  cfRule.setType(STCfType.DUPLICATE_VALUES);
    	  return rule;
    }
    
    private static XSSFConditionalFormattingRule createConditionalFormattingRuleIconSets(SheetConditionalFormatting sheetConditionalFormatting,JSONObject format) throws Exception {
  	  Field _sheet = XSSFSheetConditionalFormatting.class.getDeclaredField("_sheet");
  	  _sheet.setAccessible(true);
  	  XSSFSheet sheet = (XSSFSheet)_sheet.get(sheetConditionalFormatting);
  	  Constructor constructor = XSSFConditionalFormattingRule.class.getDeclaredConstructor(XSSFSheet.class);
  	  constructor.setAccessible(true);
  	  XSSFConditionalFormattingRule rule = (XSSFConditionalFormattingRule)constructor.newInstance(sheet);
  	  String top = format.getString("top");
  	  String leftMin = format.getString("leftMin");
  	  if("0".equals(top)) {
  		  if("0".equals(leftMin)) {
  			rule.createMultiStateFormatting(IconSet.GYR_3_ARROW); 
  		  }else if("5".equals(leftMin)) {
  			rule.createMultiStateFormatting(IconSet.GREY_3_ARROWS); 
  		  }
  	  } else if("1".equals(top)) {
  		  if("5".equals(leftMin)) {
  			rule.createMultiStateFormatting(IconSet.GREY_4_ARROWS); 
  		  }else if("0".equals(leftMin)){
  			  //3个三角形不支持，使用三色交通灯GYR_3_TRAFFIC_LIGHTS代替
  			rule.createMultiStateFormatting(IconSet.GYR_3_TRAFFIC_LIGHTS); 
  		  }
  	  }else if("2".equals(top)) {
  		if("0".equals(leftMin)) {
  			rule.createMultiStateFormatting(IconSet.GYR_4_ARROWS); 
  		}else if("5".equals(leftMin)) {
  			rule.createMultiStateFormatting(IconSet.GREY_5_ARROWS); 	
  		}
  	  }else if("3".equals(top)) {
  		 if("0".equals(leftMin)) {
  			rule.createMultiStateFormatting(IconSet.GYYYR_5_ARROWS); 
  		 }
  	  }else if("4".equals(top)) {
   		 if("0".equals(leftMin)) {
   			rule.createMultiStateFormatting(IconSet.GYR_3_TRAFFIC_LIGHTS); 
   		 }else if("5".equals(leftMin)) {
   			rule.createMultiStateFormatting(IconSet.GYR_3_TRAFFIC_LIGHTS_BOX); 	
   		 }
   	  }else if("5".equals(top)) {
   		 if("0".equals(leftMin)) {
   			rule.createMultiStateFormatting(IconSet.GYR_3_SHAPES); 
   		 }else if("5".equals(leftMin)) {
    		rule.createMultiStateFormatting(IconSet.GYRB_4_TRAFFIC_LIGHTS); 	
    	 }
   	  }else if("6".equals(top)) {
    	if("0".equals(leftMin)) {
    	   	rule.createMultiStateFormatting(IconSet.RB_4_TRAFFIC_LIGHTS); 
    	}else if("5".equals(leftMin)) {
    	}
     }else if("7".equals(top)) {
    	if("0".equals(leftMin)) {
    	   	rule.createMultiStateFormatting(IconSet.GYR_3_SYMBOLS_CIRCLE); 
    	}else if("5".equals(leftMin)) {
    		rule.createMultiStateFormatting(IconSet.GYR_3_SYMBOLS); 
    	}
     }else if("8".equals(top)) {
    	if("0".equals(leftMin)) {
    	   	rule.createMultiStateFormatting(IconSet.GYR_3_FLAGS); 
    	}else if("5".equals(leftMin)) {
    	}
     }else if("9".equals(top)) {
    	if("0".equals(leftMin)) {
    		//3个星星不支持，使用三色交通灯GYR_3_TRAFFIC_LIGHTS代替
    		rule.createMultiStateFormatting(IconSet.GYR_3_TRAFFIC_LIGHTS); 
    	}else if("5".equals(leftMin)) {
    		rule.createMultiStateFormatting(IconSet.RATINGS_4); 
    	}
     }else if("10".equals(top)) {
     	if("0".equals(leftMin)) {
     		rule.createMultiStateFormatting(IconSet.QUARTERS_5); 
     	}else if("5".equals(leftMin)) {
     		rule.createMultiStateFormatting(IconSet.RATINGS_5); 
     	}
     }else if("11".equals(top)) {
     	if("0".equals(leftMin)) {
     		//5个框不支持，使用QUARTERS_5代替
     		rule.createMultiStateFormatting(IconSet.QUARTERS_5); 
     	}else if("5".equals(leftMin)) {
     	}
     }
  	  Field _cfRule = XSSFConditionalFormattingRule.class.getDeclaredField("_cfRule");
  	  _cfRule.setAccessible(true);
  	  CTCfRule cfRule = (CTCfRule)_cfRule.get(rule);
  	  cfRule.setType(STCfType.ICON_SET);
  	  return rule;
  }
    
    private static XSSFConditionalFormattingRule createConditionalFormattingRuleRank(SheetConditionalFormatting sheetConditionalFormatting,String conditionName,JSONArray conditionValue) throws Exception {
    	Field _sheet = XSSFSheetConditionalFormatting.class.getDeclaredField("_sheet");
  	  _sheet.setAccessible(true);
  	  XSSFSheet sheet = (XSSFSheet)_sheet.get(sheetConditionalFormatting);
  	  Constructor constructor = XSSFConditionalFormattingRule.class.getDeclaredConstructor(XSSFSheet.class);
  	  constructor.setAccessible(true);
  	  XSSFConditionalFormattingRule rule = (XSSFConditionalFormattingRule)constructor.newInstance(sheet);
  	  Field _cfRule = XSSFConditionalFormattingRule.class.getDeclaredField("_cfRule");
  	  _cfRule.setAccessible(true);
  	  CTCfRule cfRule = (CTCfRule)_cfRule.get(rule);
  	  if("top10".equals(conditionName)) {
  		cfRule.setType(STCfType.TOP_10);
    	cfRule.setRank(conditionValue.getLongValue(0));  
  	  }else if("top10%".equals(conditionName)) {
    	cfRule.setType(STCfType.TOP_10);
        cfRule.setRank(conditionValue.getLongValue(0));  
        cfRule.setPercent(true);
      }else if("last10".equals(conditionName)) {
    	cfRule.setType(STCfType.TOP_10);
        cfRule.setRank(conditionValue.getLongValue(0));  
        cfRule.setBottom(true);
      }else if("last10%".equals(conditionName)) {
    	cfRule.setType(STCfType.TOP_10);
        cfRule.setRank(conditionValue.getLongValue(0));  
        cfRule.setBottom(true);
        cfRule.setPercent(true);
      }else if("AboveAverage".equals(conditionName)) {
    	cfRule.setType(STCfType.ABOVE_AVERAGE);
      }else if("SubAverage".equals(conditionName)) {
    	cfRule.setType(STCfType.ABOVE_AVERAGE);
    	cfRule.setAboveAverage(false);
      }
  	  return rule;
    }
}
