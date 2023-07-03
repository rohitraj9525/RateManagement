package com.RateManagement.Security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private RateUserDetailsService rateUserDetailsService;
	
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String requestTokenHeader = request.getHeader("Authorization");
		
		String username = null;
		String jwtToken = null;
		
		if(requestTokenHeader!= null && requestTokenHeader.startsWith("Bearer"))
		{
			jwtToken = requestTokenHeader.substring(7);
			System.out.println("TTTTTTTTTTTTTTTTTTTT"+jwtToken);
			
			try
			{
				username = jwtUtil.getUsernameFromToken(jwtToken);
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null)
			{
				UserDetails userDetails = rateUserDetailsService.loadUserByUsername(username);
				
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				
				
			}
			else
			{
				System.out.println("Token is not Validated.............");
			}
		}
		
		filterChain.doFilter(request, response);
		
	}
	

}
