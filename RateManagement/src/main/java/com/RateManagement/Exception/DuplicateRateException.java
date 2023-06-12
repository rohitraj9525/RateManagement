package com.RateManagement.Exception;

public class DuplicateRateException extends RuntimeException
{

	public DuplicateRateException(String string) {
        super("This Rate with same ALL fields is already present in the rate table with different id " + string);
    }

}
