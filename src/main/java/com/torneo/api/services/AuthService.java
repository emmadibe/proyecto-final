package com.torneo.api.services;

import com.torneo.api.dto.*;
import com.torneo.api.models.User;
import com.torneo.api.repository.UserRepository;
import com.torneo.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Servicio que gestiona el registro y login de usuarios.
 * En el registro encripta la contraseña y genera el token.
 * En el login verifica las credenciales y devuelve un JWT.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    public LoginResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);
        return new LoginResponse(token);
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);
        return new LoginResponse(token);
    }

    public void updateAuthenticatedUser(UpdateUserRequest request) {
        // Obtener el usuario autenticado desde el contexto de seguridad
     //   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       // String email = authentication.getName();
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Actualizar los campos permitidos
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        userRepository.save(user);
    }

    public List<UserResponse> getAllUsers()
    {
        return userRepository.findAll().stream()
                .map(usuario -> new UserResponse(usuario.getId(), usuario.getUsername(), usuario.getEmail()))
                .toList();
    }

    public  void deleteuser(long id)
    {
        userRepository.deleteById(id);
    }
}
