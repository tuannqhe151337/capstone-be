package com.example.capstone_project.repository;

import com.example.capstone_project.entity.AnnualReport;
import com.example.capstone_project.repository.result.AnnualReportExpenseResult;
import com.example.capstone_project.repository.result.AnnualReportResult;
import com.example.capstone_project.repository.result.ReportResult;
import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import com.example.capstone_project.repository.result.CostTypeDiagramResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AnnualReportRepository extends JpaRepository<AnnualReport, Long>, CustomAnnualReportRepository {
    @Query(value = "SELECT count( distinct(annualReport)) FROM AnnualReport annualReport " +
            " WHERE (annualReport.year = :year OR :year IS NULL) AND " +
            " annualReport.isDelete = false ")
    long countDistinctListAnnualReportPaging(String year);

//    @Query(value = " SELECT year (term.planDueDate) AS year, count (term.id) AS totalTerm, " +
//            " sum (expense.amount*expense.unitPrice) AS totalExpense, count (department.id) AS totalDepartment FROM Term term " +
//            " JOIN term.financialReports report" +
//            " JOIN report.department department " +
//            " JOIN report.reportExpenses expense " +
//            " WHERE year (term.planDueDate) = year (:now) AND " +
//            " expense.status.code = :approved AND" +
//            " term.isDelete = false AND " +
//            " expense.isDelete = false AND " +
//            " department.isDelete = false " +
//            " GROUP BY year ")
//    AnnualReportResult getAnnualReport(LocalDate now, ExpenseStatusCode approved);

//    @Query(value = " SELECT report.department.id AS departmentId, sum(expense.unitPrice*expense.amount) AS totalExpense, max(expense.unitPrice*expense.amount) AS biggestExpense, costType.id AS costTypeId FROM FinancialReportExpense expense " +
//            " JOIN expense.costType costType " +
//            " JOIN expense.financialReport report " +
//            " JOIN report.department department " +
//            " JOIN report.term term " +
//            " WHERE year(term.planDueDate) = year(:now) AND " +
//            " expense.status.code = :approved AND" +
//            " term.isDelete = false AND " +
//            " report.isDelete = false AND " +
//            " department.isDelete = false AND " +
//            " costType.isDelete = false AND " +
//            " expense.isDelete = false " +
//            " GROUP BY departmentId, costTypeId")
//    List<ReportResult> getListReports(LocalDate now, ExpenseStatusCode approved);

    @Query(value = " SELECT count (distinct (report.id)) FROM Report report " +
            " JOIN report.annualReport annualReport " +
            " JOIN report.costType costType " +
            " JOIN report.department department " +
            " WHERE annualReport.id = :annualReportId AND " +
            " (:departmentId IS NULL OR report.department.id = :departmentId) AND " +
            " (:costTypeId IS NULL OR report.costType.id = :costTypeId) AND " +
            " report.isDelete = false OR report.isDelete is null ")
    long countDistinctListExpenseWithPaginate(Long annualReportId, Long costTypeId, Long departmentId);

    @Query(value = " SELECT report.costType.id AS costTypeId, report.costType.name AS costTypeName, sum(report.totalExpense) AS totalCost FROM AnnualReport annualReport " +
            " JOIN annualReport.reports report " +
            " WHERE annualReport.id = :annualReportId AND " +
            " annualReport.isDelete = false AND report.isDelete = false " +
            " GROUP BY costTypeId, costTypeName ")
    List<CostTypeDiagramResult> getAnnualReportCostTypeDiagram(Long annualReportId);

    @Query(value = " SELECT report.department.name AS department, report.totalExpense AS totalExpense, report.biggestExpenditure AS biggestExpenditure, report.costType.name AS costType FROM Report report " +
            " WHERE report.annualReport.id = :annualReportId AND " +
            " report.isDelete = false ")
    List<AnnualReportExpenseResult> getListExpenseByAnnualReportId(Long annualReportId);

    @Query(value = " SELECT annualReport.year FROM AnnualReport annualReport " +
            " WHERE annualReport.id = :annualReportId AND " +
            " annualReport.isDelete = false ")
    String getYear(Long annualReportId);
}
