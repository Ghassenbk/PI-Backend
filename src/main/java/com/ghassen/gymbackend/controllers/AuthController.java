// src/main/java/com/ghassen/gymbackend/controller/AuthController.java
package com.ghassen.gymbackend.controllers;

import com.ghassen.gymbackend.dto.LoginRequest;
import com.ghassen.gymbackend.dto.UtilisateurDTO;
import com.ghassen.gymbackend.entities.Utilisateur;
import com.ghassen.gymbackend.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;

    @PostMapping("/login")
    public ResponseEntity<UtilisateurDTO> login(@RequestBody LoginRequest request) {

        Utilisateur user = utilisateurRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect"));

        // Plain text comparison (no encryption at all)
        if (!user.getMotDePasse().equals(request.motDePasse())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Mot de passe incorrect");
        }

        // Convert to DTO
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setId(user.getId());
        dto.setNom(user.getNom());
        dto.setPrenom(user.getPrenom());
        dto.setEmail(user.getEmail());
        dto.setDescription(user.getDescription());
        dto.setCvPath(user.getCvPath());
        dto.setImgPath(user.getImgPath());
        dto.setRoles(user.getRoles() != null
                ? user.getRoles().stream().map(Enum::name).toList()
                : null);
        dto.setAbonnementId(user.getAbonnement() != null ? user.getAbonnement().getId() : null);

        return ResponseEntity.ok(dto);
    }
}