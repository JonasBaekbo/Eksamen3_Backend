package com.example.eksamen3backend.controller;


import com.example.eksamen3backend.model.*;
import com.example.eksamen3backend.service.EmploymentService;
import com.example.eksamen3backend.service.ContactPersonService;
import com.example.eksamen3backend.service.CorporationService;
import com.example.eksamen3backend.service.PhotoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@RestController
public class ContactPersonController {

    private final ContactPersonService contactPersonService;
    private final CorporationService corporationService;
    private final EmploymentService employmentService;
    private final PhotoService photoService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ContactPersonController(ContactPersonService contactPersonService, CorporationService corporationService, EmploymentService employmentService, PhotoService photoService) {
        this.contactPersonService = contactPersonService;
        this.corporationService = corporationService;
        this.employmentService = employmentService;
        this.photoService = photoService;
    }

    @Operation(description = """
            Opretter ny contactPerson og ny employment og knytter de to sammen.
            \n
            Example requestBody: \n
            {"corpID":1,\n
              "name":"ole",\n
              "addedToCorporation": "2022-12-10",\n
              "movedFromCorporation":"null",\n
              "CPimage:"",\n
              "phonenumber": 12345678,\n
              "email": "ab@cd.dk",\n
              "position":"marketing"\n
            }
            """)
    @PostMapping("/createContactPerson")
    public ResponseEntity<List<ContactPerson>> createContactPerson(@RequestBody String jsonString) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode idNode = rootNode.path("corpID");
        Optional<Corporation> corporation_ = corporationService.findbyId(idNode.asLong());
        JsonNode nameNode = rootNode.path("name");
        ContactPerson contactPerson = new ContactPerson();
        contactPerson.setName(nameNode.asText());
        contactPerson.setIsActive(1);
        JsonNode imageNode = rootNode.path("CPimage");
        Photo photo = photoService.createPhoto(imageNode.asText());
        contactPerson.setCPimage(photo);
        contactPersonService.save(contactPerson);
        if ((corporation_.isPresent())) {
            Employment employment = objectMapper.readValue(jsonString, Employment.class);
            employmentService.makeEmployment(contactPerson, corporation_.get(), employment);
            System.out.println("Kontaktperson " + contactPerson.getName() + "oprettet");
        } else {
            System.out.println("Fejl i oprettelsen af " + contactPerson.getName());
        }
        return new ResponseEntity<>(getAll(), HttpStatus.OK);
    }

    @Operation(description = "Finder alle kontaktpersoner med status 1=aktiv")
    @GetMapping("/getAllContactPersons")
    public List<ContactPerson> getAll() {
        return contactPersonService.findByIsActiveOrderByNameAsc(1);
    }

    @Operation(description = """
            Opdatering af alle data på en kontaktperson og deres nuværende employment. Der oprettes en ny employment, hvis corpID ændre sig
            \n
            Example requestBody: \n
            {"corpID":1,\n
              "name":"ole",\n
              "addedToCorporation": "2022-12-10",\n
              "movedFromCorporation":"null",\n
              "CPimage:"",\n
              "phonenumber": 12345678,\n
              "email": "ab@cd.dk",\n
              "position":"marketing"\n
            }
            """)
    @PutMapping("/updateContactperson")
    public ResponseEntity<Map> updateContactperson(@RequestBody String jsonString, @RequestParam long contactID) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        Optional<ContactPerson> contactPerson_ = contactPersonService.findbyId(contactID);
        JsonNode rootNode = objectMapper.readTree(jsonString);

        if (contactPerson_.isPresent()) {
            ContactPerson contactPersonToUpdate = contactPerson_.get();
            JsonNode nodeName = rootNode.path("name");
            contactPersonToUpdate.setName(nodeName.asText());

            Photo currentImage = contactPersonToUpdate.getCPimage();
            JsonNode nodeImage = rootNode.path("CPimage");
            String nodeImageAsString = nodeImage.asText();
            // Sætter logoet til at være det samme, hvis der ikke er uploadet et nyt
            if (nodeImageAsString.equals("data:") || currentImage.getImageString().equals(nodeImageAsString)) {
                //currentLogo = photoService.createPhoto(nodeLogoAsString);
                contactPersonToUpdate.setCPimage(currentImage);

                //Ændre logo hvis der er uploadet et nyt
            }else if (!(nodeImageAsString.equals("data:") || currentImage.getImageString().equals(nodeImageAsString))){
                Photo newImage = photoService.createPhoto(nodeImageAsString);
                contactPersonToUpdate.setCPimage(newImage);
            }
            JsonNode corpIDNode = rootNode.path("corpID");
            Optional<Corporation> corporation_ = corporationService.findbyId(corpIDNode.asLong());
            //Tjekker om der skal ændres i employment
            if (corporation_.isPresent()) {
                Employment employment = objectMapper.readValue(jsonString, Employment.class);
                Corporation corporation = corporation_.get();

                //Optional<Employment> currentEmployment_ = Optional.ofNullable(employmentService.findByContactPersonAndMovedFromCorporationIsNull(contactPersonToUpdate));
                Optional<Employment> currentEmployment_ = Optional.ofNullable(contactPersonToUpdate.currentEmployments().iterator().next());
                if (currentEmployment_.isPresent()) {
                    employmentService.editEmployment(currentEmployment_.get(), employment, corporation);
                } else {
                    employmentService.makeEmployment(contactPersonToUpdate, corporation, employment);
                }
                contactPersonService.save(contactPersonToUpdate);

                map.put("message", "Opdaterede kontaktperson:" + contactPersonToUpdate.getName());
            } else {
                map.put("message", "Kunne ikke opdatere kontaktperson: " + contactPersonToUpdate.getName());
            }
        }
        return ResponseEntity.ok(map);
    }

    @Operation(description = "Søger i kontaktpersoner der har navne der matcher søgning og har status 1=aktiv")
    @GetMapping("/findActiveContactPersonContaining")
    public ResponseEntity<List<ContactPerson>> findActiveContactPersonContaining(@RequestParam String name) {
        List<ContactPerson> contactPeople = contactPersonService.findByIsActiveAndNameContainingOrderByNameAsc(1, name);
        return new ResponseEntity<>(contactPeople, HttpStatus.OK);
    }

    @Operation(description = "Søger i kontaktpersoner der har navne der matcher søgning og har status 0=inaktiv")
    @GetMapping("/findInactiveContactPersonContaining")
    public ResponseEntity<List<ContactPerson>> findInactiveContactPersonContaining(@RequestParam String name) {
        List<ContactPerson> contactPeople = contactPersonService.findByIsActiveAndNameContainingOrderByNameAsc(0, name);
        return new ResponseEntity<>(contactPeople, HttpStatus.OK);
    }

    @Operation(description = "Søger i kontaktpersoner ud fra contactID")
    @GetMapping("/findContactPersonById")
    public ResponseEntity<ContactPerson> findContactPersonById(@RequestParam long contactID) {
        Optional<ContactPerson> contactPerson_ = contactPersonService.findbyId(contactID);
        if (contactPerson_.isPresent()) {
            ContactPerson contactPerson = contactPerson_.get();
            return new ResponseEntity<>(contactPerson, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @Operation(description = "Ændre status på kontaktperson fra 1=aktiv til 0=inaktiv")
    @PutMapping("/archiveContact")
    public ResponseEntity<Map> archiveContact(@RequestParam long contactID) {

        Optional<ContactPerson> contactPerson_ = contactPersonService.findbyId(contactID);
        Map<String, String> map = new HashMap<>();
        if (contactPerson_.isPresent()) {
            ContactPerson contactPersonToUpdate = contactPerson_.get();
            Employment employment_ = employmentService.findByContactPersonAndMovedFromCorporationIsNull(contactPersonToUpdate);

            contactPersonToUpdate.setIsActive(0);

            employment_.setMovedFromCorporation(Timestamp.valueOf(LocalDateTime.now()));

            contactPersonService.save(contactPersonToUpdate);
            employmentService.save(employment_);

            map.put("message", "Kontaktperson er arkiveret");
            return ResponseEntity.ok(map);
        } else {
            map.put("message", "Kontaktperson kunne ikke arkiveret");
            return ResponseEntity.ok(map);
        }
    }

    @Operation(description = "Finder alle kontaktpersoner med status 0=inaktiv")
    @GetMapping("/getArchivedContactPersons")
    public List<ContactPerson> getArchivedContactPersons() {
        return contactPersonService.findByIsActiveOrderByNameAsc(0);
    }

}


