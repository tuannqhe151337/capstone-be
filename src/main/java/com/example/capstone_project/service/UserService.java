package com.example.capstone_project.service;


import com.example.capstone_project.controller.body.user.create.CreateUserBody;
import com.example.capstone_project.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<User> getAllUsers(
            String query,
            Pageable pageable
    );

    long countDistinct(String query);

    User createUser(CreateUserBody user) throws Exception;

    User getUserById(Long userId);

    User updateUser(Long userId, User user);

    void deleteUser(Long userId);
}
