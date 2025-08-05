/**
 * Entidad que representa un partido entre dos equipos dentro de un torneo.
 *
 * ✔ Cada partido pertenece a un torneo.
 * ✔ Tiene un equipo 1, equipo 2, sus respectivos puntajes y un estado (ej. "PENDIENTE", "FINALIZADO").
 * ✔ Se usa para programar y registrar resultados de enfrentamientos.
 */

package com.torneo.api.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con torneo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    // Equipo 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_team_id", nullable = false)
    private TeamEntity firstTeam;

    // Equipo 2
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_team_id", nullable = false)
    private TeamEntity secondTeam;

    @Column(nullable = false)
    private Integer firstTeamScore;

    @Column(nullable = false)
    private Integer secondTeamScore;

    @Column(nullable = false)
    private String status;
}
