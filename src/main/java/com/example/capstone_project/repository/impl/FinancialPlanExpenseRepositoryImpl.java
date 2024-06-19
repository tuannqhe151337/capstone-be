package com.example.capstone_project.repository.impl;

import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.entity.FinancialPlanExpense_;
import com.example.capstone_project.entity.FinancialPlan_;
import com.example.capstone_project.repository.CustomFinancialPlanExpenseRepository;
import com.example.capstone_project.utils.enums.PlanStatusCode;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public class FinancialPlanExpenseRepositoryImpl implements CustomFinancialPlanExpenseRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    @Transactional
    public void saveListExpenses(List<FinancialPlanExpense> expenses) {
            for (FinancialPlanExpense expense : expenses) {
                entityManager.persist(expense);
            }
            entityManager.flush(); // Ensure all changes are synchronized with the database
            entityManager.clear(); // Clear the persistence context to avoid memory issues
    }

    @Override
    public List<FinancialPlanExpense> getListExpenseWithPaginate(Long planId, String query, Long statusId, Long costTypeId, Pageable pageable) {

        // HQL query
        String hql = " SELECT expense FROM FinancialPlanExpense expense " +
                " LEFT JOIN expense.files files " +
                " LEFT JOIN files.file.plan plan " +
                " LEFT JOIN expense.status status " +
                " LEFT JOIN expense.costType costType " +
                " WHERE :planId = plan.id AND " +
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
        EntityGraph<FinancialPlanExpense> entityGraph = entityManager.createEntityGraph(FinancialPlanExpense.class);
        entityGraph.addAttributeNodes(FinancialPlanExpense_.STATUS);
        entityGraph.addAttributeNodes(FinancialPlanExpense_.COST_TYPE);

        // Run query
        return entityManager.createQuery(hql, FinancialPlanExpense.class)
                .setParameter("query", "%" + query + "%")
                .setParameter("planId", planId)
                .setParameter("costTypeId", costTypeId)
                .setParameter("statusId", statusId)
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
    }
}
