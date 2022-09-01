package com.liuwei.testng.common.testngUtil;

public class ConsoleLogger {
    public ConsoleLogger() {
    }

    public static void error(String message, Object... params) {
        message = MessageHelper.formatMsg("ERROR", message, params);
        System.out.println(message);
    }

    public static void success(String message, Object... params) {
        message = MessageHelper.formatMsg("INFO", message, params);
        System.out.println(message);
    }

    public static void warn(String message, Object... params) {
        message = MessageHelper.formatMsg("WARNING", message, params);
        System.out.println(message);
    }

    public static void info(String message, Object... params) {
        message = MessageHelper.formatMsg("INFO", message, params);
        System.out.println(message);
    }
}
