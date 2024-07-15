package com.springreport.config;

import java.util.Map;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.MapWrapper;

public class MapKeyLowerWrapper extends MapWrapper {
	 
    public MapKeyLowerWrapper(MetaObject metaObject, Map<String, Object> map) {
        super(metaObject, map);
    }
 
    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        return name==null?"":name.toLowerCase() ;
    }
}
