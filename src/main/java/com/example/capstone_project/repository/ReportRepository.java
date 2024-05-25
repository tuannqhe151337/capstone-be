package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
