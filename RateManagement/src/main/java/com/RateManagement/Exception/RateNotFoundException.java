package com.RateManagement.Exception;

/**
 * @author R.Raj
 *
 */
public class RateNotFoundException extends RuntimeException {

    /**
     * @param message
     */
	public RateNotFoundException(String string) {
        super("Rate not found with id: " + string);
    }
}

