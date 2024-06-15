package com.example.capstone_project.service.impl;

import com.example.capstone_project.controller.body.user.update.UpdateUserBody;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.AuthorityRepository;
import com.example.capstone_project.repository.UserRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.service.UserService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.helper.UserHelper;
import com.example.capstone_project.utils.mapper.user.edit.UpdateUserBodyToUserEntityMapperImpl;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.passay.IllegalCharacterRule.ERROR_CODE;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final EntityManager entityManager;
    private final AuthorityRepository authorityRepository;
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
    public User createUser(User user) {
        return null;
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id: " + userId));
    }

    @Transactional
    @Override
    public User updateUser(UpdateUserBody updateUserBody) {
        long userId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.EDIT_USER.getValue())) {
            throw new UnauthorizedException("Unauthorized");
        }
        User user = userRepository.findById(updateUserBody.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id: " + updateUserBody));
        //map update user body to user
        Department newDepartment = entityManager.find(Department.class, updateUserBody.getDepartment());
        user.setDepartment(newDepartment);
        Role newRole = entityManager.find(Role.class, updateUserBody.getRole());
        user.setRole(newRole);
        Position newPostion = entityManager.find(Position.class, updateUserBody.getPosition());
        user.setPosition(newPostion);

        //check full name co thay doi khong de update username
        if (!updateUserBody.getFullName().equals(user.getFullName())) {
            user.setUsername(generateUsernameFromFullName(updateUserBody.getFullName()));
        }
        user = new UpdateUserBodyToUserEntityMapperImpl().updateUserFromDto(updateUserBody, user);

        //get authority tu roleid
        List<Authority> authorities = authorityRepository.findAuthoritiesOfRole(user.getRole().getId());
        //update authories o trong redis
        saveAuthoritiesToRedis(user);
        return entityManager.merge(user);
    }

    private void saveAuthoritiesToRedis(User user) {
        List<Authority> authorities = authorityRepository.findAuthoritiesOfRole(user.getRole().getId());
        List<String> authoritiesCodes = new ArrayList<>();
        for (Authority authority : authorities) {
            authoritiesCodes.add(authority.getCode());
        }
        userAuthorityRepository.save(user.getId(), authoritiesCodes, Duration.ofMillis(ACCESS_TOKEN_EXPIRATION));
    }


    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id: " + userId));

        userRepository.deleteById(userId);
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
        Long count = userRepository.getCountByName(usernameBuilder.toString());
        if (count == null) {
            return usernameBuilder.toString();
        } else {
            return usernameBuilder.toString() + (count + 1);
        }
    }


}
