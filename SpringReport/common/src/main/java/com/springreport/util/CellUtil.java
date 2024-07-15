package com.springreport.util;

import java.awt.Color;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

import com.springreport.base.CellValueDto;
import com.springreport.enums.YesNoEnum;

/**  
 * @ClassName: CellUtil
 * @Description: 单元格工具类
 * @author caiyang
 * @date 2021-06-09 06:13:14 
*/  
public class CellUtil {

	private SXSSFWorkbook wb = null;

    private SXSSFSheet sheet = null;

    /**
     * @param wb
     * @param sheet
     */
    public CellUtil(SXSSFWorkbook wb, SXSSFSheet sheet) {
        this.wb = wb;
        this.sheet = sheet;
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
	public void createCells(int maxX,int maxY)
	{
		for (int i = 0; i <= maxX; i++) {
			SXSSFRow row = sheet.createRow(i);
			for (int j = 0; j <= maxY; j++) {
				row.createCell(j);
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
	public void setCellValues(List<CellValueDto> cellValues)
	{
		for (int i = 0; i < cellValues.size(); i++) {
			Cell cell = this.getCell(cellValues.get(i).getStartRow(), cellValues.get(i).getStartColumn());
			CellStyle cellStyle = this.getCellStyle(cellValues.get(i));
			cell.setCellStyle(cellStyle);
			cell.setCellValue(String.valueOf(cellValues.get(i).getData()));
			if(YesNoEnum.YES.getCode().intValue() == cellValues.get(i).getIsMerge().intValue())
			{//合并单元格
				this.mergeCell(cellValues.get(i).getStartRow(), cellValues.get(i).getEndRow(), cellValues.get(i).getStartColumn(), cellValues.get(i).getEndColumn());
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
		SXSSFRow row = sheet.getRow(rowIndex);
		Cell cell = row.getCell(colIndex);
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
	private void mergeCell(int firstRow, int lastRow, int firstCol, int lastCol) {
		CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		sheet.addMergedRegion(region);
	}
	
	private CellStyle getCellStyle(CellValueDto cellValueDto) {
		// 创建单元格样式
		XSSFCellStyle cellStyle = (XSSFCellStyle) wb.createCellStyle();
        // 创建单元格内容显示不下时自动换行
//        cellStyle.setWrapText(true);
        // 设置单元格字体样式
        XSSFFont font = (XSSFFont) wb.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeight((short) 220);
        if(StringUtil.isNotEmpty(cellValueDto.getCellClassname()) && cellValueDto.getCellClassname().contains("htCenter"))
        {
        	cellStyle.setAlignment(HorizontalAlignment.CENTER);
        }else if(StringUtil.isNotEmpty(cellValueDto.getCellClassname()) && cellValueDto.getCellClassname().contains("htLeft")) {
        	cellStyle.setAlignment(HorizontalAlignment.LEFT);
        }
        else if(StringUtil.isNotEmpty(cellValueDto.getCellClassname()) && cellValueDto.getCellClassname().contains("htRight")) {
        	cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        }else {
        	cellStyle.setAlignment(HorizontalAlignment.LEFT);
        }
        if(StringUtil.isNotEmpty(cellValueDto.getCellClassname()) && cellValueDto.getCellClassname().contains("htMiddle"))
        {
        	cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        }else if(StringUtil.isNotEmpty(cellValueDto.getCellClassname()) && cellValueDto.getCellClassname().contains("htTop"))
        {
        	cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        }
        else if(StringUtil.isNotEmpty(cellValueDto.getCellClassname()) && cellValueDto.getCellClassname().contains("htBottom"))
        {
        	cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
        }else {
        	cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        }
        if(YesNoEnum.YES.getCode().intValue() == cellValueDto.getIsBold().intValue())
        {
        	// 设置字体加粗
	        font.setBold(true);
        }
        //设置字体颜色
        if(StringUtil.isNotEmpty(cellValueDto.getFontColor()))
        {
        	if ("#000000".equals(cellValueDto.getFontColor())) {
				font.setColor(IndexedColors.BLACK.index);
			}else {
				try {
					int[] rgb = StringUtil.hexToRgb(cellValueDto.getFontColor());
			        font.setColor(new XSSFColor(new Color(rgb[0], rgb[1], rgb[2]),new DefaultIndexedColorMap()));
				} catch (Exception e) {
					font.setColor(IndexedColors.BLACK.index);
				}
			}
        }
        //设置字体大小
        if(StringUtil.isNotEmpty(cellValueDto.getFontSize()))
        {
        	font.setFontHeightInPoints(Short.parseShort(cellValueDto.getFontSize()));
        }
        //设置背景颜色
        if(StringUtil.isNotEmpty(cellValueDto.getBackColor()) && !"#FFFFFF".equals(cellValueDto.getBackColor()))
        {
        	int[] rgb = StringUtil.hexToRgb(cellValueDto.getBackColor());
        	cellStyle.setFillForegroundColor(new XSSFColor(new Color(rgb[0], rgb[1], rgb[2]),new DefaultIndexedColorMap()));
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        
        cellStyle.setFont(font);
        return cellStyle;
	}
	
}
