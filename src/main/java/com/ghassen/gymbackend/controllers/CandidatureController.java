// src/main/java/com/ghassen/gymbackend/controller/CandidatureController.java
package com.ghassen.gymbackend.controllers;

import com.ghassen.gymbackend.dto.CandidatureDTO;
import com.ghassen.gymbackend.service.CandidatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/candidatures")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CandidatureController {

    private final CandidatureService candidatureService;

    @PostMapping("/postuler")
    public ResponseEntity<?> postuler(@ModelAttribute CandidatureDTO dto) {
        try {
            String message = candidatureService.postuler(dto);
            // Return JSON instead of plain text
            return ResponseEntity.ok(Map.of("message", message));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }
}