package com.springreport.config;

import java.util.Map;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

public class MapWrapperFactory implements ObjectWrapperFactory {
    @Override
    public boolean hasWrapperFor(Object o) {
        return o != null && o instanceof Map;
    }
 
    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object o) {
        return new MapKeyLowerWrapper(metaObject, (Map) o);
    }
}