package com.liuwei.testng.common.testngUtil;

import com.alibaba.fastjson.JSONObject;
import com.liuwei.testng.base.listener.ListenerContext;
import com.liuwei.testng.base.listener.LogMonitor;
import com.liuwei.testng.common.StringUtil;
import org.testng.ITestResult;
import org.testng.log4testng.Logger;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ItestmngUtil {
    private static final String PROTOCOL = "http://";
    private static final String uri = "/itestmng/api";
    private static final String QUERY_CASES_URL = "/itestmng/api/query/allExecuteCases";
    public static final String UPLOAD_RESULT_URL = "/itestmng/api/uploadResult";
    private static final Logger LOGGER = Logger.getLogger(ItestmngUtil.class);

    public ItestmngUtil() {
    }

    public static String getTestCases(Map<String, String> params) throws IOException {
        String host = System.getProperty("REPORT_HOST");
        if (StringUtil.isEmpty(host)) {
            host = "localhost:8081";
        }

        String serverUrl = "http://" + host + "/itestmng/api/query/allExecuteCases";
        return HttpUtil.post(serverUrl, params);
    }

    public static void uploadCaseResult(JSONObject caseObject, String status, ITestResult testResult) {
        if (caseObject != null) {
            String host = System.getProperty("REPORT_HOST");
            if (StringUtil.isEmpty(host)) {
                host = "localhost:8081";
            }

            if (!ListenerContext.getSkipIds().contains(caseObject.getString("caseId"))) {
                try {
                    //此处需要对batchid和executeId进行上报
                    String batchId = System.getProperty("batch_id");
                    String executeId = System.getProperty("execution_id");
                    ConsoleLogger.info(LogMonitor.END_RECORD_LOG, new Object[0]);
                    String caseLog = "IGNORE".equalsIgnoreCase(status) ? "" : LogMonitor.dump();
                    String serverUrl = "http://" + host + "/itestmng/api/uploadResult";
                    boolean success = false;
                    caseObject.put("batchId", batchId);
                    caseObject.put("executeId", executeId);
                    caseObject.put("result", status);
                    caseObject.put("testMode", ListenerContext.getExecuteOption().getTestMode());
                    caseObject.put("timeout", testResult.getEndMillis() - testResult.getStartMillis());
                    caseObject.put("executeTime", DateUtil.getCurrentDateStr());
                    JSONObject extendInfo = new JSONObject();
                    if ("FAIL".equals(status)) {
                        extendInfo.put("exception", testResult.getThrowable() == null ? "null" : testResult.getThrowable().getClass().getName());
                        extendInfo.put("stackTrace", ListenerContext.printResult(testResult) + "\n" + AgentUtil.getStackTrace(testResult.getThrowable()) + "\n" + caseLog);
                    } else {
                        extendInfo.put("stackTrace", ListenerContext.printResult(testResult) + "\n" + AgentUtil.getStackTrace(testResult.getThrowable()) + "\n" + caseLog);
                    }

                    caseObject.put("extendInfo", extendInfo.toJSONString());
                    Map<String, String> params = new HashMap();
                    Iterator var9 = caseObject.entrySet().iterator();

                    while(var9.hasNext()) {
                        Map.Entry<String, Object> entry = (Map.Entry)var9.next();
                        params.put(entry.getKey(), entry.getValue() == null ? "" : String.valueOf(entry.getValue()));
                    }

                    String result = HttpUtil.post(serverUrl, params);
                    if (StringUtil.isNotEmpty(result)) {
                        JSONObject resultObject = JSONObject.parseObject(result);
                        success = resultObject.getBoolean("success");
                    }

                    if (!success) {
                        ConsoleLogger.warn("[UPLOAD FAIL], caseId=" + (String)params.get("caseId") + ", host=" + host + ", result=[" + result + "]", new Object[0]);
                        extendInfo.put("stackTrace", ListenerContext.printResult(testResult) + "\n" + AgentUtil.getStackTrace(testResult.getThrowable()) + "\nlog exceed max size, please view in full log");
                        params.put("extendInfo", extendInfo.toJSONString());
                        result = HttpUtil.post(serverUrl, params);
                        ConsoleLogger.warn("[UPLOAD REPEAT]" + result, new Object[0]);
                    } else {
                        ConsoleLogger.info("[UPLOAD SUCCESS], caseId=" + (String)params.get("caseId") + ", host=" + host + ", result=[" + result + "]", new Object[0]);
                    }
                } catch (Throwable var11) {
                    ConsoleLogger.error("[UPLOAD EXCEPTION], caseId=" + caseObject.getString("caseId") + ", host=" + host + ", e=[" + var11 == null ? "null" : var11.getClass().getName() + "#" + var11.getMessage() + "]", new Object[0]);
                }

            }
        }
    }
}
