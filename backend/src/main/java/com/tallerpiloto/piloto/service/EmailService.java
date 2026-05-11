package com.tallerpiloto.piloto.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String remitente;

    public void enviarEmailRecuperacion(String destinatario, String token) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(remitente);
        mensaje.setTo(destinatario);
        mensaje.setSubject("Recuperación de contraseña - Airline System");
        mensaje.setText(
                "Hola,\n\n" +
                        "Recibimos una solicitud para recuperar tu contraseña.\n\n" +
                        "Usa este token para cambiar tu contraseña:\n\n" +
                        token + "\n\n" +
                        "Este token expira en 1 hora.\n\n" +
                        "Si no solicitaste esto, ignora este email.\n\n" +
                        "Airline Management System"
        );
        mailSender.send(mensaje);
    }


}
