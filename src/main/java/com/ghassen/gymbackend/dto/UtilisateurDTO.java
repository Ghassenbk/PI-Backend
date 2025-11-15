package com.ghassen.gymbackend.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UtilisateurDTO {
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotNull
    @Email(message = "Email invalide")
    private String email;

    private String description;

    private MultipartFile cvFile;
    private MultipartFile imgFile;

    private String cvPath;
    private String imgPath;

    private List<String> roles;
    private Long abonnementId;
}
