package com.example.eksamen3backend.controller;

import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.service.ContactPersonService;
import com.example.eksamen3backend.service.CorporationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
public class CorporationController {

    private CorporationService corporationService;
    private ContactPersonService contactPersonService;

    public CorporationController(CorporationService corporationService, ContactPersonService contactPersonService) {
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


    @GetMapping("/showCorporations")
    public Set<Corporation> showAll() {
        return corporationService.findall();


    }
}
