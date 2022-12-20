package com.liuwei.testng.common.testngUtil;

import com.liuwei.testng.common.StringUtil;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class TracerIdUtil {
    private static String IP_16 = "ffffffff";
    private static String IP_int = "255255255255";
    private static final String regex = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
    private static final Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
    private static AtomicInteger count = new AtomicInteger(1000);
    public static final String PID = getPID();

    public TracerIdUtil() {
    }

    public static String getInetAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress address = null;

            while(interfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface)interfaces.nextElement();
                Enumeration addresses = ni.getInetAddresses();

                while(addresses.hasMoreElements()) {
                    address = (InetAddress)addresses.nextElement();
                    if (!address.isLoopbackAddress() && address.getHostAddress().indexOf(":") == -1) {
                        return address.getHostAddress();
                    }
                }
            }

            return null;
        } catch (Throwable var4) {
            return null;
        }
    }

    public static String getPID() {
        String processName = ManagementFactory.getRuntimeMXBean().getName();
        if (StringUtil.isBlank(processName)) {
            return "";
        } else {
            String[] processSplitName = processName.split("@");
            if (processSplitName.length == 0) {
                return "";
            } else {
                String pid = processSplitName[0];
                return StringUtil.isBlank(pid) ? "" : pid;
            }
        }
    }

    private static String getTraceId(String ip, long timestamp, int nextId) {
        StringBuilder appender = new StringBuilder(30);
        appender.append(ip).append(timestamp).append(nextId).append(PID);
        return appender.toString();
    }

    public static String generate() {
        return getTraceId(IP_16, System.currentTimeMillis(), getNextId());
    }

    static String generate(String ip) {
        return ip != null && !ip.isEmpty() && validate(ip) ? getTraceId(getIP_16(ip), System.currentTimeMillis(), getNextId()) : generate();
    }

    static String generateIpv4Id() {
        return IP_int;
    }

    static String generateIpv4Id(String ip) {
        return ip != null && !ip.isEmpty() && validate(ip) ? getIP_int(ip) : IP_int;
    }

    private static boolean validate(String ip) {
        try {
            return pattern.matcher(ip).matches();
        } catch (Throwable var2) {
            return false;
        }
    }

    private static String getIP_16(String ip) {
        String[] ips = ip.split("\\.");
        StringBuilder sb = new StringBuilder();
        String[] var3 = ips;
        int var4 = ips.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String column = var3[var5];
            String hex = Integer.toHexString(Integer.parseInt(column));
            if (hex.length() == 1) {
                sb.append('0').append(hex);
            } else {
                sb.append(hex);
            }
        }

        return sb.toString();
    }

    private static String getIP_int(String ip) {
        return ip.replace(".", "");
    }

    private static int getNextId() {
        int current;
        int next;
        do {
            current = count.get();
            next = current > 9000 ? 1000 : current + 1;
        } while(!count.compareAndSet(current, next));

        return next;
    }

    static {
        try {
            String ipAddress = getInetAddress();
            if (ipAddress != null) {
                IP_16 = getIP_16(ipAddress);
                IP_int = getIP_int(ipAddress);
            }
        } catch (Throwable var1) {
        }

    }
}
