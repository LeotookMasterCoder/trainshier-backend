package com.trainshier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final RestTemplate restTemplate;

    @Value("${mailgun.base-url:https://api.mailgun.net/v3}")
    private String baseUrl;

    @Value("${mailgun.api-key:}")
    private String apiKey;

    @Value("${mailgun.domain:}")
    private String domain;

    @Value("${mailgun.from:TrainShier <postmaster@sandbox.mailgun.org>}")
    private String from;

    public void sendPasswordRecoveryEmail(String recipientEmail, String recipientName, String newPassword) {
        // Protect personal data / privacy in logs
        String maskedEmail = recipientEmail.replaceAll("(?<=.{3}).(?=[^@]*?.@)", "*");
        log.info("Iniciando envío de correo de recuperación para: {}", maskedEmail);

        if (apiKey == null || apiKey.trim().isEmpty() || domain == null || domain.trim().isEmpty()) {
            log.warn("Credenciales de Mailgun no configuradas. El correo de recuperación para {} no se pudo enviar de forma real y se simula en consola.", maskedEmail);
            return;
        }

        String senderFrom = from;
        if (senderFrom.contains("sandbox.mailgun.org") && !domain.contains("sandbox.mailgun.org")) {
            senderFrom = "TrainShier <postmaster@" + domain + ">";
        }

        try {
            String url = baseUrl + "/" + domain + "/messages";

            // Basic Auth header
            String credentials = "api:" + apiKey;
            String authHeader = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", authHeader);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("from", senderFrom);
            body.add("to", recipientEmail);
            body.add("subject", "Restablecimiento de Contraseña - TrainShier");
            body.add("text", String.format(
                "Estimado/a %s,\n\n" +
                "Tu contraseña en la plataforma TrainShier ha sido restablecida con éxito.\n\n" +
                "Tu nueva contraseña de acceso es: %s\n\n" +
                "Por motivos de seguridad, te recomendamos cambiar tu contraseña una vez inicies sesión.\n\n" +
                "Atentamente,\nEl Equipo de TrainShier",
                recipientName, newPassword
            ));

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Correo enviado exitosamente a {} (API Mailgun)", maskedEmail);
            } else {
                log.error("Fallo al enviar correo. Código de respuesta: {}. Respuesta: {}", response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            log.error("Error al enviar correo de recuperación vía Mailgun API a {}: {}", maskedEmail, e.getMessage(), e);
        }
    }

    public boolean sendRecoveryCodeEmail(String recipientEmail, String recipientName, String code) {
        // Protect personal data / privacy in logs
        String maskedEmail = recipientEmail.replaceAll("(?<=.{3}).(?=[^@]*?.@)", "*");
        log.info("Enviando código de recuperación a: {}", maskedEmail);

        if (apiKey == null || apiKey.trim().isEmpty() || domain == null || domain.trim().isEmpty()) {
            log.warn("Credenciales de Mailgun no configuradas. El código de recuperación {} para {} se simula en consola.", code, maskedEmail);
            return false;
        }

        String senderFrom = from;
        if (senderFrom.contains("sandbox.mailgun.org") && !domain.contains("sandbox.mailgun.org")) {
            senderFrom = "TrainShier <postmaster@" + domain + ">";
        }

        try {
            String url = baseUrl + "/" + domain + "/messages";

            // Basic Auth header
            String credentials = "api:" + apiKey;
            String authHeader = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", authHeader);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("from", senderFrom);
            body.add("to", recipientEmail);
            body.add("subject", "Código de Recuperación de Contraseña - TrainShier");
            body.add("text", String.format(
                "Estimado/a %s,\n\n" +
                "Has solicitado restablecer tu contraseña en la plataforma TrainShier.\n\n" +
                "Tu código de verificación de 6 dígitos es: %s\n\n" +
                "Este código expirará pronto. Si no solicitaste este cambio, puedes ignorar este correo.\n\n" +
                "Atentamente,\nEl Equipo de TrainShier",
                recipientName, code
            ));

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Código enviado exitosamente a {} (API Mailgun)", maskedEmail);
                return true;
            } else {
                log.error("Fallo al enviar código de recuperación. Código de respuesta: {}. Respuesta: {}", response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            log.error("Error al enviar código de recuperación vía Mailgun API a {}: {}", maskedEmail, e.getMessage(), e);
        }
        return false;
    }
}
