package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.TermInterval;
import com.example.capstone_project.repository.TermIntervalRepository;
import com.example.capstone_project.service.TermIntervalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TermIntervalServiceImpl implements TermIntervalService {
    private final TermIntervalRepository termIntervalRepository;

    @Override
    public TermInterval getTermInterval(Long id) {
        return termIntervalRepository.findById(1).get();
    }
}
