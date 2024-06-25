package com.example.capstone_project.repository;

import com.example.capstone_project.entity.User;
import com.example.capstone_project.repository.result.UpdateUserDataOption;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomUserRepository {
    List<User> getUserWithPagination(String query, Pageable pageable);

    void saveUserData(User user, UpdateUserDataOption option);
}
