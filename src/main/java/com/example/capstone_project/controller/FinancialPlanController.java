package com.example.capstone_project.controller;


import com.example.capstone_project.controller.body.expense.ApprovalAllExpenseBody;
import com.example.capstone_project.controller.body.expense.ApprovalExpenseBody;
import com.example.capstone_project.controller.body.expense.DenyExpenseBody;
import com.example.capstone_project.controller.body.plan.create.NewPlanBody;
import com.example.capstone_project.controller.body.plan.detail.PlanDetailBody;
import com.example.capstone_project.controller.body.plan.download.PlanBody;
import com.example.capstone_project.controller.body.plan.download.PlanDownloadBody;
import com.example.capstone_project.controller.body.plan.reupload.ListReUploadExpenseBody;
import com.example.capstone_project.controller.body.plan.delete.DeletePlanBody;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.expense.list.ExpenseResponse;
import com.example.capstone_project.controller.responses.plan.StatusResponse;
import com.example.capstone_project.controller.responses.plan.detail.PlanDetailResponse;
import com.example.capstone_project.controller.responses.plan.list.PlanResponse;
import com.example.capstone_project.controller.responses.plan.version.VersionResponse;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.result.PlanDetailResult;
import com.example.capstone_project.repository.result.VersionResult;
import com.example.capstone_project.service.FinancialPlanService;
import com.example.capstone_project.utils.exception.InvalidInputException;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.term.InvalidDateException;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.helper.UserHelper;
import com.example.capstone_project.utils.mapper.plan.create.CreatePlanMapperImpl;
import com.example.capstone_project.utils.mapper.plan.detail.PlanDetailMapperImpl;
import com.example.capstone_project.utils.mapper.plan.expenses.PlanExpenseResponseMapperImpl;
import com.example.capstone_project.utils.mapper.plan.list.ListPlanResponseMapperImpl;
import com.example.capstone_project.utils.mapper.plan.reupload.ReUploadExpensesMapperImpl;
import com.example.capstone_project.utils.mapper.plan.status.PlanStatusMapperImpl;
import jakarta.validation.Valid;
import com.example.capstone_project.utils.mapper.plan.version.PlanListVersionResponseMapperImpl;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
public class FinancialPlanController {

    private final FinancialPlanService planService;

    @GetMapping("/list")
    public ResponseEntity<ListPaginationResponse<PlanResponse>> getListPlan(
            @RequestParam(required = false) Long termId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) throws Exception {

        try {
            // Handling page and pageSize
            Integer pageInt = PaginationHelper.convertPageToInteger(page);
            Integer sizeInt = PaginationHelper.convertPageSizeToInteger(size);

            // Handling query
            if (query == null) {
                query = "";
            }

            // Get data
            List<FinancialPlan> plans = planService.getPlanWithPagination(query, termId, departmentId, statusId, pageInt, sizeInt, sortBy, sortType);

            // Response
            ListPaginationResponse<PlanResponse> response = new ListPaginationResponse<>();

            long count = 0;

            if (plans != null) {
                // Count total record
                count = planService.countDistinct(query, termId, departmentId, statusId);

                for (FinancialPlan plan : plans) {
                    //mapperToPlanResponse
                    response.getData().add(new ListPlanResponseMapperImpl().mapToPlanResponseMapper(plan));
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            long numPages = PaginationHelper.calculateNumPages(count, sizeInt);

            response.setPagination(Pagination.builder()
                    .totalRecords(count)
                    .page(pageInt)
                    .limitRecordsPerPage(sizeInt)
                    .numPages(numPages)
                    .build());

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

    }

    @GetMapping("expenses")
    public ResponseEntity<ListPaginationResponse<ExpenseResponse>> getListExpense(
            @RequestParam(required = true) Long planId,
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) Long costTypeId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) throws Exception {
        try {
            // Handling page and pageSize
            Integer pageInt = PaginationHelper.convertPageToInteger(page);
            Integer sizeInt = PaginationHelper.convertPageSizeToInteger(size);

            // Handling query
            if (query == null) {
                query = "";
            }

            // Handling pagination
            Pageable pageable = PaginationHelper.handlingPagination(pageInt, sizeInt, sortBy, sortType);

            // Get data
            List<FinancialPlanExpense> expenses = planService.getListExpenseWithPaginate(planId, query, statusId, costTypeId, pageable);

            // Response
            ListPaginationResponse<ExpenseResponse> response = new ListPaginationResponse<>();

            long count = 0;

            if (expenses != null) {

                // Count total record
                count = planService.countDistinctListExpenseWithPaginate(query, planId, statusId, costTypeId);

                // Mapping to TermPaginateResponse
                expenses.forEach(expense -> response.getData().add(new PlanExpenseResponseMapperImpl().mapToExpenseResponseMapping(expense)));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            long numPages = PaginationHelper.calculateNumPages(count, sizeInt);

            response.setPagination(Pagination.builder()
                    .totalRecords(count)
                    .page(pageInt)
                    .limitRecordsPerPage(sizeInt)
                    .numPages(numPages)
                    .build());

            return ResponseEntity.ok(response);

        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<PlanDetailResponse> getPlanDetail(
            @Valid @RequestBody PlanDetailBody planDetailBody
    ) throws Exception {
        try {
            // Get data
            PlanDetailResult plan = planService.getPlanDetailByPlanId(planDetailBody.getPlanId());

            // Response
            PlanDetailResponse response;

            // Mapping to PlanDetail Response
            response = new PlanDetailMapperImpl().mapToPlanDetailResponseMapping(plan);
            response.setVersion(planService.getPlanVersionById(planDetailBody.getPlanId()));

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/download/xlsx")
    public ResponseEntity<byte[]> generateXlsxReport(
            @Valid @RequestBody PlanDownloadBody planBody
    ) throws Exception {
        try {
            /// Get data for file Excel
            byte[] report = planService.getBodyFileExcelXLSX(planBody.getFileId());
            if (report != null) {
                // Create file name for file Excel
                String outFileName = planService.generateXLSXFileName(planBody.getFileId());

                return createExcelFileResponseEntity(report, outFileName);

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/download/xls")
    public ResponseEntity<byte[]> generateXlsReport(
            @Valid @RequestBody PlanDownloadBody planBody
    ) throws Exception {

        /// Get data for file Excel
        byte[] report = planService.getBodyFileExcelXLS(planBody.getFileId());
        if (report != null) {
            // Create file name for file Excel
            String outFileName = planService.generateXLSFileName(planBody.getFileId());

            return createExcelFileResponseEntity(report, outFileName);

        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    private ResponseEntity<byte[]> createExcelFileResponseEntity(
            byte[] report, String fileName) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(report);
    }

    @GetMapping("/plan-status")
    public ResponseEntity<ListResponse<StatusResponse>> getListStatus() {
        try {
            // Get data
            List<PlanStatus> costTypes = planService.getListPlanStatus();

            // Response
            ListResponse<StatusResponse> responses = new ListResponse<>();

            if (costTypes != null) {

                // Mapping to CostTypeResponse
                responses.setData(costTypes.stream().map(status -> {
                    return new PlanStatusMapperImpl().mapToStatusResponseMapping(status);
                }).toList());
            } else {

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.ok(responses);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

    }

    @GetMapping("versions")
    public ResponseEntity<ListPaginationResponse<VersionResponse>> getListVersion(
            @RequestParam Long planId,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) throws Exception {
        try {
            // Handling page and pageSize
            Integer pageInt = PaginationHelper.convertPageToInteger(page);
            Integer sizeInt = PaginationHelper.convertPageSizeToInteger(size);

            // Handling pagination
            Pageable pageable = PaginationHelper.handlingPagination(pageInt, sizeInt, sortBy, sortType);

            // Get data
            List<VersionResult> planFiles = planService.getListVersionWithPaginate(planId, pageable);

            // Response
            ListPaginationResponse<VersionResponse> response = new ListPaginationResponse<>();

            long count = 0;

            if (planFiles != null) {

                // Count total record
                count = planService.countDistinctListPlanVersionPaging(planId);

                // Mapping to TermPaginateResponse
                planFiles.forEach(file -> response.getData().add(new PlanListVersionResponseMapperImpl().mapToPlanVersionResponseMapper(file)));

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            long numPages = PaginationHelper.calculateNumPages(count, sizeInt);

            response.setPagination(Pagination.builder()
                    .totalRecords(count)
                    .page(pageInt)
                    .limitRecordsPerPage(sizeInt)
                    .numPages(numPages)
                    .build());

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @DeleteMapping("/delete")
    private ResponseEntity<String> deletePlan(
            @Valid @RequestBody DeletePlanBody planBody, BindingResult bindingResult) {
        try {
            FinancialPlan deletedPlan = planService.deletePlan(planBody.getPlanId());

            if (deletedPlan == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.ok(null);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @PutMapping("/re-upload")
    private ResponseEntity<String> reUploadPlan(
            @Valid @RequestBody ListReUploadExpenseBody reUploadExpenseBody, BindingResult bindingResult
    ) throws Exception {
        try {
            FinancialPlan plan = planService.convertListExpenseAndMapToPlan(reUploadExpenseBody.getPlanId(), reUploadExpenseBody.getData());

            planService.reUploadPlan(plan);

            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> confirmExpenses(
            @Valid @RequestBody NewPlanBody planBody, BindingResult bindingResult) throws Exception {
        try {
            // Get user detail
            UserDetail userDetail = planService.getUserDetail();

            // Get term
            Term term = planService.getTermById(planBody.getTermId());

            // Mapping to planBody to FinancialPlan
            FinancialPlan plan = new CreatePlanMapperImpl().mapPlanBodyToPlanMapping(planBody, userDetail.getDepartmentId(), (long) UserHelper.getUserId(), term.getName());

            // Save plan
            FinancialPlan savedPlan = planService.creatPlan(plan, term);

            if (savedPlan == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (DuplicateKeyException | InvalidDateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/expense-approval")
    public ResponseEntity<String> approvalExpenses(
            @Valid @RequestBody ApprovalExpenseBody body, BindingResult bindingResult) throws Exception {
        try {

            planService.approvalExpenses(body.getPlanId(), body.getListExpenseId());

            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/expense-deny")
    public ResponseEntity<String> denyExpenses(
            @Valid @RequestBody DenyExpenseBody body, BindingResult bindingResult) throws Exception {
        try {

            planService.denyExpenses(body.getPlanId(), body.getListExpenseId());

            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/expense-approval-all")
    public ResponseEntity<String> approvalAllExpenses(
            @Valid @RequestBody ApprovalAllExpenseBody body, BindingResult bindingResult) throws Exception {
        try {

            planService.approvalAllExpenses(body.getPlanId());

            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/download/template/xlsx")
    public ResponseEntity<byte[]> downloadXlsxReportTemplate() throws Exception {
        String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xlsx";
        FileInputStream file = new FileInputStream(fileLocation);
        XSSFWorkbook wb = new XSSFWorkbook(file);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        wb.close();
        out.close();

        return createExcelFileResponseEntity(out.toByteArray(), "Financial_Planning_Template.xlsx");
    }


    @PostMapping("/download/template/xls")
    public ResponseEntity<byte[]> downloadXlsReportTemplate() throws Exception {
        String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xls";
        FileInputStream file = new FileInputStream(fileLocation);
        HSSFWorkbook wb = new HSSFWorkbook(file);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        wb.close();
        out.close();

        return createExcelFileResponseEntity(out.toByteArray(), "Financial_Planning_Template.xls");
    }

    @PostMapping("/download/last-version-xls")
    public ResponseEntity<byte[]> generateLastVersionXlsReport(
            @Valid @RequestBody PlanBody planBody
    ) throws Exception {
        try {
            /// Get data for file Excel
            byte[] report = planService.getLastVersionBodyFileExcelXLS(planBody.getPlanId());
            if (report != null) {
                // Create file name for file Excel
                String outFileName = planService.generateXLSFileNameByPlanId(planBody.getPlanId());

                return createExcelFileResponseEntity(report, outFileName);

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/download/last-version-xlsx")
    public ResponseEntity<byte[]> generateLastVersionXlsxReport(
            @Valid @RequestBody PlanBody planBody
    ) throws Exception {
        try {
            /// Get data for file Excel
            byte[] report = planService.getLastVersionBodyFileExcelXLSX(planBody.getPlanId());
            if (report != null) {
                // Create file name for file Excel
                String outFileName = planService.generateXLSXFileNameByPlanId(planBody.getPlanId());

                return createExcelFileResponseEntity(report, outFileName);

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/expense-status")
    public ResponseEntity<ListResponse<StatusResponse>> getListExpenseStatus() {
        try {
            // Get data
            List<ExpenseStatus> costTypes = planService.getListExpenseStatus();

            // Response
            ListResponse<StatusResponse> responses = new ListResponse<>();

            if (costTypes != null) {

                // Mapping to CostTypeResponse
                responses.setData(costTypes.stream().map(status -> {
                    return new PlanStatusMapperImpl().mapExpenseStatusToStatusResponseMapping(status);
                }).toList());
            } else {

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.ok(responses);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

    }
}
