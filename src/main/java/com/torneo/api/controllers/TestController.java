package com.torneo.api.controllers;

import com.torneo.api.dto.TestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para pruebas de funcionamiento básico de la API.
 *
 * ✔ Expone un endpoint simple para testear el backend.
 * ✔ Recibe un DTO con un nombre y responde con un saludo personalizado.
 * ✔ Útil para verificar si el proyecto está correctamente desplegado.
 */

@RestController
@RequestMapping("/api")
public class TestController {

    /**
     * Endpoint para probar la API.
     * Recibe un nombre y devuelve un saludo personalizado.
     *
     * @param dto el objeto que contiene el nombre
     * @return ResponseEntity con el saludo personalizado
     */
    @Operation(summary = "Prueba de saludo",
            description = "Este endpoint permite recibir un nombre y devolver un saludo personalizado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saludo exitoso"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/test")
    public ResponseEntity<String> test(
            @RequestBody @Parameter(description = "DTO que contiene el nombre para el saludo") TestDto dto) {
        return ResponseEntity.ok("Hola " + dto.getNombre());
    }
}
