package com.RateManagement.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RateManagement.Entity.Bungalow;

@Repository
public interface BungalowRepo extends JpaRepository<Bungalow,Long>
{

}
