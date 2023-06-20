package com.RateManagement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.RateManagement.Entity.Bungalow;
import com.RateManagement.Entity.Rate;
import com.RateManagement.Repo.BungalowRepository;
import com.RateManagement.Repo.RateRepository;
import com.RateManagement.Service.BungalowService;
import com.RateManagement.Service.BungalowServiceImpl;
import com.RateManagement.Service.RateService;
import com.RateManagement.Service.RateServiceImpl;

@SpringBootTest
public class ServiceTestIntegration 
{
	
	@Autowired
	private RateService rateService;
	
	@Autowired
	private RateRepository rateRepository;
	
	Rate rate;
	
	@BeforeEach
	public void setUp()
	{
		rate = new Rate();
		rate.setStayDateFrom(LocalDate.parse("2023-01-01"));
		rate.setStayDateTo(LocalDate.parse("2023-12-31"));
		rate.setNights(2);
		rate.setValue(1000);
		rate.setBungalowId((long) 1);
		rate.setClosedDate(null);
		
	}
	
	@AfterEach
	public void tearDown()
	{
		rate = null;
		//delete this mock from the database
		rateRepository.deleteAll();
	}
	
	
	//create rate Junit testing
	@Test
	public void createRate() throws Exception
	{
		Rate createdRate = rateService.createRate(rate);
		
		
      // Verify the results
      assertThat(createdRate).isNotNull();
      assertThat(createdRate.getRateId()).isNotNull();
      assertThat(createdRate.getStayDateFrom()).isEqualTo(rate.getStayDateFrom());
      assertThat(createdRate.getStayDateTo()).isEqualTo(rate.getStayDateTo());
      assertThat(createdRate.getNights()).isEqualTo(rate.getNights());
      assertThat(createdRate.getValue()).isEqualTo(rate.getValue());
      assertThat(createdRate.getBungalowId()).isEqualTo(rate.getBungalowId());
      assertThat(createdRate.getClosedDate()).isEqualTo(rate.getClosedDate());


		
	}
	
	
	//junit testing for the method getRateById
	@Test
	public void getRateById() throws Exception
	{
		//firstly saved the rate into the database
		
		Rate createdRate = rateService.createRate(rate);
		
		//get the mocking rate by rateid
		Rate getRateByID = rateService.getRateById(createdRate.getRateId());
		
		assertNotNull(getRateByID);
        assertEquals(rate.getRateId(), getRateByID.getRateId());
        assertEquals(rate.getStayDateFrom(), getRateByID.getStayDateFrom());
        assertEquals(rate.getStayDateTo(), getRateByID.getStayDateTo());
        assertEquals(rate.getNights(), getRateByID.getNights());
        assertEquals(rate.getValue(), getRateByID.getValue());
        assertEquals(rate.getBungalowId(), getRateByID.getBungalowId());
        assertEquals(rate.getClosedDate(), getRateByID.getClosedDate());		
		
	}
	
	
	//J-Unit testing for the delete rate by id
	
	@Test
	public void deleteRate() throws Exception
	{
		//delete the rate by its ID
		Rate createdRate = rateService.createRate(rate);
		
		rateService.deleteRate(rate.getRateId());
		
		assertFalse(rateRepository.existsById(rate.getRateId()));

	}
	
	
	//j-Unit testing for the Update the rate by its ID
	
	@Test
	public void updateRate() throws Exception
	{
		Rate createdRate = rateService.createRate(rate);
		
		rate.setStayDateFrom(LocalDate.parse("2023-02-01"));
		rate.setStayDateTo(LocalDate.parse("2023-12-31"));
		rate.setNights(3);
		rate.setValue(1500);
		//rate.setBungalowId((long) 2);
		
		
		//call the updated method
		
		Rate updatedRate = rateService.updateRate(rate.getRateId(),rate);
		
		assertEquals(LocalDate.parse("2023-02-01"), updatedRate.getStayDateFrom());
        assertEquals(LocalDate.parse("2023-12-31"), updatedRate.getStayDateTo());
        assertEquals(3, updatedRate.getNights());
        assertEquals(1500, updatedRate.getValue());
		

	}
	
	
		
	
	//Splitting and merging logic Integration testing for createRate
	
	
	
	
	
	
	
}
	
