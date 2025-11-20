package com.ghassen.gymbackend.service;

import com.ghassen.gymbackend.dto.RegisterRequest;
import com.ghassen.gymbackend.dto.UtilisateurDTO;
import com.ghassen.gymbackend.entities.Role;
import com.ghassen.gymbackend.entities.Utilisateur;
import com.ghassen.gymbackend.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<UtilisateurDTO> getAllUtilisateurs() {
        return utilisateurRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UtilisateurDTO register(RegisterRequest request) {

        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        Utilisateur u = new Utilisateur();
        u.setNom(request.getNom());
        u.setPrenom(request.getPrenom());
        u.setEmail(request.getEmail());
        u.setTelephone(request.getTelephone());
        u.setDescription(request.getDescription());

        u.setMotDePasse(request.getMotDePasse());

        u.setRoles(List.of(Role.EMPLOYER));

        utilisateurRepository.save(u);

        return toDTO(u);
    }
// src/main/java/com/ghassen/gymbackend/service/UtilisateurService.java

    public void updateProfileImage(Long id, String imgPath) {
        Utilisateur user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setImgPath(imgPath);
        utilisateurRepository.save(user);
    }

    public void updateCV(Long id, String cvPath) {
        Utilisateur user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setCvPath(cvPath);
        utilisateurRepository.save(user);
    }


}