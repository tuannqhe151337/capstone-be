package com.example.capstone_project.repository;

import com.example.capstone_project.entity.ReportStatus;
import com.example.capstone_project.entity.TermInterval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TermIntervalRepository extends JpaRepository<TermInterval,Integer> {
 TermInterval findById(Long id);
}
