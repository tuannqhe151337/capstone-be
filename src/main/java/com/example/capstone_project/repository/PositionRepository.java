package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Long> {
    List<Position> findAll();
    @Override
    boolean existsById(Long aLong);
}
