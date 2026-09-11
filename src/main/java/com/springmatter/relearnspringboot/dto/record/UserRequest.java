package com.springmatter.relearnspringboot.dto.record;

import com.springmatter.relearnspringboot.anotation.CitizenIdentityCard;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank String name,
        @NotBlank String username,
        @NotBlank String password,
        @Email @NotBlank String email,
        @Size(min = 9, max = 10, message = "Number Phone has between 9 and 10 characters")
        String phone,
        String address,
        @CitizenIdentityCard
        String citizenId
) {
    public UserRequest {
        if (phone != null && !phone.matches("^0\\d{9}$") && phone.length() > 10) {
            throw new IllegalArgumentException("Phone number is not valid");
        }
    }

}
