package com.RateManagement;

import org.springframework.boot.test.context.SpringBootTest;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.RateManagement.Service.RateService;
import com.RateManagement.Specification.RateSpecification;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import com.RateManagement.Controller.RateController;
import com.RateManagement.Entity.*;



@AutoConfigureMockMvc
@SpringBootTest	
public class EndPointTesting 
    {
    	@Autowired
    	private MockMvc mockMvc;
    	
    	@MockBean
    	private RateService rateService;
    	
    	@Autowired
    	private ObjectMapper objectMapper;
    	
    	
    	/**
    	 * @throws Exception  
    	 * JUNIT testing for createRate
    	 */
    	@Test
    	public void TestCreateRate() throws Exception
    	{
    		Rate rate = new Rate();
    		
    		 //rate.setRateId((long) 1);
    		   LocalDate s= LocalDate.of(2023, 1, 1);
    	       LocalDate q = LocalDate.of(2023, 5, 10);
    	       rate.setRateId((long) 1);
    	       rate.setValue(100.0);
    	       rate.setNights(7);
    	       rate.setStayDateFrom(s);
    	       rate.setStayDateTo(q);
    	       rate.setClosedDate(null);
    	       rate.setBungalowId((long) 1);
    	       
    			when(rateService.createRate(any(Rate.class))).thenReturn(rate);
    			
    			mockMvc.perform((RequestBuilder) ( MockMvcRequestBuilders
    					.post("/api/v1/rates")
    					.contentType(MediaType.APPLICATION_JSON)
    					.content(asJsonString(rate))))
    					.andExpect(status().isCreated());
    			
    			verify(rateService, times(1)).createRate(any(Rate.class));
    						
    	}
    	
    	private static String asJsonString(Object obj) 
    	{
            try 
            {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                return objectMapper.writeValueAsString(obj);
            } catch (Exception e) 
            {
                throw new RuntimeException(e);
            }
        }
    	
    	
    // JUnit Testing for the UPDATERATE	
    	/**
    	 * @throws Exception
    	 * JUNIT testing for updateRate
    	 */
    	@Test
    	public void TestUpdateRate() throws Exception
    	{
    		Rate rate = new Rate();
    		
    		 LocalDate s= LocalDate.of(2023, 1, 1);
  	       LocalDate q = LocalDate.of(2023, 5, 10);
  	       rate.setRateId((long) 1);
  	       rate.setValue(100.0);
  	       rate.setNights(7);
  	       rate.setStayDateFrom(s);
  	       rate.setStayDateTo(q);
  	       rate.setClosedDate(null);
  	       rate.setBungalowId((long) 1);

    		//Rate rate = new Rate();
    		rate.setRateId((long) 1);
    		rate.setValue(200.0);
    		rate.setNights(3);
    		
    		when(rateService.updateRate(any(Long.class), any(Rate.class))).thenReturn(rate);
    		
    		mockMvc.perform(MockMvcRequestBuilders
    				.put("/api/v1/rates/" + rate.getRateId())
    				.contentType(MediaType.APPLICATION_JSON)
    				.content(asJsonString(rate)))
    		        .andExpect(status().isOk()); 
    		
    		verify(rateService, times(1)).updateRate(any(Long.class), any(Rate.class));
    	}
    	
    	
    	//juNIT TESTING FOR THE DELETERATE
    	
    	/**
    	 * JUNIT testing for delete rate
    	 */
    	@Test
    	public void TestDeleteRate()
    	{
    		Rate rate = new Rate();
    		
   		 LocalDate s= LocalDate.of(2023, 1, 1);
 	       LocalDate q = LocalDate.of(2023, 5, 10);
 	       rate.setRateId((long) 1);
 	       rate.setValue(100.0);
 	       rate.setNights(7);
 	       rate.setStayDateFrom(s);
 	       rate.setStayDateTo(q);
 	       rate.setClosedDate(null);
 	       rate.setBungalowId((long) 1);
 	       
 	       when(rateService.getRateById(anyLong())).thenReturn(rate);
            doNothing().when(rateService).deleteRate(any(Long.class));
 	       
 	       try 
 	       {
			mockMvc.perform(MockMvcRequestBuilders
					   .delete("/api/v1/rates/" + rate.getRateId()))
					   .andExpect(status().isNoContent());
		   }      catch (Exception e) 
 	       {
			// TODO Auto-generated catch block
			e.printStackTrace();
		   }
    	   
    	
    	verify(rateService, times(1)).deleteRate(any(Long.class));
    	
    	
    	
    	}
    	
    	//JUnit testing for the GetRateById
    	
    	/**
    	 * JUNIT testing for getratebyid
    	 */
    	@Test
    	public void TestRateById()
    	{
    		Rate rate = new Rate();
    		
      		 LocalDate s= LocalDate.of(2023, 1, 1);
    	       LocalDate q = LocalDate.of(2023, 5, 10);
    	       rate.setRateId((long) 1);
    	       rate.setValue(100.0);
    	       rate.setNights(7);
    	       rate.setStayDateFrom(s);
    	       rate.setStayDateTo(q);
    	       rate.setClosedDate(null);
    	       rate.setBungalowId((long) 1);
    	       
    	       when(rateService.getRateById(any(Long.class))).thenReturn(rate);
    	       
    	       try {
				mockMvc.perform(MockMvcRequestBuilders
						.get("/api/v1/rates/" + rate.getRateId()))
						//.contentType(MediaType.APPLICATION_JSON)
						//.content(asJsonString(rate)))
				        .andExpect(status().isOk());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
       		verify(rateService, times(1)).getRateById(any(Long.class));


    	}
    	
    	
    	//JUNIT Test for Get All Rates 
    	/**
    	 * JUNIT testing for get all rates
    	 */
    	@Test
    	public void TestGetAllRates()
    	{
    		Rate rate = new Rate();
    		LocalDate s= LocalDate.of(2023, 1, 1);
 	       LocalDate q = LocalDate.of(2023, 5, 10);
 	       rate.setRateId((long) 1);
 	       rate.setValue(100.0);
 	       rate.setNights(7);
 	       rate.setStayDateFrom(s);
 	       rate.setStayDateTo(q);
 	       rate.setClosedDate(null);
 	       rate.setBungalowId((long) 1);
 	       
 	      Rate rate1 = new Rate();
  		LocalDate si= LocalDate.of(2024, 1, 1);
	       LocalDate qi = LocalDate.of(2024, 5, 10);
	       rate1.setRateId((long) 2);
	       rate1.setValue(1000.0);
	       rate1.setNights(17);
	       rate1.setStayDateFrom(si);
	       rate1.setStayDateTo(qi);
	       rate1.setClosedDate(null);
	       rate1.setBungalowId((long) 1);
	       
	       List<Rate> lists = new ArrayList<>();
	       
	       lists.add(rate1);
	       lists.add(rate);
	       
	       Pageable pageable = Pageable.ofSize(20);
	    	
	    	Page<Rate> pagedRes = new PageImpl<>(lists);
	    	Specification<Rate> spec = RateSpecification.searchByCriteria(null, null, null, null, null, null, null);


	    		       
	       when(rateService.getAllRates(pageable, null, null, null, null, null, null, null)).thenReturn(pagedRes);
	       
	       
	       try {
				mockMvc.perform(MockMvcRequestBuilders
						.get("/api/v1/rates/filter")).andExpect(status().isOk());
						//.contentType(MediaType.APPLICATION_JSON)
						//.content(asJsonString(rate)))
				        //.andExpect(content().contentType(MediaType.APPLICATION_JSON));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

	       
 	       
 	      verify(rateService, times(1)).getAllRates(pageable, null, null, null, null, null, null, null) ;
 	       

    	}
    }

