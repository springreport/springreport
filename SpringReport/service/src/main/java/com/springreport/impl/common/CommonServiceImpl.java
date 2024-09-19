package com.springreport.impl.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.springreport.api.common.ICommonService;
import com.springreport.constants.StatusCode;
import com.springreport.dto.common.ApiRequestDto;
import com.springreport.dto.common.PrintApiRequestDto;
import com.springreport.enums.RedisPrefixEnum;
import com.springreport.enums.RequestTypeEnum;
import com.springreport.enums.ResultTypeEnum;
import com.springreport.enums.YesNoEnum;
import com.springreport.exception.BizException;
import com.springreport.util.CheckUtil;
import com.springreport.util.DateUtil;
import com.springreport.util.DocumentToLuckysheetUtil;
import com.springreport.util.FileUtil;
import com.springreport.util.HttpClientUtil;
import com.springreport.util.ListUtil;
import com.springreport.util.Md5Util;
import com.springreport.util.MessageUtil;
import com.springreport.util.RedisUtil;
import com.springreport.util.StringUtil;
import com.springreport.util.UUIDUtil;

/**  
 * @ClassName: CommonServiceImpl
 * @Description: 共通服务实现
 * @author caiyang
 * @date 2021-07-13 06:46:41 
*/  
@Service
public class CommonServiceImpl implements ICommonService{

	/**
     * 本地保存路径
     */
	@Value("${file.path}")
    private String dirPath;
	
	@Autowired
	private RedisUtil redisUtil;
	
	/**  
	 * @Title: upload
	 * @Description: 上传文件
	 * @param file
	 * @return 
	 * @see com.caiyang.api.common.ICommonService#upload(org.springframework.web.multipart.MultipartFile) 
	 * @author caiyang
	 * @throws IOException 
	 * @date 2021-07-28 07:22:09 
	 */
	@Override
	public Object upload(MultipartFile file) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		InputStream inputStream2 = file.getInputStream();
		//文件后缀
		String fileExt = cn.hutool.core.io.FileUtil.extName(file.getOriginalFilename());
		String filename = IdWorker.getIdStr()+"."+fileExt;
		String date = DateUtil.getNow(DateUtil.FORMAT_LONOGRAM);
		File dest = new File(dirPath + date + "/" + filename);
		FileUtil.createFile(dest);
		file.transferTo(dest);
		 //拼接上传文件路径
        String fileUri = MessageUtil.getValue("file.url.prefix")+date+"/"+filename+"?t="+System.currentTimeMillis();
        result.put("fileUri", fileUri);
		BufferedImage image = ImageIO.read(inputStream2);
		int width = image.getWidth();
		int height = image.getHeight();
        result.put("width", width);
        result.put("height", height);
		return result;
	}
	
	/**  
	 * @MethodName: upload
	 * @Description: 字节流上传图片
	 * @author caiyang
	 * @param bytes
	 * @param fileName
	 * @return
	 * @see com.springreport.api.common.ICommonService#upload(byte[], java.lang.String)
	 * @date 2023-11-11 10:48:38 
	 */
	@Override
	public Map<String, String> upload(byte[] bytes, String fileName) {
		Map<String, String> result = new HashMap<String, String>();
		String date = DateUtil.getNow(DateUtil.FORMAT_LONOGRAM);
		File dest = new File(dirPath + date + "/" + fileName);
		FileUtil.createFile(dest);
		cn.hutool.core.io.FileUtil.writeBytes(bytes, dest);
		 //拼接上传文件路径
        String fileUri = MessageUtil.getValue("file.url.prefix")+date+"/"+fileName+"?t="+System.currentTimeMillis();
        result.put("fileUri", fileUri);
		return result;
	}
	
	
	/**  
	 * @Title: upload
	 * @Description: 上传文件
	 * @param file
	 * @return 
	 * @see com.caiyang.api.common.ICommonService#upload(org.springframework.web.multipart.MultipartFile) 
	 * @author caiyang
	 * @throws IOException 
	 * @date 2021-07-28 07:22:09 
	 */
	@Override
	public Object uploadFile(MultipartFile file) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		//文件后缀
		String fileExt = cn.hutool.core.io.FileUtil.extName(file.getOriginalFilename());
		String filename = IdWorker.getIdStr()+"."+fileExt;
		String date = DateUtil.getNow(DateUtil.FORMAT_LONOGRAM);
		File dest = new File(dirPath + date + "/" + filename);
		FileUtil.createFile(dest);
		file.transferTo(dest);
		 //拼接上传文件路径
        String fileUri = MessageUtil.getValue("file.url.prefix")+date+"/"+filename+"?t="+System.currentTimeMillis();
        result.put("fileUri", fileUri);
        result.put("fileName", file.getOriginalFilename());
		return result;
	}
	
	@Override
	public Object apiTest(ApiRequestDto apiRequestDto) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(!ListUtil.isEmpty(apiRequestDto.getParams()))
		{
			for (int i = 0; i < apiRequestDto.getParams().size(); i++) {
				if(apiRequestDto.getParams().get(i).get("paramCode") != null && StringUtil.isNotEmpty(String.valueOf(apiRequestDto.getParams().get(i).get("paramCode"))))
				{
					if(apiRequestDto.getParams().get(i).get("defaultValue") != null && StringUtil.isNotEmpty(String.valueOf(apiRequestDto.getParams().get(i).get("defaultValue"))))
					{
						params.put(String.valueOf(apiRequestDto.getParams().get(i).get("paramCode")), String.valueOf(apiRequestDto.getParams().get(i).get("defaultValue")));
					}
					
				}
			}
		}
		String result = "";
		if(RequestTypeEnum.POST.getCode().equals(apiRequestDto.getRequestType().toLowerCase()))
		{//post请求
			result = HttpClientUtil.doPostJson(apiRequestDto.getUrl(),JSONObject.toJSONString(params));
		}else {
			result = HttpClientUtil.doGet(apiRequestDto.getUrl(), params);
		}
		return result;
	}

	/**  
	 * @MethodName: parseXlsxByUrl
	 * @Description: 通过url解析xlsx文件
	 * @author caiyang
	 * @param model
	 * @return
	 * @throws Exception 
	 * @see com.springreport.api.common.ICommonService#parseXlsxByUrl(java.util.Map)
	 * @date 2024-09-18 10:55:34 
	 */
	@Override
	public JSONArray parseXlsxByUrl(JSONObject model) throws Exception {
		JSONArray result = null;
		String url = model.getString("url");
		String redisKey = RedisPrefixEnum.ATTACHMENTCACHE.getCode() + Md5Util.generateMd5(url);
		if(redisUtil.hasKey(redisKey)) {
			result = JSONArray.parseArray(String.valueOf(redisUtil.get(redisKey)));
		}else {
			InputStream input = new URL(url).openStream();
			String fileType = model.getString("fileType");
			if("xlsx".equals(fileType)) {
				result =  DocumentToLuckysheetUtil.xlsx2Luckysheet(input);
			}else if("csv".equals(fileType)) {
				result =  DocumentToLuckysheetUtil.csv2Luckysheet(input,model.getString("fileName"));
			}
			redisUtil.set(redisKey, JSON.toJSONString(result), 86400);
		}
		return result;
	}
}
