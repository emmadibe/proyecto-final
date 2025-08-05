package com.torneo.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchRequestDTO {

    @NotNull(message = "El ID del torneo es obligatorio")
    private Long tournamentId;

    @NotNull(message = "El ID del primer equipo es obligatorio")
    private Long firstTeamId;

    @NotNull(message = "El ID del segundo equipo es obligatorio")
    private Long secondTeamId;

    @NotNull(message = "El puntaje del primer equipo es obligatorio")
    private Integer firstTeamScore;

    @NotNull(message = "El puntaje del segundo equipo es obligatorio")
    private Integer secondTeamScore;

    @NotNull(message = "El estado del partido es obligatorio")
    private String status;
}
