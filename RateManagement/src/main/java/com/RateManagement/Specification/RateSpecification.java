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
            	predicates.add(builder.isNull(root.get("closedDate")));

            }

            if (value != null) {
                predicates.add(builder.equal(root.get("value"), value));
            	predicates.add(builder.isNull(root.get("closedDate")));

            }

            if (bungalowId != null) {
                predicates.add(builder.equal(root.get("bungalowId"), bungalowId));
            	predicates.add(builder.isNull(root.get("closedDate")));

            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
    
    public static Specification<Rate> findOverlappingRates(
            Long bungalowId,
            LocalDate stayDateFrom,
            LocalDate stayDateTo,
            int nights
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("bungalowId"), bungalowId));
            predicates.add(criteriaBuilder.isNull(root.get("closedDate")));
            predicates.add(criteriaBuilder.equal(root.get("nights"), nights));

            Predicate stayDateFromOverlaps = criteriaBuilder.and(
                    criteriaBuilder.lessThanOrEqualTo(root.get("stayDateFrom"), stayDateFrom),
                    criteriaBuilder.greaterThanOrEqualTo(root.get("stayDateTo"), stayDateFrom)
            );
            Predicate stayDateToOverlaps = criteriaBuilder.and(
                    criteriaBuilder.lessThanOrEqualTo(root.get("stayDateFrom"), stayDateTo),
                    criteriaBuilder.greaterThanOrEqualTo(root.get("stayDateTo"), stayDateTo)
            );
            Predicate stayDateWithinRange = criteriaBuilder.and(
                    criteriaBuilder.greaterThan(root.get("stayDateFrom"), stayDateFrom),
                    criteriaBuilder.lessThan(root.get("stayDateTo"), stayDateTo)
            );
            Predicate stayDateToPlusOneDay = criteriaBuilder.equal(root.get("stayDateFrom"), stayDateTo.plusDays(1));
            Predicate stayDateFromMinusOneDay = criteriaBuilder.equal(root.get("stayDateTo"), stayDateFrom.minusDays(1));


            predicates.add(criteriaBuilder.or(stayDateFromOverlaps, stayDateToOverlaps, stayDateWithinRange,stayDateToPlusOneDay,stayDateFromMinusOneDay));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    

        public static Specification<Rate> overlappingRateMerge(
                Long bungalowId,
                LocalDate stayDateFrom,
                LocalDate stayDateTo,
                int nights,
                double value
        ) {
            return (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(criteriaBuilder.equal(root.get("bungalowId"), bungalowId));
                predicates.add(criteriaBuilder.isNull(root.get("closedDate")));
                predicates.add(criteriaBuilder.equal(root.get("nights"), nights));

                Predicate stayDateFromOverlaps = criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(root.get("stayDateFrom"), stayDateFrom),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("stayDateTo"), stayDateFrom)
                );
                Predicate stayDateToOverlaps = criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(root.get("stayDateFrom"), stayDateTo),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("stayDateTo"), stayDateTo)
                );
                Predicate stayDateWithinRange = criteriaBuilder.and(
                        criteriaBuilder.greaterThan(root.get("stayDateFrom"), stayDateFrom),
                        criteriaBuilder.lessThan(root.get("stayDateTo"), stayDateTo)
                );
                Predicate stayDateToPlusOneDay = criteriaBuilder.equal(root.get("stayDateFrom"), stayDateTo.plusDays(1));
                Predicate stayDateFromMinusOneDay = criteriaBuilder.equal(root.get("stayDateTo"), stayDateFrom.minusDays(1));

                predicates.add(criteriaBuilder.or(stayDateFromOverlaps, stayDateToOverlaps, stayDateWithinRange,
                        stayDateToPlusOneDay, stayDateFromMinusOneDay));
                predicates.add(criteriaBuilder.equal(root.get("value"), value));

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
        }
    }
