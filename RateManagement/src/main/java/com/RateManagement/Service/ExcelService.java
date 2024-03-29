package com.RateManagement.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
	private RateService rateService;
	  
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
	  
      public List<Rate> importRatesFromExcel(MultipartFile file) throws IOException {
          List<Rate> rates = UploadHelper.excelToRates(file.getInputStream());
          rates.stream().forEach((rate)->{rateService.createRate(rate);});
          return rates;
      }     }
