package com.example.eksamen3backend.controller;


import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.ContactPersonHistory;
import com.example.eksamen3backend.service.ContactPersonHistoryService;
import com.example.eksamen3backend.service.ContactPersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @GetMapping("/getAllContactPersons")
    public Set<ContactPerson> getAll (){
        return contactPersonService.findall();
    }

    @DeleteMapping("deleteContactPerson")
    public ResponseEntity<Map> deleteContactPerson(@RequestBody ContactPerson contactperson){
        System.out.println("deleteUser is called");

        List<ContactPerson> userList = contactPersonService.findByName(contactperson.getName());
        ContactPerson ContactPersonToDelete = userList.get(0);
        contactPersonService.delete(ContactPersonToDelete);
        Map<String,String > map = new HashMap<>();
        map.put("message","user deleted, if found " + contactperson.getName());
        return ResponseEntity.ok(map);
    }
}
