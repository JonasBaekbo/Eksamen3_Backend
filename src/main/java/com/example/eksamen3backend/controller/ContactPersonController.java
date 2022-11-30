package com.example.eksamen3backend.controller;


import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.model.Employment;
import com.example.eksamen3backend.service.EmploymentService;
import com.example.eksamen3backend.utilities.UpdateEntity;
import com.example.eksamen3backend.service.ContactPersonService;
import com.example.eksamen3backend.service.CorporationService;
import com.fasterxml.jackson.core.JsonProcessingException;
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


/* Json format for /createContactperson" :
              "name":"ole",
              "addedToCorporation": "2022-12-10",
              "movedFromCorporation":null,
              "phonenumber": 123,
              "email": "c@d.dk",
              "position":"sælger"
*/
    @PostMapping("/createContactperson")
    public ResponseEntity<String> createUser(@RequestBody String pureJason, @RequestParam Long corpID) throws JsonProcessingException {
        String msg = "";
        Optional<Corporation> corporation_ = corporationService.findbyId(corpID);
        ObjectMapper mapper = new ObjectMapper();
        ContactPerson contactPerson = mapper.readValue(pureJason, ContactPerson.class);
        if ((contactPersonService.save(contactPerson) != null) && (corporation_.isPresent())) {
            Employment employment = mapper.readValue(pureJason, Employment.class);
            employment.setContactPerson(contactPerson);
            employment.setCorporation(corporation_.get());
            employmentService.save(employment);
            msg = "Kontaktperson oprettet: " + contactPerson.getName();
            addCorpToContactPerson(contactPerson.getId(), corpID);
        } else {
            msg = "Fejl i oprettelsen af " + contactPerson.getName();
        }
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping("/getAllContactPersons")
    public Set<ContactPerson> getAll() {
        return contactPersonService.findall();
    }

    @DeleteMapping("/deleteContactPerson")
    public ResponseEntity<Map> deleteContactPerson(@RequestBody ContactPerson contactperson) {
        System.out.println("deleteUser is called");

        List<ContactPerson> userList = contactPersonService.findByName(contactperson.getName());
        ContactPerson ContactPersonToDelete = userList.get(0);
        contactPersonService.delete(ContactPersonToDelete);
        Map<String, String> map = new HashMap<>();
        map.put("message", "user deleted, if found " + contactperson.getName());
        return ResponseEntity.ok(map);
    }

    @PostMapping("/addCorpToContactperson")
    public ResponseEntity<String> addCorpToContactPerson(@RequestParam Long contactID, @RequestParam Long corpID) {
        Optional<ContactPerson> contactPerson_ = contactPersonService.findbyId(contactID);
        Optional<Corporation> corporation_ = corporationService.findbyId(corpID);
        if (contactPerson_.isPresent()) {
            if (corporation_.isPresent()) {
                Corporation corporation = corporation_.get();

                ContactPerson contactPerson = contactPerson_.get();

                //contactPerson.setCorporation(corporation);

                contactPersonService.save(contactPerson);

                return new ResponseEntity<>("Tilføjet kontaktperson:" + contactPerson.getName() + " Til virksomhed: " + corporation.getName(), HttpStatus.OK);
            }
            return new ResponseEntity<>("Kunne ikke finde virksomhed med id: " + corpID, HttpStatus.OK);
        }
        return new ResponseEntity<>("Kunne ikke finde kontaktperson med id: " + contactID, HttpStatus.OK);
    }

    @PutMapping("/updateContactperson")
    public ResponseEntity<Map> updateContactperson(@RequestBody UpdateEntity updateEntity) {

        List<ContactPerson> contactpersonList = contactPersonService.findByName(updateEntity.getContactPersonName());
        ContactPerson contactPersonToUpdate = contactpersonList.get(0);
        Employment currentEmployment = employmentService.findByContactPersonAndMovedFromCorporationIsNull(contactPersonToUpdate);

        if (updateEntity.getContactPersonNameToUpdate() != null) {
            contactPersonToUpdate.setName(updateEntity.getContactPersonNameToUpdate());
        }
        if (updateEntity.getContactPersonPhonenumberToUpdate() != 0) {
            currentEmployment.setPhonenumber(updateEntity.getContactPersonPhonenumberToUpdate());
            //contactPersonToUpdate.setPhonenumber(updateEntity.getContactPersonPhonenumberToUpdate());
        }
        if (updateEntity.getContactPersonEmailToUpdate() != null) {
            currentEmployment.setEmail(updateEntity.getContactPersonEmailToUpdate());
            //contactPersonToUpdate.setEmail(updateEntity.getContactPersonEmailToUpdate());
        }
        if (updateEntity.getContactPersonpositionToUpdate() != null) {
            currentEmployment.setPosition(updateEntity.getContactPersonpositionToUpdate());
            //contactPersonToUpdate.setPosition(updateEntity.getContactPersonpositionToUpdate());
        }


        contactPersonService.save(contactPersonToUpdate);
        Map<String, String> map = new HashMap<>();
        map.put("message", "Contactperson updatet, if found " + updateEntity.getContactPersonName());
        return ResponseEntity.ok(map);
    }

    /*@PostMapping("/createEmploymentContact")
    public ResponseEntity<String> createEmploymentContact(@RequestBody EmplymentContract emplymentContract, @RequestParam Long corpID, @RequestParam Long contactPersonID) {
        String msg = "";
        ContactPerson contactPerson = new ContactPerson(emplymentContract.getNameContactPerson());
        System.out.println(emplymentContract.getNameContactPerson());
        Employment employment = new Employment(emplymentContract.getEmailEmployment(), emplymentContract.getPhonenumberEmployment(),
                emplymentContract.getPositionEmployment(), emplymentContract.getAddedToCorporationEmployment());
        if (contactPerson.getName() != null) {
            if (employment.getEmail() != null) {
                createContactperson(contactPerson, corpID);
                createEmployment(employment,corpID,contactPersonID);
                msg = "Kontaktperson oprettet: " + contactPerson.getName();
            } else {
                msg = "Fejl i oprettelsen af employment";
            }
        }else{
            msg = "Fejl i oprettelsen af " + contactPerson.getName();
        }
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PostMapping("/createEmployment")
    public ResponseEntity<String> createEmployment(@RequestBody Employment employment, @RequestParam Long corpID,@RequestParam Long contactPersonID) {
        String msg = "";
        if (employmentService.save(employment) != null) {
            addCorpAndContactPersonToEmployment(employment.getId(), corpID, contactPersonID);
            msg = "Employment oprettet";
        } else {
            msg = "Fejl i oprettelsen af Employment";
        }
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PostMapping("/addCorpAndContactPersonToEmployment")
    public ResponseEntity<String> addCorpAndContactPersonToEmployment(@RequestParam Long employmentId, @RequestParam Long corpID, @RequestParam Long contactID) {
        Optional<Employment> employment_ = employmentService.findbyId(employmentId);
        Optional<ContactPerson> contactPerson_ = contactPersonService.findbyId(contactID);
        Optional<Corporation> corporation_ = corporationService.findbyId(corpID);
        if (employment_.isPresent()) {
            if (contactPerson_.isPresent()) {
                if (corporation_.isPresent()) {
                    Corporation corporation = corporation_.get();

                    ContactPerson contactPerson = contactPerson_.get();

                    Employment employment = employment_.get();

                    employment.setContactPerson(contactPerson);

                    employment.setCorporation(corporation);

                    employmentService.save(employment);

                    return new ResponseEntity<>("Tilføjet kontaktperson:" + contactPerson.getName() + " Til virksomhed: " + corporation.getName(), HttpStatus.OK);
                }
                return new ResponseEntity<>("Kunne ikke finde virksomhed med id: " + corpID, HttpStatus.OK);
            }
            return new ResponseEntity<>("Kunne ikke finde kontaktperson med id: " + contactID, HttpStatus.OK);
        }
        return new ResponseEntity<>("Kunne ikke finde employment med id: " + employmentId, HttpStatus.OK);
    }*/
}
