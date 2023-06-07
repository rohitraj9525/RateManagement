package com.RateManagement.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.RateManagement.DTO.RateDTO;
import com.RateManagement.Entity.Bungalow;
import com.RateManagement.Entity.Rate;
import com.RateManagement.Exception.BungalowNotFoundException;
import com.RateManagement.Exception.RateNotFoundException;
import com.RateManagement.Repo.BungalowRepository;
//import com.RateManagement.Repo.BungalowRepository;
import com.RateManagement.Repo.RateRepository;
import com.RateManagement.Specification.RateSpecification;



@Service
public class RateServiceImpl implements RateService {

	@Autowired
    private final RateRepository rateRepository;

	@Autowired
	private BungalowService bungalowService;
	
    public RateServiceImpl(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    /**
     * @param rate
     * @return this method return save a rate Raw in the rate table
     */
    @Override
    public Rate createRate(Rate rate) {
    	Bungalow bungalow = bungalowService.getBungalowById(rate.getBungalowId());
    	if(bungalow==null) {
    		throw new BungalowNotFoundException("Bungalow with ID " + rate.getBungalowId() + " not found..!");
    	}
        return rateRepository.save(rate);
    }

    /**
     *@param id
     *@param rate
     *@return this return update the particular row
     */ 
    @Override
    public Rate updateRate(Long id, Rate rate) {
        Rate existingRate = getRateById(id)
;
        existingRate.setStayDateFrom(rate.getStayDateFrom());
        existingRate.setStayDateTo(rate.getStayDateTo());
        existingRate.setNights(rate.getNights());
        existingRate.setValue(rate.getValue());
        existingRate.setClosedDate(rate.getClosedDate());
        existingRate.setBungalowId(rate.getBungalowId());
        return rateRepository.save(existingRate);
    }

    /**
     *@param id
     *@return this method return boolean
     */
    @Override
    public void deleteRate(Long id) {
        rateRepository.deleteById(id)
;
    }

    /**
     *@param id
     *@return get rate by id
     */
    @Override
    public Rate getRateById(Long id) {
        return rateRepository.findById(id)

                .orElseThrow(() -> new RateNotFoundException("Rate not found with id: " + id));
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

    @Override
    public Page<Rate> getAllRates(Pageable pageable, Long id, LocalDate stayDateFrom, LocalDate stayDateTo,
    Integer nights, Double value, Long bungalowId) 
    {
    Specification<Rate> spec = RateSpecification.searchByCriteria(id, stayDateFrom, stayDateTo,
    nights, value, bungalowId);
       return rateRepository.findAll(spec, pageable);
    }
    
    
}