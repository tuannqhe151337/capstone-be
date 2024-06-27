package com.example.capstone_project.service;

import com.example.capstone_project.controller.body.user.changePassword.ChangePasswordBody;
import com.example.capstone_project.controller.body.user.deactive.DeactiveUserBody;
import com.example.capstone_project.controller.body.user.activate.ActivateUserBody;
import com.example.capstone_project.controller.body.user.resetPassword.ResetPasswordBody;
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

    void updateUser(User user) throws Exception;

    void activateUser(ActivateUserBody activateUserBody);
    void deactivateUser(DeactiveUserBody deactiveUserBody);
    void changePassword(ChangePasswordBody changePasswordBody);
    void resetPassword(ResetPasswordBody resetPasswordBody);
}
