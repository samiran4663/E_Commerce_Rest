package com.codewithmosh.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.DTO.SellerDto;
import com.codewithmosh.store.DTO.UserResponseDto;
import com.codewithmosh.store.entities.Seller;
import com.codewithmosh.store.entities.User;
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
}
