package com.example.capstone_project.repository;

import com.example.capstone_project.controller.responses.annualReport.diagram.CostTypeDiagramResponse;
import com.example.capstone_project.entity.AnnualReport;
import com.example.capstone_project.repository.result.AnnualReportExpenseResult;
import com.example.capstone_project.repository.result.CostTypeDiagramResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnnualReportRepository extends JpaRepository<AnnualReport, Long>, CustomAnnualReportRepository {
    @Query(value = "SELECT count( distinct(annualReport)) FROM AnnualReport annualReport " +
            " WHERE annualReport.isDelete = false ")
    long countDistinctListAnnualReportPaging();
    @Query(value = " SELECT count (distinct (report.id)) FROM Report report " +
            " JOIN report.annualReport annualReport " +
            " JOIN report.costType costType " +
            " JOIN report.department department " +
            " WHERE annualReport.id = :annualReportId AND " +
            " (:departmentId IS NULL OR report.department.id = :departmentId) AND " +
            " (:costTypeId IS NULL OR report.costType = :costTypeId) AND " +
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
