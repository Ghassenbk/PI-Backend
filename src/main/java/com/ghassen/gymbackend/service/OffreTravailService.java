// src/main/java/com/ghassen/gymbackend/service/OffreTravailService.java
package com.ghassen.gymbackend.service;

import com.ghassen.gymbackend.dto.OffreTravailCreateDTO;
import com.ghassen.gymbackend.dto.OffreTravailUpdateDTO;
import com.ghassen.gymbackend.entities.*;
import com.ghassen.gymbackend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OffreTravailService {

    private final OffreTravailRepository offreRepo;
    private final CategorieRepository categorieRepo;
    private final UtilisateurRepository userRepo;


    private final CandidatureRepository candidatureRepo;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.upload.allowed-types}")
    private String allowedTypes;

    @Transactional
    public OffreTravail createOffre(OffreTravailCreateDTO dto) {

        // nlawjou ali posta
        Utilisateur employer = userRepo.findById(dto.getPosterId())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable : " + dto.getPosterId()));


        if (!employer.getRoles().contains(Role.EMPLOYER) && !employer.getRoles().contains(Role.ADMIN)) {
            throw new IllegalArgumentException("Seuls les EMPLOYER ou ADMIN peuvent poster une offre.");
        }

        // nlawjou al categorie
        Categorie categorie = categorieRepo.findById(dto.getCategorieId())
                .orElseThrow(() -> new IllegalArgumentException("Catégorie introuvable : " + dto.getCategorieId()));


        OffreTravail offre = new OffreTravail();
        offre.setTitre(dto.getTitre());
        offre.setDescription(dto.getDescription());
        offre.setPrix(dto.getPrix());
        offre.setLocalisationX(dto.getLocalisationX());
        offre.setLocalisationY(dto.getLocalisationY());
        offre.setCategorie(categorie);
        offre.setDatePrevue(dto.getDatePrevue());
        offre.setStatus(Status.DISPONIBLE);
        offre.setEmployer(employer);


        if (dto.getImgEmployer() != null && !dto.getImgEmployer().isEmpty()) {
            validateFile(dto.getImgEmployer());
            offre.setImgPathEmployer(saveFile(dto.getImgEmployer()));
        }
        if (dto.getImgPreuve() != null && !dto.getImgPreuve().isEmpty()) {
            validateFile(dto.getImgPreuve());
            offre.setImgPathPreuve(saveFile(dto.getImgPreuve()));
        }

        return offreRepo.save(offre);
    }



    public List<Candidature> getApplicantsForMyOffer(Long offreId, Long employerId) {

        OffreTravail offre = offreRepo.findById(offreId)
                .orElseThrow(() -> new IllegalArgumentException("Offre introuvable : " + offreId));

        Utilisateur employer = userRepo.findById(employerId)
                .orElseThrow(() -> new IllegalArgumentException("Employeur introuvable : " + employerId));

        boolean isOwner = offre.getEmployer().getId().equals(employerId);
        boolean isAdmin = employer.getRoles().contains("ADMIN");
        if (!isOwner && !isAdmin) {
            throw new SecurityException("Accès refusé : vous ne pouvez voir que les candidatures de vos offres.");
        }


        return candidatureRepo.findByOffreTravailId(offreId);
    }

    @Transactional
    public OffreTravail updateStatus(Long offreId, Long userId, Status newStatus) {

        OffreTravail offre = offreRepo.findById(offreId)
                .orElseThrow(() -> new IllegalArgumentException("Offre introuvable : " + offreId));

        Utilisateur user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable : " + userId));

        boolean isOwner = offre.getEmployer().getId().equals(userId);
        boolean isAdmin = user.getRoles().contains("ADMIN");
        if (!isOwner && !isAdmin) {
            throw new SecurityException("Accès refusé : vous ne pouvez modifier que vos propres offres.");
        }

        offre.setStatus(newStatus);
        return offreRepo.save(offre);
    }
    @Transactional
    public OffreTravail cancelOffer(Long offreId, Long userId) {
        return updateStatus(offreId, userId, Status.ANNULEE);
    }


    @Transactional
    public OffreTravail updateOffre(Long offreId, OffreTravailUpdateDTO dto, Long userId) {

        OffreTravail offre = offreRepo.findById(offreId)
                .orElseThrow(() -> new IllegalArgumentException("Offre introuvable : " + offreId));


        Utilisateur user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable : " + userId));

        boolean isOwner = offre.getEmployer().getId().equals(userId);
        boolean isAdmin = user.getRoles().contains("ADMIN");
        if (!isOwner && !isAdmin) {
            throw new SecurityException("Accès refusé : vous ne pouvez modifier que vos propres offres.");
        }


        offre.setTitre(dto.getTitre());
        offre.setDescription(dto.getDescription());
        offre.setPrix(dto.getPrix());
        offre.setLocalisationX(dto.getLocalisationX());
        offre.setLocalisationY(dto.getLocalisationY());
        offre.setDatePrevue(dto.getDatePrevue());


        Categorie cat = categorieRepo.findById(dto.getCategorieId())
                .orElseThrow(() -> new IllegalArgumentException("Catégorie introuvable : " + dto.getCategorieId()));
        offre.setCategorie(cat);


        if (dto.getImgEmployer() != null && !dto.getImgEmployer().isEmpty()) {
            deleteFileIfExists(offre.getImgPathEmployer());
            offre.setImgPathEmployer(saveFile(dto.getImgEmployer()));
        }
        if (dto.getImgPreuve() != null && !dto.getImgPreuve().isEmpty()) {
            deleteFileIfExists(offre.getImgPathPreuve());
            offre.setImgPathPreuve(saveFile(dto.getImgPreuve()));
        }


        return offreRepo.save(offre);
    }

    @Transactional(readOnly = true)
    public List<OffreTravail> getAllMyOffers(Long userId) {
        return offreRepo.findByEmployerId(userId);
    }



    @Transactional
    public void deleteOffre(Long offreId, Long userId) {

        OffreTravail offre = offreRepo.findById(offreId)
                .orElseThrow(() -> new IllegalArgumentException("Offre introuvable : " + offreId));


        Utilisateur user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable : " + userId));


        boolean isOwner = offre.getEmployer().getId().equals(userId);
        boolean isAdmin = user.getRoles().contains("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new SecurityException("Accès refusé : vous ne pouvez pas supprimer cette offre.");
        }


        deleteFileIfExists(offre.getImgPathEmployer());
        deleteFileIfExists(offre.getImgPathPreuve());


        offreRepo.delete(offre);
    }



    public List<OffreTravail> getAvailableOffers() {
        return offreRepo.findByStatus(Status.DISPONIBLE);
    }


    public List<OffreTravail> getMyAvailableOffers(Long employerId) {
        System.out.println("Fetching offers for employer ID: " + employerId);
        List<OffreTravail> offers = offreRepo.findByEmployer_IdAndStatus(employerId, Status.DISPONIBLE);
        System.out.println("Found " + offers.size() + " offers");
        return offers;
    }






    // ────────────────────── FILE HELPERS ──────────────────────
    private void deleteFileIfExists(String filePath) {
        if (filePath != null && !filePath.isBlank()) {
            try {
                Path path = Paths.get(filePath);
                if (Files.exists(path)) {
                    Files.delete(path);
                }
            } catch (Exception e) {
                // Log warning but don't fail deletion
                System.err.println("Failed to delete file: " + filePath + " | " + e.getMessage());
            }
        }
    }

    private String saveFile(MultipartFile file) {
        try {
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);

            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase()
                    : "dat";

            String fileName = UUID.randomUUID() + "." + ext;
            Path target = dir.resolve(fileName);
            file.transferTo(target.toFile());

            return uploadDir + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Échec de l'enregistrement du fichier : " + e.getMessage(), e);
        }
    }
    // ────────────────────── PRIVATE HELPER METHODS ──────────────────────

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) return;

        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType)) {
            throw new IllegalArgumentException("Type de fichier non autorisé : " + contentType +
                    ". Autorisé : " + allowedTypes);
        }
    }
    @Transactional
    public OffreTravail assignCandidature(Long offreId, Long candidatureId, Long employerId) {

        OffreTravail offre = offreRepo.findById(offreId)
                .orElseThrow(() -> new IllegalArgumentException("Offre non trouvée"));

        if (!offre.getEmployer().getId().equals(employerId)) {
            throw new SecurityException("Accès refusé");
        }

        if (offre.getStatus() != Status.DISPONIBLE) {
            throw new IllegalStateException("Offre non disponible");
        }

        Candidature selected = candidatureRepo.findById(candidatureId)
                .orElseThrow(() -> new IllegalArgumentException("Candidature non trouvée"));

        if (!selected.getOffreTravail().getId().equals(offreId)) {
            throw new IllegalArgumentException("Candidature invalide");
        }

        // 1. Update offer
        offre.setStatus(Status.ATTRIBUE);
        offre.setPrix(selected.getPrixPropose());

        // 2. Accept selected candidature
        selected.setStatus(StatusCandidature.ACCEPTEE);

        // 3. Delete all others → NOW WORKS!
        candidatureRepo.deleteAllExceptSelected(offreId, candidatureId);

        return offreRepo.save(offre);
    }



}