// src/main/java/com/ghassen/gymbackend/entities/ConflitResponse.java
package com.ghassen.gymbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "conflit_responses")
public class ConflitResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @ManyToOne
    @JoinColumn(name = "conflit_id", nullable = false)
    private Conflit conflit;

    @ManyToOne
    @JoinColumn(name = "replied_by_id", nullable = false)
    private Utilisateur repliedBy;

    private LocalDateTime repliedAt = LocalDateTime.now();

    // NEW: Media attached to this response
    // In ConflitResponse.java — ADD THIS
    @OneToMany(mappedBy = "conflitResponse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConflitMedia> medias = new ArrayList<>();
}