package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.AnnualReport;
import com.example.capstone_project.entity.Report;
import com.example.capstone_project.repository.AnnualReportRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.result.AnnualReportExpenseResult;
import com.example.capstone_project.repository.result.CostTypeDiagramResult;
import com.example.capstone_project.service.AnnualReportService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AnnualReportServiceImpl implements AnnualReportService {
    private final UserAuthorityRepository userAuthorityRepository;
    private final AnnualReportRepository annualReportRepository;

    @Override
    public List<AnnualReport> getListAnnualReportPaging(Pageable pageable, String year) {
        // Get list authorities of this user
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_ANNUAL_REPORT.getValue())) {
            return annualReportRepository.getListAnnualReportPaging(pageable, year);
        } else {
            throw new UnauthorizedException("Unauthorized to view annual report");
        }
    }

    @Override
    public long countDistinctListAnnualReportPaging(String year) {
        return annualReportRepository.countDistinctListAnnualReportPaging(year);
    }

    @Override
    public List<Report> getListExpenseWithPaginate(Long annualReportId, Long costTypeId, Long departmentId, Pageable pageable) {
        // Get list authorities of this user
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_ANNUAL_REPORT.getValue())) {
            if (!annualReportRepository.existsById(annualReportId)) {
                throw new ResourceNotFoundException("Not found any annual report have id = " + annualReportId);
            }
            return annualReportRepository.getListExpenseWithPaginate(annualReportId, costTypeId, departmentId, pageable);
        } else {
            throw new UnauthorizedException("Unauthorized to view annual report");
        }
    }

    @Override
    public long countDistinctListExpenseWithPaginate(Long annualReportId, Long costTypeId, Long departmentId) {
        return annualReportRepository.countDistinctListExpenseWithPaginate(annualReportId, costTypeId, departmentId);
    }

    @Override
    public List<CostTypeDiagramResult> getAnnualReportCostTypeDiagram(Long annualReportId) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_ANNUAL_REPORT.getValue())) {
            if (!annualReportRepository.existsById(annualReportId)) {
                throw new ResourceNotFoundException("Not found any annual report have id = " + annualReportId);
            }
            return annualReportRepository.getAnnualReportCostTypeDiagram(annualReportId);
        } else {
            throw new UnauthorizedException("Unauthorized to view annual report");
        }
    }

    @Override
    public AnnualReport getAnnualReportDetail(Long annualReportId) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_ANNUAL_REPORT.getValue())) {
            if (!annualReportRepository.existsById(annualReportId)) {
                throw new ResourceNotFoundException("Not found any term have id = " + annualReportId);
            }
            return annualReportRepository.getReferenceById(annualReportId);
        } else {
            throw new UnauthorizedException("Unauthorized to view annual report");
        }

    }

    @Override
    public byte[] getBodyFileExcelXLSX(Long annualReportId) throws IOException {
        // Checkout authority and get list expenses by file id
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_ANNUAL_REPORT.getValue())) {
            List<AnnualReportExpenseResult> expenses = annualReportRepository.getListExpenseByAnnualReportId(annualReportId);

            if (expenses != null && !expenses.isEmpty()) {

                String fileLocation = "src/main/resources/fileTemplate/Financial Planning_AnnualReport_v1.0.xlsx";
                FileInputStream file = new FileInputStream(fileLocation);
                XSSFWorkbook wb = new XSSFWorkbook(file);

                return fillDataToExcel(wb, expenses);
            } else {
                throw new ResourceNotFoundException("Not exist file = " + annualReportId + " or list expenses is empty");
            }
        } else {
            throw new UnauthorizedException("Unauthorized to view annual report");
        }
    }

    @Override
    public String generateXLSXFileName(Long annualReportId) {
        String year = annualReportRepository.getYear(annualReportId);
        return "AnnualReport" + "_" + year + ".xlsx";
    }

    @Override
    public byte[] getBodyFileExcelXLS(Long annualReportId) throws IOException {
        // Checkout authority and get list expenses by file id
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_ANNUAL_REPORT.getValue())) {
            List<AnnualReportExpenseResult> expenses = annualReportRepository.getListExpenseByAnnualReportId(annualReportId);

            if (expenses != null && !expenses.isEmpty()) {

                String fileLocation = "src/main/resources/fileTemplate/Financial Planning_AnnualReport_v1.0.xls";
                FileInputStream file = new FileInputStream(fileLocation);
                HSSFWorkbook wb = new HSSFWorkbook(file);

                return fillDataToExcel(wb, expenses);
            } else {
                throw new ResourceNotFoundException("Not exist file = " + annualReportId + " or list expenses is empty");
            }
        } else {
            throw new UnauthorizedException("Unauthorized to view annual report");
        }
    }

    @Override
    public String generateXLSFileName(Long annualReportId) {
        String year = annualReportRepository.getYear(annualReportId);
        return "AnnualReport" + "_" + year + ".xls";
    }

    private byte[] fillDataToExcel(Workbook wb, List<AnnualReportExpenseResult> expenses) throws IOException {
        Sheet sheet = wb.getSheet("Expense");

        String[][] tableData = new String[expenses.size()][4];

        // Convert list expense to matrix
        for (int i = 0; i < expenses.size(); i++) {
            AnnualReportExpenseResult expense = expenses.get(i);
            tableData[i][0] = expense.getDepartment();
            tableData[i][1] = expense.getTotalExpense();
            tableData[i][2] = expense.getBiggestExpenditure();
            tableData[i][3] = expense.getCostType();
        }

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
        return out.toByteArray();
    }
}
