// src/main/java/com/ghassen/gymbackend/dto/CandidatureDTO.java
package com.ghassen.gymbackend.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class CandidatureDTO {

    private Long offreId;                    // ID of the job offer
    private Long utilisateurId;              // ID of the applicant (from localStorage)

    private BigDecimal prixPropose;          // Proposed price
    private String message;                  // Cover letter / motivation
}