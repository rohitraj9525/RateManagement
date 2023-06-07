package com.RateManagement.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.RateManagement.Entity.Bungalow;


/**
 * @author R.Raj
 * created bungalow repository for stored the bungalow related data into the database
 */

@Repository
public interface BungalowRepository extends JpaRepository<Bungalow, Long>,JpaSpecificationExecutor<Bungalow> 
{
    // Add any custom repository methods if needed
}

