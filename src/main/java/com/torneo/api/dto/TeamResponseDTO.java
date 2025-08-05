/**
 * Este DTO se usa para enviar los datos de un equipo al cliente.
 * Incluye el ID, nombre y nombre del torneo al que pertenece.
 * No expone entidades completas ni la lista de jugadores.
 */

package com.torneo.api.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamResponseDTO {
    private Long id;
    private String name;
}
