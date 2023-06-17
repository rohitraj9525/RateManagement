package com.RateManagement.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
		
		
        List<Rate> calculateOverlap = Repository.findAll(RateSpecification.findOverlappingRates(bookingFilter.getBungalowId(),  bookingFilter.getStartDate(), bookingFilter.getEndDate(), bookingFilter.getNights()));

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
	public long calculateStayDuration(LocalDate startDate, LocalDate endDate) 
	{
        return ChronoUnit.DAYS.between(startDate, endDate);
	}
	
	

	private double calculateValue(BookingFilter bookingFilter, List<Rate> calculateOverlap) 
	{
		double totalValue=0;
		long noOfDays=calculateStayDuration(bookingFilter.getStartDate(), bookingFilter.getEndDate());
		
		for (int i = 0; i < calculateOverlap.size(); i++) 
		{
  	        Rate existingRate = calculateOverlap.get(i);
  	        
  	        //CASE:01  
  	        if(bookingFilter.getStartDate().isAfter(existingRate.getStayDateFrom())&&bookingFilter.getEndDate().isBefore(existingRate.getStayDateTo()))
  	        {
  	        	totalValue+=noOfDays*(existingRate.getValue());
  	        	
  	        	System.out.println("Case:01");
  	        }
  	        
  	        //CASE:02
  	        else if(bookingFilter.getStartDate().isEqual(existingRate.getStayDateFrom())&&bookingFilter.getEndDate().isEqual(existingRate.getStayDateTo()))
  	        {
                totalValue+=noOfDays*(existingRate.getValue());
  	        	
  	        	System.out.println("Case:02");

  	        }
  	        //CASE:03
  	        else if(bookingFilter.getStartDate().isAfter(existingRate.getStayDateFrom())&&bookingFilter.getEndDate().isEqual(existingRate.getStayDateTo()))
  	        {
                totalValue+=noOfDays*(existingRate.getValue());
  	        	
  	        	System.out.println("Case:03");
  	        }
  	        //CASE:04
  	        else if(bookingFilter.getStartDate().isEqual(existingRate.getStayDateFrom())&&bookingFilter.getEndDate().isBefore(existingRate.getStayDateTo()))
  	        {
                totalValue=noOfDays*(existingRate.getValue());
  	        	
  	        	System.out.println("Case:04");
  	        }
  	        //CASE:05
  	        else if(bookingFilter.getStartDate().isAfter(existingRate.getStayDateFrom())&&bookingFilter.getEndDate().isAfter(existingRate.getStayDateTo()))
  	        {
  	        	LocalDate newStartDate = bookingFilter.getStartDate();
  	        	LocalDate newEndDate = bookingFilter.getEndDate();
  	        	long newNoOfDays = calculateStayDuration(newStartDate, newEndDate);
  	        	totalValue+=(newNoOfDays*existingRate.getValue())*1/2;
  	        	//calculate the remaing days with the next rate
  	        	
  	        	LocalDate nextRateStartDate = existingRate.getStayDateTo().plusDays(1);
  	        	LocalDate nextRateEndDate = bookingFilter.getEndDate();
  	        	long remainingNoDays = calculateStayDuration(nextRateStartDate, nextRateEndDate);
  	        	totalValue+=(remainingNoDays*getNextRateValue(bookingFilter.getBungalowId(), nextRateStartDate, nextRateEndDate, bookingFilter.getNights()))*1/2;
  	        	
  	        	
  	        	System.out.println("CASE05");
  	        	
  	        }
  	        
  	        //CASE:06
  	      else if(bookingFilter.getStartDate().isEqual(existingRate.getStayDateFrom())&&bookingFilter.getEndDate().isAfter(existingRate.getStayDateTo()))
	        {
	        	LocalDate newStartDate = bookingFilter.getStartDate();
	        	LocalDate newEndDate = bookingFilter.getEndDate();
	        	long newNoOfDays = calculateStayDuration(newStartDate, newEndDate);
	        	totalValue+=newNoOfDays*existingRate.getValue()*1/2;
	        	//calculate the remaing days with the next rate
	        	
	        	LocalDate nextRateStartDate = existingRate.getStayDateTo().plusDays(1);
	        	LocalDate nextRateEndDate = bookingFilter.getEndDate();
	        	long remainingNoDays = calculateStayDuration(nextRateStartDate, nextRateEndDate);
	        	totalValue+=remainingNoDays*getNextRateValue(bookingFilter.getBungalowId(), nextRateStartDate, nextRateEndDate, bookingFilter.getNights())*1/2;
  	        	System.out.println("CASE06");  	
	        }
	        	//CASE07:

  	        
          else if (bookingFilter.getStartDate().isEqual(existingRate.getStayDateFrom())
                  && bookingFilter.getEndDate().isAfter(existingRate.getStayDateTo())) {
              Rate nextRate = Repository.findNextRate(bookingFilter.getBungalowId(), existingRate.getStayDateTo(),
                      bookingFilter.getEndDate(), bookingFilter.getNights());
              if (nextRate != null) {
                  long daysUntilNextRate = calculateStayDuration(existingRate.getStayDateTo(), nextRate.getStayDateFrom());
                  totalValue = (noOfDays - daysUntilNextRate) * existingRate.getValue()
                          + calculateValue(bookingFilter, List.of(nextRate)); 
                  System.out.println("CASE07");

  	        
              }
          }


  	        
		}
		return totalValue;		
		
	}

	private double getNextRateValue(long bungalowId, LocalDate startDate, LocalDate endDate, int nights) {
		// TODO Auto-generated method stub
		Rate nextRate = Repository.findNextRate(bungalowId, startDate, endDate, nights);
		if(nextRate!=null)
		{
			return nextRate.getValue();
		}
		throw new IllegalArgumentException("No next rate is available for the specified booking details");
	}
	
	
}
