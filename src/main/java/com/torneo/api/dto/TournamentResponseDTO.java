package com.torneo.api.dto;

import com.torneo.api.enums.GamesCategory;
import com.torneo.api.enums.GamesState;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO de salida para mostrar información de un torneo.
 * ✔ Se agregó el campo `maxTeams` para mostrar el cupo al frontend.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentResponseDTO {

    private Long id;
    private String name;
    private String game;
    private GamesCategory category;
    private GamesState state;
    private String organizerUsername;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxTeams;
}
