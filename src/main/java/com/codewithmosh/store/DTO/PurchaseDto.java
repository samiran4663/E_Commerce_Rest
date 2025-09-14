package com.codewithmosh.store.DTO;

import java.time.Instant;
import lombok.*;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class PurchaseDto 
{

	private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Instant purchasedAt;
    private String orderStatus;
}
