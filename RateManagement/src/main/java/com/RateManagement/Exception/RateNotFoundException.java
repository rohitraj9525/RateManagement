package com.RateManagement.Exception;

public class RateNotFoundException extends RuntimeException {

    /**
     * @param message
     */
	public RateNotFoundException(String string) {
        super("Rate not found with id: " + string);
    }}

