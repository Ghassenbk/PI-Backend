// src/main/java/com/ghassen/gymbackend/repository/CandidatureRepository.java
package com.ghassen.gymbackend.repositories;

import com.ghassen.gymbackend.entities.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {

    List<Candidature> findByOffreTravailId(Long offreId);

    // Optional: stricter check
    List<Candidature> findByOffreTravailIdAndOffreTravail_Employer_Id(Long offreId, Long employerId);
}