package com.example.capstone_project.service.impl;


import com.example.capstone_project.repository.FinancialPlanRepository;
import com.example.capstone_project.service.TermService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TermServiceImpl implements TermService {
    private final FinancialPlanRepository financialPlanRepository;



}

