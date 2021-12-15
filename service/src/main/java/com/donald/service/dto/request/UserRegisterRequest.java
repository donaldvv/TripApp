package com.donald.service.dto.request;

import com.donald.service.model.enums.Role;
import com.donald.service.validator.RoleEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Getter
@Setter
public class UserRegisterRequest {

    @NotBlank
    @Size(min=5, max = 20, message = "Username must be between 5 and 20 characters long!")
    private String username;

    @NotBlank
    @Size(min = 5, message = "Password must be atleast 5 characters long!")
    private String password;

    @NotNull
    @NotEmpty
    private Collection< @RoleEnum(enumClass = Role.class, ignoreCase = true) String> roles;

}
