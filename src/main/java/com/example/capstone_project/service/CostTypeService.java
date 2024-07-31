package com.example.capstone_project.service;

import com.example.capstone_project.entity.CostType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CostTypeService {
    List<CostType> getListCostType(String query, Pageable pageable);

    void createCostType(String costTypeName);

    void updateCostType(CostType costType);

    void deleteCostType(Long costTypeId);

    long countDistinctListCostType(String query);
}
