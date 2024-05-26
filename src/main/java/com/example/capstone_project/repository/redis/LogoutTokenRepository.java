package com.example.capstone_project.repository.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@org.springframework.stereotype.Repository
public class LogoutTokenRepository {
    private final RedisTemplate<String, String> template;

    private final String LOGOUT_TOKEN_KEY = "logout.token:";

    @Autowired
    public LogoutTokenRepository(RedisTemplate<String, String> template) {
        this.template = template;
    }

    public void save(String accessToken, Duration expiredTime) {
        this.template.opsForValue().set(this.LOGOUT_TOKEN_KEY + accessToken, Boolean.toString(true), expiredTime);
    }

    public Boolean find(String accessToken) {
        return Boolean.parseBoolean(this.template.opsForValue().get(this.LOGOUT_TOKEN_KEY + accessToken));
    }

    public void delete(String accessToken) {
        this.template.opsForValue().getAndDelete(this.LOGOUT_TOKEN_KEY + accessToken);
    }
}
