package com.example.eksamen3backend.repository;

import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Corporation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CorporationRepository extends JpaRepository<Corporation, Long> {

    List<Corporation> findByName(String name);

    List<Corporation> findByIsActive(Integer isActive);
    List<Corporation> findByIsActiveOrderByNameAsc(int isActive);

    List<Corporation> findAllByNameContaining(String name);
    
    List<Corporation> findByIsActiveAndNameContainingOrderByNameAsc(int isActive,String name);
}
