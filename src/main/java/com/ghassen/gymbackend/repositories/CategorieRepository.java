package com.ghassen.gymbackend.repositories;

import com.ghassen.gymbackend.entities.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Categorie, Long> { }