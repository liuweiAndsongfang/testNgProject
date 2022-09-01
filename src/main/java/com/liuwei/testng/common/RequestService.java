package com.liuwei.testng.common;


import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import java.util.Map;
import java.util.UUID;


public class RequestService  {
    public static Request getRequest(Map<String,String> map, String requestString, String operationType){

        String rpcHostName = TestConfigManager.getTestConfig().getProperty("rpchostname");
        String httpprotocol = TestConfigManager.getTestConfig().getProperty("httpprotocol");
        String env = TestConfigManager.getTestConfig().getProperty("ENV");
        String appId = TestConfigManager.getTestConfig().getProperty("APPID");
        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType,requestString);

        String cookie = "ctoken=" + map.get("ctoken")+";"
                +"ALIPAYJSESSIONID=" + map.get("alipayjessionid")+";" +"session.cookieNameId=ALIPAYJSESSIONID;"
                +"userId="+map.get("userId");
        Request request = new Request.Builder()
                .url(httpprotocol + rpcHostName + "/mgw.htm")
                .method("POST",body)
                .addHeader("Host",rpcHostName)
                .addHeader("Accept", "*/*")
                .addHeader("appId", appId)
                .addHeader("workspaceId", env)
                .addHeader("version", "2.0")
                .addHeader("x-cors-" + appId.toLowerCase() + "-" +env, "1")
                .addHeader("Accept-Language", "en-CN")
                .addHeader("Operation-Type", operationType)
                .addHeader("Content-Length", "155")
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .addHeader("Cookie", cookie)
                .addHeader("Connection", "keep-alive")
                .build();
        System.out.print("Request==============" + request);
        return request;
    }

    public static Request getRequest(Map<String, String> paramMap, String requestData, String operationType, String contentType) {
        String uuid = UUID.randomUUID().toString();
        // universal logic
        int id=(int)(Math.random() * (1000));
        String requestBody="id="+id+"&operationType="+operationType+"&requestData="+java.net.URLEncoder.encode(requestData)+"&ts="+System.currentTimeMillis();;

        String rpchostname = TestConfigManager.getTestConfig().getProperty("rpchostname");
        String httpprotocol = TestConfigManager.getTestConfig().getProperty("httpprotocol");
        String env = TestConfigManager.getTestConfig().getProperty("ENV");
        String appId = TestConfigManager.getTestConfig().getProperty("APPID");
        String tenantId = TestConfigManager.getTestConfig().getProperty("TENANTID");
        System.out.print("RequestBody:" + requestBody +"\n");
        //LogUtil.info("RequestBody:" + requestBody);
        MediaType mediaType = MediaType.parse(contentType);
        RequestBody body = RequestBody.create(mediaType, requestBody);
        System.out.print("mediaRequestBody:" + body +"\n");
        //LogUtil.info("RequestBody:" + body);
        // set API request cookie
        String cookie="";
        if (paramMap.get("ctoken")!=null &&  paramMap.get("alipayjsessionid")!=null) {
            cookie = "ctoken=" + paramMap.get("ctoken") + "; "
                    + "ALIPAYJSESSIONID=" + paramMap.get("alipayjsessionid");
        }
        if (paramMap.get("userId")!=null && paramMap.get("userId")!="")
        {
            cookie+= ";userId=" + paramMap.get("userId")+";session.cookieNameId=ALIPAYJSESSIONID";
        }
        // String cookie ="ALIPAYJSESSIONID=GZ00F0F9C808EA62498097BD26F8ACB54CDDapmobilewalletGZ00; session.cookieNameId=ALIPAYJSESSIONID; userId=216610000000048290702; ctoken=YjaiU9Hogg5y1Eh3"
        // build API request info = Header + body
        Request request = new Request.Builder()
                .url(httpprotocol + rpchostname + "/mgw.htm")
                .method("POST", body)
                .addHeader("Host", rpchostname)
                .addHeader("tenantId", tenantId)
                .addHeader("Accept", "*/*")
                .addHeader("uuid", uuid)
                .addHeader("AppId", appId)
                .addHeader("appId", appId)
                .addHeader("workspaceId", env)
                .addHeader("version", "2.0")
                .addHeader("did", "Xz4R5GmOYBUDAFi2HfmDba2w")
                .addHeader("x-cors-" + appId.toLowerCase() + "-" + env, "1")//是否支持跨域，1就是客户端的true，0是客户端的false
                .addHeader("Accept-Language", "en-US")
                .addHeader("Operation-Type", operationType)
                .addHeader("Accept-Encoding", "gzip, deflate")
                //.addHeader("Origin","http://vodapay-m.sit.vfs.africa")
                .addHeader("Content-Length", "2608")
                .addHeader("User-Agent", "Vodapay-TEST_AWS_1/6 CFNetwork/1240.0.4 Darwin/20.6.0")
                //.addHeader("Referer",refer)
                .addHeader("X-Apdid-Token", "AuwvPCLWOQsdOAzr8QQ/xJqf2y/sgC5/GKa0PUNsJbvaVHC/dQEAAA==")
                .addHeader("Content-Type", contentType)
                .addHeader("Cookie", cookie)
                .addHeader("Connection", "keep-alive")
                .build();

        System.out.print("Request==============" + request);
        //LogUtil.info("Request==============" + request);
        return request;
    }
}
