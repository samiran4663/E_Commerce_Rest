package com.codewithmosh.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.codewithmosh.store.DTO.ProductDto;
import com.codewithmosh.store.DTO.ProductRequest;
import com.codewithmosh.store.DTO.SellerDto;
import com.codewithmosh.store.DTO.UpdateStockRequest;
import com.codewithmosh.store.entities.Category;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.entities.Seller;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import com.codewithmosh.store.repositories.SellerRepository;

@Service
public class SellerService 
{

	@Autowired
	SellerRepository sellerrepo;
	
	@Autowired
	ProductRepository productrepo;
	
	@Autowired
	CategoryRepository categoryrepo;
	
	public SellerDto addSeller(Seller seller)
	{
		seller.setPassword(new BCryptPasswordEncoder(12).encode(seller.getPassword()));
		sellerrepo.save(seller);
		return SellerDto.builder()
				.id(seller.getId())
				.name(seller.getName())
				.email(seller.getEmail())
				.build();
		
	}
	
	public ProductDto addProduct(Long seller_id,ProductRequest productreq)
	{
		Seller seller=sellerrepo.findById(seller_id).orElseThrow(()->new RuntimeException("Seller not found"));
		String category_str=productreq.getCategory();
		Category category=categoryrepo.findByName(category_str);
		
		Product product=new Product();
		
		product.setName(productreq.getName());
		
		product.setDescription(productreq.getDescription());
		
		product.setPrice(productreq.getPrice());
		
		product.setCategory(category);
		
		product.setSeller(seller);
		
		product.setStockQuantity(productreq.getStockQuantity());
		
		seller.addProducts(product);
		productrepo.save(product);
		
		return ProductDto.builder()
				.id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
				.stockQuantity(product.getStockQuantity())
				.categoryName(product.getCategoryName())
				.sellerName(product.getSeller().getName())
				.build();
	}
	public ProductDto updateStock(Long seller_id,Long product_id,UpdateStockRequest updated_stock)
	{
		Seller seller=sellerrepo.findById(seller_id).orElseThrow(()->new RuntimeException("Seller not found"));
		Product product=productrepo.findById(product_id).orElseThrow(()->new RuntimeException("Product does not exists"));
		if(!seller.existsProduct(product))
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product does not exist for the seller.. Enter a valid"
					+ "Product");
		}
		else {
			int stock=updated_stock.getUpdatedStock();
			product.setStockQuantity(product.getStockQuantity()+stock);
			productrepo.save(product);
		}
		return ProductDto.builder()
				.id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
				.stockQuantity(product.getStockQuantity())
				.categoryName(product.getCategoryName())
				.sellerName(product.getSeller().getName())
				.build();
		
	}
	public void removeProduct(Long seller_id,Long product_id)
	{
		Seller seller=sellerrepo.findById(seller_id).orElseThrow(()->new RuntimeException("Seller not Found"));
		Product product=productrepo.findById(product_id).orElseThrow(()->new RuntimeException("Product does not exist"));
		if(!seller.existsProduct(product))
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product does not exist for the seller.. Enter a valid"
					+ "Product");
		}
		else {
			seller.removeProduct(product);
			productrepo.delete(product);
			
		}
		
		
	}
}
