package com.greenrent.helper;

import com.greenrent.domain.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelReportHelper {

    static String SHEET_USER="Users";
    static String USER_HEADERS[] = {"id","FirstName","LastName","PhoneNumber","Email","Address","ZipCode","Roles" };

    // 1: Convert user informations to excel structure and return byteStrem to the client
    public static ByteArrayInputStream getUsersExcelReport(List<User> users) throws IOException {
        Workbook workBook = new XSSFWorkbook();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Sheet sheet = workBook.createSheet(SHEET_USER);

        // Header Row
        Row headerRow = sheet.createRow(0);

        for (int column=0; column<USER_HEADERS.length;column++) {
            Cell cell = headerRow.createCell(column);
            cell.setCellValue(USER_HEADERS[column]);
        }

        
        int rowId=1;
        for (User user:users
             ) {
          Row row =  sheet.createRow(rowId++);
          row.createCell(0).setCellValue(user.getId());
          row.createCell(1).setCellValue(user.getFirstName());
          row.createCell(2).setCellValue(user.getLastName());
          row.createCell(3).setCellValue(user.getPhoneNumber());
          row.createCell(4).setCellValue(user.getEmail());
          row.createCell(5).setCellValue(user.getAddress());
          row.createCell(6).setCellValue(user.getZipCode());
          row.createCell(7).setCellValue(user.getRoles().toString());
        }

        workBook.write(out);

        return new ByteArrayInputStream(out.toByteArray());

    }

}
