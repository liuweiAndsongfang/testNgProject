package com.liuwei.testng.mobile.Login;

import com.liuwei.testng.base.TestBase;
import com.liuwei.testng.testCase.checkNumberRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class testMobileLoadBankInfo extends TestBase {
    @Autowired
    checkNumberRegisterService  checkRegister;

    @Test(description = "loadBankInfo")
    public  void testMobileLoginLoadBank (){

        System.out.print("12345222"+"\n");
        Map<String,String> paramMap = new HashMap<>();
        checkRegister.checkNumberRegister("123123");
        //return paramMap;
    }

    @Test(description = "loadBankInfo")
    public  void testLoginLoadBank2 (){
        System.out.print("testLoginLoadBank2");
    }
}
