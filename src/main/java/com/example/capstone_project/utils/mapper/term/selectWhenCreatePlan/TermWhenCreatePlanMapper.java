package com.example.capstone_project.utils.mapper.term.selectWhenCreatePlan;

import com.example.capstone_project.controller.responses.term.selectWhenCreatePlan.TermWhenCreatePlanResponse;
import com.example.capstone_project.entity.Term;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TermWhenCreatePlanMapper {
    @Mapping(source = "id", target = "termId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "duration", target = "duration")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    TermWhenCreatePlanResponse mapToTermWhenCreatePlanResponseMapper(Term term);
}
