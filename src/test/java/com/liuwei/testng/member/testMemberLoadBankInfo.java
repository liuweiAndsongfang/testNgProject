package com.liuwei.testng.member;

import com.liuwei.testng.base.TestBase;
import com.liuwei.testng.testCase.checkNumberRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class testMemberLoadBankInfo extends TestBase {
    @Autowired
    checkNumberRegisterService  checkRegister;

    @Test(description = "loadBankInfo")
    public  void testMemberLoadBank (){

        System.out.print("12345222"+"\n");
        Map<String,String> paramMap = new HashMap<>();
        checkRegister.checkNumberRegister("321333018");
        //return paramMap;
    }

    @Test(description = "loadBankInfo")
    public  void testMemberLoadBank2 (){
        System.out.print("testMemberLoadBank2");
    }


    @DataProvider(name="memberUser")
    public Object[][] userMember(){

        return new Object[][]{
                {"root","321333001"},
                {"myUser","321333101"},
                {"tank","321333088"}
        };
    }
    @Test(description = "testMemberUserTest",dataProvider = "memberUser")
    public  void testMemberUserTest (String userName, String userNumber){
        System.out.print("userName is:"+ userName);
        Map<String,String> paramMap = new HashMap<>();
        checkRegister.checkNumberRegister(userNumber);
    }

}
