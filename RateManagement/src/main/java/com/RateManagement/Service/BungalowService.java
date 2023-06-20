package com.RateManagement.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.RateManagement.DTO.BungalowDTO;
import com.RateManagement.Entity.Bungalow;

public interface BungalowService 
{
	//List<Bungalow> getAllBungalows();
    //Page<Bungalow> getAllBungalowsByPagination(int page,int size);
    Page<Bungalow> getAllBungalows(Pageable pageable, String bungalowName, String bungalowType);


    Bungalow getBungalowById(Long id);
    Bungalow createBungalow(Bungalow bungalow);
    Bungalow updateBungalow(Long id, Bungalow bungalow);
    void deleteBungalow(Long id);
    
    Optional<Bungalow> findById(long id);
}
