package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.repository.ContactPersonHistoryRepository;
import com.example.eksamen3backend.repository.ContactPersonRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ContactPersonService implements IContactPerson {

    private ContactPersonRepository contactPersonRepository;

    public ContactPersonService(ContactPersonRepository contactPersonRepository){
        this.contactPersonRepository = contactPersonRepository;
    }

    @Override
    public Set<ContactPerson> findall() {
        Set<ContactPerson> set = new HashSet<>();
        contactPersonRepository.findAll().forEach(set::add);
        return set;
    }

    @Override
    public ContactPerson save(ContactPerson contactPerson) {
        return contactPersonRepository.save(contactPerson);
    }

    @Override
    public void delete(ContactPerson contactPerson) {
        contactPersonRepository.delete(contactPerson);
    }

    @Override
    public void deleteByID(Long id) {
        contactPersonRepository.deleteById(id);

    }

    @Override
    public Optional<ContactPerson> findbyId(Long id) {
        return contactPersonRepository.findById(id);
    }

    @Override
    public List<ContactPerson> findByName(String name) {
        return contactPersonRepository.findByName(name);
    }
}
