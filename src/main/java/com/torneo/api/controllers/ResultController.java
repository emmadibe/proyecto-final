package com.torneo.api.controllers;

import com.torneo.api.dto.ResultCreateDTO;
import com.torneo.api.dto.ResultDTO;
import com.torneo.api.services.ResultService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar los resultados de torneos.
 * Solo los usuarios con rol 'ADMIN' pueden crear, editar o borrar resultados.
 */
@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    /**
     * Endpoint para crear un nuevo resultado.
     *
     * @param dto los datos del resultado a crear
     * @return ResponseEntity con el resultado creado
     */
    @Operation(summary = "Crear nuevo resultado",
            description = "Este endpoint permite crear un nuevo resultado para un torneo. Solo accesible por usuarios con rol 'ADMIN'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado creado con éxito"),
            @ApiResponse(responseCode = "401", description = "Usuario no autorizado"),
            @ApiResponse(responseCode = "400", description = "Datos de resultado inválidos")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ResultDTO> createResult(
            @RequestBody @Parameter(description = "Datos para crear un nuevo resultado") ResultCreateDTO dto) {
        return ResponseEntity.ok(resultService.createResult(dto));
    }

    /**
     * Endpoint para obtener todos los resultados.
     *
     * @return ResponseEntity con la lista de todos los resultados
     */
    @Operation(summary = "Obtener todos los resultados",
            description = "Este endpoint permite obtener la lista de todos los resultados registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de resultados obtenida con éxito"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor")
    })
    @GetMapping
    public ResponseEntity<List<ResultDTO>> getAllResults() {
        return ResponseEntity.ok(resultService.getAll());
    }

    /**
     * Endpoint para obtener un resultado por su ID.
     *
     * @param id ID del resultado
     * @return ResponseEntity con los detalles del resultado
     */
    @Operation(summary = "Obtener resultado por ID",
            description = "Este endpoint permite obtener los detalles de un resultado específico mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado encontrado"),
            @ApiResponse(responseCode = "404", description = "Resultado no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResultDTO> getResultById(
            @PathVariable @Parameter(description = "ID del resultado") Long id) {
        Optional<ResultDTO> result = resultService.getById(id);
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para actualizar un resultado.
     *
     * @param id ID del resultado a actualizar
     * @param dto los nuevos datos del resultado
     * @return ResponseEntity con los detalles del resultado actualizado
     */
    @Operation(summary = "Actualizar resultado",
            description = "Este endpoint permite actualizar un resultado existente. Solo accesible por usuarios con rol 'ADMIN'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Resultado no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos del resultado inválidos")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ResultDTO> updateResult(
            @PathVariable @Parameter(description = "ID del resultado a actualizar") Long id,
            @RequestBody @Parameter(description = "Datos actualizados del resultado") ResultCreateDTO dto) {
        return ResponseEntity.ok(resultService.updateResult(id, dto));
    }

    /**
     * Endpoint para eliminar un resultado.
     *
     * @param id ID del resultado a eliminar
     * @return ResponseEntity vacío con código 204 en caso de éxito
     */
    @Operation(summary = "Eliminar resultado",
            description = "Este endpoint permite eliminar un resultado de un torneo. Solo accesible por usuarios con rol 'ADMIN'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resultado eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Resultado no encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuario no autorizado")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(
            @PathVariable @Parameter(description = "ID del resultado a eliminar") Long id) {
        resultService.deleteResult(id);
        return ResponseEntity.noContent().build();
    }
}
