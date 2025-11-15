package com.ghassen.gymbackend.controllers;

import com.ghassen.gymbackend.auth.TokenManager;
import com.ghassen.gymbackend.dto.UtilisateurDTO;
import com.ghassen.gymbackend.entities.Utilisateur;
import com.ghassen.gymbackend.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ghassen.gymbackend.repositories.UtilisateurRepository;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // pour ton Angular
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    private final UtilisateurRepository utilisateurRepository;
    private final TokenManager tokenManager;

    @GetMapping("/{id}")
    public UtilisateurDTO getUtilisateur(@PathVariable Long id) {
        return utilisateurService.getUtilisateurById(id);
    }

    @PutMapping("/{id}")
    public UtilisateurDTO updateUtilisateur(@PathVariable Long id,
                                            @RequestBody UtilisateurDTO dto) {
        return utilisateurService.updateUtilisateur(id, dto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String motDePasse = body.get("motDePasse");

        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email ou mot de passe incorrect");
        }

        Utilisateur user = userOpt.get();
        if (!user.getMotDePasse().equals(motDePasse)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email ou mot de passe incorrect");
        }

        String token = tokenManager.generateToken(user.getId());
        return ResponseEntity.ok(Map.of("token", token, "user", utilisateurService.toDTO(user)));


    }
}
