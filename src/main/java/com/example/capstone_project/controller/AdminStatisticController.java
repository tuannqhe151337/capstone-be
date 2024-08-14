package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.admin.AdminStatistic;
import com.example.capstone_project.service.AdminService;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminStatisticController {
    private final AdminService adminService;

    @GetMapping("/statistic")
    public ResponseEntity<AdminStatistic> getStatistic() {
        try {
            long totalDepartment = this.adminService.getTotalDepartment();
            long totalUser = this.adminService.getTotalEmployee();
            long totalPosition = this.adminService.getTotalPosition();

            return ResponseEntity.ok(AdminStatistic.builder()
                    .totalDepartment(totalDepartment)
                    .totalEmployee(totalUser)
                    .totalPosition(totalPosition)
                    .build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
