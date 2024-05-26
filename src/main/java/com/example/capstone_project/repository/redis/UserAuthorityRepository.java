package com.example.capstone_project.repository.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public class UserAuthorityRepository {
    private final RedisTemplate<String, String> template;

    @Autowired
    public UserAuthorityRepository(RedisTemplate<String, String> template) {
        this.template = template;
    }

    private String generateKey(long userId) {
        return "user:" + userId + ":authorities";
    }

    public void save(long userId, List<String> authorities, Duration expiredTime) {
        this.template.opsForSet().add(
                this.generateKey(userId),
                authorities.toArray(String[]::new)
        );
        this.template.expire(this.generateKey(userId), expiredTime);
    }

    public Set<String> get(long userId) {
        return this.template.opsForSet().members(this.generateKey(userId));
    }

    public void delete(long userId) {
        this.template.delete(this.generateKey(userId));
    }
}