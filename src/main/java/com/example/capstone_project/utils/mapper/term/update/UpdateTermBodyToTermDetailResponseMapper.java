package com.example.capstone_project.utils.mapper.term.update;

import com.example.capstone_project.controller.body.term.delete.DeleteTermBody;
import com.example.capstone_project.controller.body.term.update.UpdateTermBody;
import com.example.capstone_project.controller.responses.term.get.TermDetailResponse;
import com.example.capstone_project.controller.responses.user.create.UserDetailResponse;
import com.example.capstone_project.entity.Term;
import com.example.capstone_project.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UpdateTermBodyToTermDetailResponseMapper {

    TermDetailResponse mapDeleteTermBodyToDetail(UpdateTermBody user);
   // TermDetailResponse mapTermToTermDetail(Term term);
}
