package com.example.capstone_project.repository;

import com.example.capstone_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserById(Long userId);

    @Query(value = "select distinct count(user.id) from User user" +
            " where user.username like %:query%")
    long countDistinct(String query);
}
