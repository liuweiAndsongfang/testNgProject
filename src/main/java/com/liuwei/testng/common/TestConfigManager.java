package com.liuwei.testng.common;

import java.io.FileReader;
import java.util.Properties;

public class TestConfigManager {

    private static final String EXTERNAL_CONFIG_CACHE = System.getProperty("user.dir") + "/imported.properties";
    private static Properties globalProperties;
    private static String externalPropertiesPath = null;

    public static Properties getGlobalProperties(){
        return globalProperties;
    }

    public static void setGlobalProperties(Properties globalProperties){
        TestConfigManager.globalProperties = globalProperties;
    }

    public static Properties getTestConfig(){
//        Properties externalConfig =loadExternalConfig();
//        if(!externalConfig.isEmpty()){
//            return externalConfig;
//        }
        Properties coreConfigProperties = loadCoreConfig();
        String ifMainConfigOnly = coreConfigProperties.getProperty(TestBaseContants.MAIN_CONFIG_ONLY);
        Properties returnProperties = null;
        if (Boolean.parseBoolean(ifMainConfigOnly)){
            returnProperties = mergeProperties(coreConfigProperties,loadMainConfig());
            setGlobalProperties(coreConfigProperties);
        }else{
            returnProperties = mergeProperties(coreConfigProperties,mergeProperties(loadMainConfig(),loadBundleConfig()));
            setGlobalProperties(coreConfigProperties);
        }
        return returnProperties;
    }

    private static Properties mergeProperties(Properties pro1, Properties pro2){
        Properties properties = new Properties();
        if (!pro1.isEmpty()){
            properties.putAll(pro1);
        }
        if (!pro2.isEmpty()){
            properties.putAll(pro2);
        }
        return  properties;
    }

//    private static Properties loadExternalConfig(){
//
//        Properties externalPropertiesCache = loadResource(EXTERNAL_CONFIG_CACHE);
//        Properties userSetProperties = loadResource(externalPropertiesPath);
//        if(externalPropertiesCache.isEmpty()){
//
//        }
//    }

    //load core config Properties
    private static  Properties loadCoreConfig(){
        String systemRootPath = System.getProperty("user.dir");
        String commonConfigPath = FileUtil.getMainProjectRootPath(systemRootPath) + TestBaseContants.COMMON_CONFIG_FILE_PATH;
        return loadResource(commonConfigPath);

    }

    //load Main config Properties
    private static  Properties loadMainConfig(){
        String systemRootPath = System.getProperty("user.dir");
        String commonConfigPath = FileUtil.getMainProjectRootPath(systemRootPath) + TestBaseContants.MAIN_CONFIG_FILE_PATH;
        return loadResource(commonConfigPath);

    }

    //copy file
    private static void copyfile(String sourcePath, String destinationPath){
        try{
            FileUtil.copyFile(sourcePath,destinationPath);
        }catch (Exception e){

        }
    }

    private static  Properties loadResource(String filePath){
        Properties prop = new Properties();
        try{
            prop.load(new FileReader(filePath));
        }catch (Throwable e){
            //
        }
        return prop;
    }
    private static Properties loadBundleConfig(){
        String systemRootPath = System.getProperty("user.dir");
        String testConfigFilePath = FileUtil.getProjectConfigRootPath(systemRootPath) + TestBaseContants.TEST_CONFIG_FILE_PATH;

        Properties testConfig  = loadResource(testConfigFilePath);
        String env = testConfig.getProperty("ENV");

        String envConfigFilePath = FileUtil.getProjectConfigRootPath(systemRootPath) + TestBaseContants.ENV_CONFIG_FILE_PATH +env + TestBaseContants.ENV_CONFIG_POSTFIX;
        String dbConfigFilePath = FileUtil.getProjectConfigRootPath(systemRootPath) + TestBaseContants.DB_CONFIG_FILE_PATH +env + TestBaseContants.DB_CONFIG_POSTFIX;

        Properties envConfig = loadResource(envConfigFilePath);
        Properties dbConfig = loadResource(dbConfigFilePath);

        return mergeProperties(testConfig,mergeProperties(envConfig,dbConfig));

    }

    public static String getConfigByKey(String configKey){
        return getTestConfig().getProperty(configKey);
    }
}
