package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.LoginRequestBody;
import com.example.capstone_project.controller.responses.TokenPairResponse;
import com.example.capstone_project.controller.body.RefreshTokenBody;
import com.example.capstone_project.service.impl.AuthService;
import com.example.capstone_project.service.result.TokenPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {
    private final AuthService authService;

    @Autowired
    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("api/login")
    public ResponseEntity<TokenPairResponse> login(@RequestBody LoginRequestBody body) {
        final String username = body.getUsername();
        final String password = body.getPassword();

        try {
            TokenPair tokenPair = this.authService.login(username, password);

            return ResponseEntity.ok(
                    TokenPairResponse.builder()
                            .accessToken(tokenPair.getAccessToken())
                            .refreshToken(tokenPair.getRefreshToken())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @PostMapping("api/refresh-token")
    public ResponseEntity<TokenPairResponse> refreshToken(@RequestBody RefreshTokenBody body) {
        final String token = body.getToken();
        final String refreshToken = body.getRefreshToken();

        try {
            TokenPair tokenPair = this.authService.refreshToken(token, refreshToken);

            return ResponseEntity.ok(
                    TokenPairResponse.builder()
                            .accessToken(tokenPair.getAccessToken())
                            .refreshToken(tokenPair.getRefreshToken())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    // Have to use /api/*, otherwise /logout will be redirected to /login?logout
    @PostMapping("api/logout")
    public ResponseEntity<Object> logout(@RequestHeader("Authorization") String accessToken) {
        this.authService.logout(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
