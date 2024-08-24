package com.example.capstone_project.service;

import com.example.capstone_project.entity.Term;
import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface TermService {
    long countDistinctListTermWhenCreatePlan(String query) throws Exception;

    List<Term> getListTermWhenCreatePlan(String query, Pageable pageable) throws Exception;

    List<Term> getListTermPaging(Long statusId, String query, Integer pageInt, Integer sizeInt, String sortBy, String sortType);

    long countDistinctListTermPaging(Long statusId, String query);

    long countDistinct(String query) throws Exception;

    void createTerm(Term term) throws Exception;

    Term findTermById(Long id) throws Exception;

    void deleteTerm(Long id) throws Exception;

    Term updateTerm(Term term) throws Exception;

    void startTermManually(Long termId) throws Exception;


}
