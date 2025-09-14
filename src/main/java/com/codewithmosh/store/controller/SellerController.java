package com.codewithmosh.store.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.DTO.ProductDto;
import com.codewithmosh.store.DTO.ProductRequest;
import com.codewithmosh.store.DTO.UpdateStockRequest;
import com.codewithmosh.store.service.SellerService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/sellers")
public class SellerController 
{

	@Autowired
	SellerService sellerservice;
	
	@PostMapping("/{sellerId}/products")
	public ResponseEntity<ProductDto> addProduct(@PathVariable Long sellerId,@RequestBody @Valid ProductRequest product)
	{
		ProductDto created=sellerservice.addProduct(sellerId, product);
		URI location = URI.create("/sellers/" + sellerId + "/products/" + created.getId());
        return ResponseEntity.created(location).body(created);
	}
	@PutMapping("/{sellerId}/products/{productId}/stock")
	public ResponseEntity<ProductDto> updateStock(@PathVariable Long sellerId, @PathVariable Long productId, @RequestBody @Valid UpdateStockRequest updated_stock)
	{
		 ProductDto updated = sellerservice.updateStock(sellerId, productId, updated_stock);
	     return ResponseEntity.ok(updated);
	}
	 @DeleteMapping("/{sellerId}/products/{productId}")
	public ResponseEntity<Void> removeProduct(@PathVariable Long sellerId,@PathVariable Long productId)
	{
		sellerservice.removeProduct(sellerId, productId);
		return ResponseEntity.noContent().build();
	}
	
}


