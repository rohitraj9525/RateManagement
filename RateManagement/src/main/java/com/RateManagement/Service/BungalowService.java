package com.RateManagement.Service;

import java.util.List;

import com.RateManagement.DTO.BungalowDTO;

public interface BungalowService 
{
    List<BungalowDTO> getAllBungalows();
    BungalowDTO getBungalowById(Long bungalowId);
    BungalowDTO createBungalow(BungalowDTO bungalowDTO);
    BungalowDTO updateBungalow(Long bungalowId, BungalowDTO bungalowDTO);
    void deleteBungalow(Long bungalowId);
}