package com.liuwei.testng.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileUtil {

    private static String BACKSLASH  = "\\\\";
    private static String SLASH  = "/";
    public static void copyFile(String sourcePath,String targetPath) throws Exception{
        File srcFile = new File(sourcePath);
        if (!srcFile.exists()){
            throw new Exception("file not exist!");
        }
        if (!srcFile.isFile()){
            throw new Exception("it is not a file");
        }
        File targetFile = new File(targetPath);
        FileInputStream in  = null;
        FileOutputStream out = null;
        try{
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(targetFile);
            byte[] buf = new byte[8*1024];
            int len = 0;
            while ((len = in.read(buf)) != -1){
                out.write(buf ,0, len);
                out.flush();
            }
        }catch (Exception e){

        }finally {
            in.close();
            out.close();

        }
    }


    public static String getMainProjectRootPath(String systemRootPath){
        if(System.getProperties().getProperty("os.name").contains("Windows")){
            String[] pathList = systemRootPath.split(BACKSLASH);
            String splitString = BACKSLASH + pathList[pathList.length - 2] +BACKSLASH;
            return systemRootPath.split(splitString)[0] +BACKSLASH;
        }else {
            String[] pathList = systemRootPath.split(SLASH);
            String splitString = SLASH + pathList[pathList.length - 2] +SLASH;
            return systemRootPath.split(splitString)[0] +SLASH;
        }
    }

    public static String getProjectConfigRootPath(String systemRootPath){
        return systemRootPath + "/src/test/resources/config/";
    }
}
