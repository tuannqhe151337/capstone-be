package com.example.capstone_project.service.impl;

import com.example.capstone_project.controller.body.user.deactive.DeactiveUserBody;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.repository.UserRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.service.UserService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;

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
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + userId));
    }

    @Override
    public User updateUser(Long userId, User user) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + userId));

        return userRepository.save(user);
    }

    @Override
    public void deactivateUser(DeactiveUserBody deactiveUserBody) {
        long userAdminId = UserHelper.getUserId();
        User userAd = userRepository.findById(userAdminId)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + deactiveUserBody.getId()));

        if (!userAuthorityRepository.get(userAdminId).contains(AuthorityCode.DEACTIVATE_USER.getValue()) && !userAd.getIsDelete()) {
            throw new UnauthorizedException("Unauthorized to deactivate user");
        }
        User user = userRepository.findById(deactiveUserBody.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + deactiveUserBody.getId()));
        user.setIsDelete(true);
        userRepository.save(user);
    }
}
