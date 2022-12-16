package com.example.eksamen3backend.controller;

import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Conversation;
import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.service.ContactPersonService;
import com.example.eksamen3backend.service.ConversationService;
import com.example.eksamen3backend.service.CorporationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ConversationController {
    private final ConversationService conversationService;
    private final CorporationService corporationService;
    private final ContactPersonService contactPersonService;

    public ConversationController(
            ConversationService conversationService,
            CorporationService corporationService,
            ContactPersonService contactPersonService
    ) {
        this.conversationService = conversationService;
        this.corporationService = corporationService;
        this.contactPersonService = contactPersonService;
    }

    @Operation(description = "Opretter en samtale mellem en kontaktperson og en virksomhed")
    @PostMapping("/createConversation")
    public ResponseEntity<Set<Conversation>> createConversation(@RequestBody Conversation conversation, @RequestParam Long contactID, @RequestParam Long corpID) {
        Optional<Corporation> corporation_ = corporationService.findbyId(corpID);
        Optional<ContactPerson> contactPerson_ = contactPersonService.findbyId(contactID);
        if (contactPerson_.isPresent() && corporation_.isPresent()) {
            ContactPerson contactPerson = contactPerson_.get();
            Corporation corporation = corporation_.get();
            conversation.setContactPerson(contactPerson);
            conversation.setCorporation(corporation);
            conversationService.save(conversation);
            System.out.println("Samtale oprettet: " + conversation.getSubject());
            return new ResponseEntity<>(getAll(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(getAll(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(description = "Finder alle samtaler")
    @GetMapping("/getAllConversations")
    public Set<Conversation> getAll() {
        return conversationService.findall();
    }

    @Operation(description = "Finder en samtale via convoID")
    @GetMapping("/findConversationById")
    public ResponseEntity<Conversation> findConversationById(@RequestParam long convoID) {
        Optional<Conversation> conversation_ = conversationService.findbyId(convoID);
        if (conversation_.isPresent()) {
            Conversation conversation = conversation_.get();
            return new ResponseEntity<>(conversation, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}






