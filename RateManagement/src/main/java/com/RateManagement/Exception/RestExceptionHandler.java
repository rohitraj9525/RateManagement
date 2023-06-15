package com.RateManagement.Exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class RestExceptionHandler 
{
		
	/**
	 * @param ex
	 * @return control constraintviolation exception
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex)
	{
		List<Map<String, Object>> fields = new ArrayList<>();
		  for (ConstraintViolation<?> cv : ex.getConstraintViolations()) {
		    String fieldName = ((PathImpl) cv.getPropertyPath()).getLeafNode().asString();
		    String message = cv.getMessage();
		    Map<String, Object> field = new HashMap<>();
		    field.put("field", fieldName);
		    field.put("message", message);
		    fields.add(field);
		  }
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fields);
	}
	
	@ExceptionHandler(BungalowNotFoundException.class)
	public ResponseEntity<?> handleBungalowNotFoundException(BungalowNotFoundException ex)
	{
		Map<String, String> responseMap = new HashMap<>();
		responseMap.put("message", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
	}
	
	@ExceptionHandler(RateNotFoundException.class)
	public ResponseEntity<?> handleRateNotFoundException(RateNotFoundException ex)
	{
		Map<String, String> responseMap = new HashMap<>();
		responseMap.put("message", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
	}
	
	@ExceptionHandler(DuplicateRateException.class)
    public ResponseEntity<String> handleDuplicateRateException(DuplicateRateException ex) {
		String errorMessage=ex.getMessage();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
	}
	
	@ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        // Handle the exception and return an error response
		ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Please do not enter null value");
    }
	
	@ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        // Create error response
         ex.printStackTrace();
        // Return the error response with appropriate HTTP status
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Rate not found with this ID");
    }
	
	
	
	@ControllerAdvice
	public class ErrorController {
	    @ExceptionHandler(HttpMessageNotReadableException.class)
	    public ResponseEntity<String> handleException(HttpMessageNotReadableException exception, HttpServletRequest request) {
	        return new ResponseEntity<>("You gave an incorrect value for LocalDate. please Enter Correct Input....", HttpStatus.BAD_REQUEST);
	    }
	}	}


