package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.report.delete.DeleteReportBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteReport(
            @Valid @RequestBody DeleteReportBody reportBody
            ){
        System.out.println(reportBody.toString());
        return null;
    }
}
