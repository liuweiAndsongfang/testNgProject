package com.liuwei.testng;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class TestTest {

    @Test
    public void TestNgLearn() {
        System.out.println("this is TestNG test case");
    }

    @Test
    public void TestNgLearn2() {
        System.out.println("6666666666999999999");
    }

    @DataProvider(name="user")
    public Object[][] Users(){
        return new Object[][]{
                {"root","passowrd"},
                {"cnblogs.com", "tankxiao"},
                {"tank","xiao"}
        };
    }

    @Test(dataProvider="user")
    public void verifyUser(String userName, String password){
        System.out.println("Username: "+ userName + " Password: "+ password);
    }

    @Test
    @Parameters("test1")
    public void ParaTest(String test1){
        System.out.println("This is " + test1);
    }
}