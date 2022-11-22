package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.KontakpersonsHistorik;
import com.example.eksamen3backend.repository.KontakpersonRepository;
import com.example.eksamen3backend.repository.KontaktpersonsHistorikRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class KontakpersonsHistorikService implements IKontaktpersonsHistorik{

    KontaktpersonsHistorikRepository kontaktpersonsHistorikRepository;

    private KontakpersonsHistorikService(KontaktpersonsHistorikRepository kontaktpersonsHistorikRepository){
        this.kontaktpersonsHistorikRepository = kontaktpersonsHistorikRepository;
    }

    @Override
    public Set<KontakpersonsHistorik> findall() {
        Set<KontakpersonsHistorik> set = new HashSet<>();
        kontaktpersonsHistorikRepository.findAll().forEach(set::add);
        return set;
    }

    @Override
    public KontakpersonsHistorik save(KontakpersonsHistorik object) {
        return kontaktpersonsHistorikRepository.save(object);
    }

    @Override
    public void delete(KontakpersonsHistorik object) {
        kontaktpersonsHistorikRepository.delete(object);

    }

    @Override
    public void deleteByID(Long aLong) {
        kontaktpersonsHistorikRepository.deleteById(aLong);

    }

    @Override
    public Optional<KontakpersonsHistorik> findbyId(Long aLong) {
        return kontaktpersonsHistorikRepository.findById(aLong);
    }
}
