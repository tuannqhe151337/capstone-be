package com.example.capstone_project.service.impl;

import com.example.capstone_project.config.JwtHelper;
import com.example.capstone_project.entity.Authority;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.repository.AuthorityRepository;
import com.example.capstone_project.repository.LogoutTokenRepository;
import com.example.capstone_project.repository.UserGroupAuthorityRepository;
import com.example.capstone_project.repository.UserRepository;
import com.example.capstone_project.repository.result.GroupUserAuthorityCode;
import com.example.capstone_project.service.result.LoginResult;
import com.example.capstone_project.service.result.TokenPair;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${application.security.access-token.expiration}")
    private long EXPIRATION;

    private final AuthenticationProvider authenticationProvider;

    private final JwtHelper jwtHelper;

    private final UserGroupAuthorityRepository userGroupAuthorityRepository;

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final LogoutTokenRepository logoutTokenRepository;

    public LoginResult login(String username, String password) throws Exception {
        // Login using authentication provider
        Authentication usernamePasswordAuthenticationToken = this.authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        if (usernamePasswordAuthenticationToken == null || usernamePasswordAuthenticationToken.getPrincipal() == null) {
            throw new Exception();
        }

        // Get user and all authorities
        int userId = Integer.parseInt(usernamePasswordAuthenticationToken.getPrincipal().toString());
        final User user = this.getDetailedUserById(userId);
        final List<Authority> authorities = this.authorityRepository.findAuthoritiesOfRole(user.getRole().getId());
        user.setAuthorities(authorities);

        // Save authorities to redis
        this.saveUserGroupAuthoritiesToRedis(userId);

        // Generate token
        String accessToken = this.jwtHelper.generateToken(userId, user.getRole().getCode(), user.getDepartment().getId());
        String refreshToken = this.jwtHelper.generateRefreshToken(userId);

        return LoginResult.builder()
                .tokenPair(
                        TokenPair.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .build()
                )
                .user(user)
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

        // Get user and authorities
        final User user = this.getDetailedUserById(userIdFromToken);
        final List<Authority> authorities = this.authorityRepository.findAuthoritiesOfRole(user.getRole().getId());
        user.setAuthorities(authorities);

        // Save authorities to redis
        this.saveUserGroupAuthoritiesToRedis(userIdFromToken);

        // Generate token
        TokenPair tokenPair = TokenPair.builder()
                .accessToken(this.jwtHelper.generateToken(userIdFromToken, user.getRole().getCode(), user.getDepartment().getId()))
                .refreshToken(this.jwtHelper.generateRefreshToken(userIdFromToken))
                .build();

        return TokenPair.builder()
                .accessToken(tokenPair.getAccessToken())
                .refreshToken(tokenPair.getRefreshToken())
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

    private User getDetailedUserById(int userId) throws Exception {
        Optional<User> userOptional = this.userRepository.findUserById((long) userId);
        if (userOptional.isEmpty()) {
            throw new Exception("User does not exists!");
        }

        final User user = userOptional.get();

        if (user.getRole() == null || user.getDepartment() == null) {
            throw new Exception("Role or department is empty!");
        }

        return user;
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
