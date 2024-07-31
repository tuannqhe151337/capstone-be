package com.example.capstone_project.repository;

import com.example.capstone_project.entity.CostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CostTypeRepository extends JpaRepository<CostType, Long>, CustomCostTypeRepository {
    @Query(" SELECT count(distinct (costType.id)) FROM CostType costType " +
            " WHERE costType.name like %:query% AND costType.isDelete = false ")
    long countDistinctListCostType(String query);
}
