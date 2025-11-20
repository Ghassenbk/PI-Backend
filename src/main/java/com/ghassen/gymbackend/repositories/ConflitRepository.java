// src/main/java/com/ghassen/gymbackend/repository/ConflitRepository.java
package com.ghassen.gymbackend.repositories;

import com.ghassen.gymbackend.entities.Conflit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConflitRepository extends JpaRepository<Conflit, Long> {
    List<Conflit> findByOffreTravailId(Long offreTravailId);
}