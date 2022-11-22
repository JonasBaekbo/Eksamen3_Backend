package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.Virksomhed;
import com.example.eksamen3backend.repository.VirksomhedRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class VirksomhedService implements IVirksomhed {

    VirksomhedRepository virksomhedRepository;

    VirksomhedService(VirksomhedRepository virksomhedRepository){
        this.virksomhedRepository = virksomhedRepository;
    }

    @Override
    public Set<Virksomhed> findall() {
        Set<Virksomhed> set = new HashSet<>();
        virksomhedRepository.findAll().forEach(set::add);
        return set;
    }

    @Override
    public Virksomhed save(Virksomhed object) {
        return virksomhedRepository.save(object);
    }

    @Override
    public void delete(Virksomhed object) {
        virksomhedRepository.delete(object);

    }

    @Override
    public void deleteByID(Long aLong) {
        virksomhedRepository.deleteById(aLong);

    }

    @Override
    public Optional<Virksomhed> findbyId(Long aLong) {
        return virksomhedRepository.findById(aLong);
    }
}
