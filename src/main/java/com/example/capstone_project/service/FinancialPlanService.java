package com.example.capstone_project.service;

import com.example.capstone_project.entity.FinancialPlan;
import com.example.capstone_project.repository.result.PlanDetailResult;
import com.example.capstone_project.entity.Term;
import com.example.capstone_project.entity.UserDetail;
import com.example.capstone_project.entity.PlanStatus;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface FinancialPlanService {
    long countDistinct(String query, Long termId, Long departmentId, Long statusId) throws Exception;

    List<FinancialPlan> getPlanWithPagination(String query, Long termId, Long departmentId, Long statusId, Integer page, Integer size, String sortBy, String sortType) throws Exception;

    FinancialPlan creatPlan(FinancialPlan plan, Term term) throws Exception;

    UserDetail getUserDetail() throws Exception;

    Term getTermById(Long termId);

    List<PlanStatus> getListPlanStatus();

    FinancialPlan deletePlan(long planId);

    PlanDetailResult getPlanDetailByPlanId(Long planId) throws Exception;

    int getPlanVersionById(Long planId);

    byte[] getBodyFileExcelXLS(Long fileId) throws Exception;

    byte[] getBodyFileExcelXLSX(Long fileId) throws Exception;

    String generateXLSXFileName(Long fileId);

    String generateXLSFileName(Long fileId);

    byte[] getLastVersionBodyFileExcelXLS(Long planId) throws Exception;

    String generateXLSFileNameByPlanId(Long planId);

    byte[] getLastVersionBodyFileExcelXLSX(Long planId) throws Exception;

    String generateXLSXFileNameByPlanId(Long planId);

    void submitPlanForReview(Long planId) throws Exception;
}
