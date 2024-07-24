package com.example.capstone_project.service.scheduler;

import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.PlanStatus;
import com.example.capstone_project.entity.Term;
import com.example.capstone_project.repository.FinancialPlanRepository;
import com.example.capstone_project.repository.FinancialReportExpenseRepository;
import com.example.capstone_project.repository.PlanStatusRepository;
import com.example.capstone_project.repository.TermRepository;
import com.example.capstone_project.service.TermService;
import com.example.capstone_project.utils.enums.PlanStatusCode;
import com.example.capstone_project.utils.enums.TermCode;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TermSchedulerService {
    private final TermService termService;
    private final TermRepository termRepository;
    private final FinancialPlanRepository planRepository;
    private final PlanStatusRepository planStatusRepository;
    private final FinancialReportExpenseRepository expenseRepository;


    @Scheduled(cron = "0 55 20 * * *") // Execute at 12:00 AM every day
    @Transactional
    @Async
    public void startTerm() throws Exception {
        //START TERM
        List<Term> terms = termRepository.getListTermNeedToStart(TermCode.NOT_STARTED, LocalDateTime.now());
        //change status to 2 (IN_PROGRESS)
        if (terms != null) {
            for (Term term : terms) {
                termService.updateTermStatus(term, 2L);
            }
        }
    }

    @Scheduled(cron = "0 55 20 * * *") // Execute at 12:00 AM every day
    @Transactional
    @Async
    public void endTerm() throws Exception {
        //START TERM
        List<Term> terms = termRepository.getListTermNeedToClose(TermCode.IN_PROGRESS, LocalDateTime.now());
        //change status to 3 (CLOSED)
        if (terms != null) {
            for (Term term : terms) {
                termService.updateTermStatus(term, 3L);

                List<FinancialPlan> listPlanNeedToClose = new ArrayList<>();
                planRepository.findAllByTermId(term.getId()).forEach(plan -> {
                    if (plan.getStatus().getCode().equals(PlanStatusCode.WAITING_FOR_REVIEWED)
                            || plan.getStatus().getCode().equals(PlanStatusCode.NEW)) {
                        // 3L - Reviewed
                        plan.setStatus(planStatusRepository.getReferenceById(3L));
                        // Change status expense
                        listPlanNeedToClose.add(plan);
                    }
                });

                planRepository.saveAll(listPlanNeedToClose);

                // Generate report
                listPlanNeedToClose.forEach(plan -> {

                });
            }
        }
    }
}
