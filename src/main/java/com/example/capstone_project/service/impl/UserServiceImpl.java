package com.example.capstone_project.service.impl;

import com.example.capstone_project.controller.body.user.create.CreateUserBody;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.repository.UserRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.service.UserService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.exception.PermissionDenyException;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.helper.UserHelper;
import com.example.capstone_project.utils.mapper.user.create.CreateUserBodyMapperImpl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.passay.DigestDictionaryRule.ERROR_CODE;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService mailService;


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
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id: " + userId));
    }

    @Override
    public User updateUser(Long userId, User user) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id: " + userId));

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id: " + userId));

        userRepository.deleteById(userId);
    }

    @Override
    public User createUser(CreateUserBody createUserBody) throws PermissionDenyException, MessagingException {
        //check authority
        long userId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.CREATE_NEW_USER.getValue())) {
            throw new PermissionDenyException("This account do not have right to create user");
        } else {
            //register user
            //check email exist
            String email = createUserBody.getEmail();

            Optional<User> user = userRepository.findUserByEmail(email);
            if (user.isPresent()) {
                throw new DataIntegrityViolationException("Email already exists");
            }

            //convert from userDTO => user
            User newUser = new CreateUserBodyMapperImpl().mapBodytoUser(createUserBody);

            //generate random password
            String password = generatePassayPassword();
            newUser.setPassword(this.passwordEncoder.encode(password));

            newUser.setUsername(generateUsernameFromFullName(createUserBody.getFullName()));
            userRepository.save(newUser);
            String username = "Your account username is: " + newUser.getUsername() ;

            mailService.sendEmail(newUser.getEmail(), newUser.getFullName(), newUser.getUsername(), password);

            return newUser;
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


        Long count = userRepository.getCountByName(usernameBuilder.toString());
        if (count == null) {
            return usernameBuilder.toString();
        } else {
            return usernameBuilder.toString() + (count + 1);
        }
    }


}
