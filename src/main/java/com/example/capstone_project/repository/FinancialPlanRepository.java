package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.repository.result.PlanVersionResult;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface FinancialPlanRepository extends JpaRepository<FinancialPlan, Long>, CustomFinancialPlanRepository {

    List<FinancialPlan> findFinancialPlansByTermId(Long termId);

    @Query(value = "SELECT DISTINCT count(plan.id) FROM FinancialPlan plan " +
            " WHERE plan.name like %:query% AND " +
            " (:termId IS NULL OR plan.term.id = :termId) AND " +
            " (:departmentId IS NULL OR plan.department.id = :departmentId) AND " +
            " (:statusId IS NULL OR plan.status.id = :statusId) AND " +
            " plan.isDelete = false ")
    long countDistinct(@Param("query") String query, @Param("termId") Long termId, @Param("departmentId") Long departmentId, @Param("statusId") Long statusId);

    @Query(value = "SELECT DISTINCT file.plan.id AS planId ,count(file.plan.id) AS version FROM FinancialPlanFile file " +
            " WHERE file.plan.name LIKE %:query% AND " +
            " (:termId IS NULL OR file.plan.term.id = :termId) AND " +
            " (:departmentId IS NULL OR file.plan.department.id = :departmentId) AND " +
            " (:statusId IS NULL OR file.plan.status.id = :statusId) AND" +
            " file.isDelete = false " +
            " GROUP BY file.plan.id ")
    List<PlanVersionResult> getListPlanVersion(@Param("query") String query, @Param("termId") Long termId, @Param("departmentId") Long departmentId, @Param("statusId") Long statusId);
}
