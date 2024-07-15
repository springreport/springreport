package com.springreport.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.util.CellReference;

public class SheetUtil {


	 public static void main(String[] args) {
//	        System.out.println(excelColStrToNum("A"));
//	        System.out.println(excelColIndexToStr(10));
//		 System.out.println(StringUtil.getNumeric("1C2A"));
		 int row = 1;
	        int column = 1;
	        String coordinate = convertToExcelCoordinate(row, column);
	        System.out.println("Excel坐标：" + coordinate);

	        // 从Excel坐标转换
	        int[] coordinates = convertFromExcelCoordinate(coordinate);
	        System.out.println("行：" + coordinates[0]);
	        System.out.println("列：" + coordinates[1]);
	    }
	 
	 /**  
	 * @MethodName: getColumnFlag
	 * @Description: 获取列号
	 * @author caiyang
	 * @param coords
	 * @return 
	 * @return String
	 * @date 2023-04-18 02:26:10 
	 */  
	public static String getColumnFlag(String coords)
	{
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(coords);
		String result = m.replaceAll("").trim();
		coords = coords.replace(result, "");
		return coords;
	 }
	
	public static int getRowNum(String coords)
	{
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(coords);
		String result = m.replaceAll("").trim();
		return Integer.parseInt(result);
	}
	 
	/**
	     * 字母转数字
	     * @param column:ABCD列名称
	     * @return integer：将字母列名称转换成数字
	     * **/
	    public static int excelColStrToNum(String column) {
	        int num = 0;
	        int result = 0;
	        int length =column.length();
	        for(int i = 0; i < length; i++) {
	            char ch = column.charAt(length - i - 1);
	            num = (int)(ch - 'A' + 1) ;
	            num *= Math.pow(26, i);
	            result += num;
	        }
	        return result;
	    }

	    /**
	     * 数字转字母
	     * @param ：需要转换成字母的数字
	     * @return column:ABCD列名称
	     * **/
	    public static String excelColIndexToStr(int columnIndex) {
	        if (columnIndex <= 0) {
	            return null;
	        }
	        String columnStr = "";
	        columnIndex--;
	        do {
	            if (columnStr.length() > 0) {
	                columnIndex--;
	            }
	            columnStr = ((char) (columnIndex % 26 + (int) 'A')) + columnStr;
	            columnIndex = (int) ((columnIndex - columnIndex % 26) / 26);
	        } while (columnIndex > 0);
	        return columnStr;
	    }
	    
	    public static List<String> getRangeCells(int firstRow,int lastRow,int firstColumn,int lastColumn)
	    {
	    	List<String> result = new ArrayList<String>();
	    	for (int i = firstRow; i <= lastRow; i++) {
				for (int j = firstColumn; j <= lastColumn; j++) {
					String cell = i+"_"+j;
					result.add(cell);
				}
			}
	    	
	    	return result;
	    }
	    
	    public static String convertToExcelCoordinate(int row, int column) {
	        CellReference cellReference = new CellReference(row - 1, column - 1);
	        return cellReference.formatAsString();
	    }

	    public static int[] convertFromExcelCoordinate(String coordinate) {
	        CellReference cellReference = new CellReference(coordinate);
	        int row = cellReference.getRow() + 1;
	        int column = cellReference.getCol() + 1;
	        return new int[]{row, column};
	    }
}
