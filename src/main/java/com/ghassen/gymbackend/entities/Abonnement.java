package com.ghassen.gymbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Abonnement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_abonnement")
    private Long id;

    private Date dateSouscription;
    private Date dateExpiration;

    // ✅ Each abonnement has ONE type
    @ManyToOne
    @JoinColumn(name = "type_abonnement_id")
    private TypeAbonnement typeAbonnement;

    // 🔁 One abonnement can be taken by many users
    @JsonIgnore
    @OneToMany(mappedBy = "abonnement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Utilisateur> utilisateurs = new ArrayList<>();
}
