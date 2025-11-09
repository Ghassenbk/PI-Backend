package com.ghassen.gymbackend.repositories;

import com.ghassen.gymbackend.entities.OffreTravail;
import com.ghassen.gymbackend.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OffreTravailRepository extends JpaRepository<OffreTravail, Long> {
    List<OffreTravail> findByEmployerId(Long employerId);
    // Find all available offers
    List<OffreTravail> findByStatus(Status status);


    List<OffreTravail> findByEmployer_IdAndStatus(Long employerId, Status status);
}