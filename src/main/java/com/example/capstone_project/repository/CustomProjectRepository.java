package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Project;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomProjectRepository {

    List<Project> getProjectWithPagination(String query, Pageable pageable);
}
