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

import jakarta.persistence.EntityNotFoundException;

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
    	System.out.println("In CreateRate");
    	
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
         
         
         

         // Retrieve overlapping rates
         
         //List<Rate> overlappingRates = rateRepository.findOverlappingRates(rate.getBungalowId(), rate.getStayDateFrom(), rate.getStayDateTo(), rate.getNights());
         List<Rate> overlappingRates = rateRepository.findAll(RateSpecification.findOverlappingRates(rate.getBungalowId(), rate.getStayDateFrom(), rate.getStayDateTo(), rate.getNights()));
         List<Rate> overlappingRateMerge = rateRepository.findAll(RateSpecification.overlappingRateMerge(rate.getBungalowId(), rate.getStayDateFrom(), rate.getStayDateTo(), rate.getNights(),rate.getValue()));


         if (overlappingRates.isEmpty()) 
           {
               return rateRepository.save(rate);
           } 
           
           else 
          	 
           {
               split(rate, overlappingRates);
               overlappingRateMerge = rateRepository.findAll(RateSpecification.overlappingRateMerge(rate.getBungalowId(), rate.getStayDateFrom(), rate.getStayDateTo(), rate.getNights(),rate.getValue()));              
               merge(rate, overlappingRateMerge);
           }
       		   return rate;

  }
    
//MERGE OF THE CREATE RATE   //method for merging the rate 
    
  private void merge(Rate rate, List<Rate> overlappingRateMerge) 
  {
		// TODO Auto-generated method stub
	  //sort the overlapping rates by staydatefrom in ascending order
	  overlappingRateMerge.sort(Comparator.comparing(Rate::getStayDateFrom));
	  
	    for (int i = 0; i < overlappingRateMerge.size(); i++) {
	        Rate existingRate = overlappingRateMerge.get(i);

	        //CASE01:-- Check if the existing rate's stayDateFrom is adjacent to the new rate's stayDateTo
			  if (rate.getStayDateTo().equals(existingRate.getStayDateFrom().minusDays(1))) 
			  {
				  existingRate.setClosedDate(LocalDate.now());
				  
				  rateRepository.save(existingRate);
				  if(rate.getRateId() == null) {
					  rate.setStayDateTo(existingRate.getStayDateTo());	
					  rateRepository.save(rate);
					  System.out.println("CASE01 of merging if part ");
				  }
				  else 
				  {
					  rate.setClosedDate(LocalDate.now());
					  
					  rateRepository.save(rate);
					  Rate rateAfter=new Rate(rate.getStayDateFrom(),existingRate.getStayDateTo(),rate.getNights(),rate.getValue(),rate.getBungalowId());
					  rate = rateRepository.save(rateAfter);
					  System.out.println("CASE01 of merging else part ");

				  }				  
			  }
	        //CASE02:-- Check if the existing rate's stayDateTo is adjacent to the new rate's stayDateFrom
			  else if(rate.getStayDateFrom().equals(existingRate.getStayDateTo().plusDays(1)))
				  
			  {
				  
				  existingRate.setClosedDate(LocalDate.now());
				  rateRepository.save(existingRate);
				  if(rate.getRateId() == null) {
					  rate.setStayDateFrom(existingRate.getStayDateFrom());
					  rateRepository.save(rate);
					  System.out.println("CASE02 of merging if part ");

				  }
				  else {
					  rate.setClosedDate(LocalDate.now());
					  
					  rateRepository.save(rate);
					  Rate rateAfter=new Rate(existingRate.getStayDateFrom(),rate.getStayDateTo(),rate.getNights(),rate.getValue(),rate.getBungalowId());
					  rate = rateRepository.save(rateAfter);
					  System.out.println("CASE02 of merging else part ");

				  }      		
			  }
			  
			  //CASE03:-- 
	    }
  }

              // End of the merging method
         
//SPLIT OF CREATE RATE // method for the split of the table
  
    private void split(Rate rate, List<Rate> overlappingRates) 
    
 {   //.....start
  	  overlappingRates.sort(Comparator.comparing(Rate::getStayDateFrom));


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
            		 
            		 //existingRate.setValue(rate.getValue());
            		 
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
//            	 
            	 
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
            		 Rate rateBefore=new Rate(existingRate.getStayDateFrom(),rate.getStayDateFrom().minusDays(1),existingRate.getNights(),existingRate.getValue(),existingRate.getBungalowId());
            		 
            		 rateRepository.save(rateBefore);
            		 rateRepository.save(rate);
            		 existingRate.setClosedDate(LocalDate.now());
            		 
            		 rateRepository.save(existingRate);
            		 
            		 System.out.println("Case 10 of splitting in create");
            	 }
            	 
            	 //CASE 11:- if Stay-date-from(rate)==stay_date_to(rate) && stay_date_to(rate)>stay_date_to(existing rate)
            	 else if(rate.getStayDateFrom().isEqual(existingRate.getStayDateTo())&&rate.getStayDateTo().isAfter(existingRate.getStayDateTo()))
            	 {
            		 Rate rateBefore=new Rate(existingRate.getStayDateFrom(),rate.getStayDateFrom().minusDays(1),existingRate.getNights(),existingRate.getValue(),existingRate.getBungalowId());
            		 rateRepository.save(rateBefore);
            		 rateRepository.save(rate);
            		 existingRate.setClosedDate(LocalDate.now());
            		 rateRepository.save(existingRate);
            		 System.out.println("case 11 splitting for create");
            	 }
            	 
            	 
            	 //CASE:12:- if stay-date-To(Rate)==stay_date_From(existing rate) and rate staydatefrom is before staydatefrom of existingrate
            	 else if(rate.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&rate.getStayDateTo().isEqual(existingRate.getStayDateFrom())) {
            		 Rate rateAfter = new Rate(rate.getStayDateTo().plusDays(1),existingRate.getStayDateTo(),existingRate.getNights(),existingRate.getValue(),existingRate.getBungalowId());
            		  rateRepository.save(rateAfter);
            		  rateRepository.save(rate);

            		  existingRate.setClosedDate(LocalDate.now());
            		  rateRepository.save(existingRate);
            		  

            		  
            		  System.out.println("case 12 splitting for create");
            	 }
            	 //CASE:13-- if the stay_date-to is
            	 
            	 else if(rate.getStayDateFrom().equals(existingRate.getStayDateTo().plusDays(1)))
            	 {
            		 rateRepository.save(rate);
            	 }
            	 else if(rate.getStayDateFrom().equals(existingRate.getStayDateTo().minusDays(1)))
            	 {
            		 rateRepository.save(rate);
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
    public Rate updateRate(Long id, Rate update) {
        Optional<Rate> optionalRate = rateRepository.findById(id);
        if (optionalRate.isPresent()) {
            Rate existingRate = optionalRate.get();
            if (!id.equals(existingRate.getRateId())) {
                throw new IllegalArgumentException("Rate ID does not match the existing rates");
            } else {
                List<Rate> overlappingRates = rateRepository.findAll(RateSpecification.findOverlappingRates(update.getBungalowId(), update.getStayDateFrom(), update.getStayDateTo(), update.getNights()));
                List<Rate> overlappingRateMerge = rateRepository.findAll(RateSpecification.overlappingRateMerge(update.getBungalowId(), update.getStayDateFrom(), update.getStayDateTo(), update.getNights(), update.getValue()));

                if (!overlappingRates.isEmpty() && existingRate.getClosedDate() == null) {
//                    //existingRate.setClosedDate(LocalDate.now());
//                    System.out.println("Directly saved without merge and split");
//                    existingRate.setStayDateFrom(update.getStayDateFrom());
//                    existingRate.setStayDateTo(update.getStayDateTo());
//                    existingRate.setNights(update.getNights());
//                    existingRate.setValue(update.getValue());
//                    existingRate.setBungalowId(update.getBungalowId());
//                    existingRate.setClosedDate(null);
//                    return rateRepository.save(existingRate);
                	existingRate.setStayDateFrom(update.getStayDateFrom());
                    existingRate.setStayDateTo(update.getStayDateTo());
                    existingRate.setNights(update.getNights());
                    existingRate.setValue(update.getValue());
                    existingRate.setBungalowId(update.getBungalowId());
                    existingRate.setClosedDate(null);
                    
                	splitUpdate(existingRate, overlappingRates);
                    
                    overlappingRateMerge = rateRepository.findAll(RateSpecification.overlappingRateMerge(update.getBungalowId(), update.getStayDateFrom(), update.getStayDateTo(), update.getNights(), update.getValue()));
                    mergeUpdate(existingRate, overlappingRateMerge);
                    return update;

                } 
                else 
                {
                	
                	existingRate.setClosedDate(LocalDate.now());
                  System.out.println("Directly saved without merge and split");
                  existingRate.setStayDateFrom(update.getStayDateFrom());
                  existingRate.setStayDateTo(update.getStayDateTo());
                  existingRate.setNights(update.getNights());
                  existingRate.setValue(update.getValue());
                  existingRate.setBungalowId(update.getBungalowId());
                  existingRate.setClosedDate(null);
                  return rateRepository.save(existingRate);
                	 
                 }
            }
        } 
        else 
        {
            throw new EntityNotFoundException("Rate not found");
        }
    }

        

     private void mergeUpdate(Rate rate, List<Rate> overlappingRateMerge) 
    {
  		// TODO Auto-generated method stub
  	  //sort the overlapping rates by staydatefrom in ascending order
  	  overlappingRateMerge.sort(Comparator.comparing(Rate::getStayDateFrom));
  	  
  	    for (int i = 0; i < overlappingRateMerge.size(); i++) {
  	        Rate existingRate = overlappingRateMerge.get(i);

  	        //CASE01:-- Check if the existing rate's stayDateFrom is adjacent to the new rate's stayDateTo
  			  if (rate.getStayDateTo().equals(existingRate.getStayDateFrom().minusDays(1))) 
  			  {
  				  existingRate.setClosedDate(LocalDate.now());
  				  
  				  rateRepository.save(existingRate);
  				  if(rate.getRateId() == null) {
  					  rate.setStayDateTo(existingRate.getStayDateTo());	
  					  rateRepository.save(rate);
  					  System.out.println("CASE01 of merging if part ");
  				  }
  				  else 
  				  {
  					  //rate.setClosedDate(LocalDate.now());
  					  
  					  rateRepository.save(rate);
  					  Rate rateAfter=new Rate(rate.getStayDateFrom(),existingRate.getStayDateTo(),rate.getNights(),rate.getValue(),rate.getBungalowId());
  					  rate = rateRepository.save(rateAfter);
  					  System.out.println("CASE01 of merging else part ");

  				  }				  
  			  }
  	        //CASE02:-- Check if the existing rate's stayDateTo is adjacent to the new rate's stayDateFrom
  			  else if(rate.getStayDateFrom().equals(existingRate.getStayDateTo().plusDays(1)))
  				  
  			  {
  				  
  				  existingRate.setClosedDate(LocalDate.now());
  				  rateRepository.save(existingRate);
  				  if(rate.getRateId() == null) {
  					  rate.setStayDateFrom(existingRate.getStayDateFrom());
  					  rateRepository.save(rate);
  					  System.out.println("CASE02 of merging if part ");

  				  }
  				  else {
  					  //rate.setClosedDate(LocalDate.now());
  					  
  					  rateRepository.save(rate);
  					  Rate rateAfter=new Rate(existingRate.getStayDateFrom(),rate.getStayDateTo(),rate.getNights(),rate.getValue(),rate.getBungalowId());
  					  rate = rateRepository.save(rateAfter);
  					  System.out.println("CASE02 of merging else part ");

  				  }      		
  			  }
  			  
  			  //CASE03:-- 
  	    }
    }

                // End of the merging method
           
  //SPLIT OF CREATE RATE // method for the split of the table
    
      private void splitUpdate(Rate rate, List<Rate> overlappingRates) 
      
   {   //.....start
    	  overlappingRates.sort(Comparator.comparing(Rate::getStayDateFrom));


           for (Rate existingRate : overlappingRates) 
           {
          	 // Check if the rate has the same nights and bungalowId
          	 
               if (existingRate.getNights() == rate.getNights() && existingRate.getBungalowId() == rate.getBungalowId()&&existingRate.getClosedDate()==null)
               {
              	 
              	 //CASE: 01 This condition checks if the stayDateFrom of the new rate is after the 
//              	 stayDateFrom of the existing rate and if the stayDateTo of the new rate is before the 
//              	 stayDateTo of the existing rate. This indicates that the new rate is completely inside 
//              	 the existing rate
              	 
              	 if(rate.getStayDateFrom().isAfter(existingRate.getStayDateFrom())&&rate.getStayDateTo().isBefore(existingRate.getStayDateTo()))
              	 {
              		 //This line creates a new beforeRate object by taking the stayDateFrom of the existing rate 
//              		 as the stayDateFrom, and the day before the stayDateFrom of the new rate as the stayDateTo. 
//              		 The remaining properties such as nights, value, and bungalowId are copied from the 
//              		 existing rate.
              		 
              		 //here rate representing the new rate 
              		 
              		 Rate beforeRate = new Rate(existingRate.getStayDateFrom(), rate.getStayDateFrom().minusDays(1),existingRate.getNights(), existingRate.getValue(), existingRate.getBungalowId());
              		 
//              		 This line creates a new afterRate object by taking the day after the stayDateTo
//              		 of the new rate as the stayDateFrom, and the stayDateTo of the existing rate as the
//              		 stayDateTo. The remaining properties are copied from the existing rate.
              		 
              		 Rate afterRate = new Rate(rate.getStayDateTo().plusDays(1), existingRate.getStayDateTo(), existingRate.getNights(), existingRate.getValue(), existingRate.getBungalowId());
              		 
//              		 These lines add the beforeRate, rate (new rate), and afterRate to the updatedRates list. 
//              		 This effectively splits the existing rate into three parts: before the new rate, 
//              		 the new rate itself, and after the new rate.
              		 
              		 rateRepository.save(beforeRate);
              		 rateRepository.save(rate);
              		 rateRepository.save(afterRate);
              		 
              		 System.out.println("Case01 of splitting");
              		 
//              		 This line sets the closedDate of the existing rate to the current date, 
//              		 indicating that it is closed. The LocalDate.now() function returns the current date.
              		 
              		 existingRate.setClosedDate(LocalDate.now());
              		 
//              		 This line adds the existing rate with the updated closedDate to the updatedRates list.
              		 
              		 rateRepository.save(existingRate);
              		 
//              		 Overall, this code splits the existing rate into two parts 
//              		 (before and after the new rate), adds the new rate, and updates 
//              		 the existing rate by setting the closedDate.
              		             		             		 
              	 }
              	 
   //           	 CASE:02-  condition checks if the stayDateFrom of the new rate is after the 
//                	 stayDateFrom of the existing rate and if the stayDateTo of the new rate is equal the 
//                	 stayDateTo of the existing rate. 
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
              	 
//              	 CASE:03-  condition checks if the stayDateFrom of the new rate is equal the 
//            	 stayDateFrom of the existing rate and if the stayDateTo of the new rate is before the 
//            	 stayDateTo of the existing rate.
              	 
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
//            	 stayDateFrom of the existing rate and if the stayDateTo of the new rate is equal the 
//            	 stayDateTo of the existing rate.
     
  //Update and create conflict case             	 
              	 
              	 
              	 else if(rate.getStayDateFrom().isEqual(existingRate.getStayDateFrom())&&rate.getStayDateTo().isEqual(existingRate.getStayDateTo()))
              	 {
              		 //new rate save in the table
              		 
              		 existingRate.setValue(rate.getValue());
              		 
              		 rateRepository.save(existingRate);
              		 
              		 //close the existing rate
              		 
              		 //existingRate.setClosedDate(LocalDate.now());
              		 
              		 //save the closed date rate in the rate table
              		 
              		 //rateRepository.save(existingRate);
              		 
              		 System.out.println("case 04 splitting");

              	 }
              	 
              	 //Case:05- condition checks if the stayDateFrom of the new rate is equal the 
//            	 stayDateTo of the new rate and lies in between existing rate
              	 
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
//            	 stayDateFrom of the existing rate and if the stayDateTo of the new rate is after the 
//            	 stayDateTo of the existing rate.
              	 
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
//            	 stayDateFrom of the existing rate and if the stayDateTo of the new rate is equal the 
//            	 stayDateTo of the existing rate
              	 
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
//              	 
              	 
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
              		 Rate rateBefore=new Rate(existingRate.getStayDateFrom(),rate.getStayDateFrom().minusDays(1),existingRate.getNights(),existingRate.getValue(),existingRate.getBungalowId());
              		 
              		 rateRepository.save(rateBefore);
              		 rateRepository.save(rate);
              		 existingRate.setClosedDate(LocalDate.now());
              		 
              		 rateRepository.save(existingRate);
              		 
              		 System.out.println("Case 10 of splitting in create");
              	 }
              	 
              	 //CASE 11:- if Stay-date-from(rate)==stay_date_to(rate) && stay_date_to(rate)>stay_date_to(existing rate)
              	 else if(rate.getStayDateFrom().isEqual(existingRate.getStayDateTo())&&rate.getStayDateTo().isAfter(existingRate.getStayDateTo()))
              	 {
              		 Rate rateBefore=new Rate(existingRate.getStayDateFrom(),rate.getStayDateFrom().minusDays(1),existingRate.getNights(),existingRate.getValue(),existingRate.getBungalowId());
              		 rateRepository.save(rateBefore);
              		 rateRepository.save(rate);
              		 existingRate.setClosedDate(LocalDate.now());
              		 rateRepository.save(existingRate);
              		 System.out.println("case 11 splitting for create");
              	 }
              	 
              	 
              	 //CASE:12:- if stay-date-To(Rate)==stay_date_From(existing rate) and rate staydatefrom is before staydatefrom of existingrate
              	 else if(rate.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&rate.getStayDateTo().isEqual(existingRate.getStayDateFrom())) {
              		 Rate rateAfter = new Rate(rate.getStayDateTo().plusDays(1),existingRate.getStayDateTo(),existingRate.getNights(),existingRate.getValue(),existingRate.getBungalowId());
              		  rateRepository.save(rateAfter);
              		  rateRepository.save(rate);

              		  existingRate.setClosedDate(LocalDate.now());
              		  rateRepository.save(existingRate);
              		  

              		  
              		  System.out.println("case 12 splitting for create");
              	 }
              	 //CASE:13-- if the stay_date-to is
              	 
              	 else if(rate.getStayDateFrom().equals(existingRate.getStayDateTo().plusDays(1)))
              	 {
              		 rateRepository.save(rate);
              	 }
              	 else if(rate.getStayDateFrom().equals(existingRate.getStayDateTo().minusDays(1)))
              	 {
              		 rateRepository.save(rate);
              	 }
               }
           }        
      }   
      
      
        //.............End of the splitting of the rate table
      
      //

    /**
     *@param id
     *@return this method return boolean
     */
    @Override
    public void deleteRate(Long id) {
        rateRepository.deleteById(id);
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
    

      
}