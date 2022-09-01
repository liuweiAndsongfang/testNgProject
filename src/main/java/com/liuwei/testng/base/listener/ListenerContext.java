package com.liuwei.testng.base.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liuwei.testng.common.JavaAssistUtil;
import com.liuwei.testng.common.SpringMethodUtil;
import com.liuwei.testng.common.StringUtil;
import com.liuwei.testng.common.testngUtil.ClientUtil;
import com.liuwei.testng.common.testngUtil.ConsoleLogger;
import com.liuwei.testng.common.testngUtil.Md5Util;
import java.lang.String;
import org.testng.annotations.Test;
import com.liuwei.testng.base.LogUtils;
import org.testng.ITestResult;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ListenerContext {
    private static boolean init = false;
    private static  long start = 0L;
    private static ExecuteOption executeOption = new ExecuteOption();
    private static Set<String> sameIds = new HashSet<>();
    private static Map<String, JSONObject> caseMap = new ConcurrentHashMap<>();
    private static List<String> caseIds = new ArrayList<>();
    private static List<String> testMethods = new ArrayList<>();
    private static Set<String> skipIds = new HashSet<>();

    public ListenerContext(){

    }

    public static void init(){
        if (!init) {
            Class var0 = ListenerContext.class;
            synchronized(ListenerContext.class) {
                if (!init) {
                    LogUtils.info("ListenerContext init start");
                    initExecuteOption();
                    initCaseCache();
                    //initLogFile();
                    start = System.currentTimeMillis();
                    init = true;
                }
            }
        }
    }

    private static void initExecuteOption(){
        //这些参数是在工程buil之前，通过Jenkins的shell命令将这些参数设置到环境变量中，如batch_id是新建Jenkins的配置参数中shell设置的，实现了参数的传递
        executeOption.setBatchId(System.getProperty("batch_id"));
        executeOption.setAqcLabId(System.getProperty("aqc_lab"));
        executeOption.setTestMode(System.getProperty("test_mode"));
        executeOption.setExecutionId(System.getProperty("execution_id"));
    }

    public static synchronized JSONObject parseTestCase(ITestResult testResult){
        if(testResult.getAttribute("currentCase") != null){
            return (JSONObject)testResult.getAttribute("currentCase");
        }else {
            Object[] parameters = testResult.getParameters();
            JSONObject caseObject = null;
            String caseType = null;
            Test testNg = null;
            try{
                String caseName = null;
                String testMethod = testResult.getMethod().getRealClass().getSimpleName() + "." + testResult.getMethod().getMethodName();
                String casePath = testResult.getMethod().getRealClass().getSimpleName() + "." + testResult.getMethod().getMethodName();
                String caseId = null;
                caseId = casePath;
                caseName = testMethod;
                caseType = "TESTNG";
                testNg = (Test) testResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class);
                if(testNg != null){
                    String subId = parseCaseId(parameters,testResult);
                    if(!"CsvDataProvider".equals(testNg.dataProvider()) && !"YamlDataProvider".equals(testNg.dataProvider())){
                        if("ActsDataProvider".equals(testNg.dataProvider())){
                            caseType = "ACTS";
                        }else {
                            caseType = "ATS";
                        }
                        if(subId != null){
                            caseId = casePath +"."+ subId;
                            caseName = subId;
                        }
                    }
                    if(!getSameIds().contains(caseId)){
                        caseObject = (JSONObject) getCaseMap().get(caseId);
                    }else {
                        int idx = 0;
                        JSONObject tempCase = (JSONObject)getCaseMap().get(caseId);
                        if(tempCase != null && tempCase.getBoolean("isRun") != null){
                            idx = 1;
                            for (tempCase = (JSONObject) getCaseMap().get(caseId + "@" +idx); tempCase != null && tempCase.getBoolean("isRun") != null; tempCase = (JSONObject)getCaseMap().get(caseId +"@"+ idx)){
                                ++idx;
                            }
                            caseObject = tempCase;
                        }
                        if (idx != 0){
                            caseId = caseId + "@" + idx;
                            caseName = caseName + "@" + idx;
                        }
                    }

                    if(caseObject == null){
                        caseObject = new JSONObject();
                        caseObject.put("caseType",caseType);
                        caseObject.put("traceId",System.currentTimeMillis()+"");
                        caseObject.put("batchId",getBatchId());
                        getCaseMap().put(caseId, caseObject);
                        if("accurate".equalsIgnoreCase(getExecuteOption().getTestMode())){
                            getCaseIds().add(caseId);
                        }
                        if(caseId.matches("^.*@\\d+$")){
                            getSameIds().add(caseId.substring(0,caseId.lastIndexOf(64)));
                        }else {
                            getSameIds().add(caseId);
                        }

                        if("V2".equalsIgnoreCase(getExecuteOption().getVersion())){
                            caseObject.put("caseId", Md5Util.genMD5(caseId + caseType));
                        }else {
                            caseObject.put("caseId",caseId);
                        }
                    }
                    if(caseObject != null){
                        caseObject.put("caseName",caseName);
                        caseObject.put("testMethod",testMethod);
                        caseObject.put("casePath",casePath);
                        caseObject.put("isRun",true);
                    }
                }
            }catch (Exception e){
                LogUtils.info("parse testcase failed, testcase=[" + testResult.getMethod().getTestClass().getName() + "." + testResult.getMethod().getMethodName()+ "], e=[" + e == null? "null":e.getClass().getName() + "#" +e.getMessage() +"]");
            }
            if(caseObject != null){
                testResult.setAttribute("currentCase",caseObject);
                testResult.setAttribute("caseId", caseObject.getString("caseId"));
            }
            return caseObject;
        }
    }

    private static void initCaseCache(){
        //此方法为启动实验室初始化数据
        String executionId = StringUtil.defaultIfBank(executeOption.getExecutionId(), executeOption.getAqcLabId());
        boolean isAccurate = ("accurate".equals(executeOption.getTestMode()) || "remote".equalsIgnoreCase(executeOption.getTestMode()));
        if(isAccurate){
            ConsoleLogger.success("iTest远程模式开启>>> batchId=" + executeOption.getBatchId() + ", executionId=" + executionId + ", testMethod=" + executeOption.getTestMode(), new Object[0]);
            Map params = assembleParams();
            try {
                String result = ClientUtil.fetchTestCases(params);
                LogUtils.info("service give all case is:" + result);
                JSONObject jsonResult = JSON.parseObject(result);
                if (jsonResult.getBoolean("success")) {
                    JSONArray jsonArray = jsonResult.getJSONArray("data");
                    if (jsonArray != null && jsonArray.size() > 0) {
                        Iterator var6 = jsonArray.iterator();

                        while(var6.hasNext()) {
                            Object object = var6.next();
                            JSONObject jsonObject = (JSONObject)object;
                            jsonObject.put("batchId", params.get("batchId"));
                            if (jsonObject.getString("caseType") != null && jsonObject.getString("caseType").startsWith("ITEST")) {
                                jsonObject.put("caseType", "ITEST");
                            }

                            String caseId = jsonObject.getString("caseId");
                            String testMethod = jsonObject.getString("testMethod");
                            //jsonObject.put("traceId", TraceIdGenerator.generate());
                            caseIds.add(caseId);
                            if (StringUtil.isNotBlank(testMethod)) {
                                testMethods.add(testMethod);
                            }

                            caseMap.put(caseId, jsonObject);
                            if (caseId.matches("^.*@\\d+$")) {
                                sameIds.add(caseId.substring(0, caseId.lastIndexOf(64)));
                            }
                        }
                    }

                    ConsoleLogger.success("iTest远程构建用例数为>>> count=" + caseIds.size() + ", caseIds=" + caseIds, new Object[0]);
                } else {
                    ConsoleLogger.warn("获取用例失败：" + result, new Object[0]);
                }
            } catch (Throwable var11) {
                ConsoleLogger.error("get cases from itestmng exception, batchId=" + (String)params.get("batchId") + ", aqcLab=" + (String)params.get("aqcLabId") + ", portion=" + (String)params.get("portion") + "e=[" + var11 == null ? "null" : var11.getClass().getName() + "]", new Object[0]);
            }
        }
    }

    private static String parseCaseId(Object[] parameters, ITestResult testResult){
        String[] paramNames = parseParamNames(testResult);
        if(paramNames != null && parameters.length == paramNames.length){
            List<String> paramNameList = new ArrayList<>(paramNames.length);
            int idx;
            for(idx = 0; idx < paramNames.length; ++idx){
                if(paramNames[idx] == null){
                    paramNameList.add("arg" + idx);
                }else {
                    paramNameList.add(paramNames[idx].toUpperCase());
                }
            }
            idx = paramNameList.indexOf("CASEID");
            if(idx != -1 && (String.class.isAssignableFrom(parameters[idx].getClass())) || parameters[idx].getClass().isPrimitive() || isWrapClass(parameters[idx].getClass())){
                return String.valueOf(parameters[idx]);
            }
            idx = paramNameList.indexOf("CASEEXPR");
            if(idx != -1){
                String caseExpr = String.valueOf(parameters[idx]);
                if(caseExpr.contains(";")){
                    return caseExpr.split(";")[0];
                }
                return caseExpr;
            }
        }
        return null;
    }

    private static String[] parseParamNames(ITestResult testResult){
        String[] paramNames = null;
        try{
            paramNames = SpringMethodUtil.getParameterNames(testResult.getMethod().getConstructorOrMethod().getMethod());
        }catch (Throwable var5){
            try{
                paramNames = JavaAssistUtil.getMethodParamNames(testResult.getMethod().getRealClass(), testResult.getMethod().getMethodName(),testResult.getMethod().getConstructorOrMethod().getParameterTypes());

            }catch (Throwable var4){
                paramNames = new String[0];
            }
        }
        return paramNames;
    }

    public static boolean isWrapClass(Class clz){
        try{
            return ((Class)clz.getField("TYPE").get((Object) null)).isPrimitive();
        }catch (Exception var2){
            return false;
        }
    }
    private static Map<String, String> assembleParams() {
        Map<String, String> params = new HashMap(16);
        String batchId = executeOption.getBatchId();
        if (batchId != null) {
            params.put("batchId", batchId);
        }

        //String aqcLabId = StringUtil.defaultIfBlank(executeOption.getExecutionId(), executeOption.getAqcLabId());
        String aqcLabId = executeOption.getAqcLabId();
        if (aqcLabId != null) {
            params.put("aqcLabId", aqcLabId);
        }

        String executionId = executeOption.getExecutionId();
        if (executionId != null) {
            params.put("portion", executionId);
        }

        return params;
    }

    public static String printResult(ITestResult testResult) {
        Object[] parameters = testResult.getParameters();
        StringBuffer buffer = new StringBuffer();
        buffer.append("Test Method:\n");
        buffer.append("\t" + testResult.getMethod().getTestClass().getName() + "#" + testResult.getMethod().getMethodName() + "\n");
        String[] paramNames = parseParamNames(testResult);
        buffer.append("Parameters:\n");
        if (paramNames != null && parameters.length == paramNames.length) {
            for(int i = 0; i < paramNames.length; ++i) {
                paramNames[i] = "\t" + paramNames[i] + "=" + parameters[i] + "\n";
                buffer.append(paramNames[i]);
            }
        } else {
            buffer.append("\t" + Arrays.toString(parameters));
        }

        return buffer.toString();
    }

    public static Set<String> getSameIds(){
        return sameIds;
    }

    public static Map<String, JSONObject> getCaseMap(){
        return caseMap;
    }
    public static String getBatchId(){return  executeOption.getBatchId();}
    public static ExecuteOption getExecuteOption(){
        return executeOption;
    }
    public static List<String> getCaseIds() {
        return caseIds;
    }
    public static List<String> getTestMethods() {
        return testMethods;
    }
    public static Set<String> getSkipIds() {
        return skipIds;
    }
    public static long getStart() {
        return start;
    }
}
