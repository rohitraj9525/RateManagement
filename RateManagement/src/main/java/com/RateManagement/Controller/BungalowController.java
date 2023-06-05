package com.RateManagement.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RateManagement.DTO.BungalowDTO;
import com.RateManagement.Service.BungalowService;

/**
 * @author R.Raj
 *Annotation for the RestAPI
 */

@RestController
@RequestMapping("bungalows")
public class BungalowController {

    private final BungalowService bungalowService;

    @Autowired
    public BungalowController(BungalowService bungalowService) {
        this.bungalowService = bungalowService;
    }

    
    
    
    /**
     * @param Just passed Request mapping API with particular bungalowId  
     * @return this REST API will return the All ROW of the bungalow table which is present in the database. 
     */

    @GetMapping
    public List<BungalowDTO> getAllBungalows() {
        return bungalowService.getAllBungalows();
    }

    /**
     * @param Just passed Request mapping API   
     * @return this REST API will return the particular ROW of the bungalow table of which bungalowId is passed. 
     */

    
    @GetMapping("/{id}")
    public BungalowDTO getBungalowById(@PathVariable Long id) {
        return bungalowService.getBungalowById(id)
;
    }

    /**
     * @param parameter passed in the BungalowDTO Constructor are bugalowId, bungalowName, bungalowType
     * @return this REST API will return as a create a Row in the bungalow table of the database. 
     */
    
    @PostMapping
    public BungalowDTO createBungalow(@RequestBody BungalowDTO bungalowDTO) {
        return bungalowService.createBungalow(bungalowDTO);
    }

    
    /**
     * @param id
     * @param bungalowDTO just passed API with bungaloId
     * @return this API will update the particular row when we passed bungalowId
     */
    @PutMapping("/{id}")
    public BungalowDTO updateBungalow(@PathVariable Long id, @RequestBody BungalowDTO bungalowDTO) {
        return bungalowService.updateBungalow(id, bungalowDTO);
    }

    /**
     * @param
     * @param passed API with particular bungalow of which we have to delete it from the bungalow table
     * @return this will delete a row of the table
     */
    
    @DeleteMapping("/{id}")
    public void deleteBungalow(@PathVariable Long id) {
        bungalowService.deleteBungalow(id);
    }
}