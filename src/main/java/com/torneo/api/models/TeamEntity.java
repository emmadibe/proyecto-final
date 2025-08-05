/**
 * Entidad que representa un equipo participante en torneos de videojuegos.
 *
 * ✔ Tiene nombre único.
 * ✔ Se relaciona con un torneo específico (`ManyToOne`).
 * ✔ Puede tener múltiples jugadores (`OneToMany` con `PlayerEntity`).
 * ✔ Puede estar inscrito en múltiples torneos mediante `Inscription`.
 */

package com.torneo.api.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "teams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "captain_id")
    private User captain;

    @OneToMany(mappedBy = "teamEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamXPlayer> teamXPlayers;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscription> inscriptions;



}
