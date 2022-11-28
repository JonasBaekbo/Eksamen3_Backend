package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.ContactPerson;

import java.util.List;

public interface IContactPerson extends CrudService<ContactPerson, Long> {

    List<ContactPerson> findByname(String name);
}
