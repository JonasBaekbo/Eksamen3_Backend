package com.example.eksamen3backend.controller;

import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.model.UpdateEntity;
import com.example.eksamen3backend.service.ContactPersonService;
import com.example.eksamen3backend.service.CorporationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CorporationController {

    private CorporationService corporationService;

    public CorporationController(CorporationService corporationService) {
        this.corporationService = corporationService;
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

    @PutMapping("/updateCorporation")
    public ResponseEntity<Map> updateContactperson(@RequestBody UpdateEntity updateEntity){

        List<Corporation> corpList = corporationService.findByName(updateEntity.getCorpName());
        Corporation corporationToUpdate = corpList.get(0);
        corporationToUpdate.setName(updateEntity.getCorpNameToUpdate());
        corporationService.save(corporationToUpdate);
        Map<String,String > map = new HashMap<>();
        map.put("message","Contactperson updatet, if found " + updateEntity.getCorpName());
        return ResponseEntity.ok(map);
    }
}
