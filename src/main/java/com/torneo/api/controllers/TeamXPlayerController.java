package com.torneo.api.controllers;

import com.torneo.api.dto.TeamXPlayerRequestDTO;
import com.torneo.api.dto.TeamXPlayerResponseDTO;
import com.torneo.api.models.TeamXPlayer;
import com.torneo.api.services.TeamXPlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar la asignación de jugadores a equipos.
 * ✔ Permite agregar jugadores a un equipo.
 * ✔ Permite consultar los jugadores de un equipo.
 */
@RestController
@RequestMapping("/api/teamxplayer")
@RequiredArgsConstructor
public class TeamXPlayerController {

    private final TeamXPlayerService teamXPlayerService;

    @PostMapping
    @Operation(summary = "Asignar jugador a un equipo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jugador asignado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<TeamXPlayerResponseDTO> createTeamXPlayer(@RequestBody TeamXPlayerRequestDTO dto) {
        return ResponseEntity.ok(teamXPlayerService.createTeamXPlayer(dto));
    }

    @GetMapping("/team/{teamId}")
    @Operation(summary = "Listar jugadores de un equipo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jugadores encontrados")
    })
    public ResponseEntity<List<TeamXPlayerResponseDTO>> getPlayersByTeamId(@PathVariable Long teamId) {
        List<TeamXPlayer> jugadores = teamXPlayerService.getByTeamId(teamId);
        List<TeamXPlayerResponseDTO> dtos = jugadores.stream()
                .map(j -> TeamXPlayerResponseDTO.builder()
                        .id(j.getId())
                        .teamID(j.getTeamEntity().getId())
                        .userID(j.getUser().getId())
                        .isCaptain(j.isCaptain())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}
