package com.example.eksamen3backend.controller;

import com.example.eksamen3backend.service.ConversationHistoryService;
import com.example.eksamen3backend.service.ConversationService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConversationController {

    private ConversationService conversationService;
    private ConversationHistoryService conversationHistoryService;

    public ConversationController(ConversationService conversationService, ConversationHistoryService conversationHistoryService){
        this.conversationHistoryService = conversationHistoryService;
        this.conversationService = conversationService;
    }




}
