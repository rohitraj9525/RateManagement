package com.RateManagement.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.RateManagement.Service.*;

import com.RateManagement.Entity.BookingFilter;



@RestController
@RequestMapping("/api/v1/booking")
public class CalculateController 
{
	
	@Autowired
	private CalculateService calculateService;

	
	@PostMapping("/calculate")
	public ResponseEntity<String> calculatePrice(@RequestBody BookingFilter request) 
	{
		if(request.getStartDate().isAfter(request.getEndDate()))
    	{
    		return ResponseEntity.badRequest().body("Invalid stay date. 'startDate' must be before or equal to 'endDate'."); 
    	}
		if(request.getStartDate().isEqual(request.getEndDate()))
		{
			return ResponseEntity.badRequest().body("Inavlid Enter Please Do not enter StartDAte and EndDAte Same");
		}
    	if(request.getStartDate()==null||request.getEndDate()==null)
    	{
    		return ResponseEntity.badRequest().body("Inavlid Stay Dates. 'startDate and EndDate' cannot be null");
    	}
		
		try
		{
			double value = calculateService.calculateTotalValue(request);
			
			return ResponseEntity.ok("value calculated successfully:  "+ value );
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	   	    

	}  
}
	

