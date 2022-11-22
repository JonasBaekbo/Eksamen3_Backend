package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.ContactPersonHistory;
import com.example.eksamen3backend.repository.ContactPersonHistoryRepository;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ContactPersonHistoryService implements IContactPersonHistory {

    ContactPersonHistoryRepository contactPersonHistoryRepository;


    public ContactPersonHistoryService(ContactPersonHistoryRepository contactPersonHistoryRepository) {
        this.contactPersonHistoryRepository = contactPersonHistoryRepository;
    }

    @Override
    public Set<ContactPersonHistory> findall() {
        Set<ContactPersonHistory> set = new HashSet<>();
        contactPersonHistoryRepository.findAll().forEach(set::add);
        return set;
    }

    @Override
    public ContactPersonHistory save(ContactPersonHistory contactPersonHistory) {
        return contactPersonHistoryRepository.save(contactPersonHistory);
    }

    @Override
    public void delete(ContactPersonHistory contactPersonHistory) {
        contactPersonHistoryRepository.delete(contactPersonHistory);

    }

    @Override
    public void deleteByID(Long id) {
        contactPersonHistoryRepository.deleteById(id);

    }

    @Override
    public Optional<ContactPersonHistory> findbyId(Long id) {
        return contactPersonHistoryRepository.findById(id);
    }
}
