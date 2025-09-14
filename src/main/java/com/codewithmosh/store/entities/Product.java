package com.codewithmosh.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;
    
    @Transient
    public String getCategoryName()
    {
    	return category!=null?category.getName():null;
    }
    
    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name="seller_id")
    @JsonIgnore
    private Seller seller;
    
    @Column(name = "stock_quantity")
    private Integer stockQuantity;

}