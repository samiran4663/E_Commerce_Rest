package com.codewithmosh.store.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.codewithmosh.store.DTO.AddressDto;
import com.codewithmosh.store.DTO.ProfileRequestDto;
import com.codewithmosh.store.DTO.ProfileResponseDto;
import com.codewithmosh.store.entities.Profile;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.repositories.ProfileRepository;
import com.codewithmosh.store.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ProfileService 
{

	@Autowired
	private UserRepository userrepo;
	
	@Autowired
	private ProfileRepository profilerepo;
	
	@Transactional
	public ProfileResponseDto createOrUpdateProfile(Long user_id,ProfileRequestDto requestdto)
	{
		User user=userrepo.findById(user_id).orElseThrow(()->new RuntimeException("User Not Found"));
		
		
		if(profilerepo.existsById(user_id))
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Profile already exists");
		}
		
		Profile profileData = new Profile();
		profileData.setId(user_id);
		profileData.setBio(requestdto.getBio());
		profileData.setPhoneNumber(requestdto.getPhoneNumber());
		profileData.setDateOfBirth(requestdto.getDateOfBirth());
		profileData.setUser(user);
		
		profilerepo.save(profileData);
		
		List<AddressDto> addressdtos = user.getAddresses().stream()
			    .map(address -> AddressDto.builder()          // if you included id in your DTO
			        .street(address.getStreet())
			        .city(address.getCity())
			        .state(address.getState())
			        .zip(address.getZip())
			        .build()
			    )
			    .toList();
		return ProfileResponseDto.builder()
				.name(user.getName())
				.email(user.getEmail())
				.bio(profileData.getBio())
				.phoneNumber(profileData.getPhoneNumber())
				.dateOfBirth(profileData.getDateOfBirth())
				.address(addressdtos)
				.build();
	}
	public ProfileResponseDto getProfile(Long user_id)
	{
		User user=userrepo.findById(user_id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
		Profile profile=profilerepo.findById(user_id).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, 
				"Profile does not exist"));
		List<AddressDto> addressdtos = user.getAddresses().stream()
			    .map(address -> AddressDto.builder()          // if you included id in your DTO
			        .street(address.getStreet())
			        .city(address.getCity())
			        .state(address.getState())
			        .zip(address.getZip())
			        .build()
			    )
			    .toList();
		return ProfileResponseDto.builder()
				.name(user.getName())
				.email(user.getEmail())
				.bio(profile.getBio())
				.phoneNumber(profile.getPhoneNumber())
				.dateOfBirth(profile.getDateOfBirth())
				.address(addressdtos)
				.build();

	}
	
	
}
