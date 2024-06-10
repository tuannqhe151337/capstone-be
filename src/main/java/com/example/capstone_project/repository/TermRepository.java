package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Term;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository extends JpaRepository<Term, Long> {
    //crud term


}
