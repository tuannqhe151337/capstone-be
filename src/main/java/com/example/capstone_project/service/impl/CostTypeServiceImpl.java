package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.AccessTokenClaim;
import com.example.capstone_project.entity.CostType;
import com.example.capstone_project.entity.CostType_;
import com.example.capstone_project.repository.CostTypeRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.service.CostTypeService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class CostTypeServiceImpl implements CostTypeService {

    private final CostTypeRepository costTypeRepository;
    private final UserAuthorityRepository userAuthorityRepository;

    @Override
    public List<CostType> getListCostType() {
        // Get list authorities of user
        Set<String> authorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authorization
        if (authorities.contains(AuthorityCode.IMPORT_PLAN.getValue())
                || authorities.contains(AuthorityCode.RE_UPLOAD_PLAN.getValue())
                || authorities.contains(AuthorityCode.VIEW_ANNUAL_REPORT.getValue()) ) {

            return costTypeRepository.findAll(Sort.by(CostType_.ID).ascending());

        }

        return null;
    }
}
