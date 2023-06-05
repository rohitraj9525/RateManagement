package com.RateManagement.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.RateManagement.DTO.RateDTO;

public interface RateService 
{
    //public List<RateDTO> getALLRate();
    public ResponseEntity<List<RateDTO>> getAllRates();
    public ResponseEntity<RateDTO> getRateById(Long rateId);
    //public ResponseEntity<RateDTO> createRate(RateDTO rateDTO);




}