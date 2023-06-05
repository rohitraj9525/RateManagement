package com.RateManagement.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.RateManagement.DTO.RateDTO;
import com.RateManagement.Entity.Bungalow;
import com.RateManagement.Entity.Rate;
import com.RateManagement.Repo.BungalowRepository;
//import com.RateManagement.Repo.BungalowRepository;
import com.RateManagement.Repo.RateRepository;



@Service
public class RateServiceImpl implements RateService {

    private final RateRepository rateRepository;
    private final BungalowRepository bungalowRepository;

    @Autowired
    public RateServiceImpl(RateRepository rateRepository, BungalowRepository bungalowRepository) {
        this.rateRepository = rateRepository;
        this.bungalowRepository = bungalowRepository;
    }

    //business logic for get all the rates 
    
    @Override
    public ResponseEntity<List<RateDTO>> getAllRates() {
        List<Rate> rates = rateRepository.findAll();
        List<RateDTO> rateDTOs = rates.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rateDTOs);
    }	
    
    //business logic for to get the rate by id
    
    @Override
    public ResponseEntity<RateDTO> getRateById(Long rateId) {
        Optional<Rate> rate = rateRepository.findById(rateId);
        return rate.map(r -> ResponseEntity.ok(convertToDTO(r)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    //method to convert rate entity to Rate DTO   
    
    private RateDTO convertToDTO(Rate rate) {
        RateDTO rateDTO = new RateDTO();
        rateDTO.setRateId(rate.getRateId());
        rateDTO.setStayDateFrom(rate.getStayDateFrom());
        rateDTO.setStayDateTo(rate.getStayDateTo());
        rateDTO.setNights(rate.getNights());
        rateDTO.setValue(rate.getValue());
        rateDTO.setBungalowId(rate.getBungalow().getBungalowId());
        rateDTO.setClosedDate(rate.getClosedDate());
        return rateDTO;
    }
}