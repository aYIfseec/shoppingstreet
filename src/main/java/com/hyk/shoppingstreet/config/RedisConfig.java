package com.hyk.shoppingstreet.config;

import com.hyk.shoppingstreet.common.utils.RedisUtil;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
    private JedisPool pool;

    @Value("${jedis.maxActive}")
    private String maxActive;
    @Value("${jedis.maxIdle}")
    private String maxIdle;
    @Value("${jedis.maxWait}")
    private String maxWait;
    @Value("${jedis.host}")
    private String host;
    @Value("${jedis.password}")
    private String password;
    @Value("${jedis.timeout}")
    private String timeout;
    @Value("${jedis.db}")
    private String db;
    @Value("${jedis.port}")
    private String port;
//    @Value("jedis.enable")
//    private String enable;
//    @Value("jedis.sysName")
//    private String sysName;


    @Bean
    public JedisPoolConfig constructJedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        // 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        config.setMaxTotal(Integer.parseInt(maxActive));
        // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(Integer.parseInt(maxIdle));
        // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(Integer.parseInt(maxWait));
        config.setTestOnBorrow(true);

        return config;
    }

    @Bean(name = "jedisPool")
    public JedisPool constructJedisPool() {
        String ip = this.host;
        int port = Integer.parseInt(this.port);
        String password = this.password;
        int timeout = Integer.parseInt(this.timeout);
        int database = Integer.parseInt(this.db);
        database = database >= 0 ? database : 0;
        if (null == pool) {
            pool = new JedisPool(constructJedisPoolConfig(), ip, port, timeout, password, database);
        }
        return pool;
    }

    @PostConstruct
    public void initJedisPool() {
        RedisUtil.setJedisPool(constructJedisPool());
    }
}
