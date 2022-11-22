package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.SamtaleHistorik;
import com.example.eksamen3backend.repository.SamtaleHistorikRepository;
import com.example.eksamen3backend.repository.SamtaleRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class SamtaleHistorikService implements ISamtaleHistorik{

    SamtaleHistorikRepository samtaleHistorikRepository;

    SamtaleHistorikService(SamtaleHistorikRepository samtaleHistorikRepository){
        this.samtaleHistorikRepository = samtaleHistorikRepository;
    }

    @Override
    public Set<SamtaleHistorik> findall() {
        Set<SamtaleHistorik> set = new HashSet<>();
        samtaleHistorikRepository.findAll().forEach(set::add);
        return set;
    }

    @Override
    public SamtaleHistorik save(SamtaleHistorik object) {
        return samtaleHistorikRepository.save(object);
    }

    @Override
    public void delete(SamtaleHistorik object) {
        samtaleHistorikRepository.delete(object);

    }

    @Override
    public void deleteByID(Long aLong) {
        samtaleHistorikRepository.deleteById(aLong);

    }

    @Override
    public Optional<SamtaleHistorik> findbyId(Long aLong) {
        return samtaleHistorikRepository.findById(aLong);
    }
}
