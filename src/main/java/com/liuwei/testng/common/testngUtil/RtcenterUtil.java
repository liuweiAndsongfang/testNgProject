package com.liuwei.testng.common.testngUtil;

import com.alibaba.fastjson.JSONObject;
import com.liuwei.testng.base.listener.ExecuteOption;
import com.liuwei.testng.base.listener.ListenerContext;
import com.liuwei.testng.base.listener.LogMonitor;
import com.liuwei.testng.common.StringUtil;
import org.testng.ITestResult;
import org.testng.log4testng.Logger;
import java.io.IOException;
import java.util.*;

public class RtcenterUtil {
    private static final String PROTOCOL = "http://";
    private static final String uri = "/rtcenter/api";
    private static final String QUERY_CASES_URL = "/rtcenter/api/query/allExecuteCases";
    private static final String UPLOAD_RESULT_URL = "/rtcenter/api/uploadResult";
    private static final String APEND_RESULT_URL = "/rtcenter/api/appendCaseResult";
    private static final Logger LOGGER = Logger.getLogger(RtcenterUtil.class);
    private static List<Map<String, String>> caseResultList = new Vector();

    public RtcenterUtil() {
    }

    public static String getTestCases(Map<String, String> params) throws IOException {
        String host = System.getProperty("itest.itestmng_host");
        if (StringUtil.isEmpty(host)) {
            host = "jarvis.alipay.net";
        }

        String serverUrl = "http://" + host + "/rtcenter/api/query/allExecuteCases";
        return HttpUtil.post(serverUrl, params);
    }

    public static void uploadCaseResult(JSONObject caseObject, String status, ITestResult testResult) {
        if (caseObject != null) {
            String host = System.getProperty("itest.itestmng_host");
            if (StringUtil.isEmpty(host)) {
                host = "jarvis.alipay.net";
            }

            if (!ListenerContext.getSkipIds().contains(caseObject.getString("caseId"))) {
                try {
                    String caseLog = LogMonitor.dump();
                    String serverUrl = "http://" + host + "/rtcenter/api/uploadResult";
                    boolean success = false;
                    ExecuteOption executeOption = ListenerContext.getExecuteOption();
                    caseObject.put("result", status);
                    caseObject.put("testMode", executeOption.getTestMode());
                    caseObject.put("timeout", testResult.getEndMillis() - testResult.getStartMillis());
                    caseObject.put("labId", StringUtil.defaultIfBank(executeOption.getExecutionId(), executeOption.getAqcLabId()));
                    caseObject.put("executeTime", DateUtil.getCurrentDateStr());
                    JSONObject extendInfo = new JSONObject();
                    if (!"FAIL".equals(status) && !"IGNORE".equalsIgnoreCase(status)) {
                        extendInfo.put("stackTrace", ListenerContext.printResult(testResult) + "\n" + AgentUtil.getStackTrace(testResult.getThrowable()) + "\n" + caseLog);
                    } else {
                        extendInfo.put("exception", testResult.getThrowable() == null ? "null" : testResult.getThrowable().getClass().getName());
                        extendInfo.put("stackTrace", ListenerContext.printResult(testResult) + "\n" + AgentUtil.getStackTrace(testResult.getThrowable()) + "\n" + caseLog);
                    }

                    caseObject.put("extendInfo", extendInfo.toJSONString());
                    Map<String, String> params = new HashMap();
                    Iterator var10 = caseObject.entrySet().iterator();

                    while(var10.hasNext()) {
                        Map.Entry<String, Object> entry = (Map.Entry)var10.next();
                        params.put(entry.getKey(), entry.getValue() == null ? "" : String.valueOf(entry.getValue()));
                    }

                    String result = HttpUtil.post(serverUrl, params);
                    if (StringUtil.isNotEmpty(result)) {
                        JSONObject resultObject = JSONObject.parseObject(result);
                        success = resultObject.getBoolean("success");
                    }

                    if (!success) {
                        ConsoleLogger.warn("[UPLOAD FAIL], caseId={0}, host={1}, result=[{2}]", new Object[]{params.get("caseId"), host, result});
                        caseResultList.add(params);
                    } else {
                        ConsoleLogger.info("[UPLOAD SUCCESS], caseId={0}, host={1}, result=[{2}]", new Object[]{params.get("caseId"), host, result});
                    }
                } catch (Throwable var12) {
                    ConsoleLogger.error("[UPLOAD EXCEPTION], caseId=" + caseObject.getString("caseId") + ", host=" + host + ", e=[" + var12 == null ? "null" : var12.getClass().getName() + "#" + var12.getMessage() + "]", new Object[0]);
                }

            }
        }
    }

    public static void uploadAgentMonitor(String agentServiceUrl) {
        String host = System.getProperty("itest.itestmng_host");
        if (StringUtil.isBlank(host)) {
            host = "jarvis.alipay.net";
        }

        String serverUrl = "http://" + host + agentServiceUrl;

        try {
            Map<String, String> params = new HashMap();
            params.put("batchId", System.getProperty("itest.batch_id"));
            params.put("executionId", System.getProperty("itest.execution_id"));
            params.put("ip", (new HostInfo()).getAddress());
            String result = HttpUtil.post(serverUrl, params);
            ConsoleLogger.info("[UPLOAD TEST STEP], url=[{0}], result=[{1}] ", new Object[]{serverUrl, result});
        } catch (Throwable var5) {
            ConsoleLogger.error("[UPLOAD TEST STEP] exception, url=[{0}], e=[{1}]", new Object[]{serverUrl, var5 == null ? "null" : var5.getClass().getName() + "#" + var5.getMessage()});
        }

    }

    public static void appendCaseResult() {
        String host = System.getProperty("itest.itestmng_host");
        if (StringUtil.isBlank(host)) {
            host = "jarvis.alipay.net";
        }

        String serverUrl = "http://" + host + "/rtcenter/api/appendCaseResult";

        try {
            if (caseResultList.isEmpty()) {
                return;
            }

            Map<String, String> params = new HashMap();
            params.put("batchId", System.getProperty("itest.batch_id"));
            params.put("executionId", System.getProperty("itest.execution_id"));
            params.put("ip", (new HostInfo()).getAddress());
            params.put("caseResultList", JSONObject.toJSONString(caseResultList));
            String result = HttpUtil.post(serverUrl, params);
            caseResultList.clear();
            ConsoleLogger.info("[APPEND CASE RESULT], url=[{0}], result=[{1}] ", new Object[]{serverUrl, result});
        } catch (Throwable var4) {
            ConsoleLogger.error("[APPEND CASE RESULT] exception, url=[{0}], e=[{1}]", new Object[]{serverUrl, var4 == null ? "null" : var4.getClass().getName() + "#" + var4.getMessage()});
        }

    }

    public static void main(String[] args) {
        ConsoleLogger.success("hello world", new Object[0]);
    }
}
