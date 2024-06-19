package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.Position;
import com.example.capstone_project.repository.PositionRepository;
import com.example.capstone_project.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;

    @Override
    public List<Position> getPositions(String query, Pageable pageable) {
        return positionRepository.getPositionWithPagination(query, pageable);
    }

    @Override
    public long countDistinct(String query) {
        return positionRepository.countDistinct(query);
    }
}
