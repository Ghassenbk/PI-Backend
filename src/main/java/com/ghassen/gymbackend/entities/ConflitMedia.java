// src/main/java/com/ghassen/gymbackend/entities/ConflitMedia.java
package com.ghassen.gymbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "conflit_media")
public class ConflitMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType type;

    private String description;

    @ManyToOne
    @JoinColumn(name = "uploaded_by_id", nullable = false)
    private Utilisateur uploadedBy;

    @Column(nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conflit_id", nullable = true)
    private Conflit conflit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conflit_response_id", nullable = true)
    private ConflitResponse conflitResponse;

    @PrePersist
    @PreUpdate
    private void validateOwnership() {
        if ((conflit == null && conflitResponse == null) || (conflit != null && conflitResponse != null)) {
            throw new IllegalStateException("ConflitMedia must have exactly one owner: either Conflit or ConflitResponse");
        }
    }
}