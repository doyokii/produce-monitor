package com.reda.monitor.task.centre;



import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
/**
 * @author : Zhuang Jialong
 * @description : 基本任务接口
 *                添加定时任务时，必须将任务类（例：TestJob）添加到
 *                com.reda.monitor.task.config.QuarzJobNameEnum枚举类当中。
 *                然后再在页面上进行添加，任务名称：类名（TestJob），任务分组为task，cron表达式为自己配置，描述为自己填写
 * @date : 2020/9/10 19:19
 * @Copyright: Copyright(c)2020 RedaFlight.com All Rights Reserved
 */
public interface BaseJob extends Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException;
}
