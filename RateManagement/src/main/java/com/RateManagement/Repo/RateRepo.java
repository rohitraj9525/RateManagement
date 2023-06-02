package com.RateManagement.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.RateManagement.Entity.Rate;

public interface RateRepo extends JpaRepository<Rate,Long>{

}
