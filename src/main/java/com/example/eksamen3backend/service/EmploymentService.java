package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.model.Employment;
import com.example.eksamen3backend.repository.EmploymentRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EmploymentService implements IEmployment {
    private EmploymentRepository employmentRepository;

    public EmploymentService(EmploymentRepository employmentRepository) {
        this.employmentRepository = employmentRepository;
    }

    @Override
    public Set<Employment> findall() {
        Set<Employment> set = new HashSet<>();
        employmentRepository.findAll().forEach(set::add);
        return set;
    }

    @Override
    public Employment save(Employment object) {
        return employmentRepository.save(object);
    }

    @Override
    public void delete(Employment object) {
        employmentRepository.delete(object);

    }

    @Override
    public void deleteByID(Long aLong) {
        employmentRepository.deleteById(aLong);
    }

    @Override
    public Optional<Employment> findbyId(Long aLong) {
        return employmentRepository.findById(aLong);
    }

    @Override
    public Employment findByContactPersonAndMovedFromCorporationIsNull(ContactPerson contactPerson) {
        return employmentRepository.findByContactPersonAndMovedFromCorporationIsNull(contactPerson);
    }


    @Override
    public Set<Employment> findEmploymentsByContactPersonOrderByAddedToCorporationDesc(ContactPerson contactPerson) {
        return employmentRepository.findEmploymentsByContactPersonOrderByAddedToCorporationDesc(contactPerson);
    }

    public Employment editEmployment(Employment currentEmployment, Employment employment, Corporation newCorporation) {
        Corporation corporation = currentEmployment.getCorporation();
        ContactPerson contactPersonToUpdate = currentEmployment.getContactPerson();
        //tjekker om der ligger en aktiv employment på kontaktpersonen, hvis der findes en, sættes den til at slutte når den næste starter
        if (!(corporation.equals(newCorporation))) {
            currentEmployment.setMovedFromCorporation(employment.getAddedToCorporation());
            employmentRepository.save(currentEmployment);
            employment.setCorporation(newCorporation);
        } else {
            employment.setId(currentEmployment.getId());
            employment.setCorporation(corporation);
        }
        employment.setContactPerson(contactPersonToUpdate);
        employmentRepository.save(employment);
        return employment;
    }

    public Employment makeEmployment(ContactPerson contactPersonToUpdate, Corporation corporation, Employment employment) {
        employment.setCorporation(corporation);
        employment.setContactPerson(contactPersonToUpdate);
        employmentRepository.save(employment);
        return employment;
    }
}