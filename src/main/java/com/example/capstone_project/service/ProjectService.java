package com.example.capstone_project.service;

import com.example.capstone_project.entity.Project;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {
    List<Project> getListProjectPaging(String query, Pageable pageable);

    long countDistinctListProjectPaging(String query);

    void createProject(String projectName);

    void deleteProject(Long projectId);

    void updateProject(Project project);

    List<Project> getListProject();

}
