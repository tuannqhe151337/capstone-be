package com.example.capstone_project.controller;


import com.example.capstone_project.controller.body.plan.create.NewPlanBody;
import com.example.capstone_project.controller.body.plan.reupload.ListReUploadExpenseBody;
import com.example.capstone_project.controller.body.plan.delete.DeletePlanBody;
import com.example.capstone_project.controller.responses.ExceptionResponse;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.body.plan.submit.SubmitPlanBody;
import com.example.capstone_project.controller.responses.expense.list.ExpenseResponse;
import com.example.capstone_project.controller.responses.plan.StatusResponse;
import com.example.capstone_project.controller.responses.plan.detail.PlanDetailResponse;
import com.example.capstone_project.controller.responses.plan.list.PlanResponse;
import com.example.capstone_project.controller.responses.plan.version.VersionResponse;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.result.PlanDetailResult;
import com.example.capstone_project.repository.result.VersionResult;
import com.example.capstone_project.service.FinancialPlanService;
import com.example.capstone_project.service.result.ActualCostResult;
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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
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
            List<FinancialPlan> plans = planService.getPlanWithPagination(query, termId, departmentId, pageInt, sizeInt, sortBy, sortType);

            // Response
            ListPaginationResponse<PlanResponse> response = new ListPaginationResponse<>();

            long count = 0;

            if (plans != null) {
                // Count total record
                count = planService.countDistinct(query, termId, departmentId);

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
            @RequestParam(required = false) Long currencyId,
            @RequestParam(required = false) Long statusId,
            @RequestParam(required = false) Long costTypeId,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) Long picId,
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
            List<FinancialPlanExpense> expenses = planService.getListExpenseWithPaginate(planId, query, statusId, costTypeId, projectId, supplierId, picId, currencyId, pageable);

            // Response
            ListPaginationResponse<ExpenseResponse> response = new ListPaginationResponse<>();

            long count = 0;

            if (expenses != null) {

                // Count total record
                count = planService.countDistinctListExpenseWithPaginate(query, planId, statusId, costTypeId, projectId, supplierId, picId);

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
        } catch (ResourceNotFoundException | ArithmeticException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<PlanDetailResponse> getPlanDetail(
            @RequestParam(required = true) Long planId
    ) throws Exception {
        try {
            // Get data
            PlanDetailResult plan = planService.getPlanDetailByPlanId(planId);

            ActualCostResult actualCost = planService.calculateActualCostByPlanId(planId);

            BigDecimal expectedCost = planService.calculateExpectedCostByPlanId(planId);

            // Response
            PlanDetailResponse response;

            // Mapping to PlanDetail Response
            response = new PlanDetailMapperImpl().mapToPlanDetailResponseMapping(plan);
            response.setVersion(planService.getPlanVersionById(planId));

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/download-xlsx")
    public ResponseEntity<byte[]> generateXlsxReport(
            @RequestParam(required = true) Long fileId
    ) throws Exception {
        try {
            /// Get data for file Excel
            byte[] report = planService.getBodyFileExcelXLSX(fileId);
            if (report != null) {
                // Create file name for file Excel
                String outFileName = planService.generateXLSXFileName(fileId);

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

    @GetMapping("/download-xls")
    public ResponseEntity<byte[]> generateXlsReport(
            @RequestParam(required = true) Long fileId
    ) throws Exception {

        /// Get data for file Excel
        byte[] report = planService.getBodyFileExcelXLS(fileId);
        if (report != null) {
            // Create file name for file Excel
            String outFileName = planService.generateXLSFileName(fileId);

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
            List<ReportStatus> costTypes = planService.getListPlanStatus();

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
    private ResponseEntity<ExceptionResponse> deletePlan(
            @Valid @RequestBody DeletePlanBody planBody, BindingResult bindingResult) {
        try {
            FinancialPlan deletedPlan = planService.deletePlan(planBody.getPlanId());

            if (deletedPlan == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionResponse.builder().field("Delete successful").message("Deleted plan have id = " + planBody.getPlanId()).build());
            }

            return ResponseEntity.ok(null);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Unauthorized exception").message("User unauthorized").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Not exist exception").message("Not found any plan have id = " + planBody.getPlanId()).build());
        } catch (InvalidDateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Invalid time").message("Can not delete plan in this time period").build());
        }

    }

    @PutMapping("/re-upload")
    private ResponseEntity<ExceptionResponse> reUploadPlan(
            @Valid @RequestBody ListReUploadExpenseBody reUploadExpenseBody, BindingResult bindingResult
    ) throws Exception {
        try {
            List<FinancialPlanExpense> expenses = new ArrayList<>();
            reUploadExpenseBody.getData().forEach(reUploadExpense -> {
                expenses.add(new ReUploadExpensesMapperImpl().mapUpdateExpenseToPlanExpense(reUploadExpense));
            });

            FinancialPlan plan = planService.convertListExpenseAndMapToPlan(reUploadExpenseBody.getPlanId(), expenses);

            planService.reUploadPlan(plan);

            return ResponseEntity.status(HttpStatus.OK).body(ExceptionResponse.builder().field("Successful").message("Re-upload successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Unauthorized exception").message("User unauthorized").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Not exist exception").message("Not found any plan have id = " + reUploadExpenseBody.getPlanId()).build());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ExceptionResponse> confirmExpenses(
            @Valid @RequestBody NewPlanBody planBody, BindingResult bindingResult) throws Exception {
        try {

            // Mapping to planBody to FinancialPlan
            FinancialPlan plan = new CreatePlanMapperImpl().mapPlanBodyToPlanMapping(planBody);

            //
            List<FinancialPlanExpense> expenses = new CreatePlanMapperImpl().mapExpenseBodyToExpense(planBody.getExpenses());
            // Save plan
            FinancialPlan savedPlan = planService.createPlan(plan, expenses, planBody.getFileName(), planBody.getTermId());

            if (savedPlan == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionResponse.builder().field("Error Exception").message("").build());
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(ExceptionResponse.builder().field("Successful").message("Upload successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Unauthorized exception").message("User unauthorized").build());
        } catch (DuplicateKeyException | InvalidDateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error Exception").message("").build());
        }
    }

//    @PutMapping("/expense-approval")
//    public ResponseEntity<String> approvalExpenses(
//            @Valid @RequestBody ApprovalExpenseBody body, BindingResult bindingResult) throws Exception {
//        try {
//
//            planService.approvalExpenses(body.getPlanId(), body.getListExpenseId());
//
//            return ResponseEntity.status(HttpStatus.CREATED).body(null);
//        } catch (UnauthorizedException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        } catch (InvalidInputException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }
//
//    @PutMapping("/expense-deny")
//    public ResponseEntity<String> denyExpenses(
//            @Valid @RequestBody DenyExpenseBody body, BindingResult bindingResult) throws Exception {
//        try {
//
//            planService.denyExpenses(body.getPlanId(), body.getListExpenseId());
//
//            return ResponseEntity.status(HttpStatus.CREATED).body(null);
//        } catch (UnauthorizedException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        } catch (InvalidInputException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }
//
//    @PutMapping("/expense-approval-all")
//    public ResponseEntity<String> approvalAllExpenses(
//            @Valid @RequestBody ApprovalAllExpenseBody body, BindingResult bindingResult) throws Exception {
//        try {
//
//            planService.approvalAllExpenses(body.getPlanId());
//
//            return ResponseEntity.status(HttpStatus.CREATED).body(null);
//        } catch (UnauthorizedException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        } catch (InvalidInputException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//    }

    @GetMapping("/download/template-xlsx")
    public ResponseEntity<byte[]> downloadXlsxReportTemplate() throws Exception {

        /// Get data for file Excel
        byte[] report = planService.getTemplateData();

        if (report != null) {
            return createExcelFileResponseEntity(report, "Financial_Planning_Template.xlsx");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }


    @GetMapping("/download/template-xls")
    public ResponseEntity<byte[]> downloadXlsReportTemplate() throws Exception {
        /// Get data for file Excel
        byte[] report = planService.getTemplateData();

        if (report != null) {
            return createExcelFileResponseEntity(report, "Financial_Planning_Template.xls");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @GetMapping("/download/last-version-xls")
    public ResponseEntity<byte[]> generateLastVersionXlsReport(
            @RequestParam(required = true) Long planId
    ) throws Exception {
        try {
            /// Get data for file Excel
            byte[] report = planService.getLastVersionBodyFileExcelXLS(planId);
            if (report != null) {
                // Create file name for file Excel
                String outFileName = planService.generateXLSFileNameByPlanId(planId);

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

    @GetMapping("/download/last-version-xlsx")
    public ResponseEntity<byte[]> generateLastVersionXlsxReport(
            @RequestParam(required = true) Long planId
    ) throws Exception {
        try {
            /// Get data for file Excel
            byte[] report = planService.getLastVersionBodyFileExcelXLSX(planId);
            if (report != null) {
                // Create file name for file Excel
                String outFileName = planService.generateXLSXFileNameByPlanId(planId);

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

    @PutMapping("/submit-for-review")
    private ResponseEntity<String> submitPlanForReview(
            @Valid @RequestBody SubmitPlanBody submitPlanBody, BindingResult bindingResult
    ) {
        try {
            planService.submitPlanForReview(submitPlanBody.getPlanId());
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


}