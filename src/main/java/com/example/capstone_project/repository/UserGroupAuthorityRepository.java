package com.example.capstone_project.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Repository
public class UserGroupAuthorityRepository {
    private final RedisTemplate<String, String> template;

    @Autowired
    public UserGroupAuthorityRepository(RedisTemplate<String, String> template) {
        this.template = template;
    }

    private String generateBasePattern(Integer userId) {
        return "user:" + userId + ":authorities:group:";
    }

    private String generateKey(Integer userId, Integer groupId) {
        return this.generateBasePattern(userId) + groupId;
    }

    private String generateDeletePattern(Integer userId) {
        return this.generateBasePattern(userId) + "*";
    }

    public void save(Integer userId, Integer groupId, List<String> authorities, Duration expiredTime) {
        this.template.opsForSet().add(
                this.generateKey(userId, groupId),
                authorities.toArray(String[]::new)
        );
        this.template.expire(this.generateKey(userId, groupId), expiredTime);
    }

    public Set<String> find(Integer userId, Integer groupId) {
        return this.template.opsForSet().members(this.generateKey(userId, groupId));
    }

    public void delete(Integer userId) {
        Set<String> keys = this.template.keys(this.generateDeletePattern(userId));
        if (keys != null) {
            this.template.delete(keys);
        }
    }
}
