package com.springreport.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 5
 * Date: 17-3-27
 * Time: 上午9:48
 * To change this template use File | Settings | File Templates.
 * @author Administrator
 */
public class JsonUtil {
    static final Gson GSON=new Gson();
    static final Gson GSONData=new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    static final Gson GSONDataTime=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static String toJson(Object o){
        return GSON.toJson(o);
    }
    public static String toJsonByDate(Object o){
        return GSONData.toJson(o);
    }
    public static String toJsonByDateTime(Object o){
        return GSONDataTime.toJson(o);
    }

    public static void main(String[]args){
        String str="{'content':{'data':{'reportType':1,'t':'mv','v':[{'top_move':142,'top':142,'left':222,'column_focus':3,'width':73,'left_move':222,'column':[3,3],'width_move':73,'row':[7,7],'height_move':19,'row_focus':7,'height':19}],'isReport':false,'i':'Sheet7041b853dafc45068f4ddbc862f743c9'},'createTime':'1714123941814','returnMessage':'success','id':'b740afa7-c9bd-48ac-ef78-49d647e9d56b','type':3,'username':'游客_749942','status':'0'},'gridkey':'81a4c868f58545a8aa35c3b6dfdcf0b1','ipandport':'192.168.31.187:9099','sendSelf':false}";
        Map<String,Date> map=GSONData.fromJson(str,Map.class);


    }


}
