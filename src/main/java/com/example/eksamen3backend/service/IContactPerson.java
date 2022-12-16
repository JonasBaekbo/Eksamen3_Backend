package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.ContactPerson;

import java.util.List;

public interface IContactPerson extends CrudService<ContactPerson, Long> {


    List<ContactPerson> findByIsActive(int isActive);

    List<ContactPerson> findByIsActiveOrderByNameAsc(int isActive);


    List<ContactPerson> findByIsActiveAndNameContainingOrderByNameAsc(int isActive, String name);
}
