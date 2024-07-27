package com.example.capstone_project.repository.impl;

import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialPlan_;
import com.example.capstone_project.repository.CustomFinancialPlanRepository;
import com.example.capstone_project.utils.enums.PlanStatusCode;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FinancialPlanRepositoryImpl implements CustomFinancialPlanRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FinancialPlan> getPlanWithPagination(String query, Long termId, Long departmentId, Long statusId, Pageable pageable, PlanStatusCode statusCode) {

        // HQL query
        String hql = "SELECT plan FROM FinancialPlan plan " +
                " LEFT JOIN plan.term " +
                " LEFT JOIN plan.department " +
                " LEFT JOIN plan.planFiles " +
                " LEFT JOIN plan.status status" +
                " WHERE plan.name like :query AND " +
                " (:termId IS NULL OR plan.term.id = :termId) AND " +
                " (:departmentId IS NULL OR plan.department.id = :departmentId) AND " +
                " (:statusId IS NULL OR plan.status.id = :statusId) AND " +
                " (:statusCode IS NULL OR status.code != :statusCode) AND " +
                " (plan.isDelete = false OR plan.isDelete is null) " +
                " ORDER BY ";

        // Handling sort by and sort type
        List<Sort.Order> sortOrderList = pageable.getSort().get().toList();
        for (int i = 0; i < sortOrderList.size(); i++) {
            Sort.Order order = sortOrderList.get(i);

            String sortType = order.getDirection().isAscending() ? "asc" : "desc";
            switch (order.getProperty().toLowerCase()) {
                case "name", "plan_name", "plan.name":
                    hql += "plan.name " + sortType;
                    break;
                case "status", "status_id", "status.id":
                    hql += "status.id " + sortType;
                    break;
                case "department.id", "department_id", "department":
                    hql += "department.id " + sortType;
                    break;
                case "term.id", "term_id", "term":
                    hql += "term.id " + sortType;
                    break;
                case "start-date", "start_date", "start_at", "createdat":
                    hql += "plan.createdAt " + sortType;
                    break;
                case "updated-date", "updated_date", "updated_at", "updatedat":
                    hql += "plan.updatedAt " + sortType;
                    break;
                case "accountant":
                    hql += "CASE status.code\n" +
                            "        WHEN '" + PlanStatusCode.WAITING_FOR_REVIEWED + "' THEN 1\n" +
                            "        WHEN '" + PlanStatusCode.REVIEWED + "' THEN 2\n" +
                            "        WHEN '" + PlanStatusCode.NEW + "' THEN 3\n" +
                            "        WHEN '" + PlanStatusCode.APPROVED + "' THEN 4\n" +
                            "        ELSE 5 \n" +
                            "    END";
                    break;
                case "financial-staff", "financial_staff", "staff":
                    hql += "CASE status.code\n" +
                            "        WHEN '" + PlanStatusCode.REVIEWED + "' THEN 1\n" +
                            "        WHEN '" + PlanStatusCode.NEW + "' THEN 2\n" +
                            "        WHEN '" + PlanStatusCode.WAITING_FOR_REVIEWED + "' THEN 3\n" +
                            "        WHEN '" + PlanStatusCode.APPROVED + "' THEN 4\n" +
                            "        ELSE 5 \n" +
                            "    END";
                    break;
                default:
                    hql += "plan.id " + sortType;
            }

            if (i != sortOrderList.size() - 1) {
                hql += ", ";
            } else {
                hql += " ";
            }
        }

        // Handling join
        EntityGraph<FinancialPlan> entityGraph = entityManager.createEntityGraph(FinancialPlan.class);
        entityGraph.addAttributeNodes(FinancialPlan_.TERM);
        entityGraph.addAttributeNodes(FinancialPlan_.DEPARTMENT);
        entityGraph.addAttributeNodes(FinancialPlan_.STATUS);
        entityGraph.addAttributeNodes(FinancialPlan_.PLAN_FILES);

        // Run query
        return entityManager.createQuery(hql, FinancialPlan.class)
                .setParameter("query", "%" + query + "%")
                .setParameter("termId", termId)
                .setParameter("departmentId", departmentId)
                .setParameter("statusId", statusId)
                .setParameter("statusCode", statusCode)
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
    }
}
