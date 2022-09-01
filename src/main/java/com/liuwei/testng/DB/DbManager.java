package com.liuwei.testng.DB;

public class DbManager {

    public static int executeSql(String dbName, String sql){
        MySqlUtil.initializationMySqlConnection(dbName);
        return MySqlUtil.executeSql(sql);
    }

    public static DataMap executeSqlWithSingleResult(String dbName, String sql){
        MySqlUtil.initializationMySqlConnection(dbName);
        return MySqlUtil.executeSqlWithSingleResult(sql);
    }
}

