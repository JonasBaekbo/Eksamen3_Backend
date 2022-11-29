package com.example.eksamen3backend.controller;

import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.service.ContactPersonService;
import com.example.eksamen3backend.service.CorporationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
public class CorporationController {

    private CorporationService corporationService;
    private ContactPersonService contactPersonService;

    public CorporationController(CorporationService corporationService, ContactPersonService contactPersonService){
        this.corporationService = corporationService;
        this.contactPersonService = contactPersonService;
    }
    @PostMapping("/createCorporation")
    public ResponseEntity<String> createCorporation(@RequestBody Corporation corporation) {
        String msg = "";
        if (corporationService.save(corporation) != null) {
            msg = "Virksomhed oprettet: " + corporation.getName();
        } else {
            msg = "Fejl i oprettelsen af " + corporation.getName();
        }
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
    @PostMapping("/addContactpersontoCorp")
    public ResponseEntity<String> addContactpersontoCorp(@RequestParam Long contactID, @RequestParam Long corpID){
        Optional<ContactPerson> contactPerson_ = contactPersonService.findbyId(contactID);
        Optional<Corporation> corporation_ = corporationService.findbyId(corpID);
        if (contactPerson_.isPresent()){
           if (corporation_.isPresent()){
               Corporation corporation = corporation_.get();

               ContactPerson contactPerson = contactPerson_.get();

               contactPerson.setCorporation(corporation);

               contactPersonService.save(contactPerson);

               return new ResponseEntity<>("Tilføjet kontaktperson:" + contactPerson.getName() + " Til virksomhed: " + corporation.getName(), HttpStatus.OK);
           }
           return new ResponseEntity<>("Kunne ikke finde virksomhed med id: " + corpID, HttpStatus.OK);
        }
        return new ResponseEntity<>("Kunne ikke finde kontaktperson med id: " + contactID, HttpStatus.OK);
    }



}
