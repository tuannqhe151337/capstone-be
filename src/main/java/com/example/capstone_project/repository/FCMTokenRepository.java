package com.example.capstone_project.repository;

import com.example.capstone_project.entity.FCMToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FCMTokenRepository extends JpaRepository<FCMToken, Long> {
    boolean existsByToken(String token);

    void deleteByToken(String token);
}
