// src/main/java/com/ghassen/gymbackend/dto/OffreTravailCreateDTO.java
package com.ghassen.gymbackend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class OffreTravailCreateDTO {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull(message = "Le prix est obligatoire")
    @PositiveOrZero
    private BigDecimal prix;


    @NotNull(message = "Latitude (X) est obligatoire")
    private Float localisationX;

    @NotNull(message = "Longitude (Y) est obligatoire")
    private Float localisationY;

    @NotNull
    private Long categorieId;

    @NotNull
    @FutureOrPresent
    private LocalDate datePrevue;

    @NotNull(message = "L'ID du posteur est obligatoire")
    private Long posterId;

    private MultipartFile imgEmployer;
    private MultipartFile imgPreuve;
}