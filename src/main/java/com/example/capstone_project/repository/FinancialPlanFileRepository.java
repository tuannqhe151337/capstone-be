package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlanFile;
import com.example.capstone_project.repository.result.FileNameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FinancialPlanFileRepository extends JpaRepository<FinancialPlanFile, Long> {
    @Query(value = " SELECT files.id AS fileId, term.name AS termName, department.code AS departmentCode, RANK() OVER(PARTITION BY plan.id ORDER BY files.createdAt ASC) AS version FROM Term term " +
            " LEFT JOIN term.financialPlans plan " +
            " LEFT JOIN plan.department department " +
            " LEFT JOIN plan.planFiles files " +
            " WHERE plan.id = :planId ")
    List<FileNameResult> generateFileName(int planId);
    @Query(value = " SELECT files.id AS fileId, term.name AS termName, department.code AS departmentCode, RANK() OVER(PARTITION BY plan.id ORDER BY files.createdAt ASC) AS version FROM Term term " +
            " LEFT JOIN term.financialPlans plan " +
            " LEFT JOIN plan.department department " +
            " LEFT JOIN plan.planFiles files " +
            " WHERE plan.id = :planId " +
            " ORDER BY version desc " +
            " LIMIT 1 ")
    FileNameResult getLastVersionFileName(Long planId);
}
