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
    public UtilisateurDTO toDTO(Utilisateur user) {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setId(user.getId());
        dto.setNom(user.getNom());
        dto.setPrenom(user.getPrenom());
        dto.setEmail(user.getEmail());
        dto.setDescription(user.getDescription());
        dto.setCvPath(user.getCvPath());
        dto.setImgPath(user.getImgPath());
        if(user.getAbonnement() != null) {
            dto.setAbonnementId(user.getAbonnement().getId());
        }
        return dto;
    }

}
