package com.example.capstone_project.service;

import com.example.capstone_project.controller.body.user.deactive.DeactiveUserBody;
import com.example.capstone_project.controller.body.user.activate.ActivateUserBody;
import com.example.capstone_project.controller.body.user.update.UpdateUserBody;
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

    User updateUser(UpdateUserBody updateUserBody) throws Exception;

    void activateUser(ActivateUserBody activateUserBody);
    void deactivateUser(DeactiveUserBody deactiveUserBody);
}
