package com.torneo.api.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entidad que representa un partido entre dos equipos dentro de un torneo.
 * Incluye los equipos involucrados, el resultado, la fecha y el estado del partido.
 */
@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tournament_id", nullable = false)
    private Long tournamentId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "first_team_id", referencedColumnName = "id")
    private TeamEntity firstTeam;

    @ManyToOne(optional = false)
    @JoinColumn(name = "second_team_id", referencedColumnName = "id")
    private TeamEntity secondTeam;

    @Column(name = "match_date", nullable = false)
    private LocalDate matchDate;

    private Integer firstTeamScore;

    private Integer secondTeamScore;

    @Column(nullable = false)
    private String status; // Ej: "PENDIENTE", "JUGADO", "CANCELADO"
}
