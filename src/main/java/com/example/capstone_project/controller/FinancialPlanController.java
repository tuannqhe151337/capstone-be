package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.planManagement.DepartmentResponse;
import com.example.capstone_project.controller.responses.planManagement.PlanResponse;
import com.example.capstone_project.controller.responses.planManagement.StatusResponse;
import com.example.capstone_project.controller.responses.planManagement.TermResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
public class FinancialPlanController {
    @GetMapping("/list")
    public ResponseEntity<ListResponse<PlanResponse>> getListPlan(
            @RequestParam(required = false) Integer termId,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ){
        ListResponse<PlanResponse> listResponse = new ListResponse<>();
        listResponse.setData(List.of(
                PlanResponse.builder()
                        .id(1L)
                        .name("BU name_term_plan")
                        .status(StatusResponse.builder()
                                .statusId(1L)
                                .name("New").build())
                        .term(TermResponse.builder()
                                .termId(1L)
                                .name("Term name 1").build())
                        .department(DepartmentResponse.builder()
                                .departmentId(1L)
                                .name("BU 1").build())
                        .version("V1").build(),
                PlanResponse.builder()
                        .id(2L)
                        .name("BU name_term_plan")
                        .status(StatusResponse.builder()
                                .statusId(2L)
                                .name("Approved").build())
                        .term(TermResponse.builder()
                                .termId(1L)
                                .name("Term name 1").build())
                        .department(DepartmentResponse.builder()
                                .departmentId(2L)
                                .name("BU 2").build())
                        .version("V2").build()
                ));

        listResponse.setPagination(Pagination.builder()
                .count(100)
                .page(10)
                .displayRecord(0)
                .numPages(1)
                .build());

        return ResponseEntity.ok(listResponse);
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
                {"Code Expense 1","31/05/2024", "Financial plan December Q3 2021", "BU 01", "Promotion event", "Direction cost", "15000000", "3", "45000000", "RECT", "Hong Ha", "HongHD9", "Approximate", "Waiting for approximate"},
                {"Code Expense 2","31/05/2024", "Financial plan December Q3 2021", "BU 02", "Social media", "Direction cost", "1000000", "3", "3000000", "CAM1", "Internal", "LanNT12", "", "Approved"},
                {"Code Expense 3","31/05/2024", "Financial plan December Q3 2021", "BU 01", "Office supplies", "Administration cost", "1000000", "5", "5000000", "RECT1", "Internal", "AnhMN2", "", "Approved"},
                {"Code Expense 4","31/05/2024", "Financial plan December Q3 2021", "BU 02", "Internal training", "Operating cost", "1000000", "4", "4000000", "CAM2", "Internal", "LanNT12", "", "Waiting for approval"}
        };

        Row row = null;
        Cell cell = null;

        int rowPosition = 2;
        int colPosition = 0;

        for (int i = 0; i<tableData.length; i++){
            row = sheet.getRow(i+rowPosition);

            for (int j = 0; j<tableData[0].length;j++){
                cell = row.getCell(j+colPosition);

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
}
