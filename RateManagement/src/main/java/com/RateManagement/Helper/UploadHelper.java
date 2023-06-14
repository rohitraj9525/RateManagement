package com.RateManagement.Helper;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.*;

import org.apache.commons.compress.archivers.dump.DumpArchiveEntry.TYPE;
import org.springframework.web.multipart.MultipartFile;

import com.RateManagement.Entity.Rate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UploadHelper 
{
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static String[] HEADERs = {"RATE_ID", "STAY_DATE_FROM", "STAY_DATE_TO", "NIGHTS", "VALUES", "BUNGALOW_ID", "CLOSED_DATE"};
    static String SHEET = "Data";
	
	public static boolean hasExcelFormat(MultipartFile file)
	{
			if(!TYPE.equals(file.getContentType()))
			{
				return false;
			}
			// TODO Auto-generated catch block
		return true;
		
	}
	
	public static List<Rate> excelToRate(InputStream is)
	{
		
		try
		{
			Workbook workbook = new XSSFWorkbook(is);
			Sheet sheet = workbook.getSheet(SHEET);
			Iterator<Row> rows = sheet.iterator();
			
			List<Rate> rates = new ArrayList<Rate>();
			
			int rowNumber=0;
			while(rows.hasNext())
			{
				Row currentRow = rows.next();
				
				//skip header
				if(rowNumber==0)
				{
					rowNumber++;
					continue;
				}
				
				Iterator<Cell> cellsInRow= currentRow.iterator();
				Rate rate = new Rate();
				
				int cellIdx = 0;
				
				while(cellsInRow.hasNext())
				{
					Cell currenCell = cellsInRow.next();
					
					switch(cellIdx)
					{
//					case 0:
//						rate.setRateId((long) currenCell.getNumericCellValue());
//						break;
//						
						
					case 1:
						rate.setStayDateFrom(currenCell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
						break;
                    case 2:
                        rate.setStayDateTo(currenCell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        break;

                    case 3:
                        rate.setNights((int) currenCell.getNumericCellValue());
                        break;

                    case 4:
                        rate.setValue((int)currenCell.getNumericCellValue());
                        break;

                    case 5:
                        rate.setBungalowId((long) currenCell.getNumericCellValue());
                        break;
                    case 6:
                    	boolean closed = currenCell.getBooleanCellValue();
                    	rate.setClosedDate(closed ? LocalDate.now(): null);

                    default:
                        break;
                }

                cellIdx++;
            }

            rates.add(rate);
        }

        workbook.close();
						
		return rates;
		} catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }
}
