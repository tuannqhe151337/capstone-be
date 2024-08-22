package com.example.capstone_project.repository;

import com.example.capstone_project.entity.AnnualReport;
import com.example.capstone_project.repository.result.AnnualReportExpenseResult;
import com.example.capstone_project.repository.result.AnnualReportResult;
import com.example.capstone_project.repository.result.ReportResult;
import com.example.capstone_project.repository.result.CostTypeDiagramResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface AnnualReportRepository extends JpaRepository<AnnualReport, Long>, CustomAnnualReportRepository {
    @Query(value = "SELECT count( distinct(annualReport)) FROM AnnualReport annualReport " +
            " WHERE (annualReport.year = :year OR :year IS NULL) AND " +
            " annualReport.isDelete = false ")
    long countDistinctListAnnualReportPaging(String year);

    @Query(value = " SELECT year (term.finalEndTermDate) AS year, count ( distinct term.id) AS totalTerm, " +
            " count ( distinct department.id) AS totalDepartment FROM Term term " +
            " JOIN term.financialPlans plans " +
            " JOIN plans.department department " +
            " WHERE year (term.finalEndTermDate) = year (:now) AND " +
            " term.isDelete = false AND " +
            " department.isDelete = false " +
            " GROUP BY year ")
    AnnualReportResult getAnnualReport(LocalDate now);

    @Query(value = " SELECT reportStatictical.department.id AS departmentId, sum(reportStatictical.totalExpense) AS totalExpense, max(reportStatictical.biggestExpenditure) AS biggestExpense, reportStatictical.costType.id AS costTypeId FROM ReportStatistical reportStatictical " +
            " JOIN reportStatictical.report report " +
            " JOIN report.term term" +
            " WHERE year(term.finalEndTermDate) = year(:now) " +
            " GROUP BY departmentId, costTypeId ")
    List<ReportResult> generateReport(LocalDate now);

    @Query(value = " SELECT count (distinct (report.id)) FROM MonthlyReportSummary report " +
            " JOIN report.annualReport annualReport " +
            " JOIN report.costType costType " +
            " JOIN report.department department " +
            " WHERE annualReport.id = :annualReportId AND " +
            " (:departmentId IS NULL OR report.department.id = :departmentId) AND " +
            " (:costTypeId IS NULL OR report.costType.id = :costTypeId) AND " +
            " report.isDelete = false OR report.isDelete is null ")
    long countDistinctListExpenseWithPaginate(Long annualReportId, Long costTypeId, Long departmentId);

    @Query(value = " SELECT monthlyReportSummaries.costType.id AS costTypeId, monthlyReportSummaries.costType.name AS costTypeName, sum(monthlyReportSummaries.totalExpense) AS totalCost FROM AnnualReport annualReport " +
            " JOIN annualReport.monthlyReportSummaries monthlyReportSummaries " +
            " WHERE annualReport.id = :annualReportId AND " +
            " annualReport.isDelete = false AND monthlyReportSummaries.isDelete = false " +
            " GROUP BY costTypeId, costTypeName ")
    List<CostTypeDiagramResult> getAnnualReportCostTypeDiagram(Long annualReportId);

    @Query(value = " SELECT monthlyReportSummary.department.name AS department, monthlyReportSummary.totalExpense AS totalExpense, monthlyReportSummary.biggestExpenditure AS biggestExpenditure, monthlyReportSummary.costType.name AS costType FROM MonthlyReportSummary monthlyReportSummary " +
            " WHERE monthlyReportSummary.annualReport.id = :annualReportId AND " +
            " monthlyReportSummary.isDelete = false ")
    List<AnnualReportExpenseResult> getListExpenseByAnnualReportId(Long annualReportId);

    @Query(value = " SELECT annualReport.year FROM AnnualReport annualReport " +
            " WHERE annualReport.id = :annualReportId AND " +
            " annualReport.isDelete = false ")
    String getYear(Long annualReportId);

    AnnualReport findByYear(Integer year);

    @Query(value = " SELECT sum(reportStatistic.totalExpense) AS totalExpense FROM ReportStatistical reportStatistic " +
            " JOIN reportStatistic.report report " +
            " JOIN report.term term " +
            " WHERE year(term.finalEndTermDate) = :year AND " +
            " report.isDelete = false AND term.isDelete = false ")
    BigDecimal getTotalExpenseByYear(Integer year);

    @Query(value = " SELECT count( distinct term.id) FROM Term term " +
            " WHERE year(term.finalEndTermDate) = :year AND " +
            " term.isDelete = false ")
    int getTotalTermByYear(Integer year);

    @Query(value = " SELECT count( distinct plan.department.id) FROM FinancialPlan plan " +
            " JOIN plan.term term " +
            " WHERE year(term.finalEndTermDate) = :year AND " +
            " term.isDelete = false AND plan.isDelete = false ")
    int getTotalDepartmentByYear(Integer year);
}
