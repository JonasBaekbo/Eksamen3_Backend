package com.example.eksamen3backend.repository;

import com.example.eksamen3backend.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
