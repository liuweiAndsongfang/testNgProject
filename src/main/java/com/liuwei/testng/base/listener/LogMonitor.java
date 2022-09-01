package com.liuwei.testng.base.listener;

import com.liuwei.testng.common.StringUtil;
import com.liuwei.testng.common.testngUtil.ConsoleLogger;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LogMonitor {
    private static long lastTimeFileSize = 0L;
    private static ThreadLocal<StringBuffer> logBuffer = new ThreadLocal();
    private static ScheduledExecutorService executorService;
    private static File logFile;
    private static String LOG_KEY = "log";
    private static final String LOCAL_LOG = "local";
    private static Map<String, StringBuffer> logMap = new ConcurrentHashMap();
    public static String STARD_RECORD_LOG = "++ [ Start Running case ]";
    public static String STARD_RECORD_FORMAT = "++ [ Start Running %s ]";
    public static String END_RECORD_LOG = "-- [ End of case ]";
    public static String END_RECORD_FORMAT = "-- [ End of %s ]";

    public LogMonitor() {
    }

    public static void init(String filePath) {
        try {
            Class var1 = LogMonitor.class;
            synchronized(LogMonitor.class) {
                if (logFile == null || !logFile.exists()) {
                    logFile = new File(filePath);
                }
            }

            logBuffer.set(new StringBuffer());
        } catch (Exception var4) {
        }

    }

    public static void initRecordFlag(String caseId) {
        if (StringUtil.isBlank(caseId)) {
            caseId = UUID.randomUUID().toString();
        }

        STARD_RECORD_LOG = String.format(STARD_RECORD_FORMAT, caseId);
        END_RECORD_LOG = String.format(END_RECORD_FORMAT, caseId);
    }

    public static void startRecord(Object caseResult) {
        if (logFile != null && logFile.exists()) {
            try {
                executorService = Executors.newScheduledThreadPool(1);
                final RandomAccessFile randomFile = new RandomAccessFile(logFile, "rw");
                lastTimeFileSize = randomFile.length();
                if (logMap.get(LOG_KEY) == null) {
                    logMap.put(LOG_KEY, new StringBuffer());
                }

                executorService.scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        try {
                            randomFile.seek(LogMonitor.lastTimeFileSize);

                            String tmp;
                            while((tmp = randomFile.readLine()) != null) {
                                ((StringBuffer) LogMonitor.logMap.get(LogMonitor.LOG_KEY)).append(new String(tmp.getBytes("ISO8859-1"))).append("\n");
                            }

                            LogMonitor.lastTimeFileSize = randomFile.length();
                        } catch (IOException var2) {
                            throw new RuntimeException(var2);
                        }
                    }
                }, 0L, 1L, TimeUnit.MILLISECONDS);
            } catch (Exception var2) {
                ConsoleLogger.error("record case log exception" + var2 == null ? "null" : var2.getClass().getName() + "#" + var2.getMessage() + "]", new Object[0]);
            }
        }

    }

    public static synchronized String dump() {
        try {
            if (executorService != null && !executorService.isShutdown()) {
                executorService.shutdown();
            }

            String temp = String.valueOf(logMap.get(LOG_KEY));
            logMap.put(LOG_KEY, new StringBuffer());
            if (logMap.containsKey("local")) {
                temp = String.valueOf(logMap.get("local"));
            }

            return temp;
        } catch (Exception var1) {
            ConsoleLogger.error("dump case log exception" + var1 == null ? "null" : var1.getClass().getName() + "#" + var1.getMessage() + "]", new Object[0]);
            return "";
        }
    }

    public static void setLocalLog(String logText) {
        logMap.put("local", new StringBuffer(logText));
    }

    public static void clearLocalLog() {
        logMap.remove("local");
    }

    public ThreadLocal<StringBuffer> getLogBuffer() {
        return logBuffer;
    }

    public void setLogBuffer(ThreadLocal<StringBuffer> logBuffer) {
        LogMonitor.logBuffer = logBuffer;
    }
}
