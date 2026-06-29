package com.trainshier.service;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private static class Attempt {
        int count;
        LocalDateTime blockedUntil;
    }

    private final ConcurrentHashMap<String, Attempt> attempts = new ConcurrentHashMap<>();

    public void checkBlocked(String key) {
        Attempt att = attempts.get(key);
        if (att != null && att.blockedUntil != null) {
            if (LocalDateTime.now().isBefore(att.blockedUntil)) {
                long secondsLeft = java.time.Duration.between(LocalDateTime.now(), att.blockedUntil).getSeconds();
                long minutes = secondsLeft / 60;
                long seconds = secondsLeft % 60;
                throw new RuntimeException(String.format(
                        "Demasiados intentos fallidos. Tu acceso está bloqueado temporalmente. Por favor, intenta de nuevo en %d minutos y %d segundos.",
                        minutes, seconds
                ));
            } else {
                // Block expired, reset count
                att.blockedUntil = null;
                att.count = 0;
            }
        }
    }

    public void recordFailedAttempt(String key) {
        attempts.compute(key, (k, att) -> {
            if (att == null) {
                att = new Attempt();
            }
            att.count++;
            if (att.count >= 5) {
                att.blockedUntil = LocalDateTime.now().plusMinutes(3);
            }
            return att;
        });
    }

    public void resetAttempts(String key) {
        attempts.remove(key);
    }
}
