package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.Department;
import com.example.capstone_project.repository.DepartmentRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.service.DepartmentService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    DepartmentRepository departmentRepository;
    UserAuthorityRepository userAuthorityRepository;

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
            return departmentRepository.getListDepartmentWithPagination(query, pageable);
        }

        return null;
    }

    @Override
    public long countDistinctListDepartmentPaging(String query) {
        return departmentRepository.countDistinct(query);
    }
}
