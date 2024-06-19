package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Position;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomPositionRepository {

   List<Position> getPositionWithPagination(String query, Pageable pageable) ;
}
