package com.example.eksamen3backend.controller;


import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Conversation;
import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.model.Employment;
import com.example.eksamen3backend.service.ContactPersonService;
import com.example.eksamen3backend.service.ConversationService;
import com.example.eksamen3backend.service.CorporationService;
import com.example.eksamen3backend.service.EmploymentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;


@RestController
public class HistoryController {
    private ContactPersonService contactPersonService;
    private CorporationService corporationService;
    private EmploymentService employmentService;
    private ConversationService conversationService;

    public HistoryController(ContactPersonService contactPersonService, CorporationService corporationService, EmploymentService employmentService, ConversationService conversationService) {
        this.contactPersonService = contactPersonService;
        this.corporationService = corporationService;
        this.employmentService = employmentService;
        this.conversationService = conversationService;
    }

    //Der kommer både oplysninger om firma og nuværende kontaktpersoner med, dette kan sorteres fra i visning af webside, eller i koden på et senere tidspunkt
    //samtaler sorteret på virksomhed
    @GetMapping("/conversationsByCorporation")
    public ResponseEntity<Set<Conversation>> conversationsByCorporation(@RequestParam long corpId) {
        Optional<Corporation> corporation_ = corporationService.findbyId(corpId);
        if (corporation_.isPresent()) {
            Corporation corporation = corporation_.get();
            Set<Conversation> conversations = conversationService.getConversationsByCorporationOrderByDateDesc(corporation);
            return new ResponseEntity<>(conversations, HttpStatus.OK);
        } else {return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);}

    }

    //Der kommer både oplysninger om firma og nuværende kontaktpersoner med, dette kan sorteres fra i visning af webside, eller i koden på et senere tidspunkt
    //samtaler sorteret på kontaktperson
    @GetMapping("/conversationsByContactPerson")
    public ResponseEntity<Set<Conversation>> conversationsByContactPerson(@RequestParam long contactPersonId) {
        Optional<ContactPerson> contactPerson_=contactPersonService.findbyId(contactPersonId);
        if (contactPerson_.isPresent()) {
            ContactPerson contactPerson=contactPerson_.get();
            Set<Conversation> conversations = conversationService.getConversationsByContactPersonOrderByDateDesc(contactPerson);
            return new ResponseEntity<>(conversations, HttpStatus.OK);
        } else {return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);}

    }

    //ansættelseshistorik på en kontaktperson
    @GetMapping("/employmentHistory")
    public ResponseEntity<Set<Employment>> employmentHistory(@RequestParam long contactPersonId) {
        Optional<ContactPerson> contactPerson_=contactPersonService.findbyId(contactPersonId);
        if (contactPerson_.isPresent()) {
            ContactPerson contactPerson=contactPerson_.get();
            Set<Employment> employments =employmentService.findEmploymentsByContactPersonOrderByAddedToCorporationDesc(contactPerson) ;
            return new ResponseEntity<>(employments, HttpStatus.OK);
        } else {return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);}
    }
}
