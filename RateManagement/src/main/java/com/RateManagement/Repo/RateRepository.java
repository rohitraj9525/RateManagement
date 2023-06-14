package com.RateManagement.Repo;


import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.RateManagement.Entity.Rate;

/**
 * @author R.Raj
 * created rate repository for stored the rate related data into the database
 */

@Repository
public interface RateRepository extends JpaRepository<Rate, Long>,JpaSpecificationExecutor<Rate>
{
    // Add any custom repository methods if needed
	//public List<Rate> findByBungalow(Bungalow bungalow);
	//List<Rate> findAllByBungalowId(Long bungalowId);

	
    
    //List<Rate> findOverlappingRates(Long bungalowId, LocalDate stayDateFrom, LocalDate stayDateTo);
		    List<Rate> findByBungalowIdAndStayDateFromBeforeAndStayDateToAfter(
	            Long bungalowId, LocalDate stayDateTo, LocalDate stayDateFrom);
		    
		    List<Rate> findAll(Specification<Rate> specification);
		    
		   

	    	    
//	    @Query(value = "SELECT r FROM Rate r " +
//	            "WHERE r.bungalowId = :bungalowId " +
//	    		"AND r.closedDate IS NULL" + " AND r.nights= :nights " +
//	            "AND ((r.stayDateFrom <= :stayDateFrom AND r.stayDateTo >= :stayDateFrom) " + 
//	            "OR (r.stayDateFrom <= :stayDateTo AND r.stayDateTo >= :stayDateTo)"
//	            + "OR (r.stayDateFrom > :stayDateFrom AND r.stayDateTo < :stayDateTo)"
//	           + "OR (r.stayDateFrom = :#{#stayDateTo.plusDays(1)}) " +
//	            "OR (r.stayDateTo = :#{#stayDateFrom.minusDays(1)}) )")
//
//	     List<Rate> findOverlappingRates(@Param("bungalowId") Long bungalowId,
//	                                     @Param("stayDateFrom") LocalDate stayDateFrom,
//	                                     @Param("stayDateTo") LocalDate stayDateTo,
//	                                     @Param("nights") int nights);
	    
//	    @Query(value = "SELECT r FROM Rate r " +
//	            "WHERE r.bungalowId = :bungalowId " +
//	            "AND r.closedDate IS NULL" +
//	            " AND r.nights = :nights " +
//	            "AND ((r.stayDateFrom <= :stayDateFrom AND r.stayDateTo >= :stayDateFrom) " +
//	            "OR (r.stayDateFrom <= :stayDateTo AND r.stayDateTo >= :stayDateTo) " +
//	            "OR (r.stayDateFrom > :stayDateFrom AND r.stayDateTo < :stayDateTo) " +
//	            "OR (r.stayDateFrom = :#{#stayDateTo.plusDays(1)}) " +
//	            "OR (r.stayDateTo = :#{#stayDateFrom.minusDays(1)}) )" +
//	            "AND r.value = :value")
//	    List<Rate> overlappingRateMerge(@Param("bungalowId") Long bungalowId,
//	                                                 @Param("stayDateFrom") LocalDate stayDateFrom,
//	                                                 @Param("stayDateTo") LocalDate stayDateTo,
//	                                                 @Param("nights") int nights,
//	                                                 @Param("value") double value);
//	                                     
//	                                     
	    
	    

	    @Query(value = "SELECT r FROM Rate r WHERE r.stayDateFrom = :stayDateFrom " +
	            "AND r.stayDateTo = :stayDateTo AND r.nights = :nights " +
	            "AND r.value = :value AND r.bungalowId = :bungalowId "
	            )
	    Rate findByFields(@Param("stayDateFrom") LocalDate stayDateFrom,
	                      @Param("stayDateTo") LocalDate stayDateTo,
	                      @Param("nights") int nights,
	                      @Param("value") double value,
	                      @Param("bungalowId") Long bungalowId);
	    
	}
    
    



