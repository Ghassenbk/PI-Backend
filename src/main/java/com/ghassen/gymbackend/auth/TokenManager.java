package com.ghassen.gymbackend.auth;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenManager {

    // Thread-safe map pour stocker token -> userId
    private final Map<String, Long> tokens = new ConcurrentHashMap<>();

    // Générer un token pour un userId
    public String generateToken(Long userId) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, userId);
        return token;
    }

    // Vérifier et récupérer userId
    public Long getUserId(String token) {
        return tokens.get(token);
    }

    // Supprimer un token
    public void revokeToken(String token) {
        tokens.remove(token);
    }

    // Vérifier si token existe
    public boolean isValid(String token) {
        return tokens.containsKey(token);
    }


}
