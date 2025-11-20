package com.ghassen.gymbackend.controllers;

import com.ghassen.gymbackend.dto.UtilisateurDTO;
import com.ghassen.gymbackend.service.FileStorageService;
import com.ghassen.gymbackend.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // pour ton Angular
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    private final FileStorageService fileStorageService;

    @GetMapping("/{id}")
    public UtilisateurDTO getUtilisateur(@PathVariable Long id) {
        return utilisateurService.getUtilisateurById(id);
    }

    @PutMapping("/{id}")
    public UtilisateurDTO updateUtilisateur(@PathVariable Long id,
                                            @RequestBody UtilisateurDTO dto) {
        return utilisateurService.updateUtilisateur(id, dto);
    }


    @GetMapping("/all")
    public List<UtilisateurDTO> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }

    @PostMapping("/{id}/upload-profile-image")
    public ResponseEntity<String> uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Fichier vide");
            }

            String filePath = fileStorageService.storeProfileImage(file, id);
            utilisateurService.updateProfileImage(id, filePath);

            return ResponseEntity.ok(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur upload : " + e.getMessage());
        }
    }

    @PostMapping("/{id}/upload-cv")
    public ResponseEntity<String> uploadCV(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Fichier vide");
            }

            if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
                return ResponseEntity.badRequest().body("Seul les PDF sont acceptés");
            }

            String filePath = fileStorageService.storeCV(file, id);
            utilisateurService.updateCV(id, filePath);

            return ResponseEntity.ok(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur upload CV : " + e.getMessage());
        }
    }

}