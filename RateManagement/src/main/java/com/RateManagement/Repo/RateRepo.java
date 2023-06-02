package com.RateManagement.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RateManagement.Entity.Rate;

@Repository
public interface RateRepo extends JpaRepository<Rate,Long>{

}
