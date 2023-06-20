package com.RateManagement.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.RateManagement.DTO.BungalowDTO;
import com.RateManagement.Entity.Bungalow;
import com.RateManagement.Exception.BungalowNotFoundException;
//import com.RateManagement.Exception.BungalowNotFoundException;
import com.RateManagement.Repo.BungalowRepository;
import com.RateManagement.Specification.BungalowSpecification;


@Service
public class BungalowServiceImpl implements BungalowService {

    private final BungalowRepository bungalowRepository;

    /**
     * @param bungalowRepository
     */
    @Autowired
    public BungalowServiceImpl(BungalowRepository bungalowRepository) {
        this.bungalowRepository = bungalowRepository;
    }

    /**
     * @return findAll
     */
//    @Override
//    public List<Bungalow> getAllBungalows() {
//        return bungalowRepository.findAll();
//    }
    @Override
//    public Page<Bungalow> getAllBungalowsByPagination(int page,int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return bungalowRepository.findAll(pageable);
    public Page<Bungalow> getAllBungalows(Pageable pageable, String bungalowName, String bungalowType) {
        Specification<Bungalow> spec = BungalowSpecification.searchBy(bungalowName, bungalowType);
        return bungalowRepository.findAll(spec,pageable);
    }

    /**
     * @param id
     *@return findById
     */
    @Override
    public Bungalow getBungalowById(Long id) {
        return bungalowRepository.findById(id)

                .orElseThrow(() -> new BungalowNotFoundException("Bungalow not found with id: " + id));
    }

    /**
     * @return save the bungalow object
     */
    @Override
    public Bungalow createBungalow(Bungalow bungalow) {
        return bungalowRepository.save(bungalow);
    }

    /**
     *@return business logic for update the particular Row of ID in the bungalow table
     */
    @Override
    public Bungalow updateBungalow(Long id, Bungalow updatedBungalow) {
        Bungalow existingBungalow = bungalowRepository.findById(id)

                .orElseThrow(() -> new BungalowNotFoundException("Bungalow not found with id: " + id));

        existingBungalow.setBungalowName(updatedBungalow.getBungalowName());
        existingBungalow.setBungalowId(updatedBungalow.getBungalowId());
        existingBungalow.setBungalowType(updatedBungalow.getBungalowType());

        return bungalowRepository.save(existingBungalow);
    }

    /**
     *@return business logic for delete the particular Row of ID in the bungalow table
     */
    @Override
    public void deleteBungalow(Long id) {
        Bungalow existingBungalow = bungalowRepository.findById(id)

                .orElseThrow(() -> new BungalowNotFoundException("Bungalow not found with id: " + id));

        bungalowRepository.delete(existingBungalow);
    }

	@Override
	public Optional<Bungalow> findById(long id) {
		// TODO Auto-generated method stub
		return bungalowRepository.findById(id);
	}
    
    
}


