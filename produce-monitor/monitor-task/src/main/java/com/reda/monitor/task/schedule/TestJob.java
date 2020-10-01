package com.reda.monitor.task.schedule;

import com.reda.monitor.task.service.HandleLogFileService;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author : Zhuang Jialong
 * @description : 处理日志文件定时任务（读取-解析-存储）
 * @date : 2020/9/11 11:30
 * @Copyright: Copyright(c)2020 RedaFlight.com All Rights Reserved
 */
@Configuration
@Component
@EnableScheduling
public class TestJob  {

    private static Logger logger = LoggerFactory.getLogger("translate_log_file");

    /**
     *
     * @param context
     * @throws JobExecutionException
     */
    @Autowired
    private HandleLogFileService handleLogFileService;
//
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void execute() throws JobExecutionException {
//        logger.info("===================开始执行日志解析处理任务==================={}",new Date());
//        handleLogFileService.parseLogFiles();
//        logger.info("===================结束执行日志解析处理任务==================={}",new Date());
//    }
}
