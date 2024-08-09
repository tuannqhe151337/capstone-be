package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.Project;
import com.example.capstone_project.entity.Report_;
import com.example.capstone_project.repository.ProjectRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.service.ProjectService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final UserAuthorityRepository userAuthorityRepository;
    private final ProjectRepository projectRepository;

    @Override
    public List<Project> getListProjectPaging(String query, Pageable pageable) {
        // Get list authorities of this user
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_PROJECT.getValue())) {
            return projectRepository.getProjectWithPagination(query, pageable);
        } else {
            throw new UnauthorizedException("Unauthorized to view list project");
        }
    }

    @Override
    public long countDistinctListProjectPaging(String query) {
        // Get list authorities of this user
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_PROJECT.getValue())) {
            return projectRepository.countDistinctListProjectPaging(query);
        } else {
            throw new UnauthorizedException("Unauthorized to view list project");
        }
    }

    @Override
    public void createProject(String projectName) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authority or role
        try {
            if (listAuthorities.contains(AuthorityCode.CREATE_NEW_PROJECT.getValue())) {
                projectRepository.save(Project.builder().name(projectName).build());
            } else {
                throw new UnauthorizedException("Unauthorized to create new project");
            }
        } catch (Exception e) {

            throw new DuplicateKeyException("Duplicate name project");
        }
    }

    @Override
    public void deleteProject(Long projectId) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authority or role
        if (listAuthorities.contains(AuthorityCode.DELETE_PROJECT.getValue())) {
            if (!projectRepository.existsById(projectId)) {
                throw new ResourceNotFoundException("Not found any project have Id = " + projectId);
            }

            Project project = projectRepository.getReferenceById(projectId);

            project.setDelete(true);

            projectRepository.save(project);
        } else {
            throw new UnauthorizedException("Unauthorized to delete project");
        }
    }

    @Override
    public void updateProject(Project project) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        try {
            // Check authority or role
            if (listAuthorities.contains(AuthorityCode.UPDATE_PROJECT.getValue())) {
                if (!projectRepository.existsById(project.getId())) {
                    throw new ResourceNotFoundException("Not found any project have Id = " + project.getId());
                }

                projectRepository.save(project);
            } else {
                throw new UnauthorizedException("Unauthorized to update project");
            }
        } catch (Exception e) {

            throw new DuplicateKeyException("Duplicate name project");
        }
    }

    @Override
    public List<Project> getListProject() {
        // Get list authorities of this user
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_PROJECT.getValue())
                || listAuthorities.contains(AuthorityCode.VIEW_PLAN.getValue())
                || listAuthorities.contains(AuthorityCode.VIEW_REPORT.getValue())) {
            return projectRepository.findAll(Sort.by(Report_.ID).ascending());
        } else {
            throw new UnauthorizedException("Unauthorized to view list project");
        }
    }

}
