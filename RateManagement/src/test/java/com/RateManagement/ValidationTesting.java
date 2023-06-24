package com.RateManagement;

import org.springframework.boot.test.context.SpringBootTest;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.RateManagement.Service.ExcelService;
import com.RateManagement.Service.RateService;
import com.RateManagement.Specification.RateSpecification;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.RateManagement.Entity.*;



@AutoConfigureMockMvc
@SpringBootTest	
public class ValidationTesting 
    {
    	@Autowired
    	private MockMvc mockMvc;
    	
    	@MockBean
    	private RateService rateService;
    	
    	@Autowired
    	private ObjectMapper objectMapper;
    	
    	
    	/**
    	 * @throws Exception
    	 * 
    	 * validation testing by JUNIT for create rate staydatefrom>staydateto
    	 */
    	@Test
    	public void TestCreateRate() throws Exception
    	{
    		Rate rate = new Rate();
    		
    		 //rate.setRateId((long) 1);
    		   LocalDate s= LocalDate.of(2023, 1, 10);
    	       LocalDate q = LocalDate.of(2023, 1, 5);
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
    					.andExpect(status().isBadRequest());
    			
    						
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
    	
    	
    	/**
    	 * @throws Exception
    	 * validation testing by JUNIT for createRate with null staydate
    	 */
    	@Test
    	public void createRate_withNullStayDates_shouldReturnBadRequest() throws Exception
    	{
    		Rate rate = new Rate();
    		LocalDate s= LocalDate.of(2023, 1, 10);
 	       LocalDate q = LocalDate.of(2023, 1, 5);
 	       rate.setRateId((long) 1);
 	       rate.setValue(100.0);
 	       rate.setNights(7);
 	       rate.setStayDateFrom(null);
 	       rate.setStayDateTo(null);
 	       rate.setClosedDate(null);
 	       rate.setBungalowId((long) 1);
 	       
 	      when(rateService.createRate(any(Rate.class))).thenReturn(rate);
			
			mockMvc.perform((RequestBuilder) ( MockMvcRequestBuilders
					.post("/api/v1/rates")
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(rate))))
					.andExpect(status().isBadRequest());
 	       
    	}
    	
    	
    	/**
    	 * @throws Exception
    	 * Junit test for deleteRate for validation
    	 */
    	@Test
    	public void deleteRate_withInvalidRateId_shouldReturnNotFound() throws Exception
    	{
    		Rate rate = new Rate();
    		
   		 LocalDate s= LocalDate.of(2023, 1, 1);
 	       LocalDate q = LocalDate.of(2023, 5, 10);
 	       rate.setRateId((long) 987);
 	       rate.setValue(100.0);
 	       rate.setNights(7);
 	       rate.setStayDateFrom(s);
 	       rate.setStayDateTo(q);
 	       rate.setClosedDate(null);
 	       rate.setBungalowId((long) 1);
 	       
 	       when(rateService.getRateById(anyLong())).thenReturn(rate);
 	       //doThrow(new RateNotFoundException(long.class)).when(rateService).deleteRate(long.class);
 	       
 	       try 
 	       {
			mockMvc.perform(MockMvcRequestBuilders
					   .delete("/api/v1/rates/", (long) 45))
					   .andExpect(status().isNotFound());
		   }      catch (Exception e) 
 	       {
			// TODO Auto-generated catch block
			e.printStackTrace();
		   }
    	  
    	}
    	
    	/**
    	 * junit testing for getRateById for validation
    	 */
    	@Test
    	public void getRateById_WithInvalid_shouldReturnNotFound()
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
						.get("/api/v1/rates/" ,(long) 23))
						//.contentType(MediaType.APPLICATION_JSON)
						//.content(asJsonString(rate)))
				        .andExpect(status().isNotFound());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
       		//verify(rateService, times(1)).getRateById(any(Long.class));


    	}
    	
    	
    	/**
    	 * @throws Exception
    	 * 
    	 * checking validation testing for update rate if we update with invalid data
    	 */
    	@Test
    	public void updateRateById_withInvalidRateData_shouldReturnBadRequest() throws Exception
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
    		rate.setStayDateFrom(null);
    		
    		when(rateService.updateRate(any(Long.class), any(Rate.class))).thenReturn(rate);
    		
    		mockMvc.perform(MockMvcRequestBuilders
    				.put("/api/v1/rates/" + rate.getRateId())
    				.contentType(MediaType.APPLICATION_JSON)
    				.content(asJsonString(rate)))
    		        .andExpect(status().isBadRequest()); 
    		
    		//verify(rateService, times(1)).updateRate(any(Long.class), any(Rate.class));
    	}
    	
    	/**
    	 * @throws Exception
    	 * did validation testing for update rate if we pass wrong rateid
    	 */
    	@Test
    	public void updateRateById_withInvalid_shouldReturnNotFound() throws Exception
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
    				.put("/api/v1/rates/" ,(long) 34))
    				//.contentType(MediaType.APPLICATION_JSON)
    				//.content(asJsonString(rate)))
    		        .andExpect(status().isNotFound()); 
    		
    		//verify(rateService, times(1)).updateRate(any(Long.class), any(Rate.class));
    	}
    	
    	
    	/**
    	 * @throws Exception
    	 * validation testing by JUNIT for createRate with null staydate
    	 */
    	@Test
    	public void TestCreateRatewith_stayDateFromMoreThan_stayDateTo() throws Exception
    	{
    		Rate rate = new Rate();
    		
    		 //rate.setRateId((long) 1);
    		   LocalDate s= LocalDate.of(2023, 1, 10);
    	       LocalDate q = LocalDate.of(2023, 1, 5);
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
    					.andExpect(status().isBadRequest());
    			
    						
    	}
    	
    	//...........................
    	
    	
    	//Validation testing for Import excel file
    	
    	/**
    	 * @throws Exception
    	 */
    	@Test
    	public void testImportRatesFromExcel_WithEmptyFile_ShouldReturnBadRequest() throws Exception
    	{
    		MockMultipartFile emptyFile = new MockMultipartFile("file", " " , MediaType.APPLICATION_OCTET_STREAM_VALUE, new byte[0]);
    		
    		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/rates/import")
    				.file(emptyFile))
    		        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    	}
    	
    	  	
    	
    }
    	