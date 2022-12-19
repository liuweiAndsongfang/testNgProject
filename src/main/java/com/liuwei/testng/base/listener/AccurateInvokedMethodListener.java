package com.liuwei.testng.base.listener;

import com.alibaba.fastjson.JSONObject;

import com.liuwei.testng.base.LogUtils;
import com.liuwei.testng.common.testngUtil.ConsoleLogger;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

public class AccurateInvokedMethodListener implements IInvokedMethodListener {
    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult testResult) {
        if (this.isTestCase(iInvokedMethod, testResult)) {
            ConsoleLogger.info("ITestNGListener: IInvokedMethodListener --> beforeInvocation[" + testResult.getMethod().getRealClass().getName() + "." + testResult.getMethod().getConstructorOrMethod().getMethod().getName() + "][" + testResult.getMethod().getClass().getName() + "]", new Object[0]);
            JSONObject caseObject = ListenerContext.parseTestCase(testResult);
            if (this.skipTest(caseObject)) {
                ListenerContext.getSkipIds().add(caseObject.getString("caseId"));
                ConsoleLogger.warn("[ACCURATE_SKIP] [" + caseObject.get("caseId") + "]", new Object[0]);
                pushProxyDataProviderNext(testResult);
                throw new SkipException("ACCURATE_SKIP");
            }

        }
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {

    }
    private boolean isTestCase(IInvokedMethod iInvokedMethod, ITestResult iTestResult){
        boolean isNotTestCase = "org.testng.internal.ConfigurationMethod".equals(iTestResult.getMethod().getClass().getName())||iInvokedMethod.isConfigurationMethod();
        return !isNotTestCase;
    }

    private boolean skipTest(JSONObject caseObject) {
        if (caseObject == null || !"accurate".equals(ListenerContext.getExecuteOption().getTestMode()) && !"remote".equalsIgnoreCase(ListenerContext.getExecuteOption().getTestMode())) {
            return false;
        } else if ("accurate".equalsIgnoreCase(ListenerContext.getExecuteOption().getTestMode())) {
            return this.skipAccurateTest(caseObject);
        } else {
            return "remote".equalsIgnoreCase(ListenerContext.getExecuteOption().getTestMode()) ? this.skipRemoteTest(caseObject) : false;
        }
    }

    private boolean skipRemoteTest(JSONObject caseObject) {
        try {
            String caseType = caseObject.getString("caseType");
            String casePath = caseObject.getString("casePath");
            LogUtils.info("执行的case的casePath is:"+casePath);
            if ("ITEST".equalsIgnoreCase(caseType) && !ListenerContext.getCaseIds().isEmpty()) {
                return this.skipAccurateTest(caseObject);
            } else if (ListenerContext.getTestMethods().isEmpty()) {
                return false;
            } else {
                boolean iscContain = ListenerContext.getTestMethods().contains(casePath);
                LogUtils.info("执行的case is:"+casePath +";是否被包含"+iscContain);
                return !iscContain;
            }
        } catch (Exception var4) {
            ConsoleLogger.error("skip remote test exception, e=[" +
                     "]", new Object[0]);
            return false;
        }
    }

    private boolean skipAccurateTest(JSONObject caseObject){
        try {
            if (ListenerContext.getCaseIds().isEmpty()) {
                return true;
            } else {
                String caseId = caseObject.getString("caseId");
                if (caseId.contains("#")) {
                    String originCaseId = caseId.substring(0, caseId.lastIndexOf(35));
                    return !ListenerContext.getCaseIds().contains(originCaseId);
                } else {
                    return !ListenerContext.getCaseIds().contains(caseId);
                }
            }
        } catch (Exception var4) {
            ConsoleLogger.error("skip accurate test exception, e=["  + "]", new Object[0]);
            return false;
        }
    }

    private void pushProxyDataProviderNext(ITestResult testResult) {
        try {
            Method method = testResult.getMethod().getConstructorOrMethod().getMethod();
            if (method.isAnnotationPresent(Test.class)) {
                String dataProvider = ((Test)method.getAnnotation(Test.class)).dataProvider();
                if (dataProvider == null || dataProvider.isEmpty()) {
                    return;
                }

                Object instance = testResult.getInstance();
                Field field = instance.getClass().getDeclaredField("CGLIB$CALLBACK_0");
                if (field != null) {
                    field.setAccessible(true);
                    Object proxy = field.get(instance);
                    if (proxy != null) {
                        field = proxy.getClass().getDeclaredField("data");
                        if (field != null) {
                            field.setAccessible(true);
                            ThreadLocal data = (ThreadLocal)field.get(proxy);
                            if (data != null && data.get() != null) {
                                Iterator iterator = (Iterator)((Map)data.get()).get(dataProvider);
                                if (iterator != null) {
                                    iterator.next();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable var9) {
            //ConsoleLogger.error("next proxy dataProvider error:" + var9, new Object[0]);
        }

    }
}
