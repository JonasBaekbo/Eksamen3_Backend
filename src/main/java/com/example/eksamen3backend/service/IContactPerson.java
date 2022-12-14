package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.ContactPerson;

import java.util.List;

public interface IContactPerson extends CrudService<ContactPerson, Long> {

    List<ContactPerson> findByName(String name);

    List<ContactPerson> findByIsActive(int isActive);

    List<ContactPerson> findByIsActiveOrderByNameAsc(int isActive);
    List<ContactPerson> findAllByNameContaining(String name);

    List<ContactPerson> findByIsActiveAndNameContainingOrderByNameAsc(int isActive,String name);
}
