package com.torneo.api.models;

import com.torneo.api.enums.GamesCategory;
import com.torneo.api.enums.GamesState;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entidad que representa un torneo de videojuegos.
 * Tiene relación con un organizador (usuario) y define su nombre, juego, estado, categoría y fechas.
 */
@Entity
@Table(name = "tournaments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String game;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GamesCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GamesState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "max_teams", nullable = false)
    private Integer maxTeams;

}
