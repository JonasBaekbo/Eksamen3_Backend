package com.example.eksamen3backend.controller;

import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.model.Employment;
import com.example.eksamen3backend.model.Photo;
import com.example.eksamen3backend.service.CorporationService;
import com.example.eksamen3backend.service.PhotoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class CorporationController {

    private CorporationService corporationService;
    private PhotoService photoService;

    public CorporationController(CorporationService corporationService, PhotoService photoService) {
        this.corporationService = corporationService;
        this.photoService = photoService;
    }

    @PostMapping("/createCorporation")
    public ResponseEntity<List<Corporation>> createCorporation(@RequestBody String corporationString) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(corporationString);
        JsonNode nameNode=rootNode.path("name");
        String corporationName=nameNode.asText();


        if(corporationService.findByName(corporationName).isEmpty()) {
            Corporation corporation = mapper.readValue(corporationString, Corporation.class);
            JsonNode logoNode=rootNode.path("logo");
            Photo logo =new Photo();
            logo.setImageString(logoNode.asText());
            logo.setCreated(new Date());
            photoService.save(logo);
            corporation.setIsActive(1);
            corporation.setLogo(logo);
            corporationService.save(corporation);

            if (corporationService.save(corporation) != null) {
                System.out.println("Virksomhed oprettet: " + corporation.getName());
            } else {
                System.out.println("Fejl i oprettelse af: " + corporation.getName());
            }
        }else{
            System.out.println("Virksohed med dette name "+corporationName + " findes allreded");
        }

        return new ResponseEntity<>(showAll(), HttpStatus.OK);
    }

    @GetMapping("/findCorporationByName")
    public ResponseEntity<Corporation> findCorporationByName(@RequestParam String name) {
        List<Corporation> corporationList = corporationService.findByName(name);
        Corporation corporation = corporationList.get(0);

        return new ResponseEntity<>(corporation, HttpStatus.OK);
    }

    @GetMapping("/findCorporationById")
    public ResponseEntity<Corporation> findContactPersonById(@RequestParam long corpID) {
        Optional<Corporation> corporation_ = corporationService.findbyId(corpID);
        if (corporation_.isPresent()) {
            Corporation corporation = corporation_.get();
            return new ResponseEntity<>(corporation, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @GetMapping("/showCorporations")
    public List<Corporation> showAll() {
        return corporationService.findByIsActive(1);
    }

    @PutMapping("/updateCorporation")
    public ResponseEntity<Map> updateContactperson(@RequestBody Corporation updateEntity, @RequestParam long corpID) {
        Optional<Corporation> corporation_ = corporationService.findbyId(corpID);

        if (corporation_.isPresent()) {
            Corporation corporationToUpdate = corporation_.get();

            if (corporationService.findByName(updateEntity.getName()).isEmpty()) {

                if (corporationToUpdate.getName() != null) {
                    corporationToUpdate.setName(updateEntity.getName());
                }
                if (corporationToUpdate.getCity() != null) {
                    corporationToUpdate.setCity(updateEntity.getCity());
                }
                if (corporationToUpdate.getAddress() != null) {
                    corporationToUpdate.setAddress(updateEntity.getAddress());
                }
               if (corporationToUpdate.getLogo() != null) {
                    corporationToUpdate.setLogo(updateEntity.getLogo());
                }
                if (corporationToUpdate.getCountry() != null) {
                    corporationToUpdate.setCountry(updateEntity.getCountry());
                }
                System.out.println(corporationToUpdate.getName());
                corporationService.save(corporationToUpdate);
            }else{
                System.out.println("Virksohed med dette name " + updateEntity.getName() + " findes allreded");;
            }

        }
        Map<String, String> map = new HashMap<>();
        map.put("message", "corporation updatet, if found " + updateEntity.getName());
        return ResponseEntity.ok(map);
    }

    //ændre status på virksomhed fra 1=aktiv til 0=inaktiv
    @PutMapping("/archiveCorporation")
    public ResponseEntity<Map> archiveCorporation(@RequestParam long corpID) {

        Optional<Corporation> corporation_ = corporationService.findbyId(corpID);

        if (corporation_.isPresent()) {
            Corporation corporationtoUpdate = corporation_.get();
            corporationtoUpdate.setIsActive(0);

            corporationService.save(corporationtoUpdate);
        }
        Map<String, String> map = new HashMap<>();
        map.put("message", "Corporation archived");
        return ResponseEntity.ok(map);
    }

    @GetMapping("/getArchivedCorporations")
    public List<Corporation> getArchivedCorporations(){
        return corporationService.findByIsActive(0);
    }
}

