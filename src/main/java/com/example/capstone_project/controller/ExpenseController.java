package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.plan.create.NewPlanBody;
import com.example.capstone_project.entity.AccessTokenClaim;
import com.example.capstone_project.utils.helper.JwtHelper;
import com.example.capstone_project.utils.mapper.body.plan.create.CreatePlanMapper;
import com.example.capstone_project.utils.mapper.body.plan.create.CreatePlanMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
public class ExpenseController {
    private final JwtHelper jwtHelper;

    @PostMapping("/create")
    public ResponseEntity<NewPlanBody> confirmExpenses(
            @RequestHeader("Authorization") String token,
            @RequestBody NewPlanBody body) {
        //Get access token
        final String accessToken = token.substring(7);

        //Get department ID
        AccessTokenClaim accessTokenClaim = jwtHelper.parseToken(accessToken);

        System.out.println(new CreatePlanMapperImpl().newPlanBodyToPlanMapping(body));

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
