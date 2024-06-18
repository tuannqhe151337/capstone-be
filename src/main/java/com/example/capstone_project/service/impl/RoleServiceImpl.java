package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.Role;
import com.example.capstone_project.repository.RoleRepository;
import com.example.capstone_project.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
