package com.torneo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO de respuesta que representa un partido ya registrado.
 *
 * Incluye el ID del partido, nombres de los equipos, ID del torneo, fecha y (opcionalmente) resultado.
 * Este objeto se devuelve en las respuestas del controlador (GET, POST, PUT).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchResponseDTO {
    private Long id;
    private String tournamentName;
    private String firstTeamName;
    private String secondTeamName;
    private Integer firstTeamScore;
    private Integer secondTeamScore;
    private String status;
}
