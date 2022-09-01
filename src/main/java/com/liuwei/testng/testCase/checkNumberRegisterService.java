package com.liuwei.testng.testCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liuwei.testng.base.LogUtils;
import com.liuwei.testng.common.PatternHelper;
import com.liuwei.testng.common.RequestService;
import com.liuwei.testng.common.TestConfigManager;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Service;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class checkNumberRegisterService {

    public  Map<String,String> checkNumberRegister(String inputPhoneNumber){
        Map<String,String> paramMap = new HashMap<>();
        String operationType = "alipayplus.mobilewallet.user.userRegistrationCheck";
        String envInfo = TestConfigManager.getTestConfig().getProperty("envInfo_native");
        String contentType = "application/x-www-form-urlencoded; charset-UTF-8";
        String phoneNumber = "";
        try{
            if(inputPhoneNumber.endsWith("")){
                String phoneNumberTail1 = (System.currentTimeMillis()+"").substring(5);
                Random rand1 = new Random();
                int i = rand1.nextInt(9)+1; // 1-9
                String phoneNumberTail = String.valueOf(i) + phoneNumberTail1; //非0开头9位数
                Random rand = new Random();
                int randNumber = rand.nextInt(2);
                if (randNumber == 0){
                    phoneNumber = "27-" + "0" + phoneNumberTail;
                }
                if (randNumber == 1){
                    phoneNumber = "27-" + phoneNumberTail;
                }
            }else {
                phoneNumber = "27-" + inputPhoneNumber;
            }
            paramMap.put("phoneNumber",phoneNumber);
            OkHttpClient client = new OkHttpClient();
//            String requestData = "[{\"envInfo\":"+envInfo+",\"phoneNumber\":\""+phoneNumber+"\"," +
//                    "\"requestData\":\"{\\\"version\\\":\\\"wb\\\",\\\"data\\\":" +
//                    "\\\"X4F\\\\/YV\\\\/CzSbqkDDcjudn4VLeTvpUXh2nibJMkT7Pv\\\\/3A6Mf7Bo9u3FY1PPcik90i0DuZM9ttc2DdEtTKIvCN2IOWy6GK0MCG1gw1XjEYm1oBTOSx59XgZJAdX4kiYfvzF6zWoP60b2lUPqUzXswAJTYHJbyjmG1rNDBUddCxMuedTzQxdR7YZ3KsuXmu3WHYbhmvwEUG5wMCyx88FSqs2PmmqZA7DqIxyWwl4Ae6DW4u5huoM\\\\/cqcsSbI86fah6kEOZbZ1weSTRN5XMisM607syV2LhVIiZzGuSG\\\\/Ilw2OfOHxNh1PHzRQV3ExVXIHaSCaPl6VO4+3JVZ5DsT7G7nE2DUPNCK0FRUCjvvEsuwodOCeXiJrtGzV3znbu0l8aU\\\\/Ks93ka+zUQ3f4gmAShBv4NWm5lLc6AEE1L6DZ9ZRDNBcAmkbKwoSYZxhACNM3BIPgrtbfizcPhZt0mAItmI+cmd\\\\/73cfriXwgdSHDZwGjB\\\\/FzgVDANZIah1Lm2BWq2Kkwhs9ErRsOEJu03IpCYxKhSYgJLbNdSyxt86b6zb+1rjcvCdDkRuuaqKeHS9t2QqaNvOwM6uKpSqOpShb9PtTL18B7XeqEN7shhjXD1\\\\/mR2Ouv73ntcXEWjdypVEuZMQV4tWC8n2hep+vgV+1EDjAMKK0a9TmT1g8sr5zTUKTguow9b1avK+0sWsRhLfyJLkrwxj2wQh2u5JzOe2JKXzvuNmAKue8RdHOXkmFzSBJtLdEuIyw\\\\/ScV+64K9o8qeoToJbTwg\\\\/oZHPkB6w7K6Lz5wJYs3vtztxrq3GqxCC7gTfO5FuS2urWTfq\\\\/6W\\\\/8BM88+3bQhKSavCZB53vV\\\\/6Vlie6eRUJM4LJC0wTqQWpNCAh4Fil9FWtsrS5BVZhuQDoXaj5\\\\/IfP0iRYHsJQO29bzKTmwfSZYc2ZuNyKCz7rbRbk+Ja3iFQ4t4U\\\\/Xdcell7S4tL\\\\/9gSMvhvpuuYAIAQFF+9g3BWM9tGe+mc3rXwIXuCp54vrE4kxUv4o4T3cd5NYFWkNEXrSN2GJDaFb5FP\\\\/vVZLlRf1xlYzBttVAIP7Q92Qv+i2wMkmOyBCnJRCQUEYn28ZiXH6qGQZW6sWIYTULeJifmYegBa06ZhoxfVhuiUZs9hp1dm64hZq6434eMqNoWF2vgaxIdclTYIERJH7iOXm5cnFkOYt+n\\\\/8fXnDmc3oEszOEgUcrbSserPahkE0NAnx6SUC4A0xkLhsnWlD72cqyx6SDOAb5cSyqHLYmRq9Jll3VUl00M\\\\/G19ED2zYxYDS\\\\/akUVZUWZaeOmu+RiwZnJsJDKXMWt7Uqaa9Hk19kvmz7ngSAsT0lwg58xYguLz4qUfztNjyzjdfWa0KfCmd\\\\/LyXaR\\\\/b7VvFwr+jo4Dv4+3qcqW3HwUVuw\\\\/Wkh4qiceB2kLK\\\\/CIyhJc8kssctOzYURGQZpHN7500RmK1G2QsmNir8kOnIMUM9eJ4MJ1KfCnheGNQ61eIDjlZCq8Qspdq0C7HZXteUObg4FSzVamsOyJEbxe2\\\\/Mb3wPORXPnV9zn7bbXuIW0FMTmC4dQcwT\\\\/Sg==\\\"}\"}]";


            String requestData ="[{\"envInfo\":"+envInfo+",\"phoneNumber\":\""+phoneNumber+"\",\"requestData\":\"{\\\"version\\\":\\\"wb\\\",\\\"data\\\":\\\"YqeVFrBxuq0GGO0EaufbBud3\\\\/FRIu\\\\/A944gxoF8bOEJ2byb\\\\/TRXrp6KxK8mr403XGHXJaIah1MHt3XuqPcifkV2IopaJolquOP\\\\/b0OyPdl2sGKlilfxqNkg5jwvmtdaVdrChuvCYUoD4ePKxHWpFbWRFMBYnw70iZxDJKlT1Z9Y1KkzTNou2fpAt94nX25+3A4gsSl8PB9Z66\\\\/untJ5MEDSYLtz+4XtB6ztwsArXr6np\\\\/DtSlJGIH3\\\\/YR\\\\/KeVrtTPr6BKhKEnT202TX2+VYLZwNJJqBVFeQdLhweHgf7e6uGsite8ERdd4Mn3Q55oBesfavNQYzVdyTVBjP\\\\/G6\\\\/EprkqxO1o+SstthCPEnFPCawt+HWbEnM2xnOAC9EANuJXoh8JOnvlx9INewESmpeh0fkXNWEWKg9PoDAW04coPIA1DHWtbgB8EfcH\\\\/QscnK02IkH3qVptP2RkCrWpu1lCo8uf1q4oazdUI\\\\/ILWNC57ELjsjCgVFT3pbs7OQ+6hJZDyL4HVDTYDDrqBcHUHaeiIUEisocLsNvV\\\\/2CLiz4upTQgyzPGe0khAkufmt91xXiO\\\\/K8zepjX2rMuhOLurqIyC6F1HxTaLFsN6oQQyhVQMr44zGSBBa\\\\/UEeR4tmvXeNySXDk4fu3TtA6bZGkesCyzHpwvMmCUq7JZ0b5yBHacqmJcBNmOoAK97Y309+0ZchvsIatlcskOBQ89uTw9Ak0YqEcn1M+38k7BbXz+7C5aJur\\\\/Ff4Tv88YXM6RrHjqtHBqmtI7a6G9Kq2eo\\\\/1RXx7dYmITbnw5FG2nhh0rzYWTJSwIKjkFnTySh4qhNh8ajvEQUe0tytzxLE89xJ9ukkvmay+zRu1FxSp7HQimNFxLnQYjZGx\\\\/hxxehrF97Nei3QCgbOPvD6hp\\\\/T16eZtfAd6e35pWN7cUBj6iS3vKW\\\\/7v0BcycHv4gRO36LICAXx4syLl2l40eggSe9rlqiZOyyTwm9G\\\\/39xkFVZd0pFLs006vIQnsE0viuRJUxP+8iWRdEYnVcUqIA6bM4uUzZe+YPO816EBFib8I39bk7KlEwHDD3e2ebk+uZW\\\\/wqAbtCic1El7RM7NoSdwL7xmVlthvx8qHUYhS7pAz80dNRNXTdWqatRE\\\\/bHrX9QANmAgmR3EnLcYUr9jscybcsGlkG5M+LaD7blumV3L+0Rt6MiXrvaRg\\\\/uTd5XrKEWZS3B1vDkgVF+Kkr8Xq87RtpvujqCu3KIYBWNvXJVTTNHvPq+hzbxu7RXJTStBPRyQWQQlBooPLTWi4cRBV3wjUVZB5EAdNSVO2XBV7mv3IkFQxUEP44tRDWLGXgD4hyu6PLphqXboQmxB4h3TuwDXmlsWItw\\\\/fUSsRFli+8iVNof3Q2qKjqTNvZm4IBbamrEIeGgb5qRBxHqRAYXTMbDzOiLKj2kjZebsnzvklR1JwwUS8nZChUJHgTxCpaa1dVODQY71rMEtPN0GzWoviiu1XHEyZ5A5PrEJbT007omkPJczopDSe5xfZWTVnOkdy\\\\/oLPWmcpfOXCyoQR2270n2bAeTDUHEizUVla\\\\/HWb34WtmgoXt3jqh0+ei6ahVJeJiV\\\\/irFOiDSUCadA\\\"}\"}]";
            System.out.print("requestData is:"+requestData +"\n");

            Request request = RequestService.getRequest(paramMap,requestData,operationType,contentType);
            Response response = client.newCall(request).execute();

            String responseHeader = response.headers().toString();
            String responseBody = response.body().string();
            System.out.print("responseHeader is:"+responseHeader+"\n");
            //System.out.print("responseBody is:"+responseBody +"\n");
            LogUtils.info("responseBody is:"+responseBody);

            String Mgw_TraceId = PatternHelper.matchValue("Mgw-TraceId\\: ([-A-Za-z0-9]+)",responseHeader);
            //System.out.print("TraceId is:"+Mgw_TraceId);
            LogUtils.info("TraceId is:"+Mgw_TraceId);

            String string_CToken = PatternHelper.matchValue("ctoken=([-A-Za-z0-9]+)",responseHeader);
            Assert.assertTrue(StringUtils.isNotEmpty(string_CToken),"string_CToken is empty, please check sofaId: "+Mgw_TraceId);
            paramMap.put("ctoken", string_CToken);

            String String_alipayjsessionid = PatternHelper.matchValue("ALIPAYJSESSIONID=([A-Za-z0-9]+);", responseHeader);
            org.testng.Assert.assertTrue(StringUtils.isNotEmpty(String_alipayjsessionid), "alipayjsessionid is empty, please check"+  "  sofaId:" + Mgw_TraceId);
            paramMap.put("alipayjsessionid", String_alipayjsessionid);

            Assert.assertTrue(StringUtils.isNotEmpty(responseBody), String.format("responseBody is empty ,please check traceId:%s", Mgw_TraceId));

            JSONObject jsonresult = JSON.parseObject(responseBody);
            JSONObject result = jsonresult.getJSONObject("result");
            boolean status = result.getBoolean("success");
            org.testng.Assert.assertTrue(status, "The API response failed, please check" + "  sofaId:" + Mgw_TraceId + "  jsonresult" + jsonresult + "    paraMap:" + paramMap);
            paramMap.put("phoneNumber", phoneNumber);
            String action = result.getString("action");
            String hasRegister = "false";
            if (action.equals("PAGE_NORMAL_LOGIN")||action.equals("PAGE_BIOMETRIC_LOGIN")){
                hasRegister = "true";
            }
            paramMap.put("hasRegister", hasRegister);
            
        }catch (Exception e){
            org.testng.Assert.assertTrue(false, String.format("The API happen exception errorMessage is %s",e.getMessage()));
        }
        return paramMap;
    }
}
