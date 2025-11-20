// src/main/java/com/ghassen/gymbackend/entities/OffreTravail.java
package com.ghassen.gymbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "offretravail")
public class OffreTravail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_offretravail")
    private Long id;

    private String titre;
    private String description;
    private BigDecimal prix;

    // REMOVED: localite, localisation
    // ADDED:
    @Column(name = "localisation_x", nullable = false)
    private float localisationX;   // latitude

    @Column(name = "localisation_y", nullable = false)
    private float localisationY;   // longitude

    @Enumerated(EnumType.STRING)
    private Status status;

    private String imgPathEmployer;
    private String imgPathPreuve;

    // CHANGED: LocalDate
    @Column(name = "date_prevue")
    private LocalDate datePrevue;

    @Column(name = "date_realisation")
    private LocalDate dateRealisation;

    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private Utilisateur employer;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @JsonIgnore
    @OneToMany(mappedBy = "offreTravail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Candidature> candidatures = new ArrayList<>();
    
    @OneToMany(mappedBy = "offreTravail", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // avoid infinite recursion in JSON
    private List<Conflit> conflits = new ArrayList<>();
}