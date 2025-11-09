package com.ghassen.gymbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeAbonnement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_typeabonnement")
    private Long id;

    private String libelle;
    private String imgPath;
    private BigDecimal prix;

    // 🔁 One type can have multiple abonnements
    @JsonIgnore
    @OneToMany(mappedBy = "typeAbonnement")
    private List<Abonnement> abonnements= new ArrayList<>();
}
