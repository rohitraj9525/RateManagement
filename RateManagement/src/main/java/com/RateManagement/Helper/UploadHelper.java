package com.RateManagement.Helper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.RateManagement.Entity.Rate;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

 

public class UploadHelper {

    public static List<Rate> excelToRates(InputStream inputStream) throws IOException {
        List<Rate> rates = new ArrayList<>();

        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheet("RatesDownload"); // Assuming the data is in the first sheet

        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next(); // Skip the header row

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            Rate rate = new Rate();

            // Stay Date From (Cell 0)
            
            
            Cell stayDateFromCell = row.getCell(0);
            String stayDateFromValue = stayDateFromCell.getStringCellValue().trim();
            LocalDate stayDateFrom = LocalDate.parse(stayDateFromCell.getStringCellValue());
            rate.setStayDateFrom(stayDateFrom);
            
            // Stay Date To (Cell 1)
            
            
            Cell stayDateToCell = row.getCell(1);
            LocalDate stayDateTo = LocalDate.parse(stayDateToCell.getStringCellValue());
            rate.setStayDateTo(stayDateTo);
           

            // Nights (Cell 2)
            
            
            Cell nightsCell = row.getCell(2);
            int nights = (int) nightsCell.getNumericCellValue();
            rate.setNights(nights);
            
            

            // Value (Cell 3)
            
            Cell valueCell = row.getCell(3);
            double value = valueCell.getNumericCellValue();
            rate.setValue(value);
;
            

            // Bungalow ID (Cell 4)
           
            Cell bungalowIdCell = row.getCell(4);
            long bungalowId = (long) bungalowIdCell.getNumericCellValue();
            rate.setBungalowId(bungalowId);
            

            // Closed Date (Cell 5)
            Cell closedDateCell = row.getCell(5);
            System.out.println(closedDateCell);
            if (closedDateCell != null && 
            		!closedDateCell.getStringCellValue().isBlank() && !closedDateCell.getStringCellValue().isEmpty()) 
            {
                LocalDate closedDate = LocalDate.parse(closedDateCell.getStringCellValue());
                rate.setClosedDate(closedDate);
            }
            else {
            	rate.setClosedDate(null);
            }

            rates.add(rate);
        }

        workbook.close();

        return rates;
    }

	
}