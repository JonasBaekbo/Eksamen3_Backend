package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Corporation;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ICorporation extends CrudService<Corporation, Long> {

    List<Corporation> findByName(String name);

    List<Corporation> findByIsActive(Integer isActive);

    List<Corporation> findByIsActiveOrderByNameAsc(int isActive);

    List<Corporation> findByIsActiveAndNameContainingOrderByNameAsc(int isActive, String name);
}
