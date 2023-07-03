package com.RateManagement.Security;

public class JwtResponse 
{
	private final String jwttoken;

	public JwtResponse(String jwttoken) {
		super();
		this.jwttoken = jwttoken;
	}

	public String getJwttoken() {
		return this.jwttoken;
	}

	@Override
	public String toString() {
		return "JwtResponse [jwttoken=" + jwttoken + "]";
	}

	
	
	

}
