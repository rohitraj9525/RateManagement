package com.RateManagement.Service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.RateManagement.Entity.Rate;


public interface RateService {

    Rate createRate(Rate rate);
    

    Rate updateRate(Long id, Rate rate);

    void deleteRate(Long id);

    Rate getRateById(Long id);

    Page<Rate> getAllRates(Pageable pageable, Long id, LocalDate stayDateFrom, LocalDate stayDateTo,
    	    Integer nights, Double value, Long bungalowId, LocalDate closedDate);
    
   // public ByteArrayInputStream load();
}