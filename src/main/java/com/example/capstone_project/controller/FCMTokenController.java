package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.fcmToken.DeleteTokenBody;
import com.example.capstone_project.controller.body.fcmToken.RegisterTokenBody;
import com.example.capstone_project.service.FCMTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FCMTokenController {
    private final FCMTokenService fcmTokenService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterTokenBody body) {
        try {
            this.fcmTokenService.registerToken(body.getToken());
            return ResponseEntity.status(HttpStatus.CREATED).body(null);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Object> remove(@RequestBody DeleteTokenBody body) {
        try {
            this.fcmTokenService.removeToken(body.getToken());
            return ResponseEntity.status(HttpStatus.OK).body(null);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
