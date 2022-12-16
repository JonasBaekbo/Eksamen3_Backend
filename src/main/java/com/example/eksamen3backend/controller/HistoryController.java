package com.example.eksamen3backend.controller;


import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Conversation;
import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.model.Employment;
import com.example.eksamen3backend.service.ContactPersonService;
import com.example.eksamen3backend.service.ConversationService;
import com.example.eksamen3backend.service.CorporationService;
import com.example.eksamen3backend.service.EmploymentService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
public class HistoryController {
    private final ContactPersonService contactPersonService;
    private final CorporationService corporationService;
    private final EmploymentService employmentService;
    private final ConversationService conversationService;

    public HistoryController(ContactPersonService contactPersonService, CorporationService corporationService, EmploymentService employmentService, ConversationService conversationService) {
        this.contactPersonService = contactPersonService;
        this.corporationService = corporationService;
        this.employmentService = employmentService;
        this.conversationService = conversationService;
    }

   //Der kommer både oplysninger om firma og nuværende kontaktpersoner med, dette sorteres fra i visning af webside
   @Operation(description ="Finder alle samtaler der er lavet med en virksomhed ud fra corpID")
    @GetMapping("/conversationsByCorporation")
    public ResponseEntity<Set<Conversation>> conversationsByCorporation(@RequestParam long corpID) {
        Optional<Corporation> corporation_ = corporationService.findbyId(corpID);
        if (corporation_.isPresent()) {
            Corporation corporation = corporation_.get();
            Set<Conversation> conversations = conversationService.getConversationsByCorporationOrderByDateDesc(corporation);
            return new ResponseEntity<>(conversations, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    //Der kommer både oplysninger om firma og nuværende kontaktpersoner med, dette sorteres fra i visning af webside
    @Operation(description ="Finder alle samtaler der er lavet med en kontaktperson ud fra contactID")
    @GetMapping("/conversationsByContactPerson")
    public ResponseEntity<Set<Conversation>> conversationsByContactPerson(@RequestParam long contactID) {
        Optional<ContactPerson> contactPerson_ = contactPersonService.findbyId(contactID);
        if (contactPerson_.isPresent()) {
            ContactPerson contactPerson = contactPerson_.get();
            Set<Conversation> conversations = conversationService.getConversationsByContactPersonOrderByDateDesc(contactPerson);
            return new ResponseEntity<>(conversations, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(description ="Finder alle ansættelser for en kontaktperson ud fra contactID")
    @GetMapping("/employmentHistoryById")
    public ResponseEntity<Set<Employment>> employmentHistory(@RequestParam long contactID) {
        Optional<ContactPerson> contactPerson_=contactPersonService.findbyId(contactID);
        if (contactPerson_.isPresent()) {
            ContactPerson contactPerson=contactPerson_.get();
            Set<Employment> employments =employmentService.findEmploymentsByContactPersonOrderByAddedToCorporationDesc(contactPerson) ;
            return new ResponseEntity<>(employments, HttpStatus.OK);
        } else {return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);}
    }

}

