package com.example.capstone_project.service.impl;


import com.example.capstone_project.controller.responses.term.get_plans.TermDetailResponse;
import com.example.capstone_project.controller.responses.term.get_plans.TermPlanDetailResponse;
import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.repository.FinancialPlanRepository;
import com.example.capstone_project.repository.TermRepository;
import com.example.capstone_project.service.term.TermService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service

public class TermServiceImpl implements TermService {
    private final FinancialPlanRepository financialPlanRepository;



}

