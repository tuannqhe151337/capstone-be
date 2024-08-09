package com.example.capstone_project.repository.impl;

import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.entity.FinancialPlanExpense_;
import com.example.capstone_project.entity.FinancialPlan_;
import com.example.capstone_project.repository.CustomFinancialPlanExpenseRepository;
import com.example.capstone_project.repository.result.ExpenseResult;
import com.example.capstone_project.repository.result.ReportExpenseResult;
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
    public List<FinancialPlanExpense> getListExpenseWithPaginate(Long planId, String query, Long statusId, Long costTypeId, Long projectId, Long supplierId, Long picId, Pageable pageable) {

        // HQL query
        String hql = " SELECT expense FROM FinancialPlanExpense expense " +
                " LEFT JOIN expense.files files " +
                " LEFT JOIN files.file file " +
                " LEFT JOIN file.plan plan " +
                " LEFT JOIN expense.status status " +
                " LEFT JOIN expense.costType costType " +
                " LEFT JOIN expense.project project " +
                " LEFT JOIN expense.supplier supplier " +
                " LEFT JOIN expense.pic pic " +
                " WHERE :planId = plan.id AND " +
                " expense.name like :query AND " +
                " file.createdAt = (SELECT MAX(file_2.createdAt) FROM FinancialPlanFile file_2 WHERE file_2.plan.id = :planId) AND " +
                " (:costTypeId IS NULL OR costType.id = :costTypeId) AND " +
                " (:statusId IS NULL OR status.id = :statusId) AND " +
                " (:projectId IS NULL OR project.id = :projectId) AND " +
                " (:supplierId IS NULL OR supplier.id = :supplierId) AND " +
                " (:picId IS NULL OR pic.id = :picId) AND " +
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
                case "project.id", "project_id", "project":
                    hql += "project.id " + sortType;
                    break;
                case "supplier.id", "supplier_id", "supplier":
                    hql += "supplier.id " + sortType;
                    break;
                case "pic.id", "pic_id", "pic":
                    hql += "pic.id " + sortType;
                    break;
                case "created-date", "created_date", "created_at", "createdat":
                    hql += "expense.createdAt " + sortType;
                    break;
                case "updated-date", "updated_date", "updated_at", "updatedat":
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
        entityGraph.addAttributeNodes(FinancialPlanExpense_.PROJECT);
        entityGraph.addAttributeNodes(FinancialPlanExpense_.SUPPLIER);
        entityGraph.addAttributeNodes(FinancialPlanExpense_.PIC);

        // Run query
        return entityManager.createQuery(hql, FinancialPlanExpense.class)
                .setParameter("query", "%" + query + "%")
                .setParameter("planId", planId)
                .setParameter("costTypeId", costTypeId)
                .setParameter("statusId", statusId)
                .setParameter("projectId", projectId)
                .setParameter("supplierId", supplierId)
                .setParameter("picId", picId)
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
    }

    @Override
    public List<ReportExpenseResult> getListExpenseForReport(Long reportId, String query, Integer departmentId, Integer statusId, Integer costTypeId, Integer projectId, Integer supplierId, Integer picId, Pageable pageable) {
        // HQL query
        String hql = " SELECT new com.example.capstone_project.repository.result.ReportExpenseResult " +
                " (expense.id AS expenseId, expense.planExpenseKey AS expenseCode, expense.name AS expenseName, costType.id AS costTypeId ,costType.name AS costTypeName, expense.unitPrice AS unitPrice, expense.amount AS amount, expense.project.id AS projectId , expense.project.name AS projectName, " +
                " expense.supplier.id AS supplierId, expense.supplier.name AS supplierName, expense.pic.id AS picId, expense.pic.username AS picName, expense.note AS note, status.id AS statusId, cast(status.code AS string) AS statusCode ,status.name AS statusName, department.id AS departmentId, department.name AS departmentName) FROM FinancialPlanExpense expense " +
                " LEFT JOIN expense.files files " +
                " LEFT JOIN files.file file " +
                " LEFT JOIN file.plan plan " +
                " LEFT JOIN plan.department department " +
                " LEFT JOIN expense.status status " +
                " LEFT JOIN expense.costType costType " +
                " LEFT JOIN expense.project project " +
                " LEFT JOIN expense.supplier supplier " +
                " LEFT JOIN expense.pic pic " +
                " WHERE file.id IN (SELECT MAX(file_2.id) FROM FinancialPlanFile file_2 " +
                "                       JOIN file_2.plan plan_2 " +
                "                       JOIN plan_2.term term_2 " +
                "                       JOIN term_2.financialReports report_2 " +
                "                       WHERE report_2.id = :reportId AND " +
                "                       (report_2.isDelete = false OR report_2.isDelete is null)" +
                "                       GROUP BY plan_2.id)" +
                " AND " +
                " expense.name like :query AND " +
                " (:departmentId IS NULL OR department.id = :departmentId) AND " +
                " (:costTypeId IS NULL OR costType.id = :costTypeId) AND " +
                " (:statusId IS NULL OR status.id = :statusId) AND " +
                " (:projectId IS NULL OR project.id = :projectId) AND " +
                " (:supplierId IS NULL OR supplier.id = :supplierId) AND " +
                " (:picId IS NULL OR pic.id = :picId) AND " +
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
                case "department", "department_id", "department.id":
                    hql += "department.id " + sortType;
                    break;
                case "status", "status_id", "status.id":
                    hql += "status.id " + sortType;
                    break;
                case "costtype.id", "costtype_id", "costtype":
                    hql += "costType.id " + sortType;
                    break;
                case "project.id", "project_id", "project":
                    hql += "project.id " + sortType;
                    break;
                case "supplier.id", "supplier_id", "supplier":
                    hql += "supplier.id " + sortType;
                    break;
                case "pic.id", "pic_id", "pic":
                    hql += "pic.id " + sortType;
                    break;
                case "created-date", "created_date", "created_at", "createdat":
                    hql += "expense.createdAt " + sortType;
                    break;
                case "updated-date", "updated_date", "updated_at", "updatedat":
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

        // Run query
        return entityManager.createQuery(hql, ReportExpenseResult.class)
                .setParameter("query", "%" + query + "%")
                .setParameter("reportId", reportId)
                .setParameter("departmentId", departmentId)
                .setParameter("costTypeId", costTypeId)
                .setParameter("statusId", statusId)
                .setParameter("projectId", projectId)
                .setParameter("supplierId", supplierId)
                .setParameter("picId", picId)
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }
}