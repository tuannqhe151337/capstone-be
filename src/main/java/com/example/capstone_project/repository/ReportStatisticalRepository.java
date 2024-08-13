package com.example.capstone_project.repository;

import com.example.capstone_project.entity.ReportStatistical;
import com.example.capstone_project.repository.result.CostStatisticalByCurrencyResult;
import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportStatisticalRepository extends JpaRepository<ReportStatistical, Long> {
    @Query(" SELECT expense.currency.id AS currencyId, month(expense.createdAt) AS month, year(expense.createdAt) AS year, sum(expense.unitPrice*expense.amount) AS cost, max(expense.unitPrice*expense.amount) as biggestCost, plan.department.id AS departmentId, expense.costType.id AS costTypeId FROM FinancialPlanExpense expense " +
            " LEFT JOIN expense.files files " +
            " LEFT JOIN files.file file " +
            " LEFT JOIN file.plan plan " +
            " LEFT JOIN plan.term term " +
            " LEFT JOIN plan.department department " +
            " LEFT JOIN expense.status status " +
            " LEFT JOIN expense.costType costType " +
            " WHERE file.id IN (SELECT MAX(file_2.id) FROM FinancialPlanFile file_2 " +
            "                       JOIN file_2.plan plan_2 " +
            "                       JOIN plan_2.term term_2 " +
            "                       JOIN term_2.financialReports report_2 " +
            "                       WHERE report_2.id = :reportId AND " +
            "                       (report_2.isDelete = false OR report_2.isDelete is null) " +
            "                       GROUP BY plan_2.id) " +
            " AND " +
            " status.code = :code AND " +
            " (expense.isDelete = false OR expense.isDelete is null) " +
            " GROUP BY currencyId, month, year, departmentId, costTypeId")
    List<CostStatisticalByCurrencyResult> getListCostStatistical(Long reportId, ExpenseStatusCode code);
}
