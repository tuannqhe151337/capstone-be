package com.example.capstone_project.utils.helper;

import com.example.capstone_project.repository.result.ExpenseResult;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class HandleFileHelper {
    public byte[] fillDataToExcel(Workbook wb, List<ExpenseResult> expenses) throws IOException {
        Sheet sheet = wb.getSheet("Expense");

        Row row = null;

        int rowPosition = 2;
        int colPosition;
        for (ExpenseResult expenseResult : expenses) {
            colPosition = 0;
            row = sheet.getRow(rowPosition++);
            row.getCell(colPosition++).setCellValue(expenseResult.getExpenseCode());
            row.getCell(colPosition++).setCellValue(expenseResult.getDate().toLocalDate());
            row.getCell(colPosition++).setCellValue(expenseResult.getTermName());
            row.getCell(colPosition++).setCellValue(expenseResult.getDepartmentName());
            row.getCell(colPosition++).setCellValue(expenseResult.getExpenseName());
            row.getCell(colPosition++).setCellValue(expenseResult.getCostTypeName());
            row.getCell(colPosition++).setCellValue(expenseResult.getUnitPrice().doubleValue());
            row.getCell(colPosition++).setCellValue(expenseResult.getAmount());
            row.getCell(colPosition++).setCellValue(expenseResult.getTotal().doubleValue());
            row.getCell(colPosition++).setCellValue(expenseResult.getProjectName());
            row.getCell(colPosition++).setCellValue(expenseResult.getSupplierName());
            row.getCell(colPosition++).setCellValue(expenseResult.getPic());
            row.getCell(colPosition++).setCellValue(expenseResult.getNote());
            row.getCell(colPosition).setCellValue(expenseResult.getStatusName());
        }

        sheet = wb.getSheet("List");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        wb.close();
        out.close();
        return out.toByteArray();
    }
}
