package com.torneo.api.dto;

import lombok.*;

/**
 * DTO de salida para mostrar una inscripción ya registrada.
 *
 * ✔ Incluye el ID y nombre del equipo y torneo.
 * ✔ Se utiliza para retornar los datos al frontend y para lógica interna.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscriptionResponseDTO {

    private Long id;
    private int teamID;
    private String teamName;
    private String tournamentName;
}
