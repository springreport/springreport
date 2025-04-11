package com.springreport.util;


import java.lang.reflect.Field;
import java.util.*;

/**
 * 行转列终极工具类，通用于查询单个列或者多个列的结果
 *
 * @author QiJingJing
 */
public class RowConvertColUtil {
    private static Set<Object> headerSet;
    private static Set<Object> fixedColumnSet;

    private RowConvertColUtil() {
    }

    public static class ConvertData {
        private Set<Object> headerSet;
        private Set<Object> fixedColumnSet;
        private List<Map<String, Object>> dataList;

        public ConvertData(List<Map<String, Object>> dataList, Set<Object> headerSet, Set<Object> fixedColumnSet) {
            this.headerSet = headerSet;
            this.fixedColumnSet = fixedColumnSet;
            this.dataList = dataList;
        }

        public Set<Object> getHeaderSet() {
            return headerSet;
        }

        public void setHeaderSet(Set<Object> headerSet) {
            this.headerSet = headerSet;
        }

        public Set<Object> getFixedColumnSet() {
            return fixedColumnSet;
        }

        public void setFixedColumnSet(Set<Object> fixedColumnSet) {
            this.fixedColumnSet = fixedColumnSet;
        }

        public List<Map<String, Object>> getDataList() {
            return dataList;
        }

        public void setDataList(List<Map<String, Object>> dataList) {
            this.dataList = dataList;
        }
    }

    /**
     * 行转列返回 ConvertData 我们想要展示的格式
     *
     * @param orignalList     要行转列的list
     * @param headerName      要行转列的字段
     * @param fixedColumn     固定需要查询列字段
     * @param valueFiedName   行转列字段对应值列的字段名
     * @param needHeader      是否返回表头
     * @param fixedColumnName 固定需要查询列字段的昵称
     * @param nullValue       空值填充
     **/
    public static synchronized ConvertData doConvertReturnObj(List<Map<String, Object>> orignalList, String headerName, String[] fixedColumn, String valueFiedName, boolean needHeader, String[] fixedColumnName, String nullValue) throws Exception {
        List<List<Object>> lists = doConvert(orignalList, headerName, fixedColumn, valueFiedName, needHeader, fixedColumnName, nullValue);
        // 拿到每个列表需要的属性个数
        int size = lists.get(0).size();
        // 拿出总共的集合数量
        int dataListNum = lists.size() - 1;
        // 将固定字段和固定字段值做kv映射
        Map<String, String> columnMap = new HashMap<>(16);
        for (int i = 0; i < fixedColumn.length; i++) {
            columnMap.put(fixedColumnName[i], fixedColumn[i]);
        }
        // 对lists里面的数据做转换，转换成原本类的格式（一个属性对应一个值的形式）
        List<Map<String, Object>> maps = new ArrayList<>();
        for (int i = 0; i < dataListNum; i++) {
            Map<String, Object> map = new LinkedHashMap<>(16);
            for (int j = 0; j < size; j++) {
                // 列的表头昵称
                String columnName = String.valueOf(lists.get(0).get(j));
                if (fixedColumn.length > j) {
                    // 根据昵称，拿到属性名，然后将下一个列表的对应值加进去
                    map.put(columnMap.get(columnName), lists.get(i + 1).get(j));
                } else {
                    map.put(columnName, lists.get(i + 1).get(j));
                }
            }
            maps.add(map);
        }
        return new ConvertData(maps, headerSet, fixedColumnSet);
    }

    /**
     * 列表行转列的最终结果
     *
     * @param orignalList     要行转列的list
     * @param headerName      要行转列的字段
     * @param fixedColumn     固定需要查询列字段
     * @param valueFiedName   行转列字段对应值列的字段名
     * @param needHeader      是否返回表头
     * @param fixedColumnName 固定需要查询列字段的昵称
     * @param nullValue       空值填充
     **/
    public static synchronized List<List<Object>> doConvert(List<Map<String, Object>> orignalList, String headerName, String[] fixedColumn, String valueFiedName, boolean needHeader, String[] fixedColumnName, String nullValue) throws Exception {
        // 行转列的字段表头
        headerSet = new LinkedHashSet<>();
        // 固定列的值的集合
        fixedColumnSet = new LinkedHashSet<>();
        // 首行完整固定表头list
        List<List<Object>> resultList = new ArrayList<>();
        // 获取headerSet和fixedColumnSet
        getHeaderfixedColumnSet(orignalList, headerName, fixedColumn);
        if (needHeader) {
            List<Object> headerList = new ArrayList<>();
            //填充进header
            headerList.addAll(Arrays.asList(fixedColumnName));
            headerList.addAll(headerSet);
            resultList.add(headerList);
        }
        // 遍历固定列的值
        for (Object fixedColumnItem : fixedColumnSet) {
            // 每个固定列的值加入集合的前几个固定位置
            List<Object> colList = new ArrayList<>(Arrays.asList(fixedColumnItem.toString().split("\\|")));
            // 遍历表头
            for (Object headerItem : headerSet) {
                boolean flag = true;
                for (Map<String, Object> orignalObjectItem : orignalList) {
//                    Field headerField = orignalObjectItem.getClass().getDeclaredField(headerName);
//                    headerField.setAccessible(true);
                    // 如果表头一样
                    if (headerItem.equals(orignalObjectItem.get(headerName))) {
                        boolean flg = true;
//                        Field fixedColumnField;
//                        Field valueField = orignalObjectItem.getClass().getDeclaredField(valueFiedName);
//                        valueField.setAccessible(true);
                        // 判断当前列是否于固定列的所有值都一样
                        for (int i = 0; i < fixedColumn.length; i++) {
//                            fixedColumnField = orignalObjectItem.getClass().getDeclaredField(fixedColumn[i]);
//                            fixedColumnField.setAccessible(true);
                            if (!fixedColumnItem.toString().split("\\|")[i].equals(String.valueOf(orignalObjectItem.get(fixedColumn[i])))) {
                                flg = false;
                            }
                        }
                        if (flg) {
                            // 如果一样的话，则将需要行转列的表头加入进来
                            colList.add(orignalObjectItem.get(valueFiedName));
                            flag = false;
                            break;
                        }
                    }
                }
                if (flag) {
                    // 反之，加入你自定义的代替值
                    colList.add(nullValue);
                }
            }
            // 加入集合
            resultList.add(colList);
        }
        return resultList;
    }

    private static void getHeaderfixedColumnSet(List<Map<String, Object>> orignalList, String headerName, String[] fixedColumn) {
        for (Map<String, Object> item : orignalList) {
		    // 拿到list中每一列的行转列字段信息
//                Field headerField = item.getClass().getDeclaredField(headerName);
//                headerField.setAccessible(true);
		    // 将值作为表头加入headerSet
		    headerSet.add(item.get(headerName));
		    StringBuilder sBuffer = new StringBuilder();
		    int len = 1;
		    for (String name : fixedColumn) {
//                    Field fixedColumnField = item.getClass().getDeclaredField(name);
//                    fixedColumnField.setAccessible(true);
		        // 添加每个列表固定列的值
		        sBuffer.append(item.get(name));
		        if (len < fixedColumn.length) {
		            // 如果有多个固定列的话，则值用|隔开
		            sBuffer.append("|");
		        }
		        len++;
		    }
		    // 加入固定表头值集合
		    fixedColumnSet.add(sBuffer.toString());
		}
    }
}
