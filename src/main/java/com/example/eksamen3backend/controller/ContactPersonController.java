package com.example.eksamen3backend.controller;


import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.ContactPersonHistory;
import com.example.eksamen3backend.service.ContactPersonHistoryService;
import com.example.eksamen3backend.service.ContactPersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactPersonController {

    private ContactPersonService contactPersonService;
    private ContactPersonHistoryService contactPersonHistoryService;

    public ContactPersonController(ContactPersonService contactPersonService, ContactPersonHistoryService contactPersonHistoryService) {
        this.contactPersonHistoryService = contactPersonHistoryService;
        this.contactPersonService = contactPersonService;
    }

    @PostMapping("/createContactperson")
    public ResponseEntity<String> createUser(@RequestBody ContactPerson contactPerson) {
        String msg = "";
        if (contactPersonService.save(contactPerson) != null) {
            msg = "Kontaktperson oprettet: " + contactPerson.getName();
        } else {
            msg = "Fejl i oprettelsen af " + contactPerson.getName();
        }
        return new ResponseEntity<>(msg, HttpStatus.OK);

    }
}
