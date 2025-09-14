package com.codewithmosh.store.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Seller 
{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;
    
    @OneToMany(mappedBy = "seller")
    private List<Product> products = new ArrayList<>();
    
    public boolean existsProduct(Product product)
    {
    	if(products.contains(product))
    	{
    		return true;
    	}
    	return false;
    }
    public void addProducts(Product product)
    {
    	products.add(product);
    }
    
    public void removeProduct(Product product)
    {
    	products.remove(product);
    }
}
