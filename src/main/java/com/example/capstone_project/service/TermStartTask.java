package com.example.capstone_project.service;

import com.example.capstone_project.entity.Term;
import com.example.capstone_project.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TermStartTask {
private final TermService termService;
private final TermRepository termRepository;

    @Scheduled(cron =  "0 22 2 * * *") // Execute at 1:20 AM every day
    public void yourTask() throws Exception {
        //get all term which status is 1L ,  start date is now

      List<Term> terms = termRepository.findTermsByStatusIdAndStartDate();
        System.out.println(terms);
        //change status to 2 (IN_PROGRESS)
        for(Term term : terms) {
            termService.updateTermStatus(term);
            System.out.println("Gina xinh gai");
        }

    }



}
