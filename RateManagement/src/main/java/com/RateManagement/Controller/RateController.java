package com.RateManagement.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RateManagement.DTO.RateDTO;
import com.RateManagement.Service.RateService;

@RestController
@RequestMapping("/rates")
public class RateController {

    private final RateService rateService;

    @Autowired
    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    
    /**
     * @param Just passed Request mapping API   
     * @return this REST API will return the All ROW of the Rate table which is present in the database. 
     */

    @GetMapping
    public ResponseEntity<List<RateDTO>> getAllRates() {
        return rateService.getAllRates();
    }
    
    /**
     * @param Just passed Request mapping API with particular rateId  
     * @return this REST API will return the particular ROW of the Rate table which is present in the database. 
     */

        
   
    @GetMapping("/{rateId}")
    public ResponseEntity<RateDTO> getRateById(@PathVariable Long rateId) {
        return rateService.getRateById(rateId);
    }
    
}