package com.liuwei.testng.DB;

import com.liuwei.testng.common.StringUtil;

import java.math.BigDecimal;
import java.util.HashMap;

public class DataMap extends HashMap<String, Object> {

    public DataMap(){

    }
    public String getStringValue(String key){
        key = convertKey(key);
        String value = null;
        if(this.containsKey(key)){
            Object o = this.get(key);
            if(o == null){
                return null;
            }
            if(o instanceof BigDecimal){
                value = ((BigDecimal)o).toString();
            }else {
                value = String.valueOf(o);
            }
        }
        return value;
    }

    public int getIntValue(String key){
        key = convertKey(key);
        String stringValue = this.getStringValue(key);
        int value = Integer.parseInt(stringValue);
        return value;
    }

    public long getLongValue(String key){
        key = convertKey(key);
        String stringValue = this.getStringValue(key);
        long value = Long.parseLong(stringValue);
        return value;
    }

    public Object get(String key){
        String k = convertKey(key);
        return super.get(k);
    }

    public static String convertKey(String key){
        return StringUtil.isBlank(key)? null: key.toUpperCase();
    }
}
