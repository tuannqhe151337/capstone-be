package com.example.capstone_project.service.scheduler;

import com.example.capstone_project.entity.Term;
import com.example.capstone_project.repository.TermRepository;
import com.example.capstone_project.service.TermService;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TermSchedulerService {
private final TermService termService;
private final TermRepository termRepository;

    @Scheduled(cron =  "0 55 20 * * *") // Execute at 12:00 AM every day
    @Transactional
    @Async
    public void startTerm() throws Exception {
  //START TERM
      List<Term> terms = termRepository.findAll();
        //change status to 2 (IN_PROGRESS)
        for(Term term : terms) {
            //check term status not started (id 1) - turn to in progress (id 2)
            if(term.getStatus().getId() == 1 &&
                    //date-month-year equals today, now
                    term.getStartDate().toLocalDate().isEqual(LocalDate.now())){
                termService.updateTermStatus(term, 2L);
            }
        }
    }
    @Scheduled(cron =  "0 55 20 * * *") // Execute at 12:00 AM every day
    @Transactional
    @Async
    public void endTerm() throws Exception {
        //START TERM
        List<Term> terms = termRepository.findAll();
        //change status to 3 (CLOSED)
        for(Term term : terms) {
            if(term.getStatus().getId() == 2 &&
                    term.getEndDate().toLocalDate().isEqual(LocalDate.now())){
                termService.updateTermStatus(term, 3L);
            }
        }
    }





}
