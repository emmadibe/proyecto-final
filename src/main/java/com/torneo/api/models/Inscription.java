/**
 * Entidad que representa la inscripción de un equipo a un torneo.
 *
 * ✔ Cada inscripción tiene una fecha y un costo.
 * ✔ Se relaciona con un equipo (`TeamEntity`) y un torneo (`Tournament`).
 * ✔ Los jugadores quedan automáticamente vinculados a través del equipo.
 *
 * ⚠ Esta inscripción NO es por jugador individual, sino por equipo completo.
 *    Es decir, cuando un equipo se inscribe, todos sus jugadores participan del torneo.
 */

package com.torneo.api.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "inscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;
}
