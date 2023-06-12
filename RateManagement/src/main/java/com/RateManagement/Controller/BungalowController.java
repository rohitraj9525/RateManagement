package com.RateManagement.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.RateManagement.DTO.BungalowDTO;
import com.RateManagement.Entity.Bungalow;
import com.RateManagement.Service.BungalowService;

/**
 * @author R.Raj
 *Annotation for the RestAPI
 */


@RestController
@RequestMapping("/api/v1/rates/bungalows")
public class BungalowController {

    private final BungalowService bungalowService;

    /**
     * @param bungalowService
     */
    @Autowired
    public BungalowController(BungalowService bungalowService) {
        this.bungalowService = bungalowService;
    }

    /**
     * @return getAllBungalows
     */
    @GetMapping
//    public ResponseEntity<Page<Bungalow>> getAllBungalows(
//    		@RequestParam(defaultValue = "0") int page,
//    		@RequestParam(defaultValue = "5") int size) {
//        Page<Bungalow> bungalows = bungalowService.getAllBungalowsByPagination(page,size);
//        return ResponseEntity.ok(bungalows);
//    }
    public Page<Bungalow> getAllBungalows(Pageable pageable,
            @RequestParam(required = false) String bungalowName,
            @RequestParam(required = false) String bungalowType) 
    {
         return bungalowService.getAllBungalows(pageable, bungalowName, bungalowType);
    }
    /**
     * @param id
     * @return getBungalowById
     */
    @GetMapping("/{id}")
    public Bungalow getBungalowById(@PathVariable Long id) {
        return bungalowService.getBungalowById(id);
    }

    /**
     * @param bungalow
     * @return createBungalow
     */
    @PostMapping
    public Bungalow createBungalow(@RequestBody Bungalow bungalow) {
        return bungalowService.createBungalow(bungalow);
    }

    /**
     * @param id
     * @param updatedBungalow
     * @return updateBungalow
     */
    @PutMapping("/{id}")
    public Bungalow updateBungalow(@PathVariable Long id, @RequestBody Bungalow updatedBungalow) {
        return bungalowService.updateBungalow(id, updatedBungalow);
    }

    /**
     * @param id
     * @return boolean
     */
    @DeleteMapping("/{id}")
    public void deleteBungalow(@PathVariable Long id) {
        bungalowService.deleteBungalow(id);
    }
}