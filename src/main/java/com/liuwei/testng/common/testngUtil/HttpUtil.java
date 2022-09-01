package com.liuwei.testng.common.testngUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public class HttpUtil {
    public HttpUtil() {
    }

    public static String post(String url, Map<String, String> params) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();

        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(20000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            out = new PrintWriter(conn.getOutputStream());
            if (params != null) {
                Iterator paramIt = params.entrySet().iterator();

                while(paramIt.hasNext()) {
                    Map.Entry<String, String> paramEntry = (Map.Entry)paramIt.next();
                    if (paramEntry.getKey() != null && paramEntry.getValue() != null) {
                        out.print("&" + (String)paramEntry.getKey() + "=" + URLEncoder.encode((String)paramEntry.getValue(), "utf-8"));
                    }
                }
            }

            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception var17) {
            var17.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (in != null) {
                    in.close();
                }
            } catch (IOException var16) {
                var16.printStackTrace();
            }

        }

        return result.toString();
    }
}
