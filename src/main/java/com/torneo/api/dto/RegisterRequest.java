package com.torneo.api.dto;

import com.torneo.api.enums.Role;
import lombok.Data;

/**
 * Este DTO se usa para registrar un nuevo usuario.
 * Incluye el nombre de usuario, la contraseña y el rol que quiere tener.
 */
@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private Role role;

    public RegisterRequest() {}

}
