package com.torneo.api.dto;

import com.torneo.api.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserRequest
{
    @NotNull(message = "El username no puede estar vacío")
    private String username;
    @NotNull(message = "El mail no puede estar vacío")
    private String email;
    @NotNull(message = "El password no puede estar vacío")
    private String password;
    @NotNull(message = "El rl no puede estar vacío")
    private Role role;
}
