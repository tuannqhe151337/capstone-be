package com.example.capstone_project.repository.impl;

import com.example.capstone_project.entity.FinancialReportExpense;
import com.example.capstone_project.entity.FinancialReportExpense_;
import com.example.capstone_project.repository.CustomFinancialReportExpenseRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FinancialReportExpenseRepositoryImpl implements CustomFinancialReportExpenseRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FinancialReportExpense> getListExpenseWithPaginate(Long reportId, String query, Integer statusId, Integer costTypeId, Pageable pageable) {
        // HQL query
        String hql = " SELECT expense FROM FinancialReportExpense expense " +
                " LEFT JOIN expense.financialReport report " +
                " LEFT JOIN expense.status status " +
                " LEFT JOIN expense.costType costType " +
                " WHERE :reportId = report.id AND " +
                " expense.name like :query AND " +
                " (:costTypeId IS NULL OR costType.id = :costTypeId) AND " +
                " (:statusId IS NULL OR status.id = :statusId) AND " +
                " (expense.isDelete = false OR expense.isDelete is null) " +
                " ORDER BY ";

        // Handling sort by and sort type
        List<Sort.Order> sortOrderList = pageable.getSort().get().toList();
        for (int i = 0; i < sortOrderList.size(); i++) {
            Sort.Order order = sortOrderList.get(i);

            String sortType = order.getDirection().isAscending() ? "asc" : "desc";
            switch (order.getProperty().toLowerCase()) {
                case "name", "expense_name", "expense.name":
                    hql += "expense.name " + sortType;
                    break;
                case "status", "status_id", "status.id":
                    hql += "status.id " + sortType;
                    break;
                case "costtype.id", "costtype_id", "costtype":
                    hql += "costType.id " + sortType;
                    break;
                case "created-date", "created_date", "created_at", "createdat":
                    hql += "expense.createdAt " + sortType;
                    break;
                case "updated-date", "updated_date", "updated_at","updatedat":
                    hql += "expense.updatedAt " + sortType;
                    break;
                default:
                    hql += "expense.id " + sortType;
            }

            if (i != sortOrderList.size() - 1) {
                hql += ", ";
            } else {
                hql += " ";
            }
        }

        // Handling join
        EntityGraph<FinancialReportExpense> entityGraph = entityManager.createEntityGraph(FinancialReportExpense.class);
        entityGraph.addAttributeNodes(FinancialReportExpense_.STATUS);
        entityGraph.addAttributeNodes(FinancialReportExpense_.COST_TYPE);

        // Run query
        return entityManager.createQuery(hql, FinancialReportExpense.class)
                .setParameter("query", "%" + query + "%")
                .setParameter("reportId", reportId)
                .setParameter("costTypeId", costTypeId)
                .setParameter("statusId", statusId)
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
    }
}
