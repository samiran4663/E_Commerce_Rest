package com.codewithmosh.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.DTO.LoginRequest;
import com.codewithmosh.store.DTO.SellerDto;
import com.codewithmosh.store.DTO.UserResponseDto;
import com.codewithmosh.store.entities.Seller;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.service.JwtService;
import com.codewithmosh.store.service.SellerService;
import com.codewithmosh.store.service.UserService;


@RestController
@RequestMapping("/auth")
public class UniversalController 
{

	@Autowired
	SellerService sellerservice;
	
	@Autowired
	UserService userservice;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtService jwtservice;
	
	@PostMapping("/register/seller")
	public ResponseEntity<SellerDto> registerSeller(@RequestBody Seller seller)
	{
		SellerDto sellerdto=sellerservice.addSeller(seller);
		return ResponseEntity.ok(sellerdto);
	}
	
	@PostMapping("/register/user")
	public ResponseEntity<UserResponseDto> registerUser(@RequestBody User user)
	{
		UserResponseDto userresponsedto=userservice.addUser(user);
		return ResponseEntity.ok(userresponsedto);
	}
	@PostMapping("/login")
	public ResponseEntity<String> loginUsers(@RequestBody LoginRequest request)
	{
		try {
		    Authentication authentication = authenticationManager.authenticate(
		        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
		    );
		    if (authentication.isAuthenticated()) {
		        String token = jwtservice.generateToken(request.getEmail());
		        return ResponseEntity.ok(token);
		    }
		} catch (Exception e) {
		    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");

	}

}
