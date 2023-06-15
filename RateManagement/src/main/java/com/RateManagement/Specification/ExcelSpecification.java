package com.RateManagement.Specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.RateManagement.Entity.Rate;

import jakarta.persistence.criteria.Predicate;
import java.util.*;

public class ExcelSpecification {
    
    public static Specification<Rate> filterByCriteria(Long bungalowId, Integer nights, String stayDateFrom,
            String stayDateTo, Double value, String closedDate) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (bungalowId != null) {
                predicates.add(criteriaBuilder.equal(root.get("bungalowId"), bungalowId));
            }
            if (nights != null) {
                predicates.add(criteriaBuilder.equal(root.get("nights"), nights));
            }
            if (stayDateFrom != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("stayDateFrom"), LocalDate.parse(stayDateFrom)));
            }
            if (stayDateTo != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("stayDateTo"), LocalDate.parse(stayDateTo)));
            }
            if (value != null) {
                predicates.add(criteriaBuilder.equal(root.get("value"), value));
            }

            // Filter by null closedDate
            predicates.add(criteriaBuilder.isNull(root.get("closedDate")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}