package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.LoginRequestBody;
import com.example.capstone_project.controller.responses.auth.LoginResponse;
import com.example.capstone_project.controller.responses.auth.TokenPairResponse;
import com.example.capstone_project.controller.body.RefreshTokenBody;
import com.example.capstone_project.service.impl.AuthService;
import com.example.capstone_project.service.result.LoginResult;
import com.example.capstone_project.service.result.TokenPair;
import com.example.capstone_project.utils.mapper.LoginResultResponseMapper;
import com.example.capstone_project.utils.mapper.LoginResultResponseMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
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
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @PostMapping("refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshTokenBody body) {
        final String token = body.getToken();
        final String refreshToken = body.getRefreshToken();

        try {

            return ResponseEntity.ok(
                    LoginResponse.builder().build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    // Have to use /api/*, otherwise /logout will be redirected to /login?logout
    @PostMapping("logout")
    public ResponseEntity<Object> logout(@RequestHeader("Authorization") String accessToken) {
        this.authService.logout(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
