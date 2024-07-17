package com.example.capstone_project.utils.mapper.term.paginate;

import com.example.capstone_project.controller.responses.term.paginate.TermPaginateResponse;
import com.example.capstone_project.entity.Term;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TermPaginateResponseMapper {
    @Mapping(source = "id", target = "termId")
    @Mapping(source = "status.id", target = "status.statusId")
    TermPaginateResponse mapToTermPaginateResponseMapper(Term term);
}
