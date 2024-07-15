package com.springreport.util;

import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.alibaba.fastjson.JSONArray;
import com.springreport.constants.StatusCode;
import com.springreport.exception.BizException;

public class VelocityUtil {

	/**
     * 替换的模板中的参数
     *
     * @param template
     * @param parameters
     * @return 替换后的文本
     */
    public static String parse(final String template, final Map<String, Object> parameters) {
        return parse(template, parameters, "report");
    }
    
    /**
     * 替换的模板中的参数
     *
     * @param template
     * @param parameters
     * @param logTag
     * @return 替换后的文本
     */
    public static String parse(final String template, final Map<String, Object> parameters, final String logTag) {
    	if (parameters == null) {
			return template;
		}
    	try (StringWriter writer = new StringWriter()) {
            Velocity.init();
            final VelocityContext context = new VelocityContext();
            for (final Entry<String, Object> kvset : parameters.entrySet()) {
            	if (kvset.getValue() instanceof JSONArray) {
					StringBuffer stringBuffer = new StringBuffer();
					JSONArray valueArray = (JSONArray)kvset.getValue();
					if (valueArray != null && valueArray.size() == 1) {
						stringBuffer.append("'").append(valueArray.get(0)).append("'");
					}else if (valueArray != null && valueArray.size() > 1) {
						for (int i = 0; i < valueArray.size(); i++) {
							if (i == valueArray.size() - 1) {
								stringBuffer.append("'").append(valueArray.get(i)).append("'");
							} else{
								stringBuffer.append("'").append(valueArray.get(i)).append("',");
							}
						}
					}else {
						stringBuffer.append("''");
					}
					context.put(kvset.getKey(), stringBuffer.toString());
				}else {
					if(kvset.getValue() == null)
					{
						kvset.setValue("");
					}
					if (StringUtil.isNotEmpty(kvset.getValue().toString())) {
						context.put(kvset.getKey(), "'" + kvset.getValue() + "'");
					}else {
						context.put(kvset.getKey(), "''");
					}
				}
            }
            Velocity.evaluate(context, writer, logTag, template);
            return writer.toString();
        } catch (final Exception ex) {
            throw new BizException(StatusCode.FAILURE, "模板引擎解析出错，错误信息："+ex.getMessage());
        }
    }
    
    public static String parseInfluxdb(final String template, final Map<String, Object> parameters, final String logTag) {
    	if (parameters == null) {
			return template;
		}
    	try (StringWriter writer = new StringWriter()) {
            Velocity.init();
            final VelocityContext context = new VelocityContext();
            for (final Entry<String, Object> kvset : parameters.entrySet()) {
            	if (kvset.getValue() instanceof JSONArray) {
					StringBuffer stringBuffer = new StringBuffer();
					JSONArray valueArray = (JSONArray)kvset.getValue();
					if (valueArray != null && valueArray.size() == 1) {
						stringBuffer.append(valueArray.get(0));
					}else if (valueArray != null && valueArray.size() > 1) {
						for (int i = 0; i < valueArray.size(); i++) {
							if (i == valueArray.size() - 1) {
								stringBuffer.append(valueArray.get(i));
							} else{
								stringBuffer.append(valueArray.get(i)).append("|");
							}
						}
					}else {
						stringBuffer.append("''");
					}
					context.put(kvset.getKey(), stringBuffer.toString());
				}else {
					if(kvset.getValue() == null)
					{
						kvset.setValue("");
					}
					if (StringUtil.isNotEmpty(kvset.getValue().toString())) {
						context.put(kvset.getKey(),  kvset.getValue());
					}else {
						context.put(kvset.getKey(), "");
					}
				}
            }
            Velocity.evaluate(context, writer, logTag, template);
            return writer.toString();
        } catch (final Exception ex) {
            throw new BizException(StatusCode.FAILURE, "模板引擎解析出错，错误信息："+ex.getMessage());
        }
    }
    
    public static String simpleParse(final String template, final Map<String, Object> parameters) {
    	try (StringWriter writer = new StringWriter()) {
    		  Velocity.init();
              final VelocityContext context = new VelocityContext();
              for (final Entry<String, Object> kvset : parameters.entrySet()) {
            	  if(kvset.getValue() != null)
            	  {
            		  context.put(kvset.getKey(),  kvset.getValue());
            	  }else {
            		  context.put(kvset.getKey(), "");
            	  }
              }
              Velocity.evaluate(context, writer, "report", template);
              return writer.toString();
    	}catch (final Exception ex) {
            throw new BizException(StatusCode.FAILURE, "模板引擎解析出错，错误信息："+ex.getMessage());
        }
    }
}
