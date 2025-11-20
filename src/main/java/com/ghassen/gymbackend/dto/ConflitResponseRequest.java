// src/main/java/com/ghassen/gymbackend/dto/ConflitResponseRequest.java
package com.ghassen.gymbackend.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
public class ConflitResponseRequest {
    private Long conflitId;
    private String message;
    private Long repliedById;                   // User ID who replies
    private List<MultipartFile> files;
    private List<String> fileDescriptions;
}