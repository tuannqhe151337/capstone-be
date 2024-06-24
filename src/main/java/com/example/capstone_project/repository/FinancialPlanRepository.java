package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.repository.result.ExpenseResult;
import com.example.capstone_project.repository.result.PlanVersionResult;
import io.lettuce.core.dynamic.annotation.Param;
import com.example.capstone_project.repository.result.PlanDetailResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FinancialPlanRepository extends JpaRepository<FinancialPlan, Long>, CustomFinancialPlanRepository {

    List<FinancialPlan> findFinancialPlansByTermId(Long termId);

    @Query(value = " SELECT DISTINCT count(plan.id) FROM FinancialPlan plan " +
            " WHERE plan.name like %:query% AND " +
            " (:termId IS NULL OR plan.term.id = :termId) AND " +
            " (:departmentId IS NULL OR plan.department.id = :departmentId) AND " +
            " (:statusId IS NULL OR plan.status.id = :statusId) AND " +
            " plan.isDelete = false ")
    long countDistinct(@Param("query") String query, @Param("termId") Long termId, @Param("departmentId") Long departmentId, @Param("statusId") Long statusId);

    @Query(value = " SELECT DISTINCT file.plan.id AS planId ,count(file.plan.id) AS version FROM FinancialPlanFile file " +
            " WHERE file.plan.name LIKE %:query% AND " +
            " (:termId IS NULL OR file.plan.term.id = :termId) AND " +
            " (:departmentId IS NULL OR file.plan.department.id = :departmentId) AND " +
            " (:statusId IS NULL OR file.plan.status.id = :statusId) AND" +
            " file.isDelete = false " +
            " GROUP BY file.plan.id ")
    List<PlanVersionResult> getListPlanVersion(@Param("query") String query, @Param("termId") Long termId, @Param("departmentId") Long departmentId, @Param("statusId") Long statusId);

    @Query( " SELECT plan.id AS planId, plan.name AS name, MAX(expense.unitPrice * expense.amount) AS biggestExpenditure, " +
            " SUM(expense.unitPrice * expense.amount) AS totalPlan, term.id AS termId, term.name AS termName, status.id AS statusId, status.name AS statusName, " +
            " status.code AS statusCode, term.planDueDate AS planDueDate, plan.createdAt AS createdAt, department.id AS departmentId, department.name AS departmentName, " +
            " user.id AS userId , user.username AS username" +
            " FROM FinancialPlan plan " +
            " JOIN plan.term term " +
            " JOIN plan.status status " +
            " JOIN plan.department department " +
            " JOIN plan.planFiles files" +
            " JOIN files.planFileExpenses fileExpense " +
            " JOIN fileExpense.planExpense expense " +
            " JOIN files.user user" +
            " WHERE plan.id = :planId AND " +
            " files.createdAt = (SELECT MAX(file_2.createdAt) FROM FinancialPlanFile file_2 WHERE file_2.plan.id = :planId) AND " +
            " plan.isDelete = false AND expense.isDelete = false " +
            " GROUP BY plan.id, plan.name, term.id, term.name, status.id, status.name, status.code, term.planDueDate, " +
            " plan.createdAt, department.id, department.name, user.id, user.username " )
    PlanDetailResult getFinancialPlanById(@Param("planId") Long planId);

    @Query(value = " SELECT DISTINCT count(file.plan.id) FROM FinancialPlanFile file " +
            " WHERE file.plan.id = :planId" +
            " GROUP BY file.plan.id ")
    int getPlanVersionByPlanId(@Param("planId") Long planId);

    @Query(value = " SELECT expenses.planExpenseKey AS expenseCode, expenses.updatedAt AS date, terms.name AS term, departments.name AS department, expenses.name AS expense, " +
            " costTypes.name AS costType, expenses.unitPrice AS unitPrice, expenses.amount AS amount, (expenses.unitPrice*expenses.amount) AS total," +
            " expenses.projectName AS projectName, expenses.supplierName AS supplierName, expenses.pic AS pic, expenses.note AS note," +
            " statuses.code AS status  FROM FinancialPlanExpense expenses " +
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
}
