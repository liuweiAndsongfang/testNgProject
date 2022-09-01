package com.liuwei.testng.common.testngUtil;

import java.security.MessageDigest;

public class Md5Util {

    public Md5Util(){

    }
    public static String genMD5(String str){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(str.getBytes("utf-8"));
            return toHex(bytes);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes){
        char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
        StringBuilder stringBuilder = new StringBuilder(bytes.length * 2);

        for (int i = 0; i < bytes.length; ++i){
            stringBuilder.append(HEX_DIGITS[bytes[i] >> 4 & 15]);
            stringBuilder.append(HEX_DIGITS[bytes[i] & 15]);
        }
        return stringBuilder.toString();
    }
}
