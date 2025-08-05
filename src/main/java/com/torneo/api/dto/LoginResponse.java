package com.torneo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO que representa la respuesta del login.
 * Contiene el token JWT generado.
 */
@Getter
@AllArgsConstructor
public class LoginResponse {
    private String token;
}