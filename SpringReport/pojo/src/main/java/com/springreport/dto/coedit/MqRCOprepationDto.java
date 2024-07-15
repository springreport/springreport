package com.springreport.dto.coedit;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.Data;

/**  
 * @ClassName: MqRCOprepationDto
 * @Description: 删除添加行列用实体类
 * @author caiyang
 * @date 2023-10-23 01:49:38 
*/ 
@Data
public class MqRCOprepationDto {

	/**  
	 * @Fields rc : 行列 r行 c列
	 * @author caiyang
	 * @date 2023-10-23 01:50:21 
	 */  
	private String rc;
	
	/**  
	 * @Fields direction : 方向 lefttop：向上插入行，向左插入列 rightbottom:向下插入行，向右插入列
	 * @author caiyang
	 * @date 2023-10-27 01:19:47 
	 */  
	private String direction;
	
	/**  
	 * @Fields index : 操作起始行/列
	 * @author caiyang
	 * @date 2023-10-23 01:51:18 
	 */  
	private Integer index; 
	
	/**  
	 * @Fields len : 操作行/列数
	 * @author caiyang
	 * @date 2023-10-23 01:51:29 
	 */  
	private int len;
	
	/**  
	 * @Fields row : 总行数
	 * @author caiyang
	 * @date 2023-10-23 02:39:39 
	 */  
	private int row;
	
	/**  
	 * @Fields column : 总列数
	 * @author caiyang
	 * @date 2023-10-23 02:39:25 
	 */  
	private int column;
	
	/**  
	 * @Fields delKeys : 需要删除的缓存单元格key集合
	 * @author caiyang
	 * @date 2023-10-23 02:01:02 
	 */  
//	private  List<String> delKeys;
	
	/**  
	 * @Fields delIds : 需要删除的数据库单元格id集合
	 * @author caiyang
	 * @date 2023-10-23 02:02:21 
	 */  
//	private List<Long> delIds;
	
	/**  
	 * @Fields mergeCells : 需要修改的合并单元格信息单元格
	 * @author caiyang
	 * @date 2023-10-23 02:20:38 
	 */  
	private JSONObject mergeCells;
	
	/**  
	 * @Fields updateFuncCell : 需要修改的公式单元格数据
	 * @author caiyang
	 * @date 2023-10-27 10:29:52 
	 */  
	private JSONObject updateFuncCell;
	
	/**  
	 * @Fields data : 
	 * @author caiyang
	 * @date 2023-10-27 01:28:50 
	 */  
	private JSONArray data;
	
}
