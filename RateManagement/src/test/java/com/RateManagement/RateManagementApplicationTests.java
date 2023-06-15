package com.RateManagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.RateManagement.Entity.Rate;
import com.RateManagement.Repo.RateRepository;
import com.RateManagement.Service.RateService;
import java.util.*;
@SpringBootTest
public class RateManagementApplicationTests {

    @Mock
    private RateRepository rateRepository;

    @InjectMocks
    private RateService rateService;

    @Test
    public void testGetAllRates() 
    {
        // Mock the page data
    	
    	List<Rate> rates = new ArrayList<>();
    	rates.add(new Rate(1l, LocalDate.of(2022, 1, 1),LocalDate.of(2022, 1, 1), 1, 3000, 1l, null));
    	rates.add(new Rate(1l, LocalDate.of(2022, 1, 1),LocalDate.of(2022, 12, 31), 1, 5000, 2l, null));
    	
    	//set the mock behaviour
       Mockito.when(rateRepository.findAll()).thenReturn(rates);
       
       //Invokes the methoss
       
       List<Rate> result = rateService.getAllRates();

        
    }
    
    @Test
    public void testGetRateById1() {
        // Prepare test data
        Long rateId = 1L;
        Rate rate = new Rate(rateId, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 12, 31), 1, 3000, 10l, null);

        // Set up mock behavior
        Mockito.when(rateRepository.findById(rateId)).thenReturn(Optional.of(rate));

        // Invoke the method
        Rate result = rateService.getRateById(rateId);

        // Verify the result
        Assert.assertEquals(rate, result);
    }
    
    @Test
    public void testGetRateById() {
        // Arrange
        Long rateId = 1L;
        Rate rate = new Rate();
        rate.setRateId(rateId);
        // Set other properties of the rate object as needed

        when(rateRepository.findById(rateId)).thenReturn(Optional.of(rate));

        // Act
        Rate result = rateService.getRateById(rateId);

        // Assert
        verify(rateRepository, times(1)).findById(rateId);
        assertNotNull(result);
        assertEquals(rate, result);
    }
    
    @Test
    public void testDeleteRate() {
        // Prepare test data
        Long rateId = 1L;

        // Invoke the method
        rateService.deleteRate(rateId);

        // Verify the mock behavior
        Mockito.verify(rateRepository, Mockito.times(1)).deleteById(rateId);
    }
    
    @Test
    public void testGetRateById_ExistingId_ShouldReturnRate() {
        // Arrange
        Long rateId = 1L;
        Rate rate = new Rate();
        rate.setRateId(rateId);
        when(rateRepository.findById(rateId)).thenReturn(Optional.of(rate));

        // Act
        Rate result = rateService.getRateById(rateId);

        // Assert
        assertNotNull(result);
        assertEquals(rateId, result.getRateId());
        // Add more assertions for other properties if needed

        // Verify that the repository method was called
        verify(rateRepository, times(1)).findById(rateId);
    }

    @Test(expected = RateNotFoundException.class)
    public void testGetRateById_NonExistingId_ShouldThrowRateNotFoundException() {
        // Arrange
        Long rateId = 1L;
        when(rateRepository.findById(rateId)).thenReturn(Optional.empty());

        // Act
        rateService.getRateById(rateId);

        // The expected exception will be thrown, so no assertions are needed here

        // Verify that the repository method was called
        verify(rateRepository, times(1)).findById(rateId);
    }
}