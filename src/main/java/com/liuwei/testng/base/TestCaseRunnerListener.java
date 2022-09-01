package com.liuwei.testng.base;

import com.liuwei.testng.common.StringUtil;
import com.liuwei.testng.common.TestBaseContants;
import com.liuwei.testng.common.TestConfigManager;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.SkipException;

public class TestCaseRunnerListener implements IInvokedMethodListener {

    private static final String DEFAULT_TAG_INDICATOR = "##";
    private static final String TAG_INDICATOR = TestConfigManager.getConfigByKey(TestBaseContants.CASE_EXECUTION_CONTROL_TAG_KEY);

    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        String actualTagIndicator = DEFAULT_TAG_INDICATOR;
        if(TAG_INDICATOR != null && !TAG_INDICATOR.isEmpty()){
            actualTagIndicator = TAG_INDICATOR;
        }

        Object[] params = iTestResult.getParameters();
        String[] groups = iInvokedMethod.getTestMethod().getGroups();
        if(caseIdLevelControlEnable(groups, actualTagIndicator)){
            boolean isSkip = true;
            for(String group : groups){
                if(group.contains(actualTagIndicator)){
                    String testCaseId = StringUtil.subStingAfter(group,actualTagIndicator);
                    if (params != null && testCaseId.equals(params[0].toString())){
                        isSkip = false;
                        break;
                    }
                }
            }
            if(isSkip){
                throw new SkipException("Case is skip due to execution tag control");
            }
        }

    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {

    }

    private boolean caseIdLevelControlEnable(String[] groups, String indicator){
        boolean isEnable = false;
        for(String group:groups){
            if(group.contains(indicator)){
                isEnable = true;
                break;
            }
        }
        return isEnable;
    }
}
