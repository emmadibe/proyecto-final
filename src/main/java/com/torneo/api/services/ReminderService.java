package com.torneo.api.services;

import com.torneo.api.models.Inscription;
import com.torneo.api.repository.InscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio encargado de enviar recordatorios automáticos por mail
 * a los equipos inscritos cuyo torneo comienza al día siguiente.
 */
@Service
@RequiredArgsConstructor
public class ReminderService {

    private final InscriptionRepository inscriptionRepository;
    private final EmailService emailService;

    /**
     * Esta tarea se ejecuta todos los días a las 8:00 AM
     * y busca torneos que empiezan al día siguiente para notificar a los equipos.
     */
    @Scheduled(cron = "0 0 8 * * *") // todos los días a las 08:00 AM
    public void enviarRecordatorios() {
        LocalDate manana = LocalDate.now().plusDays(1);
        List<Inscription> inscripciones = inscriptionRepository.findAll();

        for (Inscription inscripcion : inscripciones) {
            LocalDate fechaTorneo = inscripcion.getTournament().getStartDate();

            if (fechaTorneo.equals(manana)) {
                String nombreEquipo = inscripcion.getTeam().getName();
                String nombreTorneo = inscripcion.getTournament().getName();

                String body = """
                        <h3>¡Recordatorio!</h3>
                        <p>El equipo <strong>%s</strong> está inscrito en el torneo <strong>%s</strong>, que comienza mañana.</p>
                        <p>¡No olviden revisar el cronograma y preparar su estrategia!</p>
                        """.formatted(nombreEquipo, nombreTorneo);

                // Enviar el mail a una casilla genérica (reemplazá con una real si tenés email del equipo)
                emailService.sendEmail("organizador@torneos.com", "Recordatorio: Torneo mañana", body);
            }
        }
    }
}
