/**
 * DTO utilizado para registrar o actualizar un resultado entre dos equipos.
 *
 * ✔ Recibe los IDs del torneo, del equipo ganador y del perdedor.
 * ✔ Incluye los puntajes obtenidos por cada equipo.
 * ✔ Es usado en endpoints POST y PUT del controlador de resultados.
 *
 * Este DTO representa los datos necesarios para crear o modificar
 * un resultado de enfrentamiento dentro de un torneo específico.
 */

package com.torneo.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultCreateDTO {

    @NotNull(message = "El ID del torneo es obligatorio")
    private Long tournamentId;

    @NotNull(message = "El ID del equipo ganador es obligatorio")
    private Long winerTeamId;

    @NotNull(message = "El ID del equipo perdedor es obligatorio")
    private Long loserTeamId;

    @NotNull(message = "El puntaje del equipo ganador es obligatorio")
    @Min(value = 0, message = "El puntaje no puede ser negativo")
    private Integer scoreWinnerTeam;

    @NotNull(message = "El puntaje del equipo perdedor es obligatorio")
    @Min(value = 0, message = "El puntaje no puede ser negativo")
    private Integer scoreLoserTeam;
}
