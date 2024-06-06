package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FinancialPlan;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FinancialPlanRepository extends JpaRepository<FinancialPlan, Long> {

    @Query(value = "SELECT DISTINCT count(plan.id) FROM FinancialPlan plan " +
            " WHERE plan.name like %:query% AND " +
            " (:termId IS NULL OR :termId = '' OR plan.status.id = :termId) AND " +
            " (:departmentId IS NULL OR :department = '' OR plan.)" +
            " (plan.isDelete = false OR plan.isDelete is null)")
    long countDistinct(@Param("query") String query, @Param("termId") Integer termId, @Param("departmentId") Integer departmentId, @Param("statusId") Integer statusId);
}
