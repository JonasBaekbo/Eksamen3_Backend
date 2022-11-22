package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.ConversationHistory;
import com.example.eksamen3backend.repository.ConversationHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ConversationHistoryService implements IConversationHistory {

    ConversationHistoryRepository conversationHistoryRepository;

    ConversationHistoryService(ConversationHistoryRepository conversationHistoryRepository){
        this.conversationHistoryRepository = conversationHistoryRepository;
    }

    @Override
    public Set<ConversationHistory> findall() {
        Set<ConversationHistory> set = new HashSet<>();
        conversationHistoryRepository.findAll().forEach(set::add);
        return set;
    }

    @Override
    public ConversationHistory save(ConversationHistory conversationHistory) {
        return conversationHistoryRepository.save(conversationHistory);
    }

    @Override
    public void delete(ConversationHistory conversationHistory) {
        conversationHistoryRepository.delete(conversationHistory);

    }

    @Override
    public void deleteByID(Long id) {
        conversationHistoryRepository.deleteById(id);

    }

    @Override
    public Optional<ConversationHistory> findbyId(Long id) {
        return conversationHistoryRepository.findById(id);
    }
}
