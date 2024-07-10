package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.plan.create.NewPlanBody;
import com.example.capstone_project.controller.body.plan.detail.PlanDetailBody;
import com.example.capstone_project.controller.body.plan.reupload.ListReUploadExpenseBody;
import com.example.capstone_project.controller.body.plan.download.PlanDownloadBody;
import com.example.capstone_project.controller.body.plan.expenses.PlanExpensesBody;
import com.example.capstone_project.controller.body.plan.reupload.ReUploadExpenseBody;
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
import com.example.capstone_project.repository.result.ExpenseResult;
import com.example.capstone_project.repository.result.PlanDetailResult;
import com.example.capstone_project.repository.result.PlanVersionResult;
import com.example.capstone_project.repository.result.VersionResult;
import com.example.capstone_project.service.FinancialPlanService;
import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import com.example.capstone_project.utils.enums.RoleCode;
import com.example.capstone_project.utils.helper.JwtHelper;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.helper.UserHelper;
import com.example.capstone_project.utils.mapper.plan.create.CreatePlanMapperImpl;
import com.example.capstone_project.utils.mapper.plan.detail.PlanDetailMapperImpl;
import com.example.capstone_project.utils.mapper.plan.expenses.PlanExpenseResponseMapperImpl;
import com.example.capstone_project.utils.mapper.plan.list.ListPlanResponseMapperImpl;
import com.example.capstone_project.utils.mapper.plan.reupload.ReUploadExpensesMapperImpl;
import com.example.capstone_project.utils.mapper.plan.status.PlanStatusMapper;
import com.example.capstone_project.utils.mapper.plan.status.PlanStatusMapperImpl;
import com.example.capstone_project.utils.mapper.plan.version.PlanListVersionResponseMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
public class FinancialPlanController {

    private final JwtHelper jwtHelper;
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        System.out.println("count = " + count);
        long numPages = PaginationHelper.calculateNumPages(count, sizeInt);

        response.setPagination(Pagination.builder()
                .totalRecords(count)
                .page(pageInt)
                .limitRecordsPerPage(sizeInt)
                .numPages(numPages)
                .build());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail")
    public ResponseEntity<PlanDetailResponse> getPlanDetail(
            @RequestParam(required = true) Long  planId
    ) throws Exception {

        // Get data
        PlanDetailResult plan = planService.getPlanDetailByPlanId(planId);

        // Response
        PlanDetailResponse response;

        if (plan != null) {
            // Mapping to PlanDetail Response
                response = new PlanDetailMapperImpl().mapToPlanDetailResponseMapping(plan);
                response.setVersion(planService.getPlanVersionById(planId));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/download")
    public ResponseEntity<byte[]> generateXlsxReport(
            @RequestBody PlanDownloadBody planBody
    ) throws Exception {

        /// Get data for file Excel
        byte[] report = planService.getBodyFileExcelXLSX(planBody.getFileId());
        if (report != null) {
            // Create file name for file Excel
            String outFileName = planService.generateFileName(planBody.getFileId());

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
    }

    @GetMapping("versions")
    public ResponseEntity<ListPaginationResponse<VersionResponse>> getListVersion(
            @RequestParam Long planId,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) throws Exception {
        // Handling page and pageSize
        Integer pageInt = PaginationHelper.convertPageToInteger(page);
        Integer sizeInt = PaginationHelper.convertPageSizeToInteger(size);

        // Handling pagination
        Pageable pageable = PaginationHelper.handlingPagination(pageInt, sizeInt, sortBy, sortType);

        // Get data
        List<VersionResult> planFiles = planService.getListVersionWithPaginate(planId, pageable);
        System.out.println(Arrays.toString(planFiles.toArray()));
        // Response
        ListPaginationResponse<VersionResponse> response = new ListPaginationResponse<>();

        long count = 0;

        if (planFiles != null) {

            // Count total record
            count = planService.countDistinctListPlanVersionPaging(planId);

            // Mapping to TermPaginateResponse
            planFiles.forEach(file -> response.getData().add( new PlanListVersionResponseMapperImpl().mapToPlanVersionResponseMapper(file)));

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        long numPages = PaginationHelper.calculateNumPages(count, sizeInt);

        response.setPagination(Pagination.builder()
                .totalRecords(count)
                .page(pageInt)
                .limitRecordsPerPage(sizeInt)
                .numPages(numPages)
                .build());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    private ResponseEntity<Object> deletePlan(
            @Validated @RequestBody DeletePlanBody planBody) {

        FinancialPlan deletedPlan = planService.deletePlan(planBody.getPlanId());

        if (deletedPlan == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        return ResponseEntity.ok(null);
    }

    @PutMapping("/re-upload")
    private ResponseEntity<String> reUploadPlan(
            @RequestBody ListReUploadExpenseBody reUploadExpenseBody
    ) throws Exception {

        List<ExpenseResult> listExpenseCreate = planService.getListExpenseByPlanId(reUploadExpenseBody.getPlanId());

        if (listExpenseCreate == null || listExpenseCreate.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        HashMap<String, ExpenseStatusCode> hashMapExpense = new HashMap<>();
        List<FinancialPlanExpense> listExpense = new ArrayList<>();
        String lastExpenseCode = planService.getLastExpenseCode(reUploadExpenseBody.getPlanId());
        String[] parts = lastExpenseCode.split("_");
        StringBuilder prefixExpenseKey = new StringBuilder();
        for (int i = 0; i < parts.length - 2; i++) {
            prefixExpenseKey.append(parts[i] + "_");
        }

        int lastIndexCode = Integer.parseInt(parts[parts.length - 1]);

        PlanVersionResult version = planService.getCurrentVersionByPlanId(reUploadExpenseBody.getPlanId());

        for (ExpenseResult expenseResult : listExpenseCreate) {
            if (expenseResult.getStatusCode().equals(ExpenseStatusCode.APPROVED)) {
                hashMapExpense.putIfAbsent(expenseResult.getExpenseCode(), ExpenseStatusCode.APPROVED);
                listExpense.add(planService.getPlanExpenseReferenceById(expenseResult.getExpenseId()));
            } else if (expenseResult.getStatusCode().equals(ExpenseStatusCode.NEW)) {
                hashMapExpense.putIfAbsent(expenseResult.getExpenseCode(), ExpenseStatusCode.NEW);
            } else if (expenseResult.getStatusCode().equals(ExpenseStatusCode.WAITING_FOR_APPROVAL)) {
                hashMapExpense.putIfAbsent(expenseResult.getExpenseCode(), ExpenseStatusCode.WAITING_FOR_APPROVAL);
            } else if (expenseResult.getStatusCode().equals(ExpenseStatusCode.DENIED)) {
                hashMapExpense.putIfAbsent(expenseResult.getExpenseCode(), ExpenseStatusCode.DENIED);
            }
        }

        for (ReUploadExpenseBody expenseBody : reUploadExpenseBody.getData()) {
            if (hashMapExpense.containsKey(expenseBody.getExpenseCode()) &&
                    !hashMapExpense.get(expenseBody.getExpenseCode()).getValue().equals(ExpenseStatusCode.APPROVED.getValue())
            ) {
                listExpense.add(new ReUploadExpensesMapperImpl().mapUpdateExpenseToPlanExpense(expenseBody));
            } else if (!hashMapExpense.containsKey(expenseBody.getExpenseCode())) {
                listExpense.add(new ReUploadExpensesMapperImpl().newExpenseToPlanExpense(expenseBody, prefixExpenseKey, version.getVersion(), ++lastIndexCode));
            }
        }

        FinancialPlan plan = new ReUploadExpensesMapperImpl().mapToPlanMapping(reUploadExpenseBody.getPlanId(), (long) UserHelper.getUserId(), version, listExpense);

        planService.reUploadPlan(plan);

        return ResponseEntity.status(HttpStatus.OK).body("Re upload successful");
    }

    @PostMapping("/create")
    public ResponseEntity<String> confirmExpenses(
            @RequestBody NewPlanBody planBody, BindingResult bindingResult) throws Exception {
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
    }
}
