package com.ghassen.gymbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @Column(name = "first_name", length = 50, nullable = false)
    @NotNull
    private String nom;

    @Column(name = "last_name", length = 50)
    @NotBlank
    private String prenom;

    @Column(unique = true)
    @NotNull
    @Email
    private String email;

    @Column(name = "telephone", length = 20, unique = true)
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(
            regexp = "^(\\+216|216|00216)?[0-9]{8}$",
            message = "Format invalide. Utilisez : 99123456 ou +21699123456"
    )
    private String telephone;  // ← NEW FIELD ADDED

    @NotBlank
    @JsonIgnore
    private String motDePasse;

    private String description;
    private String cvPath;
    private String imgPath;

    @ManyToOne
    @JoinColumn(name = "id_abonnement")
    private Abonnement abonnement;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    // ... rest of your relationships (unchanged)
    @JsonIgnore
    @OneToMany(mappedBy = "expediteur", cascade = CascadeType.ALL)
    private List<Message> messagesEnvoyes;

    @JsonIgnore
    @OneToMany(mappedBy = "destinataire", cascade = CascadeType.ALL)
    private List<Message> messagesRecus;

    @JsonIgnore
    @OneToMany(mappedBy = "evaluateur", cascade = CascadeType.ALL)
    private List<Evaluation> evaluationsDonnees;

    @JsonIgnore
    @OneToMany(mappedBy = "evalue", cascade = CascadeType.ALL)
    private List<Evaluation> evaluationsRecues;

    @JsonIgnore
    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Candidature> candidatures;

    @JsonIgnore
    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OffreTravail> offresPostees = new ArrayList<>();
}