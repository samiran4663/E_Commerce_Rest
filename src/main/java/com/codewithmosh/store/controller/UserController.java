package com.codewithmosh.store.controller;

import java.net.URI;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.DTO.AddressDto;
import com.codewithmosh.store.DTO.ProductDto;
import com.codewithmosh.store.DTO.ProfileRequestDto;
import com.codewithmosh.store.DTO.ProfileResponseDto;
import com.codewithmosh.store.DTO.PurchaseDto;
import com.codewithmosh.store.DTO.PurchaseProduct;
import com.codewithmosh.store.DTO.UserDto;
import com.codewithmosh.store.service.ProfileService;
import com.codewithmosh.store.service.UserService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/users")
public class UserController 
{

	@Autowired
	UserService userservice;
	
	@Autowired
	ProfileService profileService;
	
	@PutMapping("/{userId}/profile")
	public ResponseEntity<ProfileResponseDto> setProfile(@PathVariable Long userId, @RequestBody @Valid ProfileRequestDto profileRequest) {
	    ProfileResponseDto created = profileService.createOrUpdateProfile(userId, profileRequest);
	    URI location = URI.create("/users/" + userId + "/profile");
	    return ResponseEntity.created(location).body(created);
	}

	@GetMapping("/{userId}/profile")
	public ResponseEntity<ProfileResponseDto> getProfile(@PathVariable Long userId) {
	    ProfileResponseDto profile = profileService.getProfile(userId);
	    return ResponseEntity.ok(profile);
	}
	@PostMapping("/{userId}/address")
	public ResponseEntity<Void> addAddress(@PathVariable Long userId,@RequestBody @Valid AddressDto addressDto) 
	{
		 	Long addressId = userservice.addAddress(userId, addressDto);
		 	URI location = URI.create("/users/" + userId + "/addresses/" + addressId);
		 	return ResponseEntity.created(location).build();
	}
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUser(@PathVariable Long userId)
	{
		UserDto userdto=userservice.findUser(userId);
		return ResponseEntity.ok(userdto);
	}
	@PostMapping("/{userId}/orders")
	public ResponseEntity<PurchaseDto> buyProduct(@PathVariable Long userId,@RequestBody @Valid PurchaseProduct purchasedProduct)
	{
		PurchaseDto created = userservice.buyProduct(userId, purchasedProduct);
        URI location = URI.create("/users/" + userId + "/orders/" + created.getId());
        return ResponseEntity.created(location).body(created);
	}
	@PutMapping("/{userId}/orders/{purchaseId}/cancel")
	public ResponseEntity<PurchaseDto> cancelPurchase(@PathVariable Long userId,@PathVariable Long purchaseId)
	{
		PurchaseDto updated=userservice.cancelPurchase(userId, purchaseId);
		return ResponseEntity.ok(updated);
	}
	@PutMapping("/{userId}/orders/{purchaseId}/return")
	public ResponseEntity<PurchaseDto> returnProduct(@PathVariable Long userId,@PathVariable Long purchaseId) 
	{
	  PurchaseDto updated=userservice.returnProduct(userId, purchaseId);
	  return ResponseEntity.ok(updated);
	}
	@GetMapping("/{userId}/orders")
	public ResponseEntity<List<PurchaseDto>> purchaseList(@PathVariable Long userId)
	{
		List<PurchaseDto>purchaselist=userservice.purchaseList(userId);
		return ResponseEntity.ok(purchaselist);
		
	}
	@GetMapping("/{userId}/orders/{purchaseId}")
	public ResponseEntity<PurchaseDto> purchasedItem(@PathVariable Long userId,@PathVariable Long purchaseId) 
	{
	    PurchaseDto purchasedto = userservice.purchasedItem(userId, purchaseId);
	    if (purchasedto == null) {
	        return ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok(purchasedto);
	}
	@PostMapping("/{userId}/wishlist/{productId}")
	public ResponseEntity<Set<ProductDto>> addToWishList(@PathVariable Long userId,@PathVariable Long productId)
	{
		Set<ProductDto>product=userservice.addToWishList(userId, productId);
		return ResponseEntity.ok(product);
	}
	@DeleteMapping("/{userId}/wishlist/{productId}")
	public ResponseEntity<Void> removeFromWishList(@PathVariable Long userId,@PathVariable Long productId)
	{
		userservice.removeFromWishList(userId, productId);
		return ResponseEntity.noContent().build();
	}
	@GetMapping("/{userId}/wishlist")
	public ResponseEntity<Set<ProductDto>> favouriteProducts(@PathVariable Long userId)
	{
		Set<ProductDto>product=userservice.favouriteProduct(userId);
		return ResponseEntity.ok(product);
	}
}
	 

