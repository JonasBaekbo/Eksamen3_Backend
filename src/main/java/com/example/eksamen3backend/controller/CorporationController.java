package com.example.eksamen3backend.controller;

import com.example.eksamen3backend.model.Corporation;
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
    private ObjectMapper objectMapper=new ObjectMapper();

    public CorporationController(CorporationService corporationService, PhotoService photoService) {
        this.corporationService = corporationService;
        this.photoService = photoService;
    }

    @PostMapping("/createCorporation")
    public ResponseEntity<List<Corporation>> createCorporation(@RequestBody String jsonString) throws JsonProcessingException {

//        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode nameNode = rootNode.path("name");
        String corporationName = nameNode.asText();


        if (corporationService.findByName(corporationName).isEmpty()) {
            Corporation corporation = objectMapper.readValue(jsonString, Corporation.class);
            JsonNode logoNode = rootNode.path("logo");
            Photo logo = new Photo();
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
        } else {
            System.out.println("Virksomhed med dette name " + corporationName + " findes allerede");
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


    @GetMapping("/findCorporationContaining")
    public ResponseEntity<List<Corporation>> findCorporationContaining(@RequestParam String name) {
        List<Corporation> corporations = corporationService.findAllByNameContaining(name);

        return new ResponseEntity<>(corporations, HttpStatus.OK);
    }


    @PutMapping("/updateCorporation")
    public ResponseEntity<Map> updateCorporation(@RequestBody String jsonString, @RequestParam long corpID) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        Optional<Corporation> corporation_ = corporationService.findbyId(corpID);

        if (corporation_.isPresent()) {
            Corporation corporationToUpdate = corporation_.get();
//            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode nodeName = rootNode.path("name");
            String updateName = nodeName.asText();

            if (corporationToUpdate.getName().equals(updateName) || corporationService.findByName(updateName).isEmpty()) {
                Corporation newCorporationInfo = objectMapper.readValue(jsonString, Corporation.class);
                JsonNode nodeLogo = rootNode.path("logo");
                String nodeLogoAsString = nodeLogo.asText();
                Photo currentLogo = corporationToUpdate.getLogo();
                if (!(nodeLogoAsString.equals("null") || nodeLogoAsString.isEmpty() || currentLogo.getImageString().equals(nodeLogoAsString))) {
                    currentLogo.setImageString(nodeLogoAsString);
                    currentLogo.setCreated(Timestamp.valueOf(LocalDateTime.now()));
                    photoService.save(currentLogo);
                }
                newCorporationInfo.setLogo(currentLogo);
                newCorporationInfo.setId(corporationToUpdate.getId());
                newCorporationInfo.setIsActive(corporationToUpdate.getIsActive());

                corporationService.save(newCorporationInfo);

            } else if (!corporationService.findByName(updateName).isEmpty()) {
                map.put("message", "Virksomhed med dette name " + updateName + " findes allerede ");
                return ResponseEntity.ok(map);

            }
        }

        map.put("message", "corporation updatet, if found ");
        return ResponseEntity.ok(map);
    }


    //ændre status på virksomhed fra 1=aktiv til 0=inaktiv
    @PutMapping("/archiveCorporation")
    public ResponseEntity<Map> archiveCorporation(@RequestParam long corpID) {

        Optional<Corporation> corporation_ = corporationService.findbyId(corpID);

        if (corporation_.isPresent()) {
            Corporation corporationToUpdate = corporation_.get();
            corporationToUpdate.setIsActive(0);

            corporationService.save(corporationToUpdate);
        }
        Map<String, String> map = new HashMap<>();
        map.put("message", "Corporation archived");
        return ResponseEntity.ok(map);
    }

    @GetMapping("/getArchivedCorporations")
    public List<Corporation> getArchivedCorporations() {
        return corporationService.findByIsActive(0);
    }
}

