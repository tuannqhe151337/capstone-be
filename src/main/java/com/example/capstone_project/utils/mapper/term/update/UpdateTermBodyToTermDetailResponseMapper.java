package com.example.capstone_project.utils.mapper.term.update;

import com.example.capstone_project.controller.body.term.update.UpdateTermBody;
import com.example.capstone_project.controller.responses.term.getTermDetail.TermDetailResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UpdateTermBodyToTermDetailResponseMapper {

    TermDetailResponse mapDeleteTermBodyToDetail(UpdateTermBody user);
   // TermDetailResponse mapTermToTermDetail(Term term);
}
