package com.springreport.mapper.mqfailedmsg;
import com.springreport.entity.mqfailedmsg.MqFailedMsg;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

 /**  
* @Description: MqFailedMsgMapper类
* @author 
* @date 2023-12-22 10:38:34
* @version V1.0  
 */
public interface MqFailedMsgMapper extends BaseMapper<MqFailedMsg>{

    /**
     * 通过条件，查询数据集合，返回分页数据，字符串参数模糊查询
     *
     * @param model 包含查询条件的对象实体
     * @return 实体集合
     */
    List<MqFailedMsg> searchDataLike(final MqFailedMsg model);
}
