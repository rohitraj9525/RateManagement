package com.RateManagement.Service;

import java.time.LocalDate;
import java.util.*;

import org.springframework.stereotype.Service;

import com.RateManagement.Entity.Bungalow;
import com.RateManagement.Entity.Rate;

@Service
public class RateServiceImpl implements RateService {
	
	List<Rate> rate;
	

	
	public RateServiceImpl() {
		
		// TODO Auto-generated constructor stub
		rate=new ArrayList<>();
		rate.add(new Rate(LocalDate.parse("2023-06-13"),1,4000,LocalDate.parse("2023-06-17"),null, new Bungalow("Silver Heaven","Luxary")));
		rate.add(new Rate(LocalDate.parse("2023-06-17"),1,4000,LocalDate.parse("2023-06-20"),null,new Bungalow("Silver Heaven","simple")));
	}


	@Override
	public List<Rate> getRate() {
		// TODO Auto-generated method stub
		return rate;
	}

	

}
