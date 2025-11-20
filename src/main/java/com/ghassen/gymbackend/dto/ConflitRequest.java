// src/main/java/com/ghassen/gymbackend/dto/ConflitRequest.java
package com.ghassen.gymbackend.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
public class ConflitRequest {
    private String description;
    private Long offreTravailId;
    private Long openedById;                    // User ID who opens conflict
    private List<MultipartFile> files;
    private List<String> fileDescriptions;      // optional captions
}