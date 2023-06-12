package com.RateManagement.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.RateManagement.DTO.RateDTO;
import com.RateManagement.Entity.Bungalow;
import com.RateManagement.Entity.Rate;
import com.RateManagement.Exception.BungalowNotFoundException;
import com.RateManagement.Exception.DuplicateRateException;
import com.RateManagement.Exception.RateNotFoundException;
import com.RateManagement.Repo.BungalowRepository;
//import com.RateManagement.Repo.BungalowRepository;
import com.RateManagement.Repo.RateRepository;
import com.RateManagement.Specification.RateSpecification;
//import java.util.*;
import java.util.*;

//

//import java.io.ByteArrayInputStream;

//import com.RateManagement.Helper.*;





@Service
public class RateServiceImpl implements RateService {

	@Autowired
    private final RateRepository rateRepository;

	@Autowired
	private BungalowService bungalowService;
	
	@Autowired
    public RateServiceImpl(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    /**
     * @param rate
     * @return this method return save a rate Raw in the rate table
     */
    @Override
    public Rate createRate(Rate rate) 
 {
    	
    	//checking the bungalowid which is for rate is going to add in the rate table is already present or not
    	// if it is not present then new rate will not be add in the rate table..
    	
    	
    	Bungalow bungalow = bungalowService.getBungalowById(rate.getBungalowId());
    	if(bungalow==null) 
    	{
    		throw new BungalowNotFoundException("Bungalow with ID " + rate.getBungalowId() + " not found..!");
    	}
    	
    	//.........for  check the duplicate entry in the rate table...
    	
    	
    	 Rate existingRates=rateRepository.findByFields(rate.getStayDateFrom(), rate.getStayDateTo(), rate.getNights(), rate.getValue(), rate.getBungalowId());    
         
         if(existingRates!=null&&!existingRates.getRateId().equals(rate.getRateId()))
         {
         	throw new DuplicateRateException("");
         }
    	  	
         //empty list
         
         
         
         List<Rate> updatedRates = new ArrayList<>();

         // Retrieve overlapping rates
         
         List<Rate> overlappingRates = rateRepository.findOverlappingRates(rate.getBungalowId(), rate.getStayDateFrom(), rate.getStayDateTo(), rate.getNights());

         if (overlappingRates.isEmpty()) 
           {
               return rateRepository.save(rate);
           } 
           
           else 
          	 
           {
               split(rate, overlappingRates);
               overlappingRates = rateRepository.findOverlappingRates(rate.getBungalowId(), rate.getStayDateFrom(), rate.getStayDateTo(), rate.getNights());
               System.out.println(rate);
               for(Rate temp : overlappingRates){
            	   System.out.println(temp);
               }
               merge(rate, overlappingRates);
               return rate;
           }
       }
    
//MERGE OF THE CREATE RATE   //method for merging the rate 
    
  private void merge(Rate rate, List<Rate> overlappingRates) 
  {
		// TODO Auto-generated method stub
	  
	  for(Rate existingRate : overlappingRates)
	  {
		  //check if the rate has the same nights and bungalowID
		  if(existingRate.getNights()==rate.getNights()&&existingRate.getBungalowId()==rate.getBungalowId()&&existingRate.getValue()==rate.getValue()&&existingRate.getClosedDate()==null)
		  {
			  //CASE:01- check if the new rate completely overlaps the existing rate and has the same value
			  
			  if(rate.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&rate.getStayDateTo().isAfter(existingRate.getStayDateTo()))
			  {
				  
				  //save of the new rate
				  
				  rateRepository.save(rate);
				  
				  //closed date for the existing rate
				  
				  existingRate.setClosedDate(LocalDate.now());
				  
				  //save in the rate table
				  
				  rateRepository.save(existingRate);
         		 System.out.println("case 01 merging");

				  
				  
			  }
			  
			  //CASE:02- check if the existing rate has the same stay dates as the new rate and has the same values
			  
//			  else if(rate.getStayDateFrom().isEqual(existingRate.getStayDateFrom())&&rate.getStayDateTo().isEqual(existingRate.getStayDateTo()))
//			  {
//				  //save of the new rate
//				  
//				  rateRepository.save(rate);
//				  
//				  //closed of the existing rate 
//				  
//				  existingRate.setClosedDate(LocalDate.now());
//				  
//				  //save in the rate table
//				  
//				  rateRepository.save(existingRate);
//	         		 System.out.println("case 02 merging");
//
//			  }
			  
			  //CASE:03- check if the existing rate ends exactly at the start of the new rate
			  
			  else if(rate.getStayDateFrom().isEqual(existingRate.getStayDateTo())&&rate.getStayDateTo().isAfter(existingRate.getStayDateTo()))
			  {
				  
				  //merge the existing rate with the new rate by updating the stay date to of existing rate
				  //existingRate.setStayDateTo(rate.getStayDateTo());
				  rate.setStayDateFrom(existingRate.getStayDateFrom());
			  
			  //save the new rate
			  
			  rateRepository.save(rate);
			  
			  //closed date to local date now of the existing rate
			  
			  existingRate.setClosedDate(LocalDate.now());
			  
			  //save the existing rate to the rate Repo
			  
			  rateRepository.save(existingRate);
      		 System.out.println("case 03 merging");

			  
			  }
			  
			  //CASE:04- check if the new rate stay date to is equal to the stay date from	of the existing rate
			  
			  else if(rate.getStayDateTo().isEqual(existingRate.getStayDateFrom()))
			  {
				  //merge the existing rate with the new rate
				  
				  rate.setStayDateTo(existingRate.getStayDateTo());
				  
				  //save the new rate
				  
				  rateRepository.save(rate);
				  
				  //closed date to the local date now to the existing 
				  
				  existingRate.setClosedDate(LocalDate.now());
				  
				  //save the closed date of the NOTNULL to the rate table
				  
				  rateRepository.save(existingRate);
	         		 System.out.println("case 04 merging");

				  
			  }
			  
			  //CASE: 05-- if the stay date from of new rate lies in between existing rate but stay date to of new rate lies
			  // after the stay date to of existing rate
			  
			  else if(rate.getStayDateFrom().isBefore(existingRate.getStayDateTo())&&rate.getStayDateTo().isAfter(existingRate.getStayDateTo())&&rate.getStayDateFrom().isAfter(existingRate.getStayDateFrom()))
			  {
				  //merge the stay date to of existing to the stay date to of new rate
				  
				  //existingRate.setStayDateTo(rate.getStayDateTo());
				  rate.setStayDateFrom(existingRate.getStayDateFrom());
				  
				  
				  //save the new rate
				  
				  rateRepository.save(rate);
				  
				  //set closed date of exustung rate to localdate now
				  
				  existingRate.setClosedDate(LocalDate.now());
				  
				  //save the existing not null rate
				  
				  rateRepository.save(existingRate);
	         		 System.out.println("case 05 merging");

				  
				  
			  }
			  
			  //CASE 06:- if the stay date from of new rate is before the stay date from of existing rate nut the stay date 
			  // to of the new rate is after the stay date from of existing rate but before from the stay date to of existing rate
			  
			  else if(rate.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&rate.getStayDateTo().isAfter(existingRate.getStayDateFrom())&&rate.getStayDateTo().isBefore(existingRate.getStayDateTo()))
			  {
				  //merge of the existing rate with the new rate
				  
				  //existingRate.setStayDateFrom(rate.getStayDateFrom());
				  
				  rate.setStayDateTo(existingRate.getStayDateTo());
				  
				  //save the new rate
				  
				  rateRepository.save(rate);
				  
				  //set closed date of existing to the inactive
				  
				  existingRate.setClosedDate(LocalDate.now());
				  
				  //save the inactive exisitng rate
				  
				  rateRepository.save(existingRate);
	         		 System.out.println("case 06 merging");

			  }
			  
			  //CASE 07:- complete overlaps that mean the new rate completely inside the existing rate
			  
			  else if(rate.getStayDateFrom().isAfter(existingRate.getStayDateFrom())&&rate.getStayDateTo().isBefore(existingRate.getStayDateTo()))
			  {
				  //merge the new rate with existing rate
				  
				  rate.setStayDateFrom(existingRate.getStayDateFrom());
				  rate.setStayDateTo(existingRate.getStayDateTo());
				  
				  //save the new rate
				  
				  rateRepository.save(rate);
				  
				  //set closed date of existing to local date now
				  
				  existingRate.setClosedDate(LocalDate.now());
				  
				  //save the existing rate
				  
				  rateRepository.save(existingRate);
	         		 System.out.println("case 07 merging");

			  }
			  
			  //CASE 08:-if the existing rate lies in between the new rate
			  
			  else if(rate.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&rate.getStayDateTo().isAfter(existingRate.getStayDateTo()))
			  {
				  // save the all rate
				  //Rate temp = new Rate(rate.getStayDateFrom(), rate.getStayDateTo(), rate.getNights(), rate.getValue(), rate.getBungalowId());
				  rateRepository.save(rate);
				  
				  //set closed date of existing rate to inactive
				  
				  existingRate.setClosedDate(LocalDate.now());
				  
				  //save the existing rate
				  
				  rateRepository.save(existingRate);
	         		 System.out.println("case 08 merging");

			  }
			  
			  //CASE 09:-   if the stay date from of of new rate is just a day after of stay date to of existing rate but the stay date to new 
			  //rate is after the stay date to of existing rate and the stay date from of the existing rate is before of stay date from of stay date from
			  
			  else if(rate.getStayDateFrom().equals(existingRate.getStayDateTo().plusDays(1)))
					  
			  {
				  
				  existingRate.setClosedDate(LocalDate.now());
				  rateRepository.save(existingRate);
				  if(rate.getRateId() == null) {
					  rate.setStayDateFrom(existingRate.getStayDateFrom());
					  rateRepository.save(rate);
				  }
				  else {
					  rate.setClosedDate(LocalDate.now());
					  
					  rateRepository.save(rate);
					  Rate rateAfter=new Rate(existingRate.getStayDateFrom(),rate.getStayDateTo(),rate.getNights(),rate.getValue(),rate.getBungalowId());
					  rate = rateRepository.save(rateAfter);
					  
				  }
				  
          		
			  }
			  
			  //CASE 10:  
			  else if (rate.getStayDateTo().equals(existingRate.getStayDateFrom().minusDays(1))) 
			  {
				  existingRate.setClosedDate(LocalDate.now());
				  
				  rateRepository.save(existingRate);
				  if(rate.getRateId() == null) {
					  rate.setStayDateTo(existingRate.getStayDateTo());	
					  rateRepository.save(rate);
				  }
				  else 
				  {
					  rate.setClosedDate(LocalDate.now());
					  
					  rateRepository.save(rate);
					  Rate rateAfter=new Rate(rate.getStayDateFrom(),existingRate.getStayDateTo(),rate.getNights(),rate.getValue(),rate.getBungalowId());
					  rate = rateRepository.save(rateAfter);					  
				  }				  
			  }
			  
			  
			  //
			  
			  
		  }
	  }
		
  }

              // End of the merging method
         
//SPLIT OF CREATE RATE // method for the split of the table
  
    private void split(Rate rate, List<Rate> overlappingRates) 
    
 {   //.....start

         for (Rate existingRate : overlappingRates) 
         {
        	 // Check if the rate has the same nights and bungalowId
        	 
             if (existingRate.getNights() == rate.getNights() && existingRate.getBungalowId() == rate.getBungalowId()&&existingRate.getClosedDate()==null)
             {
            	 
            	 //CASE: 01 This condition checks if the stayDateFrom of the new rate is after the 
//            	 stayDateFrom of the existing rate and if the stayDateTo of the new rate is before the 
//            	 stayDateTo of the existing rate. This indicates that the new rate is completely inside 
//            	 the existing rate
            	 
            	 if(rate.getStayDateFrom().isAfter(existingRate.getStayDateFrom())&&rate.getStayDateTo().isBefore(existingRate.getStayDateTo()))
            	 {
            		 //This line creates a new beforeRate object by taking the stayDateFrom of the existing rate 
//            		 as the stayDateFrom, and the day before the stayDateFrom of the new rate as the stayDateTo. 
//            		 The remaining properties such as nights, value, and bungalowId are copied from the 
//            		 existing rate.
            		 
            		 //here rate representing the new rate 
            		 
            		 Rate beforeRate = new Rate(existingRate.getStayDateFrom(), rate.getStayDateFrom().minusDays(1),existingRate.getNights(), existingRate.getValue(), existingRate.getBungalowId());
            		 
//            		 This line creates a new afterRate object by taking the day after the stayDateTo
//            		 of the new rate as the stayDateFrom, and the stayDateTo of the existing rate as the
//            		 stayDateTo. The remaining properties are copied from the existing rate.
            		 
            		 Rate afterRate = new Rate(rate.getStayDateTo().plusDays(1), existingRate.getStayDateTo(), existingRate.getNights(), existingRate.getValue(), existingRate.getBungalowId());
            		 
//            		 These lines add the beforeRate, rate (new rate), and afterRate to the updatedRates list. 
//            		 This effectively splits the existing rate into three parts: before the new rate, 
//            		 the new rate itself, and after the new rate.
            		 
            		 rateRepository.save(beforeRate);
            		 rateRepository.save(rate);
            		 rateRepository.save(afterRate);
            		 
            		 System.out.println("Case01 of splitting");
            		 
//            		 This line sets the closedDate of the existing rate to the current date, 
//            		 indicating that it is closed. The LocalDate.now() function returns the current date.
            		 
            		 existingRate.setClosedDate(LocalDate.now());
            		 
//            		 This line adds the existing rate with the updated closedDate to the updatedRates list.
            		 
            		 rateRepository.save(existingRate);
            		 
//            		 Overall, this code splits the existing rate into two parts 
//            		 (before and after the new rate), adds the new rate, and updates 
//            		 the existing rate by setting the closedDate.
            		             		             		 
            	 }
            	 
 //           	 CASE:02-  condition checks if the stayDateFrom of the new rate is after the 
//              	 stayDateFrom of the existing rate and if the stayDateTo of the new rate is equal the 
//              	 stayDateTo of the existing rate. 
//            	 
            	 else if(rate.getStayDateFrom().isAfter(existingRate.getStayDateFrom())&&rate.getStayDateTo().isEqual(existingRate.getStayDateTo()))
            	 {
            		//split the existing rate into two: the preceding part and after the new rate
            		 
            		 Rate precedingRate = new Rate(existingRate.getStayDateFrom(), rate.getStayDateFrom().minusDays(1), existingRate.getNights(), existingRate.getValue(), existingRate.getBungalowId());
            		 
            		 //Rate afterRate = new Rate(rate.getStayDateFrom(), rate.getStayDateTo(), rate.getNights(), rate.getValue(), rate.getBungalowId());
            		 
            		 rateRepository.save(precedingRate);
            		 //rateRepository.save(afterRate);
            		 rateRepository.save(rate);
            		 
            		 //close the existing rate
            		 
            		 existingRate.setClosedDate(LocalDate.now());
            		 
            		 rateRepository.save(existingRate);
            		 System.out.println("case 02 splitting");
            		 			 
            	 }
            	 
//            	 CASE:03-  condition checks if the stayDateFrom of the new rate is equal the 
//          	 stayDateFrom of the existing rate and if the stayDateTo of the new rate is before the 
//          	 stayDateTo of the existing rate.
            	 
            	 else if(rate.getStayDateFrom().isEqual(existingRate.getStayDateFrom())&&rate.getStayDateTo().isBefore(existingRate.getStayDateTo()))
            	 {
            		 //split the existing rate into two 
            		 
            		 Rate preceeding = new Rate(rate.getStayDateTo().plusDays(1),existingRate.getStayDateTo(), existingRate.getNights(), existingRate.getValue(), existingRate.getBungalowId());
            	     
            		 
            		 //save in the database
            		 
            		 rateRepository.save(rate);
            		 rateRepository.save(preceeding);
            		 
            		 //close the existing rates
            		 
            		 existingRate.setClosedDate(LocalDate.now());
            		 
            		 //save the closed date rate in the rate table
            		 
            		 rateRepository.save(existingRate);
            		 System.out.println("case 03 splitting");

            	 }
            	 
            	// CASE:04-  condition checks if the stayDateFrom of the new rate is equal the 
//          	 stayDateFrom of the existing rate and if the stayDateTo of the new rate is equal the 
//          	 stayDateTo of the existing rate.
            	 
            	 else if(rate.getStayDateFrom().isEqual(existingRate.getStayDateFrom())&&rate.getStayDateTo().isEqual(existingRate.getStayDateTo()))
            	 {
            		 //new rate save in the table
            		 
            		 rateRepository.save(rate);
            		 
            		 //close the existing rate
            		 
            		 existingRate.setClosedDate(LocalDate.now());
            		 
            		 //save the closed date rate in the rate table
            		 
            		 rateRepository.save(existingRate);
            		 
            		 System.out.println("case 04 splitting");

            	 }
            	 
            	 //Case:05- condition checks if the stayDateFrom of the new rate is equal the 
//          	 stayDateTo of the new rate and lies in between existing rate
            	 
            	 else if(rate.getStayDateFrom().isEqual(rate.getStayDateTo())&&rate.getStayDateFrom().isAfter(existingRate.getStayDateFrom())&&rate.getStayDateTo().isBefore(existingRate.getStayDateTo()))
            		 
            	 {
            		 //split the given table into three
            		 
            		 Rate rateBefore = new Rate(existingRate.getStayDateFrom(), rate.getStayDateFrom().minusDays(1), existingRate.getNights(), existingRate.getValue(),existingRate.getBungalowId());
            		 Rate rateAfter = new Rate(rate.getStayDateTo().plusDays(1),existingRate.getStayDateTo(), existingRate.getNights(), existingRate.getValue(), existingRate.getBungalowId());
            		 
            		 //save rates in the rate repo
            		 
            		 rateRepository.save(rateBefore);
            		 rateRepository.save(rateAfter);
            		 rateRepository.save(rate);
            		 
            		 //set closed date to now
            		 
            		 existingRate.setClosedDate(LocalDate.now());
            		 
            		 //save the inactive rate
            		 
            		 rateRepository.save(existingRate);
            		 System.out.println("case 05 splitting");

            		 
            		 
            	 }
            	 
            	 //CASE: 06- condition checks if the stayDateFrom of the new rate is equal the 
//          	 stayDateFrom of the existing rate and if the stayDateTo of the new rate is after the 
//          	 stayDateTo of the existing rate.
            	 
            	 else if(rate.getStayDateFrom().isEqual(existingRate.getStayDateFrom())&&rate.getStayDateTo().isAfter(existingRate.getStayDateTo()))
            	 {
            		 //save the new entry
            		 rateRepository.save(rate);
            		 
            		 //closed date to not null of existing rate
            		 
            		 existingRate.setClosedDate(LocalDate.now());
            		 
            		 //save the not null existing rate
            		 
            		 rateRepository.save(existingRate);
            		 System.out.println("case 06 splitting");

            		 
            	 }
            	 
            	//CASE: 07- condition checks if the stayDateFrom of the new rate is before the 
//          	 stayDateFrom of the existing rate and if the stayDateTo of the new rate is equal the 
//          	 stayDateTo of the existing rate
            	 
            	 else if(rate.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&rate.getStayDateTo().isEqual(existingRate.getStayDateTo()))
            	 {
            		 //save the new rate
            		 rateRepository.save(rate);
            		 
            		 //set closed rate to not null
            		 
            		 existingRate.setClosedDate(LocalDate.now());
            		 
            		 //save the inactive existing rate
            		 
            		 rateRepository.save(existingRate);
            		 System.out.println("case 07 splitting");

            	 }
            	 
            	 //CASE:08- replace all null with notnull if the all existing rate come in between the new rate
            	 
            	 
            	 else if(isExistingRateNewRate(existingRate, rate))
            	 {
            		 System.out.println("case 8");
            		 //rateRepository.save(rate);
            		 existingRate.setClosedDate(LocalDate.now());
            		 
            		 rateRepository.save(existingRate);
            		 rateRepository.save(rate);
            	 }
//            	 else if(rate.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&rate.getStayDateTo().isAfter(existingRate.getStayDateTo()))
//            	 {
//            		 //save the new rate
//            		 
//            		 Rate rateAfter=new Rate(existingRate.se)
//            		 
//            		 rateRepository.save(rate);
//            		 
//            		 //set now of closed date of existing rate
//            		 if(existingRate.getClosedDate()==null)
//            		 {
//            		 existingRate.setClosedDate(LocalDate.now());
//            		 
//            		 //save of the existing inactive rate
//            		 
//            		 rateRepository.save(existingRate);
//            		 }
//            		 
//            	 }
            	 //CASE 09:- complete inside the existing rate in the new rate in that all the closed date should be CLosedDATE be INACTIVE
            	 
            	 //CASE 09: if the new rate stay date from is before of the existing rate stay date from 
            	 //and stay date to of new rate is before stay date to of existing rate
            	 
            	 else if(rate.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&rate.getStayDateTo().isBefore(existingRate.getStayDateTo())&&rate.getStayDateTo().isAfter(existingRate.getStayDateFrom()))
            	 {
            		 Rate rateAfter=new Rate(rate.getStayDateTo().plusDays(1),existingRate.getStayDateTo(),existingRate.getNights(),existingRate.getValue(),existingRate.getBungalowId());
            		 rateRepository.save(rateAfter);
            		 rateRepository.save(rate);
            		 existingRate.setClosedDate(LocalDate.now());
            		 rateRepository.save(existingRate);
            		 System.out.println("Case 09 of splitting in create");
            		 
            		 
            	 }
            	 
            	 //CASE 10:- if the new rate of stay date from is after the stay date from of existing rate but stay date to of new rate is after the stay date to of
            	 //of existing rate stay date to 
            	 
            	 else if(rate.getStayDateFrom().isAfter(existingRate.getStayDateFrom())&&rate.getStayDateTo().isAfter(existingRate.getStayDateTo())&&rate.getStayDateFrom().isBefore(existingRate.getStayDateTo())) 
            	 {
            		 Rate rateBefore=new Rate(existingRate.getStayDateFrom(),rate.getStayDateFrom().minusDays(0),existingRate.getNights(),existingRate.getValue(),existingRate.getBungalowId());
            		 
            		 rateRepository.save(rateBefore);
            		 rateRepository.save(rate);
            		 existingRate.setClosedDate(LocalDate.now());
            		 
            		 rateRepository.save(existingRate);
            		 
            		 System.out.println("Case 10 of splitting in create");
            	 }
             }
         }        
    }   
    
    
    private boolean isExistingRateNewRate(Rate existingRate, Rate rate)
    {
    	return rate.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&rate.getStayDateTo().isAfter(existingRate.getStayDateTo());
    }
 //.............End of the splitting of the rate table
    
    //
         


    	//.............

    /**
     *@param id
     *@param rate
     *@return this return update the particular row
     */ 
    @Override
    public Rate updateRate(Long id, Rate rate) {
//        Rate existingRate = getRateById(id);
//        existingRate.setStayDateFrom(rate.getStayDateFrom());
//        existingRate.setStayDateTo(rate.getStayDateTo());
//        existingRate.setNights(rate.getNights());
//        existingRate.setValue(rate.getValue());
//        existingRate.setClosedDate(rate.getClosedDate());
//        existingRate.setBungalowId(rate.getBungalowId());
//        
        
        //......................................................
        
        //Splitting and merging logic
        
        List<Rate> overlappingRates = rateRepository.findOverlappingRates(rate.getBungalowId(), rate.getStayDateFrom(), rate.getStayDateTo(), rate.getNights());

        if (overlappingRates.isEmpty()) 
          {
              return rateRepository.save(rate);
          } 
          
          else 
         	 
          {
              splitUpdate(rate, overlappingRates);
              overlappingRates = rateRepository.findOverlappingRates(rate.getBungalowId(), rate.getStayDateFrom(), rate.getStayDateTo(), rate.getNights());

              mergeUpdate(rate, overlappingRates);
              return rate;
          }
      }
   
        
//SPLITING OF THE UPDATE  // logic for splitting the rate table while updating the rate table by rateID      
        
        private void splitUpdate(Rate updateRate, List<Rate> overlappingRates) 
        {
		// TODO Auto-generated method stub
        	
            for (Rate existingRate : overlappingRates) 
            {
            	if(existingRate.getBungalowId()==updateRate.getBungalowId()&&existingRate.getNights()==updateRate.getNights())
            	{
            		if(updateRate.getStayDateFrom().isAfter(existingRate.getStayDateFrom())&&updateRate.getStayDateTo().isBefore(existingRate.getStayDateTo()))
            		{
            			//CASE: 01 This condition checks if the stayDateFrom of the new rate is after the 
//                   	 stayDateFrom of the existing rate and if the stayDateTo of the new rate is before the 
//                   	 stayDateTo of the existing rate. This indicates that the new rate is completely inside 
//                   	 the existing rate
                   	 
                   	 
                   		 //This line creates a new beforeRate object by taking the stayDateFrom of the existing rate 
//                   		 as the stayDateFrom, and the day before the stayDateFrom of the update rate as the stayDateTo. 
//                   		 The remaining properties such as nights, value, and bungalowId are copied from the 
//                   		 existing rate.
                   		 
                   		 //here update rate representing the new rate 
                   		 
                   		 Rate beforeRate = new Rate(existingRate.getStayDateFrom(), updateRate.getStayDateFrom().minusDays(1),existingRate.getNights(), existingRate.getValue(), existingRate.getBungalowId());
                   		 
//                   		 This line creates a new afterRate object by taking the day after the stayDateTo
//                   		 of the update rate as the stayDateFrom, and the stayDateTo of the existing rate as the
//                   		 stayDateTo. The remaining properties are copied from the existing rate.
                   		 
                   		 Rate afterRate = new Rate(updateRate.getStayDateFrom().plusDays(1), existingRate.getStayDateTo(), existingRate.getNights(), existingRate.getValue(), existingRate.getBungalowId());
                   		 
//                   		 These lines add the beforeRate, update rate , and afterRate to the updatedRates list. 
//                   		 This effectively splits the existing rate into three parts: before the update rate, 
//                   		 the update rate itself, and after the update rate.
                   		 
                   		 rateRepository.save(beforeRate);
                   		 rateRepository.save(updateRate);
                   		 rateRepository.save(afterRate);
                   		 
//                   		 This line sets the closedDate of the existing rate to the current date, 
//                   		 indicating that it is closed. The LocalDate.now() function returns the current date.
                   		 
                   		 existingRate.setClosedDate(LocalDate.now());
                   		 
//                   		 This line adds the existing rate with the updated closedDate to the updatedRates list.
                   		 
                   		 rateRepository.save(existingRate);
                   		 
//                   		 Overall, this code splits the existing rate into two parts 
//                   		 (before and after the update  rate), adds the update rate, and updates 
//                   		 the existing rate by setting the closedDate.
                   		 
                 		 System.out.println("case 01 splitting update");

            		}
                   	 
                // CASE:02-  condition checks if the stayDateFrom of the update rate is after the 
//             	 stayDateFrom of the existing rate and if the stayDateTo of the update rate is equal the 
//             	 stayDateTo of the existing rate. 
//           	 
           	 else if(updateRate.getStayDateFrom().isAfter(existingRate.getStayDateFrom())&&updateRate.getStayDateTo().isEqual(existingRate.getStayDateTo()))
           	 {
           		//split the existing rate into two: the preceding part and after the update rate
           		 
           		 Rate precedingRate = new Rate(existingRate.getStayDateFrom(), updateRate.getStayDateFrom().minusDays(1), existingRate.getNights(), existingRate.getValue(), existingRate.getBungalowId());
           		 
           		 //Rate afterRate = new Rate(rate.getStayDateFrom(), rate.getStayDateTo(), rate.getNights(), rate.getValue(), rate.getBungalowId());
           		 
           		 rateRepository.save(precedingRate);
           		 //rateRepository.save(afterRate);
           		 rateRepository.save(updateRate);
           		 
           		 //close the existing rate
           		 
           		 existingRate.setClosedDate(LocalDate.now());
           		 
           		 rateRepository.save(existingRate);
         		 System.out.println("case 02 splitting update");

           		 			 
           	 }
            		
            	//CASE 03:-  condition checks if the stayDateFrom of the update rate is equal the 
//             	 stayDateFrom of the existing rate and if the stayDateTo of the update rate is before the 
//             	 stayDateTo of the existing rate.
               	 
               	 else if(updateRate.getStayDateFrom().isEqual(existingRate.getStayDateFrom())&&updateRate.getStayDateTo().isBefore(existingRate.getStayDateTo()))
               	 {
               		 //split the existing rate into two 
               		 
               		 Rate preceeding = new Rate(updateRate.getStayDateTo().plusDays(1),existingRate.getStayDateTo(), existingRate.getNights(), existingRate.getValue(), existingRate.getBungalowId());
               	     
               		 
               		 //save in the database
               		 
               		 rateRepository.save(updateRate);
               		 rateRepository.save(preceeding);
               		 
               		 //close the existing rates
               		 
               		 existingRate.setClosedDate(LocalDate.now());
               		 
               		 //save the closed date rate in the rate table
               		 
               		 rateRepository.save(existingRate);
             		 System.out.println("case 03 splitting update");

               	 }
            		
            	// CASE:04-  condition checks if the stayDateFrom of the update rate is equal the 
//             	 stayDateFrom of the existing rate and if the stayDateTo of the update rate is equal the 
//             	 stayDateTo of the existing rate.
               	 
               	 else if(updateRate.getStayDateFrom().isEqual(existingRate.getStayDateFrom())&&updateRate.getStayDateTo().isEqual(existingRate.getStayDateTo()))
               	 {
               		 //new rate save in the table
               		 
               		 rateRepository.save(updateRate);
               		 
               		 //close the existing rate
               		 
               		 existingRate.setClosedDate(LocalDate.now());
               		 
               		 //save the closed date rate in the rate table
               		 
               		 rateRepository.save(existingRate);
             		 System.out.println("case 04 splitting update");

               	 }
            		
            	//CASE 05:- condition checks if the stayDateFrom of the update rate is equal the 
//             	 stayDateTo of the update rate and lies in between existing rate
               	 
               	 else if(updateRate.getStayDateFrom().isEqual(updateRate.getStayDateTo())&&updateRate.getStayDateFrom().isAfter(existingRate.getStayDateFrom())&&updateRate.getStayDateTo().isBefore(existingRate.getStayDateTo()))
               		 
               	 {
               		 //split the given table into three
               		 
               		 Rate rateBefore = new Rate(existingRate.getStayDateFrom(), updateRate.getStayDateFrom().minusDays(1), existingRate.getNights(), existingRate.getValue(),existingRate.getBungalowId());
               		 Rate rateAfter = new Rate(updateRate.getStayDateTo().plusDays(1),existingRate.getStayDateTo(), existingRate.getNights(), existingRate.getValue(), existingRate.getBungalowId());
               		 
               		 //save rates in the rate repo
               		 
               		 rateRepository.save(rateBefore);
               		 rateRepository.save(rateAfter);
               		 rateRepository.save(updateRate);
               		 
               		 //set closed date to now
               		 
               		 existingRate.setClosedDate(LocalDate.now());
               		 
               		 //save the inactive rate
               		 
               		 rateRepository.save(existingRate);
             		 System.out.println("case 05 splitting update");

               		 
               		 
               	 }
            		
              //CASE: 06- condition checks if the stayDateFrom of the update rate is equal the 
//             	 stayDateFrom of the existing rate and if the stayDateTo of the update rate is after the 
//             	 stayDateTo of the existing rate.
               	 
               	 else if(updateRate.getStayDateFrom().isEqual(existingRate.getStayDateFrom())&&updateRate.getStayDateTo().isAfter(existingRate.getStayDateTo()))
               	 {
               		 //save the new entry
               		 rateRepository.save(updateRate);
               		 
               		 //closed date to not null of existing rate
               		 
               		 existingRate.setClosedDate(LocalDate.now());
               		 
               		 //save the not null existing rate
               		 
               		 rateRepository.save(existingRate);
             		 System.out.println("case 06 splitting update");

               		 
               	 }
            		
            	 //CASE: 07- condition checks if the stayDateFrom of the update rate is before the 
//             	 stayDateFrom of the existing rate and if the stayDateTo of the update rate is equal the 
//             	 stayDateTo of the existing rate
               	 
               	 else if(updateRate.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&updateRate.getStayDateTo().isEqual(existingRate.getStayDateTo()))
               	 {
               		 //save the new rate
               		 rateRepository.save(updateRate);
               		 
               		 //set closed rate to not null
               		 
               		 existingRate.setClosedDate(LocalDate.now());
               		 
               		 //save the inactive existing rate
               		 
               		 rateRepository.save(existingRate);
             		 System.out.println("case 07 splitting update");

               	 }
            		
//SUSPENSE		//CASE 08:- //CASE:08- replace all null with not null if the all existing rate come in between the update rate
	            	 
	            	 
            	 else if(isExistingRateNewRate1(existingRate, updateRate))
            	 {
            		 //rateRepository.save(updateRate);
            		 existingRate.setClosedDate(LocalDate.now());
            		 
            		 rateRepository.save(existingRate);
            		 rateRepository.save(updateRate);
             		 System.out.println("case 08 splitting update");


            	 }
                 //CASE 09:-  .............OTHER CASE
            		
            		//CASE 09: if the new rate stay date from is before of the existing rate stay date from 
               	 //and stay date to of new rate is before stay date to of existing rate
               	 
               	 else if(updateRate.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&updateRate.getStayDateTo().isBefore(existingRate.getStayDateTo())&&updateRate.getStayDateTo().isAfter(existingRate.getStayDateFrom()))
               	 {
               		 Rate rateAfter=new Rate(updateRate.getStayDateTo().plusDays(1),existingRate.getStayDateTo(),existingRate.getNights(),existingRate.getValue(),existingRate.getBungalowId());
               		 rateRepository.save(rateAfter);
               		 rateRepository.save(updateRate);
               		 existingRate.setClosedDate(LocalDate.now());
               		 rateRepository.save(existingRate);
               		 System.out.println("Case 09 of splitting in update");
               		 
               		 
               	 }
               	 
               	 //CASE 10:- if the new rate of stay date from is after the stay date from of existing rate but stay date to of new rate is after the stay date to of
               	 //of existing rate stay date to 
               	 
               	 else if(updateRate.getStayDateFrom().isAfter(existingRate.getStayDateFrom())&&updateRate.getStayDateTo().isAfter(existingRate.getStayDateTo())&&updateRate.getStayDateFrom().isBefore(existingRate.getStayDateTo())) 
               	 {
               		 Rate rateBefore=new Rate(existingRate.getStayDateFrom(),updateRate.getStayDateFrom().minusDays(0),existingRate.getNights(),existingRate.getValue(),existingRate.getBungalowId());
               		 
               		 rateRepository.save(rateBefore);
               		 rateRepository.save(updateRate);
               		 existingRate.setClosedDate(LocalDate.now());
               		 
               		 rateRepository.save(existingRate);
               		 
               		 System.out.println("Case 10 of splitting in create");
               	 }
			  
		  }
	  }
}

         private boolean isExistingRateNewRate1(Rate existingRate, Rate updateRate)
        {
	        return updateRate.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&updateRate.getStayDateTo().isAfter(existingRate.getStayDateTo());
        }


        
//MERGE        // logic for merging the rate table while updating the rate table by rateID 

		private void mergeUpdate(Rate update, List<Rate> overlappingRates) 
		{
	        // TODO Auto-generated method stub
			 for(Rate existingRate : overlappingRates)
			  {
				  //check if the rate has the same nights and bungalowID
				  if(existingRate.getNights()==update.getNights()&&existingRate.getBungalowId()==update.getBungalowId()&&existingRate.getValue()==update.getValue()&&existingRate.getClosedDate()==null)
				  {
					  //CASE:01- check if the update rate completely overlaps the existing rate and has the same value
					  
					  if(update.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&update.getStayDateTo().isAfter(existingRate.getStayDateTo()))
					  {
						  
						  //save of the update rate
						  
						  rateRepository.save(update);
						  
						  //closed date for the existing rate
						  
						  existingRate.setClosedDate(LocalDate.now());
						  
						  //save in the rate table
						  
						  rateRepository.save(existingRate);
		             		 System.out.println("case 01 merging update");

						  
						  
					  }	
					  
					  //CASE:02-- //CASE:02- check if the existing rate has the same stay dates as the update rate and has the same values
					  
					  else if(update.getStayDateFrom().isEqual(existingRate.getStayDateFrom())&&update.getStayDateTo().isEqual(existingRate.getStayDateTo()))
					  {
						  //save of the update rate
						  
						  rateRepository.save(update);
						  
						  //closed of the existing rate 
						  
						  existingRate.setClosedDate(LocalDate.now());
						  
						  //save in the rate table
						  
						  rateRepository.save(existingRate);
		             		 System.out.println("case 02 merging update");

					  }
					  
					  //CASE:03- check if the existing rate ends exactly at the start of the update rate
					  
					  else if(update.getStayDateFrom().isEqual(existingRate.getStayDateTo()))
					  {
						  
						  //merge the existing rate with the update rate by updating the stay date to of existing rate
						  //existingRate.setStayDateTo(rate.getStayDateTo());
						  update.setStayDateFrom(existingRate.getStayDateFrom());
					  
					  //save the new rate
					  
					  rateRepository.save(update);
					  
					  //closed date to local date now of the existing rate
					  
					  existingRate.setClosedDate(LocalDate.now());
					  
					  //save the existing rate to the rate repo
					  
					  rateRepository.save(existingRate);
	             		 System.out.println("case 03 merging update");

					  
					  }
					  
					  //CASE:04- check if the update rate stay date to is equal to the stay date from of the existing rate
					  
					  else if(update.getStayDateTo().isEqual(existingRate.getStayDateFrom()))
					  {
						  //merge the existing rate with the update rate
						  
						  update.setStayDateTo(existingRate.getStayDateTo());
						  
						  //save the new rate
						  
						  rateRepository.save(update);
						  
						  //closed date to the local date now to the existing 
						  
						  existingRate.setClosedDate(LocalDate.now());
						  
						  //save the closed date of the NOTNULL to the rate table
						  
						  rateRepository.save(existingRate);
		             		 System.out.println("case 04 merging update");

						  
					  }
					  
					  //CASE: 05-- if the stay date from of update rate lies in between existing rate but stay date to of update rate lies
					  // after the stay date to of existing rate
					  
					  else if(update.getStayDateFrom().isBefore(existingRate.getStayDateTo())&&update.getStayDateTo().isAfter(existingRate.getStayDateTo())&&update.getStayDateFrom().isAfter(existingRate.getStayDateFrom()))
					  {
						  //merge the stay date to of existing to the stay date to of new rate
						  
						  //existingRate.setStayDateTo(rate.getStayDateTo());
						  update.setStayDateFrom(existingRate.getStayDateFrom());
						  
						  
						  //save the new rate
						  
						  rateRepository.save(update);
						  
						  //set closed date of existing rate to local date now
						  
						  existingRate.setClosedDate(LocalDate.now());
						  
						  //save the existing not null rate
						  
						  rateRepository.save(existingRate);
		             		 System.out.println("case 05 merging update");

						  
						  
					  }
					  
					  //CASE 06:- if the stay date from of update rate is before the stay date from of existing rate nut the stay date 
					  // to of the update rate is after the stay date from of existing rate but before from the stay date to of existing rate
					  
					  else if(update.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&update.getStayDateTo().isAfter(existingRate.getStayDateFrom())&&update.getStayDateTo().isBefore(existingRate.getStayDateTo()))
					  {
						  //merge of the existing rate with the update rate
						  
						  //existingRate.setStayDateFrom(update.getStayDateFrom());
						  
						  update.setStayDateTo(existingRate.getStayDateTo());
						  
						  //save the new rate
						  
						  rateRepository.save(update);
						  
						  //set closed date of existing to the inactive
						  
						  existingRate.setClosedDate(LocalDate.now());
						  
						  //save the inactive existing rate
						  
						  rateRepository.save(existingRate);
		             		 System.out.println("case 06 merging update");

					  }
					  
					  //CASE 07:- complete overlaps that mean the update rate completely inside the existing rate
					  
					  else if(update.getStayDateFrom().isAfter(existingRate.getStayDateFrom())&&update.getStayDateTo().isBefore(existingRate.getStayDateTo()))
					  {
						  //merge the update rate with existing rate
						  
						  update.setStayDateFrom(existingRate.getStayDateFrom());
						  update.setStayDateTo(existingRate.getStayDateTo());
						  
						  //save the update rate
						  
						  rateRepository.save(update);
						  
						  //set closed date of existing to local date now
						  
						  existingRate.setClosedDate(LocalDate.now());
						  
						  //save the existing rate
						  
						  rateRepository.save(existingRate);
		             		 System.out.println("case 07 merging update");

					  }
					  
//SUSPENSE			  //CASE 08:-if the existing rate lies in between the new rate
					  
					  else if(update.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&update.getStayDateTo().isAfter(existingRate.getStayDateTo()))
					  {
						  // save the all rate
						  //Rate temp = new Rate(update.getStayDateFrom(), update.getStayDateTo(), update.getNights(), update.getValue(), update.getBungalowId());
						  rateRepository.save(update);
						  
						  //set closed date of existing rate to inactive
						  
						  existingRate.setClosedDate(LocalDate.now());
						  
						  //save the existing rate
						  
						  rateRepository.save(existingRate);
		             		 System.out.println("case 08 merging update");

					  }
					  
					//CASE 09:- OTHER CASES HANDLE IN THE MERGE OF UPDARE METHOD
					  
					//CASE 09:-   if the stay date from of of new rate is just a day after of stay date to of existing rate but the stay date to new 
					  //rate is after the stay date to of existing rate and the stay date from of the existing rate is before of stay date from of stay date from
					  
					  else if(update.getStayDateFrom().equals(existingRate.getStayDateTo().plusDays(1)))
							  
					  {
						  
						  existingRate.setClosedDate(LocalDate.now());
						  rateRepository.save(existingRate);
						  if(update.getRateId() == null) {
							  update.setStayDateFrom(existingRate.getStayDateFrom());
							  rateRepository.save(update);
						  }
						  else {
							  update.setClosedDate(LocalDate.now());
							  
							  rateRepository.save(update);
							  Rate rateAfter=new Rate(existingRate.getStayDateFrom(),update.getStayDateTo(),update.getNights(),update.getValue(),update.getBungalowId());
							  update = rateRepository.save(rateAfter);
							  
						  }
						  
		          		
					  }
					  
					  //CASE 10:  
					  else if (update.getStayDateTo().equals(existingRate.getStayDateFrom().minusDays(1))) 
					  {
						  existingRate.setClosedDate(LocalDate.now());
						  
						  rateRepository.save(existingRate);
						  if(update.getRateId() == null) {
							  update.setStayDateTo(existingRate.getStayDateTo());	
							  rateRepository.save(update);
						  }
						  else 
						  {
							  update.setClosedDate(LocalDate.now());
							  
							  rateRepository.save(update);
							  Rate rateAfter=new Rate(update.getStayDateFrom(),existingRate.getStayDateTo(),update.getNights(),update.getValue(),update.getBungalowId());
							  update = rateRepository.save(rateAfter);					  
						  }				  
					  }
					//.........................  
					  
				  }
			  }
		}
 


        

		//..............................................................
//        return rateRepository.save(existingRate);
//    }

    /**
     *@param id
     *@return this method return boolean
     */
    @Override
    public void deleteRate(Long id) {
        rateRepository.deleteById(id)
;
    }

    /**
     *@param id
     *@return get rate by id
     */
    @Override
    public Rate getRateById(Long id) 
    {
        return rateRepository.findById(id)

                .orElseThrow(() -> new RateNotFoundException("Rate not found with id: " + id));
    }

    /**
     * @param pageable
     * @param id
     * @param stayDateFrom
     * @param stayDateTo
     * @param nights
     * @param value
     * @param bungalowId
     * @return get rate by specification of all fields
     */

    @Override
    public Page<Rate> getAllRates(Pageable pageable, Long id, LocalDate stayDateFrom, LocalDate stayDateTo,
    Integer nights, Double value, Long bungalowId, LocalDate closedDate) 
    {
    Specification<Rate> spec = RateSpecification.searchByCriteria(id, stayDateFrom, stayDateTo,
    nights, value, bungalowId,closedDate);
       return rateRepository.findAll(spec, pageable);
    }
    
    
    
    //Import Rate from an Excels(upload)
    
    
    
    
    //export rate from an database to excel(download)
   
    
    //............................End of download

    
    
    
    
    
}