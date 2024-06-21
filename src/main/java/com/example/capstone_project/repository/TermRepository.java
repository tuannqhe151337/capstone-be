package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Term;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TermRepository extends JpaRepository<Term, Long> {
    @Query(" SELECT CASE " +
                " WHEN EXISTS (SELECT 1 FROM Term term " +
                            " JOIN term.financialPlans plan " +
                            " WHERE term.id = :termId AND " +
                            " plan.department.id = :departmentId) " +
                " THEN true " +
                " ELSE false " +
            " END ")
    boolean existsPlanOfDepartmentInTerm(long departmentId, Long termId);
    //crud term


}
