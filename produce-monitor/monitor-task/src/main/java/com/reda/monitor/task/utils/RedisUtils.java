package com.reda.monitor.task.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : Zhuang Jialong
 * @description :
 * @date : 2020/9/16 下午 3:23
 * @Copyright: Copyright(c)2019 RedaFlight.com All Rights Reserved
 */
@Component
public class RedisUtils implements InitializingBean {

    private static final String SHARDED = "SHARDED";

    private static final String CLUSTER = "CLUSTER";

    private static String redisType;
    private static String redisHost;
    private static String redisPoolSize;

    @Value("${redis.pool.type}")
    private String type;
    @Value(("${redis.node.address:127.0.0.1:6379}"))
    private String host;
    @Value("${redis.pool.maxTotal:8}")
    private String size;

    private static ShardedJedisPool jedisPool;
    private static JedisCluster jedisCluster;

    /**
     * 初始化jedisPool&jedisCluster
     */
    private static void init(){
        if (SHARDED.equalsIgnoreCase(redisType)) {
            jedisPool = new ShardedJedisPool(buildConfig(Integer.parseInt(redisPoolSize)), getJedisShardInfos(redisHost));
        } else if (CLUSTER.equalsIgnoreCase(redisType)) {
            jedisCluster = new JedisCluster(getNodes(redisHost));
        } else {
            throw new IllegalStateException("redis configuration may not init correct!please check it out!");
        }
    }

    public static JedisCommands getRedisCommands() {
        if(jedisPool==null && jedisCluster==null){
            init();
        }
        if (SHARDED.equals(redisType)) {
            return jedisPool.getResource();
        } else if (CLUSTER.equals(redisType)) {
            return jedisCluster;
        } else {
            throw new RuntimeException("redis type does not exist,please check the configuration of service.properties");
        }

    }

    public static void close(JedisCommands commands) {
        if (commands != null) {
            if (commands instanceof ShardedJedis) {
                ((ShardedJedis) commands).close();
            }
        }
    }

    private static Set<HostAndPort> getNodes(String redisHost) {
        String[] hosts = redisHost.split(",");
        Set<HostAndPort> hostAndPorts = new HashSet<>(hosts.length);
        for (String host : hosts) {
            String[] split = host.split(":");
            hostAndPorts.add(new HostAndPort(split[0], Integer.parseInt(split[1])));
        }
        return hostAndPorts;
    }

    private static JedisPoolConfig buildConfig(Integer redisPoolMaxTotal) {
        JedisPoolConfig jpc = new JedisPoolConfig();
        jpc.setMaxTotal(redisPoolMaxTotal);
        jpc.setMaxWaitMillis(1000L);
        jpc.setTestOnBorrow(true);
        return jpc;
    }

    private static List<JedisShardInfo> getJedisShardInfos(String hostsAndPorts) {
        List<JedisShardInfo> rs = new ArrayList();
        String[] split = redisHost.split(":");
        rs.add(new JedisShardInfo(split[0], Integer.parseInt(split[1])));
        return rs;
    }

    /**
     * 为解决static属性无法注入
     * 实现spring的InitializingBean接口，并重写afterPropertiesSet方法
     * 在调用之前将变量赋值给静态常量
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        redisType=type;
        redisHost=host;
        redisPoolSize=size;
    }
}
