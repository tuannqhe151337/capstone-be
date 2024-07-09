package com.example.capstone_project.service;

import com.example.capstone_project.entity.Term;
import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface TermService {
    long countDistinctListTermWhenCreatePlan(String query) throws Exception;

    List<Term> getListTermWhenCreatePlan(String query, Pageable pageable) throws Exception;

    List<Term> getListTermPaging(String query, Pageable pageable);

    long countDistinctListTermPaging(String query);

    void createTerm(Term term) throws Exception;

    Term findTermById(Long id) throws Exception;

    void deleteTerm(Long id) throws Exception;
}

