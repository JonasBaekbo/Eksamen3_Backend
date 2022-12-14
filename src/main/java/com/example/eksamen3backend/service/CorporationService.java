package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.repository.CorporationRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
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

    @Override
    public List<Corporation> findByName(String name) {
        return corporationRepository.findByName(name);
    }
    @Override
    public List<Corporation> findByIsActive(Integer isActive){
        return corporationRepository.findByIsActive(isActive);
    }

    @Override
    public List<Corporation> findAllByNameContaining(String name) {
        return  corporationRepository.findAllByNameContaining(name);
    }

    @Override
    public List<Corporation> findByIsActiveAndNameContainingOrderByNameAsc(int isActive, String name) {
        return corporationRepository.findByIsActiveAndNameContainingOrderByNameAsc(isActive,name);
    }


}
