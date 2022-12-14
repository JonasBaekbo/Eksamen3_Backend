package com.example.eksamen3backend.controller;

import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.model.Photo;
import com.example.eksamen3backend.service.CorporationService;
import com.example.eksamen3backend.service.PhotoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CorporationController {

    private final CorporationService corporationService;
    private final PhotoService photoService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CorporationController(CorporationService corporationService, PhotoService photoService) {
        this.corporationService = corporationService;
        this.photoService = photoService;
    }
    @Operation(description = """
                     Opdatering af virksomhed\n
                     Example requestBody: \n
                   {"name":"Firma navn",\n
                    "address":"Gadevej 199",\n
                    "city":"København",\n
                    "country":"Danmark",\n
                    "logo":""}""")
    @PostMapping("/createCorporation")
    public ResponseEntity<List<Corporation>> createCorporation(@RequestBody String jsonString) throws JsonProcessingException {

        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode nameNode = rootNode.path("name");
        String corporationName = nameNode.asText();


        if (corporationService.findByName(corporationName).isEmpty()) {
            Corporation corporation = objectMapper.readValue(jsonString, Corporation.class);
            JsonNode logoNode = rootNode.path("logo");
            Photo logo = photoService.createPhoto(logoNode.asText());
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

@Operation(description ="Finder en virksomhed via corpID")
    @GetMapping("/findCorporationById")
    public ResponseEntity<Corporation> findCorporationById(@RequestParam long corpID) {
        Optional<Corporation> corporation_ = corporationService.findbyId(corpID);
        if (corporation_.isPresent()) {
            Corporation corporation = corporation_.get();
            return new ResponseEntity<>(corporation, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(description ="Finder alle virksomheder med status 1=aktiv")
    @GetMapping("/showCorporations")
    public List<Corporation> showAll() {
        return corporationService.findByIsActiveOrderByNameAsc(1);
    }

    @Operation(description ="Finder alle virksomheder med navn der matcher søgning og med status 1=aktiv")
    @GetMapping("/findActiveCorporationContaining")
    public ResponseEntity<List<Corporation>> findActiveCorporationContaining(@RequestParam String name) {
        List<Corporation> corporations = corporationService.findByIsActiveAndNameContainingOrderByNameAsc(1, name);

        return new ResponseEntity<>(corporations, HttpStatus.OK);
    }
    @Operation(description ="Finder alle virksomheder med navn der matcher søgning og med status o=inaktiv")
    @GetMapping("/findInactiveCorporationContaining")
    public ResponseEntity<List<Corporation>> findInactiveCorporationContaining(@RequestParam String name) {
        List<Corporation> corporations = corporationService.findByIsActiveAndNameContainingOrderByNameAsc(0, name);

        return new ResponseEntity<>(corporations, HttpStatus.OK);
    }

    @Operation(description = """
                    Opdatering af virksomhedsoplysninger\n
                    Example requestBody: \n
                   {"name":"Firma navn",\n
                    "address":"Gadevej 199",\n
                    "city":"København",\n
                    "country":"Danmark",\n
                    "logo":""}""")
    @PutMapping("/updateCorporation")
    public ResponseEntity<Map> updateCorporation(@RequestBody String jsonString, @RequestParam long corpID) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        Optional<Corporation> corporation_ = corporationService.findbyId(corpID);

        if (corporation_.isPresent()) {
            Corporation corporationToUpdate = corporation_.get();

            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode nodeName = rootNode.path("name");
            String updateName = nodeName.asText();

            if (corporationToUpdate.getName().equals(updateName) || corporationService.findByName(updateName).isEmpty()) {
                Corporation newCorporationInfo = objectMapper.readValue(jsonString, Corporation.class);
                JsonNode nodeLogo = rootNode.path("logo");
                String nodeLogoAsString = nodeLogo.asText();
                Photo currentLogo = corporationToUpdate.getLogo();
                // Sætter logoet til at være det samme, hvis der ikke er uploadet et nyt
                if (nodeLogoAsString.equals("data:") || currentLogo.getImageString().equals(nodeLogoAsString)) {
                    //currentLogo = photoService.createPhoto(nodeLogoAsString);
                    newCorporationInfo.setLogo(currentLogo);

                //Ændre logo hvis der er uploadet et nyt
                }else if (!(nodeLogoAsString.equals("data:") || currentLogo.getImageString().equals(nodeLogoAsString))){
                    Photo newLogo = photoService.createPhoto(nodeLogoAsString);
                newCorporationInfo.setLogo(newLogo);
                }
                newCorporationInfo.setEmployments(corporationToUpdate.getEmployments());
                newCorporationInfo.setId(corporationToUpdate.getId());
                newCorporationInfo.setIsActive(corporationToUpdate.getIsActive());

                corporationService.save(newCorporationInfo);

            } else if (!corporationService.findByName(updateName).isEmpty()) {
                map.put("message", "Virksomhed med dette name " + updateName + " findes allerede ");
                return ResponseEntity.ok(map);
            }
        }
        map.put("message", "Virksomhed opdateret ");
        return ResponseEntity.ok(map);
    }

    @Operation(description = "Ændre status på virksomhed fra 1=aktiv til 0=inaktiv")
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
    @Operation(description = "Finder alle virksomheder med status 0=inaktiv")
    @GetMapping("/getArchivedCorporations")
    public List<Corporation> getArchivedCorporations() {
        return corporationService.findByIsActiveOrderByNameAsc(0);
    }
}

