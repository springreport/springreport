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
	    
	    /**  
	     * @MethodName: calculateFormula
	     * @Description: 计算公式
	     * @author caiyang
	     * @param formula 公式
	     * @param num 移动的行数或者列数
	     * @param type 1横向 2纵向
	     * @return String
	     * @date 2024-08-13 11:57:48 
	     */ 
	    public static String calculateFormula(String formula,int num,int type) {
	    	 // 定义一个字符串缓冲区，用来存储修改后的公式
	        StringBuffer sb = new StringBuffer();
	 
	        // 定义一个正则表达式，用来匹配公式中的列引用，如A1，B2等
	        String regex = "([A-Z]+)(\\d+)";
	 
	        // 定义一个模式，用来编译正则表达式
	        Pattern pattern = Pattern.compile(regex);
	 
	        // 定义一个匹配器，用来匹配公式
	        Matcher matcher = pattern.matcher(formula);
	        // 遍历公式中的每一个匹配项
	        while (matcher.find()) {
	        	// 获取匹配项的内容，如A1，B2等
	            String match = matcher.group();
	            // 获取匹配项中的列引用，如A，B等
	            String colRef = matcher.group(1);
	            // 获取匹配项中的行引用，如1，2等
	            String rowRef = matcher.group(2);
	            if(type == 1) {
	            	//横向扩展
	            	int colNumRef = colNameToNumber(colRef);
	            	// 将列数字加上移动的位置，得到新的列数字
	                int newColNumRef = colNumRef + num;
	                // 将新的列数字转换为列引用，如1为A，2为B等
	                String newColRef = colNumberToName(newColNumRef);
	                // 将新的列引用和原来的行引用拼接起来，得到新的匹配项，如A1变为F1，B2变为G2等
	                String newMatch = newColRef + rowRef;
	                // 将原来的匹配项替换为新的匹配项
	                matcher.appendReplacement(sb, newMatch);
	            }else {
	            	//纵向扩展
	            	String newRowref = String.valueOf(Integer.parseInt(rowRef)+num);
	            	//将原来的列引用和新的行拼接起来，得到新的匹配项，如A1变为A3
	            	String newMatch = colRef+newRowref;
	            	// 将原来的匹配项替换为新的匹配项
	                matcher.appendReplacement(sb, newMatch);
	            }
	        }
	       // 将剩余的部分添加到字符串缓冲区
	        matcher.appendTail(sb);
	 
	        // 返回修改后的公式
	        return sb.toString();
	    }
	    
	    /**
	     * 自定义的方法，用来将列引用转换为数字，如A为1，B为2等
	     * @param colName  列引用
	     * @return
	     */
	    public static int colNameToNumber(String colName) {
	        // 定义一个变量，用来存储列数字
	        int colNum = 0;
	 
	        // 遍历列引用中的每一个字符
	        for (int i = 0; i < colName.length(); i++) {
	            // 获取当前字符，如A，B等
	            char c = colName.charAt(i);
	 
	            // 将当前字符转换为数字，如A为1，B为2等
	            int n = c - 'A' + 1;
	 
	            // 将列数字乘以26，再加上当前字符的数字，得到新的列数字
	            colNum = colNum * 26 + n;
	        }
	 
	        // 返回列数字
	        return colNum;
	    }

		
		/**
	     * 自定义的方法，用来将数字转换为列引用，如1为A，2为B等
	     * @param colNum
	     * @return
	     */
	    public static String colNumberToName(int colNum) {
	        // 定义一个字符串缓冲区，用来存储列引用
	        StringBuffer sb = new StringBuffer();
	 
	        // 当列数字大于0时，循环执行
	        while (colNum > 0) {
	            // 将列数字减去1，得到新的列数字
	            colNum--;
	 
	            // 将列数字除以26，得到商和余数
	            int quotient = colNum / 26;
	            int remainder = colNum % 26;
	 
	            // 将余数转换为字符，如0为A，1为B等
	            char c = (char) (remainder + 'A');
	 
	            // 将字符插入到字符串缓冲区的最前面
	            sb.insert(0, c);
	 
	            // 将商赋值给列数字，继续循环
	            colNum = quotient;
	        }
	 
	        // 返回列引用
	        return sb.toString();
	    }
}
