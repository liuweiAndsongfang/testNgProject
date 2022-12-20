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
        return paramNames;
    }
    public static String[] getMethodParams(String className, String methodName) throws ClassNotFoundException {
        Class<?> aClass = Class.forName(className);
        Method[] methods = aClass.getMethods();
        String[] params = null;
        Method[] var5 = methods;
        int var6 = methods.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Method method = var5[var7];
            if (method.getName().equals(methodName)) {
                params = getParameterNames(method);
                if (params != null && params.length == 0) {
                }
                break;
            }
        }

        return params;
    }
}
