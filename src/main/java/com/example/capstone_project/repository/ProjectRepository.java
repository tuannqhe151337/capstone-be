package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project,Long>, CustomProjectRepository {
    @Query( " SELECT count (distinct project.id) FROM Project project " +
            " WHERE project.name like %:query% AND " +
            " (project.isDelete = false OR project.isDelete is null )")
    long countDistinctListProjectPaging(String query);
}
