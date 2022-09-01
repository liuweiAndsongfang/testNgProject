package com.liuwei.testng.common.testngUtil;

import java.text.MessageFormat;

public class MessageHelper {
    public MessageHelper() {
    }

    public static String formatMsg(String level, String msgTemplate, Object... positionValues) {
        try {
            return positionValues != null && positionValues.length != 0 ? MessageFormat.format(msgTemplate, positionValues) : "[" + level + "][TestNG-Agent][" + DateUtil.currentDate() + "]" + msgTemplate;
        } catch (Exception var6) {
            StringBuilder buf = new StringBuilder("Resource information placeholder replacement exception, placeholder parameter information:");

            for(int i = 0; i < positionValues.length; ++i) {
                buf.append(" arg[" + i + "]=" + positionValues[i]);
            }

            ConsoleLogger.warn(buf.toString(), new Object[]{var6});
            return "[" + level + "][TestNG-Agent][" + DateUtil.currentDate() + "]" + msgTemplate;
        }
    }

    public static String escape(String value) {
        if (value == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder(value.length());
            int i = 0;

            for(int n = value.length(); i < n; ++i) {
                char source = value.charAt(i);
                switch(source) {
                    case '\n':
                        sb.append("%0A");
                        break;
                    case '\r':
                        sb.append("%0D");
                        break;
                    case '(':
                        sb.append("%28");
                        break;
                    case ')':
                        sb.append("%29");
                        break;
                    case ',':
                        sb.append("%2C");
                        break;
                    case '[':
                        sb.append("%5B");
                        break;
                    case ']':
                        sb.append("%5D");
                        break;
                    default:
                        sb.append(source);
                }
            }

            return sb.toString();
        }
    }
}
