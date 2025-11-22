package com.ghassen.gymbackend.service;

import com.ghassen.gymbackend.entities.Evaluation;
import com.ghassen.gymbackend.entities.Utilisateur;
import com.ghassen.gymbackend.repositories.EvaluationRepository;
import com.ghassen.gymbackend.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepo;
    private final UtilisateurRepository userRepo;

    /**
     * Ajouter une évaluation
     */
    public Evaluation addEvaluation(Evaluation ev, Long evaluateurId, Long evalueId) {

        Utilisateur evaluateur = userRepo.findById(evaluateurId)
                .orElseThrow(() -> new IllegalArgumentException("Évaluateur introuvable : " + evaluateurId));

        Utilisateur evalue = userRepo.findById(evalueId)
                .orElseThrow(() -> new IllegalArgumentException("Évalué introuvable : " + evalueId));

        if (evaluateurId.equals(evalueId)) {
            throw new IllegalArgumentException("Un utilisateur ne peut pas s'auto-évaluer.");
        }

        ev.setEvaluateur(evaluateur);
        ev.setEvalue(evalue);
        ev.setDatePublication(new Date());

        return evaluationRepo.save(ev);
    }

    /**
     * Liste des avis reçus par un utilisateur
     */
    public List<Evaluation> getEvaluationsByUser(Long userId) {

        Utilisateur user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable : " + userId));

        return evaluationRepo.findByEvalue(user);
    }

    /**
     * Moyenne des notes d'un utilisateur
     */
    public double getMoyenneNote(Long userId) {
        List<Evaluation> evaluations = getEvaluationsByUser(userId);

        return evaluations.stream()
                .mapToInt(Evaluation::getNote)
                .average()
                .orElse(0.0);
    }
}
