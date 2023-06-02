package com.RateManagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import com.RateManagement.Entity.*;
import com.RateManagement.Service.RateService;

@RestController
public class MyController 
{
//	@GetMapping("/home")
//	public String home()
//	{
//		return "my name is rohit";
//	}
	// Get all rates
	@Autowired
	private RateService rateService;
	
	@GetMapping("/rates")
	public List<Rate> getRate()
	{
		return this.rateService.getRate();
		
	}

}
