package com.codewithmosh.store.DTO;
import lombok.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseProduct 
{
	private Long productId;
	private Integer quantity;
	private Instant purchasedAt;
	
}
