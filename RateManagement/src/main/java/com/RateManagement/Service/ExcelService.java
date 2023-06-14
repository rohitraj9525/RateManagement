package com.RateManagement.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.RateManagement.Entity.Rate;
import com.RateManagement.Helper.DownloadHelper;
import com.RateManagement.Helper.UploadHelper;
import com.RateManagement.Repo.RateRepository;
import com.RateManagement.Specification.ExcelSpecification;

@Service
public class ExcelService 
{
	private static RateRepository repository;
	  
      @Autowired
	  public ExcelService(RateRepository repository)
	  {
		  this.repository = repository;
	  }

	  public ByteArrayInputStream load() {
	    List<Rate> rateExcel = repository.findAll();

	    ByteArrayInputStream in = DownloadHelper.rateToExcel(rateExcel);
	    return in;
	  }
      
      public ByteArrayInputStream exportFilteredRates(
    	        Long bungalowId,
    	        Integer nights,
    	        String stayDateFrom,
    	        String stayDateTo,
    	        Double value,
    	        String closedDate
    	) {
    	    List<Rate> filteredRates = repository.findAll(
    	            ExcelSpecification.filterByCriteria(bungalowId, nights, stayDateFrom, stayDateTo, value,closedDate)
    	    );
    	    return DownloadHelper.rateToExcel(filteredRates);
    	}	  
	  
	  public static void importRatesFromExcel(MultipartFile file)
	  {
		  try {
	            // Check if the file has Excel format
	            if (UploadHelper.hasExcelFormat(file)) {
	                InputStream is = file.getInputStream();

	                // Convert Excel data to Rate objects
	                List<Rate> rates = UploadHelper.excelToRate(is);

	                // Save the rates to the repository
	                repository.saveAll(rates);
	            } else {
	                throw new RuntimeException("Invalid file format. Only Excel files are supported.");
	            }
	        } catch (IOException e) {
	            throw new RuntimeException("Failed to import rates from excel: " + e.getMessage());
	        }
	    }
	}