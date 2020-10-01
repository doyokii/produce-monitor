package com.reda.monitor.task.config;


import com.reda.monitor.task.schedule.TranslateLogFileJob;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author
 * @Description:
 * @Create Date Time: 2019年11月22日 09:45 上午
 * @Update Date Time: 2019年11月22日 09:45 上午
 * @see
 */

/**
 * @author : Zhuang Jialong
 * @description : 添加定时任务时，必须将任务类（例：TestJob）添加到此枚举类中
 * @date : 2020/9/10 19:24
 * @Copyright: Copyright(c)2020 RedaFlight.com All Rights Reserved
 */
public enum QuarzJobNameEnum {

    TranslateLogFileJobEnum("TranslateLogFileJob", TranslateLogFileJob.class);

    private String jobName;
    private Class jobFullPathName;

    private QuarzJobNameEnum(String jobName, Class jobClass) {
        this.jobName = jobName;
        this.jobFullPathName = jobClass;
    }

    public String getJobName() {
        return jobName;
    }

    public static Class getJobClass(String className) {
        QuarzJobNameEnum[] enums = values();
        AtomicReference<Class> clazz = null;
        Arrays.asList(enums).stream().forEach(e -> {
            if (e.getJobName().equals(className)) {
                clazz.set(e.getJobFullPathName());
            }
        });
        return clazz.get();
    }

    public Class getJobFullPathName() {
        return jobFullPathName;
    }
}
