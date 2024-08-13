package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.CostType;
import com.example.capstone_project.entity.CostType_;
import com.example.capstone_project.entity.Department;
import com.example.capstone_project.repository.CostTypeRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.service.CostTypeService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
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
    public List<CostType> getListCostType(String query, Pageable pageable) {
        // Get list authorities of user
        Set<String> authorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authorization
        if (authorities.contains(AuthorityCode.CREATE_NEW_COST_TYPE.getValue())
                || authorities.contains(AuthorityCode.IMPORT_PLAN.getValue())
                || authorities.contains(AuthorityCode.RE_UPLOAD_PLAN.getValue())
                || authorities.contains(AuthorityCode.VIEW_ANNUAL_REPORT.getValue())) {

            return costTypeRepository.getListCostTypePaginate(query, pageable);

        } else {
            throw new UnauthorizedException("User unauthorized");
        }
    }

    @Override
    public void createCostType(String costTypeName) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authority or role
        try {
            if (listAuthorities.contains(AuthorityCode.CREATE_NEW_COST_TYPE.getValue())) {
                costTypeRepository.save(CostType.builder().name(costTypeName).build());
            } else {
                throw new UnauthorizedException("Unauthorized to create new cost type");
            }
        } catch (Exception e) {
            throw new DuplicateKeyException("Duplicate name cost type");
        }
    }

    @Override
    public void updateCostType(CostType costType) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authority or role
        if (listAuthorities.contains(AuthorityCode.UPDATE_COST_TYPE.getValue())) {
            if (!costTypeRepository.existsById(costType.getId())) {
                throw new ResourceNotFoundException("Not found any cost type have Id = " + costType.getId());
            }
            CostType updateCostType = costTypeRepository.getReferenceById(costType.getId());

            costTypeRepository.save(updateCostType);
        } else {
            throw new UnauthorizedException("Unauthorized to update cost type");
        }
    }

    @Override
    public void deleteCostType(Long costTypeId) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authority or role
        if (listAuthorities.contains(AuthorityCode.DELETE_COST_TYPE.getValue())) {
            if (!costTypeRepository.existsById(costTypeId)) {
                throw new ResourceNotFoundException("Not found any cost type have Id = " + costTypeId);
            }
            CostType costType = costTypeRepository.getReferenceById(costTypeId);

            costType.setDelete(true);

            costTypeRepository.save(costType);
        } else {
            throw new UnauthorizedException("Unauthorized to delete cost type");
        }
    }

    @Override
    public long countDistinctListCostType(String query) {
        // Get list authorities of user
        Set<String> authorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authorization
        if (authorities.contains(AuthorityCode.CREATE_NEW_COST_TYPE.getValue())
                || authorities.contains(AuthorityCode.IMPORT_PLAN.getValue())
                || authorities.contains(AuthorityCode.RE_UPLOAD_PLAN.getValue())
                || authorities.contains(AuthorityCode.VIEW_ANNUAL_REPORT.getValue())) {

            return costTypeRepository.countDistinctListCostType(query);

        } else {
            throw new UnauthorizedException("User unauthorized");
        }
    }

    @Override
    public List<CostType> getListCostType() {
        // Get list authorities of user
        Set<String> authorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authorization
        if (authorities.contains(AuthorityCode.IMPORT_PLAN.getValue())
                || authorities.contains(AuthorityCode.RE_UPLOAD_PLAN.getValue())
                || authorities.contains(AuthorityCode.VIEW_ANNUAL_REPORT.getValue())) {

            return costTypeRepository.findAll(Sort.by(CostType_.ID).ascending());
        } else {
            throw new UnauthorizedException("User unauthorized");
        }
    }
}
