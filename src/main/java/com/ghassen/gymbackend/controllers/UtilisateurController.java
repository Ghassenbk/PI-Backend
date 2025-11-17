package com.ghassen.gymbackend.controllers;

import com.ghassen.gymbackend.dto.UtilisateurDTO;
import com.ghassen.gymbackend.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // pour ton Angular
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

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

}