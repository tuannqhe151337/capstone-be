package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.Department;
import com.example.capstone_project.repository.DepartmentRepository;

import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.service.DepartmentService;

import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.helper.UserHelper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final UserAuthorityRepository userAuthorityRepository;


    @Override
    public List<Department> getListDepartmentPaging(String query, Pageable pageable) {
        // Get list authorities of this user
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_PLAN.getValue())
                || listAuthorities.contains(AuthorityCode.VIEW_LIST_USERS.getValue())
                || listAuthorities.contains(AuthorityCode.CREATE_NEW_USER.getValue())
                || listAuthorities.contains(AuthorityCode.EDIT_USER.getValue())
                || listAuthorities.contains(AuthorityCode.VIEW_REPORT.getValue())
                || listAuthorities.contains(AuthorityCode.VIEW_ANNUAL_REPORT.getValue())) {
            return departmentRepository.getDepartmentWithPagination(query, pageable);
        } else {
            throw new UnauthorizedException("Unauthorized to view list department");
        }
    }

    @Override
    public long countDistinctListDepartmentPaging(String query) {
        return departmentRepository.countDistinct(query);
    }

    @Override
    public void createDepartment(String departmentName) throws Exception {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authority or role
        try {
            if (listAuthorities.contains(AuthorityCode.CREATE_NEW_DEPARTMENT.getValue())) {
                departmentRepository.save(Department.builder().name(departmentName).build());
            } else {
                throw new UnauthorizedException("Unauthorized to create new department");
            }
        } catch (Exception e) {

            throw new DuplicateKeyException("Duplicate name department");
        }
    }

    @Override
    public void deleteDepartment(Long departmentId) throws Exception {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authority or role
        if (listAuthorities.contains(AuthorityCode.DELETE_DEPARTMENT.getValue())) {
            if (!departmentRepository.existsById(departmentId)) {
                throw new ResourceNotFoundException("Not found any department have Id = " + departmentId);
            }

            departmentRepository.deleteById(departmentId);
        } else {
            throw new UnauthorizedException("Unauthorized to delete department");
        }
    }

    @Override
    public void updateDepartment(Department department) throws Exception {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        try {
            // Check authority or role
            if (listAuthorities.contains(AuthorityCode.UPDATE_DEPARTMENT.getValue())) {
                if (!departmentRepository.existsById(department.getId())) {
                    throw new ResourceNotFoundException("Not found any department have Id = " + department.getId());
                }

                departmentRepository.save(department);
            } else {
                throw new UnauthorizedException("Unauthorized to update department");
            }
        } catch (Exception e) {

            throw new DuplicateKeyException("Duplicate name department");
        }
    }


    @Override
    public long countDistinct(String query) {
        String queryPattern = "%" + query + "%";
        return departmentRepository.countDistinct(queryPattern);
    }


}