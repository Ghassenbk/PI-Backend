// src/main/java/com/ghassen/gymbackend/dto/OffreTravailUpdateDTO.java
package com.ghassen.gymbackend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OffreTravailUpdateDTO {

    @NotBlank
    private String titre;

    @NotBlank
    private String description;

    @NotNull
    @PositiveOrZero
    private BigDecimal prix;

    @NotNull
    @DecimalMin("-90.0") @DecimalMax("90.0")
    private Float localisationX;

    @NotNull
    @DecimalMin("-180.0") @DecimalMax("180.0")
    private Float localisationY;

    @NotNull
    private Long categorieId;

    @NotNull
    @FutureOrPresent
    private LocalDate datePrevue;

    private MultipartFile imgEmployer; // optional
    private MultipartFile imgPreuve;   // optional
}