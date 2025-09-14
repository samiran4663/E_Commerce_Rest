package com.codewithmosh.store.service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private String private_key;
	public JwtService()
	{
		try {
			private_key=generatekey();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String generateToken(String username)
	{
		return Jwts
		.builder()
		.setSubject(username)
		.setIssuedAt(new Date(System.currentTimeMillis()))
		.setExpiration(new Date(System.currentTimeMillis()+1000*60*10))
		.signWith(getKey(), SignatureAlgorithm.HS256)
		.compact();
		
	}
	private String generatekey() throws NoSuchAlgorithmException
	{
		KeyGenerator keygen =KeyGenerator.getInstance("HmacSHA256");
		SecretKey secretkey=keygen.generateKey();
		return Base64.getEncoder().encodeToString(secretkey.getEncoded());
	}
	private Key getKey() {
		byte bytekeys[]=Base64.getDecoder().decode(private_key);
		return Keys.hmacShaKeyFor(bytekeys);
	}
	public String extractusername(String token) {
		// TODO Auto-generated method stub
		return extractClaim(token,Claims::getSubject);
	}
	private <T> T  extractClaim(String token,Function<Claims,T>claimresolver)
	{
		Claims claim=extractAllclaims(token);
		return claimresolver.apply(claim);
	}
	private Claims extractAllclaims(String token)
	{
		return Jwts
			    .parserBuilder()
			    .setSigningKey(getKey())
			    .build()
			    .parseClaimsJws(token)
			    .getBody();

	}
	public boolean validatetoken(String token, UserDetails userdetails) {
		// TODO Auto-generated method stub
		String username=extractusername(token);
		return (username.equals(userdetails.getUsername()) && !istokenExpired(token));
	}
	private boolean istokenExpired(String token) {
		// TODO Auto-generated method stub
		return extractClaim(token, Claims::getExpiration).before(new Date(System.currentTimeMillis()));
	}

}

