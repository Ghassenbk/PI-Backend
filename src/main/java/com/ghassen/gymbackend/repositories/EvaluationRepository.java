package com.ghassen.gymbackend.repositories;

import com.ghassen.gymbackend.entities.Evaluation;
import com.ghassen.gymbackend.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    // Toutes les évaluations reçues par un utilisateur
    List<Evaluation> findByEvalue(Utilisateur evalue);

    // Toutes les évaluations faites par un utilisateur
    List<Evaluation> findByEvaluateur(Utilisateur evaluateur);
}
