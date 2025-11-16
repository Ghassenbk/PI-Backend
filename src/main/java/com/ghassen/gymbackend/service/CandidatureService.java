// src/main/java/com/ghassen/gymbackend/service/CandidatureService.java
package com.ghassen.gymbackend.service;

import com.ghassen.gymbackend.dto.CandidatureDTO;
import com.ghassen.gymbackend.entities.*;
import com.ghassen.gymbackend.repositories.CandidatureRepository;
import com.ghassen.gymbackend.repositories.OffreTravailRepository;
import com.ghassen.gymbackend.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CandidatureService {

    private final CandidatureRepository candidatureRepository;
    private final OffreTravailRepository offreRepository;
    private final UtilisateurRepository utilisateurRepository;

    private static final String UPLOAD_DIR = "uploads/preuves/";

    public String postuler(CandidatureDTO dto) throws Exception {

        // 1. Fetch entities
        OffreTravail offre = offreRepository.findById(dto.getOffreId())
                .orElseThrow(() -> new RuntimeException("Offre non trouvée"));

        Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Prevent owner from applying
        if (offre.getEmployer().getId().equals(utilisateur.getId())) {
            throw new RuntimeException("Vous ne pouvez pas postuler à votre propre offre !");
        }

        // Prevent duplicate
        if (candidatureRepository.existsByUtilisateurAndOffreTravail(utilisateur, offre)) {
            throw new RuntimeException("Vous avez déjà postulé à cette offre !");
        }

        // Create and save candidature
        Candidature candidature = new Candidature();
        candidature.setPrixPropose(dto.getPrixPropose());
        candidature.setMessage(dto.getMessage());
        candidature.setDateCandidature(new Date());
        candidature.setStatus(StatusCandidature.EN_ATTENTE);
        candidature.setUtilisateur(utilisateur);
        candidature.setOffreTravail(offre);

        candidatureRepository.save(candidature);

        // BETTER SUCCESS MESSAGE
        return "Vous avez postulé avec succès à cette offre ! Attendez l'approbation de l'employeur.";
    }

}