// src/main/java/com/ghassen/gymbackend/service/FileStorageService.java
package com.ghassen.gymbackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
}