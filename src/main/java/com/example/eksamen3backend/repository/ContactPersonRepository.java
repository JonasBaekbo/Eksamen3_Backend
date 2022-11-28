package com.example.eksamen3backend.repository;

import com.example.eksamen3backend.model.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactPersonRepository extends JpaRepository<ContactPerson, Long> {
    List<ContactPerson> findByName(String name);
}
