/**
 * Servicio que encapsula la lógica de envío de correos electrónicos.
 *
 * ✔ Usa JavaMailSender para enviar mails SMTP.
 * ✔ Permite enviar mensajes simples con asunto, destinatario y cuerpo.
 * ✔ Se puede invocar desde cualquier parte del sistema (inscripciones, alertas, etc.).
 */

package com.torneo.api.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("torneos@tuapp.com"); // Podés personalizarlo
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void enviarCorreoRegistroHtml(String para, String nombreUsuario) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

        // Prepara el contexto para Thymeleaf
        Context context = new Context();
        context.setVariable("nombre", nombreUsuario);

        // Procesa la plantilla
        String contenidoHtml = templateEngine.process("email/bienvenida.html", context);

        helper.setTo(para);
        helper.setSubject("Bienvenido a nuestra plataforma");
        helper.setText(contenidoHtml, true); // true = HTML

        helper.setFrom("tu-correo@gmail.com");

        mailSender.send(mensaje);
    }

    public void campeonEmail(String para) throws MessagingException
    {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

        // Prepara el contexto para Thymeleaf
        Context context = new Context();

        // Procesa la plantilla
        String contenidoHtml = templateEngine.process("email/campeon.html", context);

        helper.setTo(para);
        helper.setSubject("CAMPEON");
        helper.setText(contenidoHtml, true); // true = HTML

        helper.setFrom("tu-correo@gmail.com");

        mailSender.send(mensaje);
    }


}
