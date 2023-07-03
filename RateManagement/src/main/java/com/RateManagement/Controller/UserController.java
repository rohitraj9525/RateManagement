package com.RateManagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RateManagement.Entity.User;
import com.RateManagement.Repo.UserRepository;
import com.RateManagement.Security.JwtRequest;
import com.RateManagement.Security.JwtResponse;
import com.RateManagement.Security.JwtUtil;
import com.RateManagement.Security.RateUserDetailsService;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController 
{
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private RateUserDetailsService rateUserDetailsService;
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody User user)
	{
		
		try
		{
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
			
			return new ResponseEntity<>(user, HttpStatus.OK);
			
			
		}
		catch (Exception e)
		{
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest) throws Exception
	{
		
		String username = jwtRequest.getUsername();
		String password = jwtRequest.getPassword();
		
		try
		{
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			
			
		}
		
		catch (Exception e)
		{
			throw new Exception("Bad Credentials" +e);
		}
		
		UserDetails userDetails = rateUserDetailsService.loadUserByUsername(username);
		
		String token = jwtUtil.generateToken(userDetails);
		return new ResponseEntity<> (new JwtResponse(token), HttpStatus.OK);
		
	}
	

}
