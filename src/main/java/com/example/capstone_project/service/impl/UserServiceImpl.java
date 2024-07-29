package com.example.capstone_project.service.impl;

import com.example.capstone_project.controller.body.user.changePassword.ChangePasswordBody;
import com.example.capstone_project.controller.body.user.deactive.DeactiveUserBody;
import com.example.capstone_project.controller.body.user.activate.ActivateUserBody;
import com.example.capstone_project.controller.body.user.forgotPassword.ForgetPasswordEmailBody;
import com.example.capstone_project.controller.body.user.otp.OTPBody;
import com.example.capstone_project.controller.body.user.resetPassword.ResetPasswordBody;
import com.example.capstone_project.controller.body.user.updateUserSetting.UpdateUserSettingBody;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.entity.UserSetting;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.impl.MailRepository;
import com.example.capstone_project.repository.redis.OTPTokenRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.redis.UserDetailRepository;
import com.example.capstone_project.repository.redis.UserIdTokenRepository;
import com.example.capstone_project.repository.result.UpdateUserDataOption;
import com.example.capstone_project.service.UserService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.department.InvalidDepartmentIdException;
import com.example.capstone_project.utils.exception.position.InvalidPositionIdException;
import com.example.capstone_project.utils.exception.role.InvalidRoleIdException;
import com.example.capstone_project.utils.helper.JwtHelper;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.passay.DigestDictionaryRule.ERROR_CODE;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.Duration;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailRepository mailRepository;
    private final UserSettingRepository userSettingRepository;
    private final RoleRepository roleRepository;
    private final PositionRepository positionRepository;
    private final AuthorityRepository authorityRepository;
    private final UserDetailRepository userDetailRepository;
    private final OTPTokenRepository otpTokenRepository;
    private final UserIdTokenRepository userIdTokenRepository;
    private final JwtHelper jwtHelper;

    @Value("${application.security.access-token.expiration}")
    private long ACCESS_TOKEN_EXPIRATION;

    @Value("${application.security.blank-token-email.expiration}")
    private String BLANK_TOKEN_EMAIL_EXPIRATION;

    @Value("${application.security.blank-token-otp.expiration}")
    private String BLANK_TOKEN_OTP_EXPIRATION;


    @Override
    public List<User> getAllUsers(
            Long roleId, Long departmentId, Long positionId, String query, Pageable pageable) {
        long userId = UserHelper.getUserId();

        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_LIST_USERS.getValue())) {
            return userRepository.getUserWithPagination(roleId, departmentId, positionId, query, pageable);
        }else{
            throw new UnauthorizedException("Unauthorized to view all users");
        }

    }

    @Override
    public long countDistinct(String query) {
        return userRepository.countDistinct(query);
    }


    @Override
    public User getUserById(Long userId) throws Exception {
        long actorId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(actorId).contains(AuthorityCode.VIEW_USER_DETAILS.getValue())) {
            throw new UnauthorizedException("Unauthorized to view user details");
        }
        Optional<User> user = userRepository.findUserDetailedById(userId);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        } else {
            return user.get();
        }
    }

    @Transactional
    @Override
    public void updateUser(User user) throws Exception {
        // Authorization: check if user has the right AuthorityCode.EDIT_USER
        long userId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.EDIT_USER.getValue())) {
            throw new UnauthorizedException("Unauthorized");
        }

        // Find user from database to track what field is changed
        User oldUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id: " + user));

        // Check email unique
        if (!oldUser.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(user.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists");
        }

        // Check department, position, role exists
        CheckDepartmentRolePositionExistsResult result = this.checkDepartmentRolePositionExists(
                user.getDepartment().getId(),
                user.getRole().getId(),
                user.getPosition().getId(),
                CheckDepartmentRolePositionExistsOption.builder()
                        .getRole(true)
                        .build()
        );

        // Check if fullName is updated so that we'll regenerate username
        if (!oldUser.getFullName().equals(user.getFullName())) {
            user.setUsername(generateUsernameFromFullName(user.getFullName()));
        }

        // Save to database
        userRepository.saveUserData(user, UpdateUserDataOption.builder()
                .ignoreFullName(oldUser.getFullName().equals(user.getFullName()))
                .ignoreUsername(oldUser.getFullName().equals(user.getFullName()))
                .ignoreEmail(oldUser.getEmail().equals(user.getEmail()))
                .ignoreDepartment(oldUser.getDepartment().getId().equals(user.getDepartment().getId()))
                .ignorePosition(oldUser.getPosition().getId().equals(user.getPosition().getId()))
                .ignoreRole(oldUser.getRole().getId().equals(user.getRole().getId()))
                .build());

        // Update user's authorities and departmentId, roleCode into redis
        List<Authority> authorities = authorityRepository.findAuthoritiesOfRole(user.getRole().getId());
        List<String> authCodes = getAuthCodes(authorities);

        userAuthorityRepository.save(user.getId(), authCodes, Duration.ofMillis(ACCESS_TOKEN_EXPIRATION));
        userDetailRepository.save(
                user.getId(),
                UserDetail.builder()
                        .roleCode(result.getRole().getCode())
                        .departmentId(user.getDepartment().getId())
                        .build(),
                Duration.ofMillis(ACCESS_TOKEN_EXPIRATION)
        );
    }

    private List<String> getAuthCodes(List<Authority> authorityList) {
        List<String> authoritiesCodes = new ArrayList<>();
        for (Authority authority : authorityList) {
            authoritiesCodes.add(authority.getCode().getValue());
        }
        return authoritiesCodes;
    }


    @Override
    public void deactivateUser(DeactiveUserBody deactiveUserBody) {
        long userAdminId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(userAdminId).contains(AuthorityCode.DEACTIVATE_USER.getValue())) {
            throw new UnauthorizedException("Unauthorized to deactivate user");
        }
        User user = userRepository.findById(deactiveUserBody.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + deactiveUserBody.getId()));
        user.setIsDelete(true);
        userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordBody changePasswordBody) {
        String oldPassword = changePasswordBody.getOldPassword();
        String newPassword = changePasswordBody.getNewPassword();
        long userId = UserHelper.getUserId();
        User user = userRepository.getReferenceById(userId);
        if (this.passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(this.passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Password does not match");
        }

    }

    @Override
    public void resetPassword(ResetPasswordBody resetPasswordBody) {

    }

    @Override
    public String otpValidate(String otp) throws Exception {
        return "";
    }

    @Override
    public String otpValidate(OTPBody otp, String authHeaderToken) throws Exception {
        //get token from redis by id from header
        if (authHeaderToken == null) {
            throw new DataIntegrityViolationException("Invalid token");
        }
        //compare otp
        //get userid
        String userId = otpTokenRepository.getUserID(authHeaderToken);

        if (userId == null) {
            throw new InvalidDataAccessResourceUsageException("Invalid token, missing user id");
        }
        //get otp
        String savedOtp = otpTokenRepository.getOtpCode(authHeaderToken, Long.parseLong(userId));
        //compare
        if (!savedOtp.equals(otp.getOtp())) {
            throw new UnauthorizedException("Invalid OTP");
        }
        //gen new token
        String newTokenForUserId = jwtHelper.genBlankTokenOtp();

        //save token with id
        userIdTokenRepository.save(newTokenForUserId, Long.parseLong(userId), Duration.ofMillis(Long.parseLong(BLANK_TOKEN_OTP_EXPIRATION)));

        //return token
        return newTokenForUserId;
    }

    @Override
    public String forgetPassword(ForgetPasswordEmailBody forgetPasswordEmailBody) throws Exception {
        //email
        String email = forgetPasswordEmailBody.getEmail();
        //get user by email
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("Email does not exist");
        }
        //generate blank token
        String token = jwtHelper.genBlankTokenEmail();

        //CHECK OTP EXIST to delete
        otpTokenRepository.deleteOtpCodeExists(user.get().getId());

        //generate otp
        String otp = String.valueOf(generateOTP());

        //save token with otp to redis  Duration.ofMillis(ACCESS_TOKEN_EXPIRATION)
        int expire = Integer.parseInt(BLANK_TOKEN_EMAIL_EXPIRATION);
        otpTokenRepository.save(user.get().getId(), token, otp, Duration.ofMillis(expire));

        //send token to email
        mailRepository.sendOTP(user.get().getEmail(), user.get().getUsername(), otp);

        //return token to front end
        return token;
    }


    private int generateOTP() {
        Random random = new Random();
        int OTP = 100000 + random.nextInt(900000);
        return OTP;
    }

    @Override
    public long countDistinct(String query, Long roleId, Long departmentId, Long positionId) {
        return userRepository.countDistinct(roleId, departmentId, positionId, query);
    }

    @Override
    public void resetPassword(String authHeader, ResetPasswordBody resetPasswordBody) {
        //get new password
        String newPassword = resetPasswordBody.getNewPassword();
        //get id from header to find that user
        String userId = userIdTokenRepository.find(authHeader);
        Optional<User> user = userRepository.findUserById(Long.parseLong(userId));
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User does not exist");
        } else {
            //update new password encoded
            user.get().setPassword(this.passwordEncoder.encode(newPassword));
        }
        userRepository.save(user.get());
    }

    @Override
    public void updateUserSetting(UpdateUserSettingBody updateUserSettingBody) {
        long userId = UserHelper.getUserId();
        User user = userRepository.getReferenceById(userId);
        UserSetting userSetting = userSettingRepository.findByUserId(userId).get();
        userSetting.setLanguage(updateUserSettingBody.getLanguage());
        userSetting.setTheme(updateUserSettingBody.getTheme());
        userSetting.setDarkMode(updateUserSettingBody.isDarkMode());
        userSetting.setUser(user);
        userSettingRepository.save(userSetting);

    }

    @Override
    public void createUser(User user) throws Exception {
        //check authority
        long userId = UserHelper.getUserId();

        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.CREATE_NEW_USER.getValue())) {
            throw new UnauthorizedException("Unauthorized to create new user");
        } else {
            // Check email exist
            String email = user.getEmail();

            if (userRepository.existsByEmail(email)) {
                throw new DataIntegrityViolationException("Email already exists");
            }

            // Check department or position or role exist
            CheckDepartmentRolePositionExistsResult result = this.checkDepartmentRolePositionExists(
                    user.getDepartment().getId(),
                    user.getRole().getId(),
                    user.getPosition().getId(),
                    CheckDepartmentRolePositionExistsOption.builder().build()
            );

            // Generate random password
            String password = generatePassayPassword();
            user.setPassword(this.passwordEncoder.encode(password));

            user.setUsername(generateUsernameFromFullName(user.getFullName()));
            user.setIsDelete(false);

            userRepository.save(user);

            // Create user setting
            UserSetting userSetting = UserSetting.builder()
                    .language("en")
                    .theme("blue")
                    .darkMode(false)
                    .user(user)
                    .build();
            userSettingRepository.save(userSetting);

            mailRepository.sendEmail(user.getEmail(), user.getFullName(), user.getUsername(), password);
        }
    }

    private CheckDepartmentRolePositionExistsResult checkDepartmentRolePositionExists(long departmentId, long roleId, long positionId, CheckDepartmentRolePositionExistsOption option)
            throws InvalidDepartmentIdException, InvalidPositionIdException, InvalidRoleIdException {
        // Result
        CheckDepartmentRolePositionExistsResult result = new CheckDepartmentRolePositionExistsResult();

        // Department
        if (option.isGetDepartment()) {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new InvalidDepartmentIdException("Department does not exist"));

            result.setDepartment(department);

        } else {
            if (!departmentRepository.existsById(departmentId)) {
                throw new InvalidDepartmentIdException("Department does not exist");
            }
        }


        // Position
        if (option.isGetPosition()) {
            Position position = positionRepository.findById(positionId)
                    .orElseThrow(() -> new InvalidPositionIdException("Position does not exist"));

            result.setPosition(position);

        } else {
            if (!positionRepository.existsById(positionId)) {
                throw new InvalidPositionIdException("Position does not exist");
            }
        }

        // Role
        if (option.isGetRole()) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new InvalidRoleIdException("Role does not exist"));

            result.setRole(role);

        } else {
            if (!roleRepository.existsById(roleId)) {
                throw new InvalidRoleIdException("Role does not exist");
            }
        }

        return result;
    }

    private String generatePassayPassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return ERROR_CODE;
            }

            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);

        return gen.generatePassword(10, splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
    }

    private String generateUsernameFromFullName(String fullname) {
        String[] nameParts = fullname.trim().split("\\s+");
        // Tạo username từ tên và họ
        StringBuilder usernameBuilder = new StringBuilder();
        usernameBuilder.append(nameParts[nameParts.length - 1]); // Họ (tên cuối cùng)
        for (int i = 0; i < nameParts.length - 1; i++) {
            usernameBuilder.append(nameParts[i].toUpperCase().charAt(0)); // Lấy chữ cái đầu tiên của tên đệm
        }


        String latestUsername = userRepository.getLatestSimilarUsername(usernameBuilder.toString());
        if (latestUsername != null) {
            // Sử dụng biểu thức chính quy để tách phần số từ chuỗi
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(latestUsername);
            long count = 0L;
            String numericPart = "";
            if (matcher.find()) {
                numericPart = matcher.group();  // vi du 14 từ GiangDV14
                count = Long.parseLong(numericPart); // 14
            }
            if (count == 0L) {
                return usernameBuilder.toString() + 2; //vi du GiangDV -> GiangDV2
            } else {
                return usernameBuilder.toString() + (count + 1);
            }
        } else {
            return usernameBuilder.toString();
        }
    }

    @Override
    public void activateUser(ActivateUserBody activateUserBody) {
        long userAdminId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(userAdminId).contains(AuthorityCode.ACTIVATE_USER.getValue())) {
            throw new UnauthorizedException("Unauthorized to activate user");
        }
        User user = userRepository.findById(activateUserBody.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + activateUserBody.getId()));
        user.setIsDelete(false);
        userRepository.save(user);
    }
}

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
class CheckDepartmentRolePositionExistsOption {
    private boolean getDepartment;
    private boolean getRole;
    private boolean getPosition;
}

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
class CheckDepartmentRolePositionExistsResult {
    private Department department;
    private Role role;
    private Position position;
}
