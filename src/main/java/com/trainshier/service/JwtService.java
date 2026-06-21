package com.trainshier.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * @param JWT service
 */
@Service
public class JwtService {

    /**
     * @param secretKey secret key used to sign JWT
     */
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    /**
     * @param tokenExpiration token lifetime in milliseconds
     */
    @Value("${security.jwt.token-expiration}")
    private Long tokenExpiration;

    /**
     * @param token JWT token
     * @param resolver claim resolver
     * @return extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {

        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return resolver.apply(claims);
    }

    /**
     * @param token JWT token
     * @return username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * @param token JWT token
     * @return user id
     */
    public Long extractUserId(String token) {
        return extractClaim(token,
                claims -> claims.get("userId", Long.class));
    }

    /**
     * @param token JWT token
     * @return user role
     */
    public String extractRole(String token) {
        return extractClaim(token,
                claims -> claims.get("role", String.class));
    }

    /**
     * @param userId user identifier
     * @param username username
     * @param roleId role identifier
     * @return generated JWT
     */
    public String generateToken(
            Long userId,
            String username,
            String role) {

        return Jwts.builder()
                .claims(Map.of(
                        "userId", userId,
                        "role", role))
                .subject(username)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + tokenExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * @param token JWT token
     * @return true if token is valid
     */
    public boolean isTokenValid(String token) {

        try {

            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (JwtException e) {

            return false;

        }
    }

    /**
     * @param token old JWT token
     * @return refreshed JWT token
     */
    public String refreshToken(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return generateToken(
                claims.get("userId", Long.class),
                claims.getSubject(),
                claims.get("role", String.class)
        );
    }

    /**
     * @return signing key
     */
    private SecretKey getSigningKey() {

        byte[] keyBytes =
                secretKey.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
