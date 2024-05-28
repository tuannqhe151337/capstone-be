package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.User;
import com.example.capstone_project.repository.UserRepository;
import com.example.capstone_project.service.UserService;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public List<User> getAllUsers(
            String query,
            Pageable pageable) {
        return userRepository.getUserWithPagination(query, pageable);
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
}
