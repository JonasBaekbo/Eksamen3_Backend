package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.AnsættelsesHistorik;
import com.example.eksamen3backend.repository.AnsættelsesHistorikRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class AnsættelsesHistorikService implements IAnsættelsesHistorik{

   AnsættelsesHistorikRepository ansættelsesHistorikRepository;

   private AnsættelsesHistorikService(AnsættelsesHistorikRepository ansættelsesHistorikRepository){
       this.ansættelsesHistorikRepository = ansættelsesHistorikRepository;
   }

    @Override
    public Set<AnsættelsesHistorik> findall() {
        Set<AnsættelsesHistorik> set = new HashSet<>();
        ansættelsesHistorikRepository.findAll().forEach(set::add);
        return set;
    }

    @Override
    public AnsættelsesHistorik save(AnsættelsesHistorik object) {
        return ansættelsesHistorikRepository.save(object);
    }

    @Override
    public void delete(AnsættelsesHistorik object) {
       ansættelsesHistorikRepository.save(object);

    }

    @Override
    public void deleteByID(Long aLong) {
       ansættelsesHistorikRepository.deleteById(aLong);

    }

    @Override
    public Optional<AnsættelsesHistorik> findbyId(Long aLong) {
        return ansættelsesHistorikRepository.findById(aLong);
    }
}
