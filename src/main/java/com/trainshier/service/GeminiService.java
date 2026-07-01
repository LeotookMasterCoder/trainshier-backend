package com.trainshier.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GeminiService {

    @Value("${gemini.api-key:}")
    private String apiKey;

    @Value("classpath:knowledge.txt")
    private org.springframework.core.io.Resource knowledgeResource;

    private String knowledgeBase = null;

    private final RestTemplate restTemplate = new RestTemplate();

    private synchronized String getKnowledgeBase() {
        if (knowledgeBase == null) {
            try {
                if (knowledgeResource != null && knowledgeResource.exists()) {
                    java.io.InputStream is = knowledgeResource.getInputStream();
                    knowledgeBase = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
                } else {
                    knowledgeBase = "INSTRUCCIONES DE SISTEMA:\nEres el Asistente de TrainShier. Responde preguntas sobre TrainShier y el simulador de POS. No hables de nada ajeno.";
                }
            } catch (Exception e) {
                log.error("Failed to load knowledge.txt", e);
                knowledgeBase = "Error al cargar la documentación de TrainShier.";
            }
        }
        return knowledgeBase;
    }

    public String generateAssistantResponse(String message) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            log.info("No Gemini API key found. Using simulated assistant response.");
            return "Lo siento, la API Key de Gemini no está configurada, por lo que el asistente interactivo está deshabilitado en este momento. Por favor revisa el manual de operaciones o contacta al administrador.";
        }

        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

            String systemInstructions = getKnowledgeBase();

            // Construct payload manually
            Map<String, Object> requestBody = new HashMap<>();
            
            Map<String, Object> contentMap = new HashMap<>();
            Map<String, Object> partMap = new HashMap<>();
            partMap.put("text", systemInstructions + "\n\nMensaje del usuario: " + message + "\n\nRespuesta del asistente:");
            contentMap.put("parts", List.of(partMap));
            requestBody.put("contents", List.of(contentMap));

            Map<String, Object> configMap = new HashMap<>();
            configMap.put("maxOutputTokens", 500);
            configMap.put("temperature", 0.3);
            requestBody.put("generationConfig", configMap);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, entity, Map.class);

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                Map responseBody = responseEntity.getBody();
                List candidates = (List) responseBody.get("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    Map candidate = (Map) candidates.get(0);
                    Map content = (Map) candidate.get("content");
                    if (content != null) {
                        List parts = (List) content.get("parts");
                        if (parts != null && !parts.isEmpty()) {
                            Map part = (Map) parts.get(0);
                            String text = (String) part.get("text");
                            if (text != null) {
                                return text.replace("\"", "")
                                           .replace("'", "")
                                           .replace("*", "")
                                           .trim();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error calling Gemini API for assistant: {}", e.getMessage());
        }

        return "Lo siento, ha ocurrido un error al procesar tu consulta con el asistente virtual de TrainShier.";
    }

    public String generateCustomerResponse(String customerName, String mood, String difficulty, String cartProducts, int patience, String message) {
        // Fallback if no API Key is provided
        if (apiKey == null || apiKey.trim().isEmpty()) {
            log.info("No Gemini API key found. Using simulated customer responses.");
            return getSimulatedResponse(mood, message);
        }

        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

            String systemInstructions = String.format(
                "Eres un cliente en un supermercado en una caja POS. Tu nombre es %s. " +
                "Tu estado de ánimo/actitud es: %s. La dificultad de la simulación es: %s. " +
                "Llevas en tu carrito estos productos: %s. Tu paciencia actual es de %d/100. " +
                "Responde en español de forma realista, muy corta (máximo 1 o 2 frases) y de acuerdo con tu temperamento. " +
                "No hables de nada ajeno al supermercado o la compra. Si te preguntan cosas sin sentido, responde que tienes prisa y quieres pagar. " +
                "EXCEPCIÓN IMPORTANTE: Si el usuario te hace preguntas básicas sobre cómo usar el simulador, qué es el simulador, cómo jugar, qué es el arqueo de caja o qué es TrainShier, debes salir brevemente de tu papel de cliente y responderle con amabilidad como un tutor de la plataforma TrainShier para explicarle cómo funciona la simulación y cómo proceder, y luego recuérdale con gracia volver al cobro de sus productos.",
                customerName, mood, difficulty, cartProducts, patience
            );

            // Construct payload manually with maps for spring serialization
            Map<String, Object> requestBody = new HashMap<>();
            
            Map<String, Object> contentMap = new HashMap<>();
            Map<String, Object> partMap = new HashMap<>();
            partMap.put("text", systemInstructions + "\n\nMensaje del cajero: " + message + "\n\nRespuesta del cliente:");
            contentMap.put("parts", List.of(partMap));
            requestBody.put("contents", List.of(contentMap));

            Map<String, Object> configMap = new HashMap<>();
            configMap.put("maxOutputTokens", 120);
            configMap.put("temperature", 0.7);
            requestBody.put("generationConfig", configMap);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, entity, Map.class);

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                Map responseBody = responseEntity.getBody();
                List candidates = (List) responseBody.get("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    Map candidate = (Map) candidates.get(0);
                    Map content = (Map) candidate.get("content");
                    if (content != null) {
                        List parts = (List) content.get("parts");
                        if (parts != null && !parts.isEmpty()) {
                            Map part = (Map) parts.get(0);
                            return (String) part.get("text");
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error calling Gemini API: {}. Falling back to simulated response.", e.getMessage());
        }

        return getSimulatedResponse(mood, message);
    }

    private String getSimulatedResponse(String mood, String message) {
        String msg = message.toLowerCase();
        
        if (msg.contains("simulador") || msg.contains("como uso") || msg.contains("cómo uso") || msg.contains("que es") || msg.contains("qué es") || msg.contains("ayuda") || msg.contains("como juego") || msg.contains("cómo juego") || msg.contains("arqueo")) {
            return "Como asistente de TrainShier te oriento: El simulador de caja registradora te permite practicar la facturación. Configura la dificultad a la derecha, dale a 'Iniciar Simulación', digita o busca productos por el botón 'Buscar Producto', registra el pago y realiza al final tu Arqueo de Caja sorpresa.";
        }

        if (mood == null) mood = "Amable";
        
        switch (mood.toLowerCase()) {
            case "impatiente":
            case "impaciente":
                if (msg.contains("bolsa")) return "Sí, por favor, pero apúrese que tengo prisa.";
                if (msg.contains("pago") || msg.contains("efectivo") || msg.contains("tarjeta")) return "Pagaré con tarjeta, por favor proceda rápido.";
                if (msg.contains("hola") || msg.contains("buenos")) return "Hola. Pasemos rápido los productos, por favor.";
                return "Disculpe, pero ¿podríamos apurarnos? Tengo que irme pronto.";
                
            case "angry":
            case "enojado":
                if (msg.contains("bolsa")) return "No quiero bolsa, solo quiero terminar con esto.";
                if (msg.contains("descuento") || msg.contains("promocion")) return "Espero que los descuentos se apliquen bien, no quiero cobros erróneos.";
                return "Qué servicio tan demorado. Solo cobre los productos y ya.";
                
            case "confused":
            case "confundido":
                if (msg.contains("pago") || msg.contains("efectivo") || msg.contains("tarjeta")) return "¿Eh? No estoy seguro. ¿Puedo pagar una parte en efectivo y otra con tarjeta?";
                if (msg.contains("descuento")) return "Ah, pensé que este producto tenía descuento. ¿Me puede explicar cómo funciona?";
                return "Es que... es la primera vez que compro esto. ¿El precio que sale ahí es el correcto?";
                
            case "polite":
            case "amable":
            default:
                if (msg.contains("bolsa")) return "Sí, por favor, muchas gracias.";
                if (msg.contains("pago") || msg.contains("efectivo") || msg.contains("tarjeta")) return "Pagaré con efectivo, aquí tiene.";
                if (msg.contains("hola") || msg.contains("buenos")) return "¡Hola! Buenos días, ¿cómo está?";
                return "Claro, no hay problema. Tómese su tiempo.";
        }
    }
}
