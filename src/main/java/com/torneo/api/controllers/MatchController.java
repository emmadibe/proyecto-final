package com.torneo.api.controllers;

import com.torneo.api.dto.MatchRequestDTO;
import com.torneo.api.dto.MatchResponseDTO;
import com.torneo.api.services.MatchService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar partidos (matches) entre equipos.
 *
 * ✔ Permite crear, editar, eliminar y consultar partidos.
 * ✔ Filtra partidos por torneo.
 * ✔ Utiliza DTOs para evitar exponer entidades directamente.
 * ✔ Protegido con roles si se desea agregar seguridad más adelante.
 */

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    /**
     * Endpoint para obtener todos los partidos.
     *
     * @return ResponseEntity con la lista de todos los partidos
     */
    @Operation(summary = "Obtener todos los partidos",
            description = "Este endpoint permite obtener la lista de todos los partidos registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de partidos obtenida con éxito"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor")
    })
    @GetMapping
    public ResponseEntity<List<MatchResponseDTO>> getAllMatches() {
        return ResponseEntity.ok(matchService.getAllMatches());
    }

    /**
     * Endpoint para obtener un partido por su ID.
     *
     * @param id ID del partido
     * @return ResponseEntity con los detalles del partido
     */
    @Operation(summary = "Obtener partido por ID",
            description = "Este endpoint permite obtener los detalles de un partido específico mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partido encontrado"),
            @ApiResponse(responseCode = "404", description = "Partido no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MatchResponseDTO> getMatchById(
            @PathVariable @Parameter(description = "ID del partido") Long id) {
        return ResponseEntity.ok(matchService.getMatchById(id));
    }

    /**
     * Endpoint para obtener los partidos por torneo.
     *
     * @param tournamentId ID del torneo
     * @return ResponseEntity con la lista de partidos para el torneo específico
     */
    @Operation(summary = "Obtener partidos por torneo",
            description = "Este endpoint permite obtener la lista de partidos de un torneo específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de partidos obtenida con éxito"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado")
    })
    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<MatchResponseDTO>> getByTournament(
            @PathVariable @Parameter(description = "ID del torneo") Long tournamentId) {
        return ResponseEntity.ok(matchService.getMatchesByTournament(tournamentId));
    }

    /**
     * Endpoint para crear un nuevo partido.
     *
     * @param dto los datos del nuevo partido
     * @return ResponseEntity con los detalles del partido creado
     */
    @Operation(summary = "Crear nuevo partido",
            description = "Este endpoint permite crear un nuevo partido en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partido creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos del partido inválidos")
    })
    @PostMapping
    public ResponseEntity<MatchResponseDTO> createMatch(
            @RequestBody @Parameter(description = "Datos para crear un nuevo partido") MatchRequestDTO dto) {
        return ResponseEntity.ok(matchService.createMatch(dto));
    }

    /**
     * Endpoint para actualizar un partido.
     *
     * @param id ID del partido a actualizar
     * @param dto los nuevos datos del partido
     * @return ResponseEntity con los detalles del partido actualizado
     */
    @Operation(summary = "Actualizar partido",
            description = "Este endpoint permite actualizar un partido existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partido actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Partido no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos del partido inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MatchResponseDTO> updateMatch(
            @PathVariable @Parameter(description = "ID del partido a actualizar") Long id,
            @RequestBody @Parameter(description = "Datos actualizados del partido") MatchRequestDTO dto) {
        return ResponseEntity.ok(matchService.updateMatch(id, dto));
    }

    /**
     * Endpoint para eliminar un partido.
     *
     * @param id ID del partido a eliminar
     * @return ResponseEntity vacío con código 204 en caso de éxito
     */
    @Operation(summary = "Eliminar partido",
            description = "Este endpoint permite eliminar un partido del sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Partido eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Partido no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(
            @PathVariable @Parameter(description = "ID del partido a eliminar") Long id) {
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }
}
