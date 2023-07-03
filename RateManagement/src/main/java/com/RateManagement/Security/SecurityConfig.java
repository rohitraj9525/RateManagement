package com.RateManagement.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig  
{
	@Autowired
	RateUserDetailsService rateUserDetailsService;
	
	@Autowired
	JwtAuthenticationFilter jwtAuthenticationFilter;
	
	//Filter chain
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.csrf((csrf) -> csrf.disable())
		.authorizeHttpRequests((authReq)->{
			authReq
			.requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
		    .requestMatchers(HttpMethod.POST, "/api/v1/rates/**").hasRole("ADMIN")
			.requestMatchers(HttpMethod.PUT, "/api/v1/rates/{id}/**").hasRole("ADMIN")
			.requestMatchers(HttpMethod.GET, "/api/v1/rates/{id}/**").hasAnyRole("ADMIN", "USER")
			.requestMatchers(HttpMethod.DELETE, "/api/v1/rates/{id}/**").hasRole("ADMIN")
			.requestMatchers(HttpMethod.GET, "/api/v1/rates/filter/**").hasAnyRole("ADMIN", "USER")
			.requestMatchers(HttpMethod.POST, "/api/v1/booking/calculate/**").hasAnyRole("ADMIN", "USER")
			.requestMatchers(HttpMethod.POST, "/api/v1/rates/bungalows/**").hasRole("ADMIN")
			.requestMatchers(HttpMethod.GET, "/api/v1/rates/bungalows/{id}/**").hasAnyRole("ADMIN", "USER")
			.requestMatchers(HttpMethod.PUT, "/api/v1/rates/bungalows/{id}/**").hasRole("ADMIN")
			.requestMatchers(HttpMethod.GET, "/api/v1/rates/bungalows/**").hasAnyRole("ADMIN", "USER")
			.requestMatchers(HttpMethod.DELETE, "/api/v1/rates/bungalows/{id}/**").hasRole("ADMIN")
			.requestMatchers(HttpMethod.GET, "/api/v1/rates/filters/**").hasRole("ADMIN")
			.requestMatchers(HttpMethod.POST, "/api/v1/rates/import/**").hasRole("ADMIN")
			.anyRequest().authenticated()
			;
		})		
		.sessionManagement((sm)->{
			sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		})
		.userDetailsService(rateUserDetailsService);

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception
	{
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	

}


