package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TermRepository extends JpaRepository<Term, Long>, CustomTermRepository {

    @Query(" SELECT CASE " +
                " WHEN EXISTS (SELECT 1 FROM Term term " +
                            " JOIN term.financialPlans plan " +
                            " WHERE term.id = :termId AND " +
                            " plan.department.id = :departmentId) " +
                " THEN true " +
                " ELSE false " +
            " END ")
    boolean existsPlanOfDepartmentInTerm(long departmentId, Long termId);

    @Query(value = "SELECT DISTINCT count(term.id) FROM Term term " +
            " WHERE term.name like %:query% AND " +
            " term.status.name != :close AND " +
            " term.planDueDate >= :now AND " +
            " term.isDelete = false ")
    long countDistinctListTermWhenCreatePlan(@Param("query") String query,
                       @Param("close") String close,
                       @Param("now") LocalDateTime now);

    //crud term


}
