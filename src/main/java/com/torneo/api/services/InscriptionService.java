package com.torneo.api.services;

import com.torneo.api.dto.InscriptionRequestDTO;
import com.torneo.api.dto.InscriptionResponseDTO;
import com.torneo.api.enums.GamesState;
import com.torneo.api.exceptions.NotFoundException;
import com.torneo.api.models.Inscription;
import com.torneo.api.models.TeamEntity;
import com.torneo.api.models.TeamXPlayer;
import com.torneo.api.models.Tournament;
import com.torneo.api.models.User;
import com.torneo.api.repository.InscriptionRepository;
import com.torneo.api.repository.TeamRepository;
import com.torneo.api.repository.TeamXPlayerRepository;
import com.torneo.api.repository.TournamentRepository;
import com.torneo.api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona las inscripciones de equipos a torneos.
 *
 * ✔ Valida que el equipo y el torneo existan.
 * ✔ Verifica que el usuario que intenta inscribir sea parte del equipo (nuevo).
 * ✔ Evita inscripciones duplicadas al mismo torneo.
 * ✔ Controla que el cupo máximo no haya sido alcanzado.
 * ✔ Si se completa el cupo, cambia el estado del torneo a ACTIVO.
 * ✔ Genera los partidos iniciales mediante el PhaseService.
 * ✔ Envía un email a cada jugador cuando el torneo comienza.
 *
 * Esta clase es clave para el flujo de inscripción, ya que asegura la integridad
 * y lógica de negocio antes de permitir registrar un equipo en un torneo.
 */
@Service
@RequiredArgsConstructor
public class InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;
    private final EmailService emailService;
    private final PhaseService phaseService;
    private final TeamXPlayerService teamXPlayerService;
    private final TeamXPlayerRepository teamXPlayerRepository;
    private final UserRepository userRepository;

    public InscriptionResponseDTO registerInscription(InscriptionRequestDTO dto) {
        // Validar existencia del equipo
        TeamEntity team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new NotFoundException("Equipo no encontrado"));

        // Validar existencia del torneo
        Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
                .orElseThrow(() -> new NotFoundException("Torneo no encontrado"));

        // Validar estado del torneo
        if (!tournament.getState().equals(GamesState.INSCRIPTION)) {
            throw new IllegalStateException("El torneo no está disponible para inscripciones.");
        }

        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // Verificar si el usuario pertenece al equipo
        List<TeamXPlayer> asociaciones = teamXPlayerRepository.findByUser_IdAndTeamEntity_Id(currentUser.getId(), dto.getTeamId());
        if (asociaciones.isEmpty()) {
            throw new IllegalArgumentException("No pertenecés al equipo que estás intentando inscribir.");
        }

        // Validar si ya está inscripto
        if (inscriptionRepository.findByTeam_IdAndTournament_Id(team.getId(), tournament.getId()).isPresent()) {
            throw new IllegalArgumentException("Este equipo ya está inscripto en este torneo.");
        }

        // Validar cupo disponible
        List<Inscription> inscripcionesExistentes = inscriptionRepository.findByTournamentId(tournament.getId());
        if (inscripcionesExistentes.size() >= tournament.getMaxTeams()) {
            throw new IllegalStateException("El cupo del torneo ya está completo.");
        }

        // Guardar la inscripción
        Inscription nueva = Inscription.builder()
                .team(team)
                .tournament(tournament)
                .build();

        inscriptionRepository.save(nueva);

        // Verificar si se completó el cupo con esta inscripción
        List<Inscription> inscripcionesTotales = inscriptionRepository.findByTournamentId(tournament.getId());
        if (inscripcionesTotales.size() == tournament.getMaxTeams()) {
            // Cambiar el estado a ACTIVO
            tournament.setState(GamesState.ACTIVE);
            tournamentRepository.save(tournament);

            // Obtener todos los equipos
            List<TeamEntity> equipos = inscripcionesTotales.stream()
                    .map(Inscription::getTeam)
                    .collect(Collectors.toList());

            // Generar los partidos iniciales
            phaseService.generateInitialPhase(tournament, equipos);

            // Enviar correo a los jugadores de cada equipo
            for (TeamEntity equipo : equipos) {
                List<TeamXPlayer> jugadores = teamXPlayerService.getByTeamId(equipo.getId());
                for (TeamXPlayer txp : jugadores) {
                    emailService.sendEmail(
                            txp.getUser().getEmail(),
                            "¡Comienzan los partidos!",
                            "Ya están definidos los partidos del torneo: " + tournament.getName()
                    );
                }
            }
        }

        return mapToDTO(nueva);
    }

    public List<InscriptionResponseDTO> getAll() {
        return inscriptionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<InscriptionResponseDTO> getByTournament(Long tournamentId) {
        return inscriptionRepository.findByTournamentId(tournamentId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<InscriptionResponseDTO> getByTeam(Long teamId) {
        return inscriptionRepository.findByTeamId(teamId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Inscription getInscriptionByTeamAndTournament(Long teamId, Long tournamentId) {
        return inscriptionRepository.findByTeam_IdAndTournament_Id(teamId, tournamentId)
                .orElseThrow(() -> new EntityNotFoundException("Inscripción no encontrada"));
    }

    public void delete(Long id) {
        if (!inscriptionRepository.existsById(id)) {
            throw new NotFoundException("Inscripción no encontrada");
        }
        inscriptionRepository.deleteById(id);
    }

    private InscriptionResponseDTO mapToDTO(Inscription i) {
        return InscriptionResponseDTO.builder()
                .id(i.getId())
                .teamName(i.getTeam().getName())
                .tournamentName(i.getTournament().getName())
                .build();
    }
}
