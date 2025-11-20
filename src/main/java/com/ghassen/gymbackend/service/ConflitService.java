// src/main/java/com/ghassen/gymbackend/service/ConflitService.java
package com.ghassen.gymbackend.service;

import com.ghassen.gymbackend.dto.*;
import com.ghassen.gymbackend.entities.*;
import com.ghassen.gymbackend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConflitService {

    private final ConflitRepository conflitRepo;
    private final OffreTravailRepository offreRepo;
    private final UtilisateurRepository userRepo;
    private final FileStorageService fileStorageService;

    public ConflitResponseDto createConflit(ConflitRequest req) {
        Utilisateur openedBy = userRepo.findById(req.getOpenedById())
                .orElseThrow(() -> new RuntimeException("User not found"));

        OffreTravail offre = offreRepo.findById(req.getOffreTravailId())
                .orElseThrow(() -> new RuntimeException("Offre not found"));

        Conflit conflit = new Conflit();
        conflit.setDescription(req.getDescription());
        conflit.setOpenedBy(openedBy);
        conflit.setOffreTravail(offre);
        conflit.setStatus(ConflitStatus.PENDING);

        attachMedia(req.getFiles(), req.getFileDescriptions(), openedBy, conflit, null);

        conflitRepo.save(conflit);
        return mapToDto(conflit);
    }

    // src/main/java/com/ghassen/gymbackend/service/ConflitService.java

    public ConflitResponseDto replyToConflit(ConflitResponseRequest req) {
        Utilisateur repliedBy = userRepo.findById(req.getRepliedById())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Conflit conflit = conflitRepo.findById(req.getConflitId())
                .orElseThrow(() -> new RuntimeException("Conflit non trouvé"));

        ConflitResponse response = new ConflitResponse();
        response.setMessage(req.getMessage());
        response.setRepliedBy(repliedBy);
        response.setConflit(conflit);
        response.setRepliedAt(LocalDateTime.now()); // ← AJOUTÉ

        // Attacher les médias (images/vidéos)
        attachMedia(req.getFiles(), req.getFileDescriptions(), repliedBy, null, response);

        // Ajouter la réponse au conflit
        conflit.getResponses().add(response);

        // SAUVEGARDER OBLIGATOIREMENT !
        conflitRepo.save(conflit);

        return mapToDto(conflit); // Retourne le conflit mis à jour avec la nouvelle réponse
    }

    private void attachMedia(List<MultipartFile> files, List<String> descriptions,
                             Utilisateur uploader, Conflit conflit, ConflitResponse response) {

        if (files == null || files.isEmpty()) return;

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            String desc = descriptions != null && i < descriptions.size() ? descriptions.get(i) : null;

            String filePath = fileStorageService.storeFile(file);

            ConflitMedia media = new ConflitMedia();
            media.setFilePath(filePath);
            media.setType(file.getContentType() != null && file.getContentType().startsWith("video/") ? MediaType.VIDEO : MediaType.IMAGE);
            media.setDescription(desc);
            media.setUploadedBy(uploader);

            if (conflit != null) {
                media.setConflit(conflit);
                conflit.getMedias().add(media);
            } else if (response != null) {
                media.setConflitResponse(response);
                response.getMedias().add(media);
            }
        }
    }

    public ConflitResponseDto getConflit(Long id) {
        Conflit c = conflitRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Conflit not found"));
        return mapToDto(c);
    }

    private ConflitResponseDto mapToDto(Conflit c) {
        ConflitResponseDto dto = new ConflitResponseDto();
        dto.setId(c.getId());
        dto.setDescription(c.getDescription());
        dto.setDateCreation(c.getDateCreation());
        dto.setStatus(c.getStatus());
        dto.setOpenedByName(c.getOpenedBy().getNom() + " " + c.getOpenedBy().getPrenom());
        dto.setOffreTitre(c.getOffreTravail().getTitre());

        dto.setMedias(c.getMedias().stream().map(this::toMediaDto).toList());

        dto.setResponses(c.getResponses().stream().map(r -> {
            var rd = new ConflitResponseDto.ResponseDto();
            rd.setMessage(r.getMessage());
            rd.setRepliedByName(r.getRepliedBy().getNom());
            rd.setRepliedAt(r.getRepliedAt());
            rd.setMedias(r.getMedias().stream().map(this::toMediaDto).toList());
            return rd;
        }).toList());

        return dto;
    }

    private ConflitResponseDto.MediaDto toMediaDto(ConflitMedia m) {
        var md = new ConflitResponseDto.MediaDto();
        md.setFilePath(m.getFilePath());
        md.setType(m.getType());
        md.setDescription(m.getDescription());
        md.setUploadedByName(m.getUploadedBy().getNom());
        md.setUploadedAt(m.getUploadedAt());
        return md;
    }
    // Add these methods to your ConflitService

    public List<ConflitResponseDto> getAllConflits() {
        return conflitRepo.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ConflitResponseDto> getConflitsByOffre(Long offreId) {
        return conflitRepo.findByOffreTravailId(offreId).stream()
                .map(this::mapToDto)
                .toList();
    }
    // Add this method to your ConflitService.java
    public ConflitResponseDto changeStatus(Long conflitId, ConflitStatus newStatus) {
        Conflit conflit = conflitRepo.findById(conflitId)
                .orElseThrow(() -> new RuntimeException("Conflit non trouvé"));

        if (conflit.getStatus() == newStatus) {
            throw new RuntimeException("Le statut est déjà " + newStatus);
        }

        conflit.setStatus(newStatus);

        if (newStatus == ConflitStatus.RESOLVED || newStatus == ConflitStatus.REJECTED) {
            conflit.setDateResolution(LocalDateTime.now());
        }

        conflitRepo.save(conflit);
        return mapToDto(conflit);
    }

}