package com.RateManagement.Exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

}