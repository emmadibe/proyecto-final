package com.torneo.api.controllers;

import com.torneo.api.dto.*;
import com.torneo.api.models.User;
import com.torneo.api.services.AuthService;
import com.torneo.api.services.EmailService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador que expone los endpoints públicos para autenticación.
 * <p>
 * - POST /api/auth/register → Registra un nuevo usuario
 * - POST /api/auth/login → Autentica y genera un token JWT
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Autowired
    private EmailService emailService;

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param request los datos del nuevo usuario a registrar
     * @return ResponseEntity con la respuesta de login (JWT)
     */
    @Operation(summary = "Registra un nuevo usuario",
            description = "Este endpoint permite registrar un nuevo usuario en el sistema. "
                    + "Después del registro, se envía un correo de bienvenida.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de registro inválidos"),
            @ApiResponse(responseCode = "500", description = "Error en el servidor al enviar el correo de bienvenida")
    })
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(
            @Valid @RequestBody @Parameter(description = "Datos para registrar un nuevo usuario") RegisterRequest request) {
        try {
            // Enviar correo de bienvenida
            emailService.enviarCorreoRegistroHtml(request.getEmail(), request.getUsername());
        } catch (MessagingException e) {
            System.err.println(e);
        }
        return ResponseEntity.ok(authService.register(request));
    }

    @PutMapping("/update")
    @Operation(summary = "Actualizar perfil del usuario autenticado",
            description = "Permite al usuario autenticado actualizar su perfil personal.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil actualizado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error en el servidor")
    })
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateUserRequest request) {
        try {
            authService.updateAuthenticatedUser(request);
            return ResponseEntity.ok("Perfil actualizado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el perfil");
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id)
    {
        authService.deleteuser(id);
    }

    /**
     * Endpoint para autenticar a un usuario y generar un token JWT.
     *
     * @param request las credenciales de usuario (username y password)
     * @return ResponseEntity con el token JWT generado
     */
    @Operation(summary = "Autentica un usuario",
            description = "Este endpoint autentica al usuario con su nombre de usuario y contraseña, "
                    + "y devuelve un token JWT para la autenticación futura.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario autenticado con éxito"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "500", description = "Error interno en el servidor")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody @Parameter(description = "Credenciales de acceso para login") LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAllUser()
    {
        return ResponseEntity.ok((authService.getAllUsers()));
    }
}
