package com.example.capstone_project.service.impl;

import com.example.capstone_project.config.JwtHelper;
import com.example.capstone_project.repository.LogoutTokenRepository;
import com.example.capstone_project.repository.UserGroupAuthorityRepository;
import com.example.capstone_project.repository.result.GroupUserAuthorityCode;
import com.example.capstone_project.service.result.TokenPair;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${application.security.access-token.expiration}")
    private long EXPIRATION;

    private final AuthenticationProvider authenticationProvider;

    private final JwtHelper jwtHelper;

    private final UserGroupAuthorityRepository userGroupAuthorityRepository;

    private final LogoutTokenRepository logoutTokenRepository;

//    private final GroupUserAuthorityRepository groupUserAuthorityRepository;

    public TokenPair login(String username, String password) throws Exception {
        // Login using authentication provider
        Authentication usernamePasswordAuthenticationToken = this.authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        if (usernamePasswordAuthenticationToken == null || usernamePasswordAuthenticationToken.getPrincipal() == null) {
            throw new Exception();
        }

        // Get user and all group authorities of his group
        int userId = Integer.parseInt(usernamePasswordAuthenticationToken.getPrincipal().toString());

        this.saveUserGroupAuthoritiesToRedis(userId);

        // Generate token
        String accessToken = this.jwtHelper.generateToken(userId);
        String refreshToken = this.jwtHelper.generateRefreshToken(userId);

        return TokenPair.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenPair refreshToken(String token, String refreshToken) throws Exception {
        // Check if accessToken and refreshToken belong to the same user
        // Extract userId from token and compare
        final int userIdFromToken = this.jwtHelper.extractUserIdFromExpiredToken(token);
        final int userIdFromRefreshToken = this.jwtHelper.extractUserIdFromRefreshToken(refreshToken);

        if (userIdFromToken != userIdFromRefreshToken) {
            throw new Exception();
        }

        this.saveUserGroupAuthoritiesToRedis(userIdFromToken);

        // Generate token
        return TokenPair.builder()
                .accessToken(this.jwtHelper.generateToken(userIdFromToken))
                .refreshToken(this.jwtHelper.generateRefreshToken(userIdFromToken))
                .build();
    }

    public void logout(String accessToken) {
        accessToken = accessToken.substring(7); // Remove Bearer

        // Remove user authorities from redis
        final int userId = this.jwtHelper.extractUserId(accessToken);
        this.userGroupAuthorityRepository.delete(userId);

        // Add the accessToken into "blacklist"
        this.logoutTokenRepository.save(accessToken, Duration.ofMillis(EXPIRATION));
    }

    private void saveUserGroupAuthoritiesToRedis(int userId) {
//        List<GroupUserAuthorityCode> listAuthority = groupUserAuthorityRepository.getAllUserGroupAuthority((long) userId);
//
//        Map<Long, List<String>> userGroupIdListHashMap = new HashMap<>();
//
//        listAuthority.forEach(groupUserAuthorityCode -> {
//            if (userGroupIdListHashMap.containsKey(groupUserAuthorityCode.getGroupId())) {
//                userGroupIdListHashMap.get(groupUserAuthorityCode.getGroupId())
//                        .add(groupUserAuthorityCode.getAuthorityCode());
//            } else {
//                userGroupIdListHashMap.put(groupUserAuthorityCode.getGroupId(), new ArrayList<>());
//                userGroupIdListHashMap.get(groupUserAuthorityCode.getGroupId())
//                        .add(groupUserAuthorityCode.getAuthorityCode());
//            }
//        });
//
//        // Save user and all hist group's authorities to redis
//        userGroupIdListHashMap.forEach((groupUserId, authorities) -> {
//            userGroupIdListHashMap.forEach((userGroupId, strings) -> {
//                this.userGroupAuthorityRepository.save(
//                        userId,
//                        userGroupId.intValue(),
//                        authorities,
//                        Duration.ofMillis(1000 * 60 * 24)
//                );
//            });
//        });
    }
}
