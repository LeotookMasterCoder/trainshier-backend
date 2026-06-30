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

    @Value("${resend.api-key:}")
    private String resendApiKey;

    @Value("${brevo.api-key:}")
    private String brevoApiKey;

    public void sendPasswordRecoveryEmail(String recipientEmail, String recipientName, String newPassword) {
        String body = String.format(
            "Estimado/a %s,\n\n" +
            "Tu contraseña en la plataforma TrainShier ha sido restablecida con éxito.\n\n" +
            "Tu nueva contraseña de acceso es: %s\n\n" +
            "Por motivos de seguridad, te recomendamos cambiar tu contraseña una vez inicies sesión.\n\n" +
            "Atentamente,\nEl Equipo de TrainShier",
            recipientName, newPassword
        );
        sendEmailViaApi(recipientEmail, "Restablecimiento de Contraseña - TrainShier", body);
    }

    public boolean sendRecoveryCodeEmail(String recipientEmail, String recipientName, String code) {
        String body = String.format(
            "Estimado/a %s,\n\n" +
            "Has solicitado restablecer tu contraseña en la plataforma TrainShier.\n\n" +
            "Tu código de verificación de 6 dígitos es: %s\n\n" +
            "Este código expirará pronto. Si no solicitaste este cambio, puedes ignorar este correo.\n\n" +
            "Atentamente,\nEl Equipo de TrainShier",
            recipientName, code
        );
        return sendEmailViaApi(recipientEmail, "Código de Recuperación de Contraseña - TrainShier", body);
    }

    private boolean sendEmailViaApi(String recipientEmail, String subject, String bodyText) {
        String maskedEmail = recipientEmail.replaceAll("(?<=.{3}).(?=[^@]*?.@)", "*");

        // 1. Try Resend if API key is set
        if (resendApiKey != null && !resendApiKey.trim().isEmpty()) {
            try {
                String url = "https://api.resend.com/emails";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "Bearer " + resendApiKey.trim());

                java.util.Map<String, Object> body = new java.util.HashMap<>();
                body.put("from", "TrainShier <onboarding@resend.dev>");
                body.put("to", new String[]{recipientEmail});
                body.put("subject", subject);
                body.put("text", bodyText);

                HttpEntity<java.util.Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
                ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    log.info("Correo enviado exitosamente a {} (API Resend)", maskedEmail);
                    return true;
                } else {
                    log.error("Fallo al enviar correo vía Resend. Código: {}. Respuesta: {}", response.getStatusCode(), response.getBody());
                }
            } catch (Exception e) {
                log.error("Error al enviar correo vía Resend API a {}: {}", maskedEmail, e.getMessage());
            }
        }

        // 2. Try Brevo if API key is set
        if (brevoApiKey != null && !brevoApiKey.trim().isEmpty()) {
            try {
                String url = "https://api.brevo.com/v3/smtp/email";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("api-key", brevoApiKey.trim());

                java.util.Map<String, Object> body = new java.util.HashMap<>();
                body.put("sender", java.util.Map.of("name", "TrainShier", "email", "postmaster@sandbox.mailgun.org"));
                body.put("to", java.util.List.of(java.util.Map.of("email", recipientEmail)));
                body.put("subject", subject);
                body.put("textContent", bodyText);

                HttpEntity<java.util.Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
                ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    log.info("Correo enviado exitosamente a {} (API Brevo)", maskedEmail);
                    return true;
                } else {
                    log.error("Fallo al enviar correo vía Brevo. Código: {}. Respuesta: {}", response.getStatusCode(), response.getBody());
                }
            } catch (Exception e) {
                log.error("Error al enviar correo vía Brevo API a {}: {}", maskedEmail, e.getMessage());
            }
        }

        // 3. Try Mailgun if API key is set
        if (apiKey != null && !apiKey.trim().isEmpty() && domain != null && !domain.trim().isEmpty()) {
            try {
                String url = baseUrl + "/" + domain + "/messages";
                String credentials = "api:" + apiKey;
                String authHeader = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                headers.set("Authorization", authHeader);

                String senderFrom = from;
                if (domain != null && !domain.trim().isEmpty()) {
                    senderFrom = "TrainShier <postmaster@" + domain.trim() + ">";
                }

                MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
                body.add("from", senderFrom);
                body.add("to", recipientEmail);
                body.add("subject", subject);
                body.add("text", bodyText);

                HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
                ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    log.info("Correo enviado exitosamente a {} (API Mailgun)", maskedEmail);
                    return true;
                } else {
                    log.error("Fallo al enviar correo vía Mailgun. Código: {}. Respuesta: {}", response.getStatusCode(), response.getBody());
                }
            } catch (Exception e) {
                log.error("Error al enviar correo vía Mailgun API a {}: {}", maskedEmail, e.getMessage());
            }
        }

        log.warn("Ninguna API de correo (Resend, Brevo o Mailgun) está configurada o todas fallaron. El correo para {} no se envió.", maskedEmail);
        return false;
    }
}
