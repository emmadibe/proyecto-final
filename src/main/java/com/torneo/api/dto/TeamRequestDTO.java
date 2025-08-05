/**
 * Este DTO se usa para registrar o actualizar un equipo.
 * Recibe el nombre del equipo, la lista de IDs de jugadores y el torneo al que pertenece.
 */

package com.torneo.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamRequestDTO {

    @NotNull(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no debe superar los 50 caracteres")
    private String name;

    @NotNull(message = "La lista de jugadores es obligatoria")
    private List<Long> playerIds;

}
