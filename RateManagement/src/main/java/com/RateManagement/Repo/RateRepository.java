package com.RateManagement.Repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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

}

