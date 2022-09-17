package com.liuwei.testng.mobile;

import com.liuwei.testng.base.TestBase;
import com.liuwei.testng.testCase.checkNumberRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class testLoadBankInfo extends TestBase {
    @Autowired
    checkNumberRegisterService  checkRegister;

    @Test(description = "loadBankInfo")
    public  void testLoadBank (){

        System.out.print("12345222"+"\n");
        Map<String,String> paramMap = new HashMap<>();
        checkRegister.checkNumberRegister("321666031");
        //return paramMap;
    }

    @Test(description = "loadBankInfo2")
    public  void testLoadBank2 (){
        System.out.print("12345222");
    }
}
