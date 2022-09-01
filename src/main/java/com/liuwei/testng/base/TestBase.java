package com.liuwei.testng.base;

import com.liuwei.testng.common.TestConfigManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.util.Properties;

@ContextConfiguration(locations = {"classpath*:/spring/*.xml"})
public class TestBase extends AbstractTestNGSpringContextTests {
    public  static Properties testContext = new Properties();
    private static String externalPropertiesPath = null;

    public static void getTestConfig(){
        if (testContext.isEmpty()){
            testContext = TestConfigManager.getTestConfig();
        }
    }

    public static  Properties getTestContext(){
        return testContext;
    }


    public static void sleep(long mills){
        try {
            Thread.sleep(mills);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    @BeforeMethod
    protected void beforeTestCase(){
        try{
            System.out.println("[ " +this.getClass().getSimpleName() +"start to execute  ]");
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    @AfterMethod
    protected void afterTestCase(){
        try{
            System.out.println("[ " +this.getClass().getSimpleName() +" execute end ]");
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
}
