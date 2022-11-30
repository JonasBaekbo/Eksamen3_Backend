package com.example.eksamen3backend.repository;

import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.model.Employment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmploymentRepository extends JpaRepository<Employment, Long> {
        Employment findByContactPersonAndMovedFromCorporationIsNull(ContactPerson contactPerson);
        List<Employment> findByCorporationAndMovedFromCorporationIsNull(Corporation corporation);
    }

