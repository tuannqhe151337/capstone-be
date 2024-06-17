package com.example.capstone_project.service.impl;

import com.example.capstone_project.controller.responses.CustomSort;
import com.example.capstone_project.entity.AccessTokenClaim;
import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialPlan_;
import com.example.capstone_project.repository.FinancialPlanRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.result.PlanVersionResult;
import com.example.capstone_project.service.FinancialPlanService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.enums.RoleCode;
import com.example.capstone_project.utils.helper.JwtHelper;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.helper.UserHelper;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FinancialPlanServiceImpl implements FinancialPlanService {
    private final FinancialPlanRepository planRepository;
    private final UserAuthorityRepository userAuthorityRepository;

    @Override
    public long countDistinct(String query, Long termId, Long departmentId, Long statusId) {
        return planRepository.countDistinct(query, termId, departmentId, statusId);
    }

    @Override
    @Transactional
    public List<FinancialPlan> getPlanWithPagination(String query, Long termId, Long departmentId, Long statusId, Integer pageInt, Integer sizeInt, String sortBy, String sortType, AccessTokenClaim tokenClaim) {

        // Check authority
        if (userAuthorityRepository.get(tokenClaim.getUserId()).contains(AuthorityCode.VIEW_PLAN.getValue())) {

            // Handling pagination
            Pageable pageable = null;
            if (sortBy == null || sortBy.isEmpty()) {
                if (tokenClaim.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
                    pageable = PaginationHelper.handlingPaginationWithMultiSort(pageInt, sizeInt, List.of(
                            CustomSort.builder().sortBy(RoleCode.ACCOUNTANT.toString()).sortType("").build(),
                            CustomSort.builder().sortBy(FinancialPlan_.UPDATED_AT).sortType("desc").build(),
                            CustomSort.builder().sortBy(FinancialPlan_.ID).sortType("desc").build()
                    ));
                } else if (tokenClaim.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
                    // Default sort of financial staff role
                    pageable = PaginationHelper.handlingPaginationWithMultiSort(pageInt, sizeInt, List.of(
                            CustomSort.builder().sortBy(RoleCode.FINANCIAL_STAFF.toString()).sortType("").build(),
                            CustomSort.builder().sortBy(FinancialPlan_.UPDATED_AT).sortType("desc").build(),
                            CustomSort.builder().sortBy(FinancialPlan_.ID).sortType("desc").build()
                    ));

                    // Financial staff only see list-plan of their department
                    departmentId = tokenClaim.getDepartmentId();
                }
            } else {
                // Sort by request
                pageable = PaginationHelper.handlingPaginationWithMultiSort(pageInt, sizeInt, List.of(
                        CustomSort.builder().sortBy(sortBy).sortType(sortType).build(),
                        CustomSort.builder().sortBy(FinancialPlan_.ID).sortType("desc").build()
                ));
            }

            List<FinancialPlan> listResult = planRepository.getPlanWithPagination(query, termId, departmentId, statusId, pageable);
            List<PlanVersionResult> listVersions = planRepository.getListPlanVersion(query, termId, departmentId, statusId);

            listResult.forEach(plan ->
                    {
                        listVersions.forEach(
                                version -> {
                                    if (Objects.equals(plan.getId(), version.getPlanId())){
                                        plan.setVersion(version.getVersion());
                                    }
                                }
                        );
                    }
            );

            return listResult;
        }
        return null;
    }

}
