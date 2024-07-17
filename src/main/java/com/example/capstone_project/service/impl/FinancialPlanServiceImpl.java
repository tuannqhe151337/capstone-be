package com.example.capstone_project.service.impl;

import com.example.capstone_project.controller.responses.CustomSort;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.entity.FinancialPlanExpense;
import com.example.capstone_project.entity.FinancialPlan_;
import com.example.capstone_project.entity.UserDetail;
import com.example.capstone_project.repository.FinancialPlanExpenseRepository;
import com.example.capstone_project.repository.FinancialPlanRepository;
import com.example.capstone_project.repository.TermRepository;
import com.example.capstone_project.repository.PlanStatusRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.redis.UserDetailRepository;
import com.example.capstone_project.repository.result.PlanDetailResult;
import com.example.capstone_project.repository.result.PlanVersionResult;
import com.example.capstone_project.service.FinancialPlanService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.enums.RoleCode;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.term.InvalidDateException;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.helper.UserHelper;
import com.example.capstone_project.utils.mapper.plan.create.CreatePlanMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FinancialPlanServiceImpl implements FinancialPlanService {
    private final FinancialPlanRepository planRepository;
    private final PlanStatusRepository planStatusRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final UserDetailRepository userDetailRepository;
    private final TermRepository termRepository;
    private final FinancialPlanExpenseRepository expenseRepository;


    @Override
    public long countDistinct(String query, Long termId, Long departmentId, Long statusId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority or role
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())
                && userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
            departmentId = userDetail.getDepartmentId();
        }

        return planRepository.countDistinct(query, termId, departmentId, statusId);
    }

    @Override
    @Transactional
    public List<FinancialPlan> getPlanWithPagination(String query, Long termId, Long departmentId, Long statusId, Integer pageInt, Integer sizeInt, String sortBy, String sortType) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {
            throw new UnauthorizedException("Unauthorized to view plan");
        } else {

            // Handling pagination
            Pageable pageable = null;
            if (sortBy == null || sortBy.isEmpty()) {
                if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
                    pageable = PaginationHelper.handlingPaginationWithMultiSort(pageInt, sizeInt, List.of(
                            CustomSort.builder().sortBy(RoleCode.ACCOUNTANT.toString()).sortType("").build(),
                            CustomSort.builder().sortBy(FinancialPlan_.UPDATED_AT).sortType("desc").build(),
                            CustomSort.builder().sortBy(FinancialPlan_.ID).sortType("desc").build()
                    ));
                } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
                    // Default sort of financial staff role
                    pageable = PaginationHelper.handlingPaginationWithMultiSort(pageInt, sizeInt, List.of(
                            CustomSort.builder().sortBy(RoleCode.FINANCIAL_STAFF.toString()).sortType("").build(),
                            CustomSort.builder().sortBy(FinancialPlan_.UPDATED_AT).sortType("desc").build(),
                            CustomSort.builder().sortBy(FinancialPlan_.ID).sortType("desc").build()
                    ));

                    // Financial staff only see list-plan of their department
                    departmentId = userDetail.getDepartmentId();
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
                                    if (Objects.equals(plan.getId(), version.getPlanId())) {
                                        plan.setVersion(version.getVersion());
                                    }
                                }
                        );
                    }
            );

            return listResult;
        }
    }

    @Override
    public List<PlanStatus> getListPlanStatus() {
        // Get list authorities of user
        Set<String> authorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authorization
        if (authorities.contains(AuthorityCode.VIEW_PLAN.getValue())) {

            return planStatusRepository.findAll(Sort.by(CostType_.ID).ascending());

        } else {
            throw new UnauthorizedException("Unauthorized to view plan");
        }

    }

    @Override
    @Transactional
    public FinancialPlan creatPlan(FinancialPlan plan, Term term) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authorization
        // Check any plan of user department is existing in this term
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.IMPORT_PLAN.getValue())) {
            throw new UnauthorizedException("Unauthorized to create plan");
        }
        if (termRepository.existsPlanOfDepartmentInTerm(userDetail.getDepartmentId(), plan.getTerm().getId())) {
            throw new DuplicateKeyException("This term already have plan of department id = " + userDetail.getDepartmentId());
        }
        if (!LocalDateTime.now().isBefore(term.getPlanDueDate())) {
            throw new InvalidDateException("Plan due date of this term was expired");
        }
        return planRepository.save(plan);
    }

    public UserDetail getUserDetail() throws Exception {
        return userDetailRepository.get(UserHelper.getUserId());
    }

    @Override
    public Term getTermById(Long termId) {
        termRepository.findById(termId).orElseThrow(() ->
                new ResourceNotFoundException("Not found any term have id = " + termId));

        return termRepository.getReferenceById(termId);
    }

    @Override
    @Transactional
    public FinancialPlan deletePlan(long planId) {
        // Check authorization
        if (userAuthorityRepository.get(UserHelper.getUserId()).contains(AuthorityCode.DELETE_PLAN.getValue())) {

            FinancialPlan financialPlan = planRepository.findById(planId).orElseThrow(() ->
                    new ResourceNotFoundException("Not found any plan have id = " + planId));

            financialPlan.setDelete(true);

            planRepository.save(financialPlan);

            return financialPlan;
        } else {
            throw new UnauthorizedException("Unauthorized to delete plan");
        }
    }

    @Override
    public PlanDetailResult getPlanDetailByPlanId(Long planId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {
            // Accountant role can view all plan
            if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
                return planRepository.getFinancialPlanById(planId);

                // Financial staff can only view plan of their department
            } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
                PlanDetailResult planResult = planRepository.getFinancialPlanById(planId);

                // Check department
                if (planResult.getDepartmentId() == userDetail.getDepartmentId()) {
                    return planResult;
                } else {
                    throw new UnauthorizedException("User can't view this department because departmentId of plan not equal with departmentId of user");
                }
            } else {
                throw new UnauthorizedException("Unauthorized to view plan");
            }
        } else {
            throw new UnauthorizedException("Unauthorized to view plan");
        }
    }

    @Override
    public int getPlanVersionById(Long planId) {
        return planRepository.getPlanVersionByPlanId(planId);
    }

    @Override
    @Transactional
    public List<FinancialPlanExpense> getListExpenseWithPaginate(Long planId, String query, Long statusId, Long costTypeId, Pageable pageable) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();
        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        FinancialPlan plan = planRepository.getReferenceById(planId);
        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())) {
            // Checkout role, accountant can view all plan
            if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {

                return expenseRepository.getListExpenseWithPaginate(planId, query, statusId, costTypeId, pageable);

                // But financial staff can only view plan of their department
            } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {

                if (userDetail.getDepartmentId() == plan.getDepartment().getId()) {

                    return expenseRepository.getListExpenseWithPaginate(planId, query, statusId, costTypeId, pageable);
                } else {
                    throw new UnauthorizedException("User can't view this department because departmentId of plan not equal with departmentId of user");
                }
            }
            throw new UnauthorizedException("Unauthorized to view plan");
        } else {
            throw new UnauthorizedException("Unauthorized to view plan");
        }
    }


    @Override
    public long countDistinctListExpenseWithPaginate(String query, Long planId, Long statusId, Long costTypeId) {
        return expenseRepository.countDistinctListExpenseWithPaginate(query, planId, statusId, costTypeId);
    }
}
