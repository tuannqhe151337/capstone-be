package com.example.capstone_project.repository.impl;

import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialPlanFile;
import com.example.capstone_project.entity.FinancialPlanFile_;
import com.example.capstone_project.entity.FinancialPlan_;
import com.example.capstone_project.repository.CustomFinancialPlanRepository;
import com.example.capstone_project.repository.result.VersionResult;
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
    public List<FinancialPlan> getPlanWithPagination(String query, Long termId, Long departmentId, Pageable pageable) {

        // HQL query
        String hql = "SELECT plan FROM FinancialPlan plan " +
                " LEFT JOIN plan.term " +
                " LEFT JOIN plan.department " +
                " LEFT JOIN plan.planFiles " +
                " WHERE plan.name like :query AND " +
                " (:termId IS NULL OR plan.term.id = :termId) AND " +
                " (:departmentId IS NULL OR plan.department.id = :departmentId) AND " +
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
                default:
                    hql += "plan.id " + sortType;
            }

            if (i != sortOrderList.size() - 1) {
                hql += ", ";
            } else {
                hql += " ";
            }
        }
        System.out.println("BUG___Plan");
        System.out.println(hql);
        // Handling join
        EntityGraph<FinancialPlan> entityGraph = entityManager.createEntityGraph(FinancialPlan.class);
        entityGraph.addAttributeNodes(FinancialPlan_.TERM);
        entityGraph.addAttributeNodes(FinancialPlan_.DEPARTMENT);
        entityGraph.addAttributeNodes(FinancialPlan_.PLAN_FILES);

        // Run query
        return entityManager.createQuery(hql, FinancialPlan.class)
                .setParameter("query", "%" + query + "%")
                .setParameter("termId", termId)
                .setParameter("departmentId", departmentId)
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
    }

    @Override
    public List<VersionResult> getListVersionWithPaginate(Long planId, Pageable pageable) {
        // HQL query
        String hql = " SELECT new com.example.capstone_project.repository.result.VersionResult(file.id AS fileId, (RANK() OVER(PARTITION BY file.plan.id ORDER BY file.createdAt ASC)), file.createdAt AS createdAt, user.id AS userId, user.username AS username) FROM FinancialPlanFile file " +
                " LEFT JOIN file.user user " +
                " WHERE file.plan.id = :planId AND " +
                " file.isDelete = false " +
                " ORDER BY ";

        // Handling sort by and sort type
        List<Sort.Order> sortOrderList = pageable.getSort().get().toList();
        for (int i = 0; i < sortOrderList.size(); i++) {
            Sort.Order order = sortOrderList.get(i);

            String sortType = order.getDirection().isAscending() ? "asc" : "desc";
            switch (order.getProperty().toLowerCase()) {
                case "username", "user-name", "user_name":
                    hql += "user.name " + sortType;
                    break;
                case "createdat", "created_at", "create_at", "publisheddate", "published_date", "published-date":
                    hql += "file.createdAt " + sortType;
                    break;
                case "fileid", "id", "file_id":
                    hql += "file.id " + sortType;
                    break;
                default:
                    hql += "file.createdAt " + "desc";
            }

            if (i != sortOrderList.size() - 1) {
                hql += ", ";
            } else {
                hql += " ";
            }
        }

        // Handling join
        EntityGraph<FinancialPlanFile> entityGraph = entityManager.createEntityGraph(FinancialPlanFile.class);
        entityGraph.addAttributeNodes(FinancialPlanFile_.USER);

        // Run query
        return entityManager.createQuery(hql, VersionResult.class)
                .setParameter("planId", planId)
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
    }

}
