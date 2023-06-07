package com.RateManagement.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.RateManagement.DTO.RateDTO;
import com.RateManagement.Entity.Rate;
import com.RateManagement.Service.RateService;

@RestController
@RequestMapping("/rates")
public class RateController {

    private final RateService rateService;

    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    /**
     * @param rate
     * @return create rate
     */
    @PostMapping
    public ResponseEntity<Rate> createRate(@RequestBody Rate rate) {
        Rate createdRate = rateService.createRate(rate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRate);
    }

    /**
     * @param id
     * @param rate
     * @return update rate
     */
    @PutMapping("/{id}")
    public ResponseEntity<Rate> updateRate(@PathVariable("id") Long id, @RequestBody Rate rate) {
        Rate updatedRate = rateService.updateRate(id, rate);
        return ResponseEntity.ok(updatedRate);
    }

    /**
     * @param id
     * @return boolean this mehod is for delete the row by id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRate(@PathVariable("id") Long id) {
        rateService.deleteRate(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * @param id
     * @return get rate by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Rate> getRateById(@PathVariable("id") Long id) {
        Rate rate = rateService.getRateById(id);
        return ResponseEntity.ok(rate);
    }

    
        /**
         * @param pageable
         * @param id
         * @param stayDateFrom
         * @param stayDateTo
         * @param nights
         * @param value
         * @param bungalowId
         * @return get rate by specification of all fields
         */
        @GetMapping
        public ResponseEntity<List<Rate>> getAllRates(Pageable pageable,
                                                      @RequestParam(required = false) Long id,
                                                      @RequestParam(required = false) LocalDate stayDateFrom,
                                                      @RequestParam(required = false) LocalDate stayDateTo,
                                                      @RequestParam(required = false) Integer nights,
                                                      @RequestParam(required = false) Double value,
                                                      @RequestParam(required = false) Long bungalowId) {
            Page<Rate> rates = rateService.getAllRates(pageable, id, stayDateFrom, stayDateTo,
                    nights, value, bungalowId);
            return ResponseEntity.ok(rates.getContent());
        }
 }
    