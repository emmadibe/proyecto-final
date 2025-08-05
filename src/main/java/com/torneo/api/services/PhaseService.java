package com.torneo.api.services;

import com.torneo.api.models.Match;
import com.torneo.api.models.TeamEntity;
import com.torneo.api.models.Tournament;
import com.torneo.api.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona las fases de los torneos (partidos).
 * ✔ Genera los partidos iniciales al completar el cupo.
 * ✔ Genera nuevas fases a medida que se completan las anteriores.
 */
@Service
@RequiredArgsConstructor
public class PhaseService {

    private final MatchRepository matchRepository;

    public void generateInitialPhase(Tournament tournament, List<TeamEntity> equipos) {
        Collections.shuffle(equipos);

        for (int i = 0; i < equipos.size(); i += 2) {
            Match match = Match.builder()
                    .tournament(tournament)
                    .firstTeam(equipos.get(i))
                    .secondTeam(equipos.get(i + 1))
                    .firstTeamScore(0)
                    .secondTeamScore(0)
                    .status("PENDIENTE")
                    .build();

            matchRepository.save(match);
        }
    }

    public void generateNextPhase(Tournament tournament) {
        List<Match> finalizados = matchRepository.findByTournamentId(tournament.getId()).stream()
                .filter(m -> m.getStatus().equalsIgnoreCase("FINALIZADO"))
                .collect(Collectors.toList());

        int cantidadEsperada = tournament.getMaxTeams() - 1;
        if (finalizados.size() < cantidadEsperada / 2 || finalizados.size() % 2 != 0) return;

        List<TeamEntity> ganadores = finalizados.stream()
                .map(this::getWinner)
                .collect(Collectors.toList());

        if (ganadores.size() >= 2) {
            generateInitialPhase(tournament, ganadores);
        }
    }

    private TeamEntity getWinner(Match match) {
        return match.getFirstTeamScore() >= match.getSecondTeamScore()
                ? match.getFirstTeam()
                : match.getSecondTeam();
    }
}
