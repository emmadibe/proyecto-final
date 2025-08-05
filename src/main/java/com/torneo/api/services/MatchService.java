/**
 * Servicio que gestiona la lógica de los partidos (matches) entre equipos.
 *
 * ✔ Permite crear, editar, eliminar y listar partidos.
 * ✔ Cada partido pertenece a un torneo.
 * ✔ Cada partido tiene dos equipos, sus puntajes y un estado (ej. PENDIENTE, FINALIZADO).
 */

package com.torneo.api.services;

import com.torneo.api.dto.MatchRequestDTO;
import com.torneo.api.dto.MatchResponseDTO;
import com.torneo.api.exceptions.NotFoundException;
import com.torneo.api.models.Match;
import com.torneo.api.models.TeamEntity;
import com.torneo.api.models.Tournament;
import com.torneo.api.repository.MatchRepository;
import com.torneo.api.repository.TeamRepository;
import com.torneo.api.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;

    public MatchResponseDTO createMatch(MatchRequestDTO dto) {
        Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
                .orElseThrow(() -> new NotFoundException("Torneo no encontrado"));

        TeamEntity firstTeam = teamRepository.findById(dto.getFirstTeamId())
                .orElseThrow(() -> new NotFoundException("Primer equipo no encontrado"));

        TeamEntity secondTeam = teamRepository.findById(dto.getSecondTeamId())
                .orElseThrow(() -> new NotFoundException("Segundo equipo no encontrado"));

        Match match = Match.builder()
                .tournament(tournament)
                .firstTeam(firstTeam)
                .secondTeam(secondTeam)
                .firstTeamScore(dto.getFirstTeamScore())
                .secondTeamScore(dto.getSecondTeamScore())
                .status(dto.getStatus())
                .build();

        return mapToDTO(matchRepository.save(match));
    }

    public MatchResponseDTO updateMatch(Long id, MatchRequestDTO dto) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Partido no encontrado"));

        Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
                .orElseThrow(() -> new NotFoundException("Torneo no encontrado"));

        TeamEntity firstTeam = teamRepository.findById(dto.getFirstTeamId())
                .orElseThrow(() -> new NotFoundException("Primer equipo no encontrado"));

        TeamEntity secondTeam = teamRepository.findById(dto.getSecondTeamId())
                .orElseThrow(() -> new NotFoundException("Segundo equipo no encontrado"));

        match.setTournament(tournament);
        match.setFirstTeam(firstTeam);
        match.setSecondTeam(secondTeam);
        match.setFirstTeamScore(dto.getFirstTeamScore());
        match.setSecondTeamScore(dto.getSecondTeamScore());
        match.setStatus(dto.getStatus());

        return mapToDTO(matchRepository.save(match));
    }

    public void deleteMatch(Long id) {
        if (!matchRepository.existsById(id)) {
            throw new NotFoundException("Partido no encontrado");
        }
        matchRepository.deleteById(id);
    }

    public List<MatchResponseDTO> getAllMatches() {
        return matchRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<MatchResponseDTO> getMatchesByTournament(Long tournamentId) {
        return matchRepository.findByTournamentId(tournamentId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public MatchResponseDTO getMatchById(Long id) {
        return matchRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new NotFoundException("Partido no encontrado"));
    }

    private MatchResponseDTO mapToDTO(Match match) {
        return MatchResponseDTO.builder()
                .id(match.getId())
                .tournamentName(match.getTournament().getName())
                .firstTeamName(match.getFirstTeam().getName())
                .secondTeamName(match.getSecondTeam().getName())
                .firstTeamScore(match.getFirstTeamScore())
                .secondTeamScore(match.getSecondTeamScore())
                .status(match.getStatus())
                .build();
    }
}
