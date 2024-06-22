package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.TermStatus;
import com.example.capstone_project.repository.TermStatusRepository;
import com.example.capstone_project.service.TermService;
import com.example.capstone_project.service.TermStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TermStatusServiveImpl implements TermStatusService {
    private final TermStatusRepository termStatusRepository;
    @Override
    public List<TermStatus> getTermStatuses() {
        return termStatusRepository.findAll();
    }
}
