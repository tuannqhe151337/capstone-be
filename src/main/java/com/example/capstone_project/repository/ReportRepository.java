package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Report;
import com.example.capstone_project.repository.result.CostTypeDiagramResult;
import com.example.capstone_project.repository.result.DepartmentDiagramResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query(value = " SELECT report.costType.id AS costTypeId, report.costType.name AS costTypeName, sum(report.totalExpense) AS totalCost FROM AnnualReport annualReport " +
            " JOIN annualReport.reports report " +
            " WHERE annualReport.year = :year AND " +
            " (report.department.id = :departmentId OR :departmentId is null) AND " +
            " annualReport.isDelete = false AND report.isDelete = false " +
            " GROUP BY costTypeId, costTypeName " +
            " ORDER BY totalCost desc LIMIT 5")
    List<CostTypeDiagramResult> getCostTypeYearDiagram(Integer year, Long departmentId);

    @Query(value = " SELECT report.department.id AS departmentId, report.department.name AS departmentName, sum(report.totalExpense) AS totalCost FROM AnnualReport annualReport " +
            " JOIN annualReport.reports report " +
            " WHERE annualReport.year = :year AND " +
            " annualReport.isDelete = false AND report.isDelete = false " +
            " GROUP BY departmentId, departmentName " +
            " ORDER BY totalCost desc LIMIT 5")
    List<DepartmentDiagramResult> getDepartmentYearDiagram(Integer year);

    List<CostTypeDiagramResult> getReportCostTypeDiagram(Long reportId, Long departmentId);

    List<DepartmentDiagramResult> getReportDepartmentDiagram(Long reportId);
}
