// src/main/java/com/ghassen/gymbackend/entities/Conflit.java
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
@Table(name = "conflits")
public class Conflit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConflitStatus status = ConflitStatus.PENDING;

    // Who opened the conflict
    @ManyToOne
    @JoinColumn(name = "opened_by_id", nullable = false)
    private Utilisateur openedBy;

    // The job offer this conflict is about
    @ManyToOne
    @JoinColumn(name = "offre_travail_id", nullable = false)
    private OffreTravail offreTravail;



    private LocalDateTime dateResolution;

    // Media proof attached to the conflict (images/videos)
    // In your Conflit.java — ADD THIS
    @OneToMany(mappedBy = "conflit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConflitMedia> medias = new ArrayList<>();


    // Responses from the other party (optional back-and-forth)
    @OneToMany(mappedBy = "conflit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConflitResponse> responses = new ArrayList<>();
}