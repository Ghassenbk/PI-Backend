package com.ghassen.gymbackend.controllers;

import com.ghassen.gymbackend.entities.Evaluation;
import com.ghassen.gymbackend.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EvaluationController {

    private final EvaluationService evaluationService;

    /**
     * Ajouter une évaluation
     */
    @PostMapping("/add/{evaluateurId}/{evalueId}")
    public Evaluation addEvaluation(
            @RequestBody Evaluation evaluation,
            @PathVariable Long evaluateurId,
            @PathVariable Long evalueId
    ) {
        return evaluationService.addEvaluation(evaluation, evaluateurId, evalueId);
    }

    /**
     * Récupérer toutes les evaluations reçues par un user
     */
    @GetMapping("/user/{userId}")
    public List<Evaluation> getEvaluations(@PathVariable Long userId) {
        return evaluationService.getEvaluationsByUser(userId);
    }

    /**
     * Obtenir la moyenne
     */
    @GetMapping("/moyenne/{userId}")
    public double getMoyenne(@PathVariable Long userId) {
        return evaluationService.getMoyenneNote(userId);
    }
}
