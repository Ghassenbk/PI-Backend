package com.ghassen.gymbackend.service;

import com.ghassen.gymbackend.auth.TokenManager;
import com.ghassen.gymbackend.entities.Utilisateur;
import com.ghassen.gymbackend.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private TokenManager tokenManager;

    public String login(String email, String motDePasse) {
        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email invalide"));

        if (!user.getMotDePasse().equals(motDePasse)) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        return tokenManager.generateToken(user.getId());
    }

    public void logout(String token) {
        tokenManager.revokeToken(token);
    }

    public Long authenticate(String token) {
        if (!tokenManager.isValid(token)) {
            throw new RuntimeException("Token invalide");
        }
        return tokenManager.getUserId(token);
    }
}
