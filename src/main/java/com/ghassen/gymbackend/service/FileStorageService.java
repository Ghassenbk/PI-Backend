// src/main/java/com/ghassen/gymbackend/service/FileStorageService.java
package com.ghassen.gymbackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    // Chemin absolu où on stocke les fichiers
    @Value("${app.upload.dir:C:/uploads/conflits}")
    private String uploadDir;

    /**
     * Sauvegarde un fichier et retourne le chemin relatif à mettre en base de données
     */
    public String storeFile(MultipartFile file) {
        try {
            Path rootPath = Paths.get(uploadDir);
            if (!Files.exists(rootPath)) {
                Files.createDirectories(rootPath);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String fileName = UUID.randomUUID() + extension;

            Path targetPath = rootPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // Ce qui est sauvegardé en base de données
            return "uploads/conflits/" + fileName;   // OUI, exactement comme ça

        } catch (Exception e) {
            throw new RuntimeException("Échec de l'enregistrement du fichier : " + e.getMessage(), e);
        }
    }
// Ajoute ça dans ton FileStorageService.java

    public String storeProfileImage(MultipartFile file, Long userId) {
        try {
            Path root = Paths.get("C:/uploads/profiles");
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            String userFolder = root.resolve(userId.toString()).toString();
            if (!Files.exists(Paths.get(userFolder))) {
                Files.createDirectories(Paths.get(userFolder));
            }

            String ext = "";
            String original = file.getOriginalFilename();
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf("."));
            }

            String fileName = "profile_" + System.currentTimeMillis() + ext;
            Path target = Paths.get(userFolder).resolve(fileName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            // Retourne le chemin relatif pour la DB
            return "uploads/profiles/" + userId + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("Erreur upload photo : " + e.getMessage(), e);
        }
    }

    public String storeCV(MultipartFile file, Long userId) {
        try {
            Path root = Paths.get("C:/uploads/cvs");
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            String userFolder = root.resolve(userId.toString()).toString();
            if (!Files.exists(Paths.get(userFolder))) {
                Files.createDirectories(Paths.get(userFolder));
            }

            String fileName = "cv_" + userId + "_" + System.currentTimeMillis() + ".pdf";
            Path target = Paths.get(userFolder).resolve(fileName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            // Retourne le chemin relatif pour la DB
            return "uploads/cvs/" + userId + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("Erreur upload CV : " + e.getMessage(), e);
        }
    }

    private String storeFile(MultipartFile file, String subfolder) {
        try {
            Path root = Paths.get("C:/uploads").resolve(subfolder);
            Files.createDirectories(root);

            String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String fileName = "profile_" + System.currentTimeMillis() + "." + ext;
            if (subfolder.contains("cvs")) {
                fileName = "cv_" + System.currentTimeMillis() + ".pdf";
            }

            Path target = root.resolve(fileName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            return subfolder + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Erreur upload: " + e.getMessage());
        }
    }
}