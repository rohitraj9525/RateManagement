package com.RateManagement.Helper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.RateManagement.Entity.Rate;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
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
            
            if(stayDateFromCell == null || stayDateFromCell.getCellType()!=CellType.STRING)
            {
            	throw new IllegalArgumentException("Missing or Invalid Stay Date From in row " + row.getRowNum());
            }
            LocalDate stayDateFrom = LocalDate.parse(stayDateFromCell.getStringCellValue());
            rate.setStayDateFrom(stayDateFrom);
            
            // Stay Date To (Cell 1)
            
            
            Cell stayDateToCell = row.getCell(1);
            if(stayDateToCell==null||stayDateToCell.getCellType()!=CellType.STRING)
            {
            	throw new IllegalArgumentException("Missing or Invalid stay Date To in Row " +row.getRowNum());
            }
            LocalDate stayDateTo = LocalDate.parse(stayDateToCell.getStringCellValue());
            rate.setStayDateTo(stayDateTo);
           

            // Nights (Cell 2)
            
            
            Cell nightsCell = row.getCell(2);
            if(nightsCell ==null||nightsCell.getCellType()!=CellType.NUMERIC)
            {
            	throw new IllegalArgumentException("Missing or Invalid nights in row "+ row.getRowNum());
            }
            int nights = (int) nightsCell.getNumericCellValue();
            rate.setNights(nights);
            
            

            // Value (Cell 3)
            
            Cell valueCell = row.getCell(3);
            if(valueCell==null||valueCell.getCellType()!=CellType.NUMERIC)
            {
            	throw new IllegalArgumentException("Missing or Invalid Value iN row "+ row.getRowNum());
            }
            double value = valueCell.getNumericCellValue();
            rate.setValue(value);
;
            

            // Bungalow ID (Cell 4)
           
            Cell bungalowIdCell = row.getCell(4);
            if(bungalowIdCell==null||bungalowIdCell.getCellType()!=CellType.NUMERIC)
            {
            	throw new IllegalArgumentException("Missing or Invalid bungalowID IN ROW "+row.getRowNum());
            }
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