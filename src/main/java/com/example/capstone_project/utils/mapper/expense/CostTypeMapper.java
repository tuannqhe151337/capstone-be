package com.example.capstone_project.utils.mapper.expense;

import com.example.capstone_project.controller.responses.expense.CostTypeResponse;
import com.example.capstone_project.entity.CostType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CostTypeMapper {
    @Mapping(source = "id", target = "costTypeId")
    @Mapping(source = "name", target = "name")
    CostTypeResponse mapToCostTypeResponse (CostType costType);
}
