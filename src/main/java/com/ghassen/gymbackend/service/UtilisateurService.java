package com.ghassen.gymbackend.service;

import com.ghassen.gymbackend.dto.UtilisateurDTO;
import com.ghassen.gymbackend.entities.Utilisateur;
import com.ghassen.gymbackend.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    // by id
    public UtilisateurDTO getUtilisateurById(Long id) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return toDTO(u);
    }

    // maj utilisateur
    public UtilisateurDTO updateUtilisateur(Long id, UtilisateurDTO dto) {
        Utilisateur u = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        u.setNom(dto.getNom());
        u.setPrenom(dto.getPrenom());
        u.setEmail(dto.getEmail());
        u.setDescription(dto.getDescription());
        u.setCvPath(dto.getCvPath());
        u.setImgPath(dto.getImgPath());

        utilisateurRepository.save(u);

        return toDTO(u);
    }

    // Mapper entity -> DTO
    private UtilisateurDTO toDTO(Utilisateur u) {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setId(u.getId());
        dto.setNom(u.getNom());
        dto.setPrenom(u.getPrenom());
        dto.setEmail(u.getEmail());
        dto.setDescription(u.getDescription());
        dto.setCvPath(u.getCvPath());
        dto.setImgPath(u.getImgPath());
        return dto;
    }
}
