package com.atguigu.yygh.order.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wang
 * @create 2022-07-01
 */
@Configuration
public class RedissonConfig {
    //redis服务器的配置，
    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        //单机
        config.useSingleServer().setAddress("redis://" + "192.168.80.128" + ":" + "6379");
        //useSentinelServer：是用哨兵
        //useClusterServer：是用集群
        //useMasterSlaveServers：是用主从
        return Redisson.create(config);
    }
}
