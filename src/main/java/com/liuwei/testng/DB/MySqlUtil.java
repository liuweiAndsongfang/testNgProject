package com.liuwei.testng.DB;

import com.liuwei.testng.base.LogUtils;
import com.liuwei.testng.common.StringUtil;
import com.liuwei.testng.common.TestConfigManager;
import org.testng.Assert;

import java.sql.*;

public class MySqlUtil {

    static Connection conn = null;

    public static void initializationMySqlConnection(String dbname){
        String dbUrl = TestConfigManager.getTestConfig().getProperty(dbname + "_db_url");
        String dbUserName = TestConfigManager.getTestConfig().getProperty(dbname + "_db_url_username");
        String dbPassword = TestConfigManager.getTestConfig().getProperty(dbname + "_db_url_password");

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbUrl,dbUserName,dbPassword);
            if(conn != null){
                LogUtils.info("Success to initialization mysql connection");
            }
        }catch (Exception e){
            LogUtils.info("Failed to initialization mysql connection");
        }
    }

    public static int executeSql(String sql){
        Statement stat = null;
        try{
            if(conn != null){
                stat = conn.createStatement();
                LogUtils.info("SQL to be execute:" + sql);
                int lines = stat.executeUpdate(sql);
                LogUtils.info("Affected lines" + lines);
                conn.close();
                stat.close();
                return lines;
            }
        }catch (SQLException e){
            LogUtils.info("something wrong happening:" + e.getMessage());
            Assert.fail(e.getStackTrace().toString());
        }finally {
            finalClose(null,stat);
        }
        return 0;
    }

    public static DataMap executeSqlWithSingleResult(String sql){
        DataMap map = null;
        ResultSet resultSet = null;
        Statement statement = null;
        try{
            if(conn != null){
                statement = conn.createStatement();
                LogUtils.info("SQL to be execute:"+ sql);
                resultSet = statement.executeQuery(sql);
                if(resultSet != null){
                    if(resultSet.next()){
                        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                        map = new DataMap();
                        for(int i = 1; i <= resultSetMetaData.getColumnCount(); i++){
                            map.put(StringUtil.toUpperCase(resultSetMetaData.getColumnName(i)), resultSet.getObject(i));
                        }
                    }
                }else {
                    LogUtils.info("No record get out!!");
                }
            }else {
                Assert.fail("mysql connection object is null, please initialize first!!!");
            }
            conn.close();
            statement.close();
            resultSet.close();
        }catch (SQLException e){
            LogUtils.info("something wrong happening:" + e.getMessage());
        }finally {
            finalClose(resultSet,statement);
        }
        return map;
    }

    private static void finalClose(ResultSet resultSet, Statement statement){
        try {
            if(resultSet != null){
                resultSet.close();
            }
            if(statement != null){
                statement.close();
            }
        }catch (SQLException e){
            LogUtils.info( e.getMessage());
        }
    }
}
