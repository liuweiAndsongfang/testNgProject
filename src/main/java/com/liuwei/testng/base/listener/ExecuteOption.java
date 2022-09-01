package com.liuwei.testng.base.listener;

import java.io.Serializable;

public class ExecuteOption implements Serializable {

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getAqcLabId() {
        return aqcLabId;
    }

    public void setAqcLabId(String aqcLabId) {
        this.aqcLabId = aqcLabId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getPortion() {
        return portion;
    }

    public void setPortion(String portion) {
        this.portion = portion;
    }

    public String getTestMode() {
        return testMode;
    }

    public void setTestMode(String testMode) {
        this.testMode = testMode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    private String batchId;
    @Deprecated
    private String aqcLabId;
    private String executionId;
    private String portion;
    private String testMode;
    private String version;

    public ExecuteOption(){

    }
    public String toString(){
        return "ExecuteOption{batchId='"+this.batchId + '\'' + ", aqcLabId='" + this.aqcLabId + '\''
                + ", testMode='" + this.testMode + '\'' + ", version='" + this.version + '\'' +'}';
    }
}
