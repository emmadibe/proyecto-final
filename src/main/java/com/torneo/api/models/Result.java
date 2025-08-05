/**
 * Entidad que representa el resultado de un enfrentamiento entre dos equipos en un torneo.
 *
 * ✔ Guarda el equipo ganador, perdedor y los puntajes.
 * ✔ Se relaciona con un `Tournament` específico.
 * ✔ Permite registrar estadísticas más completas.
 */

package com.torneo.api.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_team_id", nullable = false)
    private TeamEntity winnerTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loser_team_id", nullable = false)
    private TeamEntity loserTeam;

    @Column(nullable = false)
    private Integer scoreWinnerTeam;

    @Column(nullable = false)
    private Integer scoreLoserTeam;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private  Match match;

}
