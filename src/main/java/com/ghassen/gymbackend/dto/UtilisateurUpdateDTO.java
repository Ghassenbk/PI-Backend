package com.ghassen.gymbackend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UtilisateurUpdateDTO {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    private String description;

    private String cvPath;

    private String imgPath;

    // tu peux ajouter d'autres champs modifiables comme le mot de passe si besoin
}
