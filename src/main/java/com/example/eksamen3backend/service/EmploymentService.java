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
public class EmploymentService implements IEmployment{
    private EmploymentRepository employmentRepository;

    public EmploymentService(EmploymentRepository employmentRepository) {
        this.employmentRepository = employmentRepository;
    }

    @Override
    public Set<Employment> findall(){
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
    public List<Employment> findByCorporationAndMovedFromCorporationIsNull(Corporation corporation) {
        return employmentRepository.findByCorporationAndMovedFromCorporationIsNull(corporation);
    }

}
