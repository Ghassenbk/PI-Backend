// src/main/java/com/ghassen/gymbackend/controller/OffreTravailController.java
package com.ghassen.gymbackend.controllers;

import com.ghassen.gymbackend.dto.OffreTravailCreateDTO;
import com.ghassen.gymbackend.dto.OffreTravailUpdateDTO;
import com.ghassen.gymbackend.entities.Candidature;
import com.ghassen.gymbackend.entities.OffreTravail;
import com.ghassen.gymbackend.service.OffreTravailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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




}