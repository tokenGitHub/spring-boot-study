package com.hanhe.study.utils;

import java.lang.reflect.Field;

public interface FilterInterface {
    String filter(String column, Object data);

    //把 Object 转化为 json 字符串
    static String toString(Object object) throws Exception{
        Class clazz = object.getClass();
        String name = clazz.getName();

        Field[] fields = clazz.getDeclaredFields();

        String fieldStr = " fields : { ";

        for(int i = 0; i < fields.length;i++){
            fields[i].setAccessible(true);
            fieldStr += fields[i].getName() + " : " + fields[i].get(object) + " ,";
        }

        fieldStr ="String : { name : " + name + " } , " + fieldStr.substring(0, fieldStr.length() - 1) + " } }";

        return fieldStr;
    }
}
