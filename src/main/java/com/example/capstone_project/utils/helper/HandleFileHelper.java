package com.example.capstone_project.utils.helper;

import com.example.capstone_project.repository.result.ExpenseResult;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
@Component
public class HandleFileHelper {
    public byte[] fillDataToExcel(Workbook wb, List<ExpenseResult> expenses) throws IOException {
        Sheet sheet = wb.getSheet("Expense");

        String[][] tableData = new String[expenses.size()][14];

        // Convert list expense to matrix
        for (int i = 0; i < expenses.size(); i++) {
            ExpenseResult expense = expenses.get(i);
            tableData[i][0] = expense.getExpenseCode();
            tableData[i][1] = expense.getDate().toLocalDate().toString();
            tableData[i][2] = expense.getTermName();
            tableData[i][3] = expense.getDepartmentName();
            tableData[i][4] = expense.getExpenseName();
            tableData[i][5] = expense.getCostTypeName();
            tableData[i][6] = expense.getUnitPrice();
            tableData[i][7] = expense.getAmount();
            tableData[i][8] = expense.getTotal();
            tableData[i][9] = expense.getProjectName();
            tableData[i][10] = expense.getSupplierName();
            tableData[i][11] = expense.getPic();
            tableData[i][12] = expense.getNote();
            tableData[i][13] = expense.getStatusName();
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
