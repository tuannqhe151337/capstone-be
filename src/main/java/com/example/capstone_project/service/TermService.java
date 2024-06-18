package com.example.capstone_project.service;

import com.example.capstone_project.entity.Term;
import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface TermService {
    long countDistinctListTermWhenCreatePlan(String query);

    List<Term> getListTermWhenCreatePlan(String query, Pageable pageable);

    List<Term> getListTermPaging(String query, Pageable pageable);

    long countDistinctListTermPaging(String query);
}
