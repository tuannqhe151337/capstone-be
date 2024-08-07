package com.example.capstone_project.repository;

import com.example.capstone_project.entity.ReportStatus;
import com.example.capstone_project.utils.enums.ReportStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportStatusRepository extends JpaRepository<ReportStatus,Long> {
    ReportStatus findByCode(ReportStatusCode reportStatusCode);
}
