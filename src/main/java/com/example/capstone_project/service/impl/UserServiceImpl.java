package com.example.capstone_project.service.impl;

import com.example.capstone_project.controller.body.user.deactive.DeactiveUserBody;
import com.example.capstone_project.controller.body.user.activate.ActivateUserBody;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.entity.UserSetting;
import com.example.capstone_project.controller.body.user.update.UpdateUserBody;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.redis.UserDepartmentAuthorityRepository;
import com.example.capstone_project.service.UserService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.department.InvalidDepartmentIdException;
import com.example.capstone_project.utils.exception.position.InvalidPositiontIdException;
import com.example.capstone_project.utils.exception.role.InvalidRoleIdException;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.passay.*;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final UserDepartmentAuthorityRepository departmentAuthorityRepository;

    @Value("${application.security.access-token.expiration}")
    private long ACCESS_TOKEN_EXPIRATION;

    @Override
    public List<User> getAllUsers(
            String query,
            Pageable pageable) {
        long userId = UserHelper.getUserId();

        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_LIST_USERS.getValue())) {
            return userRepository.getUserWithPagination(query, pageable);
        }

        return null;
    }

    @Override
    public long countDistinct(String query) {
        return userRepository.countDistinct(query);
    }


    @Override
    public User getUserById(Long userId) throws Exception{
        long actorId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(actorId).contains(AuthorityCode.VIEW_USER_DETAILS.getValue())){
          throw new UnauthorizedException("Unauthorized to view user details");
        }
        Optional<User> user = userRepository.findUserDetailedById(userId);
        if (user.isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }else {
            return user.get();
        }
    }

    @Transactional
    @Override
    public User updateUser(UpdateUserBody updateUserBody) throws Exception{

        long userId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.EDIT_USER.getValue())) {
            throw new UnauthorizedException("Unauthorized");
        }
        User user = userRepository.findById(updateUserBody.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id: " + updateUserBody));

        // Check department
        // Optional<User> user = userRepository.findUserByEmail(email);
        if (!updateUserBody.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(updateUserBody.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists");
        }

        // Check department, position, role exists
        if(!departmentRepository.existsById(updateUserBody.getDepartment())){
            throw new InvalidDepartmentIdException("Department does not exist");
        }

        if(!positionRepository.existsById(updateUserBody.getPosition())){
            throw new InvalidPositiontIdException("Position does not exist");
        }

        if(!roleRepository.existsById(updateUserBody.getRole())){
            throw new InvalidRoleIdException("Role does not exist");
        }

//        //map update user body to user
//        Department newDepartment = entityManager.find(Department.class, updateUserBody.getDepartment());
//        user.setDepartment(newDepartment);
//        Role newRole = entityManager.find(Role.class, updateUserBody.getRole());
//        user.setRole(newRole);
//        Position newPostion = entityManager.find(Position.class, updateUserBody.getPosition());
//        user.setPosition(newPostion);
//
//
//        //check full name co thay doi khong de update username
//        if (!updateUserBody.getFullName().equals(user.getFullName())) {
//            user.setUsername(generateUsernameFromFullName(updateUserBody.getFullName()));
//        }
//
//        user = new UpdateUserBodyToUserEntityMapperImpl().updateUserFromDto(updateUserBody, user);
//
//        //get authority tu roleid
//        List<Authority> authorities = authorityRepository.findAuthoritiesOfRole(user.getRole().getId());
//        List<String> authCodes = getAuthCodes(authorities);
//        //update authories o trong redis
//        userAuthorityRepository.save(user.getId(), authCodes, Duration.ofMillis(ACCESS_TOKEN_EXPIRATION));
//        departmentAuthorityRepository.save(user.getId().intValue(), user.getDepartment().getId().intValue(), authCodes, Duration.ofMillis(ACCESS_TOKEN_EXPIRATION));
//        return entityManager.merge(user);

        return null;
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
        if (!userAuthorityRepository.get(userAdminId).contains(AuthorityCode.DEACTIVATE_USER.getValue()) ) {
            throw new UnauthorizedException("Unauthorized to deactivate user");
        }
        User user = userRepository.findById(deactiveUserBody.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + deactiveUserBody.getId()));
        user.setIsDelete(true);
        userRepository.save(user);
    }

    @Override
    public void createUser(User user) throws Exception {
        //check authority
        long userId = UserHelper.getUserId();

        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.CREATE_NEW_USER.getValue())) {
            throw new UnauthorizedException("Unauthorized to create new user");
        } else {
            //register user
            //check email exist
            String email = user.getEmail();

            //Optional<User> user = userRepository.findUserByEmail(email);
            if ( userRepository.existsByEmail(email) ) {
                throw new DataIntegrityViolationException("Email already exists");
            }
            //check department
            if(!departmentRepository.existsById(user.getDepartment().getId())){
                throw new InvalidDepartmentIdException("Department does not exist");
            }
            if(!positionRepository.existsById(user.getPosition().getId())){
                throw new InvalidPositiontIdException("Position does not exist");
            }
            if(!roleRepository.existsById(user.getRole().getId())){
                throw new InvalidRoleIdException("Role does not exist");
            }


            //generate random password
            String password = generatePassayPassword();
            user.setPassword(this.passwordEncoder.encode(password));

            user.setUsername(generateUsernameFromFullName(user.getFullName()));
            user.setIsDelete(false);

            userRepository.save(user);

            //create user setting
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

    public String generateUsernameFromFullName(String fullname) {
        String[] nameParts = fullname.trim().split("\\s+");
        // Tạo username từ tên và họ
        StringBuilder usernameBuilder = new StringBuilder();
        usernameBuilder.append(nameParts[nameParts.length - 1]); // Họ (tên cuối cùng)
        for (int i = 0; i < nameParts.length - 1; i++) {
            usernameBuilder.append(nameParts[i].toUpperCase().charAt(0)); // Lấy chữ cái đầu tiên của tên đệm
        }


        String lastestUsername = userRepository.getCountByName(usernameBuilder.toString());
        if (lastestUsername != null) {
            // Sử dụng biểu thức chính quy để tách phần số từ chuỗi
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(lastestUsername);
            Long count = 0L;
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
