package com.liuwei.testng.common;
import org.apache.commons.lang3.StringUtils;

public class StringUtil extends StringUtils{

    public static String subStingAfter(String str, String separator){
        if(str != null && str.length() != 0){
            if (separator == null){
                return "";
            }else {
                int pos = str.indexOf(separator);
                return pos == -1? "" : str.substring(pos + separator.length());
            }
        }else{
            return str;
        }
    }
    public static String toUpperCase(String str){
        return str == null? null:str.toUpperCase();
    }

    public static boolean isBlank(String str){
        int strLen;
        if(str != null && (strLen = str.length()) != 0){
            for(int i= 0; i < strLen; ++i){
                if(!Character.isWhitespace(str.charAt(i))){
                    return false;
                }
            }
            return true;
        }else {
            return true;
        }
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return str != null && str.length() > 0;
    }

    public static boolean isNotBlank(String str) {
        int length;
        if (str != null && (length = str.length()) != 0) {
            for(int i = 0; i < length; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static String defaultIfBank(String str, String defaultStr){
        return isBlank(str) ? defaultStr : str;
    }
}
