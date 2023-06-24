package com.RateManagement.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.RateManagement.Entity.BookingFilter;
import com.RateManagement.Entity.Rate;
import com.RateManagement.Repo.RateRepository;
import com.RateManagement.Specification.RateSpecification;


@Service
public class CalculateService 
{
	@Autowired
    private final RateRepository Repository;

	public CalculateService(RateRepository Repository) {
		super();
		this.Repository = Repository;
	}
	
	public double calculateTotalValue(BookingFilter bookingFilter)
	{
		
		
        List<Rate> calculateOverlap = Repository.findAll(RateSpecification.findOverlappingCalculate(bookingFilter.getBungalowId(),  bookingFilter.getStartDate(), bookingFilter.getEndDate()),
        		Sort.by("stayDateFrom"));

        System.out.println(calculateOverlap);
        if(calculateOverlap.isEmpty())
        {
        	throw new IllegalArgumentException("No rates available for the specified booking details");
        }
        else
        {
        	double value = calculateValue(bookingFilter, calculateOverlap);
        	return value;
        }
        		
	}
	/**
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public long calculateStayDuration(LocalDate startDate, LocalDate endDate) 
	{
        return ChronoUnit.DAYS.between(startDate, endDate);
	}
	
	

	/**
	 * @param bookingFilter
	 * @param calculateOverlap
	 * @return
	 */
	private double calculateValue(BookingFilter bookingFilter, List<Rate> calculateOverlap) 
	{
		double totalValue=0;
		int count = 0;
		long noOfDays=calculateStayDuration(bookingFilter.getStartDate(), bookingFilter.getEndDate());
		

		LocalDate currentDate = bookingFilter.getStartDate();
		while(currentDate.isBefore(bookingFilter.getEndDate()) )
		{
			System.out.println("current date : "+currentDate);
			List<Rate> list = new ArrayList<>();
			++count;
			for(Rate rate: calculateOverlap)
			{
				if(currentDate.isBefore(rate.getStayDateTo())&&currentDate.isAfter(rate.getStayDateFrom()))
				{
					list.add(rate);
					System.out.println("CASE:01");
				}
				else if(currentDate.isEqual(rate.getStayDateFrom())&&currentDate.isBefore(rate.getStayDateTo()))
				{
					list.add(rate);
					System.out.println("CASE:02");

				}
				else if(currentDate.isEqual(rate.getStayDateFrom())&&currentDate.isEqual(rate.getStayDateTo()))
				{
					list.add(rate);
					System.out.println("CASE:03");

				}
				else if(currentDate.isAfter(rate.getStayDateFrom())&&currentDate.isEqual(rate.getStayDateTo()))
				{
					list.add(rate);
					System.out.println("CASE:04");

				}
				
			}
			boolean rateFound = false;
			
			System.out.println("--------------------------------");
			Collections.sort(list,Comparator.comparingDouble(r-> r.getValue()/r.getNights()));
			System.out.println(list.size());
			for(Rate rate : list) 
			{
							
				Rate minvalue=list.get(0);
				LocalDate nextDate = currentDate.plusDays(1);
				
				if((nextDate.isBefore(rate.getStayDateTo())||nextDate.isEqual(rate.getStayDateTo())||nextDate.equals(rate.getStayDateTo().plusDays(1)))&&(!nextDate.isAfter(bookingFilter.getEndDate())))
				{
					totalValue=totalValue+minvalue.getValue()/minvalue.getNights();
					System.out.println(totalValue);
					currentDate = nextDate;
					
					rateFound = true;
					break;
				}
				
				
//				for(int i=0;i<list.size();i++)
//				{
//					
//					Rate minvalue = list.get(i);
//					LocalDate nextDate = currentDate.plusDays(minvalue.getNights());
////					
//					if((nextDate.isBefore(rate.getStayDateTo())||nextDate.isEqual(rate.getStayDateTo())||nextDate.equals(rate.getStayDateTo().plusDays(1)))&&(!nextDate.isAfter(bookingFilter.getEndDate())))
//					{
//						
//						
//						totalValue=totalValue+minvalue.getValue();
//						System.out.println(totalValue);
//						currentDate = nextDate;
//						noOfDays=noOfDays-minvalue.getNights();
////						
////						totalValue=totalValue+(minvalue.getValue()/minvalue.getNights())*noOfDays;
//						System.out.println(noOfDays);
//						rateFound = true;
//						break;
//				}
//				}
			}
			if(!rateFound) 
			{
				break;
			}
			
		}
		
		if(currentDate.isBefore(bookingFilter.getEndDate()))
		{
			throw new IllegalArgumentException("No sufficient rate is available for the specified booking duration ");
		}
		
		
		//System.out.println("Total value  "+totalValue);
		return totalValue;		
		
			
	//CalculateValue
		
	}	 
	
	
	
	//CalculateService
}
