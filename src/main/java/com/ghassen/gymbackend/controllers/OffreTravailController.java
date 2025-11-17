// src/main/java/com/ghassen/gymbackend/controller/OffreTravailController.java
package com.ghassen.gymbackend.controllers;

import com.ghassen.gymbackend.dto.OffreTravailCreateDTO;
import com.ghassen.gymbackend.dto.OffreTravailUpdateDTO;
import com.ghassen.gymbackend.entities.Candidature;
import com.ghassen.gymbackend.entities.OffreTravail;
import com.ghassen.gymbackend.entities.StatusCandidature;
import com.ghassen.gymbackend.entities.Utilisateur;
import com.ghassen.gymbackend.service.OffreTravailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/offres")
@RequiredArgsConstructor
public class OffreTravailController {

    private final OffreTravailService offreService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<OffreTravail> createOffre(@Valid @ModelAttribute OffreTravailCreateDTO dto) {

        OffreTravail saved = offreService.createOffre(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping("/{offreId}/employer/{userId}")
    public ResponseEntity<Void> deleteOffre(
            @PathVariable Long offreId,
            @PathVariable Long userId) {

        offreService.deleteOffre(offreId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employer/{userId}/all")
    public ResponseEntity<List<OffreTravail>> getAllMyOffers(@PathVariable Long userId) {
        List<OffreTravail> offers = offreService.getAllMyOffers(userId);
        return ResponseEntity.ok(offers);
    }

    @GetMapping("/employer/{userId}")
    public ResponseEntity<List<OffreTravail>> getMyOffers(@PathVariable Long userId) {
        List<OffreTravail> offers = offreService.getMyAvailableOffers(userId);
        return ResponseEntity.ok(offers);
    }


    @GetMapping("/disponibles")
    public ResponseEntity<List<OffreTravail>> getAvailableOffers() {
        List<OffreTravail> offers = offreService.getAvailableOffers();
        return ResponseEntity.ok(offers);
    }




    @PutMapping("/{offreId}/employer/{userId}")
    public ResponseEntity<OffreTravail> updateOffre(
            @PathVariable Long offreId,
            @PathVariable Long userId,
            @Valid @ModelAttribute OffreTravailUpdateDTO dto) {

        OffreTravail updated = offreService.updateOffre(offreId, dto, userId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{offreId}/employer/{employerId}/candidatures")
    public ResponseEntity<List<Candidature>> getApplicants(
            @PathVariable Long offreId,
            @PathVariable Long employerId) {

        List<Candidature> applicants = offreService.getApplicantsForMyOffer(offreId, employerId);
        return ResponseEntity.ok(applicants);
    }

    @PutMapping("/{offreId}/cancel/{userId}")
    public ResponseEntity<OffreTravail> cancelOffre(
            @PathVariable Long offreId,
            @PathVariable Long userId) {

        OffreTravail updated = offreService.cancelOffer(offreId, userId);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{offreId}/assign/{candidatureId}/employer/{employerId}")
    public ResponseEntity<OffreTravail> assignCandidature(
            @PathVariable Long offreId,
            @PathVariable Long candidatureId,
            @PathVariable Long employerId) {

        OffreTravail updatedOffre = offreService.assignCandidature(offreId, candidatureId, employerId);
        return ResponseEntity.ok(updatedOffre);
    }

    @GetMapping("/all")
    public List<OffreTravail> getAllOffers() {
        return offreService.getAllOffers();
    }





    @GetMapping("/{offreId}/employer/{employerId}/assigned-worker")
    public ResponseEntity<?> getAssignedWorker(
            @PathVariable Long offreId,
            @PathVariable Long employerId) {

        try {
            OffreTravail offre = offreService.getOffreByIdAndEmployer(offreId, employerId);

            // Find the accepted candidature (status = ACCEPTEE)
            Candidature accepted = offre.getCandidatures().stream()
                    .filter(c -> c.getStatus() == StatusCandidature.ACCEPTEE)
                    .findFirst()
                    .orElse(null);

            if (accepted == null) {
                return ResponseEntity.noContent().build(); // 204 → no worker assigned
            }

            // Force load utilisateur (to avoid lazy loading)
            Hibernate.initialize(accepted.getUtilisateur());
            Utilisateur worker = accepted.getUtilisateur();

            var response = new LinkedHashMap<String, Object>();

            response.put("candidatureId", accepted.getId());
            response.put("prixFinal", offre.getPrix());
            response.put("prixPropose", accepted.getPrixPropose());
            response.put("message", accepted.getMessage());
            response.put("dateCandidature", accepted.getDateCandidature());

            // PROOF IMAGE IS HERE → directly from OffreTravail
            response.put("proofImage", offre.getImgPathPreuve());

            response.put("worker", Map.of(
                    "id", worker.getId(),
                    "name", worker.getNom() + " " + worker.getPrenom(),
                    "email", worker.getEmail(),
                    "telephone", worker.getTelephone() != null ? worker.getTelephone() : "",
                    "photo", worker.getImgPath() != null ? worker.getImgPath() : ""
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", "Offre not found or access denied"));
        }
    }
}