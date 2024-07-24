package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import com.example.capstone_project.utils.enums.TermCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface FinancialPlanExpenseRepository extends JpaRepository<FinancialPlanExpense, Long>, CustomFinancialPlanExpenseRepository {
    @Query(" SELECT count(distinct expense.id) FROM FinancialPlanExpense expense " +
            " JOIN expense.files fileExpense " +
            " JOIN fileExpense.file file " +
            " JOIN file.plan plan " +
            " JOIN plan.term term " +
            " JOIN term.status status " +
            " WHERE expense.id IN (:listExpenses) AND " +
            " status.code = :inProgress AND " +
            " term.planDueDate >= :now AND " +
            " plan.id = :planId AND " +
            " file.createdAt = (SELECT MAX(file_2.createdAt) FROM FinancialPlanFile file_2 WHERE file_2.plan.id = :planId) AND " +
            " expense.isDelete = false ")
    long countTotalExpenseInPlanLastVersion(Long planId, List<Long> listExpenses, TermCode inProgress, LocalDateTime now);

    @Query(" SELECT expense FROM FinancialPlanExpense expense " +
            " JOIN expense.files fileExpense " +
            " JOIN fileExpense.file file " +
            " JOIN file.plan plan " +
            " JOIN plan.term term " +
            " JOIN term.status status " +
            " WHERE plan.id = :planId AND " +
            " status.code = :inProgress AND " +
            " term.planDueDate >= :now AND " +
            " file.createdAt = (SELECT MAX(file_2.createdAt) FROM FinancialPlanFile file_2 WHERE file_2.plan.id = :planId) AND " +
            " expense.isDelete = false ")
    List<FinancialPlanExpense> getListExpenseByPlanId(Long planId, TermCode inProgress, LocalDateTime now);

    @Query(" SELECT expense FROM FinancialPlanExpense expense " +
            " JOIN expense.files fileExpense " +
            " JOIN fileExpense.file file " +
            " JOIN file.plan plan " +
            " JOIN expense.status status " +
            " WHERE plan.id = :planId AND " +
            " status.code = :waitingForApproval AND " +
            " file.createdAt = (SELECT MAX(file_2.createdAt) FROM FinancialPlanFile file_2 WHERE file_2.plan.id = :planId) AND " +
            " expense.isDelete = false ")
    List<FinancialPlanExpense> getListExpenseNeedToCloseByPlanId(Long planId, ExpenseStatusCode waitingForApproval);
}
