package com.example.capstone_project.service.impl;

import com.example.capstone_project.controller.body.user.update.UpdateUserBody;
import com.example.capstone_project.controller.responses.ExceptionResponse;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.redis.UserDepartmentAuthorityRepository;
import com.example.capstone_project.service.UserService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.department.InvalidDepartmentIdException;
import com.example.capstone_project.utils.exception.postion.InvalidPositiontIdException;
import com.example.capstone_project.utils.exception.role.InvalidRoleIdException;
import com.example.capstone_project.utils.helper.UserHelper;
import com.example.capstone_project.utils.mapper.user.edit.UpdateUserBodyToUserEntityMapperImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.passay.IllegalCharacterRule.ERROR_CODE;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    @PersistenceContext
    private final EntityManager entityManager;
    private final AuthorityRepository authorityRepository;
    @Value("${application.security.access-token.expiration}")
    private long ACCESS_TOKEN_EXPIRATION;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final PositionRepository positionRepository;
    private final UserDepartmentAuthorityRepository departmentAuthorityRepository;

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
    public User updateUser(UpdateUserBody updateUserBody) throws Exception{

        long userId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.EDIT_USER.getValue())) {
            throw new UnauthorizedException("Unauthorized");
        }
        User user = userRepository.findById(updateUserBody.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id: " + updateUserBody));

        //check department
        //Optional<User> user = userRepository.findUserByEmail(email);
        if (!updateUserBody.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(updateUserBody.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        //check department
        if(!departmentRepository.existsById(updateUserBody.getDepartment())){
            throw new InvalidDepartmentIdException("Department does not exist");
        }
        if(!positionRepository.existsById(updateUserBody.getPosition())){
            throw new InvalidPositiontIdException("Position does not exist");
        }
        if(!roleRepository.existsById(updateUserBody.getRole())){
            throw new InvalidRoleIdException("Role does not exist");
        }

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
        List<String> authCodes = getAuthCodes(authorities);
        //update authories o trong redis
        userAuthorityRepository.save(user.getId(), authCodes, Duration.ofMillis(ACCESS_TOKEN_EXPIRATION));
        departmentAuthorityRepository.save(user.getId().intValue(), user.getDepartment().getId().intValue(), authCodes, Duration.ofMillis(ACCESS_TOKEN_EXPIRATION));
        return entityManager.merge(user);
    }

    private List<String> getAuthCodes(List<Authority> authorityList) {
        List<String> authoritiesCodes = new ArrayList<>();
        for (Authority authority : authorityList) {
            authoritiesCodes.add(authority.getCode());
        }
        return authoritiesCodes;
    }


    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id: " + userId));

        userRepository.deleteById(userId);
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
                return usernameBuilder.toString() + (count + 1); //ví dụ GiangDV3 -> GiangDV4
            }
        } else {
            return usernameBuilder.toString();
        }


    }



}
