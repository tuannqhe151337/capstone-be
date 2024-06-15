package com.example.capstone_project.service;


import com.example.capstone_project.controller.body.user.deactive.DeactiveUserBody;
import com.example.capstone_project.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<User> getAllUsers(
            String query,
            Pageable pageable
    );

    long countDistinct(String query);

    User createUser(User user);

    User getUserById(Long userId);

    User updateUser(Long userId, User user);

    void deactivateUser(DeactiveUserBody deactiveUserBody);
}
