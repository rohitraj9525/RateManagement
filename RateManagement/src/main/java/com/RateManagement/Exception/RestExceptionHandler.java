package com.RateManagement.Exception;

import java.net.http.HttpHeaders;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) 
	{
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : "Validation error";
        return ResponseEntity.badRequest().body(errorMessage);
    }
	
	@ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> handleNumberFormatException(NumberFormatException ex) {
        String errorMessage = "Invalid number format";
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException ex) {
		ex.printStackTrace();
        String errorMessage = "Invalid date format";
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
	
//	@ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
//        // Create an ErrorResponse object with the error message
//        String errorResponse = "Invalid Date Format";
//        // Return the ErrorResponse in the response entity with the appropriate HTTP status
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }
	
	@ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        // Extract the error message from the exception
        String errorMessage = "Invalid Entry";

        // Return the error message in the response entity with the appropriate HTTP status
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
	
	@ExceptionHandler(NotOfficeXmlFileException.class)
    public ResponseEntity<String> handleNotOfficeXmlFileException(NotOfficeXmlFileException ex) {
        String errorMessage = "Invalid file format. Please upload a valid Office XML file.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex)
	{
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		
	}
		
	
	@ControllerAdvice
	public class ErrorController {
	    @ExceptionHandler(HttpMessageNotReadableException.class)
	    public ResponseEntity<String> handleException(HttpMessageNotReadableException exception, HttpServletRequest request) {
	        return new ResponseEntity<>("You gave an incorrect value for LocalDate. please Enter Correct Input....", HttpStatus.BAD_REQUEST);
	    }
	    
	  }
	
	}


