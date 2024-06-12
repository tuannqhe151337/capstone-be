package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.repository.result.PlanDetailResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FinancialPlanRepository extends JpaRepository<FinancialPlan, Long>, CustomFinancialPlanRepository {
    @Query( " SELECT plan.id AS planId, plan.name AS name, MAX(expense.unitPrice * expense.amount) AS biggestExpenditure, " +
            " SUM(expense.unitPrice * expense.amount) AS totalPlan, term.id AS termId, term.name AS termName, status.id AS statusId, " +
            " status.code AS statusName, term.planDueDate AS planDueDate, plan.createdAt AS createdAt, department.id AS departmentId, department.name AS departmentName, " +
            " user.id AS userId , user.username AS username, files.version AS version" +
            " FROM FinancialPlan plan " +
            " JOIN plan.term term " +
            " JOIN plan.status status " +
            " JOIN plan.department department " +
            " JOIN plan.planFiles files" +
            " JOIN files.planFileExpenses fileExpense " +
            " JOIN fileExpense.planExpense expense " +
            " JOIN files.user user" +
            " WHERE plan.id = :planId AND " +
            " plan.createdAt = (SELECT MAX(p.createdAt) FROM FinancialPlan p WHERE p.id = :planId) AND " +
            " plan.isDelete = false " +
            " GROUP BY plan.id, plan.name, term.id, term.name, status.id, status.code, term.planDueDate, " +
            " plan.createdAt, department.id, department.name, user.id, user.username, files.version " )
    PlanDetailResult getFinancialPlanById(@Param("planId") Long planId);
}
