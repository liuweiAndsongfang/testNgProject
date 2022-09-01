package com.liuwei.testng.common.testngUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostInfo {
    private final String HOST_NAME;
    private final String HOST_ADDRESS;

    public HostInfo() {
        String hostName;
        String hostAddress;
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            hostName = localhost.getHostName();
            hostAddress = localhost.getHostAddress();
        } catch (UnknownHostException var4) {
            hostName = "localhost";
            hostAddress = "127.0.0.1";
        }

        this.HOST_NAME = hostName;
        this.HOST_ADDRESS = hostAddress;
    }

    public final String getName() {
        return this.HOST_NAME;
    }

    public final String getAddress() {
        return this.HOST_ADDRESS;
    }

    public final String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Host Name:    ").append(this.getName());
        buffer.append("Host Address: ").append(this.getAddress());
        return buffer.toString();
    }

    public static void main(String[] args) {
        HostInfo hostInfo = new HostInfo();
        System.out.println(hostInfo.toString());
    }
}
