package com.RateManagement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.RateManagement.Entity.Rate;
import com.RateManagement.Repo.RateRepository;
import com.RateManagement.Service.RateService;
import com.RateManagement.Service.RateServiceImpl;

import jakarta.transaction.Transactional;

@SpringBootTest
public class RepoUnitTest 
{
	
//	@Mock
//	private RateRepository ratesRepository;
//	
//	@InjectMocks
//	private RateService rateService = new RateServiceImpl(ratesRepository);
//	

	
    @Autowired
    private RateRepository rateRepository;
    
    //RateRepository Test 
    /**
     * JUNIT testing for repository
     */
    @Test
    @Transactional
    void isRateExistById()
    {
    	Rate rate = new Rate();
    	rate.setRateId((long) 154);
    	rate.setStayDateFrom(LocalDate.of(2023, 2, 15));
    	rate.setStayDateTo(LocalDate.of(2023, 4, 30));
    	rate.setNights(2);
    	rate.setValue(11000);
    	rate.setBungalowId((long) 1);
    	rate.setClosedDate(null);    	
    	rateRepository.save(rate);
    	boolean actualResult = rateRepository.existsById((long) 154);
    	assertThat(actualResult).isFalse();
    	
    }
    
    //Service layer Test 
    //createRate test
    
//    @Test
//    void createRate()
//    {
//    	//set the rate 
//    	Rate rate = new Rate();
//    	rate.setStayDateFrom(LocalDate.of(2023, 6, 1));
//    	rate.setStayDateTo(LocalDate.of(2023, 6, 8));
//    	rate.setNights(2);
//    	rate.setValue(3000);
//    	rate.setBungalowId((long) 100000);
//    	rate.setClosedDate(null);
//    	
//    	
//    	when(ratesRepository.save(rate)).thenReturn(rate);
//    	
//    	
//    	//checked saved rate
//    	Rate createdRate = rateService.createRate(rate);
//    	
//    	
//    	
//    	//Assert that is it equal or not
//    	
//    	assertThat(createdRate).isNotNull();
//    	assertThat(createdRate.getRateId()).isNotNull();
//    	assertThat(createdRate.getStayDateFrom()).isEqualTo(rate.getStayDateFrom());
//    	assertThat(createdRate.getStayDateTo()).isEqualTo(rate.getStayDateTo());
//    	assertThat(createdRate.getNights()).isEqualTo(rate.getNights());
//    	assertThat(createdRate.getValue()).isEqualTo(rate.getValue());
//    	assertThat(createdRate.getBungalowId()).isEqualTo(rate.getBungalowId());
//    	assertThat(createdRate.getClosedDate()).isEqualTo(rate.getClosedDate());
//    	
//    }
//    
//    @Test
//    void deleteRate() {
//        // Arrange
//        Long rateId = 1L;
//        Rate rate = new Rate();
//        rate.setRateId(rateId);
//        rate.setStayDateFrom(LocalDate.of(2023, 6, 1));
//        rate.setStayDateTo(LocalDate.of(2023, 6, 8));
//        rate.setNights(7);
//        rate.setValue(100.0);
//        rate.setBungalowId((long) 100000);
//        rate.setClosedDate(null);
//
//        when(ratesRepository.existsById(rateId)).thenReturn(true);
//
//        rateService.deleteRate(rateId);
//    }
//    
//    @Test
//    void updateRate() {
//        // Arrange
//        Long rateId = 1L;
//        Rate existingRate = new Rate();
//        existingRate.setRateId(rateId);
//        existingRate.setStayDateFrom(LocalDate.of(2023, 6, 1));
//        existingRate.setStayDateTo(LocalDate.of(2023, 6, 8));
//        existingRate.setNights(7);
//        existingRate.setValue(100);
//        existingRate.setBungalowId((long) 100000);
//        //existingRate.setClosedDate(null);
//
//        Rate updatedRate = new Rate();
//        updatedRate.setRateId(rateId);
//        updatedRate.setStayDateFrom(LocalDate.of(2023, 6, 10)); // Set the updated stayDateFrom value
//        updatedRate.setStayDateTo(LocalDate.of(2023, 6, 15)); // Set the updated stayDateTo value
//        updatedRate.setNights(6); // Set the updated nights value
//        updatedRate.setValue(120); // Set the updated value
//        updatedRate.setBungalowId((long) 100000); // Set the updated bungalowId value
//        //updatedRate.setClosedDate(null); // Set the updated closedDate value
//
//        when(ratesRepository.existsById(rateId)).thenReturn(true);
//        when(ratesRepository.save(updatedRate)).thenReturn(updatedRate);
//
//        rateService.updateRate(rateId, updatedRate);
//
//    }
//    
    
        
}