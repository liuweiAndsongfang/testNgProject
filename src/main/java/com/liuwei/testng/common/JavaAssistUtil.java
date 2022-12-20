package com.liuwei.testng.common;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.testng.TestException;

import java.util.Arrays;
import java.util.TreeMap;


public class JavaAssistUtil {

    public static final ClassPool POOL = ClassPool.getDefault();
    public JavaAssistUtil(){

    }

    public static String[] getMethodParamNames(Class<?> clazz, String methodName, Class<?>[] paramTypes){
        try {
            CtClass cc = getCTClass(clazz);
            CtMethod cm1 = getMethodParamNames(cc.getDeclaredMethods(), methodName, paramTypes);
            if (cm1 != null) {
                return getMethodParamNames(cm1);
            } else {
                CtMethod cm;
                if (cc != null && cc.getSuperclass() != null) {
                    cm = getMethodParamNames(cc.getSuperclass().getDeclaredMethods(), methodName, paramTypes);
                    if (cm != null) {
                        return getMethodParamNames(cm);
                    }
                }

                cm = cc.getDeclaredMethod(methodName);
                if (cm != null) {
                    return getMethodParamNames(cm);
                } else {
                    CtMethod cm3 = getMethodParamNames(cc.getMethods(), methodName, paramTypes);
                    return getMethodParamNames(cm3);
                }
            }
        } catch (Exception var7) {
            throw new TestException("can't find method[name=" + methodName + ",class=" + clazz + "]", var7);
        }
    }

    public static String[] getMethodParamNames(CtMethod ctMethod){
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute)codeAttribute.getAttribute("LocalVariableTable");

        try {
            String[] paramNames = new String[ctMethod.getParameterTypes().length];
            int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
            TreeMap<Integer, String> sortMap = new TreeMap();

            for(int i = 0; i < attr.tableLength(); ++i) {
                sortMap.put(attr.index(i), attr.variableName(i));
            }

            paramNames = (String[])Arrays.copyOfRange(sortMap.values().toArray(new String[0]), pos, paramNames.length + pos);
            return paramNames;
        } catch (NotFoundException var8) {
            throw new TestException("can't get method param types[method=" + ctMethod.getName() + "]", var8);
        }
    }

    private static CtMethod getMethodParamNames(CtMethod[] ctMethods, String methodName, Class<?>[] paramTypes) throws NotFoundException{
        CtMethod[] var3 = ctMethods;
        int var4 = ctMethods.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            CtMethod cm = var3[var5];
            if (cm.getName().equals(methodName)) {
                CtClass[] ctParamTypes = cm.getParameterTypes();
                if (ctParamTypes != null && paramTypes.length == ctParamTypes.length) {
                    boolean typeEqual = true;

                    for(int i = 0; i < paramTypes.length; ++i) {
                        if (!paramTypes[i].getName().equals(ctParamTypes[i].getName())) {
                            typeEqual = false;
                            break;
                        }
                    }

                    if (typeEqual) {
                        return cm;
                    }
                }
            }
        }

        return null;
    }

    public static CtClass getCTClass(Class<?> clazz){
        try {
            return POOL.get(clazz.getName());
        }catch (NotFoundException var4){
            POOL.appendClassPath(new ClassClassPath(clazz));
            try {
                return POOL.get(clazz.getName());
            }catch (NotFoundException var3){
                throw new TestException("can not find class [name=" + clazz.getName() +"]", var4);
            }
        }
    }
}
