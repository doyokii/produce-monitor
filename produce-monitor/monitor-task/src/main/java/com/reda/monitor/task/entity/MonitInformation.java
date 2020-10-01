package com.reda.monitor.task.entity;

import java.util.Date;

/**
 * @author : Zhuang Jialong
 * @description : 监控信息实体
 * @date : 2020/9/15 上午 10:23
 * @Copyright: Copyright(c)2019 RedaFlight.com All Rights Reserved
 */
public class MonitInformation {
    private String logType;
    private String name;
    private String totalMemory;
    private String ip;
    private String processPort;
    private String exceptionPort;
    private String memoryUsed;
    private String memoryFree;
    private String buffCache;
    private String disk;
    private String diskUsed;
    private String totalOpenfilesNum;
    private String openfilesNum;
    private String cpuUsed;
    private Date   updateTime;

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(String totalMemory) {
        this.totalMemory = totalMemory;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getProcessPort() {
        return processPort;
    }

    public void setProcessPort(String processPort) {
        this.processPort = processPort;
    }

    public String getExceptionPort() {
        return exceptionPort;
    }

    public void setExceptionPort(String exceptionPort) {
        this.exceptionPort = exceptionPort;
    }

    public String getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(String memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public String getMemoryFree() {
        return memoryFree;
    }

    public void setMemoryFree(String memoryFree) {
        this.memoryFree = memoryFree;
    }

    public String getBuffCache() {
        return buffCache;
    }

    public void setBuffCache(String buffCache) {
        this.buffCache = buffCache;
    }

    public String getDisk() {
        return disk;
    }

    public void setDisk(String disk) {
        this.disk = disk;
    }

    public String getDiskUsed() {
        return diskUsed;
    }

    public void setDiskUsed(String diskUsed) {
        this.diskUsed = diskUsed;
    }

    public String getTotalOpenfilesNum() {
        return totalOpenfilesNum;
    }

    public void setTotalOpenfilesNum(String totalOpenfilesNum) {
        this.totalOpenfilesNum = totalOpenfilesNum;
    }

    public String getOpenfilesNum() {
        return openfilesNum;
    }

    public void setOpenfilesNum(String openfilesNum) {
        this.openfilesNum = openfilesNum;
    }

    public String getCpuUsed() {
        return cpuUsed;
    }

    public void setCpuUsed(String cpuUsed) {
        this.cpuUsed = cpuUsed;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
