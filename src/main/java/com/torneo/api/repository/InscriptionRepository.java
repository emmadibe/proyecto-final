package com.torneo.api.repository;

import com.torneo.api.models.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> { //Tabla intermedia entre equipos y torneos.

    List<Inscription> findByTournamentId(Long tournamentId);

    List<Inscription> findByTeamId(Long teamId);

    Optional<Inscription> findByTeam_IdAndTournament_Id(Long teamId, Long tournamentId);

}
