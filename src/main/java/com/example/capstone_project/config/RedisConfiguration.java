package com.example.capstone_project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
class RedisConfiguration {
    @Value("${application.redis-host}")
    private String REDIS_HOST;

    @Value("${application.redis-port}")
    private int REDIS_PORT;

    @Bean
    public JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(REDIS_HOST);
        configuration.setPort(REDIS_PORT);
        return new JedisConnectionFactory(configuration);
    }
}
