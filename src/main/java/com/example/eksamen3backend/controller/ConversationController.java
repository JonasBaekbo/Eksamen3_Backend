package com.example.eksamen3backend.controller;

import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Conversation;
import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.service.ContactPersonService;
import com.example.eksamen3backend.service.ConversationService;
import com.example.eksamen3backend.service.CorporationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ConversationController {


    private ConversationService conversationService;
    private CorporationService corporationService;
    private ContactPersonService contactPersonService;

    public ConversationController(
            ConversationService conversationService,
            CorporationService corporationService,
            ContactPersonService contactPersonService
    ) {
        this.conversationService = conversationService;
        this.corporationService = corporationService;
        this.contactPersonService = contactPersonService;
    }


    @PostMapping("/createConversation")
    public ResponseEntity<Set<Conversation>> createConversation(@RequestBody Conversation conversation, @RequestParam Long contactID, @RequestParam Long corpID) {
        if (conversationService.save(conversation) != null) {
            System.out.println("Samtale oprettet: " + conversation.getSubject());
            addConversation(conversation.getId(), corpID, contactID);
        } else {
            System.out.println("Fejl i oprettelsen af " + conversation.getSubject());
        }
        return new ResponseEntity<>(getAll(), HttpStatus.OK);
    }

    @PostMapping("/addConversation")
    public ResponseEntity<String> addConversation(@RequestParam Long contactID, @RequestParam Long convoID, @RequestParam Long corpID) {
        Optional<ContactPerson> contactPerson_ = contactPersonService.findbyId(contactID);
        Optional<Conversation> conversation_ = conversationService.findbyId(convoID);
        Optional<Corporation> corporation_ = corporationService.findbyId(corpID);
        if (contactPerson_.isPresent() && conversation_.isPresent() && corporation_.isPresent()) {
            ContactPerson contactPerson = contactPerson_.get();
            Conversation conversation = conversation_.get();
            Corporation corporation = corporation_.get();

            conversation.setContactPerson(contactPerson);
            conversation.setCorporation(corporation);

            conversationService.save(conversation);

            return new ResponseEntity<>("Samtale: " + conversation.getSubject() + " fra Kontaktperson: " + contactPerson.getName() + " oprettet med virksomhed: " + corporation.getName() + " gemt.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Fejl i oprettelse af samtale", HttpStatus.OK);
        }

    }

    @GetMapping("/getAllConversations")
    public Set<Conversation> getAll() {
        return conversationService.findall();
    }

    @GetMapping("/findConversationById")
    public ResponseEntity<Conversation> findConversationById(@RequestParam long convId) {
        Optional<Conversation> conversation_ = conversationService.findbyId(convId);
        if (conversation_.isPresent()) {
            Conversation conversation = conversation_.get();
            return new ResponseEntity<>(conversation, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}






