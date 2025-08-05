package com.torneo.api.controllers;

import com.torneo.api.dto.InscriptionRequestDTO;
import com.torneo.api.dto.InscriptionResponseDTO;
import com.torneo.api.services.InscriptionService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar inscripciones de equipos en torneos.
 * Permite registrar, listar y eliminar inscripciones.
 */
@RestController
@RequestMapping("/api/inscriptions")
@RequiredArgsConstructor
public class InscriptionController {

    private final InscriptionService inscriptionService;

    /**
     * Endpoint para registrar una nueva inscripción.
     *
     * @param dto los datos de la inscripción a registrar
     * @return ResponseEntity con la inscripción registrada
     */
    @Operation(summary = "Registrar inscripción de un equipo",
            description = "Este endpoint permite registrar una nueva inscripción de un equipo en un torneo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscripción registrada con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de inscripción inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuario no autorizado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER', 'PLAYER')")
    @PostMapping
    public ResponseEntity<InscriptionResponseDTO> register(
            @RequestBody @Parameter(description = "Datos para registrar una nueva inscripción") InscriptionRequestDTO dto) {
        return ResponseEntity.ok(inscriptionService.registerInscription(dto));
    }

    /**
     * Endpoint para obtener todas las inscripciones.
     *
     * @return ResponseEntity con la lista de todas las inscripciones
     */
    @Operation(summary = "Obtener todas las inscripciones",
            description = "Este endpoint permite obtener la lista de todas las inscripciones registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inscripciones obtenida con éxito"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<InscriptionResponseDTO>> getAll() {
        return ResponseEntity.ok(inscriptionService.getAll());
    }

    /**
     * Endpoint para obtener inscripciones por torneo.
     *
     * @param tournamentId ID del torneo
     * @return ResponseEntity con la lista de inscripciones para un torneo específico
     */
    @Operation(summary = "Obtener inscripciones por torneo",
            description = "Este endpoint permite obtener las inscripciones para un torneo específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inscripciones del torneo obtenida con éxito"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<InscriptionResponseDTO>> getByTournament(
            @PathVariable @Parameter(description = "ID del torneo") Long tournamentId) {
        return ResponseEntity.ok(inscriptionService.getByTournament(tournamentId));
    }

    /**
     * Endpoint para obtener inscripciones por equipo.
     *
     * @param teamId ID del equipo
     * @return ResponseEntity con la lista de inscripciones para un equipo específico
     */
    @Operation(summary = "Obtener inscripciones por equipo",
            description = "Este endpoint permite obtener las inscripciones de un equipo específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inscripciones del equipo obtenida con éxito"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<InscriptionResponseDTO>> getByTeam(
            @PathVariable @Parameter(description = "ID del equipo") Long teamId) {
        return ResponseEntity.ok(inscriptionService.getByTeam(teamId));
    }

    /**
     * Endpoint para eliminar una inscripción.
     *
     * @param id ID de la inscripción a eliminar
     * @return ResponseEntity vacío con código 204 en caso de éxito
     */
    @Operation(summary = "Eliminar inscripción",
            description = "Este endpoint permite eliminar una inscripción de un equipo en un torneo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Inscripción eliminada con éxito"),
            @ApiResponse(responseCode = "401", description = "Usuario no autorizado"),
            @ApiResponse(responseCode = "404", description = "Inscripción no encontrada")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Parameter(description = "ID de la inscripción a eliminar") Long id) {
        inscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
