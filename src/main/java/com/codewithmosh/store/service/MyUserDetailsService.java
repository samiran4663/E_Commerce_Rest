package com.codewithmosh.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codewithmosh.store.entities.Seller;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.repositories.SellerRepository;
import com.codewithmosh.store.repositories.UserRepository;



@Service
public class MyUserDetailsService implements UserDetailsService
{

	@Autowired
	UserRepository userrepo;

	@Autowired
	 SellerRepository sellerrepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user=userrepo.findByEmail(email);
		if(user==null)
		{
			Seller seller=sellerrepo.findByEmail(email);
			if(seller==null)
			{
				throw new UsernameNotFoundException("User not Found");
			}
			else {
				UserDetails sellerdetails= org.springframework.security.core.userdetails.User
									  .withUsername(seller.getEmail())
									  .password(seller.getPassword())
									  .roles("SELLER")
									  .build();
				return sellerdetails;
			}
		}
		UserDetails userdetails=org.springframework.security.core.userdetails.User
				.withUsername(user.getEmail())
				.password(user.getPassword())
				.roles("USER")
				.build();
		return userdetails;
		
	}

}
