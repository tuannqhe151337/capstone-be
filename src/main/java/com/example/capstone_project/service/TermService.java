package com.example.capstone_project.service;

import com.example.capstone_project.entity.Term;
import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface TermService {
    long countDistinct(String query) throws Exception;

    List<Term> getListTermWhenCreatePlan(String query, Pageable pageable) throws Exception;

    long countDistinctListTermWhenCreatePlan(String query);

    List<Term> getListTermPaging(String query, Pageable pageable);

    void createTerm(Term term) throws Exception;

    Term findTermById(Long id) throws Exception;

    long countDistinctListTermPaging(String query);
}
