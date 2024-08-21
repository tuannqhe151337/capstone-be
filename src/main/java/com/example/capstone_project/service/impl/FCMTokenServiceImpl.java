package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.FCMToken;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.entity.UserDetail;
import com.example.capstone_project.repository.FCMTokenRepository;
import com.example.capstone_project.repository.UserRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.redis.UserDetailRepository;
import com.example.capstone_project.service.FCMTokenService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.enums.RoleCode;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FCMTokenServiceImpl implements FCMTokenService {
    private final FCMTokenRepository fcmTokenRepository;
    private final UserDetailRepository userDetailRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final UserRepository userRepository;

    @Override
    public void registerToken(String token) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {
            // If the FCM Token already exists, skip
            if (this.fcmTokenRepository.existsByToken(token)) {
                return;
            }

            // Else insert into database
            User user = this.userRepository.getReferenceById(userId);

            FCMToken fcmToken = FCMToken.builder()
                    .token(token)
                    .user(user)
                    .build();

            fcmTokenRepository.save(fcmToken);
        }
    }

    @Override
    public void removeToken(String token) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {
            this.fcmTokenRepository.deleteByToken(token);
        }
    }
}
