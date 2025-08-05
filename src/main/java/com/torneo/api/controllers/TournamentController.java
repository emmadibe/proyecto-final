package com.torneo.api.controllers;

import com.torneo.api.dto.TournamentRequestDTO;
import com.torneo.api.dto.TournamentResponseDTO;
import com.torneo.api.enums.GamesCategory;
import com.torneo.api.enums.GamesState;
import com.torneo.api.services.TournamentService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
/**
 * Controlador REST para la gestión de torneos.
 * Permite crear, consultar, filtrar, actualizar y eliminar torneos.
 * Solo ADMIN puede modificar o crear torneos.
 */

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;

    /**
     * Endpoint para crear un torneo.
     * Solo accesible por usuarios con rol 'ADMIN' o 'ORGANIZER'.
     *
     * @param dto los datos del torneo a crear
     * @return ResponseEntity con el torneo creado
     */
    @Operation(summary = "Crear torneo",
            description = "Este endpoint permite crear un nuevo torneo. Solo accesible por usuarios con rol 'ADMIN' o 'ORGANIZER'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Torneo creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos del torneo inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuario no autorizado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    @PostMapping
    public ResponseEntity<TournamentResponseDTO> createTournament(
            @RequestBody @Valid @Parameter(description = "Datos para crear un nuevo torneo") TournamentRequestDTO dto) {
        return ResponseEntity.ok(tournamentService.createTournament(dto));
    }

    /**
     * Endpoint para obtener todos los torneos.
     *
     * @return ResponseEntity con la lista de todos los torneos
     */
    @Operation(summary = "Obtener todos los torneos",
            description = "Este endpoint permite obtener la lista de todos los torneos registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de torneos obtenida con éxito"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor")
    })
    @GetMapping
    public ResponseEntity<List<TournamentResponseDTO>> getAllTournaments() {
        return ResponseEntity.ok(tournamentService.getAllTournaments());
    }

    /**
     * Endpoint para obtener un torneo por su ID.
     *
     * @param id ID del torneo
     * @return ResponseEntity con los detalles del torneo
     */
    @Operation(summary = "Obtener torneo por ID",
            description = "Este endpoint permite obtener los detalles de un torneo específico mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Torneo encontrado"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponseDTO> getTournamentById(
            @PathVariable @Parameter(description = "ID del torneo") Long id) {
        return ResponseEntity.ok(tournamentService.getTournamentById(id));
    }

    /**
     * Endpoint para obtener torneos por su estado.
     *
     * @param state el estado de los torneos
     * @return ResponseEntity con los torneos que coinciden con el estado
     */
    @Operation(summary = "Obtener torneos por estado",
            description = "Este endpoint permite obtener los torneos filtrados por su estado (por ejemplo, 'EN_CURSO', 'FINALIZADO').")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de torneos obtenida con éxito"),
            @ApiResponse(responseCode = "400", description = "Estado inválido")
    })
    @GetMapping("/state")
    public ResponseEntity<List<TournamentResponseDTO>> getTournamentsByState(
            @RequestParam @Parameter(description = "Estado de los torneos") GamesState state) {
        return ResponseEntity.ok(tournamentService.getByState(state));
    }

    /**
     * Endpoint para obtener torneos por su categoría.
     *
     * @param category la categoría de los torneos
     * @return ResponseEntity con los torneos que coinciden con la categoría
     */
    @Operation(summary = "Obtener torneos por categoría",
            description = "Este endpoint permite obtener los torneos filtrados por su categoría (por ejemplo, 'DEPORTIVO', 'AMISTOSO').")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de torneos obtenida con éxito"),
            @ApiResponse(responseCode = "400", description = "Categoría inválida")
    })
    @GetMapping("/category")
    public ResponseEntity<List<TournamentResponseDTO>> getTournamentsByCategory(
            @RequestParam @Parameter(description = "Categoría de los torneos") GamesCategory category) {
        return ResponseEntity.ok(tournamentService.getByCategory(category));
    }

    /**
     * Endpoint para actualizar un torneo.
     * Solo accesible por usuarios con rol 'ADMIN'.
     *
     * @param id el ID del torneo a actualizar
     * @param dto los nuevos datos del torneo
     * @return ResponseEntity con el torneo actualizado
     */
    @Operation(summary = "Actualizar torneo",
            description = "Este endpoint permite actualizar un torneo existente. Solo accesible por usuarios con rol 'ADMIN'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Torneo actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos del torneo inválidos")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TournamentResponseDTO> updateTournament(
            @PathVariable @Parameter(description = "ID del torneo a actualizar") Long id,
            @RequestBody @Valid @Parameter(description = "Datos actualizados del torneo") TournamentRequestDTO dto) {
        return ResponseEntity.ok(tournamentService.updateTournament(id, dto));
    }

    /**
     * Endpoint para eliminar un torneo.
     * Solo accesible por usuarios con rol 'ADMIN'.
     *
     * @param id ID del torneo a eliminar
     * @return ResponseEntity vacío con código 204 en caso de éxito
     */
    @Operation(summary = "Eliminar torneo",
            description = "Este endpoint permite eliminar un torneo. Solo accesible por usuarios con rol 'ADMIN'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Torneo eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuario no autorizado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(
            @PathVariable @Parameter(description = "ID del torneo a eliminar") Long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }
}

