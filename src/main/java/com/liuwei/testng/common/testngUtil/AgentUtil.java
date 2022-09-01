package com.liuwei.testng.common.testngUtil;


import com.liuwei.testng.base.listener.AccurateInvokedMethodListener;
import com.liuwei.testng.base.listener.AccurateTestListener;
import org.testng.TestNG;
import org.testng.log4testng.Logger;
import java.io.PrintWriter;
import java.io.StringWriter;

public class AgentUtil {
    public static final Logger LOGGER = Logger.getLogger(AgentUtil.class);

    public AgentUtil() {
    }

    public static void injectListenrs(TestNG testNG) {
        try {
            if ("itest".equals(System.getProperty("trigger")) && testNG != null) {
                testNG.addListener(new AccurateTestListener());
                testNG.addListener(new AccurateInvokedMethodListener());
                ConsoleLogger.success("inject itest listeners successfully", new Object[0]);
                String projectDir = System.getProperty("user.dir");
                if (projectDir.endsWith("/test") && "V2".equalsIgnoreCase(System.getProperty("itest.agent_version"))) {
                    RtcenterUtil.uploadAgentMonitor("/rtcenter/api/ceStart");
                }
            }
        } catch (Throwable var2) {
            ConsoleLogger.error("inject itest listeners failed, e=[" + getStackTrace(var2) + "]", new Object[0]);
        }

    }

    public static String getStackTrace(Throwable t) {
        if (t == null) {
            return "null";
        } else {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);

            String var3;
            try {
                t.printStackTrace(pw);
                var3 = sw.toString();
            } finally {
                pw.close();
            }

            return var3;
        }
    }
}
