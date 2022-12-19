package com.liuwei.testng.base.listener;

import com.alibaba.fastjson.JSONObject;
import com.liuwei.testng.base.LogUtils;
import com.liuwei.testng.common.StringUtil;
import com.liuwei.testng.common.testngUtil.ClientUtil;
import com.liuwei.testng.common.testngUtil.ConsoleLogger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import java.util.ArrayList;
import java.util.List;

public class AccurateTestListener extends TestListenerAdapter {
    public static final String RESULT_SUCCESS = "SUCCESS";
    public static final String RESULT_FAIL = "FAIL";
    public static final String RESULT_SKIP = "IGNORE";
    public static final String MANUAL_SKIP = "MANUAL_SKIP";
    public static final List<String> JUNIT_TEST_METHOD_LIST = new ArrayList();


    public void onTestSuccess(ITestResult testResult) {
        ConsoleLogger.info("ITestNGListener: ITestListener --> onTestSuccess[" + testResult.getMethod().getRealClass().getName() + "." + testResult.getMethod().getConstructorOrMethod().getName() + "][" + testResult.getMethod().getClass().getName() + "]", new Object[0]);
        JSONObject caseObject = ListenerContext.parseTestCase(testResult);
        if (caseObject != null) {
            LogUtils.info(caseObject.toJSONString());
            ClientUtil.uploadCaseResult(caseObject, "SUCCESS", testResult);
        }
    }

    public void onTestFailure(ITestResult testResult) {
        ConsoleLogger.warn("ITestNGListener: ITestListener --> onTestFailure[" + testResult.getMethod().getRealClass().getName() + "." + testResult.getMethod().getConstructorOrMethod().getName() + "][" + testResult.getMethod().getClass().getName() + "]", new Object[0]);
        JSONObject caseObject = ListenerContext.parseTestCase(testResult);
        if (caseObject != null) {
            ClientUtil.uploadCaseResult(caseObject, "FAIL", testResult);
        }
    }

    public void onTestSkipped(ITestResult testResult) {
        ConsoleLogger.warn("ITestNGListener: ITestListener --> onTestSkipped[" + testResult.getMethod().getRealClass().getName() + "." + testResult.getMethod().getConstructorOrMethod().getName() + "][" + testResult.getMethod().getClass().getName() + "]", new Object[0]);
        String message = testResult.getThrowable() != null ? testResult.getThrowable().getMessage() : null;
        JSONObject caseObject = ListenerContext.parseTestCase(testResult);
        if (caseObject == null) {
            ConsoleLogger.warn("case object is null, testMethod [" + testResult.getMethod().getRealClass().getName() + "." + testResult.getMethod().getConstructorOrMethod().getName() + "][" + testResult.getMethod().getClass().getName() + "], skipMessage[" + message + "]", new Object[0]);
        } else {
            if (StringUtil.isNotBlank(message) && message.startsWith("MANUAL_SKIP")) {
                ConsoleLogger.warn("[MANUAL_SKIP] [" + caseObject.getString("caseId") + "] " + message, new Object[0]);
                ClientUtil.uploadCaseResult(caseObject, "SUCCESS", testResult);
            } else {
                if ("ACCURATE_SKIP".equalsIgnoreCase(message)) {
                    ConsoleLogger.warn("[ACCURATE_SKIP] [" + caseObject.get("caseId") + "]", new Object[0]);
                    return;
                }

                ClientUtil.uploadCaseResult(caseObject, "IGNORE", testResult);
            }

        }
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult testResult) {
    }

    public void onStart(ITestContext context) {
        ListenerContext.init();
        ConsoleLogger.info("ITestNGListener --> onStart", new Object[0]);
    }

    public void onFinish(ITestContext testContext) {
        ConsoleLogger.info("ITestNGListener: ITestListener --> onFinish[{0}min]", new Object[]{(System.currentTimeMillis() - ListenerContext.getStart()) / 60000L});
//        if ("V2".equalsIgnoreCase(System.getProperty("itest.agent_version"))) {
//            RtcenterUtil.appendCaseResult();
//        }

    }
}
