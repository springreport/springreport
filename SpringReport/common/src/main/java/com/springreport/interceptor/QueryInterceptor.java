package com.springreport.interceptor;
import com.alibaba.fastjson.JSONObject;
import com.springreport.base.BaseEntity;
import com.springreport.util.EscapeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**  
 * @ClassName: QueryInterceptor
 * @Description: 自定义拦截器方法，处理模糊查询中包含特殊字符（_、%、\）和前后空格
 * @author caiyang
 * @date 2020-08-26 07:49:36 
*/  
@Intercepts({
	@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
	@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class,BoundSql.class}),
	@Signature(type = Executor.class, method = "queryCursor", args = {MappedStatement.class, Object.class, RowBounds.class})
})

public class QueryInterceptor implements Interceptor{

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		try {
			// 拦截sql
	        Object[] args = invocation.getArgs();
	        MappedStatement statement = (MappedStatement)args[0];
	        Object parameterObject = args[1];
	        BoundSql boundSql = statement.getBoundSql(parameterObject);
	        String sql = boundSql.getSql();
	        // 处理特殊字符
	        modifyLikeSql(sql, parameterObject, boundSql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
        // 返回
        return invocation.proceed();
	}
	
	@Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    @SuppressWarnings("unchecked")
    public static String modifyLikeSql(String sql, Object parameterObject, BoundSql boundSql) {
        if (parameterObject instanceof HashMap) {
        } else {
        	if(parameterObject instanceof BaseEntity)
        	{
        		JSONObject jsonObject = (JSONObject) JSONObject.toJSON(parameterObject);
        		JSONObject param = new JSONObject();
        		Set<String> set = jsonObject.keySet(); 
        		Iterator<String> it = set.iterator();
        		while (it.hasNext()) {
        			String key = it.next();
        			Object value = jsonObject.get(key);
        			if(value != null) {
        				if (value instanceof String && (value.toString().contains("_") || value.toString().contains("\\") || value.toString()
        	                    .contains("%"))) {
        					param.put(key, EscapeUtil.escapeChar(value.toString()));
        				}else {
        					param.put(key, value);
        				}
        			}
        		}
        		try {
					Object result = parameterObject.getClass().newInstance();
					result = JSONObject.toJavaObject(param, parameterObject.getClass());
					BeanUtils.copyProperties(result, parameterObject);
				} catch (Exception e) {
					e.printStackTrace();
				} 
        	}
            return sql;
        }
        //将
        if (!sql.toLowerCase().contains(" like ") || !sql.toLowerCase().contains("?")) {
            return sql;
        }
        // 获取关键字的个数（去重）
        String[] strList = sql.split("\\?");
        Set<String> keyNames = new HashSet<>();
        for (int i = 0; i < strList.length; i++) {
            if (strList[i].toLowerCase().contains(" like ")) {
                String keyName = boundSql.getParameterMappings().get(i).getProperty();
                keyNames.add(keyName);
            }
        }
        // 对关键字进行特殊字符“清洗”，如果有特殊字符的，在特殊字符前添加转义字符（\）
        for (String keyName : keyNames) {
            HashMap parameter = (HashMap)parameterObject;
            if (keyName.contains("ew.paramNameValuePairs.") && sql.toLowerCase().contains(" like ?")) {
                // 第一种情况：在业务层进行条件构造产生的模糊查询关键字
                QueryWrapper wrapper = (QueryWrapper)parameter.get("ew");
                parameter = (HashMap)wrapper.getParamNameValuePairs();

                String[] keyList = keyName.split("\\.");
                // ew.paramNameValuePairs.MPGENVAL1，截取字符串之后，获取第三个，即为参数名
                Object a = parameter.get(keyList[2]);
                if (a instanceof String && (a.toString().contains("_") || a.toString().contains("\\") || a.toString()
                    .contains("%"))) {
                    parameter.put(keyList[2],
                        "%" + EscapeUtil.escapeChar(a.toString().substring(1, a.toString().length() - 1)) + "%");
                }
            } else if (!keyName.contains("ew.paramNameValuePairs.") && sql.toLowerCase().contains(" like ?")) {
                // 第二种情况：未使用条件构造器，但是在service层进行了查询关键字与模糊查询符`%`手动拼接
                Object a = parameter.get(keyName);
                if (a instanceof String && (a.toString().contains("_") || a.toString().contains("\\") || a.toString()
                    .contains("%"))) {
                    parameter.put(keyName,
                        "%" + EscapeUtil.escapeChar(a.toString().substring(1, a.toString().length() - 1)) + "%");
                }
            } else {
                // 第三种情况：在Mapper类的注解SQL中进行了模糊查询的拼接
                Object a = parameter.get(keyName);
                if (a instanceof String && (a.toString().contains("_") || a.toString().contains("\\") || a.toString()
                    .contains("%")) &&(!a.toString().contains("/%") && !a.toString().contains("/_"))) {
                    parameter.put(keyName, EscapeUtil.escapeChar(a.toString()));
                }
            }
        }
        return sql;
    }
}
