package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
    @Override
    boolean existsById(Long aLong);
}
