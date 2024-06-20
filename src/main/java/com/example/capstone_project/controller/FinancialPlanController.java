package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.plan.create.NewPlanBody;
import com.example.capstone_project.controller.body.ListBody;
import com.example.capstone_project.controller.body.plan.expenses.PlanExpensesBody;
import com.example.capstone_project.controller.body.plan.reupload.ReUploadExpenseBody;
import com.example.capstone_project.controller.body.plan.delete.DeletePlanBody;
import com.example.capstone_project.controller.body.user.create.CreateUserBody;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.Responses;
import com.example.capstone_project.controller.responses.department.paginate.DepartmentPaginateResponse;
import com.example.capstone_project.controller.responses.expense.CostTypeResponse;
import com.example.capstone_project.controller.responses.expense.list.ExpenseResponse;
import com.example.capstone_project.controller.responses.plan.DepartmentResponse;
import com.example.capstone_project.controller.responses.plan.StatusResponse;
import com.example.capstone_project.controller.responses.plan.TermResponse;
import com.example.capstone_project.controller.responses.plan.UserResponse;
import com.example.capstone_project.controller.responses.plan.detail.PlanDetailResponse;
import com.example.capstone_project.controller.responses.plan.list.PlanResponse;
import com.example.capstone_project.controller.responses.plan.version.VersionResponse;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.result.PlanDetailResult;
import com.example.capstone_project.service.FinancialPlanService;
import com.example.capstone_project.utils.enums.RoleCode;
import com.example.capstone_project.utils.helper.JwtHelper;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.plan.detail.PlanDetailMapperImpl;
import com.example.capstone_project.utils.mapper.plan.expenses.ExpenseResponseMapper;
import com.example.capstone_project.utils.mapper.plan.expenses.ExpenseResponseMapperImpl;
import com.example.capstone_project.utils.mapper.plan.list.ListPlanResponseMapperImpl;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
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
            @RequestBody PlanExpensesBody planBody,
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
        List<FinancialPlanExpense> expenses = planService.getListExpenseWithPaginate(planBody.getPlanId(), query, statusId, costTypeId, pageable);

        // Response
        ListPaginationResponse<ExpenseResponse> response = new ListPaginationResponse<>();

        long count = 0;

        if (expenses != null) {

            // Count total record
            count = planService.countDistinctListExpenseWithPaginate(query, planBody.getPlanId(), statusId, costTypeId);

            // Mapping to TermPaginateResponse
            expenses.forEach(expense -> response.getData().add(new ExpenseResponseMapperImpl().mapToExpenseResponseMapping(expense)));
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
            @RequestParam Long planId
    ) {

        // Get data
        PlanDetailResult plan = planService.getPlanDetailByPlanId(planId);

        // Response
        PlanDetailResponse response;

        if (plan != null) {
            // Mapping to TermPaginateResponse
            response = new PlanDetailMapperImpl().mapToPlanDetailResponseMapping(plan);
            response.setVersion(planService.getPlanVersionById(planId));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/download")
    public ResponseEntity<byte[]> generateXlsxReport(
            @RequestParam Integer planId
    ) throws Exception {
        String fileLocation = "src/main/resources/fileTemplate/Financial Planning_v1.0.xlsx";
        FileInputStream file = new FileInputStream(fileLocation);
        XSSFWorkbook wb = new XSSFWorkbook(file);

        Sheet sheet = wb.getSheet("Expense");

        String[][] tableData = {
                {"Code Expense 1", "31/05/2024", "Financial plan December Q3 2021", "BU 01", "Promotion event", "Direction cost", "15000000", "3", "45000000", "RECT", "Hong Ha", "HongHD9", "Approximate", "Waiting for approximate"},
                {"Code Expense 2", "31/05/2024", "Financial plan December Q3 2021", "BU 02", "Social media", "Direction cost", "1000000", "3", "3000000", "CAM1", "Internal", "LanNT12", "", "Approved"},
                {"Code Expense 3", "31/05/2024", "Financial plan December Q3 2021", "BU 01", "Office supplies", "Administration cost", "1000000", "5", "5000000", "RECT1", "Internal", "AnhMN2", "", "Approved"},
                {"Code Expense 4", "31/05/2024", "Financial plan December Q3 2021", "BU 02", "Internal training", "Operating cost", "1000000", "4", "4000000", "CAM2", "Internal", "LanNT12", "", "Waiting for approval"}
        };

        Row row = null;
        Cell cell = null;

        int rowPosition = 2;
        int colPosition = 0;

        for (int i = 0; i < tableData.length; i++) {
            row = sheet.getRow(i + rowPosition);

            for (int j = 0; j < tableData[0].length; j++) {
                cell = row.getCell(j + colPosition);

                cell.setCellValue(tableData[i][j]);
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        wb.close();
        out.close();
        byte[] report = out.toByteArray();

        String outFileName = "report.xlsx";

        return createResponseEntity(report, outFileName);
    }

    private ResponseEntity<byte[]> createResponseEntity(
            byte[] report, String fileName) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(report);
    }

    @GetMapping("/plan-status")
    public ResponseEntity<Responses<StatusResponse>> getListStatusPaging() {
        Responses<StatusResponse> responses = new Responses<>();
        responses.setData(List.of(
                StatusResponse.builder()
                        .statusId(1L)
                        .name("New")
                        .build(),
                StatusResponse.builder()
                        .statusId(2L)
                        .name("Waiting for reviewed")
                        .build(),
                StatusResponse.builder()
                        .statusId(1L)
                        .name("Approved")
                        .build(),
                StatusResponse.builder()
                        .statusId(1L)
                        .name("Reviewed")
                        .build()
        ));

        return ResponseEntity.ok(responses);
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
            @Validated @RequestBody DeletePlanBody planBody) {
        System.out.println(planBody.toString());
        return ResponseEntity.ok("id " + planBody.getPlanId());
    }

    @PutMapping("/re-upload")
    private ResponseEntity<ListBody<ReUploadExpenseBody>> reUploadPlan(
            @RequestBody ListBody<ReUploadExpenseBody> expenseListBody
    ) {

        return ResponseEntity.status(HttpStatus.OK).body(expenseListBody);
    }

    @PostMapping("/create")
    public ResponseEntity<String> confirmExpenses(
            @RequestBody NewPlanBody planBody, BindingResult bindingResult) throws Exception {


        planService.creatPlan(planBody);

        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }
}
