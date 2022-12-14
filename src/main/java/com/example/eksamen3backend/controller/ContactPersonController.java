package com.example.eksamen3backend.controller;


import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.model.Employment;
import com.example.eksamen3backend.model.Photo;
import com.example.eksamen3backend.service.EmploymentService;
import com.example.eksamen3backend.service.ContactPersonService;
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
import java.util.List;

@RestController
public class ContactPersonController {

    private ContactPersonService contactPersonService;
    private CorporationService corporationService;
    private EmploymentService employmentService;
    private PhotoService photoService;
    private ObjectMapper objectMapper = new ObjectMapper();

    public ContactPersonController(ContactPersonService contactPersonService, CorporationService corporationService, EmploymentService employmentService, PhotoService photoService) {
        this.contactPersonService = contactPersonService;
        this.corporationService = corporationService;
        this.employmentService = employmentService;
        this.photoService = photoService;
    }

    /* Json format for "/createContactPerson" :
                                  {"corpID":1,
                                  "name":"ole",
                                  "addedToCorporation": "2022-12-10",
                                  "movedFromCorporation":null,
                                  "CPimage:""
                                  "phonenumber": 123,
                                  "email": "c@d.dk",
                                  "position":"sælger"}
                    */
    // opretter ny contactPerson og ny employment og knytter de to sammen
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
            System.out.println("Kontaktperson oprettet: " + contactPerson.getName());
        } else {
            System.out.println("Fejl i oprettelsen af " + contactPerson.getName());
        }
        return new ResponseEntity<>(getAll(), HttpStatus.OK);
    }


    @GetMapping("/getAllContactPersons")
    public List<ContactPerson> getAll() {
        return contactPersonService.findByIsActive(1);
    }


    //kan opdatere alle data på en kontaktperson, samt oprette ny employment, hvis corpID ændre sig
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
            //tjekker om der er ændringer i billede
            if (!(nodeImageAsString.equals("null") || nodeImageAsString.isEmpty() || currentImage.getImageString().equals(nodeImageAsString))) {
                photoService.createPhoto(nodeImageAsString);
            }
            JsonNode corpIDNode = rootNode.path("corpID");
            Optional<Corporation> corporation_ = corporationService.findbyId(corpIDNode.asLong());
            //Tjekker om der skal ændres i employment
            if (corporation_.isPresent()) {
                Employment employment = objectMapper.readValue(jsonString, Employment.class);
                Corporation corporation = corporation_.get();

                Optional<Employment> currentEmployment_ = Optional.ofNullable(employmentService.findByContactPersonAndMovedFromCorporationIsNull(contactPersonToUpdate));
                if (currentEmployment_.isPresent()) {
                    employmentService.editEmployment(currentEmployment_.get(), employment, corporation);
                } else {
                    employmentService.makeEmployment(contactPersonToUpdate, corporation, employment);
                }
                contactPersonService.save(contactPersonToUpdate);

                map.put("message", "Opdaterede kontaktperson:" + contactPersonToUpdate.getName());
            } else {
                map.put("message", "Kunne ikke oprette forbindelse mellem virksomhed og medarbejder");
            }
        }
        return ResponseEntity.ok(map);
    }

    @GetMapping("/findActiveContactPersonContaining")
    public ResponseEntity<List<ContactPerson>> findActiveContactPersonContaining(@RequestParam String name) {
        List<ContactPerson> contactPeople = contactPersonService.findByIsActiveAndNameContainingOrderByNameAsc(1, name);
        return new ResponseEntity<>(contactPeople, HttpStatus.OK);
    }

    @GetMapping("/findInactiveContactPersonContaining")
    public ResponseEntity<List<ContactPerson>> findInactiveContactPersonContaining(@RequestParam String name) {
        List<ContactPerson> contactPeople = contactPersonService.findByIsActiveAndNameContainingOrderByNameAsc(0, name);
        return new ResponseEntity<>(contactPeople, HttpStatus.OK);
    }

    @GetMapping("/findContactPersonByName")
    public ResponseEntity<ContactPerson> findContactPersonByName(@RequestParam String name) {
        List<ContactPerson> contactPeople = contactPersonService.findByName(name);
        ContactPerson contactPerson = contactPeople.get(0);
        return new ResponseEntity<>(contactPerson, HttpStatus.OK);
    }

    @GetMapping("/findContactPersonById")
    public ResponseEntity<ContactPerson> findContactPersonById(@RequestParam long contactID) {
        Optional<ContactPerson> contactPerson_ = contactPersonService.findbyId(contactID);
        if (contactPerson_.isPresent()) {
            ContactPerson contactPerson = contactPerson_.get();
            return new ResponseEntity<>(contactPerson, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/archiveContact")
    public ResponseEntity<Map> archiveContact(@RequestParam long contactID) {

        Optional<ContactPerson> contactPerson_ = contactPersonService.findbyId(contactID);

        if (contactPerson_.isPresent()) {
            ContactPerson contactPersonToUpdate = contactPerson_.get();
            Employment employment_ = employmentService.findByContactPersonAndMovedFromCorporationIsNull(contactPersonToUpdate);

            contactPersonToUpdate.setIsActive(0);

            employment_.setMovedFromCorporation(Timestamp.valueOf(LocalDateTime.now()));

            contactPersonService.save(contactPersonToUpdate);
            employmentService.save(employment_);

        }
        Map<String, String> map = new HashMap<>();
        map.put("message", "Contactperson archived, if found ");
        return ResponseEntity.ok(map);
    }

    @GetMapping("/getArchivedContactPersons")
    public List<ContactPerson> getArchivedContactPersons() {
        return contactPersonService.findByIsActive(0);
    }

}


