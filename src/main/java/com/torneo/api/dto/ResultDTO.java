/**
 * DTO de salida que representa un resultado ya registrado.
 *
 * ✔ Contiene IDs del torneo, del equipo ganador y perdedor.
 * ✔ Incluye los puntajes obtenidos por cada equipo.
 * ✔ Es devuelto al frontend luego de crear, consultar o modificar un resultado.
 *
 * Este DTO permite mostrar de forma clara los resultados registrados en la base
 * sin exponer entidades completas.
 */

package com.torneo.api.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultDTO {

    private Long id;

    private Long tournamentId;

    private Long winerTeamId;

    private Long loserTeamId;

    private Integer scoreWinnerTeam;

    private Integer scoreLoserTeam;
}
