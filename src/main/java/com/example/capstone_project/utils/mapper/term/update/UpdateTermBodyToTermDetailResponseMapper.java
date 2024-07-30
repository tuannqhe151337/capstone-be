package com.example.capstone_project.utils.mapper.term.update;

import com.example.capstone_project.controller.body.term.update.UpdateTermBody;
import com.example.capstone_project.controller.responses.term.getTermDetail.TermDetailResponse;
import com.example.capstone_project.entity.Term;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UpdateTermBodyToTermDetailResponseMapper {

    Term mapTermBodyToTermEntity(UpdateTermBody term);
    TermDetailResponse mapTermToTermDetailResponse(Term term);
}
