package com.example.eksamen3backend.controller;

import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.utilities.UpdateEntity;
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
    public ResponseEntity<Set<Corporation>> createCorporation(@RequestBody Corporation corporation) {
        if (corporationService.save(corporation) != null) {
            System.out.println("Virksomhed oprettet: " + corporation.getName());
        } else {
            System.out.println("Fejl i oprettelse af: " + corporation.getName());
        }
        return new ResponseEntity<>(showAll(), HttpStatus.OK);
    }

    @GetMapping("/findCorporationByName")
    public ResponseEntity<Corporation> findCorporationByName(@RequestParam String name){

        List<Corporation> corporationSet = corporationService.findByName(name);
        Corporation corporation = corporationSet.get(0);

        return new ResponseEntity<>(corporation,HttpStatus.OK);

    }


    @GetMapping("/showCorporations")
    public Set<Corporation> showAll() {
        return corporationService.findall();
    }

    @PutMapping("/updateCorporation")
    public ResponseEntity<Map> updateContactperson(@RequestBody UpdateEntity updateEntity){

        List<Corporation> corpList = corporationService.findByName(updateEntity.getCorpName());
        Corporation corporationToUpdate = corpList.get(0);

        if(updateEntity.getCorpNameToUpdate() != null){
            corporationToUpdate.setName(updateEntity.getCorpNameToUpdate());
        }
        if(updateEntity.getCorpCityToUpdate() != null){
            corporationToUpdate.setCity(updateEntity.getCorpCityToUpdate());
        }
        if(updateEntity.getCorpAddressToUpdate() != null){
            corporationToUpdate.setAddress(updateEntity.getCorpAddressToUpdate());
        }
        if(updateEntity.getCorpLogoToUpdate() != null){
            corporationToUpdate.setLogo(updateEntity.getCorpLogoToUpdate());
        }
        if(updateEntity.getCorpCountryToUpdate() != null){
            corporationToUpdate.setCountry(updateEntity.getCorpCountryToUpdate());
        }
        corporationService.save(corporationToUpdate);
        Map<String,String > map = new HashMap<>();
        map.put("message","Contactperson updatet, if found " + updateEntity.getCorpName());
        return ResponseEntity.ok(map);
    }
}
