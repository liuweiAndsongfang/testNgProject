package com.liuwei.testng;

import org.testng.TestNG;

import java.util.ArrayList;
import java.util.List;

public class TestRunnerMain {

    public static void main(String[] args) {
        List<String> suites = new ArrayList<>();
        String xmlPath = "myTest/src/test/resources/testsuite/mytest-intergartion.xml";
        suites.add(xmlPath);
        TestNG tng = new TestNG();
        tng.setTestSuites(suites);
        tng.run();
    }
}
