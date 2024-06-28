package com.example.capstone_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/healthcheck")
@RequiredArgsConstructor
public class HealthcheckController {
    @GetMapping("")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}
