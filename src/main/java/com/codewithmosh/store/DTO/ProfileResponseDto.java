package com.codewithmosh.store.DTO;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto 
{

    
    private String name;

    private String email;
    
	private String bio;

    private String phoneNumber;

    private LocalDate dateOfBirth;
    
    private List<AddressDto> address;
}
