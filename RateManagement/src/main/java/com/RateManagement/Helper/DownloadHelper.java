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

public class DownloadHelper 
{
	public static String Type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	
	  static String[] HEADERs = { "RATE_ID", "STAY_DATE_FROM", "STAY_DATE_TO", "NIGHTS", "VALUE", "BUNGALOW_ID", "CLOSED_DATE" };
	  
	  static String SHEET = "RatesDownload";
	  
	  public static ByteArrayInputStream rateToExcel(List<Rate> rate) 
	  {
		  
		  try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
		      Sheet sheet = workbook.createSheet(SHEET);
		      
		   // Header
		      Row headerRow = sheet.createRow(0);
		      
		      for (int col = 0; col < HEADERs.length; col++) 
		      {
		          Cell cell = headerRow.createCell(col);
		          cell.setCellValue(HEADERs[col]);
		      }
		      
		      int rowIdx = 1;
		      for (Rate rates : rate) 
		      {
		        Row row = sheet.createRow(rowIdx++);
		        
		        row.createCell(0).setCellValue(rates.getRateId());
		        row.createCell(1).setCellValue(rates.getStayDateFrom().toString());
		        row.createCell(2).setCellValue(rates.getStayDateTo().toString());
		        row.createCell(3).setCellValue(rates.getNights());
		        row.createCell(4).setCellValue(rates.getValue());
		        row.createCell(5).setCellValue(rates.getBungalowId());
		        row.createCell(6).setCellValue(rates.getClosedDate()!=null? rates.getClosedDate().toString(): " ");
		      }

		  
		      workbook.write(out);
		      return new ByteArrayInputStream(out.toByteArray());
		      
		    } 
		  
		  catch (IOException e) 
		  
		  {
		      throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		  }
	}
}