package com.torneo.api.dto;

import com.torneo.api.enums.GamesCategory;
import com.torneo.api.enums.GamesState;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO de entrada para registrar o actualizar un torneo.
 * ✔ Se agregó el campo `maxTeams` para definir el cupo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TournamentRequestDTO {

    @NotNull(message = "El nombre del torneo es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String name;

    @NotNull(message = "El nombre del juego es obligatorio")
    @Size(max = 50, message = "El juego no puede tener más de 50 caracteres")
    private String game;

    @NotNull(message = "La categoría del juego es obligatoria")
    private GamesCategory category;

    @NotNull(message = "El estado del torneo es obligatorio")
    private GamesState state;

    @NotNull(message = "El ID del organizador es obligatorio")
    private Long organizerId;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate startDate;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Future(message = "La fecha de fin debe ser futura")
    private LocalDate endDate;

    @NotNull(message = "El cupo máximo de equipos es obligatorio")
    private Integer maxTeams;
}
