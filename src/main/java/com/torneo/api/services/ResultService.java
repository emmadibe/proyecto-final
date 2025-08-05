package com.torneo.api.services;

import com.torneo.api.dto.InscriptionResponseDTO;
import com.torneo.api.dto.MatchResponseDTO;
import com.torneo.api.dto.ResultCreateDTO;
import com.torneo.api.dto.ResultDTO;
import com.torneo.api.enums.GamesState;
import com.torneo.api.exceptions.NotFoundException;
import com.torneo.api.models.*;
import com.torneo.api.repository.ResultRepository;
import com.torneo.api.repository.TeamRepository;
import com.torneo.api.repository.TournamentRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona los resultados de partidos.
 *
 * ✔ Guarda el equipo ganador y perdedor.
 * ✔ Elimina al perdedor del torneo (inscription).
 * ✔ Elimina el partido jugado.
 * ✔ Verifica si quedan partidos. Si no, avanza fase o declara campeón.
 * ✔ Usa el método campeonEmail() para enviar el mail al ganador.
 */
@Service
@RequiredArgsConstructor
public class ResultService {

    private final ResultRepository resultRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;

    @Autowired private TeamXPlayerService teamXPlayerService;
    @Autowired private UserDetailsServiceImpl userDetailsService;
    @Autowired private InscriptionService inscriptionService;
    @Autowired private MatchService matchService;
    @Autowired private TournamentService tournamentService;
    @Autowired private EmailService emailService;
    @Autowired private PhaseService phaseService;

    public ResultDTO createResult(ResultCreateDTO dto) {
        Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
                .orElseThrow(() -> new NotFoundException("Torneo no encontrado"));

        TeamEntity winner = teamRepository.findById(dto.getWinerTeamId())
                .orElseThrow(() -> new NotFoundException("Equipo ganador no encontrado"));

        TeamEntity loser = teamRepository.findById(dto.getLoserTeamId())
                .orElseThrow(() -> new NotFoundException("Equipo perdedor no encontrado"));

        Result result = Result.builder()
                .tournament(tournament)
                .winnerTeam(winner)
                .loserTeam(loser)
                .scoreWinnerTeam(dto.getScoreWinnerTeam())
                .scoreLoserTeam(dto.getScoreLoserTeam())
                .build();

        // Eliminar inscripción del perdedor
        Inscription inscription = inscriptionService.getInscriptionByTeamAndTournament(
                loser.getId(), tournament.getId()
        );
        inscriptionService.delete(inscription.getId());

        // Eliminar partido relacionado
        List<MatchResponseDTO> partidos = matchService.getMatchesByTournament(tournament.getId());
        partidos.stream()
                .filter(p -> (p.getFirstTeamName().equals(winner.getName()) && p.getSecondTeamName().equals(loser.getName())) ||
                        (p.getFirstTeamName().equals(loser.getName()) && p.getSecondTeamName().equals(winner.getName())))
                .findFirst()
                .ifPresent(match -> matchService.deleteMatch(match.getId()));

        // Verificar si quedan partidos
        List<MatchResponseDTO> partidosRestantes = matchService.getMatchesByTournament(tournament.getId());

        if (partidosRestantes.isEmpty()) {
            List<InscriptionResponseDTO> inscripciones = inscriptionService.getByTournament(tournament.getId());

            if (inscripciones.size() == 1) {
                // CAMPEÓN → Enviar mail
                Long equipoId = Long.valueOf(inscripciones.get(0).getTeamID());
                List<TeamXPlayer> jugadores = teamXPlayerService.getByTeamId(equipoId);

                jugadores.stream().findFirst().ifPresent(txp -> {
                    Optional<User> user = userDetailsService.getById(txp.getUser().getId());
                    user.ifPresent(u -> {
                        try {
                            emailService.campeonEmail(u.getEmail());
                        } catch (MessagingException e) {
                            System.err.println("Error al enviar el email al campeón: " + e.getMessage());
                        }
                    });
                });

                // Marcar torneo como FINALIZADO
                tournament.setState(GamesState.FINISHED);
                tournamentService.update(tournament);
            } else {
                // Generar nueva fase
                phaseService.generateNextPhase(tournament);
            }
        }

        return mapToDTO(resultRepository.save(result));
    }

    public ResultDTO updateResult(Long id, ResultCreateDTO dto) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Resultado no encontrado"));

        Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
                .orElseThrow(() -> new NotFoundException("Torneo no encontrado"));

        TeamEntity winner = teamRepository.findById(dto.getWinerTeamId())
                .orElseThrow(() -> new NotFoundException("Equipo ganador no encontrado"));

        TeamEntity loser = teamRepository.findById(dto.getLoserTeamId())
                .orElseThrow(() -> new NotFoundException("Equipo perdedor no encontrado"));

        result.setTournament(tournament);
        result.setWinnerTeam(winner);
        result.setLoserTeam(loser);
        result.setScoreWinnerTeam(dto.getScoreWinnerTeam());
        result.setScoreLoserTeam(dto.getScoreLoserTeam());

        return mapToDTO(resultRepository.save(result));
    }

    public void deleteResult(Long id) {
        resultRepository.deleteById(id);
    }

    public List<ResultDTO> getAll() {
        return resultRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ResultDTO> getById(Long id) {
        return resultRepository.findById(id).map(this::mapToDTO);
    }

    private ResultDTO mapToDTO(Result result) {
        return ResultDTO.builder()
                .id(result.getId())
                .tournamentId(result.getTournament().getId())
                .winerTeamId(result.getWinnerTeam().getId())
                .loserTeamId(result.getLoserTeam().getId())
                .scoreWinnerTeam(result.getScoreWinnerTeam())
                .scoreLoserTeam(result.getScoreLoserTeam())
                .build();
    }
}
