package com.example.capstone_project.repository.impl;

import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.CustomAnnualReportRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class AnnualReportRepositoryImpl implements CustomAnnualReportRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AnnualReport> getListAnnualReportPaging(Pageable pageable, String year) {
        String hql = "SELECT annualReport FROM AnnualReport annualReport " +
                " WHERE (annualReport.year = :year OR :year IS NULL) AND " +
                " annualReport.isDelete = false OR annualReport.isDelete is null " +
                " ORDER BY ";

        // Handling sort by and sort type
        List<Sort.Order> sortOrderList = pageable.getSort().get().toList();
        for (int i = 0; i < sortOrderList.size(); i++) {
            Sort.Order order = sortOrderList.get(i);

            String sortType = order.getDirection().isAscending() ? "asc" : "desc";
            switch (order.getProperty().toLowerCase()) {
                case "term", "totalterm", "annualreport.totalterm" :
                    hql += " annualReport.totalTerm " + sortType;
                    break;
                case "department", "totaldepartment", "annualreport.totaldepartment" :
                    hql += " annualReport.totalDepartment " + sortType;
                    break;
                case "expense", "totalexpense", "annualreport.totalexpense" :
                    hql += " annualReport.totalExpense " + sortType;
                    break;
                case "id", "annualreport_id", "annualreport.id" :
                    hql += " annualReport.id " + sortType;
                    break;
                default:
                    hql += " annualReport.createdAt desc";
            }
            if (i != sortOrderList.size() - 1) {
                hql += ", ";
            } else {
                hql += " ";
            }
        }

        return entityManager.createQuery(hql, AnnualReport.class)
                .setParameter("year",year)
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public List<MonthlyReportSummary> getListExpenseWithPaginate(Long annualReportId, Long costTypeId, Long departmentId, Pageable pageable) {
        String hql = " SELECT report FROM MonthlyReportSummary report " +
                " JOIN report.annualReport annualReport " +
                " JOIN report.costType costType " +
                " JOIN report.department department " +
                " WHERE annualReport.id = :annualReportId AND " +
                " (:departmentId IS NULL OR report.department.id = :departmentId) AND " +
                " (:costTypeId IS NULL OR report.costType.id = :costTypeId) AND " +
                " report.isDelete = false OR report.isDelete is null " +
                " ORDER BY ";

        // Handling sort by and sort type
        List<Sort.Order> sortOrderList = pageable.getSort().get().toList();
        for (int i = 0; i < sortOrderList.size(); i++) {
            Sort.Order order = sortOrderList.get(i);

            String sortType = order.getDirection().isAscending() ? "asc" : "desc";
            switch (order.getProperty().toLowerCase()) {
                case "total", "totalexpense", "total_expense", "report.totalExpense" :
                    hql += " report.totalExpense " + sortType;
                    break;
                case "department", "department_id", "departmentid" :
                    hql += " department.id " + sortType;
                    break;
                case "biggest", "biggestexpenditure", "biggest_expenditure" :
                    hql += " report.biggestExpenditure " + sortType;
                    break;
                case "id", "report_id", "report.id" :
                    hql += " report.id " + sortType;
                    break;
                default:
                    hql += " report.id " + sortType;
            }
            if (i != sortOrderList.size() - 1) {
                hql += ", ";
            } else {
                hql += " ";
            }
        }

        // Handling join
        EntityGraph<MonthlyReportSummary> entityGraph = entityManager.createEntityGraph(MonthlyReportSummary.class);
        entityGraph.addAttributeNodes(MonthlyReportSummary_.COST_TYPE);
        entityGraph.addAttributeNodes(MonthlyReportSummary_.DEPARTMENT);

        return entityManager.createQuery(hql, MonthlyReportSummary.class)
                .setParameter("annualReportId", annualReportId)
                .setParameter("departmentId", departmentId)
                .setParameter("costTypeId", costTypeId)
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
    }
}
