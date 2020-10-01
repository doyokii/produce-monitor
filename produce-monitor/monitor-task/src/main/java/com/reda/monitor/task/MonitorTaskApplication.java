package com.reda.monitor.task;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author : Zhuang Jialong
 * @description :
 * @date : 2020/9/16 下午 3:58
 * @Copyright: Copyright(c)2019 RedaFlight.com All Rights Reserved
 */
@SpringBootApplication(scanBasePackages = {"com.reda.monitor.task"})
@EnableScheduling
@EnableAsync
@MapperScan("com.reda.monitor.task.mapper")
public class MonitorTaskApplication {
   /**
    * @author : Zhuang Jialong
    * @description : 程序启动入口
    * @date : 2020/9/10 18:54
    * @Copyright: Copyright(c)2020 RedaFlight.com All Rights Reserved
    */
    public static void main(String[] args) {
        SpringApplication.run(MonitorTaskApplication.class, args);
    }

}
