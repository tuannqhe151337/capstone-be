package com.example.capstone_project.repository.redis;


import com.example.capstone_project.entity.UserDetail;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class OTPTokenRepository {
    private final RedisTemplate<String, String> template;

    @Autowired
    public OTPTokenRepository(RedisTemplate<String, String> template) {
        this.template = template;
    }

    private String generateKey(String tokenString, long userId) {
        return "user:"+userId+":otp-code:" + tokenString;
    }

    public void save(long userId, String tokenString,  String otpCode, Duration expiredTime) {
        HashMap<String, String> userAndOtp = new HashMap<>();
        userAndOtp.put("userId", String.valueOf(userId));
        userAndOtp.put("otpCode", otpCode);

        this.template.opsForHash().putAll(
                this.generateKey(tokenString, userId),
                userAndOtp
        );
        this.template.expire(this.generateKey(tokenString, userId), expiredTime);
    }

    //get otp code
    public String getOtpCode(String tokenString, long userId) {
        Map<Object, Object> userAndOtp = this.template.opsForHash().entries(this.generateKey(tokenString, userId));
        for (Object key : userAndOtp.keySet()) {
            if (key.toString().equals("otpCode") ){
                return (String) userAndOtp.get(key);
            }
        }
        return null;
    }
    //get user id
    public String getUserID(String tokenString, long userId) {
        Map<Object, Object> userAndOtp = this.template.opsForHash().entries(this.generateKey(tokenString, userId));
        for (Object key : userAndOtp.keySet()) {
            if (key.toString().equals(Fields.USER_ID.getValue())) {
                return (String) userAndOtp.get(key);
            }
        }
        return null;
    }
    //get user id
    public String getUserID(String tokenString) {
        Set<String> keys =  this.template.keys("*");
        if ( keys!= null) {
            for (String key : keys) {
                if (key.contains(tokenString)) {
                    String[] parts = key.split(":");
                    String userId = parts[1];
                  return userId;
                }
            }
        }

        return null;
    }
    public void deleteOtpCodeExists(long userId) {
        Set<String> keys =  this.template.keys("*");
        if ( keys!= null) {
            for (String key : keys) {
                if (key.contains("user:"+userId+":otp-code")) {
                    String[] parts = key.split(":");
                    // get token 3rd part
                    String token = parts[parts.length - 1];
                    //xoa ca key and value do
                    delete(token, userId);
                }
            }
        }
    }
    public void delete(String token, long userId) {
        this.template.delete(this.generateKey(token, userId));
    }
}

@Getter
enum Fields {
    USER_ID("userId"),
    OTP_CODE("otpCode");

    private final String value;

    Fields(String value) {
        this.value = value;
    }
}

