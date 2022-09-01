package com.liuwei.testng.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternHelper {

    public static final String pattern_ctoken = "ctoken=([-_A-Za-z0-9]+)";
    public static final String pattern_cookie = "ALIPAYJSESSIONID=([-_A-Za-z0-9]+)";

    public static String matchValue(String pat, String data){
        Pattern p = Pattern.compile(pat);
        Matcher m = p.matcher(data);
        String patternValue = null;
        if (m.find()){
            patternValue = m.group(1);
        }
        return patternValue;
    }
}
