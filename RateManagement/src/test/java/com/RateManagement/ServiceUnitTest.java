package com.RateManagement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.RateManagement.Entity.Bungalow;
import com.RateManagement.Entity.Rate;
import com.RateManagement.Repo.BungalowRepository;
import com.RateManagement.Repo.RateRepository;
import com.RateManagement.Service.BungalowService;
import com.RateManagement.Service.RateService;
import com.RateManagement.Service.RateServiceImpl;
import com.RateManagement.Specification.RateSpecification;

import jakarta.persistence.criteria.Predicate;

@SpringBootTest
public class ServiceUnitTest {

    @Mock
    private RateRepository rateRepository;
    
    @Mock
    BungalowService bungalowService;
    
    @Mock
    RateSpecification rateSpecification;

    @InjectMocks
    private RateServiceImpl rateServiceImpl;

    private Rate rate;
    private Rate rate1;
    private Bungalow bungalow = new Bungalow();

    @BeforeEach
    void setUp() {
        // Create a sample Rate object for testing
        rate = new Rate();
        rate.setStayDateFrom(LocalDate.of(2023, 6, 1));
        rate.setStayDateTo(LocalDate.of(2023, 6, 8));
        rate.setNights(7);
        rate.setValue(100.0);
        rate.setBungalowId((long) 1);
        rate.setClosedDate(null);
        
        rate1 = new Rate();
        rate1.setStayDateFrom(LocalDate.of(2023, 6, 1));
        rate1.setStayDateTo(LocalDate.of(2023, 6, 8));
        rate1.setNights(8);
        rate1.setValue(1000.0);
        rate1.setBungalowId((long) 1);
        rate1.setClosedDate(null);

        
        
        //bungalow.setBungalowId((long)1);
        bungalow.setBungalowName("Silver Heavens");
        bungalow.setBungalowType("3 BHK");
        
    }

    //J-Unit testing for the create rate

    @Test
    @DisplayName("Test createRate method")
    public void testCreateRate() {
        // Mock the behavior of the repository
    	when(rateRepository.save(rate)).thenReturn(rate);
    	when(bungalowService.getBungalowById(rate.getBungalowId())).thenReturn(bungalow);
        assertThat(rateServiceImpl.createRate(rate)).isEqualTo(rate);

        
    }
    
    
    //J-Unit testing for the delete rate
    @Test
    @DisplayName("deleteRate method")
    public void testdeleteRate()
    {
    	//Mock the behavior of the repository
    	when(rateRepository.save(rate)).thenReturn(rate);
    	when(bungalowService.getBungalowById(rate.getBungalowId())).thenReturn(bungalow);
    	
		rateServiceImpl.deleteRate(rate.getRateId());

		assertFalse(rateRepository.existsById(rate.getRateId()));   	
    	
    }
    
    //J-Unit testing for he update rate
    
    @Test
    @DisplayName("updateRate method")
    public void testupdateRate()
    {
    	
    	rate.setRateId((long) 1);
    	rate.setStayDateFrom(LocalDate.parse("2023-02-01"));
		rate.setStayDateTo(LocalDate.parse("2023-12-31"));
		rate.setNights(3);
		rate.setValue(1500);
		
		when(rateRepository.save(rate)).thenReturn(rate);
		when(rateRepository.findById(anyLong())).thenReturn(Optional.of(rate));
    	when(bungalowService.getBungalowById(rate.getBungalowId())).thenReturn(bungalow);
		assertThat(rateServiceImpl.updateRate(rate.getRateId(), rate)).isEqualTo(rate);



    }
    
    //Test for get rate by id
    @Test
    @DisplayName("getRateById method")
    public void testgetRateById()
    {
    	
    	rate.setRateId((long) 1);
    	
 

    	when(rateRepository.save(rate)).thenReturn(rate);
		when(rateRepository.findById(anyLong())).thenReturn(Optional.of(rate));

    	when(bungalowService.getBungalowById(rate.getBungalowId())).thenReturn(bungalow);
    	
    	assertThat(rateServiceImpl.getRateById(rate.getRateId())).isEqualTo(rate);
    	
    }
    
    //Test for get All Rates
    
    @Test
    @DisplayName("getAllRates method")
    public void testGetAllRates() throws Exception
    {
    	
    	List<Rate> list = new ArrayList<>();
    	list.add(rate);
    	list.add(rate1);
    	
    	List<Rate> list1 = new ArrayList<>();
    	list1.add(rate);
    	list1.add(rate1);
    	
    	Pageable pageable = Pageable.ofSize(20);
    	
    	Page<Rate> pagedRes = new PageImpl<>(list);
    	Page<Rate> pagedRes1 = new PageImpl<>(list1);
    	Specification<Rate> spec = RateSpecification.searchByCriteria(null, null, null, null, null, null, null);
    	
    	
    	
       	when(rateRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(pagedRes1);

    	assertThat(rateServiceImpl.getAllRates(pageable, null, null, null, null, null, null, null)).isEqualTo(pagedRes);
     	
        
    }
    
    //Test for get All Rates by values and nights
    
    @Test
    @DisplayName("getRateByValuesAndNights methods")
    public void testGetAllRatesByNightsAndValue()
    {
    	List<Rate> list = new ArrayList<>();
    	list.add(rate);
    	list.add(rate1);
    	
    	LocalDate stayDate = LocalDate.of(2023, 06, 1);
    	LocalDate stayDateto = LocalDate.of(2023, 06, 8);

    	
    	Pageable pageable = Pageable.ofSize(20);
    	
    	Page<Rate> pageTemp = new PageImpl<>(list);
    	
    	Specification<Rate> spec = RateSpecification.searchByCriteria(null, null, null, 8, 100.0, null, null);
    	
       	when(rateRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(pageTemp);
    	
    	assertThat(rateServiceImpl.getAllRates(pageable, null, null, null, 8, 100.0, null, null)).isEqualTo(pageTemp);
    	
    	
    	    	
    }
    
    // j-UNIT testing get rate by bungalowId
    @Test
    @DisplayName("getRateByBungalowId method")
    public void testRateByBungalowId()
    {
    	List<Rate> list = new ArrayList<>();
    	list.add(rate);
    	list.add(rate1);
    	
    	LocalDate stayDate = LocalDate.of(2023, 06, 1);
    	LocalDate stayDateto = LocalDate.of(2023, 06, 8);

    	
    	Pageable pageable = Pageable.ofSize(20);
    	
    	Page<Rate> pageTemp = new PageImpl<>(list);
    	
    	Specification<Rate> spec = RateSpecification.searchByCriteria(null, null, null, null, null, (long) 3, null);
    	
       	when(rateRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(pageTemp);
    	
    	assertThat(rateServiceImpl.getAllRates(pageable, null, null, null, null, null, (long) 3, null)).isEqualTo(pageTemp);
    	
    	

    	
    }
    
    //J-Unit Testing for to get rate by stayDateFrom and stayDateEnd
    @Test
    @DisplayName("getRateByDates methods")
    public void testGetRatesByDates()
    {
    	List<Rate> list = new ArrayList<>();
    	list.add(rate);
    	list.add(rate1);
    	
    	LocalDate stayDate = LocalDate.of(2024, 06, 1);
    	LocalDate stayDateto = LocalDate.of(2024, 06, 8);

    	
    	Pageable pageable = Pageable.ofSize(20);
    	
    	Page<Rate> pageTemp = new PageImpl<>(list);
    	
    	Specification<Rate> spec = RateSpecification.searchByCriteria(null, stayDate, stayDateto, null, null, null, null);
    	
       	when(rateRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(pageTemp);
    	
    	assertThat(rateServiceImpl.getAllRates(pageable, null, stayDate, stayDateto, null, null, null, null)).isEqualTo(pageTemp);
    	
    }   
    
    
    //J-Unit Testing for to get rate by stayDateFrom

    @Test
    @DisplayName("getRateByDates methods")
    public void testGetRatesByStartDates()
    {
    	List<Rate> list = new ArrayList<>();
    	list.add(rate);
    	list.add(rate1);
    	
    	LocalDate stayDate = LocalDate.of(2024, 06, 1);

    	
    	Pageable pageable = Pageable.ofSize(20);
    	
    	Page<Rate> pageTemp = new PageImpl<>(list);
    	
    	Specification<Rate> spec = RateSpecification.searchByCriteria(null, stayDate, null, null, null, (long) 3, null);
    	
       	when(rateRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(pageTemp);
    	
    	assertThat(rateServiceImpl.getAllRates(pageable, null, stayDate, null, null, null, null, null)).isEqualTo(pageTemp);
    }
    
    //J-Unit Testing for to get rate by stayDateFrom

    @Test
    @DisplayName("getRateByDates methods")
    public void testGetRatesByStartDatestTo()
    {
    	List<Rate> list = new ArrayList<>();
    	list.add(rate);
    	list.add(rate1);
    	
    	LocalDate stayDateto = LocalDate.of(2024, 06, 8);

    	
    	Pageable pageable = Pageable.ofSize(20);
    	
    	Page<Rate> pageTemp = new PageImpl<>(list);
    	
    	Specification<Rate> spec = RateSpecification.searchByCriteria(null, null, stayDateto, null, null, null, null);
    	
       	when(rateRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(pageTemp);
    	
    	assertThat(rateServiceImpl.getAllRates(pageable, null, null, stayDateto, null, null, null, null)).isEqualTo(pageTemp);
    }
    
    //J-Unit Testing for to get rate by nights,value and stayDateto
    
    @Test
    @DisplayName("getRateByDates methods")
    public void testGetRatesByStartDatestToNightsValue()
    {
    	List<Rate> list = new ArrayList<>();
    	list.add(rate);
    	list.add(rate1);
    	
    	LocalDate stayDateto = LocalDate.of(2024, 06, 8);

    	
    	Pageable pageable = Pageable.ofSize(20);
    	
    	Page<Rate> pageTemp = new PageImpl<>(list);
    	
    	Specification<Rate> spec = RateSpecification.searchByCriteria(null, null, stayDateto, 8, 100.0, null, null);
    	
       	when(rateRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(pageTemp);
    	
    	assertThat(rateServiceImpl.getAllRates(pageable, null, null, stayDateto, 8, 100.0, null, null)).isEqualTo(pageTemp);
    }
    
}
