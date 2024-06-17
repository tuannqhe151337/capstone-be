package com.example.capstone_project.repository.redis;

import com.example.capstone_project.entity.UserDetail;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDetailRepository {
    private final RedisTemplate<String, String> template;

    @Autowired
    public UserDetailRepository(RedisTemplate<String, String> template) {
        this.template = template;
    }

    private String generateKey(long userId) {
        return "user:" + userId + ":detail";
    }

    public void save(long userId, UserDetail userDetail, Duration expiredTime) {
        HashMap<String, String> userDetailHashMap = new HashMap<>();

        userDetailHashMap.put(Field.DEPARTMENT_ID.getValue(), String.valueOf(userDetail.getDepartmentId()));
        userDetailHashMap.put(Field.ROLE_CODE.getValue(), userDetail.getRoleCode());

        this.template.opsForHash().putAll(
                this.generateKey(userId),
                userDetailHashMap
        );
        this.template.expire(this.generateKey(userId), expiredTime);
    }

    public UserDetail get(long userId) throws Exception {
        Map<Object, Object> userDetailHashmap = this.template.opsForHash().entries(this.generateKey(userId));

        UserDetail userDetail = new UserDetail();
        for (Object key: userDetailHashmap.keySet()) {
            if (key.toString().equals(Field.DEPARTMENT_ID.getValue())) {
                long departmentId = Long.parseLong(userDetailHashmap.get(key).toString());
                userDetail.setDepartmentId(departmentId);
            } else if (key.toString().equals(Field.ROLE_CODE.getValue())) {
                String roleCode = userDetailHashmap.get(key).toString();
                userDetail.setRoleCode(roleCode);
            }
        }

        return userDetail;
    }

    public void delete(long userId) {
        this.template.delete(this.generateKey(userId));
    }
}

@Getter
enum Field {
    DEPARTMENT_ID("departmentId"),
    ROLE_CODE("roleCode");

    private final String value;

    Field(String value) {
        this.value = value;
    }
}
