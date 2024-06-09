package com.example.capstone_project.service;

import com.example.capstone_project.entity.AccessTokenClaim;
import com.example.capstone_project.entity.CostType;

import java.util.List;

public interface CostTypeService {
    List<CostType> getListCostType(AccessTokenClaim tokenClaim);
}
