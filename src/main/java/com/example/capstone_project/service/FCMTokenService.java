package com.example.capstone_project.service;

import org.springframework.stereotype.Service;

public interface FCMTokenService {
    void registerToken(String token) throws Exception;

    void removeToken(String token) throws Exception;
}
