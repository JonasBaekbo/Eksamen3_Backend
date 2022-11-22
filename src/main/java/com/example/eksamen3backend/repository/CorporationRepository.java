package com.example.eksamen3backend.repository;

import com.example.eksamen3backend.model.Corporation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorporationRepository extends JpaRepository<Corporation, Long> {
}
