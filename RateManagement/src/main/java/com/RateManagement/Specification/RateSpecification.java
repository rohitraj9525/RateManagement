package com.RateManagement.Specification;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.*;


import com.RateManagement.Entity.Rate;

import jakarta.persistence.criteria.Predicate;

public class RateSpecification {

    /**
     * @param id
     * @param stayDateFrom
     * @param stayDateTo
     * @param nights
     * @param value
     * @param bungalowId
     * @return logic for specification of get rate by id, staydatefrom, staydateto, nights, value, bungalowId
     */
    public static Specification<Rate> searchByCriteria(Long id, LocalDate stayDateFrom, LocalDate stayDateTo,
                                                       Integer nights, Double value, Long bungalowId, LocalDate closedDate) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null) {
                predicates.add(builder.equal(root.get("id"), id));
            }

            if (stayDateFrom != null) {
            	//if(closedDate!=LocalDate.now())
            		
                predicates.add(builder.greaterThanOrEqualTo(root.get("stayDateFrom"), stayDateFrom));
            	predicates.add(builder.isNull(root.get("closedDate")));
            }

            if (stayDateTo != null) {
            	//if(closedDate!=LocalDate.now())
                predicates.add(builder.lessThanOrEqualTo(root.get("stayDateTo"), stayDateTo));
            	predicates.add(builder.isNull(root.get("closedDate")));
            }

            if (nights != null) {
                predicates.add(builder.equal(root.get("nights"), nights));
            }

            if (value != null) {
                predicates.add(builder.equal(root.get("value"), value));
            }

            if (bungalowId != null) {
                predicates.add(builder.equal(root.get("bungalowId"), bungalowId));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}