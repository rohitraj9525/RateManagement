package com.RateManagement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.RateManagement.Entity.Bungalow;
import com.RateManagement.Repo.BungalowRepository;
import com.RateManagement.Service.BungalowService;

@SpringBootTest
public class bungalowTest 
{
	@Autowired
	private BungalowRepository bungalowRepository;
	
	@Autowired
	private BungalowService bungalowService;
	
	
	Bungalow bungalow;
	
	@BeforeEach
	public void setUp()
	{
		bungalow = new Bungalow();
		
		bungalow.setBungalowName("SilverHeaven");
		bungalow.setBungalowType("Luxary3BHK");
		
		
	}
	
	@AfterEach
	public void tearDown()
	{
		bungalowRepository.deleteAll();
		bungalow = null;
	}
	
	//testing for create
	
	@Test
	public void createBungalow()
	{
		//save bungalow object
		Bungalow createdBungalow = bungalowService.createBungalow(bungalow);
		
		//veriefy the result 
		
	      assertThat(createdBungalow).isNotNull();
	      assertThat(createdBungalow.getBungalowId()).isNotNull();
	      assertThat(createdBungalow.getBungalowName()).isEqualTo(bungalow.getBungalowName());
	      assertThat(createdBungalow.getBungalowType()).isEqualTo(bungalow.getBungalowType());
	      

	}
	
	//testing for update
	
	@Test
	public void updateBungalow()
	{
		//save bungalow object
		Bungalow createdBungalow = bungalowService.createBungalow(bungalow);
		
		//update name
		bungalow.setBungalowName("TPrism");
		
		Bungalow updatedBungalow = bungalowService.updateBungalow(bungalow.getBungalowId(), bungalow);
		
        assertEquals("TPrism", updatedBungalow.getBungalowName());

		
	}
	
	
	//delete the bungalowobject by its id
	
	@Test
	public void deleteBungalow()
	{
		//save bungalow object
		Bungalow createdBungalow = bungalowService.createBungalow(bungalow);
		
		bungalowService.deleteBungalow(bungalow.getBungalowId());
		
		assertFalse(bungalowRepository.existsById(bungalow.getBungalowId()));
	}
	
	
	//get bungalow by its id
	
	@Test
	public void getBungalowById()
	{
		//save bungalow object
		Bungalow createdBungalow = bungalowService.createBungalow(bungalow);
		
		Bungalow getBungalowByID = bungalowService.getBungalowById(createdBungalow.getBungalowId());
		
		assertNotNull(getBungalowByID);
		assertEquals(bungalow.getBungalowId(), getBungalowByID.getBungalowId());
		assertEquals(bungalow.getBungalowName(), getBungalowByID.getBungalowName());
		assertEquals(bungalow.getBungalowType(), getBungalowByID.getBungalowType());
		
				
				
	}
	
	

}
