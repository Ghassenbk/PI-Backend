// src/main/java/com/ghassen/gymbackend/repository/CandidatureRepository.java
package com.ghassen.gymbackend.repositories;

import com.ghassen.gymbackend.entities.Candidature;
import com.ghassen.gymbackend.entities.OffreTravail;
import com.ghassen.gymbackend.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {

    List<Candidature> findByOffreTravailId(Long offreId);
    // In CandidatureRepository.java
    boolean existsByUtilisateurAndOffreTravail(Utilisateur utilisateur, OffreTravail offre);
    // Optional: stricter check

    List<Candidature> findByOffreTravailIdAndOffreTravail_Employer_Id(Long offreId, Long employerId);

         // ← ADD THIS METHOD
        // FIXED: Use Long instead of entity
            @Modifying
            @Query("DELETE FROM Candidature c WHERE c.offreTravail.id = :offreId AND c.id != :selectedId")
            void deleteAllExceptSelected(
                    @Param("offreId") Long offreId,
                    @Param("selectedId") Long selectedId
            );

}