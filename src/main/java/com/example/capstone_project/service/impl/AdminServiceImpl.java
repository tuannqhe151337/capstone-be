package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.UserDetail;
import com.example.capstone_project.repository.DepartmentRepository;
import com.example.capstone_project.repository.PositionRepository;
import com.example.capstone_project.repository.UserRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.redis.UserDetailRepository;
import com.example.capstone_project.service.AdminService;
import com.example.capstone_project.utils.enums.RoleCode;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserDetailRepository userDetailRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final PositionRepository positionRepository;

    public long getTotalDepartment() throws Exception {
        UserDetail userDetail = this.userDetailRepository.get(UserHelper.getUserId());

        if (userDetail == null || !userDetail.getRoleCode().equals(RoleCode.ADMIN.getValue())) {
            throw new UnauthorizedException("Unauthorized");
        }

        return this.departmentRepository.countDistinct("");
    }

    public long getTotalEmployee() throws Exception {
        UserDetail userDetail = this.userDetailRepository.get(UserHelper.getUserId());

        if (userDetail == null || !userDetail.getRoleCode().equals(RoleCode.ADMIN.getValue())) {
            throw new UnauthorizedException("Unauthorized");
        }

        return this.userRepository.getTotalEmployee("");
    }

    public long getTotalPosition() throws Exception {
        UserDetail userDetail = this.userDetailRepository.get(UserHelper.getUserId());

        if (userDetail == null || !userDetail.getRoleCode().equals(RoleCode.ADMIN.getValue())) {
            throw new UnauthorizedException("Unauthorized");
        }

        return this.positionRepository.countDistinct("");
    }
}
