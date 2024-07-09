package com.example.capstone_project.utils.mapper.annual;

import com.example.capstone_project.controller.responses.annualReport.diagram.CostTypeDiagramResponse;
import com.example.capstone_project.repository.result.CostTypeDiagramResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface CostTypeDiagramMapper {
    @Mapping(expression = "java(costTypeDiagramResult.getCostTypeId())", target = "costType.costTypeId")
    @Mapping(expression = "java(costTypeDiagramResult.getCostTypeName())", target = "costType.name")
    @Mapping(expression = "java(costTypeDiagramResult.getTotalCost())", target = "totalCost")
    CostTypeDiagramResponse mapToCostTypeDiagramResponseMapping(CostTypeDiagramResult costTypeDiagramResult);
}
