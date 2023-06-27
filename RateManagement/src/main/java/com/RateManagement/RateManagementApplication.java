package com.RateManagement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RateManagementApplication 
{


	public static void main(String[] args) 
	{
		SpringApplication.run(RateManagementApplication.class, args);
	}

}


