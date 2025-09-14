package com.codewithmosh.store.DTO;


import java.time.LocalDate;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileRequestDto {

    
    private String bio;

    private String phoneNumber;

    private LocalDate dateOfBirth;
}
