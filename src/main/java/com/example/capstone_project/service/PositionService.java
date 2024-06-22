package com.example.capstone_project.service;

import com.example.capstone_project.entity.Position;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PositionService {
    List<Position> getPositions(String query, Pageable pageable);
    long countDistinct(String query);
}
