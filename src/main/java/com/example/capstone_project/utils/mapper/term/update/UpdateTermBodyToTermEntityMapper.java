package com.example.capstone_project.utils.mapper.term.update;

import com.example.capstone_project.controller.body.term.update.UpdateTermBody;
import com.example.capstone_project.controller.responses.term.getTermDetail.TermDetailResponse;
import com.example.capstone_project.entity.Term;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UpdateTermBodyToTermEntityMapper {

    Term mapUpdateTermBodyToTermEntity(UpdateTermBody updateTermBody);

}
