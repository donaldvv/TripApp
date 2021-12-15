package com.donald.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {

    @NotBlank
    @Size(min=5, max = 20 , message = "Username must be between 5 and 20 characters long!")
    private String username;

    @NotBlank
    @Size(min = 5, message = "Password must be atleast 5 characters long!")
    private String password;

}
