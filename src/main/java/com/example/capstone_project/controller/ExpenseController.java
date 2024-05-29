package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.confirmExpenses.ConfirmNewExpenseBody;
import com.example.capstone_project.entity.AccessTokenClaim;
import com.example.capstone_project.utils.helper.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
public class ExpenseController {
    private final JwtHelper jwtHelper;

    @PostMapping("/upload")
    public ResponseEntity<ConfirmNewExpenseBody> confirmExpenses(
            @RequestHeader("Authorization") String token,
            @RequestBody ConfirmNewExpenseBody body) {
        //Get access token
        final String accessToken = token.substring(7);

        //Get department ID
        AccessTokenClaim accessTokenClaim = jwtHelper.parseToken(accessToken);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
