package com.example.capstone_project.repository;

import com.example.capstone_project.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomUserRepository {
    List<User> getUserWithPagination(String query, Pageable pageable);
}
