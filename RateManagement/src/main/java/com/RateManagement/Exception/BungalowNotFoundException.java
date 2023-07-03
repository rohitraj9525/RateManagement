package com.RateManagement.Exception;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author R.Raj
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BungalowNotFoundException extends RuntimeException {

    /**
     * @param string
     */
    public BungalowNotFoundException(String string) {
        super("Bungalow not found with id: " + string);
    }
}