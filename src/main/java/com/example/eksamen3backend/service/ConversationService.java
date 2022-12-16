package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Conversation;
import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.repository.ConversationRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ConversationService implements IConversation {

    ConversationRepository conversationRepository;

    ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    public Set<Conversation> findall() {
        Set<Conversation> set = new HashSet<>();
        conversationRepository.findAll().forEach(set::add);
        return set;
    }

    @Override
    public Conversation save(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    @Override
    public void delete(Conversation conversation) {
        conversationRepository.delete(conversation);
    }

    @Override
    public void deleteByID(Long id) {
        conversationRepository.deleteById(id);
    }

    @Override
    public Optional<Conversation> findbyId(Long id) {
        return conversationRepository.findById(id);
    }

    @Override
    public Set<Conversation> getConversationsByCorporationOrderByDateDesc(Corporation corporation) {
        return conversationRepository.getConversationsByCorporationOrderByDateDesc(corporation);
    }

    @Override
    public Set<Conversation> getConversationsByContactPersonOrderByDateDesc(ContactPerson contactPerson) {
        return conversationRepository.getConversationsByContactPersonOrderByDateDesc(contactPerson);
    }


}
