package com.example.capstone_project.service;

import com.example.capstone_project.controller.body.user.changePassword.ChangePasswordBody;
import com.example.capstone_project.controller.body.user.deactive.DeactiveUserBody;
import com.example.capstone_project.controller.body.user.activate.ActivateUserBody;
import com.example.capstone_project.controller.body.user.forgotPassword.ForgetPasswordEmailBody;
import com.example.capstone_project.controller.body.user.otp.OTPBody;
import com.example.capstone_project.controller.body.user.resetPassword.ResetPasswordBody;
import com.example.capstone_project.controller.body.user.updateUserSetting.UpdateUserSettingBody;
import com.example.capstone_project.controller.responses.user.diagram.UserOverTimeDiagramResponse;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.repository.result.UserDepartmentDiagramResult;
import com.example.capstone_project.repository.result.UserOverTimeDiagramResult;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<User> getAllUsers(Long roleId, Long departmentId, Long positionId, String query, Pageable pageable);

    long countDistinct(String query);

    void createUser(User user) throws Exception;

    User getUserById(Long userId) throws Exception;

    void updateUser(User user) throws Exception;

    void activateUser(ActivateUserBody activateUserBody);

    void deactivateUser(DeactiveUserBody deactiveUserBody);

    void changePassword(ChangePasswordBody changePasswordBody);

    void updateUserSetting(UpdateUserSettingBody updateUserSettingBody);

    void resetPassword(String authHeader, ResetPasswordBody resetPasswordBody);

    String forgetPassword(ForgetPasswordEmailBody forgetPasswordEmailBody) throws Exception;

    String otpValidate(OTPBody otp, String authHeader) throws Exception;

    long countDistinct(String query, Long roleId, Long departmentId, Long positionId);

    List<UserOverTimeDiagramResponse> getUserCreatedOverTimeDiagram(Integer year) throws Exception;

    List<UserDepartmentDiagramResult> getNumberUserOfDepartmentDiagram() throws Exception;

}