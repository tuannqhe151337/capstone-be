package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.repository.result.ExpenseResult;
import com.example.capstone_project.repository.result.PlanVersionResult;
import com.example.capstone_project.utils.enums.PlanStatusCode;
import io.lettuce.core.dynamic.annotation.Param;
import com.example.capstone_project.repository.result.PlanDetailResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FinancialPlanRepository extends JpaRepository<FinancialPlan, Long>, CustomFinancialPlanRepository {

    List<FinancialPlan> findAllByTermId(Long termId);

    @Query(value = "SELECT count(distinct (plan.id)) FROM FinancialPlan plan " +
            " WHERE plan.name like %:query% AND " +
            " (:termId IS NULL OR plan.term.id = :termId) AND " +
            " (:departmentId IS NULL OR plan.department.id = :departmentId) AND " +
            " plan.isDelete = false ")
    long countDistinct(@Param("query") String query, @Param("termId") Long termId, @Param("departmentId") Long departmentId);

    @Query(value = "SELECT file.plan.id AS planId ,count(distinct (file.plan.id)) AS version FROM FinancialPlanFile file " +
            " WHERE file.plan.name LIKE %:query% AND " +
            " (:termId IS NULL OR file.plan.term.id = :termId) AND " +
            " (:departmentId IS NULL OR file.plan.department.id = :departmentId) AND " +
            " file.isDelete = false " +
            " GROUP BY file.plan.id ")
    List<PlanVersionResult> getListPlanVersion(@Param("query") String query, @Param("termId") Long termId, @Param("departmentId") Long departmentId);

    @Query( " SELECT plan.id AS planId, plan.name AS name, MAX(expense.unitPrice * expense.amount) AS biggestExpenditure, " +
            " SUM(expense.unitPrice * expense.amount) AS totalPlan," +
            " term.id AS termId, term.name AS termName, term.startDate AS termStartDate, term.endDate AS termEndDate, term.reuploadStartDate AS termReuploadStartDate, term.reuploadEndDate AS termReuploadEndDate, term.finalEndTermDate AS termFinalEndTermDate, " +
            " plan.createdAt AS createdAt, " +
            " department.id AS departmentId, department.name AS departmentName, " +
            " user.id AS userId , user.username AS username" +
            " FROM FinancialPlan plan " +
            " LEFT JOIN plan.term term " +
            " LEFT JOIN plan.department department " +
            " LEFT JOIN plan.planFiles files" +
            " LEFT JOIN files.planFileExpenses fileExpense " +
            " LEFT JOIN fileExpense.planExpense expense " +
            " LEFT JOIN files.user user" +
            " WHERE plan.id = :planId AND " +
            " files.id = (SELECT MAX(file_2.id) FROM FinancialPlanFile file_2 WHERE file_2.plan.id = :planId) AND " +
            " (plan.isDelete = false OR plan.isDelete IS NULL) AND" +
            " (expense.isDelete = false OR expense.isDelete IS NULL) " +
            " GROUP BY plan.id, plan.name," +
            " term.id, term.name, term.startDate, term.endDate, term.reuploadStartDate, term.reuploadEndDate, term.finalEndTermDate," +
            " plan.createdAt, department.id, department.name, user.id, user.username " )
    PlanDetailResult getFinancialPlanById(@Param("planId") Long planId);

    @Query(value = " SELECT count(distinct (file.id)) FROM FinancialPlanFile file " +
            " WHERE file.plan.id = :planId AND " +
            " (file.isDelete = false OR file.isDelete is null) " )
    int getPlanVersionByPlanId(@Param("planId") Long planId);

    @Query(value = " SELECT expenses.id AS expenseId, expenses.planExpenseKey AS expenseCode, expenses.updatedAt AS date, terms.name AS termName, departments.name AS departmentName, expenses.name AS expenseName, " +
            " costTypes.name AS costTypeName, expenses.unitPrice AS unitPrice, expenses.amount AS amount, (expenses.unitPrice*expenses.amount) AS total," +
            " expenses.project.name AS projectName, expenses.supplier.name AS supplierName, expenses.pic.username AS picName, expenses.note AS note," +
            " statuses.code AS statusCode, expenses.currency.name AS currencyName  FROM FinancialPlanExpense expenses " +
            " LEFT JOIN expenses.files files " +
            " LEFT JOIN files.file file " +
            " LEFT JOIN file.plan plan " +
            " LEFT JOIN plan.term terms " +
            " LEFT JOIN plan.department departments " +
            " LEFT JOIN expenses.costType costTypes " +
            " LEFT JOIN expenses.status statuses  " +
            " WHERE file.id = :fileId AND " +
            " files.isDelete = false AND expenses.isDelete = false ")
    List<ExpenseResult> getListExpenseByFileId(@Param("fileId") Long fileId);

    @Query(value = " SELECT plan.id FROM FinancialPlan plan " +
            " JOIN plan.planFiles files " +
            " WHERE files.id = :fileId AND " +
            " plan.isDelete = false ")
    int getPlanIdByFileId(@Param("fileId") Long fileId);

    @Query(value = " SELECT plan.department.id FROM FinancialPlan plan " +
            " WHERE plan.id = :planId AND " +
            " plan.isDelete = false ")
    long getDepartmentIdByPlanId(Long planId);

    @Query(value = " SELECT DISTINCT file.plan.id AS planId ,count(file.plan.id) AS version, file.plan.term.name AS termName, file.plan.department.name AS departmentName FROM FinancialPlanFile file " +
            " WHERE file.plan.id = :planId AND " +
            " file.isDelete = false " +
            " GROUP BY file.plan.id, file.plan.term.name, file.plan.department.name ")
    PlanVersionResult getCurrentVersionByPlanId(Long planId);

    @Query(value = " SELECT expenses.id AS expenseId, expenses.planExpenseKey AS expenseCode, expenses.updatedAt AS date, terms.name AS termName, departments.name AS departmentName, expenses.name AS expenseName, " +
            " costTypes.name AS costTypeName, expenses.unitPrice AS unitPrice, expenses.amount AS amount, (expenses.unitPrice*expenses.amount) AS total," +
            " expenses.project.name AS projectName, expenses.supplier.name AS supplierName, expenses.pic.username AS picName, expenses.note AS note," +
            " statuses.code AS statusCode, expenses.currency.name AS currencyName  FROM FinancialPlanExpense expenses " +
            " LEFT JOIN expenses.files files " +
            " LEFT JOIN files.file file " +
            " LEFT JOIN file.plan plan " +
            " LEFT JOIN plan.term terms " +
            " LEFT JOIN plan.department departments " +
            " LEFT JOIN expenses.costType costTypes " +
            " LEFT JOIN expenses.status statuses  " +
            " WHERE plan.id = :planId AND " +
            " file.createdAt = (SELECT MAX(file_2.createdAt) FROM FinancialPlanFile file_2 WHERE file_2.plan.id = :planId) AND " +
            " file.isDelete = false AND expenses.isDelete = false ")
    List<ExpenseResult> getListExpenseByPlanId(Long planId);
}
