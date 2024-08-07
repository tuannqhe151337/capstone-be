package com.example.capstone_project.repository.impl;

import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialPlan_;
import com.example.capstone_project.entity.FinancialReport;
import com.example.capstone_project.entity.FinancialReport_;
import com.example.capstone_project.repository.CustomFinancialReportRepository;
import com.example.capstone_project.utils.enums.PlanStatusCode;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class FinancialReportRepositoryImpl implements CustomFinancialReportRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<FinancialReport> getReportWithPagination(String query, Long termId, Long statusId, Pageable pageable) {

        // HQL query
        String hql = "SELECT report FROM FinancialReport report " +
                " LEFT JOIN report.term term " +
                " LEFT JOIN report.status status" +
                " WHERE report.name like :query AND " +
                " (:termId IS NULL OR report.term.id = :termId) AND " +
                " (:statusId IS NULL OR report.status.id = :statusId) AND " +
                " (report.isDelete = false OR report.isDelete is null) " +
                " ORDER BY ";

        // Handling sort by and sort type
        List<Sort.Order> sortOrderList = pageable.getSort().get().toList();
        for (int i = 0; i < sortOrderList.size(); i++) {
            Sort.Order order = sortOrderList.get(i);

            String sortType = order.getDirection().isAscending() ? "asc" : "desc";
            switch (order.getProperty().toLowerCase()) {
                case "name", "plan_name", "plan.name":
                    hql += "report.name " + sortType;
                    break;
                case "status", "status_id", "status.id":
                    hql += "status.id " + sortType;
                    break;
                case "term.id", "term_id", "term":
                    hql += "term.id " + sortType;
                    break;
                case "start-date", "start_date", "start_at", "createdat":
                    hql += "report.createdAt " + sortType;
                    break;
                case "updated-date", "updated_date", "updated_at","updatedat":
                    hql += "report.updatedAt " + sortType;
                    break;
                default:
                    hql += "report.id " + sortType;
            }

            if (i != sortOrderList.size() - 1) {
                hql += ", ";
            } else {
                hql += " ";
            }
        }

        // Handling join
        EntityGraph<FinancialReport> entityGraph = entityManager.createEntityGraph(FinancialReport.class);
        entityGraph.addAttributeNodes(FinancialReport_.TERM);
        entityGraph.addAttributeNodes(FinancialReport_.STATUS);

        // Run query
        return entityManager.createQuery(hql, FinancialReport.class)
                .setParameter("query", "%" + query + "%")
                .setParameter("termId", termId)
                .setParameter("statusId", statusId)
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
    }
}