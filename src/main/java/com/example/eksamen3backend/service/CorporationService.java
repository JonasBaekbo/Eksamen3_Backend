package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.repository.CorporationRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CorporationService implements ICorporation {

    CorporationRepository corporationRepository;

    CorporationService(CorporationRepository corporationRepository){
        this.corporationRepository = corporationRepository;
    }

    @Override
    public Set<Corporation> findall() {
        Set<Corporation> set = new HashSet<>();
        corporationRepository.findAll().forEach(set::add);
        return set;
    }

    @Override
    public Corporation save(Corporation corporation) {
        return corporationRepository.save(corporation);
    }

    @Override
    public void delete(Corporation corporation) {
        corporationRepository.delete(corporation);

    }

    @Override
    public void deleteByID(Long id) {
        corporationRepository.deleteById(id);

    }

    @Override
    public Optional<Corporation> findbyId(Long id) {
        return corporationRepository.findById(id);
    }
}