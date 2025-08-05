package com.torneo.api.services;

import com.torneo.api.dto.TeamXPlayerRequestDTO;
import com.torneo.api.dto.TeamXPlayerResponseDTO;
import com.torneo.api.exceptions.NotFoundException;
import com.torneo.api.models.TeamEntity;
import com.torneo.api.models.TeamXPlayer;
import com.torneo.api.models.User;
import com.torneo.api.repository.TeamRepository;
import com.torneo.api.repository.TeamXPlayerRepository;
import com.torneo.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio que gestiona la asignación de jugadores a equipos (TeamXPlayer).
 *
 * ✔ Permite agregar jugadores a un equipo, indicando si son capitanes.
 * ✔ Evita que un mismo jugador sea agregado más de una vez al mismo equipo.
 * ✔ Ofrece métodos para consultar todos los jugadores de un equipo específico.
 * ✔ Convierte las entidades a DTOs para facilitar la comunicación con la API.
 *
 * ✨ Validaciones:
 * - Si se intenta agregar un jugador que ya está en el equipo, lanza una excepción.
 * - Si ya existe un capitán en el equipo, no permite agregar otro con el mismo rol.
 *
 * Esta clase representa el núcleo de la relación muchos-a-muchos entre equipos y usuarios.
 */
@Service
@RequiredArgsConstructor
public class TeamXPlayerService {

    @Autowired
    private TeamXPlayerRepository teamXPlayerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    public TeamXPlayerResponseDTO createTeamXPlayer(TeamXPlayerRequestDTO dto) {
        TeamEntity team = teamRepository.findById(dto.getTeamID())
                .orElseThrow(() -> new NotFoundException("No existe el team"));

        User user = userRepository.findById(dto.getUserID())
                .orElseThrow(() -> new NotFoundException("No existe el user"));

        // Validar que el jugador no esté ya en el equipo
        boolean yaExiste = teamXPlayerRepository.findByTeamEntity_Id(team.getId()).stream()
                .anyMatch(txp -> txp.getUser().getId().equals(user.getId()));
        if (yaExiste) {
            throw new IllegalArgumentException("El jugador ya está en este equipo.");
        }

        // Validar que no haya otro capitán si este jugador será capitán
        if (dto.isCaptain()) {
            boolean yaHayCapitan = teamXPlayerRepository.findByTeamEntity_Id(team.getId()).stream()
                    .anyMatch(TeamXPlayer::isCaptain);
            if (yaHayCapitan) {
                throw new IllegalArgumentException("Este equipo ya tiene un capitán asignado.");
            }
        }

        TeamXPlayer teamXPlayer = TeamXPlayer.builder()
                .teamEntity(team)
                .user(user)
                .isCaptain(dto.isCaptain())
                .build();

        return mapToResponseDTO(teamXPlayerRepository.save(teamXPlayer));
    }

    public List<TeamXPlayer> getByTeamId(Long teamID) {
        return teamXPlayerRepository.findByTeamEntity_Id(teamID);
    }

    public List<TeamXPlayerResponseDTO> getDTOsByTeamId(Long teamID) {
        return getByTeamId(teamID).stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    private TeamXPlayerResponseDTO mapToResponseDTO(TeamXPlayer txp) {
        return TeamXPlayerResponseDTO.builder()
                .id(txp.getId())
                .teamID(txp.getTeamEntity().getId())
                .userID(txp.getUser().getId())
                .isCaptain(txp.isCaptain())
                .build();
    }
}
