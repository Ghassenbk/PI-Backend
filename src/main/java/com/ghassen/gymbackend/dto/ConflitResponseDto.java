// src/main/java/com/ghassen/gymbackend/dto/ConflitResponseDto.java
package com.ghassen.gymbackend.dto;

import com.ghassen.gymbackend.entities.ConflitStatus;
import com.ghassen.gymbackend.entities.MediaType;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ConflitResponseDto {
    private Long id;
    private String description;
    private LocalDateTime dateCreation;
    private ConflitStatus status;
    private String openedByName;
    private String offreTitre;

    private List<MediaDto> medias;              // Media from main conflict
    private List<ResponseDto> responses;        // All replies

    @Data
    public static class MediaDto {
        private String filePath;                // e.g. /uploads/conflits/abc.jpg
        private MediaType type;
        private String description;
        private String uploadedByName;
        private LocalDateTime uploadedAt;
    }

    @Data
    public static class ResponseDto {
        private String message;
        private String repliedByName;
        private LocalDateTime repliedAt;
        private List<MediaDto> medias;          // Media attached to this reply
    }
}