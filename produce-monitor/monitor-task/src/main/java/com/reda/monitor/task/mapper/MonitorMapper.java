package com.reda.monitor.task.mapper;

import com.reda.monitor.task.entity.MonitInformation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : Zhuang Jialong
 * @description :
 * @date : 2020/9/18 下午 2:46
 * @Copyright: Copyright(c)2019 RedaFlight.com All Rights Reserved
 */
@Mapper
public interface MonitorMapper {
    @Insert("<script>" +
            "insert into REALTIME_PRODUCE_INFORMATION\n" +
            "    (   LOG_TYPE,\n" +
            "        MONITOR_NAME,\n" +
            "        TOTAL_MEMORY,\n" +
            "        IP,\n" +
            "        PROCESS_PORT,\n" +
            "        EXCEPTION_PORT,\n" +
            "        MEMORY_USED,\n" +
            "        MEMORY_FREE,\n" +
            "        BUFF_CACHE,\n" +
            "        DISK,\n" +
            "        DISK_USED,\n" +
            "        TOTAL_OPENFILES_NUM,\n" +
            "        OPENFILES_NUM,\n" +
            "        CPU_USED,\n" +
            "        UPDATE_TIME)\n" +
            "        <foreach collection=\"list\" item=\"item\" separator=\"union all\" >\n" +
            "            select\n" +
            "            #{item.logType,jdbcType=VARCHAR},\n" +
            "            #{item.name,jdbcType=VARCHAR},\n" +
            "            #{item.totalMemory,jdbcType=VARCHAR},\n" +
            "            #{item.ip,jdbcType=VARCHAR},\n" +
            "            #{item.processPort,jdbcType=VARCHAR},\n" +
            "            #{item.exceptionPort,jdbcType=VARCHAR},\n" +
            "            #{item.memoryUsed,jdbcType=VARCHAR},\n" +
            "            #{item.memoryFree,jdbcType=VARCHAR},\n" +
            "            #{item.buffCache,jdbcType=VARCHAR},\n" +
            "            #{item.disk,jdbcType=VARCHAR},\n" +
            "            #{item.diskUsed,jdbcType=VARCHAR},\n" +
            "            #{item.totalOpenfilesNum,jdbcType=VARCHAR},\n" +
            "            #{item.openfilesNum,jdbcType=VARCHAR},\n" +
            "            #{item.cpuUsed,jdbcType=VARCHAR},\n" +
            "            #{item.updateTime,jdbcType=TIMESTAMP}\n" +
            "            from dual\n" +
            "        </foreach>" +
            "</script>")
    int insert(@Param("list") List<MonitInformation> monitInformations);
}
