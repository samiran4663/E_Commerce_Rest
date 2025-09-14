package com.codewithmosh.store.service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codewithmosh.store.entities.Address;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.entities.Purchase;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.repositories.AddressRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import com.codewithmosh.store.repositories.PurchaseRepository;
import com.codewithmosh.store.repositories.UserRepository;
import com.codewithmosh.store.DTO.AddressDto;
import com.codewithmosh.store.DTO.ProductDto;
import com.codewithmosh.store.DTO.PurchaseDto;
import com.codewithmosh.store.DTO.PurchaseProduct;
import com.codewithmosh.store.DTO.UserDto;
import com.codewithmosh.store.DTO.UserResponseDto;

import org.springframework.web.server.ResponseStatusException;



@Service
public class UserService {

    @Autowired
    private UserRepository userrepo;

    @Autowired
    private AddressRepository addressrepo;

    @Autowired
    private ProductRepository productrepo;

    @Autowired
    private PurchaseRepository purchaserepo;

    public UserResponseDto addUser(User user) {
    	userrepo.save(user);
    	return UserResponseDto.builder()
    			.id(user.getId())
    			.name(user.getName())
    			.email(user.getEmail())
    			.build();
    }

    @Transactional
    public Long addAddress(Long user_id,AddressDto addressdto) 
    { 
    	User user=userrepo.findById(user_id)
    			.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")); 
    	
    	Address address=new Address();
    	address.setStreet(addressdto.getStreet());
    	address.setCity(addressdto.getCity());
    	address.setState(addressdto.getState());
    	address.setZip(addressdto.getZip());
    	address.setUser(user); 
    	
    	addressrepo.save(address); 
    	user.addAddress(address); 
    	userrepo.save(user); 
    	
    	return address.getId();
    }
    @Transactional(readOnly = true)
    public UserDto findUser(Long user_id) {
    	
    	User user=userrepo.findById(user_id)
    			.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")); 
    	
    	List<AddressDto> addressdtolist = user.getAddresses().stream()
    		    .map(a -> AddressDto.builder()
    		        .street(a.getStreet())
    		        .city(a.getCity())
    		        .state(a.getState())
    		        .zip(a.getZip())
    		        .build())
    		    .collect(Collectors.toList());

    						  
    	return UserDto.builder()
    			.id(user_id)
    			.name(user.getName())
    			.email(user.getEmail())
    			.addresses(addressdtolist)
    			.build();
    }

    @Transactional
    public PurchaseDto buyProduct(Long user_id, PurchaseProduct purchased_product) {
        Long product_id = purchased_product.getProductId();
        User user=userrepo.findById(user_id)
    			.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Product product = productrepo.findById(product_id)
                .orElseThrow(() -> new RuntimeException("Product does not exist"));

        if (product.getStockQuantity() == null || product.getStockQuantity() <= 0) {
        	throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "Product is out of stock");
        }

        int qtyRequested = purchased_product.getQuantity() == null ? 1 : purchased_product.getQuantity();
        if (qtyRequested <= 0) {
        	throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "Quantity must be at least 1");
        }

        if (qtyRequested > product.getStockQuantity()) {
        	throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT, "Requested quantity exceeds available stock");
        }

        Purchase purchase = Purchase.builder()
                .product(product)
                .quantity(qtyRequested)
                .purchasedAt(purchased_product.getPurchasedAt() == null ? Instant.now() : purchased_product.getPurchasedAt())
                .orderStatus("CONFIRMED")
                .build();

        // set bidirectional relationship
        purchase.setUser(user);
        purchaserepo.save(purchase);        // persist purchase
        // maintain in-memory user purchases if you rely on it elsewhere
        user.addPurchases(purchase);
        userrepo.save(user);

        // update product stock
        product.setStockQuantity(product.getStockQuantity() - qtyRequested);
        productrepo.save(product);

        return PurchaseDto.builder()
        		.id(purchase.getId())
        		.productId(product_id)
        		.productName(purchase.getProductName())
        		.quantity(purchase.getQuantity())
        		.purchasedAt(purchase.getPurchasedAt())
        		.orderStatus(purchase.getOrderStatus())
        		.build();
        
        
    }

    @Transactional
    public PurchaseDto cancelPurchase(Long user_id, Long purchase_id) {
        User user = userrepo.findById(user_id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Purchase purchase = purchaserepo.findById(purchase_id)
        		.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase item not Found"));

        // check ownership without loading entire user.purchases collection
        if (purchase.getUser() == null || !purchase.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order does not exist for corresponding user");
        }

        // only cancel if not already cancelled/returned
        String status = purchase.getOrderStatus();
        if ("CANCELLED".equalsIgnoreCase(status) || "RETURNED".equalsIgnoreCase(status)) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order already exist"+status);
        }

        purchase.setOrderStatus("CANCELLED");
        Product product = purchase.getProduct();
        product.setStockQuantity(product.getStockQuantity() + purchase.getQuantity());

        purchaserepo.save(purchase);
        productrepo.save(product);

        return PurchaseDto.builder()
        		.id(purchase_id)
        		.productId(product.getId())
        		.productName(product.getName())
        		.purchasedAt(purchase.getPurchasedAt())
        		.quantity(purchase.getQuantity())
        		.orderStatus(purchase.getOrderStatus())
        		.build();
    }

    @Transactional
    public PurchaseDto returnProduct(Long user_id, Long purchase_id) {
        User user = userrepo.findById(user_id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Purchase purchase = purchaserepo.findById(purchase_id)
        		.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase item not Found"));

        if (purchase.getUser() == null || !purchase.getUser().getId().equals(user.getId())) {
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order does not exist for corresponding user");
        }

        String status = purchase.getOrderStatus();
        if ("RETURNED".equalsIgnoreCase(status)) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order already returned");
        }

        purchase.setOrderStatus("RETURNED");
        Product product = purchase.getProduct();
        product.setStockQuantity(product.getStockQuantity() + purchase.getQuantity());

        purchaserepo.save(purchase);
        productrepo.save(product);

        return PurchaseDto.builder()
        		.id(purchase_id)
        		.productId(product.getId())
        		.productName(product.getName())
        		.purchasedAt(purchase.getPurchasedAt())
        		.quantity(purchase.getQuantity())
        		.orderStatus(purchase.getOrderStatus())
        		.build();
    }

    /**
     * Prefer to add a repository method like:
     * List<Purchase> findByUserId(Long userId);
     * to avoid loading user entity with purchases lazily.
     */
    public List<PurchaseDto> purchaseList(Long user_id) {
        // if you don't have a repo method yet, you can still fetch the user and initialize:
        User user = userrepo.findById(user_id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<PurchaseDto> purchasedtolist=user.getPurchases().stream()
        									.map(purchase->PurchaseDto.builder()
        							        		.id(purchase.getId())
        							        		.productId(purchase.getProduct().getId())
        							        		.productName(purchase.getProductName())
        							        		.purchasedAt(purchase.getPurchasedAt())
        							        		.quantity(purchase.getQuantity())
        							        		.orderStatus(purchase.getOrderStatus())
        							        		.build())
        									.collect(Collectors.toList());
        return purchasedtolist;
    }

    public PurchaseDto purchasedItem(Long user_id, Long purchase_id) {
        // safer ownership check: fetch purchase and compare user id
        Purchase purchase = purchaserepo.findById(purchase_id)
        		.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase item does not found"));

        if (purchase.getUser() == null || !purchase.getUser().getId().equals(user_id)) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase Order does not exist for corresponding user");
        }
        return PurchaseDto.builder()
        		.id(purchase_id)
        		.productId(purchase.getProduct().getId())
        		.productName(purchase.getProductName())
        		.purchasedAt(purchase.getPurchasedAt())
        		.quantity(purchase.getQuantity())
        		.orderStatus(purchase.getOrderStatus())
        		.build();
    }

    @Transactional
    public Set<ProductDto> addToWishList(Long user_id, Long product_id) {
        User user = userrepo.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productrepo.findById(product_id).orElseThrow(() -> new RuntimeException("Product not found"));

        if (!user.getFavoriteProducts().contains(product)) {
        	user.addFavoriteProduct(product);
            userrepo.save(user);
        }

        
        return user.getFavoriteProducts().stream()
        		.map(product_mapper->ProductDto.builder()
        			.id(product_id)
        			.name(product_mapper.getName())
        			.price(product_mapper.getPrice())
        			.sellerName(product_mapper.getSeller().getName())
        			.categoryName(product_mapper.getCategoryName())
        			.stockQuantity(product_mapper.getStockQuantity())
        			.build())
        		.collect(Collectors.toSet());
        			
    }

    @Transactional
    public void removeFromWishList(Long user_id, Long product_id) {
        User user = userrepo.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productrepo.findById(product_id).orElseThrow(() -> new RuntimeException("Product not found"));

        user.removeFavoriteProduct(product);
        userrepo.save(user);
        
    }

    public Set<ProductDto> favouriteProduct(Long user_id) {
        User user = userrepo.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getFavoriteProducts().stream()
        		.map(product_mapper->ProductDto.builder()
        			.id(product_mapper.getId())
        			.name(product_mapper.getName())
        			.price(product_mapper.getPrice())
        			.sellerName(product_mapper.getSeller().getName())
        			.categoryName(product_mapper.getCategoryName())
        			.stockQuantity(product_mapper.getStockQuantity())
        			.build())
        		.collect(Collectors.toSet());
    }
}
