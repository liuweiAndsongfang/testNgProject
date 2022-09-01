package com.liuwei.testng.common;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;

public class SpringMethodUtil {
    public  SpringMethodUtil() {
    }

    public static String[] getParameterNames(Method method){
        LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = parameterNameDiscoverer.getParameterNames(method);
        parameterNameDiscoverer = null;
        return  paramNames;
    }

}
