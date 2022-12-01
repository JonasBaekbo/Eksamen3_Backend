package com.example.eksamen3backend.repository;

import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Conversation;
import com.example.eksamen3backend.model.Corporation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Set<Conversation> getConversationsByCorporationOrderByDateDesc(Corporation corporation);
    Set<Conversation>getConversationsByContactPersonOrderByDateDesc(ContactPerson contactPerson);
}
