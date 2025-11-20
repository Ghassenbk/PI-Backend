// src/main/java/com/ghassen/gymbackend/controllers/ConflitController.java
package com.ghassen.gymbackend.controllers;

import com.ghassen.gymbackend.dto.*;
import com.ghassen.gymbackend.entities.ConflitStatus;
import com.ghassen.gymbackend.service.ConflitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/conflits")
@RequiredArgsConstructor
public class ConflitController {

    private final ConflitService conflitService;

    @PostMapping
    public ResponseEntity<ConflitResponseDto> create(@ModelAttribute ConflitRequest req) {
        return ResponseEntity.ok(conflitService.createConflit(req));
    }

    @PostMapping("/{conflitId}/reply")
    public ResponseEntity<ConflitResponseDto> reply(
            @PathVariable Long conflitId,
            @ModelAttribute ConflitResponseRequest req) {
        req.setConflitId(conflitId);
        return ResponseEntity.ok(conflitService.replyToConflit(req));
    }

    // ONLY ONE OF THESE → I recommend this one (clean name)
    @GetMapping("/{id}")
    public ResponseEntity<ConflitResponseDto> getConflit(@PathVariable Long id) {
        return ResponseEntity.ok(conflitService.getConflit(id));
    }

    @GetMapping
    public ResponseEntity<List<ConflitResponseDto>> getAll() {
        return ResponseEntity.ok(conflitService.getAllConflits());
    }

    @GetMapping("/offre/{offreId}")
    public ResponseEntity<List<ConflitResponseDto>> getByOffre(@PathVariable Long offreId) {
        return ResponseEntity.ok(conflitService.getConflitsByOffre(offreId));
    }
    // Add this to your controller
    @PatchMapping("/{id}/status")
    public ResponseEntity<ConflitResponseDto> changeStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        ConflitStatus status = ConflitStatus.valueOf(body.get("status"));
        return ResponseEntity.ok(conflitService.changeStatus(id, status));
    }
}