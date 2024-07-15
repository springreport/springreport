package com.springreport.mapper.luckysheetonlinecell;
import com.springreport.entity.luckysheetonlinecell.LuckysheetOnlineCell;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: LuckysheetOnlineCellMapper类
* @author 
* @date 2023-02-06 08:03:17
* @version V1.0  
 */
public interface LuckysheetOnlineCellMapper extends BaseMapper<LuckysheetOnlineCell>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<LuckysheetOnlineCell> searchDataLike(final LuckysheetOnlineCell model);
    
    /**  
     * @MethodName: updateAddr
     * @Description: 插入行更新横坐标
     * @author caiyang
     * @param sheetId
     * @param index
     * @param rows 
     * @return void
     * @date 2023-02-16 03:17:51 
     */  
    void updateAddr(@Param("sheetId") long sheetId,@Param("index") int index, @Param("rows") int rows,@Param("direction") String direction);
    
    
    /**  
     * @MethodName: updateAddc
     * @Description: 插入列更新纵坐标
     * @author caiyang
     * @param sheetId
     * @param index
     * @param rows
     * @param direction 
     * @return void
     * @date 2023-02-16 03:26:23 
     */  
    void updateAddc(@Param("sheetId") long sheetId,@Param("index") int index, @Param("rows") int rows,@Param("direction") String direction);
    
    /**  
     * @MethodName: updateMinusr
     * @Description: 删除行更新纵坐标
     * @author caiyang
     * @param sheetId
     * @param index
     * @param rows
     * @param direction 
     * @return void
     * @date 2023-02-16 04:08:45 
     */  
    void updateMinusr(@Param("sheetId") long sheetId,@Param("index") int index, @Param("rows") int rows);
    
    /**  
     * @MethodName: deleteCellsByDelRows
     * @Description: 根据删除的行删除对应的单元格
     * @author caiyang
     * @param sheetId
     * @param index
     * @param rows 
     * @return void
     * @date 2023-02-16 04:18:25 
     */  
    void deleteCellsByDelRows(@Param("sheetId") long sheetId,@Param("index") int index, @Param("rows") int rows);
    
    /**  
     * @MethodName: updateMinusc
     * @Description: 删除行更新纵坐标
     * @author caiyang
     * @param sheetId
     * @param index
     * @param rows
     * @param direction 
     * @return void
     * @date 2023-02-16 04:09:10 
     */  
    void updateMinusc(@Param("sheetId") long sheetId,@Param("index") int index, @Param("rows") int rows);
    
    /**  
     * @MethodName: deleteCellsByDelCols
     * @Description: 根据删除的列删除对应的单元格
     * @author caiyang
     * @param sheetId
     * @param index
     * @param rows 
     * @return void
     * @date 2023-02-16 04:18:51 
     */  
    void deleteCellsByDelCols(@Param("sheetId") long sheetId,@Param("index") int index, @Param("rows") int rows);
}
