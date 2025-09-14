package com.codewithmosh.store.DTO;

import java.math.BigDecimal;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

	    private String name;
	    private String description;
	    private BigDecimal price;
	    private String category;  // Instead of full Category entity    
	    private Integer stockQuantity;
	
}
