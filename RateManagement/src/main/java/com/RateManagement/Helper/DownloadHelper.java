package com.RateManagement.Helper;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.RateManagement.Entity.Rate;

/**
 * @author R.Raj
 *
 */
public class DownloadHelper {
    public static String Type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    static String[] HEADERs = { "STAY_DATE_FROM", "STAY_DATE_TO", "NIGHTS", "VALUE", "BUNGALOW_ID", "CLOSED_DATE" };

    static String SHEET = "RatesDownload";

    /**
     * @param rates
     * @return
     */
    public static ByteArrayInputStream rateToExcel(List<Rate> rates) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }

            int rowIdx = 1;
            for (Rate rate : rates) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(rate.getStayDateFrom().toString());
                row.createCell(1).setCellValue(rate.getStayDateTo().toString());
                row.createCell(2).setCellValue(rate.getNights());
                row.createCell(3).setCellValue(rate.getValue());
                row.createCell(4).setCellValue(rate.getBungalowId());
                row.createCell(5).setCellValue(rate.getClosedDate() != null ? rate.getClosedDate().toString() : "");
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to import data to Excel file: " + e.getMessage());
        }
    }
}