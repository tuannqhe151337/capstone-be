package com.example.capstone_project.repository;

import com.example.capstone_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    @Query(value = "select user from User user " +
            "join user.role " +
            "join user.department " +
            "join user.position " +
            "join user.userSetting " +
            "where user.username = :username and (user.isDelete != false or user.isDelete is null)")
    Optional<User> findUserByUsername(String username);

    @Query(value = "select user from User user " +
            "join user.role " +
            "join user.department " +
            "join user.position " +
            "join user.userSetting " +
            "where user.id = :userId and (user.isDelete != false or user.isDelete is null)")
    Optional<User> findUserById(Long userId);

    @Query(value = "select distinct count(user.id) from User user " +
            "where user.username like %:query% and (user.isDelete != false or user.isDelete is null)")
    long countDistinct(String query);
}
