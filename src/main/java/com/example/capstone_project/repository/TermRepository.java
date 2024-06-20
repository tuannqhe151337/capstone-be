package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Term;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TermRepository extends JpaRepository<Term, Long>, CustomTermRepository {
    @Query(value = " SELECT DISTINCT count(term.id) FROM Term term " +
            " LEFT JOIN term.financialPlans plan" +
            " WHERE term.name like %:query% AND " +
            " term.status.name != :close AND " +
            " term.planDueDate >= :now AND " +
            " plan.department.id != :departmentId AND " +
            " term.isDelete = false " +
            " GROUP BY term.id ")
    long countDistinctListTermWhenCreatePlan(@Param("query") String query,
                       @Param("close") String close,
                       @Param("now") LocalDateTime now, @Param("departmentId") Long departmentId);

    //crud term


}
