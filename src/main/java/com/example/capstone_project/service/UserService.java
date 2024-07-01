package com.example.capstone_project.service;

import com.example.capstone_project.controller.body.user.changePassword.ChangePasswordBody;
import com.example.capstone_project.controller.body.user.deactive.DeactiveUserBody;
import com.example.capstone_project.controller.body.user.activate.ActivateUserBody;
import com.example.capstone_project.controller.body.user.forgotPassword.ForgetPasswordEmailBody;
import com.example.capstone_project.controller.body.user.otp.OTPBody;
import com.example.capstone_project.controller.body.user.resetPassword.ResetPasswordBody;
import com.example.capstone_project.controller.body.user.update.UpdateUserBody;
import com.example.capstone_project.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserService {
    List<User> getAllUsers(
            String query,
            Pageable pageable
    );
    List<User> getAllUsers(
            Long roleId, Long departmentId, Long positionId,
            String query,
            Pageable pageable
    );

    void createUser(User user) throws Exception;

    User getUserById(Long userId) throws Exception;

    void updateUser(User user) throws Exception;

    void activateUser(ActivateUserBody activateUserBody);
    void deactivateUser(DeactiveUserBody deactiveUserBody);
    void changePassword(ChangePasswordBody changePasswordBody);
    void resetPassword(String authHeader, ResetPasswordBody resetPasswordBody);
    String forgetPassword(ForgetPasswordEmailBody forgetPasswordEmailBody) throws Exception;
    String otpValidate(OTPBody otp, String authHeader) throws Exception;
    long countDistinct( Long roleId, Long departmentId, Long positionId,  String query);

}
