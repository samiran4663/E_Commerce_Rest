package com.codewithmosh.store.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.codewithmosh.store.service.MyUserDetailsService;



@Configuration
@EnableWebSecurity
public class SecurityConfig 
{

	@Autowired
	MyUserDetailsService userdetailsservice;
	
	@Autowired
	JwtFilter jwtFilter;
	
	@Bean
	DaoAuthenticationProvider authenticationprovider()
	{
		DaoAuthenticationProvider authentication=new DaoAuthenticationProvider();
		authentication.setUserDetailsService(userdetailsservice);
		authentication.setPasswordEncoder(new BCryptPasswordEncoder(12));
		return authentication;
	}
	
	@Bean
	SecurityFilterChain defaultsecurityfilterchain(HttpSecurity http) throws Exception
	{
		return http
				.csrf(csrf->csrf.disable())
				.httpBasic(basic->basic.disable())
				.formLogin(form->form.disable())
				.authorizeHttpRequests(auth->auth.requestMatchers("/auth/register/**").permitAll()
												  .requestMatchers("auth/login").permitAll()
												  .requestMatchers("/users/**").hasRole("USER")
												  .requestMatchers("/sellers/**").hasRole("SELLER")
												  .anyRequest().authenticated())
				.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.logout(logout->logout.permitAll())
				.build();
	}
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
	{
		return config.getAuthenticationManager();
	}
	
}
