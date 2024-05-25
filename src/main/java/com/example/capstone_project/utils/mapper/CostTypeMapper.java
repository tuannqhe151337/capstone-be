package com.example.capstone_project.utils.mapper;

import com.example.capstone_project.controller.responses.CostTypeResponse;
import com.example.capstone_project.entity.CostType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CostTypeMapper {
    CostTypeResponse mapToCostTypeResponse(CostType costType);
    CostType mapToCostType(CostTypeResponse costTypeResponse);
}
