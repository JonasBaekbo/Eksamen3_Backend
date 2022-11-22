package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.EmploymentHistory;
import com.example.eksamen3backend.repository.EmploymentHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class EmploymentHistoryService implements IEmploymentHistory {

   EmploymentHistoryRepository employmentHistoryRepository;

   private EmploymentHistoryService(EmploymentHistoryRepository employmentHistoryRepository){
       this.employmentHistoryRepository = employmentHistoryRepository;
   }

    @Override
    public Set<EmploymentHistory> findall() {
        Set<EmploymentHistory> set = new HashSet<>();
        employmentHistoryRepository.findAll().forEach(set::add);
        return set;
    }

    @Override
    public EmploymentHistory save(EmploymentHistory employmentHistory) {
        return employmentHistoryRepository.save(employmentHistory);
    }

    @Override
    public void delete(EmploymentHistory employmentHistory) {
       employmentHistoryRepository.save(employmentHistory);

    }

    @Override
    public void deleteByID(Long id) {
       employmentHistoryRepository.deleteById(id);

    }

    @Override
    public Optional<EmploymentHistory> findbyId(Long id) {
        return employmentHistoryRepository.findById(id);
    }
}
