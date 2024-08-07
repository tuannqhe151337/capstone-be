package com.example.capstone_project.service;

public interface GenerateAnnualReportService {
    void generateAnnualReport();

    void generateActualCostAndExpectedCost(Long termId);
}