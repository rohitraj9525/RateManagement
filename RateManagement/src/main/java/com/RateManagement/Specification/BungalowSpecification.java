package com.RateManagement.Specification;
import org.springframework.data.jpa.domain.Specification;
import java.util.*;

import com.RateManagement.Entity.*;

import jakarta.persistence.criteria.Predicate;

public class BungalowSpecification {

    /**
     * @param bungalowName
     * @param bungalowType
     * @return logic for getting bungalow details by id, name and type
     */
    public static Specification<Bungalow> searchBy(String bungalowName, String bungalowType) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (bungalowName != null && !bungalowName.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("bungalowName")),
                        "%" + bungalowName.toLowerCase() + "%"));
            }

            if (bungalowType != null && !bungalowType.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("bungalowType")),
                        "%" + bungalowType.toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}