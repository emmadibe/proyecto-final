package com.torneo.api.services;

import com.torneo.api.dto.RegisterRequest;
import com.torneo.api.models.User;
import com.torneo.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Servicio que implementa UserDetailsService.
 * Permite a Spring Security cargar usuarios desde la base de datos.
 * Es invocado automáticamente al procesar un login JWT.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }



    public Optional<User> getById(Long id)
    {
        return userRepository.findById(id);
    }


}
