package com.example.capstone_project.service;


import com.example.capstone_project.entity.TermStatus;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TermStatusService {

    List<TermStatus> getTermStatuses();
}
