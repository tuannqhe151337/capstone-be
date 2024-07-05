package com.example.capstone_project.repository.redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;


@Repository
public class UserIdTokenRepository {
    private final RedisTemplate<String, String> template;

    private final String USER_ID = "user";

    @Autowired
    public UserIdTokenRepository(RedisTemplate<String, String> template) {
        this.template = template;
    }

    public void save(String blankToken, Long userId, Duration expiredTime) {
        this.template.opsForValue().set(this.USER_ID+ ":otp-token:" + blankToken, String.valueOf(userId), expiredTime);
    }

    public String find(String blankToken) {
        return this.template.opsForValue().get(this.USER_ID+ ":otp-token:" + blankToken);
    }

    public void delete(String accessToken) {
        this.template.opsForValue().getAndDelete(this.USER_ID + accessToken);
    }
}

