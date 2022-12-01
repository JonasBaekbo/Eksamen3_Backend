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


    /* Json format for "/createContactPerson" :
                  {"name":"ole",
                  "addedToCorporation": "2022-12-10",
                  "movedFromCorporation":null,
                  "phonenumber": 123,
                  "email": "c@d.dk",
                  "position":"sælger"}
    */
    // opretter ny contactPerson og ny employment og knytter de to sammen
    @PostMapping("/createContactPerson")
    public ResponseEntity<Set<ContactPerson>> createContactPerson(@RequestBody String Json, @RequestParam Long corpID) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Optional<Corporation> corporation_ = corporationService.findbyId(corpID);
        ContactPerson contactPerson = mapper.readValue(Json, ContactPerson.class);
        if ((contactPersonService.save(contactPerson) != null) && (corporation_.isPresent())) {
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
    public Set<ContactPerson> getAll() {
        return contactPersonService.findall();
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

    // laver ny employment på en kontaktperson, og afslutter den nuværende hvis der er noget.
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
    public ResponseEntity<Map> updateContactperson(@RequestBody UpdateEntity updateEntity) {

        List<ContactPerson> contactpersonList = contactPersonService.findByName(updateEntity.getContactPersonName());
        ContactPerson contactPersonToUpdate = contactpersonList.get(0);
        Employment currentEmployment = employmentService.findByContactPersonAndMovedFromCorporationIsNull(contactPersonToUpdate);

        if (updateEntity.getContactPersonNameToUpdate() != null) {
            contactPersonToUpdate.setName(updateEntity.getContactPersonNameToUpdate());
        }
        if (updateEntity.getContactPersonPhonenumberToUpdate() != 0) {
            currentEmployment.setPhonenumber(updateEntity.getContactPersonPhonenumberToUpdate());
        }
        if (updateEntity.getContactPersonEmailToUpdate() != null) {
            currentEmployment.setEmail(updateEntity.getContactPersonEmailToUpdate());
        }
        if (updateEntity.getContactPersonpositionToUpdate() != null) {
            currentEmployment.setPosition(updateEntity.getContactPersonpositionToUpdate());
        }


        contactPersonService.save(contactPersonToUpdate);
        Map<String, String> map = new HashMap<>();
        map.put("message", "Contactperson updatet, if found " + updateEntity.getContactPersonName());
        return ResponseEntity.ok(map);
    }
}
/*

    @PostMapping("/addCorpToContactperson")
    public ResponseEntity<String> addCorpToContactPerson(@RequestParam Long contactID, @RequestParam Long corpID) {
        Optional<ContactPerson> contactPerson_ = contactPersonService.findbyId(contactID);
        Optional<Corporation> corporation_ = corporationService.findbyId(corpID);
        if (contactPerson_.isPresent()) {
            if (corporation_.isPresent()) {
                Corporation corporation = corporation_.get();

                ContactPerson contactPerson = contactPerson_.get();


                contactPersonService.save(contactPerson);

                return new ResponseEntity<>("Tilføjet kontaktperson:" + contactPerson.getName() + " Til virksomhed: " + corporation.getName(), HttpStatus.OK);
            }
            return new ResponseEntity<>("Kunne ikke finde virksomhed med id: " + corpID, HttpStatus.OK);
        }
        return new ResponseEntity<>("Kunne ikke finde kontaktperson med id: " + contactID, HttpStatus.OK);
    }

   @PostMapping("/createEmploymentContact")
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
    }*/


