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

    void createUser(User user) throws Exception;

    User getUserById(Long userId) throws Exception;

    User updateUser(Long userId, User user);

    void deleteUser(Long userId);
}
