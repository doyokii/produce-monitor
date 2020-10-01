package com.reda.monitor.task.config;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * @author : Zhuang Jialong
 * @description :
 * @date : 2020/9/15 下午 5:52
 * @Copyright: Copyright(c)2019 RedaFlight.com All Rights Reserved
 */

@Configuration
@PropertySource("classpath:parseMapping.properties")
public class ParseMappingConfig implements EnvironmentAware {

    private Environment environment;

    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
