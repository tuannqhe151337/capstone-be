package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Override
    boolean existsById(Long aLong);
}
