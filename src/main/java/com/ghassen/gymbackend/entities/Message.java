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
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_message")
    private Long id;

    private String contenu;
    private Date dateEnvoi;

    // 🔁 L’expéditeur du message
    @ManyToOne
    @JoinColumn(name = "expediteur_id")
    private Utilisateur expediteur;

    // 🔁 Le destinataire du message
    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private Utilisateur destinataire;
}
