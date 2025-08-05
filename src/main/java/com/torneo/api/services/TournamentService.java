package com.torneo.api.services;

import com.torneo.api.dto.TournamentRequestDTO;
import com.torneo.api.dto.TournamentResponseDTO;
import com.torneo.api.enums.GamesCategory;
import com.torneo.api.enums.GamesState;
import com.torneo.api.exceptions.NotFoundException;
import com.torneo.api.models.Tournament;
import com.torneo.api.models.User;
import com.torneo.api.repository.TournamentRepository;
import com.torneo.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona la lógica de negocio relacionada a torneos.
 * ✔ Se agregó validación de cupo permitido (2, 4, 8 o 16).
 * ✔ Se reflejó el campo maxTeams en los DTOs.
 */
@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;

    public TournamentResponseDTO createTournament(TournamentRequestDTO dto) {
        validarCupo(dto.getMaxTeams());

        User organizer = userRepository.findById(dto.getOrganizerId())
                .orElseThrow(() -> new NotFoundException("Organizador no encontrado"));

        Tournament tournament = Tournament.builder()
                .name(dto.getName())
                .game(dto.getGame())
                .category(dto.getCategory())
                .state(dto.getState())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .organizer(organizer)
                .maxTeams(dto.getMaxTeams())
                .build();

        return mapToResponseDTO(tournamentRepository.save(tournament));
    }

    public TournamentResponseDTO updateTournament(Long id, TournamentRequestDTO dto) {
        validarCupo(dto.getMaxTeams());

        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Torneo no encontrado"));

        User organizer = userRepository.findById(dto.getOrganizerId())
                .orElseThrow(() -> new NotFoundException("Organizador no encontrado"));

        tournament.setName(dto.getName());
        tournament.setGame(dto.getGame());
        tournament.setCategory(dto.getCategory());
        tournament.setState(dto.getState());
        tournament.setStartDate(dto.getStartDate());
        tournament.setEndDate(dto.getEndDate());
        tournament.setOrganizer(organizer);
        tournament.setMaxTeams(dto.getMaxTeams());

        return mapToResponseDTO(tournamentRepository.save(tournament));
    }

    public List<TournamentResponseDTO> getAllTournaments() {
        return tournamentRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public TournamentResponseDTO getTournamentById(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Torneo no encontrado"));
        return mapToResponseDTO(tournament);
    }

    public List<TournamentResponseDTO> getByState(GamesState state) {
        return tournamentRepository.findByState(state).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TournamentResponseDTO> getByCategory(GamesCategory category) {
        return tournamentRepository.findByCategory(category).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public void deleteTournament(Long id) {
        if (!tournamentRepository.existsById(id)) {
            throw new NotFoundException("Torneo no encontrado");
        }
        tournamentRepository.deleteById(id);
    }

    public void update(Tournament tournament) {
        tournamentRepository.save(tournament);
    }

    private TournamentResponseDTO mapToResponseDTO(Tournament t) {
        return TournamentResponseDTO.builder()
                .id(t.getId())
                .name(t.getName())
                .game(t.getGame())
                .category(t.getCategory())
                .state(t.getState())
                .organizerUsername(t.getOrganizer().getUsername())
                .startDate(t.getStartDate())
                .endDate(t.getEndDate())
                .maxTeams(t.getMaxTeams())
                .build();
    }

    private void validarCupo(Integer maxTeams) {
        List<Integer> validos = List.of(2, 4, 8, 16);
        if (!validos.contains(maxTeams)) {
            throw new IllegalArgumentException("El cupo debe ser 2, 4, 8 o 16.");
        }
    }
}
