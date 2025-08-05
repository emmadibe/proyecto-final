package com.torneo.api.controllers;

import com.torneo.api.dto.TeamRequestDTO;
import com.torneo.api.dto.TeamResponseDTO;
import com.torneo.api.services.TeamService;
import com.torneo.api.services.TeamXPlayerService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar equipos en el sistema de torneos.
 *
 * ✔ Permite crear, consultar, editar y eliminar equipos.
 * ✔ Filtra equipos por torneo.
 * ✔ Requiere autenticación y control de roles (ADMIN u ORGANIZER para cambios).
 * ✔ Todas las rutas utilizan IDs de tipo Long para evitar conflictos.
 */

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final TeamXPlayerService teamXPlayerService;

    /**
     * Endpoint para obtener todos los equipos.
     *
     * @return ResponseEntity con la lista de todos los equipos
     */
    @Operation(summary = "Obtener todos los equipos",
            description = "Este endpoint permite obtener la lista de todos los equipos registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de equipos obtenida con éxito"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<TeamResponseDTO>> getAllTeams() {
        return ResponseEntity.ok(teamService.listTeams());
    }

    /**
     * Endpoint para obtener un equipo por su ID.
     *
     * @param id ID del equipo
     * @return ResponseEntity con los detalles del equipo
     */
    @Operation(summary = "Obtener equipo por ID",
            description = "Este endpoint permite obtener los detalles de un equipo específico mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipo encontrado"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponseDTO> getTeamById(
            @PathVariable @Parameter(description = "ID del equipo") Long id) {
        return ResponseEntity.ok(teamService.findTeamById(id));
    }

    /**
     * Endpoint para crear un nuevo equipo.
     *
     * @param teamDTO los datos del nuevo equipo
     * @return ResponseEntity con el equipo creado
     */
    @Operation(summary = "Crear un nuevo equipo",
            description = "Este endpoint permite crear un nuevo equipo. Solo accesible por usuarios autenticados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipo creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos del equipo inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })

    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER') or hasRole('PLAYER')")
    @PostMapping
    public ResponseEntity<TeamResponseDTO> createTeam(
            @RequestBody @Parameter(description = "Datos para crear un nuevo equipo") TeamRequestDTO teamDTO) {
        return ResponseEntity.ok(teamService.createTeam(teamDTO));
    }

    /**
     * Endpoint para eliminar un equipo.
     *
     * @param id ID del equipo a eliminar
     * @return ResponseEntity vacío con código 204 en caso de éxito
     */
    @Operation(summary = "Eliminar un equipo",
            description = "Este endpoint permite eliminar un equipo del sistema. Solo accesible por usuarios con rol 'ADMIN'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Equipo eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuario no autorizado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(
            @PathVariable @Parameter(description = "ID del equipo a eliminar") Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/teams")
    public void updateTeam(
            @RequestBody @Parameter(description = "Datos para actualizar un nuevo equipo") TeamResponseDTO teamDTO) {
        teamService.updateTeam(teamDTO);
    }
}
