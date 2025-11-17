package com.ghassen.gymbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @Email
    @NotBlank(message = "Email obligatoire")
    private String email;

    @NotBlank(message = "Mot de passe obligatoire")
    private String motDePasse;

    @NotBlank(message = "Téléphone obligatoire")
    private String telephone;

    private String description;
}
