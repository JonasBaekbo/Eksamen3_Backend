package com.example.eksamen3backend.controller;


import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.model.Employment;
import com.example.eksamen3backend.service.EmploymentService;
import com.example.eksamen3backend.utilities.UpdateEntity;
import com.example.eksamen3backend.service.ContactPersonService;
import com.example.eksamen3backend.service.CorporationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ContactPersonController {

    private ContactPersonService contactPersonService;
    private CorporationService corporationService;
    private EmploymentService employmentService;

    public ContactPersonController(ContactPersonService contactPersonService, CorporationService corporationService, EmploymentService employmentService) {
        this.contactPersonService = contactPersonService;
        this.corporationService = corporationService;
        this.employmentService = employmentService;
    }

        /* Json format for "/createContactPerson" :
                  {"corpID":1,
                  "name":"ole",
                  "addedToCorporation": "2022-12-10",
                  "movedFromCorporation":null,
                  "phonenumber": 123,
                  "email": "c@d.dk",
                  "position":"sælger"}
    */
        // opretter ny contactPerson og ny employment og knytter de to sammen
        @PostMapping("/createContactPerson")
        public ResponseEntity<List<ContactPerson>> createContactPerson(@RequestBody String Json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(Json);
        JsonNode idNode = rootNode.path("corpID");
        Optional<Corporation> corporation_ = corporationService.findbyId(idNode.asLong());
        JsonNode nameNode=rootNode.path("name");
        ContactPerson contactPerson= new ContactPerson();
        contactPerson.setName(nameNode.asText());
        contactPerson.setIsActive(1);
        contactPersonService.save(contactPerson);
        if ( (corporation_.isPresent())) {
            Employment employment = mapper.readValue(Json, Employment.class);
            employment.setContactPerson(contactPerson);
            employment.setCorporation(corporation_.get());
            employmentService.save(employment);
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

    //sætter en slutdato på en employment.
    @PutMapping("/setEndDateOnEmployment")
    public ResponseEntity<Map> setEndDateOnEmployment(@RequestBody Employment employment, @RequestParam Long empId) {
        Map<String, String> message = new HashMap<>();

        Optional<Employment> employment_ = employmentService.findbyId(empId);
        if (employment_.isPresent()) {
            Employment currentEmployment = employment_.get();
            currentEmployment.setMovedFromCorporation(employment.getMovedFromCorporation());
            employmentService.save(currentEmployment);

            message.put("message", "employment end-date set to " + employment.getMovedFromCorporation());

        } else {

            message.put("message", "no employment was found");
        }
        return ResponseEntity.ok(message);
    }

    // laver ny employment på en kontaktperson, og afslutter den nuværende employment hvis der er en.
    @PostMapping("/makeNewEmployment")
    public ResponseEntity<String> makeNewEmployment(@RequestParam Long contactID, @RequestParam Long corpID, @RequestBody Employment employment) {
        Optional<ContactPerson> contactPerson_ = contactPersonService.findbyId(contactID);
        Optional<Corporation> corporation_ = corporationService.findbyId(corpID);

        if ((contactPerson_.isPresent()) && (corporation_.isPresent())) {
            Corporation corporation = corporation_.get();
            ContactPerson contactPerson = contactPerson_.get();
            //tjekker om der ligger en aktiv employment på kontaktpersonen, hvis der findes en, sættes den til at slutte når den næste starter
            Employment currentEmployment = employmentService.findByContactPersonAndMovedFromCorporationIsNull(contactPerson);
            if (currentEmployment != null) {
                currentEmployment.setMovedFromCorporation(employment.getAddedToCorporation());
                employmentService.save(currentEmployment);
            }

            employment.setCorporation(corporation);
            employment.setContactPerson(contactPerson);

            employmentService.save(employment);

            return new ResponseEntity<>("Tilføjet kontaktperson:" + contactPerson.getName() + " Til virksomhed: " + corporation.getName(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Kunne ikke oprette forbindelse mellem virksomhed og medarbejder", HttpStatus.OK);
    }

    @PutMapping("/updateContactperson")
    public ResponseEntity<Map> updateContactperson(@RequestBody ContactPerson updateEntity, @RequestParam long contId) {

        Optional<ContactPerson> contactPerson_ = contactPersonService.findbyId(contId);

        if (contactPerson_.isPresent()) {
            ContactPerson contactPersonToUpdate = contactPerson_.get();
            contactPersonToUpdate.setName(updateEntity.getName());

            contactPersonService.save(contactPersonToUpdate);
        }
        Map<String, String> map = new HashMap<>();
        map.put("message", "Contactperson updatet, if found " + updateEntity.getName());
        return ResponseEntity.ok(map);
    }


    @GetMapping("/findContactPersonByName")
    public ResponseEntity<ContactPerson> findContactPersonByName(@RequestParam String name) {
        List<ContactPerson> contactPeople = contactPersonService.findByName(name);
        ContactPerson contactPerson = contactPeople.get(0);

        return new ResponseEntity<>(contactPerson, HttpStatus.OK);
    }

    @GetMapping("/findContactPersonById")
    public ResponseEntity<ContactPerson> findContactPersonById(@RequestParam long contId) {
        Optional<ContactPerson> contactPerson_ = contactPersonService.findbyId(contId);
        if (contactPerson_.isPresent()) {
            ContactPerson contactPerson = contactPerson_.get();
            return new ResponseEntity<>(contactPerson, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    @PutMapping("/archiveContact")
    public ResponseEntity<Map> archiveContact(@RequestParam long contId) {

        Optional<ContactPerson> contactPerson_ = contactPersonService.findbyId(contId);

        if (contactPerson_.isPresent()) {
            ContactPerson contactPersonToUpdate = contactPerson_.get();
            contactPersonToUpdate.setIsActive(0);

            contactPersonService.save(contactPersonToUpdate);
        }
        Map<String, String> map = new HashMap<>();
        map.put("message", "Contactperson archived, if found ");
        return ResponseEntity.ok(map);
    }
    @GetMapping("/getArchivedContactPersons")
    public List<ContactPerson> getArchivedContactPersons(){
        return contactPersonService.findByIsActive(0);
    }

}


