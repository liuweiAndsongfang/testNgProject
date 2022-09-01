package com.liuwei.testng.common.testngUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public DateUtil() {
    }

    public static String getCurrentDateStr() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss,SSS");
        return simpleDateFormat.format(new Date());
    }

    public static String currentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        return simpleDateFormat.format(new Date());
    }

    public static void main(String[] args) {
        ConsoleLogger.success("nihao{0}", new Object[]{"qianqiu"});
    }
}