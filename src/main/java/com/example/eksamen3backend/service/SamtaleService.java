package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.Samtale;
import com.example.eksamen3backend.repository.SamtaleRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class SamtaleService implements ISamtale {

    SamtaleRepository samtaleRepository;

    SamtaleService(SamtaleRepository samtaleRepository){
        this.samtaleRepository = samtaleRepository;
    }

    @Override
    public Set<Samtale> findall() {
        Set<Samtale> set = new HashSet<>();
        samtaleRepository.findAll().forEach(set::add);
        return set;
    }

    @Override
    public Samtale save(Samtale object) {
        return samtaleRepository.save(object);
    }

    @Override
    public void delete(Samtale object) {
        samtaleRepository.delete(object);

    }

    @Override
    public void deleteByID(Long aLong) {
        samtaleRepository.deleteById(aLong);

    }

    @Override
    public Optional<Samtale> findbyId(Long aLong) {
        return samtaleRepository.findById(aLong);
    }
}
