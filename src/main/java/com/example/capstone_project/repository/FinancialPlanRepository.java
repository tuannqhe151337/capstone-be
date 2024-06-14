package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlan;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface FinancialPlanRepository extends CrudRepository<FinancialPlan, Long>, CustomFinancialPlanRepository {

    List<FinancialPlan> findFinancialPlansByTermId(Long termId);

    @Query(value = "SELECT DISTINCT count(plan.id) FROM FinancialPlan plan " +
            " WHERE plan.name like %:query% AND " +
            " (:termId IS NULL OR plan.status.id = :termId) AND " +
            " (:departmentId IS NULL OR plan.department.id = :departmentId) AND " +
            " (:statusId IS NULL OR plan.status.id = :statusId) AND " +
            " (plan.isDelete = false OR plan.isDelete is null) ")
    long countDistinct(@Param("query") String query, @Param("termId") Long termId, @Param("departmentId") Long departmentId, @Param("statusId") Long statusId);
}
