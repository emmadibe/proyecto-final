/**
 * DTO de entrada para registrar una inscripción.
 *
 * ✔ Recibe los IDs del equipo y del torneo.
 * ✔ Incluye el costo de la inscripción.
 */

package com.torneo.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscriptionRequestDTO
{
    @NotNull(message = "El ID del equipo es obligatorio")
    private Long teamId;

    @NotNull(message = "El ID del torneo es obligatorio")
    private Long tournamentId;
}

