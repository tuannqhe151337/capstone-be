package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.LoginRequestBody;
import com.example.capstone_project.controller.responses.auth.LoginResponse;
import com.example.capstone_project.controller.body.RefreshTokenBody;
import com.example.capstone_project.controller.responses.auth.UserDataResponse;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.service.impl.AuthService;
import com.example.capstone_project.service.result.LoginResult;
import com.example.capstone_project.service.result.TokenPair;
import com.example.capstone_project.utils.helper.JwtHelper;
import com.example.capstone_project.utils.helper.UserHelper;
import com.example.capstone_project.utils.mapper.LoginResultResponseMapperImpl;
import com.example.capstone_project.utils.mapper.UserEntityDetailResponseMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequestBody body) {
        final String username = body.getUsername();
        final String password = body.getPassword();

        try {
            LoginResult loginResult = this.authService.login(username, password);

            return ResponseEntity.ok(
                    new LoginResultResponseMapperImpl().mapToLoginResponse(loginResult)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("me")
    public ResponseEntity<UserDataResponse> me() {
        try {
            int userId = UserHelper.getUserId();
            User user = this.authService.getDetailedUserById(userId);

            return ResponseEntity.ok(
                new UserEntityDetailResponseMapperImpl().mapToUserDataResponse(user)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("refresh-token")
    public ResponseEntity<TokenPair> refreshToken(
            @RequestHeader("Authorization") String token,
            @RequestBody RefreshTokenBody body)
    {
        final String accessToken = token.substring(7);
        final String refreshToken = body.getRefreshToken();

        try {
            TokenPair tokenPair = this.authService.refreshToken(accessToken, refreshToken);

            return ResponseEntity.ok(
                    TokenPair.builder()
                            .accessToken(tokenPair.getAccessToken())
                            .refreshToken(tokenPair.getRefreshToken())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // Have to use /api/*, otherwise /logout will be redirected to /login?logout
    @PostMapping("logout")
    public ResponseEntity<Object> logout(@RequestHeader("Authorization") String accessToken) {
        this.authService.logout(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
