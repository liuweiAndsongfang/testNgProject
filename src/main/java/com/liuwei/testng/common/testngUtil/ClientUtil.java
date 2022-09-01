package com.liuwei.testng.common.testngUtil;

import com.alibaba.fastjson.JSONObject;
import com.liuwei.testng.base.listener.ListenerContext;
import org.testng.ITestResult;

import java.io.IOException;
import java.util.Map;

public class ClientUtil {
    public static final String ITESTMNG_VERSION = "V1";
    public static final String RTCENTER_VERSION = "V2";

    public ClientUtil() {
    }

    public static void uploadCaseResult(JSONObject caseObject, String status, ITestResult testResult) {
        if ("V2".equalsIgnoreCase(ListenerContext.getExecuteOption().getVersion())) {
            RtcenterUtil.uploadCaseResult(caseObject, status, testResult);
        } else {
            ItestmngUtil.uploadCaseResult(caseObject, status, testResult);
        }

    }

    public static String fetchTestCases(Map<String, String> params) throws IOException {
        return "V2".equalsIgnoreCase(ListenerContext.getExecuteOption().getVersion()) ? RtcenterUtil.getTestCases(params) : ItestmngUtil.getTestCases(params);
    }
}
