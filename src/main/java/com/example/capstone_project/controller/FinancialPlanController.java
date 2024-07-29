package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.expense.ApprovalExpenseBody;
import com.example.capstone_project.controller.body.plan.create.NewPlanBody;
import com.example.capstone_project.controller.body.ListBody;
import com.example.capstone_project.controller.body.plan.detail.PlanDetailBody;
import com.example.capstone_project.controller.body.plan.download.PlanDownloadBody;
import com.example.capstone_project.controller.body.plan.reupload.ReUploadExpenseBody;
import com.example.capstone_project.controller.body.plan.delete.DeletePlanBody;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.expense.list.ExpenseResponse;
import com.example.capstone_project.controller.responses.plan.StatusResponse;
import com.example.capstone_project.controller.responses.plan.UserResponse;
import com.example.capstone_project.controller.responses.plan.detail.PlanDetailResponse;
import com.example.capstone_project.controller.responses.plan.list.PlanResponse;
import com.example.capstone_project.controller.responses.plan.version.VersionResponse;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.result.PlanDetailResult;
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
import com.example.capstone_project.utils.mapper.plan.status.PlanStatusMapperImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

            if (plan != null) {
                // Mapping to PlanDetail Response
                response = new PlanDetailMapperImpl().mapToPlanDetailResponseMapping(plan);
                response.setVersion(planService.getPlanVersionById(planDetailBody.getPlanId()));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
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

                return createResponseEntity(report, outFileName);

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

            return createResponseEntity(report, outFileName);

        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    private ResponseEntity<byte[]> createResponseEntity(
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
            @RequestParam Integer planId,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        ListPaginationResponse<VersionResponse> listPaginationResponse = new ListPaginationResponse<>();
        listPaginationResponse.setData(List.of(
                VersionResponse.builder()
                        .version("v1")
                        .publishedDate(LocalDate.of(2024, 4, 10))
                        .uploadedBy(UserResponse.builder()
                                .userId(1L)
                                .username("Anhln").build()).build(),
                VersionResponse.builder()
                        .version("v2")
                        .publishedDate(LocalDate.now())
                        .uploadedBy(UserResponse.builder()
                                .userId(1L)
                                .username("Anhln").build()).build(),
                VersionResponse.builder()
                        .version("v3")
                        .publishedDate(LocalDate.now())
                        .uploadedBy(UserResponse.builder()
                                .userId(1L)
                                .username("Anhln").build()).build()
        ));

        listPaginationResponse.setPagination(Pagination.builder()
                .totalRecords(2222)
                .page(10)
                .limitRecordsPerPage(33)
                .numPages(1)
                .build());

        return ResponseEntity.ok(listPaginationResponse);
    }

    @DeleteMapping("/delete")
    private ResponseEntity<String> deletePlan(
            @Valid @RequestBody DeletePlanBody planBody, BindingResult bindingResult) {
        try {
            FinancialPlan deletedPlan = planService.deletePlan(planBody.getPlanId());

            if (deletedPlan == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.ok("Delete successful plan id: " + deletedPlan.getId());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to delete plan");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found any plan have id = " + planBody.getPlanId());
        }

    }

    @PutMapping("/re-upload")
    private ResponseEntity<ListBody<ReUploadExpenseBody>> reUploadPlan(
            @Valid @RequestBody ListBody<ReUploadExpenseBody> expenseListBody
    ) {

        return ResponseEntity.status(HttpStatus.OK).body(expenseListBody);
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

            return ResponseEntity.status(HttpStatus.CREATED).body("Create successful");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized to create plan");
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This term already have plan of this department");
        } catch (InvalidDateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Plan due date of this term was expired");
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
}
