// src/main/java/com/ghassen/gymbackend/dto/LoginRequest.java
package com.ghassen.gymbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email(message = "Email invalide")
        @NotBlank(message = "Email obligatoire")
        String email,

        @NotBlank(message = "Mot de passe obligatoire")
        String motDePasse
) {}