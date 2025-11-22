package com.ghassen.gymbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluation")
    private Long id;

    private int note;
    private String commentaire;

    @Temporal(TemporalType.TIMESTAMP)
    private Date datePublication;

    // 🔁 L’évaluateur (celui qui évalue)
    @ManyToOne
    @JoinColumn(name = "evaluateur_id")
    private Utilisateur evaluateur;

    // 🔁 L’évalué (celui qui reçoit l’évaluation)
    @ManyToOne
    @JoinColumn(name = "evalue_id")
    private Utilisateur evalue;
}
