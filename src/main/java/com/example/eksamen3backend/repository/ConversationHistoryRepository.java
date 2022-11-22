package com.example.eksamen3backend.repository;

import com.example.eksamen3backend.model.ConversationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationHistoryRepository extends JpaRepository<ConversationHistory, Long> {
}
