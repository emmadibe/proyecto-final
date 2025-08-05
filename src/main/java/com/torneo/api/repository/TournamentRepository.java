package com.torneo.api.repository;

import com.torneo.api.enums.GamesCategory;
import com.torneo.api.enums.GamesState;
import com.torneo.api.models.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    List<Tournament> findByState(GamesState state);

    List<Tournament> findByCategory(GamesCategory category);

    List<Tournament> findByStartDate(LocalDate startDate);

    List<Tournament> findByOrganizer_Id(Long id);

}
