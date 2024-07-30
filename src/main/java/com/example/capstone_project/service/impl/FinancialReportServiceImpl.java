package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.FinancialReport;
import com.example.capstone_project.entity.FinancialReportExpense;
import com.example.capstone_project.entity.UserDetail;
import com.example.capstone_project.repository.FinancialReportExpenseRepository;
import com.example.capstone_project.repository.FinancialReportRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.redis.UserDetailRepository;
import com.example.capstone_project.repository.result.ReportDetailResult;
import com.example.capstone_project.repository.result.ExpenseResult;
import com.example.capstone_project.repository.result.FileNameResult;
import com.example.capstone_project.service.FinancialReportService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.enums.RoleCode;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.helper.HandleFileHelper;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialReportServiceImpl implements FinancialReportService {
    private final UserAuthorityRepository userAuthorityRepository;
    private final UserDetailRepository userDetailRepository;
    private final FinancialReportRepository financialReportRepository;
    private final FinancialReportExpenseRepository expenseRepository;
    private final HandleFileHelper handleFileHelper;

    @Override
    public List<FinancialReport> getListReportPaginate(String query, Long termId, Long departmentId, Long statusId, Pageable pageable) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {
            if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
                // Financial staff only see list-plan of their department
                departmentId = userDetail.getDepartmentId();
            }

            return financialReportRepository.getReportWithPagination(query, termId, departmentId, statusId, pageable);
        } else {
            throw new UnauthorizedException("Unauthorized to view report");
        }

    }

    @Override
    public long countDistinctListReportPaginate(String query, Long termId, Long departmentId, Long statusId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority or role
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {
            if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
                departmentId = userDetail.getDepartmentId();
            }
            return financialReportRepository.countDistinctListReportPaginate(query, termId, departmentId, statusId);
        } else {
            throw new UnauthorizedException("Unauthorized to create plan");
        }

    }

    @Override
    public ReportDetailResult getReportDetailByReportId(Long reportId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {
            // Accountant role can view all plan
            if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
                ReportDetailResult planResult = financialReportRepository.getFinancialReportById(reportId);
                if (planResult == null) {
                    throw new ResourceNotFoundException("Not found any report have id = " + reportId);
                }
                return planResult;
                // Financial staff can only view plan of their department
            } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
                ReportDetailResult planResult = financialReportRepository.getFinancialReportById(reportId);

                // Check department
                if (planResult.getDepartmentId() == userDetail.getDepartmentId()) {
                    return planResult;
                } else {
                    throw new UnauthorizedException("User can't view this report because departmentId of plan not equal with departmentId of user");
                }
            }
            throw new UnauthorizedException("Unauthorized to view report");
        } else {
            throw new UnauthorizedException("Unauthorized to view report");
        }
    }

    @Override
    @Transactional
    public FinancialReport deleteReport(Long reportId) {
        // Check authorization
        if (userAuthorityRepository.get(UserHelper.getUserId()).contains(AuthorityCode.DELETE_REPORT.getValue())) {
            FinancialReport financialReport = financialReportRepository.findById(reportId).orElseThrow(() ->
                    new ResourceNotFoundException("Not found any report have id = " + reportId));
            financialReport.setDelete(true);

            financialReportRepository.save(financialReport);

            return financialReport;
        } else {
            throw new ResourceNotFoundException("Unauthorized to delete report");
        }
    }

    @Override
    @Transactional
    public List<FinancialReportExpense> getListExpenseWithPaginate(Long reportId, String query, Integer statusId, Integer costTypeId, Pageable pageable) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();
        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {
            if (!financialReportRepository.existsById(reportId)) {
                throw new ResourceNotFoundException("Not found any report have id = " + reportId);
            }

            FinancialReport report = financialReportRepository.getReferenceById(reportId);

            // Checkout role, accountant can view all plan
            if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {

                return expenseRepository.getListExpenseWithPaginate(reportId, query, statusId, costTypeId, pageable);

                // But financial staff can only view plan of their department
            } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {

                if (userDetail.getDepartmentId() == report.getDepartment().getId()) {

                    return expenseRepository.getListExpenseWithPaginate(reportId, query, statusId, costTypeId, pageable);
                } else {

                    throw new UnauthorizedException("User can't view this report because departmentId of plan not equal with departmentId of user");
                }
            }
            throw new UnauthorizedException("Unauthorized to view report");
        } else {
            throw new UnauthorizedException("Unauthorized to view report");
        }
    }

    @Override
    public long countDistinctListExpenseWithPaginate(String query, Long reportId, Integer statusId, Integer costTypeId) {
        return expenseRepository.countDistinctListExpenseWithPaginate(query, reportId, statusId, costTypeId);
    }

    @Override
    public byte[] getBodyFileExcelXLSX(Long reportId) throws Exception {
        // Checkout authority and get list expenses by file id
        List<ExpenseResult> expenses = getListExpenseByReportId(reportId);

        if (expenses != null) {

            String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xlsx";
            FileInputStream file = new FileInputStream(fileLocation);
            XSSFWorkbook wb = new XSSFWorkbook(file);

            return handleFileHelper.fillDataToExcel(wb, expenses);
        }

        return null;
    }

    @Override
    public String generateXLSXFileName(Long reportId) {
        FileNameResult fileNameResult = financialReportRepository.generateFileName(reportId);
        if (fileNameResult != null) {
            return fileNameResult.getTermName() + "_" + fileNameResult.getDepartmentCode() + "_Report.xlsx";
        }
        return null;
    }

    @Override
    public byte[] getBodyFileExcelXLS(Long reportId) throws Exception {
        // Checkout authority and get list expenses by file id
        List<ExpenseResult> expenses = getListExpenseByReportId(reportId);

        if (expenses != null) {

            String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xls";
            FileInputStream file = new FileInputStream(fileLocation);
            HSSFWorkbook wb = new HSSFWorkbook(file);

            return handleFileHelper.fillDataToExcel(wb, expenses);
        }

        return null;
    }

    @Override
    public String generateXLSFileName(Long reportId) {
        FileNameResult fileNameResult = financialReportRepository.generateFileName(reportId);


        if (fileNameResult != null) {
            return fileNameResult.getTermName() + "_" + fileNameResult.getDepartmentCode() + "_Report.xls";
        }

        return null;
    }

    private List<ExpenseResult> getListExpenseByReportId(Long reportId) throws Exception {
        // Get userId from token
        long userId = UserHelper.getUserId();

        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        // Check authority
        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_REPORT.getValue())) {
            if (!financialReportRepository.existsById(reportId)) {
                throw new ResourceNotFoundException("Not found any report have id = " + reportId);
            }
            // Accountant role can view all plan
            if (userDetail.getRoleCode().equals(RoleCode.ACCOUNTANT.getValue())) {
                return financialReportRepository.getListExpenseByReportId(reportId);

                // Financial staff can only view plan of their department
            } else if (userDetail.getRoleCode().equals(RoleCode.FINANCIAL_STAFF.getValue())) {
                long departmentId = financialReportRepository.getDepartmentId(reportId);

                // Check department
                if (departmentId == userDetail.getDepartmentId()) {
                    return financialReportRepository.getListExpenseByReportId(reportId);
                } else {
                    throw new UnauthorizedException("User can't view this report because departmentId of plan not equal with departmentId of user");
                }
            }
            throw new UnauthorizedException("Unauthorized to view report");
        } else {
            throw new UnauthorizedException("Unauthorized to view report");
        }

    }
}
