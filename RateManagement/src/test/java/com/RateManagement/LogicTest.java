package com.RateManagement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.RateManagement.Entity.Rate;
import com.RateManagement.Repo.RateRepository;
import com.RateManagement.Service.RateService;
import com.RateManagement.Service.RateServiceImpl;
import com.RateManagement.Specification.RateSpecification;


@SpringBootTest
public class LogicTest 
{
	@Autowired
	private RateServiceImpl rateServiceImpl;
	
	@Autowired
	private RateRepository rateRepository;
	
	Rate existingRate;
	
	@BeforeEach
	public void setUp()
	{
		existingRate = new Rate();
		existingRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		existingRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		existingRate.setNights(2);
		existingRate.setValue(1000);
		existingRate.setBungalowId((long) 1);
		existingRate.setClosedDate(null);
		
		
		
	}
	
	@AfterEach
	public void tearDown()
	{
		existingRate = null;
		//delete this mock from the database
		rateRepository.deleteAll();
	}
	
	
	
	 //CASE:01----if(rate.getStayDateFrom().isAfter(existingRate.getStayDateFrom())&&rate.getStayDateTo().isBefore(existingRate.getStayDateTo()))

	@Test
	public void CreateRateCase1()
	{
		Rate oldRate = rateRepository.save(existingRate);
		
		//create the new Rates
		
		Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2024-03-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-03-31"));
		newRate.setNights(2);
		newRate.setValue(5000);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		

		
		Rate AllRate = rateServiceImpl.createRate(newRate);
		
		List<Rate> list = rateRepository.findAll();
		
		
		//After Split Before that
		Rate BeforeThat = new Rate();
		BeforeThat.setStayDateFrom(LocalDate.parse("2024-01-01"));
		BeforeThat.setStayDateTo(LocalDate.parse("2024-02-29"));
		BeforeThat.setNights(2);
		BeforeThat.setValue(1000);
		BeforeThat.setBungalowId((long) 1);
		BeforeThat.setClosedDate(null);
		
		//After Split After that
		Rate AfterThat = new Rate();
		AfterThat.setStayDateFrom(LocalDate.parse("2024-04-01"));
		AfterThat.setStayDateTo(LocalDate.parse("2024-12-31"));
		AfterThat.setNights(2);
		AfterThat.setValue(1000);
		AfterThat.setBungalowId((long) 1);
		AfterThat.setClosedDate(null);
		
		//after splitting old rate
		
		oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		oldRate.setNights(2);
		oldRate.setValue(1000);
		oldRate.setBungalowId((long) 1);
		oldRate.setClosedDate(null);
		oldRate.setClosedDate(LocalDate.now());
		
		
		
		
		System.out.println("------------------------------------------------");
		
		System.out.println(list.contains(AllRate));
		assertTrue(list.contains(newRate));
		assertTrue(list.contains(BeforeThat));
		assertTrue(list.contains(AfterThat));
		assertTrue(list.contains(oldRate));

				
	}

	
	//CASE:02--- else if(rate.getStayDateFrom().isAfter(existingRate.getStayDateFrom())&&rate.getStayDateTo().isEqual(existingRate.getStayDateTo()))

	@Test
	public void CreateRateCase2()
	{
		Rate oldRate = rateRepository.save(existingRate);
		
		//create the new Rates
		
		Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2024-03-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		newRate.setNights(2);
		newRate.setValue(6000);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		
		Rate NewRate = rateServiceImpl.createRate(newRate);
		List<Rate> list = rateRepository.findAll();

		
		
		//After split 
		
		Rate BeforeThat = new Rate();
		BeforeThat.setStayDateFrom(LocalDate.parse("2024-01-01"));
		BeforeThat.setStayDateTo(LocalDate.parse("2024-02-29"));
		BeforeThat.setNights(2);
		BeforeThat.setValue(1000);
		BeforeThat.setBungalowId((long) 1);
		BeforeThat.setClosedDate(null);
		
		//after splitting old rate
		
				oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
				oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
				oldRate.setNights(2);
				oldRate.setValue(1000);
				oldRate.setBungalowId((long) 1);
				oldRate.setClosedDate(null);
				oldRate.setClosedDate(LocalDate.now());
		

		
		

		System.out.println(list.contains(NewRate));

		assertTrue(list.contains(newRate));
		assertTrue(list.contains(BeforeThat));
		assertTrue(list.contains(oldRate));
		
		
	}
	
	//CASE:03--- else if(rate.getStayDateFrom().isEqual(existingRate.getStayDateFrom())&&rate.getStayDateTo().isEqual(existingRate.getStayDateTo()))

	@Test
	public void CreateRateCase3()
	{
		Rate oldRate = rateRepository.save(existingRate);
		
		//create the new Rates
		
		Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		newRate.setNights(2);
		newRate.setValue(7000);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		

		
		Rate NewRate = rateServiceImpl.createRate(newRate);
		
		List<Rate> list = rateRepository.findAll();

		
		//this will directely save
		
		//after splitting old rate
		
		oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		oldRate.setNights(2);
		oldRate.setValue(1000);
		oldRate.setBungalowId((long) 1);
		oldRate.setClosedDate(null);
		oldRate.setClosedDate(LocalDate.now());

		System.out.println(list.contains(NewRate));

		assertTrue(list.contains(newRate));
		assertTrue(list.contains(oldRate));

		
		
		
		
	}
	
	//CASE:04:- else if(rate.getStayDateFrom().isEqual(existingRate.getStayDateFrom())&&rate.getStayDateTo().isBefore(existingRate.getStayDateTo()))
	
	@Test
	public void CreateRateCase4()
	{
		Rate oldRate = rateRepository.save(existingRate);
		
		//create the new Rates
		
		Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-03-31"));
		newRate.setNights(2);
		newRate.setValue(9000);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);

		
		Rate NewRate = rateServiceImpl.createRate(newRate);
		List<Rate> list = rateRepository.findAll();

		
		//after splitting old rate
		
				oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
				oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
				oldRate.setNights(2);
				oldRate.setValue(1000);
				oldRate.setBungalowId((long) 1);
				oldRate.setClosedDate(null);
				oldRate.setClosedDate(LocalDate.now());
				
				//After Split After that
				Rate AfterThat = new Rate();
				AfterThat.setStayDateFrom(LocalDate.parse("2024-04-01"));
				AfterThat.setStayDateTo(LocalDate.parse("2024-12-31"));
				AfterThat.setNights(2);
				AfterThat.setValue(1000);
				AfterThat.setBungalowId((long) 1);
				AfterThat.setClosedDate(null);
				
				System.out.println(list.contains(NewRate));

				assertTrue(list.contains(newRate));
				assertTrue(list.contains(oldRate));

		
		
	}
	
	
	//CASE:05:--else if(rate.getStayDateFrom().isEqual(rate.getStayDateTo())&&rate.getStayDateFrom().isAfter(existingRate.getStayDateFrom())&&rate.getStayDateTo().isBefore(existingRate.getStayDateTo()))
	
	@Test
	public void CreateRateCase5()
	{
		Rate oldRate = rateRepository.save(existingRate);
		
		//create the new Rates
		
		Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-11-25"));
		newRate.setNights(2);
		newRate.setValue(3000);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		

		
		Rate NewRate = rateServiceImpl.createRate(newRate);
		
		List<Rate> list = rateRepository.findAll();
		
		//after splitting old rate
		
		oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		oldRate.setNights(2);
		oldRate.setValue(1000);
		oldRate.setBungalowId((long) 1);
		oldRate.setClosedDate(null);
		oldRate.setClosedDate(LocalDate.now());
		
		//After Split After that
		Rate AfterThat = new Rate();
		AfterThat.setStayDateFrom(LocalDate.parse("2024-11-26"));
		AfterThat.setStayDateTo(LocalDate.parse("2024-12-31"));
		AfterThat.setNights(2);
		AfterThat.setValue(1000);
		AfterThat.setBungalowId((long) 1);
		AfterThat.setClosedDate(null);
		
		System.out.println(list.contains(NewRate));

		assertTrue(list.contains(newRate));
		assertTrue(list.contains(oldRate));
		
		
	}
	
	
	//CASE:06--else if(rate.getStayDateFrom().isEqual(existingRate.getStayDateFrom())&&rate.getStayDateTo().isAfter(existingRate.getStayDateTo()))
	
	@Test
	public void CreateRateCase6()
	{
		Rate oldRate = rateRepository.save(existingRate);
		
		//create the new Rates
		
		Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2025-11-25"));
		newRate.setNights(2);
		newRate.setValue(3300);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		

		
Rate NewRate = rateServiceImpl.createRate(newRate);
		
		List<Rate> list = rateRepository.findAll();

		
		//this will directely save
		
		//after splitting old rate
		
		oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		oldRate.setNights(2);
		oldRate.setValue(1000);
		oldRate.setBungalowId((long) 1);
		oldRate.setClosedDate(null);
		oldRate.setClosedDate(LocalDate.now());

		System.out.println(list.contains(NewRate));

		assertTrue(list.contains(newRate));
		assertTrue(list.contains(oldRate));
		
		
	}
	
	
	//CASE:07--else if(rate.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&rate.getStayDateTo().isEqual(existingRate.getStayDateTo()))
	
	@Test
	public void CreateRateCase7()
	{
		Rate oldRate = rateRepository.save(existingRate);
		oldRate.setClosedDate(LocalDate.now());
		
		//create the new Rates
		
		Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2023-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		newRate.setNights(2);
		newRate.setValue(3500);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		
Rate NewRate = rateServiceImpl.createRate(newRate);
		
		List<Rate> list = rateRepository.findAll();

		
		//this will directely save
		
		//after splitting old rate
		
		oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		oldRate.setNights(2);
		oldRate.setValue(1000);
		oldRate.setBungalowId((long) 1);
		oldRate.setClosedDate(null);
		oldRate.setClosedDate(LocalDate.now());

		System.out.println(list.contains(NewRate));

		assertTrue(list.contains(newRate));
		assertTrue(list.contains(oldRate));
		
		
	}
	
	//CASE08:--//CASE:08- replace all null with notnull if the all existing rate come in between the new rate
	
	@Test
	public void CreateRateCase8()
	{
		Rate oldRate = rateRepository.save(existingRate);
		oldRate.setClosedDate(LocalDate.now());
		
		//create the new Rates
		
		Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2023-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2025-12-31"));
		newRate.setNights(2);
		newRate.setValue(4500);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		
Rate NewRate = rateServiceImpl.createRate(newRate);
		
		List<Rate> list = rateRepository.findAll();

		
		//this will directely save
		
		//after splitting old rate
		
		oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		oldRate.setNights(2);
		oldRate.setValue(1000);
		oldRate.setBungalowId((long) 1);
		oldRate.setClosedDate(null);
		oldRate.setClosedDate(LocalDate.now());

		System.out.println(list.contains(NewRate));

		assertTrue(list.contains(newRate));
		assertTrue(list.contains(oldRate));
		
		
		
	}
	
	//CASE09:--else if(rate.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&rate.getStayDateTo().isBefore(existingRate.getStayDateTo())&&rate.getStayDateTo().isAfter(existingRate.getStayDateFrom()))
	
	@Test
	public void CreateRateCase9()
	{
		Rate oldRate = rateRepository.save(existingRate);
		oldRate.setClosedDate(LocalDate.now());
		
		//create the new Rates
		
		Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2023-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-11-30"));
		newRate.setNights(2);
		newRate.setValue(6500);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		
Rate NewRate = rateServiceImpl.createRate(newRate);
		
		List<Rate> list = rateRepository.findAll();

		
		//after split 
		
		//After Split After that
				Rate AfterThat = new Rate();
				AfterThat.setStayDateFrom(LocalDate.parse("2024-12-01"));
				AfterThat.setStayDateTo(LocalDate.parse("2024-12-31"));
				AfterThat.setNights(2);
				AfterThat.setValue(1000);
				AfterThat.setBungalowId((long) 1);
				AfterThat.setClosedDate(null);
		
		
		
		//after splitting old rate
		
		oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		oldRate.setNights(2);
		oldRate.setValue(1000);
		oldRate.setBungalowId((long) 1);
		oldRate.setClosedDate(null);
		oldRate.setClosedDate(LocalDate.now());

		System.out.println(list.contains(NewRate));

		assertTrue(list.contains(newRate));
		assertTrue(list.contains(oldRate));
		
		assertTrue(list.contains(AfterThat));



	}	
	
	//CASE10:--else if(rate.getStayDateFrom().isAfter(existingRate.getStayDateFrom())&&rate.getStayDateTo().isAfter(existingRate.getStayDateTo())&&rate.getStayDateFrom().isBefore(existingRate.getStayDateTo())) 

	
	@Test
	public void CreateRateCase10()
	{
		Rate oldRate = rateRepository.save(existingRate);
		
		//create the new Rates
		
		Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2024-02-01"));
		newRate.setStayDateTo(LocalDate.parse("2025-11-30"));
		newRate.setNights(2);
		newRate.setValue(7500);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		
		
        Rate NewRate = rateServiceImpl.createRate(newRate);
		
		List<Rate> list = rateRepository.findAll();
		
		//after split before that
		
		//After split 
		
				Rate BeforeThat = new Rate();
				BeforeThat.setStayDateFrom(LocalDate.parse("2024-01-01"));
				BeforeThat.setStayDateTo(LocalDate.parse("2024-01-31"));
				BeforeThat.setNights(2);
				BeforeThat.setValue(1000);
				BeforeThat.setBungalowId((long) 1);
				BeforeThat.setClosedDate(null);
		
		
		
		//after splitting old rate

		
				oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
				oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
				oldRate.setNights(2);
				oldRate.setValue(1000);
				oldRate.setBungalowId((long) 1);
				oldRate.setClosedDate(null);
				oldRate.setClosedDate(LocalDate.now());

				System.out.println(list.contains(NewRate));

				assertTrue(list.contains(newRate));
				assertTrue(list.contains(oldRate));
				assertTrue(list.contains(BeforeThat));

				
		
       


	}	
	
	//CASE11--:else if(rate.getStayDateFrom().isEqual(existingRate.getStayDateTo())&&rate.getStayDateTo().isAfter(existingRate.getStayDateTo()))
	
	@Test
	public void CreateRateCase11()
	{
		Rate oldRate = rateRepository.save(existingRate);
		
		//create the new Rates
		
		Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2025-11-30"));
		newRate.setNights(2);
		newRate.setValue(8500);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		

        Rate NewRate = rateServiceImpl.createRate(newRate);
		
		List<Rate> list = rateRepository.findAll();

		
		
		//this will directely save
		
				//after splitting old rate
				
				oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
				oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
				oldRate.setNights(2);
				oldRate.setValue(1000);
				oldRate.setBungalowId((long) 1);
				oldRate.setClosedDate(null);
				oldRate.setClosedDate(LocalDate.now());

				System.out.println(list.contains(NewRate));

				assertTrue(list.contains(newRate));
				assertTrue(list.contains(oldRate));

		
		


	}	
	
	//CASE12--:else if(rate.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&rate.getStayDateTo().isEqual(existingRate.getStayDateFrom()))
	
	@Test
	public void CreateRateCase12()
	{
		Rate oldRate = rateRepository.save(existingRate);
		oldRate.setClosedDate(LocalDate.now());
		
		//create the new Rates
		
		Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2023-03-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-01-01"));
		newRate.setNights(2);
		newRate.setValue(9500);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		
        Rate NewRate = rateServiceImpl.createRate(newRate);
		
		List<Rate> list = rateRepository.findAll();
		
		//After Split After that
		Rate AfterThat = new Rate();
		AfterThat.setStayDateFrom(LocalDate.parse("2024-01-02"));
		AfterThat.setStayDateTo(LocalDate.parse("2024-12-31"));
		AfterThat.setNights(2);
		AfterThat.setValue(1000);
		AfterThat.setBungalowId((long) 1);
		AfterThat.setClosedDate(null);

		
		
		
				//after splitting old rate
				
				oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
				oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
				oldRate.setNights(2);
				oldRate.setValue(1000);
				oldRate.setBungalowId((long) 1);
				oldRate.setClosedDate(null);
				oldRate.setClosedDate(LocalDate.now());

				System.out.println(list.contains(NewRate));

				assertTrue(list.contains(newRate));
				assertTrue(list.contains(oldRate));
				assertTrue(list.contains(AfterThat));



	}
	
	//CASE13--:else if(rate.getStayDateFrom().equals(existingRate.getStayDateTo().plusDays(1)))
	
	@Test
	public void CreateRateCase13()
	{
		Rate oldRate = rateRepository.save(existingRate);
		
		
		Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2025-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2026-01-01"));
		newRate.setNights(2);
		newRate.setValue(5000);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		
       Rate NewRate = rateServiceImpl.createRate(newRate);
		
		List<Rate> list = rateRepository.findAll();
		
		System.out.println(list.contains(NewRate));

		assertTrue(list.contains(newRate));




	}
	
	//CASE14--:else if(rate.getStayDateFrom().equals(existingRate.getStayDateTo().minusDays(1)))

	@Test
	public void CreateRateCase14()
	{
		Rate oldRate = rateRepository.save(existingRate);
		
		//create the new Rates
		
		Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2024-12-30"));
		newRate.setStayDateTo(LocalDate.parse("2025-01-12"));
		newRate.setNights(2);
		newRate.setValue(5000);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		
        Rate NewRate = rateServiceImpl.createRate(newRate);
		
		List<Rate> list = rateRepository.findAll();
		
		System.out.println(list.contains(NewRate));

		assertTrue(list.contains(newRate));

	}
	
	
	//CASE:15--  merging of the adjacent
	
	public void createRateCase15()
	{
		Rate oldRate = rateRepository.save(existingRate);
		
		//create a new rate
		
        Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2025-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2025-01-31"));
		newRate.setNights(2);
		newRate.setValue(1000);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		
       Rate NewRate = rateServiceImpl.createRate(newRate);
		
		List<Rate> list = rateRepository.findAll();

		
		//after merging new date
		
		Rate mergeRate = new Rate();
		mergeRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		mergeRate.setStayDateTo(LocalDate.parse("2025-01-31"));
		mergeRate.setNights(2);
		mergeRate.setValue(1000);
		mergeRate.setBungalowId((long) 1);
		mergeRate.setClosedDate(null);
		
		//oldrate will closed
		oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		oldRate.setNights(2);
		oldRate.setValue(1000);
		oldRate.setBungalowId((long) 1);
		oldRate.setClosedDate(null);
		oldRate.setClosedDate(LocalDate.now());
		
		newRate.setClosedDate(LocalDate.now());

		
		
		System.out.println(list.contains(NewRate));

		assertTrue(list.contains(mergeRate));
		assertTrue(list.contains(oldRate));
		assertTrue(list.contains(newRate));


	}
	
	//CASE:17--merging from the backward
	
	public void createRateCase16()
	{
		Rate oldRate = rateRepository.save(existingRate);
		
		//create a new rate
		
        Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2023-11-30"));
		newRate.setStayDateTo(LocalDate.parse("2023-12-31"));
		newRate.setNights(2);
		newRate.setValue(1000);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		
       Rate NewRate = rateServiceImpl.createRate(newRate);
		
		List<Rate> list = rateRepository.findAll();

		
		//after merging new date
		
		Rate mergeRate = new Rate();
		mergeRate.setStayDateFrom(LocalDate.parse("2023-11-30"));
		mergeRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		mergeRate.setNights(2);
		mergeRate.setValue(1000);
		mergeRate.setBungalowId((long) 1);
		mergeRate.setClosedDate(null);
		
		//oldrate will closed
		oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		oldRate.setNights(2);
		oldRate.setValue(1000);
		oldRate.setBungalowId((long) 1);
		oldRate.setClosedDate(null);
		oldRate.setClosedDate(LocalDate.now());
		
		newRate.setClosedDate(LocalDate.now());

		
		
		System.out.println(list.contains(NewRate));

		assertTrue(list.contains(mergeRate));
		assertTrue(list.contains(oldRate));
		assertTrue(list.contains(newRate));


	}
	

	
	
	
	//Update logic for both splitting and merging
	/**
	 * @throws Exception
	 * CASE: 01 This condition checks if the stayDateFrom of the new rate is after the 
//              	 stayDateFrom of the existing rate and if the stayDateTo of the new rate is before the 
//              	 stayDateTo of the existing rate. This indicates that the new rate is completely inside 
//              	 the existing rate
	 */
	
	//CASE:01--if(rate.getStayDateFrom().isAfter(existingRate.getStayDateFrom())&&rate.getStayDateTo().isBefore(existingRate.getStayDateTo()))
	
	

	@Test
	public void updateRateCase01() throws Exception
	{
		Rate oldRate = rateRepository.save(existingRate);

		
        Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		newRate.setNights(3);
		newRate.setValue(9000);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		newRate = rateRepository.save(newRate);

		
		//update the new rate	
		//newRate.setRateId(newRate.getRateId());
		newRate.setStayDateFrom(LocalDate.parse("2024-03-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-03-31"));
		newRate.setValue(200.0);
		newRate.setNights(2);

		
		
		Rate updatedRate = rateServiceImpl.updateRate(newRate.getRateId(),newRate);
		List<Rate> list = rateRepository.findAll();

		
        //Old rate will be closed
		
		oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		oldRate.setNights(2);
		oldRate.setValue(1000);
		oldRate.setBungalowId((long) 1);
		oldRate.setClosedDate(null);
		oldRate.setClosedDate(LocalDate.now());
		
		//after split
		
		//beforethat
		
		Rate BeforeThat = new Rate();
		BeforeThat.setStayDateFrom(LocalDate.parse("2024-01-01"));
		BeforeThat.setStayDateTo(LocalDate.parse("2024-02-29"));
		BeforeThat.setNights(2);
		BeforeThat.setValue(1000);
		BeforeThat.setBungalowId((long) 1);
		BeforeThat.setClosedDate(null);
		
		//after that
		Rate AfterThat = new Rate();
		AfterThat.setStayDateFrom(LocalDate.parse("2024-04-01"));
		AfterThat.setStayDateTo(LocalDate.parse("2024-12-31"));
		AfterThat.setNights(2);
		AfterThat.setValue(1000);
		AfterThat.setBungalowId((long) 1);
		AfterThat.setClosedDate(null);
		
		
		
		System.out.println(list.contains(updatedRate));

		assertTrue(list.contains(AfterThat));
		assertTrue(list.contains(oldRate));
		assertTrue(list.contains(BeforeThat));
		assertTrue(list.contains(newRate));

		
	}
	
	//CASE:02--else if(rate.getStayDateFrom().isAfter(existingRate.getStayDateFrom())&&rate.getStayDateTo().isEqual(existingRate.getStayDateTo()))

	/**
	 * @throws Exception
	 * CASE:02-  condition checks if the stayDateFrom of the new rate is after the 
//                	 stayDateFrom of the existing rate and if the stayDateTo of the new rate is equal the 
//                	 stayDateTo of the existing rate. 

	 */
	@Test
	public void updateRateCase02() throws Exception
	{
       Rate oldRate = rateRepository.save(existingRate);

		
        Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		newRate.setNights(3);
		newRate.setValue(9000);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		newRate = rateRepository.save(newRate);

		
		//update the new rate	
		//newRate.setRateId(newRate.getRateId());
		newRate.setStayDateFrom(LocalDate.parse("2024-03-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		newRate.setValue(200.0);
		newRate.setNights(2);

		
		
		Rate updatedRate = rateServiceImpl.updateRate(newRate.getRateId(),newRate);
		List<Rate> list = rateRepository.findAll();

		
        //Old rate will be closed
		
		oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		oldRate.setNights(2);
		oldRate.setValue(1000);
		oldRate.setBungalowId((long) 1);
		oldRate.setClosedDate(null);
		oldRate.setClosedDate(LocalDate.now());
		
		//after split
		
		//beforethat
		
				Rate BeforeThat = new Rate();
				BeforeThat.setStayDateFrom(LocalDate.parse("2024-01-01"));
				BeforeThat.setStayDateTo(LocalDate.parse("2024-02-29"));
				BeforeThat.setNights(2);
				BeforeThat.setValue(1000);
				BeforeThat.setBungalowId((long) 1);
				BeforeThat.setClosedDate(null);
				
				System.out.println(list.contains(updatedRate));

				assertTrue(list.contains(oldRate));
				assertTrue(list.contains(BeforeThat));
				assertTrue(list.contains(newRate));
		
		
		

	}
	
	//CASE:03--:else if(rate.getStayDateFrom().isEqual(existingRate.getStayDateFrom())&&rate.getStayDateTo().isBefore(existingRate.getStayDateTo()))
	
	/**
	 * @throws Exception
	 * CASE:03-  condition checks if the stayDateFrom of the new rate is equal the 
//            	 stayDateFrom of the existing rate and if the stayDateTo of the new rate is before the 
//            	 stayDateTo of the existing rate.
	 */
	@Test
	public void updateRateCase03() throws Exception
	{
		 Rate oldRate = rateRepository.save(existingRate);

			
	        Rate newRate = new Rate();
			
			newRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
			newRate.setStayDateTo(LocalDate.parse("2024-12-31"));
			newRate.setNights(3);
			newRate.setValue(9000);
			newRate.setBungalowId((long) 1);
			newRate.setClosedDate(null);
			newRate = rateRepository.save(newRate);

			
			//update the new rate	
			//newRate.setRateId(newRate.getRateId());
			newRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
			newRate.setStayDateTo(LocalDate.parse("2024-03-31"));
			newRate.setValue(200.0);
			newRate.setNights(2);

			
			
			Rate updatedRate = rateServiceImpl.updateRate(newRate.getRateId(),newRate);
			List<Rate> list = rateRepository.findAll();

			
	        //Old rate will be closed
			
			oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
			oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
			oldRate.setNights(2);
			oldRate.setValue(1000);
			oldRate.setBungalowId((long) 1);
			oldRate.setClosedDate(LocalDate.now());
			
			//after split
			
			//after that
			Rate AfterThat = new Rate();
			AfterThat.setStayDateFrom(LocalDate.parse("2024-04-01"));
			AfterThat.setStayDateTo(LocalDate.parse("2024-12-31"));
			AfterThat.setNights(2);
			AfterThat.setValue(1000);
			AfterThat.setBungalowId((long) 1);
			AfterThat.setClosedDate(null);
			
			
			
			System.out.println(list.contains(updatedRate));

			assertTrue(list.contains(AfterThat));
			assertTrue(list.contains(oldRate));
			assertTrue(list.contains(newRate));
		

	}
	
	//CASE04:--else if(rate.getStayDateFrom().isEqual(existingRate.getStayDateFrom())&&rate.getStayDateTo().isEqual(existingRate.getStayDateTo()))

	/**
	 * @throws Exception
	 * // CASE:04-  condition checks if the stayDateFrom of the new rate is equal the 
//            	 stayDateFrom of the existing rate and if the stayDateTo of the new rate is equal the 
//            	 stayDateTo of the existing rate.
	 */
	@Test
	public void updateRateCase04() throws Exception
	{
		Rate oldRate = rateRepository.save(existingRate);

		
        Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		newRate.setNights(3);
		newRate.setValue(9000);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		newRate = rateRepository.save(newRate);

		
		//update the new rate	
		//newRate.setRateId(newRate.getRateId());
		newRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		newRate.setValue(200.0);
		newRate.setNights(2);

		
		
		Rate updatedRate = rateServiceImpl.updateRate(newRate.getRateId(),newRate);
		List<Rate> list = rateRepository.findAll();

		
        //Old rate will be closed
		
		oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		oldRate.setNights(2);
		oldRate.setValue(1000);
		oldRate.setBungalowId((long) 1);
		oldRate.setClosedDate(LocalDate.now());
		
		//after split	
		//..........................no splitting
		
		System.out.println(list.contains(updatedRate));

		//assertTrue(list.contains(oldRate));
		assertTrue(list.contains(newRate));

	}
	
	//CASE05:---else if(rate.getStayDateFrom().isEqual(rate.getStayDateTo())&&rate.getStayDateFrom().isAfter(existingRate.getStayDateFrom())&&rate.getStayDateTo().isBefore(existingRate.getStayDateTo()))
	
	/**
	 * @throws Exception
	 * //Case:05- condition checks if the stayDateFrom of the new rate is equal the 
//            	 stayDateTo of the new rate and lies in between existing rate
              	 
	 */
	@Test
	public void updateRateCase05() throws Exception
	{
      Rate oldRate = rateRepository.save(existingRate);

		
        Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		newRate.setNights(3);
		newRate.setValue(9000);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		newRate = rateRepository.save(newRate);

		
		//update the new rate	
		//newRate.setRateId(newRate.getRateId());
		newRate.setStayDateFrom(LocalDate.parse("2024-03-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-03-01"));
		newRate.setValue(200.0);
		newRate.setNights(2);

		
		
		Rate updatedRate = rateServiceImpl.updateRate(newRate.getRateId(),newRate);
		List<Rate> list = rateRepository.findAll();

		
        //Old rate will be closed
		
		oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		oldRate.setNights(2);
		oldRate.setValue(1000);
		oldRate.setBungalowId((long) 1);
		oldRate.setClosedDate(LocalDate.now());
		
		//after split	
		
		//after that
		Rate AfterThat = new Rate();
		AfterThat.setStayDateFrom(LocalDate.parse("2024-03-02"));
		AfterThat.setStayDateTo(LocalDate.parse("2024-12-31"));
		AfterThat.setNights(2);
		AfterThat.setValue(1000);
		AfterThat.setBungalowId((long) 1);
		AfterThat.setClosedDate(null);
		
		//beforethat
		
		Rate BeforeThat = new Rate();
		BeforeThat.setStayDateFrom(LocalDate.parse("2024-01-01"));
		BeforeThat.setStayDateTo(LocalDate.parse("2024-02-29"));
		BeforeThat.setNights(2);
		BeforeThat.setValue(1000);
		BeforeThat.setBungalowId((long) 1);
		BeforeThat.setClosedDate(null);
		
		System.out.println(list.contains(updatedRate));

		assertTrue(list.contains(newRate));
		assertTrue(list.contains(BeforeThat));

		assertTrue(list.contains(AfterThat));

		assertTrue(list.contains(oldRate));


		
		

	}
	
	//CASE06---:else if(rate.getStayDateFrom().isEqual(existingRate.getStayDateFrom())&&rate.getStayDateTo().isAfter(existingRate.getStayDateTo()))

	/**
	 * @throws Exception
	 * //CASE: 06- condition checks if the stayDateFrom of the new rate is equal the 
//            	 stayDateFrom of the existing rate and if the stayDateTo of the new rate is after the 
//            	 stayDateTo of the existing rate.
	 */
	@Test
	public void updateRateCase06() throws Exception
	{
       Rate oldRate = rateRepository.save(existingRate);

		
        Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		newRate.setNights(3);
		newRate.setValue(9000);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		newRate = rateRepository.save(newRate);

		
		//update the new rate	
		//newRate.setRateId(newRate.getRateId());
		newRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-03-31"));
		newRate.setValue(200.0);
		newRate.setNights(2);

		
		
		Rate updatedRate = rateServiceImpl.updateRate(newRate.getRateId(),newRate);
		List<Rate> list = rateRepository.findAll();

		
        //Old rate will be closed
		
		oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		oldRate.setNights(2);
		oldRate.setValue(1000);
		oldRate.setBungalowId((long) 1);
		oldRate.setClosedDate(LocalDate.now());
		
		//after split	
		
		//after that
		Rate AfterThat = new Rate();
		AfterThat.setStayDateFrom(LocalDate.parse("2024-04-01"));
		AfterThat.setStayDateTo(LocalDate.parse("2024-12-31"));
		AfterThat.setNights(2);
		AfterThat.setValue(1000);
		AfterThat.setBungalowId((long) 1);
		AfterThat.setClosedDate(null);
		
		
		System.out.println(list.contains(updatedRate));

		assertTrue(list.contains(newRate));

		assertTrue(list.contains(AfterThat));

		assertTrue(list.contains(oldRate));

		

	}
	
	//CASE07--:else if(rate.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&rate.getStayDateTo().isEqual(existingRate.getStayDateTo()))
	
	/**
	 * @throws Exception
	 * //CASE: 07- condition checks if the stayDateFrom of the new rate is before the 
//            	 stayDateFrom of the existing rate and if the stayDateTo of the new rate is equal the 
//            	 stayDateTo of the existing rate
	 */
	@Test
	public void updateRateCase07() throws Exception
	{
        Rate oldRate = rateRepository.save(existingRate);

		
        Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		newRate.setNights(3);
		newRate.setValue(9000);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		newRate = rateRepository.save(newRate);

		
		//update the new rate	
		//newRate.setRateId(newRate.getRateId());
		newRate.setStayDateFrom(LocalDate.parse("2023-03-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		newRate.setValue(200.0);
		newRate.setNights(2);

		
		
		Rate updatedRate = rateServiceImpl.updateRate(newRate.getRateId(),newRate);
		List<Rate> list = rateRepository.findAll();

		
        //Old rate will be closed
		
		oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		oldRate.setNights(2);
		oldRate.setValue(1000);
		oldRate.setBungalowId((long) 1);
		oldRate.setClosedDate(LocalDate.now());
		
		//after split	
		
		
		System.out.println(list.contains(updatedRate));

		assertTrue(list.contains(newRate));


		assertTrue(list.contains(oldRate));

		

	}
	
	//CASE:08else if(rate.getStayDateFrom().isBefore(existingRate.getStayDateFrom())&&rate.getStayDateTo().isBefore(existingRate.getStayDateTo())&&rate.getStayDateTo().isAfter(existingRate.getStayDateFrom()))
	/**
	 * @throws Exception
	 * //CASE 08: if the new rate stay date from is before of the existing rate stay date from 
              	 //and stay date to of new rate is before stay date to of existing rate
              	 
	 */
	public void updateRateCase08() throws Exception
	{
       Rate oldRate = rateRepository.save(existingRate);

		
        Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		newRate.setNights(3);
		newRate.setValue(9000);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		newRate = rateRepository.save(newRate);

		
		//update the new rate	
		//newRate.setRateId(newRate.getRateId());
		newRate.setStayDateFrom(LocalDate.parse("2023-03-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-03-31"));
		newRate.setValue(200.0);
		newRate.setNights(2);

		
		
		Rate updatedRate = rateServiceImpl.updateRate(newRate.getRateId(),newRate);
		List<Rate> list = rateRepository.findAll();

		
        //Old rate will be closed
		
		oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		oldRate.setNights(2);
		oldRate.setValue(1000);
		oldRate.setBungalowId((long) 1);
		oldRate.setClosedDate(LocalDate.now());
		
		//after split	
		
		//after that
		Rate AfterThat = new Rate();
		AfterThat.setStayDateFrom(LocalDate.parse("2024-03-02"));
		AfterThat.setStayDateTo(LocalDate.parse("2024-12-31"));
		AfterThat.setNights(2);
		AfterThat.setValue(1000);
		AfterThat.setBungalowId((long) 1);
		AfterThat.setClosedDate(null);
		
		
		System.out.println(list.contains(updatedRate));

		assertTrue(list.contains(newRate));

		assertTrue(list.contains(AfterThat));

		assertTrue(list.contains(oldRate));

		

	}
	
	//CASE09--:else if(isExistingRateNewRate(existingRate, rate))
	/**
	 * @throws Exception
	 *  //CASE:08- replace all null with notnull if the all existing rate come in between the new rate
              	 
	 */
	public void updateRateCase09() throws Exception
	{
        Rate oldRate = rateRepository.save(existingRate);

		
        Rate newRate = new Rate();
		
		newRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		newRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		newRate.setNights(3);
		newRate.setValue(9000);
		newRate.setBungalowId((long) 1);
		newRate.setClosedDate(null);
		newRate = rateRepository.save(newRate);

		
		//update the new rate	
		//newRate.setRateId(newRate.getRateId());
		newRate.setStayDateFrom(LocalDate.parse("2023-03-01"));
		newRate.setStayDateTo(LocalDate.parse("2025-03-01"));
		newRate.setValue(200.0);
		newRate.setNights(2);

		
		
		Rate updatedRate = rateServiceImpl.updateRate(newRate.getRateId(),newRate);
		List<Rate> list = rateRepository.findAll();

		
        //Old rate will be closed
		
		oldRate.setStayDateFrom(LocalDate.parse("2024-01-01"));
		oldRate.setStayDateTo(LocalDate.parse("2024-12-31"));
		oldRate.setNights(2);
		oldRate.setValue(1000);
		oldRate.setBungalowId((long) 1);
		oldRate.setClosedDate(LocalDate.now());
		
		//after split	
		
		
		
		System.out.println(list.contains(updatedRate));

		assertTrue(list.contains(newRate));


		assertTrue(list.contains(oldRate));


	}
	
	
}
