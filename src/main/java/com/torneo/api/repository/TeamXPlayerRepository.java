package com.torneo.api.repository;

import com.torneo.api.models.TeamXPlayer;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TeamXPlayerRepository extends JpaRepository<TeamXPlayer, Long>
{
    List<TeamXPlayer> findByTeamEntity_Id(Long id);///→ Devuelve todos los usuarios que pertenecen a un equipo.
    List<TeamXPlayer> findByUser_IdAndTeamEntity_Id(Long userId, Long teamId);///→ Verifica si un usuario específico pertenece a un equipo específico (clave para validar inscripciones).

}
