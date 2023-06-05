package com.RateManagement.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RateManagement.DTO.BungalowDTO;
import com.RateManagement.Entity.Bungalow;
import com.RateManagement.Repo.BungalowRepository;

@Service
public class BungalowServiceImpl implements BungalowService {

    private final BungalowRepository bungalowRepository;

    @Autowired
    public BungalowServiceImpl(BungalowRepository bungalowRepository) {
        this.bungalowRepository = bungalowRepository;
    }

    
    //Business logic for get all the bungalows
    
    @Override
    public List<BungalowDTO> getAllBungalows() {
        List<Bungalow> bungalows = bungalowRepository.findAll();
        return convertToDTOList(bungalows);
    }

    //Business logic for get the bungalow by id

    @Override
    public BungalowDTO getBungalowById(Long bungalowId) {
        Optional<Bungalow> bungalow = bungalowRepository.findById(bungalowId)
;
        return bungalow.map(this::convertToDTO).orElse(null);
    }
    
    //Business logic for create the bungalow

    @Override
    public BungalowDTO createBungalow(BungalowDTO bungalowDTO) {
        Bungalow bungalow = convertToEntity(bungalowDTO);
        Bungalow savedBungalow = bungalowRepository.save(bungalow);
        return convertToDTO(savedBungalow);
    }

    //Business logic for update the bungalow by id

    @Override
    public BungalowDTO updateBungalow(Long bungalowId, BungalowDTO bungalowDTO) {
        Optional<Bungalow> bungalowOptional = bungalowRepository.findById(bungalowId)
;
        if (bungalowOptional.isPresent()) {
            Bungalow existingBungalow = bungalowOptional.get();
            existingBungalow.setBungalowName(bungalowDTO.getBungalowName());
            existingBungalow.setBungalowType(bungalowDTO.getBungalowType());
            Bungalow updatedBungalow = bungalowRepository.save(existingBungalow);
            return convertToDTO(updatedBungalow);
        }
        return null;
    }

    //Business logic for delete the bungalow by id

    @Override
    public void deleteBungalow(Long bungalowId) {
        bungalowRepository.deleteById(bungalowId)
;
    }

    // this method is for convert the table entity to DTO 
    
    private BungalowDTO convertToDTO(Bungalow bungalow) {
        BungalowDTO bungalowDTO = new BungalowDTO();
        bungalowDTO.setbungalowId(bungalow.getBungalowId());
        bungalowDTO.setBungalowName(bungalow.getBungalowName());
        bungalowDTO.setBungalowType(bungalow.getBungalowType());
        return bungalowDTO;
    }

    private List<BungalowDTO> convertToDTOList(List<Bungalow> bungalows) {
        return bungalows.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    //this method is to convert the DTO into the table entity
    
    private Bungalow convertToEntity(BungalowDTO bungalowDTO) {
        Bungalow bungalow = new Bungalow();
        bungalow.setBungalowId(bungalowDTO.getbungalowId());
        bungalow.setBungalowName(bungalowDTO.getBungalowName());
        bungalow.setBungalowType(bungalowDTO.getBungalowType());
        return bungalow;
    }
}