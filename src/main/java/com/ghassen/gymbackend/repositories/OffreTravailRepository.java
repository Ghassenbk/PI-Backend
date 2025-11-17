package com.ghassen.gymbackend.repositories;

import com.ghassen.gymbackend.entities.OffreTravail;
import com.ghassen.gymbackend.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OffreTravailRepository extends JpaRepository<OffreTravail, Long> {
    List<OffreTravail> findByEmployerId(Long employerId);
    // Find all available offers
    List<OffreTravail> findByStatus(Status status);


    List<OffreTravail> findByEmployer_IdAndStatus(Long employerId, Status status);


    Optional<OffreTravail> findByIdAndEmployer_Id(Long id, Long employerId);



}