package com.example.capstone_project.repository.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Set;

// In case we need more complex authorization mechanism
@Repository
public class UserDepartmentAuthorityRepository {
    private final RedisTemplate<String, String> template;

    @Autowired
    public UserDepartmentAuthorityRepository(RedisTemplate<String, String> template) {
        this.template = template;
    }

    private String generateBasePattern(Integer userId) {
        return "user:" + userId + ":authorities:department:";
    }

    private String generateKey(Integer userId, Integer groupId) {
        return this.generateBasePattern(userId) + groupId;
    }

    private String generateDeletePattern(Integer userId) {
        return this.generateBasePattern(userId) + "*";
    }

    public void save(Integer userId, Integer departmentId, List<String> authorities, Duration expiredTime) {
        this.template.opsForSet().add(
                this.generateKey(userId, departmentId),
                authorities.toArray(String[]::new)
        );
        this.template.expire(this.generateKey(userId, departmentId), expiredTime);
    }

    public Set<String> find(Integer userId, Integer departmentId) {
        return this.template.opsForSet().members(this.generateKey(userId, departmentId));
    }

    public void delete(Integer userId) {
        Set<String> keys = this.template.keys(this.generateDeletePattern(userId));
        if (keys != null) {
            this.template.delete(keys);
        }
    }
}
