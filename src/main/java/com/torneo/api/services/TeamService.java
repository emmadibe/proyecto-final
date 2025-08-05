/**
 * Servicio que gestiona las operaciones sobre equipos dentro del sistema.
 *
 * ✔ Crea, actualiza, elimina y consulta equipos.
 * ✔ Relaciona equipos con torneos si corresponde.
 * ✔ Maneja correctamente las conversiones de Set a List para los jugadores.
 * ✔ Convierte entidades a DTOs y viceversa.
 */

package com.torneo.api.services;

import com.torneo.api.dto.TeamRequestDTO;
import com.torneo.api.dto.TeamResponseDTO;
import com.torneo.api.dto.TeamXPlayerRequestDTO;
import com.torneo.api.exceptions.NotFoundException;
import com.torneo.api.models.*;
import com.torneo.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamXPlayerService teamXPlayerService;

    public void updateTeam(TeamResponseDTO dto)
    {
        TeamEntity team = TeamEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
        teamRepository.save(team);
    }

    public TeamResponseDTO createTeam(TeamRequestDTO dto) {

        // 1. Obtener todos los usuarios enviados
        List<User> players = userRepository.findAllById(dto.getPlayerIds()); //Si un id no existe, no emite una excepción; simplemente, lo omite.

        //2. Quiero corroborar que todos los ids pasados como usuarios para que pertezcan al team, existan:
        if(players.size() != dto.getPlayerIds().size()){
            System.err.println("Uno de los ids, por lo menos, no existe en la Base de datos");
            return null;
        }

        // 3. Crear el equipo
        TeamEntity team = TeamEntity.builder()
                .name(dto.getName())
                .build();
        
        //Guardo el equipo
        TeamResponseDTO teamResponseDTO = mapToDTO(teamRepository.save(team));

        // 3. Obtener al usuario autenticado (para marcarlo como capitán)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario autenticado no encontrado"));

        // 4. Crear TeamXPlayer por cada jugador (uno de ellos será capitán)
        for (User p : players) //Voy iterando la lista de jugadores.
        {
            boolean esCapitan = p.getId().equals(creator.getId()); //esCapital será True si el id de p coincide con el del usuario autentificado  (el crador del Team).

            TeamXPlayerRequestDTO teamXPlayerRequestDTO = TeamXPlayerRequestDTO.builder()
                    .teamID(teamResponseDTO.getId())
                    .userID(p.getId())
                    .isCaptain(esCapitan)
                    .build();

            teamXPlayerService.createTeamXPlayer(teamXPlayerRequestDTO);
        }

        return teamResponseDTO;
    }


    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new NotFoundException("Equipo no encontrado");
        }
        teamRepository.deleteById(id);
    }

    public List<TeamResponseDTO> listTeams() {
        return teamRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public TeamResponseDTO findTeamById(Long id) {
        return teamRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new NotFoundException("Equipo no encontrado"));
    }


    private TeamResponseDTO mapToDTO(TeamEntity team) {
        return TeamResponseDTO.builder()
                .id(team.getId())
                .name(team.getName())
                .build();
    }
}
