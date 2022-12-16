package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.model.Employment;

import java.util.List;
import java.util.Set;

public interface IEmployment extends CrudService<Employment, Long> {

    Employment findByContactPersonAndMovedFromCorporationIsNull(ContactPerson contactPerson);

    Set<Employment> findEmploymentsByContactPersonOrderByAddedToCorporationDesc(ContactPerson contactPerson);

}
