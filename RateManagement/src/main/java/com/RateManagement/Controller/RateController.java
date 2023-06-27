package com.RateManagement.Controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
//import java.net.http.HttpHeaders;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.RateManagement.DTO.RateDTO;
import com.RateManagement.Entity.Rate;
import com.RateManagement.Service.ExcelService;
import com.RateManagement.Service.RateService;
import org.springframework.http.HttpHeaders;

import jakarta.annotation.Resource;
import jakarta.servlet.Filter;
import jakarta.validation.Valid;

//

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;

//

@RestController
@RequestMapping("/api/v1/rates")
public class RateController {

	@Autowired
    private final RateService rateService;
	@Autowired
    private final ExcelService excelService;


    /**
     * @param rateService
     */
    //@Autowired
    public RateController(RateService rateService) {
        this.rateService = rateService;
		this.excelService = null;
    }
    
    public long calculateStayDurations(LocalDate stayDateFrom, LocalDate stayDateTo) 
	{
        return ChronoUnit.DAYS.between(stayDateFrom, stayDateTo);
	}
    

    /**
     * @param rate
     * @return create rate
     */
    @PostMapping
    public ResponseEntity<String> createRate(@Valid @RequestBody Rate rate) 
    {
    	Long noOfDays = calculateStayDurations(rate.getStayDateFrom(),rate.getStayDateTo());
    	if(rate.getStayDateFrom().isAfter(rate.getStayDateTo()))
    	{
    		return ResponseEntity.badRequest().body("Invalid stay date. 'stayDateFrom' must be before or equal to 'stayDateTo'."); 
    	}
    	if(rate.getStayDateFrom()==null||rate.getStayDateTo()==null)
    	{
    		return ResponseEntity.badRequest().body("Inavlid Stay Dates. 'stayDateTo and stayDateFrom' cannot be null");
    	}
//    	if(noOfDays<rate.getNights())
//    	{
//    		return ResponseEntity.badRequest().body("Please Do not enter StayDateFrom and StayDAteTo lesser than no of nights");
//    	}
    	

        Rate createdRate = rateService.createRate(rate);
               
        return ResponseEntity.status(HttpStatus.CREATED).body("Rate created succesfully with ID: " +createdRate.getRateId());
        
        
    }

    /**
     * @param id
     * @param rate
     * @return update rate
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRate(@PathVariable("id") Long id, @Valid  @RequestBody Rate rate) 
    {
    	if(rate.getStayDateFrom()==null||rate.getStayDateTo()==null)
    	{
    		return ResponseEntity.badRequest().body("Invalid Stay date. 'stayDateFrom' and 'stayDateTo' cannot be null");
    	}
    	if(rate.getStayDateFrom().isAfter(rate.getStayDateTo()))
    	{
    		return ResponseEntity.badRequest().body("Invalid stay dates. 'stayDateFrom' must be before or equal to 'stayDateTo");
    	}
        Rate updatedRate = rateService.updateRate(id, rate);
    	//Rate updatedRate=rateService.getRateById(id);
    	
        if(updatedRate==null)
        {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rate Not Found..please enter correct RateID");
        }
        return ResponseEntity.ok("Rate updated succesfully with Given ID: " );
    }

    /**
     * @param id
     * @return boolean this mehod is for delete the row by id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRate(@Valid @PathVariable("id") Long id) 
    {
      Rate deletedRate=rateService.getRateById(id);
    	
        if(deletedRate==null)
        {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rate Not Found..please enter correct RateID");
        }  	
        rateService.deleteRate(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * @param id
     * @return get rate by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Rate> getRateById(@Valid @PathVariable("id") Long id)
    {
        Rate rate = rateService.getRateById(id);
        return ResponseEntity.ok(rate);
    }

    
        /**
         * @param pageable
         * @param id
         * @param stayDateFrom
         * @param stayDateTo
         * @param nights
         * @param value
         * @param bungalowId
         * @return get rate by specification of all fields
         */
//        @GetMapping
//        public ResponseEntity<List<Rate>> getAllRates(Pageable pageable,
//                                                      @RequestParam(required = false) Long id,
//                                                      @RequestParam(required = false) LocalDate stayDateFrom,
//                                                      @RequestParam(required = false) LocalDate stayDateTo,
//                                                      @RequestParam(required = false) Integer nights,
//                                                      @RequestParam(required = false) Double value,
//                                                      @RequestParam(required = false) Long bungalowId,
//                                                      @RequestParam(required=false) LocalDate closedDate)
//        {
//            Page<Rate> rates = rateService.getAllRates(pageable, id, stayDateFrom, stayDateTo,
//                    nights, value, bungalowId,closedDate);
//            return ResponseEntity.ok(rates.getContent());
        
     
        @GetMapping("/filter")
        public Page<Rate> getAllRates(Pageable pageable,
                                                      @RequestParam Map<String, String> filter)
        {
        	for(Map.Entry<String, String> etry : filter.entrySet()) {
        		System.out.println(etry.getKey());
        		System.out.println(etry.getValue());
        		
        	}
        	
        	
        	Long id = filter.get("id") != null ? Long.parseLong(filter.get("id")) : null;
        	Double value = filter.get("value") != null ? Double.parseDouble(filter.get("value")) : null;
        	Integer nights = filter.get("nights") !=null? Integer.parseInt(filter.get("nights")): null;
        	LocalDate stayDateFrom = filter.get("stayDateFrom") !=null ? LocalDate.parse(filter.get("stayDateFrom")) : null;
        	LocalDate stayDateTo = filter.get("stayDateTo") !=null ? LocalDate.parse(filter.get("stayDateTo")) :null;
        	Long bungalowId = filter.get("bungalowId") != null ? Long.parseLong(filter.get("bungalowId")) : null;
        	LocalDate closedDate = filter.get("closedDate") != null ? LocalDate.parse(filter.get("closedDate")): null;
        	
        	System.out.println(value);
        	System.out.println(nights);

        	System.out.println(pageable);

        	
           return rateService.getAllRates(pageable, id, stayDateFrom, stayDateTo, nights, value, bungalowId, closedDate);
        }
        
        @GetMapping
        public Page<Rate> getAllRatesFilter(Pageable pageable,
                                                      @RequestParam(required = false) Long id,
                                                      @RequestParam(required = false) LocalDate stayDateFrom,
                                                      @RequestParam(required = false) LocalDate stayDateTo,
                                                      @RequestParam(required = false) Integer nights,
                                                      @RequestParam(required = false) Double value,
                                                      @RequestParam(required = false) Long bungalowId,
                                                      @RequestParam(required=false) LocalDate closedDate)
        {
            return rateService.getAllRates(pageable, id, stayDateFrom, stayDateTo, nights, value, bungalowId, closedDate);
        }
        
        //Download
        
        @GetMapping("/filters")
        public ResponseEntity<InputStreamResource> getFile(@RequestParam Map<String, String> filter)
        {
        		//Long id = filter.get("id") != null ? Long.parseLong(filter.get("id")) : null;
            	Double value = filter.get("value") != null ? Double.parseDouble(filter.get("value")) : null;
            	Integer nights = filter.get("nights") !=null? Integer.parseInt(filter.get("nights")): null;
            	String stayDateFrom = filter.get("stayDateFrom") !=null ? (filter.get("stayDateFrom")) : null;
            	String stayDateTo = filter.get("stayDateTo") !=null ? (filter.get("stayDateTo")) :null;
            	Long bungalowId = filter.get("bungalowId") != null ? Long.parseLong(filter.get("bungalowId")) : null;
            	String closedDate = filter.get("closedDate") != null ? (filter.get("closedDate")): null;
                
        
        
            String filename = "rates.xlsx";
            ByteArrayInputStream inputStream = excelService.exportFilteredRates(
                    bungalowId, nights, stayDateFrom, stayDateTo, value, closedDate);

            InputStreamResource file = new InputStreamResource(inputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(file);
        
        }
        
        //private final FileService fileService1;
        
        
//        @GetMapping("/download")
//        public ResponseEntity<InputStreamResource> getFile(
//                @RequestParam(name = "bungalowId",required = false) Long bungalowId,
//                @RequestParam(name = "nights",required = false) Integer nights,
//                @RequestParam(name = "stayDateFrom",required = false) String stayDateFrom,
//                @RequestParam(name = "stayDateTo",required = false) String stayDateTo,
//                @RequestParam(name = "value",required = false) Double value,
//                @RequestParam(name = "closedDate",required = false) String closedDate
//                
//        )
//        {
//            String filename = "rates.xlsx";
//            ByteArrayInputStream inputStream = excelService.exportFilteredRates(
//                    bungalowId, nights, stayDateFrom, stayDateTo, value, closedDate);
//
//            InputStreamResource file = new InputStreamResource(inputStream);
//
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
//                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
//                    .body(file);
//        }    
        
//        @GetMapping("/download")
//        public ResponseEntity<InputStreamResource> exportFilteredRates(@ModelAttribute Filter filterParams) {
//            String filename = "rates.xlsx";
//            ByteArrayInputStream inputStream = excelService.exportFilteredRates(
//                    ((Rate) filterParams).getBungalowId(),
//                    ((Rate) filterParams).getNights(),
//                    ((Rate) filterParams).getStayDateFrom().toString(),
//                    ((Rate) filterParams).getStayDateTo().toString(),
//                    ((Rate) filterParams).getValue(),
//                    ((Rate) filterParams).getClosedDate().toString()
//            );
//
//            InputStreamResource file = new InputStreamResource(inputStream);
//
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
//                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
//                    .body(file);
//        }
        
        
        @PostMapping("/import")
        public ResponseEntity<String> importRatesFromExcel(@RequestParam("file") MultipartFile file) {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file uploaded.");
            }
            
            try {
                List<Rate> importedRates = excelService.importRatesFromExcel(file);
                
                // Convert LocalDate to String for imported rates
                for (Rate rate : importedRates) {
                    String stayDateFrom = rate.getStayDateFrom().toString();
                    String stayDateTo = rate.getStayDateTo().toString();
                    
                    // Use the converted string values as needed
                    // ...
                }
                
                // Additional processing or saving of imported rates
                // ...
                
                return ResponseEntity.ok("Excel file imported successfully.");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import Excel file.");
            }
        }
        
        
        
        
        
        //upload
}

 
    