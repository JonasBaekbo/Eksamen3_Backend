package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.Kontaktperson;
import com.example.eksamen3backend.repository.KontakpersonRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class KontakpersonService  implements IKontaktperson{

    private KontakpersonRepository kontakpersonRepository;

    public KontakpersonService(KontakpersonRepository kontakpersonRepository){
        this.kontakpersonRepository = kontakpersonRepository;
    }

    @Override
    public Set<Kontaktperson> findall() {
        Set<Kontaktperson> set = new HashSet<>();
        kontakpersonRepository.findAll().forEach(set::add);
        return set;
    }

    @Override
    public Kontaktperson save(Kontaktperson object) {
        return kontakpersonRepository.save(object);
    }

    @Override
    public void delete(Kontaktperson object) {
        kontakpersonRepository.delete(object);
    }

    @Override
    public void deleteByID(Long aLong) {
        kontakpersonRepository.deleteById(aLong);

    }

    @Override
    public Optional<Kontaktperson> findbyId(Long aLong) {
        return kontakpersonRepository.findById(aLong);
    }
}
