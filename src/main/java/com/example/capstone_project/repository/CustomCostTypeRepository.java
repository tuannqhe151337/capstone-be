package com.example.capstone_project.repository;

import com.example.capstone_project.entity.CostType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCostTypeRepository {

    List<CostType> getListCostTypePaginate(String query, Pageable pageable);
}
