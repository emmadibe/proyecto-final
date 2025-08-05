package com.torneo.api.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * Enum que representa los roles posibles de un usuario en el sistema:
 * - ADMIN → acceso completo
 * - ORGANIZER → gestiona equipos, torneos y resultados
 * - PLAYER → solo visualiza e interactúa con sus propios datos
 *
 * Implementa GrantedAuthority para integrarse con Spring Security.
 */

public enum Role implements GrantedAuthority {
    ADMIN,
    ORGANIZER,
    PLAYER;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();  // Devuelve "ROLE_ADMIN", etc.
    }
}
