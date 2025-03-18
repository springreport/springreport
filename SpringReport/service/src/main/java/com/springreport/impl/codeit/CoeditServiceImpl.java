package com.springreport.impl.codeit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springreport.api.coedit.ICoeditService;
import com.springreport.api.luckysheet.ILuckysheetService;
import com.springreport.api.luckysheetcell.ILuckysheetCellService;
import com.springreport.api.reportrangeauth.IReportRangeAuthService;
import com.springreport.api.reportrangeauthuser.IReportRangeAuthUserService;
import com.springreport.base.BaseEntity;
import com.springreport.base.MesExportExcel;
import com.springreport.base.MesSheetConfig;
import com.springreport.base.UserInfoDto;
import com.springreport.constants.Constants;
import com.springreport.constants.StatusCode;
import com.springreport.dto.coedit.MesDownloadDto;
import com.springreport.dto.coedit.MqRCOprepationDto;
import com.springreport.dto.coedit.RangeValueDto;
import com.springreport.dto.coedit.ShareModeInfo;
import com.springreport.dto.coedit.WSUserModel;
import com.springreport.entity.luckysheet.Luckysheet;
import com.springreport.entity.luckysheetcell.LuckysheetCell;
import com.springreport.entity.onlinetpl.OnlineTpl;
import com.springreport.entity.reportrangeauth.ReportRangeAuth;
import com.springreport.entity.reportrangeauthuser.ReportRangeAuthUser;
import com.springreport.enums.DelFlagEnum;
import com.springreport.enums.MqTypeEnums;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.enums.SheetOperationEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;
import com.springreport.mapper.luckysheet.LuckysheetMapper;
import com.springreport.mapper.onlinetpl.OnlineTplMapper;
import com.springreport.util.GzipHandle;
import com.springreport.util.JfGridFileUtil;
import com.springreport.util.JsonUtil;
import com.springreport.util.ListUtil;
import com.springreport.util.MessageUtil;
import com.springreport.util.RedisUtil;
import com.springreport.util.ReportExcelUtil;
import com.springreport.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**  
 * @ClassName: CoeditServiceImpl
 * @Description: 协同编辑用服务
 * @author caiyang
 * @date 2023-08-12 06:24:41 
*/ 
@Slf4j
@Service
public class CoeditServiceImpl extends ServiceImpl<LuckysheetMapper, Luckysheet> implements ICoeditService{

	@Autowired
	private GridFileRedisCacheService redisService;
	
	@Autowired
	private OnlineTplMapper onlineTplMapper;
	
	@Autowired
	private RedisUtil redisUtil;
	
//	@Autowired
//	private ILuckysheetHisService iLuckysheetHisService;
	
	@Autowired
	protected HttpServletRequest httpServletRequest;
	
	@Autowired
	private ILuckysheetService iLuckysheetService;
	
	@Autowired
	private MqProcessService mqProcessService;
	
	@Autowired
	private ILuckysheetCellService iLuckysheetCellService;
	
	@Autowired
	private IReportRangeAuthUserService iReportRangeAuthUserService;
	
	/**  
	 * @MethodName: getDefaultByGridKey
	 * @Description: 获取表格数据 按gridKey获取,默认载入status为1
	 * @author caiyang
	 * @param listId
	 * @return
	 * @see com.springreport.api.coedit.ICoeditService#getDefaultByGridKey(java.lang.String)
	 * @date 2023-08-12 06:29:00 
	 */
	@Override
	public List<JSONObject> getDefaultByGridKey(String listId,String isLoadALL,UserInfoDto userInfoDto) {
		String blockId = this.getIdByListId(listId);
		List<String> keys = redisUtil.getKeys(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_");
		List<Object> list = redisUtil.multiGet(keys);
		int sheetOrder = -1;
		String activeIndex = "";
		if(ListUtil.isEmpty(list))
		{
			QueryWrapper<Luckysheet> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("list_id", listId);
			queryWrapper.eq("block_id", blockId);
			queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			queryWrapper.orderByAsc("sheet_order");
			queryWrapper.orderByAsc("id");
			List<Luckysheet> luckysheets = this.list(queryWrapper);
			for (int i = 0; i < luckysheets.size(); i++) {
				if(luckysheets.get(i).getHide().intValue() == 0) {
					if(sheetOrder == -1)
					{
						sheetOrder = luckysheets.get(i).getSheetOrder();
						activeIndex = luckysheets.get(i).getSheetIndex();
					}else {
						if(luckysheets.get(i).getSheetOrder().intValue()<sheetOrder)
						{
							sheetOrder = luckysheets.get(i).getSheetOrder();
							activeIndex = luckysheets.get(i).getSheetIndex();
						}
					}
				}
				this.mqProcessService.updateRedisConfigCache(MqTypeEnums.INITSHEETCONFIG.getCode(), JSONObject.toJSONString(luckysheets.get(i)), luckysheets.get(i).getSheetIndex(), listId, blockId,null,null);
			}
		}else {
			for (int i = 0; i < list.size(); i++) {
				Luckysheet redisCache = JSON.parseObject(String.valueOf(list.get(i)),Luckysheet.class);
				if(redisCache != null)
				{
					if(redisCache.getHide().intValue() == 0)
					{
						if(sheetOrder == -1)
						{
							sheetOrder = redisCache.getSheetOrder();
							activeIndex = redisCache.getSheetIndex();
						}else {
							if(redisCache.getSheetOrder().intValue()<sheetOrder)
							{
								sheetOrder = redisCache.getSheetOrder();
								activeIndex = redisCache.getSheetIndex();
							}
						}
					}
				}
				
			}
			
		}
		List<JSONObject> dbObject = this.getByGridKey_NOCelldata(listId, blockId);
		if(!ListUtil.isEmpty(dbObject))
		{
			for(int x=0;x<dbObject.size();x++){
				JSONObject _o = dbObject.get(x);
				String index=_o.get("index").toString();
				if(x == 0)
				{//获取当前是否是编辑模式
					String shareModeKey = RedisPrefixEnum.COEDITSHAREMODE.getCode()+listId;
					Object redisShareModeInfo = redisUtil.get(shareModeKey);
					if(redisShareModeInfo == null)
					{
						_o.put("shareMode", false);
					}else {
						_o.put("shareMode", true);	
						ShareModeInfo shareModeInfo = JSON.parseObject(redisShareModeInfo.toString(), ShareModeInfo.class);
						if(userInfoDto.getUserId() != null && shareModeInfo.getUserId()!=null && shareModeInfo.getUserId().longValue() == userInfoDto.getUserId().longValue())
						{
							_o.put("shareUser", true);	
						}else {
							_o.put("shareUser", false);	
						}
						_o.put("shareUserName", shareModeInfo.getUserName());	
						if(StringUtil.isNotEmpty(shareModeInfo.getSheetIndex())) {
							activeIndex = shareModeInfo.getSheetIndex();
						}
						if(shareModeInfo.getScrollLeft() != null)
						{
							_o.put("scrollLeft", shareModeInfo.getScrollLeft());	
						}else {
							_o.put("scrollLeft", 0);	
						}
						if(shareModeInfo.getScrollTop() != null)
						{
							_o.put("scrollTop", shareModeInfo.getScrollTop());	
						}else {
							_o.put("scrollTop", 0);	
						}
						if(!ListUtil.isEmpty(shareModeInfo.getRange()))
						{
							_o.put("range", shareModeInfo.getRange());	
						}
					}
				}
				if(activeIndex.equals(index)){
					//覆盖当前对象的数据信息
					List<Object> _celldata=getCelldataBlockMergeByGridKey(listId,index,blockId);
					_o.put("status",1);
                    _o.put("celldata",_celldata);
                    _o.put("load", "1");
				}else {
					if("1".equals(isLoadALL))
					{
						List<Object> _celldata=getCelldataBlockMergeByGridKey(listId,index,blockId);
	                    _o.put("celldata",_celldata);
	                    _o.put("load", "1");
					}
				}
				if(_o.containsKey("calcChain")){
                    Object calcChain=JfGridFileUtil.getObjectByIndex(_o, "calcChain");
                    _o.put("calcChain", calcChain);
                }
			}
		}
		redisUtil.set(RedisPrefixEnum.DOCUMENTCACHEFLAG.getCode()+listId, 1,Constants.LUCKYCACHETIME);
		return dbObject;
	}
	
	/**  
	 * @MethodName: getByGridKey_NOCelldata
	 * @Description: 按指定xls，sheet顺序返回整个xls结构
	 * 				 不返回celldata ,只获取信息块
	 * @author caiyang
	 * @param listId
	 * @return List<JSONObject>
	 * @date 2023-08-13 10:37:00 
	 */ 
	private List<JSONObject> getByGridKey_NOCelldata(String listId,String blockId)
	{
		List<JSONObject> result=new ArrayList<JSONObject>();
		QueryWrapper<Luckysheet> queryWrapper = new QueryWrapper<Luckysheet>();
		queryWrapper.eq("block_id", blockId);
		queryWrapper.eq("list_id", listId);
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		queryWrapper.orderByAsc("sheet_order");
		List<String> keys = redisUtil.getKeys(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId);
		List<Object> objs= redisUtil.multiGet(keys);
		List<Luckysheet> list = null;
		if(ListUtil.isEmpty(objs))
		{
			list= this.list(queryWrapper);
			if(!ListUtil.isEmpty(list))
			{
				for (int i = 0; i <list.size(); i++) {
					this.mqProcessService.updateRedisConfigCache(MqTypeEnums.INITSHEETCONFIG.getCode(), JSONObject.toJSONString(list.get(i)), list.get(i).getSheetIndex(), listId, blockId,null,null);
					JSONObject pgd = this.getSheetJsonData(list.get(i));
					result.add(pgd);
				}
			}
		}else {
			List<Luckysheet> luckysheets = new ArrayList<>();
			for (Object obj : objs) {
				Luckysheet luckysheet = JSON.parseObject(obj.toString(), Luckysheet.class);
				if(luckysheet != null)
				{
					luckysheets.add(luckysheet);
				}
			}
			luckysheets.sort((o1, o2) -> o1.getSheetOrder().compareTo(o2.getSheetOrder()));
			for (Luckysheet luckysheet : luckysheets) {
				JSONObject pgd = this.getSheetJsonData(luckysheet);
				result.add(pgd);
			}
		}
		return result;
	}
	
	private JSONObject getSheetJsonData(Luckysheet luckysheet) {
		JSONObject result = new JSONObject();
		result.put("row", luckysheet.getRowSize());
		result.put("column", luckysheet.getColumnSize());
		result.put("name", luckysheet.getSheetName());
		if(StringUtil.isNotEmpty(luckysheet.getColor()))
		{
			result.put("color", luckysheet.getColor());
		}
		result.put("index", luckysheet.getSheetIndex());
		result.put("hide", luckysheet.getHide());
		result.put("order", luckysheet.getSheetOrder());
		result.put("isPivotTable", false);
		if(StringUtil.isNotEmpty(luckysheet.getCalcchain()))
		{
			result.put("calcChain", JSON.parseArray(luckysheet.getCalcchain()));
		}
		if(StringUtil.isNotEmpty(luckysheet.getFilterSelect()))
		{
			result.put("filter_select", JSON.parseObject(luckysheet.getFilterSelect()));
		}
		if(StringUtil.isNotEmpty(luckysheet.getFilter()))
		{
			result.put("filter", JSON.parseObject(luckysheet.getFilter()));
		}
		if(StringUtil.isNotEmpty(luckysheet.getHyperlink()))
		{
			result.put("hyperlink", JSON.parseObject(luckysheet.getHyperlink()));
		}
		if(StringUtil.isNotEmpty(luckysheet.getFrozen()))
		{
			result.put("frozen", JSON.parseObject(luckysheet.getFrozen()));
		}
		if(StringUtil.isNotEmpty(luckysheet.getImage()))
		{
			result.put("images", JSON.parseObject(luckysheet.getImage()));
		}
		if(StringUtil.isNotEmpty(luckysheet.getDataverification()))
		{
			result.put("dataVerification", JSON.parseObject(luckysheet.getDataverification()));
		}
		if(StringUtil.isNotEmpty(luckysheet.getLuckysheetAlternateformatSave()))
		{
			result.put("luckysheet_alternateformat_save", JSON.parseArray(luckysheet.getLuckysheetAlternateformatSave()));
		}
		if(StringUtil.isNotEmpty(luckysheet.getLuckysheetConditionformatSave()))
		{
			result.put("luckysheet_conditionformat_save", JSON.parseArray(luckysheet.getLuckysheetConditionformatSave()));
		}
		if(StringUtil.isNotEmpty(luckysheet.getChart()))
		{
			result.put("chart", JSON.parseArray(luckysheet.getChart()));
		}
		JSONObject config = new JSONObject();
		if(StringUtil.isNotEmpty(luckysheet.getMergeInfo()))
		{
			config.put("merge", JSON.parseObject(luckysheet.getMergeInfo()));
		}
		if(StringUtil.isNotEmpty(luckysheet.getRowlen()))
		{
			config.put("rowlen", JSON.parseObject(luckysheet.getRowlen()));
		}
		if(StringUtil.isNotEmpty(luckysheet.getColumnlen()))
		{
			config.put("columnlen", JSON.parseObject(luckysheet.getColumnlen()));
		}
		if(StringUtil.isNotEmpty(luckysheet.getRowhidden()))
		{
			config.put("rowhidden", JSON.parseObject(luckysheet.getRowhidden()));
		}
		if(StringUtil.isNotEmpty(luckysheet.getColhidden()))
		{
			config.put("colhidden", JSON.parseObject(luckysheet.getColhidden()));
		}
		if(StringUtil.isNotEmpty(luckysheet.getBorderInfo()))
		{
			config.put("borderInfo", JSON.parseArray(luckysheet.getBorderInfo()));
		}
		if(StringUtil.isNotEmpty(luckysheet.getFilterrowhidden()))
		{
			config.put("filterrowhidden", JSON.parseObject(luckysheet.getFilterrowhidden()));
		}
		result.put("config", config);
		return result;
	}
	
	/**  
	 * @MethodName: getCelldataBlockMergeByGridKey
	 * @Description: 按list_id获取，返回指定sheet 当前sheet的全部分块数据（并合并）
	 * @author caiyang
	 * @param listId
	 * @param index
	 * @return JSONArray
	 * @date 2023-08-13 11:09:47 
	 */ 
	private List<Object> getCelldataBlockMergeByGridKey(String listId,String index,String blockId) {
		//每一个分块数据合并后的对象
        List<Object> _celldata = new ArrayList<>();
        List<String> keys = redisUtil.getKeys(RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index);
        _celldata= redisUtil.multiGet(keys);
        if(ListUtil.isEmpty(_celldata))
        {
        	_celldata = new ArrayList<>();
        	QueryWrapper<LuckysheetCell> queryWrapper = new QueryWrapper<LuckysheetCell>();
            queryWrapper.eq("list_id", listId);
            queryWrapper.eq("sheet_index", index);
            queryWrapper.eq("block_id", blockId);
            queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
            List<LuckysheetCell> list = this.iLuckysheetCellService.list(queryWrapper);
            Map<String, JSONObject> redisCellData = null;
            List<Map<String, JSONObject>> datas = new ArrayList<Map<String,JSONObject>>();
            if(!ListUtil.isEmpty(list))
            {
            	for (int i = 0; i < list.size(); i++) {
            		if(i == 0 || i%Constants.MQ_LIST_LIMIT_SIZE == 0)
    				{
    					Map<String, JSONObject> tempMap = new HashMap<String, JSONObject>();
    					redisCellData = tempMap;
    					datas.add(redisCellData);
    				}
                	JSONObject cellData = this.getCellData(list.get(i));
                	String key = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" + index + "_" +list.get(i).getRowNo()+ "_" + list.get(i).getColumnNo();
                	_celldata.add(cellData);
                	redisCellData.put(key, cellData);
                }
            	for (int i = 0; i < datas.size(); i++) {
            		this.mqProcessService.updateRedisBatchCellValueCache(MqTypeEnums.BATCHINSERTCELLDATA.getCode(), datas.get(i), index, listId, blockId,null,null,null);
				}
            }
        }
        return _celldata;
	}
	
	private JSONObject getCellData(LuckysheetCell luckysheetCell) {
		JSONObject result = JSON.parseObject(luckysheetCell.getCellData());
		result.put("r", luckysheetCell.getRowNo());
		result.put("c", luckysheetCell.getColumnNo());
		result.put("id", luckysheetCell.getId());
		return result;
	}

	/**
     * 获取指定的xls激活的sheet页的 返回index（控制块）
     *
     * @param listId
     * @return
     */
	@Override
	public String getFirstBlockIndexByGridKey(String listId,String blockId) {
		QueryWrapper<Luckysheet> queryWrapper = new QueryWrapper<Luckysheet>();
	    queryWrapper.eq("list_id", listId);
	    queryWrapper.eq("block_id", blockId);
	    queryWrapper.eq("status", 1);
	    queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
	    Luckysheet luckysheet = this.getOne(queryWrapper,false);
		return luckysheet.getSheetIndex();
	}

	/**  
	 * @MethodName: Operation_mv
	 * @Description: TODO(暂时还不知道这个方法具体是干嘛的)
	 * @author caiyang
	 * @param gridKey
	 * @param bson
	 * @see com.springreport.api.coedit.ICoeditService#Operation_mv(java.lang.String, com.alibaba.fastjson.JSONObject)
	 * @date 2023-08-13 08:20:50 
	 */
	@Override
	public void Operation_mv(String gridKey, JSONObject bson, WSUserModel wsUserModel) {
		new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String i = bson.get("i").toString();//	当前sheet的index值
                    String v = bson.get("v").toString();  //	单元格的值 v=null 删除单元格
                    log.info("Operation_mv---v" + v);
                    Object db = bson.get("v");
                    //更新操作（第一块）
            		String blockId = getIdByListId(gridKey);
                    JSONObject conditions = new JSONObject();
                    conditions.put("list_id",gridKey);
                    conditions.put("index",i);
                    conditions.put("block_id",blockId);
                    boolean _result = true;
                    if (!_result) {
                        log.info("更新失败");
                    }
                } catch (Exception e) {
                    log.warn(e.getMessage());
                } finally {

                }
            }
        }).start();
	}

	@Override
	public String updateRvDbContent(String gridKey, JSONObject bson, String key,WSUserModel wsUserModel) {
		List<JSONObject> bsons = redisService.rgetDbDataContent(key);
		loadRvMsgForLock(gridKey, bsons, key,wsUserModel);
		return "";
	}
	
	private void loadRvMsgForLock(String gridKey, List<JSONObject> bsons, String key,WSUserModel wsUserModel) {
		String blockId = getIdByListId(gridKey);
        for (JSONObject dbObject : bsons) {
            Operation_rv(gridKey, dbObject,blockId,wsUserModel);
        }
	}
	
	//3.1	批量单元格操作v
    private String Operation_rv(String gridKey, JSONObject bson,String blockId,WSUserModel wsUserModel) {
        if (GzipHandle.runGzip) {

            return "";
        }
        //不压缩处理
        try {
            log.info("start---Operation_bv" + bson.toString());
            String i = bson.get("i").toString();//	当前sheet的index值
            String operate = bson.getString("operate");
            JSONObject range = JfGridFileUtil.getJSONObjectByIndex(bson, "range");
            List columns = (List) range.get("column");
            List rows = (List) range.get("row");
            Integer r = Integer.parseInt(rows.get(0).toString());//	单元格的行号
            Integer c = Integer.parseInt(columns.get(0).toString());//	单元格的列号
            Object all = bson.get("v");  //	单元格的值 v=null 删除单元格

            //已存在的数据原始数据
            HashMap<String, JSONObject> originalExistsBlock = new HashMap<String, JSONObject>();
            //已存在的块
            HashMap<String, JSONObject> _existsBlock = new HashMap<String, JSONObject>();
            //不存在的块
            HashMap<String, JSONObject> _noExistsBlock = new HashMap<String, JSONObject>();
            JSONArray data = (JSONArray) all;
            List<String> keys = new ArrayList<>();
            Map<String, JSONObject> redisMapData = null;
            Map<String, JSONObject> changeBeforeData = null;
            Map<String, Long> blockIds = new HashMap<>();
            List<Long> delIds = new ArrayList<>();//需要删除的数据id集合
            int rl = r;
            for (int j=0; j<data.size(); j++) {
            	JSONArray arrayList=(JSONArray)data.get(j);
                int cl = c;
                for (Object v : arrayList) {
                	String block_id = JfGridConfigModel.getRange(rl, cl,null);
                	String key =RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +gridKey + "_" +i + "_" +block_id;
                	keys.add(key);
                	cl ++;
                }
                rl ++;
            }
            List<Object> redisDatas = redisUtil.multiGet(keys);
            if(!ListUtil.isEmpty(redisDatas))
            {
            	redisMapData = new HashMap<>();
            	changeBeforeData = new HashMap<>();
            	for (int k = 0; k < redisDatas.size(); k++) {
            		if(redisDatas.get(k) != null)
            		{
            			JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(redisDatas.get(k)));
            			JSONObject jsonObject2 = JSON.parseObject(JSON.toJSONString(redisDatas.get(k)));
    					String _r = jsonObject.getString("r");
    					String _c = jsonObject.getString("c");
    					Long id = jsonObject.getLongValue("id");
    					redisMapData.put(_r+"_"+_c, jsonObject);
    					changeBeforeData.put(_r+"_"+_c, jsonObject2);
    					blockIds.put(_r+"_"+_c, id);
            		}
				}
            }
            for (int j=0; j<data.size(); j++) {
                JSONArray arrayList=(JSONArray)data.get(j);
                int cl = c;
                for (int k = 0; k < arrayList.size(); k++) {
                	JSONObject v = arrayList.getJSONObject(k);
                    //获取数据所在块的编号
                    String block_id = JfGridConfigModel.getRange(r, cl,null);
                    boolean isExists = false;
                    JSONObject _dbObject = null;
                    if (_existsBlock.containsKey(block_id)) {
                        //db已存在的，处理成执行语句
                        isExists = true;
                        _dbObject = _existsBlock.get(block_id);
                    } else if (_noExistsBlock.containsKey(block_id)) {
                        //db不存在的
                        _dbObject = _noExistsBlock.get(block_id);
                    } else {
                        //已有的中不存在
                        //1、先获取原数据（直接获取到某个sheet）
                    	_dbObject = redisMapData.get(block_id);
                        if (_dbObject == null) {
                        	if(v != null)
                        	{
                                JSONObject db = new JSONObject();
                                _noExistsBlock.put(block_id, db);
                                _dbObject = db;
                        	}
                        } else {
                            //已存在
                            isExists = true;
                            _existsBlock.put(block_id, _dbObject);
                            originalExistsBlock.put(block_id, JSON.parseObject(JSON.toJSONString(_dbObject)));
                        }

                    }
                    //单元格处理，这一步对象已经获取
                    if (v != null) {
                    	v.remove("customKey");
                        //修改/添加
                        if (isExists) {
                            //已存在的
                        	_dbObject.put("v", v);
                        } else {
                            //假定页面提交不存在重复数据，不存在的添加
                            _dbObject.put("r", r);
                            _dbObject.put("c", cl);
                            _dbObject.put("v", v);
                        }
                    } else {
                        //删除
                        if (isExists) {
                        	_dbObject.clear();
                        }
                    }
                    cl++;
                }
                r++;
            }

            List<String> redisDelKeys = new ArrayList<>();//redis中需要删除的数据key集合
            Map<String, Object> cacheDatas = new HashMap<>();//redis中需要插入或者更新的数据
            //处理
            log.info("_existsBlock--" + _existsBlock.size() + ",_noExistsBlock:" + _noExistsBlock.size());
            List<LuckysheetCell> updateDatas = new ArrayList<>();
            if (_existsBlock.size() > 0) {
            	LuckysheetCell updateData = null;
                for (String _block : _existsBlock.keySet()) {
                	updateData = new LuckysheetCell();
                    JSONObject _bson = _existsBlock.get(_block);
                    String key =RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +gridKey + "_" +i + "_" +_block;
                    if(StringUtil.isEmptyMap(_bson))
                    {
                    	delIds.add(blockIds.get(_block));
                    	redisDelKeys.add(key);
                    }else {
                    	Long id = _bson.getLongValue("id");
                    	int _r = _bson.getIntValue("r");
                    	int _c = _bson.getIntValue("c");
                        updateData.setId(id);
                        _bson.remove("r");
                        _bson.remove("c");
                        updateData.setCellData(JSON.toJSONString(_bson));
                        updateDatas.add(updateData);
                        _bson.put("r",_r);
                        _bson.put("c",_c);
                        cacheDatas.put(key, _bson);
                    }
                }
            }
            List<LuckysheetCell> insertDatas = new ArrayList<>();
            if (_noExistsBlock.size() > 0) {
            	LuckysheetCell insertData = null;
                for (JSONObject _d : _noExistsBlock.values()) {
                	insertData = new LuckysheetCell();
                	insertData.setId(IdWorker.getId());
                	insertData.setBlockId(blockId);
                	int _r = _d.getIntValue("r");
                	int _c = _d.getIntValue("c");
                	insertData.setRowNo(_r);
                	insertData.setColumnNo(_c);
                	insertData.setSheetIndex(i);
                	insertData.setListId(gridKey);
                	_d.remove("r");
                	_d.remove("c");
                	_d.put("id", insertData.getId());
                	insertData.setCellData(JSON.toJSONString(_d));
                    insertDatas.add(insertData);
                    _d.put("r", _r);
                    _d.put("c", _c);
                    String key =RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +gridKey + "_" +i + "_" +_r+"_"+_c;
                    cacheDatas.put(key, _d);
                }
            }
            if(!ListUtil.isEmpty(redisDelKeys))
            {
            	this.mqProcessService.updateRedisBatchCellValueCache(MqTypeEnums.BATCHDELETECELLDATA.getCode(), redisDelKeys, i, gridKey, blockId,wsUserModel.getToken(),operate,changeBeforeData);
            }
            if(!cacheDatas.isEmpty())
            {
            	this.mqProcessService.updateRedisBatchCellValueCache(MqTypeEnums.BATCHINSERTCELLDATA.getCode(), cacheDatas, i, gridKey, blockId,wsUserModel.getToken(),operate,changeBeforeData);
            }
	        if(!ListUtil.isEmpty(delIds))
	        {
	        	this.mqProcessService.batchProcessCellData(MqTypeEnums.BATCHDELETECELLDATA.getCode(), delIds,i, gridKey,blockId);
	        }
            if(!ListUtil.isEmpty(insertDatas))
            {
            	this.mqProcessService.batchProcessCellData(MqTypeEnums.BATCHINSERTCELLDATA.getCode(), insertDatas,i, gridKey,blockId);
            }
            if(!ListUtil.isEmpty(updateDatas))
            {
            	this.mqProcessService.batchProcessCellData(MqTypeEnums.BATCHUPDATECELLDATA.getCode(), updateDatas,i, gridKey,blockId);
            }
//            LuckysheetHis luckysheetHis = new LuckysheetHis();
//            UserInfoDto userInfoDto = JWTUtil.getUserInfo(wsUserModel.getToken());
//            luckysheetHis.setSheetIndex(i);
//			luckysheetHis.setListId(gridKey);
//			JSONArray column = bson.getJSONObject("range").getJSONArray("column");
//			JSONArray row = bson.getJSONObject("range").getJSONArray("row");
//			String info="起始单元格：["+row.getString(0)+","+column.getString(0)+"]，结束单元格：["+row.getString(1)+","+column.getString(1)+"]";
//			if(StringUtil.isNotEmpty(bson.getString("operate")) && (bson.getString("operate").contains("删除单元格-左移") || bson.getString("operate").contains("删除单元格-上移")))
//			{
//				luckysheetHis.setChangeDesc(bson.getString("operate"));
//			}else {
//				luckysheetHis.setChangeDesc("批量操作单元格-"+info);
//			}
//			luckysheetHis.setRemark(bson.getString("operate"));
//			luckysheetHis.setType(1);
//			luckysheetHis.setOperateKey("bv");
//			luckysheetHis.setBeforeJson(null);
//			luckysheetHis.setOperator(userInfoDto.getUserName());
//			luckysheetHis.setCreator(userInfoDto.getUserId());
//			luckysheetHis.setCreateTime(new Date());
//			luckysheetHis.setUpdater(userInfoDto.getUserId());
//			luckysheetHis.setUpdateTime(new Date());
//			this.iLuckysheetHisService.insert(luckysheetHis);
            log.info("修改行列数据结果--end");
        } catch (Exception ex) {
        	ex.printStackTrace();
            log.error(ex.getMessage());
        }

        return "";
    }
    
    private JSONObject getCelldataByGridKey(String listId, String index, String blockId) {
    	try {
    		String key =RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" +index + "_" +blockId;
    		Object redisData = redisUtil.get(key);
    		JSONObject db = null;
    		if(redisData == null)
    		{
    			QueryWrapper<LuckysheetCell> queryWrapper = new QueryWrapper<LuckysheetCell>();
                queryWrapper.eq("list_id", listId);
                queryWrapper.eq("sheet_index", index);
                queryWrapper.eq("block_id", blockId);
                queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
                queryWrapper.orderByDesc("id");
                LuckysheetCell luckysheetCell = this.iLuckysheetCellService.getOne(queryWrapper, false);
            	if(luckysheetCell != null)
            	{
            		db = this.getCellData(luckysheetCell);
            	}
    		}else {
    			db = JSON.parseObject(JSON.toJSONString(redisData));
    		}
        	return db;
		} catch (Exception e) {
			log.error(e.getMessage());
            return null;
		}
    }
    
	/**  
	 * @MethodName: getIndexRvForThread
	 * @Description: 批量单元格操作，将需要操作的数据记录在redis中当再次收到rv_end指令时，在进行具体的实现操作
	 * @author caiyang
	 * @param gridKey
	 * @param bson
	 * @return
	 * @see com.springreport.api.coedit.ICoeditService#getIndexRvForThread(java.lang.String, com.alibaba.fastjson.JSONObject)
	 * @date 2023-08-14 03:42:16 
	 */
	@Override
	public String getIndexRvForThread(String gridKey, JSONObject bson,String key,WSUserModel wsUserModel) {
		List<JSONObject> bsons = new ArrayList<JSONObject>();
		bsons.add(bson);
		loadRvMsgForLock(gridKey, bsons, key,wsUserModel);
	    return "";
	}

	@Override
	public String handleUpdate(String gridKey, Object bson,WSUserModel wsUserModel) {
        StringBuilder _sb = new StringBuilder();
        if (bson instanceof List) {
            List<JSONObject> _list = (List<JSONObject>) bson;
            //JSONArray _list=(JSONArray)bson;
            //汇聚全部的 3.1单元格操作v
            List<JSONObject> _vlist = new ArrayList<JSONObject>();
            for (int x = 0; x < _list.size(); x++) {
                if (_list.get(x).containsKey("t") && _list.get(x).get("t").equals("v")) {
                    //单元格处理
                    _vlist.add(_list.get(x));
                } else {
                    //其他操作
                    log.info("其他操作--sb.append:chooseOperation");
                    _sb.append(chooseOperation(gridKey, _list.get(x),wsUserModel));
                }
            }
            if (_vlist.size() > 0) {
            	String blockId = this.getIdByListId(gridKey);
                //执行单元格批量处理
                if (_vlist.size() == 1) {
                    _sb.append(Operation_v(gridKey, _vlist.get(0),blockId,wsUserModel));
                } else {
                    _sb.append(Operation_v(gridKey, _vlist,blockId,wsUserModel));
                }
            }
        } else if (bson instanceof JSONObject) {
            log.info("bson instanceof BasicDBObject--bson");
            _sb.append(chooseOperation(gridKey, (JSONObject)bson,wsUserModel));
        }
        return _sb.toString();
	}

	//选择操作类型
    private String chooseOperation(String gridKey, JSONObject bson,WSUserModel wsUserModel) {
        if (bson.containsKey("t")) {
            String _result = "";
            if (SheetOperationEnum.contains(bson.get("t").toString())) {
                SheetOperationEnum _e = SheetOperationEnum.valueOf(bson.get("t").toString());
                 String blockId = this.getIdByListId(gridKey);
                switch (_e) {
                    case c:
                        //3.9	图表操作 （第一块）
                        _result = Operation_c(gridKey, bson,blockId,wsUserModel);
                        break;
                    case v:
                        //3.1	单元格操作v  gzip 分片
                        _result = Operation_v(gridKey, bson,blockId,wsUserModel);
                        break;
                    case cg:
                        //3.2   config操作cg （第一块）
                        _result = Operation_cg(gridKey, bson,blockId,wsUserModel);
                        break;
                    case all:
                        //3.3 通用保存 （第一块）
                        _result = Operation_all(gridKey, bson,blockId,wsUserModel);
                        break;
                    case fc:
                        //3.4.1 函数链接 （第一块）
                        _result = Operation_fc(gridKey, bson,blockId,wsUserModel);
                        break;
                    case f:
                        //3.6.1 选择筛选条件 （第一块）
//                        _result = Operation_f(gridKey, bson,blockId,wsUserModel);
                        break;
                    case fsc:
                        //3.6.2	清除筛 （第一块）
                        _result = Operation_fsc(gridKey, bson,blockId,wsUserModel);
                        break;
                    case fsr:
                        //3.6.3	恢复筛选 （第一块）
                        _result = Operation_fsr(gridKey, bson,blockId,wsUserModel);
                        break;
                    case drc:
                        //3.5.1 删除行或列   gzip 分块
                        _result = Operation_drc(gridKey, bson,blockId,wsUserModel);
                        break;
                    case arc:
                        //3.5.2	增加行或列  gzip 分块
                        _result = Operation_arc(gridKey, bson,blockId,wsUserModel);
                        break;
                    case sha:
                        //3.7.1	新建sha  sheet操作  gzip 分块
                        _result = Operation_sha(gridKey, bson,blockId,wsUserModel);
                        break;
                    case shc:
                        //3.7.2	复制shc 分块
                        _result = Operation_shc(gridKey, bson,wsUserModel);
                        break;
                    case shd:
                        //3.7.3	删除shd 分块
                        _result = Operation_shd(gridKey, bson,blockId,wsUserModel);
                        break;
                    case shr:
                        //3.7.4	位置shr 分块
                        _result = Operation_shr(gridKey, bson,blockId,wsUserModel);
                        break;
                    case shs:
                        //3.7.5	激活shs 分块
//                        _result = Operation_shs(gridKey, bson,blockId);
                        break;
                    case sh:
                        //3.8.1	隐藏    3.8	sheet属性sh  分块
                        _result = Operation_sh(gridKey, bson,blockId,wsUserModel);
                        break;
                    case na:
                        //3.10.1	表格名称 修改 数据库
                        _result = Operation_na(gridKey, bson);
                        break;
                    case thumb:
                        //3.10.2	缩略图    修改数据库
//                        _result = Operation_thumb(gridKey, bson,blockId);
                        break;
                    case fbv:
                        //3.10.2	缩略图    修改数据库
                        _result = Operation_fbv(gridKey, bson,blockId,wsUserModel);
                        break;
                    case rv:
                        //批量单元格操作
//                        _result = getIndexRvForThread(gridKey, bson);
                        break;
                    case shre:
//                        _result = Operation_shre(gridKey, bson,blockId);
                        break;
                    case mv:
                        break;
                    default:
                        _result = "无对应操作符：" + JsonUtil.toJson(bson);
                        break;
                }
            } else {
                _result = "无对应操作符：" + JsonUtil.toJson(bson);
            }
            return _result;
        } else {
            return "无操作符：" + JsonUtil.toJson(bson);
        }
    }
    
    /**
     * 3.9	图表操作 TODO
     *
     * @param gridKey2
     * @param bson
     * @return
     */
    public String Operation_c(String gridKey, JSONObject bson,String blockId,WSUserModel wsUserModel) {
    	try {
    		 //当前sheet的index值
            String i = bson.get("i").toString();
            String k = bson.get("k").toString();
            String operate = bson.getString("operate");
            Object _v = null;//需要替换的值
            if(bson.get("v") != null) {
            	_v =  bson.get("v");
            }
            if (_v == null) {
                //没有要修改的值
                return "";
            }
            String keyName = "";
            if("add".equals(k)) {
            	keyName = MqTypeEnums.INSERTCHART.getCode();
            }else if("mr".equals(k)) {
            	keyName = MqTypeEnums.MOVECHART.getCode();
            }else if("rangeChange".equals(k)) {
            	keyName = MqTypeEnums.CHANGECHARTRANGE.getCode();
            }else if("update".equals(k)) {
            	keyName = MqTypeEnums.UPDATECHART.getCode();
            }else if("delete".equals(k)) {
            	keyName = MqTypeEnums.DELETECHART.getCode();
            }
            this.mqProcessService.updateRedisConfigCache(keyName, _v, i, gridKey, blockId,wsUserModel.getToken(),operate);
            Luckysheet luckysheet = this.getLuckysheet(gridKey, i, blockId);
            this.mqProcessService.updateSheetConfig(keyName, _v, luckysheet.getId(),i, blockId, gridKey);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
    	return "";
    }

    /**  
     * @MethodName: getChartByGridKey
     * @Description: 获取图表数据（第一块）
     * @author caiyang
     * @param listId
     * @param index
     * @return JSONObject
     * @date 2023-08-14 04:46:08 
     */ 
    private JSONObject getChartByGridKey(String listId, String index,String blockId) {
    	try {
    		JSONObject db=new JSONObject();
        	QueryWrapper<Luckysheet> queryWrapper = new QueryWrapper<>();
        	queryWrapper.eq("list_id", listId);
    		queryWrapper.eq("block_id", blockId);
    		queryWrapper.eq("sheet_index", index);
    		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
    		Map<String, Object> map = this.getMap(queryWrapper);
    		for (String key : map.keySet()) {
                if("json_data".equals(key)){
                	JSONObject jsonData = JSON.parseObject(JSON.toJSONString(map.get("json_data")));
                    if(jsonData.get("chart")!=null){
                        try{
                            JSONArray pgd=jsonData.getJSONArray("chart");
                            db.put(key.toLowerCase(), pgd);
                        }catch (Exception e) {
                            db.put(key.toLowerCase(), new JSONArray());
                        }
                    }else{
                        db.put("chart", null);
                    }

                }else{
                    db.put(key.toLowerCase(), map.get(key));
                }
            }
            return db;
		} catch (Exception e) {
			log.error(e.getMessage());
            return null;
		}
    }
    
    /**
     * 3.1	单元格操作v  多个不同的分块,
     *
     * @param gridKey
     * @param dbObject
     * @return
     */
    private String Operation_v(String gridKey, List<JSONObject> dbObject,String blockId,WSUserModel wsUserModel) {
        try {

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "";
    }
    
    /**
     * 3.1	单元格操作v
     *
     * @param gridKey
     * @param bson
     * @return
     */
    private String Operation_v(String gridKey, JSONObject bson,String blockId,WSUserModel wsUserModel) {
    	
        if (GzipHandle.runGzip) {
            //压缩处理
            return "";
        }

        //不压缩处理
        try {
            log.info("start---Operation_v" + bson.toString());
            String i = bson.get("i").toString();//	当前sheet的index值
            Integer r = Integer.parseInt(bson.get("r").toString());//	单元格的行号
            Integer c = Integer.parseInt(bson.get("c").toString());//	单元格的列号
            JSONObject v = bson.getJSONObject("v");  //	单元格的值 v=null 删除单元格
            if(v != null) {
            	v.remove("customKey");
            }
            String operate = bson.getString("operate");
            //获取数据所在块的编号
            String block_id = JfGridConfigModel.getRange(r, c,null);
            
            log.info("block_id---Operation_v" + block_id);
            //1、先获取原数据（直接获取到某个sheet）
            JSONObject _dbObject = this.getCelldataByGridKey(gridKey, i, block_id);
            JSONObject changeBefore = JSON.parseObject(JSON.toJSONString(_dbObject));
            if (_dbObject == null) {
                //不存在，需要创建一个块
                if (v != null) {
                    //必须有值
                    //单元格
                    JSONObject _v = new JSONObject();
                    _v.put("v", v);
                    LuckysheetCell insert = new LuckysheetCell();
                    insert.setId(IdWorker.getId());
                    insert.setBlockId(blockId);
//                    insert.setRowCol(block_id);
                    insert.setRowNo(r);
                    insert.setColumnNo(c);
                    insert.setSheetIndex(i);
                    insert.setListId(gridKey);
                    _v.put("id", insert.getId());
                    insert.setCellData(JSON.toJSONString(_v));
                    insert.setDelFlag(DelFlagEnum.UNDEL.getCode());
                    this.mqProcessService.processCellData(MqTypeEnums.INSERTCELLDATA.getCode(),insert,i);
                    boolean result = true;
                    _v.put("r", r);
                    _v.put("c", c);
//                    redisUtil.set(RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +gridKey + "_" +i + "_" + r+"_"+c,_v);
                    this.mqProcessService.updateRedisCellValueCache(MqTypeEnums.UPDATECELLDATA.getCode(), _v, i, gridKey, blockId,r+"_"+c,wsUserModel.getToken(),operate,null);
                    //新增操作
                    if (!result) {
                        return "更新失败";
                    }
                }
            } else {
                if (v == null) {
                	_dbObject.remove("v");
                } else {
                	_dbObject.put("v", v);
                }
                this.mqProcessService.updateRedisCellValueCache(MqTypeEnums.UPDATECELLDATA.getCode(), _dbObject, i, gridKey, blockId,r+"_"+c,wsUserModel.getToken(),operate,changeBefore);
                LuckysheetCell luckysheetCell = new LuckysheetCell();
                luckysheetCell.setId(_dbObject.getLong("id"));
                _dbObject.remove("r");
                _dbObject.remove("c");
                luckysheetCell.setCellData(JSON.toJSONString(_dbObject));
                this.mqProcessService.processCellData(MqTypeEnums.UPDATECELLDATA.getCode(),luckysheetCell,i);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "";
    }
    
    /**
     * 3.2   config操作cg
     *
     * @param gridKey
     * @param bson
     * @return
     */
    private String Operation_cg(String gridKey, JSONObject bson,String blockId,WSUserModel wsUserModel) {
        try {
            //当前sheet的index值
            String i = bson.get("i").toString();
            String k = bson.get("k").toString();
            String operate = bson.getString("operate");
            Object _v = null;//需要替换的值
            if (bson.get("v") != null) {
                _v = bson.get("v");
            }
            if (_v == null) {
                //没有要修改的值
                return "";
            }
            String keyName = "";
            if("borderInfo".equals(k))
            {
            	keyName = MqTypeEnums.UPDATEBORDERINFO.getCode();
            }else if("rowhidden".equals(k))
            {
            	keyName = MqTypeEnums.UPDATEROWHIDDEN.getCode();
            }else if("colhidden".equals(k))
            {
            	keyName = MqTypeEnums.UPDATECOLHIDDEN.getCode();
            }else if("rowlen".equals(k))
            {
            	keyName = MqTypeEnums.UPDATEROWLEN.getCode();
            }else if("columnlen".equals(k))
            {
            	keyName = MqTypeEnums.UPDATECOLLEN.getCode();
            }
            else if("filterrowhidden".equals(k))
            {
            	keyName = MqTypeEnums.UPDATEFILTERROWHIDDEN.getCode();
            }
            this.mqProcessService.updateRedisConfigCache(keyName, _v, i, gridKey, blockId,wsUserModel.getToken(),operate);
            Luckysheet luckysheet = this.getLuckysheet(gridKey, i, blockId);
            this.mqProcessService.updateSheetConfig(keyName, _v, luckysheet.getId(),i, blockId, gridKey);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "";
    }
    
  //3.3 通用保存
    private String Operation_all(String gridKey, JSONObject bson,String blockId,WSUserModel wsUserModel) {
        try {
            String i = bson.get("i").toString();//	当前sheet的index值
            String k = bson.get("k").toString();   //	需要保存的key-value中的key
            log.info("Operation_all:i:" + i + "k:" + k);
            Object _v = bson.get("v");;//需要替换的值 (可能是对象也可能是字符串)
            String keyName = "";
            String operate = bson.getString("operate");
            if("config".equals(k))
            {
            	keyName = MqTypeEnums.UPDATECONFIG.getCode();
            }else if("hyperlink".equals(k))
            {
            	keyName = MqTypeEnums.UPDATEHYPERLINK.getCode();
            }else if("images".equals(k))
            {
            	keyName = MqTypeEnums.UPDATEIMAGES.getCode();
            }else if("frozen".equals(k))
            {
            	keyName = MqTypeEnums.UPDATEFROZEN.getCode();
            }else if("filter_select".equals(k))
            {
            	keyName = MqTypeEnums.UPDATEFILTERSELECT.getCode();
            }else if("filter".equals(k))
            {
            	keyName = MqTypeEnums.UPDATEFILTER.getCode();
            }else if("dataVerification".equals(k))
            {
            	keyName = MqTypeEnums.UPDATEDATAVERIFICATION.getCode();
            }else if("calcChain".equals(k))
            {
            	keyName = MqTypeEnums.UPDATECALCCHAIN.getCode();
            }else if("luckysheet_conditionformat_save".equals(k))
            {
            	keyName = MqTypeEnums.UPDATELUCKYSHEETCONDITIONFORMATSAVE.getCode();
            }else if("luckysheet_alternateformat_save".equals(k))
            {
            	keyName = MqTypeEnums.UPDATELUCKYSHEETALTERNATEFORMATSAVE.getCode();
            }else if("row".equals(k))
            {
            	keyName = MqTypeEnums.UPDATEROW.getCode();
            }else if("column".equals(k))
            {
            	keyName = MqTypeEnums.UPDATECOLUMN.getCode();
            }else if("name".equals(k))
            {
            	keyName = MqTypeEnums.UPDATENAME.getCode();
            }else if("color".equals(k))
            {
            	keyName = MqTypeEnums.UPDATECOLOR.getCode();
            }
            this.mqProcessService.updateRedisConfigCache(keyName, _v, i, gridKey, blockId,wsUserModel.getToken(),operate);
            Luckysheet luckysheet = this.getLuckysheet(gridKey, i, blockId);
            this.mqProcessService.updateSheetConfig(keyName, _v, luckysheet.getId(),i, blockId, gridKey);
        } catch (Exception ex) {
            log.error("Operation_all--err--all:" + ex.getMessage());
        }
        return "";
    }
    
    /**
     * 3.4.1 函数链接
     *
     * @param gridKey
     * @param bson
     * @return
     */
    private String Operation_fc(String gridKey, JSONObject bson,String blockId,WSUserModel wsUserModel) {
    	String i = bson.get("i").toString();
    	//对象值，这里对象的内部字段不需要单独更新，所以存为文本即可  2018-11-28 前段需求必须取出时为对象
        JSONObject v = bson.getJSONObject("v");
        String operate = bson.getString("operate");
        //操作类型,add为新增，update为更新，del为删除
        String op = bson.get("op").toString();
        String keyName = "";
        if (op.equals("add")) {
        	//新增公式
        	keyName = MqTypeEnums.ADDFC.getCode();
        }else if(op.equals("del"))
        {//删除公式
        	keyName = MqTypeEnums.DELFC.getCode();
        }
        this.mqProcessService.updateRedisConfigCache(keyName, v, i, gridKey, blockId,wsUserModel.getToken(),operate);
        Luckysheet luckysheet = this.getLuckysheet(gridKey, i, blockId);
        v.put("listId", gridKey);
        v.put("index", i);
        v.put("blockId", blockId);
        this.mqProcessService.updateSheetConfig(keyName, v, luckysheet.getId(),i, blockId, gridKey);
    	return "";
    }
    
    
    /**
     * 3.6.2	清除筛
     *
     * @param gridKey
     * @param bson
     * @return
     */
    private String Operation_fsc(String gridKey, JSONObject bson,String blockId,WSUserModel wsUserModel) {
        try {
            //当前sheet的index值
            String i = bson.get("i").toString();
            String operate = bson.getString("operate");
            this.mqProcessService.updateRedisConfigCache(MqTypeEnums.CLEARFILTER.getCode(), null, i, gridKey, blockId,wsUserModel.getToken(),operate);
            Luckysheet luckysheet = this.getLuckysheet(gridKey, i, blockId);
            this.mqProcessService.updateSheetConfig(MqTypeEnums.CLEARFILTER.getCode(), null, luckysheet.getId(),i, blockId, gridKey);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "";
    }
    
    /**
     * 3.6.3	恢复筛选
     *
     * @param gridKey
     * @param bson
     * @return
     */
    private String Operation_fsr(String gridKey, JSONObject bson,String blockId,WSUserModel wsUserModel) {
        try {
            //当前sheet的index值
//            String i = bson.get("i").toString();
//            Object filter = null;
//            if (bson.getJSONObject("v").get("filter") != null) {
//                filter = bson.getJSONObject("v").get("filter");
//            } else {
//                filter = new JSONObject();
//            }
//
//            Object filter_select = null;//
//            if (bson.getJSONObject("v").get("filter_select") != null) {
//                filter_select = (Object) bson.getJSONObject("v").get("filter_select");
//            } else {
//                filter_select = new JSONObject();
//            }
//
//            //1、先获取原数据
//            JSONObject _dbObject = this.getConfigByGridKey(gridKey, i,blockId);
//            if (_dbObject == null) {
//                return "list_id=" + gridKey + ",index=" + i + "的sheet不存在";
//            }
//
//            JSONObject query=getQuery(gridKey,i,blockId);
//
//            JSONObject db = new JSONObject();
//            db.put("filter", filter);
//            db.put("filter_select", filter_select);
//            boolean _result = this.recordDataUpdataUpdateService.updateJsonbDataForKeys(query, db);
//            LuckysheetHis luckysheetHis = new LuckysheetHis();
//            UserInfoDto userInfoDto = JWTUtil.getUserInfo(wsUserModel.getToken());
//            luckysheetHis.setSheetIndex(i);
//			luckysheetHis.setListId(gridKey);
//			luckysheetHis.setChangeDesc("恢复筛选");
//			luckysheetHis.setRemark(bson.getString("operate"));
//			luckysheetHis.setType(1);
//			luckysheetHis.setOperateKey("filter,filter_select");
//			luckysheetHis.setBeforeJson(_dbObject);
//			luckysheetHis.setBson(db);
//			luckysheetHis.setOperator(userInfoDto.getUserName());
//			luckysheetHis.setCreator(userInfoDto.getUserId());
//			luckysheetHis.setCreateTime(new Date());
//			luckysheetHis.setUpdater(userInfoDto.getUserId());
//			luckysheetHis.setUpdateTime(new Date());
//			this.iLuckysheetHisService.insert(luckysheetHis);
//            if (!_result) {
//                return "更新失败";
//            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "";
    }
    
    /**
     * 3.5.1 删除行或列
     *
     * @param gridKey
     * @param bson
     * @return
     */
    private String Operation_drc(String gridKey, JSONObject bson,String blockId,WSUserModel wsUserModel) {
        try {
            //当前sheet的index值
            String i = bson.get("i").toString();
            //行操作还是列操作，值r代表行，c代表列
            String rc = bson.get("rc").toString();
            String operate = bson.getString("operate");
            //从第几行或者列开始删除
            Integer index = null;
            //删除多少行或者列
            Integer len = null;
            //需要修改的合并单元格信息
            JSONObject mergeCells = null;
            //需要修改的公式单元格数据
            JSONObject updateFuncCell = null;
            
            if (bson.get("v") != null && bson instanceof JSON) {
                JSONObject _v = bson.getJSONObject("v");
                if (_v.containsKey("index")) {
                    index = Integer.parseInt(_v.get("index").toString());
                }
                if (_v.containsKey("len")) {
                    len = Integer.parseInt(_v.get("len").toString());
                }
                if (_v.containsKey("mergeCells")) {
                	mergeCells =  _v.getJSONObject("mergeCells");
                }
                if (_v.containsKey("updateFuncCell")) {
                	updateFuncCell =  _v.getJSONObject("updateFuncCell");
                }
            }
            if (index == null || len == null) {
                return "参数错误";
            }

            JSONObject sheetJsonData = this.getConfigByGridKey(gridKey, i,blockId);
            int row = sheetJsonData.getIntValue("row");//总行数
            int column = sheetJsonData.getIntValue("column");//总列数
            MqRCOprepationDto mqRCOprepationDto = new MqRCOprepationDto();
            mqRCOprepationDto.setRc(rc);
            mqRCOprepationDto.setIndex(index);
            mqRCOprepationDto.setLen(len);
            mqRCOprepationDto.setRow(row);
            mqRCOprepationDto.setColumn(column);
            mqRCOprepationDto.setMergeCells(mergeCells);
            mqRCOprepationDto.setUpdateFuncCell(updateFuncCell);
            this.mqProcessService.updateRedisBatchCellValueCache(MqTypeEnums.DELETEROWCOLS.getCode(), mqRCOprepationDto, i, gridKey, blockId,wsUserModel.getToken(),operate,null);
            this.mqProcessService.rowColOperate(MqTypeEnums.DELETEROWCOLS.getCode(),mqRCOprepationDto,i, gridKey, blockId);
            //1、先获取原数据

        } catch (Exception ex) {
            log.error(ex.getMessage());
            return ex.getMessage();
        }
        return "";
    }
    
    private JSONObject getQuery(String gridKey,String i,String blockId){
        JSONObject query=new JSONObject();
        query.put("list_id",gridKey);
        query.put("index",i);
        query.put("block_id",blockId);
        return query;
    }
    
    /**  
     * @MethodName: getConfigByGridKey
     * @Description: 获取指定xls、sheet中的config中数据 （存放在第一块中）
     * @author caiyang
     * @return JSONObject
     * @date 2023-08-15 09:43:37 
     */ 
    private JSONObject getConfigByGridKey(String listId, String index,String blockId) {
    	Luckysheet luckysheet = this.getLuckysheet(listId, index, blockId);
		return this.getSheetJsonData(luckysheet);
    }
    
    private Luckysheet getLuckysheet(String listId, String index,String blockId){
    	Object object = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId);
    	Luckysheet luckysheet = null;
		if(object == null)
		{
			QueryWrapper<Luckysheet> queryWrapper = new QueryWrapper<Luckysheet>();
        	queryWrapper.eq("list_id", listId);
        	queryWrapper.eq("sheet_index", index);
        	queryWrapper.eq("block_id", blockId);
        	queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
        	luckysheet = this.baseMapper.selectOne(queryWrapper);
        	redisUtil.set(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+index+"_"+blockId,JSON.toJSONString(luckysheet));
		}else {
			luckysheet = JSON.parseObject(object.toString(), Luckysheet.class);
		}
		return luckysheet;
    }
    
    /**
     * 用mc重置data中数据
     *
     * @param mc
     * @param _celldatas
     */
    private void drc_arc_handle_mc(JSONObject mc, JSONArray _celldatas) {
        List<ConfigMergeModel> _list = ConfigMergeModel.getListByDBObject(mc);
        for (int x = _celldatas.size() - 1; x >= 0; x--) {
            try {
                JSONObject _cell = _celldatas.getJSONObject(x);
                Integer _r = Integer.parseInt(_cell.get("r").toString());
                Integer _c = Integer.parseInt(_cell.get("c").toString());
                for (ConfigMergeModel _cmModel : _list) {
                    if (_cmModel.isRange(_r, _c)) {
                        if (_cell.containsKey("v")) {
                            JSONObject _v = _cell.getJSONObject("v");
                            if (_v.containsKey("mc")) {
                                JSONObject _mc = _v.getJSONObject("mc");
                                _mc.put("r", _cmModel.getR());
                                _mc.put("c", _cmModel.getC());
                            }
                        }
                        break;
                    }
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }
    
	private Map<String, JSONObject> getFuncCellsMapData(JSONArray funcCells)
	{
		 Map<String, JSONObject> result = null;
		if(!ListUtil.isEmpty(funcCells))
		{
			result = new HashMap<>();
			for (int i = 0; i < funcCells.size(); i++) {
				int r = funcCells.getJSONObject(i).getIntValue("r");
				int c = funcCells.getJSONObject(i).getIntValue("c");
				result.put(r+"_"+c, funcCells.getJSONObject(i));
			}
		}
		return result;
	}
	/**
     * 3.5.2 增加行或列
     *
     * @param gridKey
     * @param bson
     * @return
     */
    private String Operation_arc(String gridKey, JSONObject bson,String blockId,WSUserModel wsUserModel) {
        try {
            String i = bson.get("i").toString();//	当前sheet的index值
            String operate = bson.getString("operate");
            String rc = bson.get("rc").toString();   //行操作还是列操作，值r代表行，c代表列
            Integer index = null;//  		从第几行或者列开始新增
            Integer len = null;// 		增加多少行或者列
            JSONArray data = null;// 	新增行或者列的内容
            JSONObject mc = null;//     	需要修改的合并单元格信息
            String direction = null;//方向
            //需要修改的合并单元格信息
            JSONObject mergeCells = null;
            //需要修改的公式单元格数据
            JSONObject updateFuncCell = null;
            if (bson.get("v") != null && bson instanceof JSON) {
                JSONObject _v = bson.getJSONObject("v");
                if (_v.containsKey("index")) {
                    index = Integer.parseInt(_v.get("index").toString());
                }
                if (_v.containsKey("len")) {
                    len = Integer.parseInt(_v.get("len").toString());
                }
                if (_v.containsKey("data") && _v.get("data") instanceof JSONArray) {
                    data = _v.getJSONArray("data");
                }
                if (_v.containsKey("mc")) {
                    mc = _v.getJSONObject("mc");
                }
                if (_v.containsKey("direction")) {
                    direction = _v.get("direction").toString().trim();
                }
                if (_v.containsKey("mergeCells")) {
                	mergeCells =  _v.getJSONObject("mergeCells");
                }
                if (_v.containsKey("updateFuncCell")) {
                	updateFuncCell =  _v.getJSONObject("updateFuncCell");
                }

            }
            if (index == null || len == null) {
                return "参数错误";
            }
            JSONObject sheetJsonData = this.getConfigByGridKey(gridKey, i,blockId);
            int row = sheetJsonData.getIntValue("row");//总行数
            int column = sheetJsonData.getIntValue("column");//总列数
            MqRCOprepationDto mqRCOprepationDto = new MqRCOprepationDto();
            mqRCOprepationDto.setRc(rc);
            mqRCOprepationDto.setDirection(direction);
            mqRCOprepationDto.setIndex(index);
            mqRCOprepationDto.setLen(len);
            mqRCOprepationDto.setRow(row);
            mqRCOprepationDto.setColumn(column);
            mqRCOprepationDto.setMergeCells(mergeCells);
            mqRCOprepationDto.setUpdateFuncCell(updateFuncCell);
            mqRCOprepationDto.setData(data);
            this.mqProcessService.updateRedisBatchCellValueCache(MqTypeEnums.ADDROWCOLS.getCode(), mqRCOprepationDto, i, gridKey, blockId,wsUserModel.getToken(),operate,null);
            this.mqProcessService.rowColOperate(MqTypeEnums.ADDROWCOLS.getCode(),mqRCOprepationDto,i, gridKey, blockId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "";
    }
    
    /**
     * 3.7.1	新建sha  sheet操作
     *
     * @param gridKey
     * @param bson
     * @return
     */
    private String Operation_sha(String gridKey, JSONObject bson,String blockId,WSUserModel wsUserModel) {
        try {
            //Integer i=Integer.parseInt(bson.get("i").toString());// 当前sheet的index值,此处为null
            JSONObject v =  bson.getJSONObject("v");   //创建的对象
            String operate = bson.getString("operate");
            log.info("Operation_sha--v:" + v);
            String index = null;// v中Index索引
            if (v.containsKey("index")) {
                index = "" + v.get("index").toString();
            }
            if (index == null) {
                return "index不能为null";
            }
            log.info("Operation_sha---" + index);
            long id = IdWorker.getId();
            v.put("id", id);
            this.mqProcessService.updateRedisConfigCache(MqTypeEnums.ADDSHEET.getCode(), v, index, gridKey, blockId,wsUserModel.getToken(),operate);
            this.mqProcessService.updateSheetConfig(MqTypeEnums.ADDSHEET.getCode(), v, id, index, blockId, gridKey);
//            this.mqProcessService.updateSheetConfig(MqTypeEnums.ADDSHEET.getCode(), v, id, index, blockId, gridKey);
//            //1、先获取原数据
//            List<JSONObject> _dbObject = this.getBlocksByGridKey(gridKey.toString(), false,blockId);
//            log.info("getIndexByGridKey---" + _dbObject);
//            if (_dbObject == null) {
//                return "gridKey=" + gridKey + "的数据表格不存在";
//            }
//            //2、数据所在的sheet的序号
//            Integer _sheetPosition = JfGridFileUtil.getSheetPositionByIndex(_dbObject, index);
//            log.info("_sheetPosition--" + _sheetPosition);
//            if (_sheetPosition != null) {
//                return "index=" + index + "的sheet已经存在";
//            }
//
//            Luckysheet luckysheet = new Luckysheet();
//            luckysheet.setId(IdWorker.getId());
//            luckysheet.setListId(gridKey);
//            luckysheet.setBlockId(blockId);
//            luckysheet.setSheetIndex(index);
//            luckysheet.setStatus(0);
//            luckysheet.setSheetOrder(Integer.valueOf(v.get("order").toString()));
//            luckysheet.setJsonData(v);
//            v.remove("list_id");
//            v.remove("block_id");
//            v.remove("index");
//            v.remove("status");
//            v.remove("order");
//            boolean dbKey = this.save(luckysheet);
//            LuckysheetHis luckysheetHis = new LuckysheetHis();
//            UserInfoDto userInfoDto = JWTUtil.getUserInfo(wsUserModel.getToken());
//            luckysheetHis.setSheetIndex(index);
//			luckysheetHis.setListId(gridKey);
//			luckysheetHis.setChangeDesc("新增sheet");
//			luckysheetHis.setRemark(bson.getString("operate"));
//			luckysheetHis.setType(1);
//			luckysheetHis.setOperateKey("sha");
//			luckysheetHis.setBeforeJson(null);
//			luckysheetHis.setBson(v);
//			luckysheetHis.setOperator(userInfoDto.getUserName());
//			luckysheetHis.setCreator(userInfoDto.getUserId());
//			luckysheetHis.setCreateTime(new Date());
//			luckysheetHis.setUpdater(userInfoDto.getUserId());
//			luckysheetHis.setUpdateTime(new Date());
//			this.iLuckysheetHisService.insert(luckysheetHis);
//            if (!dbKey) {
//                return "更新失败";
//            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "";
    }
    
    /**
     * 3.7.2	复制shc
     *
     * @param gridKey
     * @param bson
     * @return
     */
    private String Operation_shc(String gridKey, JSONObject bson,WSUserModel wsUserModel) {
        try {
            String i = bson.get("i").toString();//	新建sheet的位置
            String operate = bson.getString("operate");
            String copyindex = null;//复制对象
            String name = null;
            JSONObject _v = null;
            if (bson.containsKey("v")) {
                _v = bson.getJSONObject("v");
                if (_v.containsKey("copyindex")) {
                    copyindex = _v.get("copyindex").toString();
                }
                if (_v.containsKey("name")) {
                    name = (String) _v.get("name");
                }
            }
            if (copyindex == null) {
                return "参数错误";
            }
            long id = IdWorker.getId();
            String blockId = this.getIdByListId(gridKey);
            String key = RedisPrefixEnum.DOCOMENTDATA.getCode()+gridKey+"_"+copyindex+"_"+blockId;
            Object redisSheetData = redisUtil.get(key);
            if(redisSheetData != null)
            {
            	Luckysheet luckysheet = JSON.parseObject(redisSheetData.toString(), Luckysheet.class);
            	int row = luckysheet.getRowSize();
            	int column = luckysheet.getColumnSize();
            	JSONObject cellIds = new JSONObject();//单元格对应的id
            	for (int j = 0; j < row; j++) {
					for (int j2 = 0; j2 < column; j2++) {
						cellIds.put(j+"_"+j2, IdWorker.getId());
					}
				}
            	Luckysheet newData = new Luckysheet();
            	BeanUtils.copyProperties(luckysheet, newData);
            	newData.setId(id);
            	newData.setSheetName(name);
            	newData.setSheetIndex(i);
            	this.mqProcessService.updateRedisConfigCache(MqTypeEnums.COPYSHEET.getCode(), newData, i, gridKey, blockId,wsUserModel.getToken(),operate);
            	this.mqProcessService.updateSheetConfig(MqTypeEnums.COPYSHEET.getCode(), newData, null, i, blockId, gridKey);
            	List<String> keys = redisUtil.getKeys(RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +gridKey + "_" +copyindex);
            	if(!ListUtil.isEmpty(keys))
            	{
            		List<Object> redisCellDatas = redisUtil.multiGet(keys);
            		if(!ListUtil.isEmpty(redisCellDatas))
            		{
            			List<Map<String, JSONObject>> datas = new ArrayList<Map<String,JSONObject>>();
            			List<LuckysheetCell> list = new ArrayList<LuckysheetCell>();
            			JSONObject cellData = null;
            			LuckysheetCell newCellData = null;
            			Map<String, JSONObject> map = null;
            			for (int j = 0; j < redisCellDatas.size(); j++) {
            				if(j == 0 || j%Constants.MQ_LIST_LIMIT_SIZE == 0)
            				{
            					Map<String, JSONObject> tempMap = new HashMap<String, JSONObject>();
            					map = tempMap;
            					datas.add(map);
            				}
            				cellData = (JSONObject) redisCellDatas.get(j);
            				int r = cellData.getIntValue("r");
            				int c = cellData.getIntValue("c");
            				long cellId = IdWorker.getId();
            				cellData.put("id", cellId);
            				cellData.remove("r");
            				cellData.remove("c");
            				newCellData = new LuckysheetCell();
            				newCellData.setId(cellId);
            				newCellData.setBlockId(blockId);
            				newCellData.setRowNo(r);
            				newCellData.setColumnNo(c);
            				newCellData.setSheetIndex(i);
            				newCellData.setListId(gridKey);
            				newCellData.setCellData(JSON.toJSONString(cellData));
            				list.add(newCellData);
            				String cellDataKey = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +gridKey + "_" + i + "_" + r + "_" + c;
            				cellData.put("r", r);
            				cellData.put("c", c);
            				map.put(cellDataKey, cellData);
            			}
            			for (int j = 0; j < datas.size(); j++) {
            				this.mqProcessService.updateRedisBatchCellValueCache(MqTypeEnums.BATCHINSERTCELLDATA.getCode(), datas.get(j), i, gridKey, blockId,wsUserModel.getToken(),operate,null);
						}
            			List<List<LuckysheetCell>> subs = ListUtils.partition(list , Constants.MQ_LIST_LIMIT_SIZE);
            			for (int j = 0; j < subs.size(); j++) {
            				this.mqProcessService.batchProcessCellData(MqTypeEnums.BATCHINSERTCELLDATA.getCode(), subs.get(j),i, gridKey,blockId);
						}
            		}
            	}
            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "";
    }
    
    /**
     * 3.7.3	删除shd
     *
     * @param gridKey
     * @param bson
     * @return
     */
    private String Operation_shd(String gridKey, JSONObject bson,String blockId,WSUserModel wsUserModel) {
        try {
            String deleIndex = null;//	需要删除的sheet索引
            if (bson.containsKey("v")) {
                JSONObject _v = bson.getJSONObject("v");
                if (_v.containsKey("deleIndex")) {
                    deleIndex = _v.get("deleIndex").toString();
                }
            }
            if (deleIndex == null) {
                return "参数错误";
            }
            String operate = bson.getString("operate");
            this.mqProcessService.updateRedisConfigCache(MqTypeEnums.DELSHEET.getCode(), deleIndex, deleIndex, gridKey, blockId,wsUserModel.getToken(),operate);
            this.mqProcessService.updateRedisCellValueCache(MqTypeEnums.DELSHEETCELL.getCode(),null,deleIndex,gridKey, blockId,null,wsUserModel.getToken(),operate,null);
            this.mqProcessService.updateSheetConfig(MqTypeEnums.DELSHEET.getCode(), deleIndex, null, deleIndex, blockId, gridKey);
            this.mqProcessService.batchProcessCellData(MqTypeEnums.DELSHEETCELL.getCode(),null, deleIndex, gridKey,blockId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "";
    }
    
    /**
     * 3.7.4	位置shr
     *
     * @param gridKey
     * @param bson
     * @return
     */
    private String Operation_shr(String gridKey, JSONObject bson,String blockId,WSUserModel wsUserModel) {
        try {
            if (!bson.containsKey("v")) {
                return "参数错误";
            }
            JSONObject _v = bson.getJSONObject("v");
            String operate = bson.getString("operate");
            if (_v != null && _v.keySet().size() > 0) {
            	this.mqProcessService.updateRedisConfigCache(MqTypeEnums.UPDATEORDER.getCode(), _v, null, gridKey, blockId,wsUserModel.getToken(),operate);
            	this.mqProcessService.updateSheetConfig(MqTypeEnums.UPDATEORDER.getCode(), _v, null,gridKey, blockId, gridKey);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "";
    }
  //3.8.1	隐藏    3.8	sheet属性sh
    /*
    * 	更新“i”对应sheet的根路径hide字段为v，当隐藏时status值为0，当显示时为1，
    * 	如果为隐藏则更新index对应cur的sheet的status状态为1
    *     [{"t":"sh","i":0,"v":1,"op":"hide","cur":1}]
          [{"t":"sh","i":0,"v":0,"op":"hide"}]
    *     隐藏：where index=i set hide=v，status=0
    *           where index=cur set status=1
    * 取消隐藏：where status=1 set status=0         将原来的status标记1的设置为 0
    *           where index=i set hide=v，status=1  将i对应的index的status设置为 1
    **/
    private String Operation_sh(String gridKey, JSONObject bson,String blockId,WSUserModel wsUserModel) {
        try {
            String i = bson.get("i").toString();//当前sheet的index值
            String op = bson.get("op").toString();// 	操作选项，有hide、show。
            Integer v = 0;  //如果hide为1则隐藏，为0或者空则为显示
            String operate = bson.getString("operate");
            String cur = null; //	隐藏后设置索引对应cur的sheet为激活状态
            if (bson.containsKey("v") && bson.get("v") != null) {
                v = Integer.parseInt(bson.get("v").toString());
            }
            if (bson.containsKey("cur") && bson.get("cur") != null) {
                cur = bson.get("cur").toString();
            }
            this.mqProcessService.updateRedisConfigCache(MqTypeEnums.UPDATEHIDESTATUS.getCode(), v, i, gridKey, blockId,wsUserModel.getToken(),operate);
            Luckysheet luckysheet = this.getLuckysheet(gridKey, i, blockId);
            this.mqProcessService.updateSheetConfig(MqTypeEnums.UPDATEHIDESTATUS.getCode(), v, luckysheet.getId(),i, blockId, gridKey);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "";
    }
    
    /**
     * 3.10.1	表格名称 修改 数据库
     *
     * @param gridKey
     * @param bson
     * @return
     */
    public String Operation_na(String gridKey, JSONObject bson) {
        try {
            String v = null;// 	表格的名称
            if (bson.containsKey("v")) {
                v = bson.get("v").toString().trim();
            }
            LuckySheetGridModel model = new LuckySheetGridModel();
            model.setList_id(gridKey);
            model.setGrid_name(v);

            //更新文件名
            int i = 1;
            if (i == 0) {
                return "改名失败";
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "";
    }
    
    
    /**  
     * @MethodName: updateDataForReDel
     * @Description: 删除sheet（非物理删除）
     * @author caiyang
     * @return boolean
     * @date 2023-08-15 08:38:52 
     */ 
    private boolean updateDataForReDel(GridRecordDataModel model) {
    	try {
    		Luckysheet luckysheet = new Luckysheet();
            luckysheet.setDelFlag(model.getIs_delete());
            UpdateWrapper<Luckysheet> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("sheet_index", model.getIndex());
            updateWrapper.eq("list_id", model.getList_id());
            this.update(luckysheet, updateWrapper);
            return true;
		} catch (Exception e) {
			log.error(e.getMessage());
            return false;
		}
    }
    
    /**  
     * @MethodName: batchUpdateForNoJsonbData
     * @Description: 批量更新order 按listId，index，首块
     * @author caiyang
     * @param models
     * @return boolean
     * @date 2023-08-15 08:59:43 
     */ 
    private boolean batchUpdateForNoJsonbData(List<GridRecordDataModel> models) {
    	try {
    		Luckysheet luckysheet = null;
        	UpdateWrapper<Luckysheet> updateWrapper = null;
        	for (int i = 0; i < models.size(); i++) {
        		luckysheet = new Luckysheet();
        		luckysheet.setSheetOrder(models.get(i).getOrder());
        		updateWrapper = new UpdateWrapper<>();
        		updateWrapper.eq("list_id", models.get(i).getList_id());
        		updateWrapper.eq("sheet_index", models.get(i).getIndex());
        		updateWrapper.eq("block_id", models.get(i).getBlock_id());
        		this.update(luckysheet, updateWrapper);
    		}
        	return true;
		} catch (Exception e) {
			 log.error(e.getMessage());
	         return false;
		}
    }
    
    /**  
     * @MethodName: updateDataStatus
     * @Description: 更新status状态
     * @author caiyang
     * @param model
     * @return boolean
     * @date 2023-08-15 09:23:15 
     */ 
    private boolean updateDataStatus(GridRecordDataModel model) {
    	try {
    		Luckysheet luckysheet = new Luckysheet();
        	luckysheet.setStatus(0);
        	UpdateWrapper<Luckysheet> updateWrapper = new UpdateWrapper<>();
        	updateWrapper.eq("list_id", model.getList_id());
        	updateWrapper.eq("block_id", model.getBlock_id());
        	updateWrapper.eq("status", 1);
        	this.update(luckysheet, updateWrapper);
        	luckysheet = new Luckysheet();
        	luckysheet.setStatus(1);
        	updateWrapper = new UpdateWrapper<>();
        	updateWrapper.eq("list_id", model.getList_id());
        	updateWrapper.eq("block_id", model.getBlock_id());
        	updateWrapper.eq("sheet_index", model.getIndex());
        	this.update(luckysheet, updateWrapper);
        	return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
    	
    }

	/**  
	 * @MethodName: getByGridKeys
	 * @Description: 获取sheet数据  参数为gridKey（表格主键） 和 index（sheet主键合集
	 * @author caiyang
	 * @param listId
	 * @param indexs
	 * @return
	 * @see com.springreport.api.coedit.ICoeditService#getByGridKeys(java.lang.String, java.util.List)
	 * @date 2023-08-18 06:10:00 
	 */
	@Override
	public LinkedHashMap getByGridKeys(String listId, String index) {
		LinkedHashMap _resultModel=new LinkedHashMap<>();
        if(StringUtil.isNullOrEmpty(index)){
            return _resultModel;
        }
        String blockId = this.getIdByListId(listId);
        String[] indexes = index.split(",");
        for (int i = 0; i < indexes.length; i++) {
        	 Object redisData = redisUtil.get(RedisPrefixEnum.DOCOMENTDATA.getCode()+listId+"_"+indexes[i]+"_"+blockId);
        	 Luckysheet luckysheet = JSONObject.parseObject(redisData.toString(), Luckysheet.class);
             List<Object> cellDatas = this.getCelldataBlockMergeByGridKey(listId, indexes[i], blockId);
             _resultModel.put(indexes[i], cellDatas);
		}
       
		return _resultModel;
	}

	@Override
	public String getIdByListId(String listId) {
		Object blockId = redisUtil.get(RedisPrefixEnum.COEDIT.getCode()+listId);
		if(blockId == null)
		{
			QueryWrapper<OnlineTpl> onlineTplWrapper = new QueryWrapper<OnlineTpl>();
			onlineTplWrapper.eq("list_id", listId);
			onlineTplWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			OnlineTpl onlineTpl = this.onlineTplMapper.selectOne(onlineTplWrapper);
			if(onlineTpl == null) {
				throw new BizException(StatusCode.FAILURE, "文档不存在。");
			}
			blockId = String.valueOf(onlineTpl.getId());
			redisUtil.set(RedisPrefixEnum.COEDIT.getCode()+listId, blockId, 240,TimeUnit.MINUTES);
		}
		return String.valueOf(blockId);
	}
	
	private String getIdNameByListId(String listId) {
		String blockId = (String) redisUtil.get(RedisPrefixEnum.COEDIT.getCode()+listId+"_id_name");
		if(StringUtil.isNullOrEmpty(blockId))
		{
			QueryWrapper<OnlineTpl> onlineTplWrapper = new QueryWrapper<OnlineTpl>();
			onlineTplWrapper.eq("list_id", listId);
			onlineTplWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
			OnlineTpl onlineTpl = this.onlineTplMapper.selectOne(onlineTplWrapper);
			blockId = String.valueOf(onlineTpl.getId())+"_"+onlineTpl.getTplName();
			redisUtil.set(RedisPrefixEnum.COEDIT.getCode()+listId+"_id_name", blockId, 240,TimeUnit.MINUTES);
		}
		return blockId;
	}

	/**  
	 * @MethodName: downLoadExcel
	 * @Description: 导出excel
	 * @author caiyang
	 * @param model
	 * @throws Exception 
	 * @see com.springreport.api.coedit.ICoeditService#downLoadExcel(com.springreport.dto.coedit.MesDownloadDto)
	 * @date 2023-08-31 02:31:16 
	 */
	@Override
	public void downLoadExcel(MesDownloadDto model,HttpServletResponse httpServletResponse,UserInfoDto userInfoDto) throws Exception {
		QueryWrapper<OnlineTpl> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("list_id", model.getGridKey());
		queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
		OnlineTpl onlineTpl = this.onlineTplMapper.selectOne(queryWrapper, false);
		Map<String, List<String>> noViewAuthCells = new HashMap<>();
		if(userInfoDto !=null && userInfoDto.getUserId() != null && onlineTpl.getCreator() != null && onlineTpl != null && onlineTpl.getCreator().longValue() != userInfoDto.getUserId().longValue()) {
			//获取该用户没有查看权限的单元格
			Map<String, List<ReportRangeAuth>> noViewAauthRange = this.iReportRangeAuthUserService.getUserNoAuthRange(model.getGridKey(), userInfoDto);
			if(noViewAauthRange != null) {
				for (String key : noViewAauthRange.keySet()) {
					List<ReportRangeAuth> auths = noViewAauthRange.get(key);
					for (int i = 0; i < auths.size(); i++) {
						JSONArray rowsNo = JSON.parseArray(auths.get(i).getRowsNo());
						JSONArray colsNo = JSON.parseArray(auths.get(i).getColsNo());
						int startr = rowsNo.getIntValue(0);
						int endr = rowsNo.getIntValue(1);
						int startc = colsNo.getIntValue(0);
						int endc = colsNo.getIntValue(1);
						List<String> cells = null;
						if(noViewAuthCells.containsKey(key)) {
							cells = noViewAuthCells.get(key);
						}else {
							cells = new ArrayList<>();
							noViewAuthCells.put(key, cells);
						}
						for (int j = startr; j <= endr; j++) {
							for (int k = startc; k < endc; k++) {
								String cellKey = j+"_"+k;
								cells.add(cellKey);
							}
						}
					}
				}
			}
		}
		String blockIdName = this.getIdNameByListId(model.getGridKey());
		String name = blockIdName.split("_")[1];
		List<String> keys = redisUtil.getKeys(RedisPrefixEnum.DOCOMENTDATA.getCode()+model.getGridKey()+"_");
		List<Object> list = redisUtil.multiGet(keys);
		if(!ListUtil.isEmpty(list))
		{
			MesExportExcel mesExportExcel = new MesExportExcel();
			List<MesSheetConfig> sheetConfigs = new ArrayList<MesSheetConfig>();
			MesSheetConfig mesSheetConfig = null;
			for (int i = 0; i < list.size(); i++) {
				Luckysheet redisCache = JSON.parseObject(String.valueOf(list.get(i)),Luckysheet.class);
				if(redisCache != null) {
					mesSheetConfig = new MesSheetConfig();
					mesSheetConfig.setSheetIndex(redisCache.getSheetIndex());
					String sheetName = redisCache.getSheetName();
					mesSheetConfig.setSheetname(sheetName);
					if(noViewAuthCells.containsKey(mesSheetConfig.getSheetIndex())) {
						mesSheetConfig.setNoViewAuthCells(noViewAuthCells.get(mesSheetConfig.getSheetIndex()));
					}
					if(StringUtil.isNotEmpty(redisCache.getHyperlink()))
					{
						Map<String, Map<String, Object>> hyperlink = JSON.parseObject(redisCache.getHyperlink(), Map.class);
						mesSheetConfig.setHyperlinks(hyperlink);
					}
					if(StringUtil.isNotEmpty(redisCache.getFrozen()))
					{
						JSONObject frozen = JSONObject.parseObject(redisCache.getFrozen());
						mesSheetConfig.setFrozen(frozen);
					}
					if(StringUtil.isNotEmpty(redisCache.getDataverification()))
					{
						JSONObject dataVerification = JSONObject.parseObject(redisCache.getDataverification());
						mesSheetConfig.setDataVerification(dataVerification);
					}
					if(StringUtil.isNotEmpty(redisCache.getImage())) {
						JSONObject images = JSONObject.parseObject(redisCache.getImage());
						mesSheetConfig.setBase64Images(images);
					}
					if(StringUtil.isNotEmpty(redisCache.getFilterSelect()))
					{
						JSONObject filter = JSONObject.parseObject(redisCache.getFilterSelect());
						mesSheetConfig.setFilter(filter);
					}
					if(StringUtil.isNotEmpty(redisCache.getRowlen()))
					{
						JSONObject rowlen = JSONObject.parseObject(redisCache.getRowlen());
						mesSheetConfig.setRowlen(rowlen);
					}
					if(StringUtil.isNotEmpty(redisCache.getColumnlen()))
					{
						JSONObject columnlen = JSONObject.parseObject(redisCache.getColumnlen());
						mesSheetConfig.setColumnlen(columnlen);
					}
					if(StringUtil.isNotEmpty(redisCache.getRowhidden()))
					{
						JSONObject rowhidden = JSONObject.parseObject(redisCache.getRowhidden());
						mesSheetConfig.setRowhidden(rowhidden);
					}
					if(StringUtil.isNotEmpty(redisCache.getColhidden()))
					{
						JSONObject colhidden = JSONObject.parseObject(redisCache.getColhidden());
						mesSheetConfig.setRowhidden(colhidden);
					}
					if(StringUtil.isNotEmpty(redisCache.getBorderInfo()))
					{
						JSONArray borderInfo = JSONObject.parseArray(redisCache.getBorderInfo());
						mesSheetConfig.setBorderInfos(borderInfo);
					}
					if(StringUtil.isNotEmpty(redisCache.getMergeInfo()))
					{
						JSONObject merge = JSONObject.parseObject(redisCache.getMergeInfo());
						mesSheetConfig.setMerge(merge);
					}
					if(StringUtil.isNotEmpty(redisCache.getLuckysheetConditionformatSave()))
					{
						JSONArray luckysheetConditionFormat = JSONObject.parseArray(redisCache.getLuckysheetConditionformatSave());
						mesSheetConfig.setLuckysheetConditionformatSave(luckysheetConditionFormat);
					}
					if(StringUtil.isNotEmpty(redisCache.getChart()))
					{
						JSONArray chart = JSONObject.parseArray(redisCache.getChart());
						mesSheetConfig.setChart(chart);
					}
					Map<String, Integer> maxXAndY = new HashMap<>();
					int maxr = 0;
					int maxc = 0;
					//获取所有的单元格
					List<String> cellKeys = redisUtil.getKeys(RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +model.getGridKey() + "_" +redisCache.getSheetIndex());
					List<Object> cells = redisUtil.multiGet(cellKeys);
					if(!ListUtil.isEmpty(cells)) {
						List<Map<String, Object>> cellDatas = new ArrayList<>();
						for (int j = 0; j < cells.size(); j++) {
							JSONObject cellData = (JSONObject) cells.get(j);
							int r = cellData.getIntValue("r");
							int c = cellData.getIntValue("c");
							if(r>maxr)
							{
								maxr = r;
							}
							if(c > maxc) {
								maxc = c;
							}
							cellDatas.add(cellData);
						}
						mesSheetConfig.setCellDatas(cellDatas);
					}
					maxXAndY.put("maxX",maxr);
					maxXAndY.put("maxY",maxc);
					mesSheetConfig.setMaxXAndY(maxXAndY);
					mesSheetConfig.setIsCoedit(YesNoEnum.YES.getCode());
					sheetConfigs.add(mesSheetConfig);
				}
			}
			mesExportExcel.setSheetConfigs(sheetConfigs);
			ReportExcelUtil.export(mesExportExcel,name,"",httpServletResponse);
		}
		
	}

	/**  
	 * @MethodName: beforeEnterShareMode
	 * @Description: 进入共享模式前调用url，锁定共享模式
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @return
	 * @see com.springreport.api.coedit.ICoeditService#beforeEnterShareMode(com.springreport.dto.coedit.MesDownloadDto, com.springreport.base.UserInfoDto)
	 * @date 2024-01-01 04:32:10 
	 */
	@Override
	public BaseEntity beforeEnterShareMode(MesDownloadDto model, UserInfoDto userInfoDto) {
		BaseEntity result = new BaseEntity();
		String key = RedisPrefixEnum.COEDITSHAREMODE.getCode()+model.getGridKey();
		ShareModeInfo shareModeInfo = new ShareModeInfo();
		shareModeInfo.setGridKey(model.getGridKey());
		shareModeInfo.setUserId(userInfoDto.getUserId());
		shareModeInfo.setUserName(userInfoDto.getUserName());
		shareModeInfo.setUserRealName(userInfoDto.getUserRealName());
		boolean b = redisUtil.setIfAbsent(key, JSONObject.toJSONString(shareModeInfo), Constants.SHAREMODE_TIME);
		if(!b)
		{
			String shareModeInfoStr = (String) redisUtil.get(key);
			shareModeInfo = JSONObject.parseObject(shareModeInfoStr, ShareModeInfo.class);
			throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.sharemode.enter",new String[] {shareModeInfo.getUserName()}));
		}
		return result;
	}
	
	 /**
     * 
     * 数据改动引起有引用公式的单元格数据重新计算更新
     * @param gridKey
     * @param bson
     * @return
     */
    public String Operation_fbv(String gridKey, JSONObject bson,String blockId,WSUserModel wsUserModel) {
       if(!StringUtil.isEmptyMap(bson.getJSONObject("v")))
       {
    	   List<String> keys = null;
    	   LuckysheetCell luckysheetCell = null;
    	   for(Map.Entry entry : bson.getJSONObject("v").entrySet()){
    		   String index = (String) entry.getKey();
    		   JSONArray celldatas = (JSONArray)entry.getValue();
    		   if(ListUtil.isNotEmpty(celldatas))
    		   {
    			   keys = new ArrayList<>();
    			   Map<String, JSONObject> sheetCells = new HashMap<>();
    			   for (int i = 0; i < celldatas.size(); i++) {
    				   JSONObject cellData = celldatas.getJSONObject(i);
    				   int r = cellData.getIntValue("r");
    				   int c = cellData.getIntValue("c");
    				   String key = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +gridKey + "_" + index + "_" +r+ "_" + c;
    				   keys.add(key);
    				   sheetCells.put(r+"_"+c, cellData);
    			   }
    			   List<Object> redisDatas = redisUtil.multiGet(keys);
    			   if(ListUtil.isNotEmpty(redisDatas))
    			   {
    				   Map<String, JSONObject> updateCells = new HashMap<>();
    		    	   List<LuckysheetCell> luckysheetCells = new ArrayList<>();
    				   JSONObject cellData = null;
    				   for (int i = 0; i < redisDatas.size(); i++) {
    					   cellData = (JSONObject) redisDatas.get(i);
    					   int r = cellData.getIntValue("r");
        				   int c = cellData.getIntValue("c");
        				   Long id = cellData.getLongValue("id");
        				   if(sheetCells.containsKey(r+"_"+c))
        				   {
        					   sheetCells.get(r+"_"+c).put("id", id);
            				   String key = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +gridKey + "_" + index + "_" +r+ "_" + c;
            				   updateCells.put(key, sheetCells.get(r+"_"+c));
            				   luckysheetCell = new LuckysheetCell();
            				   luckysheetCell.setId(id);
            				   luckysheetCell.setCellData(JSON.toJSONString(sheetCells.get(r+"_"+c)));
            				   luckysheetCells.add(luckysheetCell);
        				   }
					   }
    				   if(!StringUtil.isEmptyMap(updateCells))
    		    	   {
    		    		   this.mqProcessService.updateRedisBatchCellValueCache(MqTypeEnums.BATCHINSERTCELLDATA.getCode(), updateCells, index, gridKey, blockId,wsUserModel.getToken(),null,null);
    		    	   }
    		    	   if(ListUtil.isNotEmpty(luckysheetCells))
    		    	   {
    		    		   this.mqProcessService.batchProcessCellData(MqTypeEnums.BATCHUPDATECELLDATA.getCode(), luckysheetCells,index, gridKey,blockId);
    		    	   }
    			   }
    		   }
    	   }
    	   
       }
       return "";
    }
    
    /**  
     * @MethodName: getNoAuthKeys
     * @Description: 获取无权限查看的单元格key
     * @author caiyang
     * @param listId
     * @param userInfoDto
     * @return Map<String,Map<String,String>>
     * @date 2024-06-10 09:29:03 
     */ 
    private Map<String, Map<String, String>> getNoAuthKeys(String listId,UserInfoDto userInfoDto){
    	Map<String, Map<String, String>> result = new HashMap<>();
    	Map<String, List<ReportRangeAuth>> noAuthRange = this.iReportRangeAuthUserService.getUserNoAuthRange(listId, userInfoDto);
    	if(!StringUtil.isEmptyMap(noAuthRange)) {
    		ReportRangeAuth rangeAuth = null;
    		for(String key : noAuthRange.keySet()) {
    			List<ReportRangeAuth> rangeAuths = noAuthRange.get(key);
    			Map<String, String> sheetAuth = new HashMap<>();
    			for (int i = 0; i < rangeAuths.size(); i++) {
    				rangeAuth = rangeAuths.get(i);
    				JSONArray rowsNo = JSON.parseArray(rangeAuth.getRowsNo());
    				JSONArray colsNo = JSON.parseArray(rangeAuth.getColsNo());
    				int str = rowsNo.getIntValue(0);
    				int edr = rowsNo.getIntValue(1);
    				int stc = colsNo.getIntValue(0);
    				int edc = colsNo.getIntValue(1);
    				for (int j = str; j <= edr; j++) {
						for (int k = stc; k <= edc; k++) {
							String redisKey = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +listId + "_" + key + "_" +j+ "_" + k;
							sheetAuth.put(redisKey, "1");
						}
					}
				}
    			result.put(key, sheetAuth);
    		}
    	}
    	return result;
    }

	/**  
	 * @MethodName: getRangeValues
	 * @Description: 获取范围内的单元格数据
	 * @author caiyang
	 * @param model
	 * @param userInfoDto
	 * @return
	 * @see com.springreport.api.coedit.ICoeditService#getRangeValues(com.springreport.dto.coedit.RangeValueDto, com.springreport.base.UserInfoDto)
	 * @date 2024-06-12 04:26:47 
	 */
	@Override
	public List<Object> getRangeValues(RangeValueDto model, UserInfoDto userInfoDto) {
		int str = model.getRange().getJSONArray("row").getIntValue(0);
		int edr = model.getRange().getJSONArray("row").getIntValue(1);
		int stc = model.getRange().getJSONArray("column").getIntValue(0);
		int edc = model.getRange().getJSONArray("column").getIntValue(1);
		List<String> keys = new ArrayList<>();
		for (int i = str; i <= edr; i++) {
			for (int j = stc; j <= edc; j++) {
				String cellDataKey = RedisPrefixEnum.DOCOMENTCELLDATA.getCode() +model.getListId() + "_" + model.getSheetIndex() + "_" + i + "_" + j;
				keys.add(cellDataKey);
			}
		}
		List<Object> list = null;
		if(ListUtil.isNotEmpty(keys)) {
			list = redisUtil.multiGet(keys);
		}
		
		return list;
	}

}
