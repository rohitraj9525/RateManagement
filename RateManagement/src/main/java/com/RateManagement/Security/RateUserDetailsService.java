package com.RateManagement.Security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.RateManagement.Entity.User;
import com.RateManagement.Repo.UserRepository;

@Service
public class RateUserDetailsService implements UserDetailsService
{
	
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		// TODO Auto-generated method stub
		Optional<User> userOptional = userRepository.findByUsername(username);
		
		if(userOptional.isEmpty())
		{
			throw new UsernameNotFoundException(username);
		}
		
		return new RateUserDetails(userOptional.get());
	}
	

}
