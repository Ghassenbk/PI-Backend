// src/main/java/com/ghassen/gymbackend/entities/Candidature.java
package com.ghassen.gymbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "candidature")
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_candidature")
    private Long id;

    private BigDecimal prixPropose;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_candidature")
    private Date dateCandidature;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusCandidature status = StatusCandidature.EN_ATTENTE;

    // RELATIONSHIPS
    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

    @JsonIgnore  // PREVENT INFINITE LOOP
    @ManyToOne
    @JoinColumn(name = "id_offre")
    private OffreTravail offreTravail;
}