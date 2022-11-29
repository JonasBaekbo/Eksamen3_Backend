package com.example.eksamen3backend.controller;


import com.example.eksamen3backend.model.ContactPerson;
import com.example.eksamen3backend.model.ContactPersonHistory;
import com.example.eksamen3backend.model.Corporation;
import com.example.eksamen3backend.model.UpdateEntity;
import com.example.eksamen3backend.service.ContactPersonHistoryService;
import com.example.eksamen3backend.service.ContactPersonService;
import com.example.eksamen3backend.service.CorporationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ContactPersonController {

    private ContactPersonService contactPersonService;
    private ContactPersonHistoryService contactPersonHistoryService;
    private CorporationService corporationService;

    public ContactPersonController(ContactPersonService contactPersonService, ContactPersonHistoryService contactPersonHistoryService, CorporationService corporationService) {
        this.contactPersonHistoryService = contactPersonHistoryService;
        this.contactPersonService = contactPersonService;
        this.corporationService = corporationService;

    }

    @PostMapping("/createContactperson")
    public ResponseEntity<String> createUser(@RequestBody ContactPerson contactPerson, @RequestParam Long corpID) {
        String msg = "";
        if (contactPersonService.save(contactPerson) != null) {
            msg = "Kontaktperson oprettet: " + contactPerson.getName();
            addCorpToContactpersonto(contactPerson.getId(), corpID);
        } else {
            msg = "Fejl i oprettelsen af " + contactPerson.getName();
        }
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping("/getAllContactPersons")
    public Set<ContactPerson> getAll (){
        return contactPersonService.findall();
    }

    @DeleteMapping("/deleteContactPerson")
    public ResponseEntity<Map> deleteContactPerson(@RequestBody ContactPerson contactperson){
        System.out.println("deleteUser is called");

        List<ContactPerson> userList = contactPersonService.findByName(contactperson.getName());
        ContactPerson ContactPersonToDelete = userList.get(0);
        contactPersonService.delete(ContactPersonToDelete);
        Map<String,String > map = new HashMap<>();
        map.put("message","user deleted, if found " + contactperson.getName());
        return ResponseEntity.ok(map);
    }

    @PostMapping("/addCorpToContactperson")
    public ResponseEntity<String> addCorpToContactpersonto(@RequestParam Long contactID, @RequestParam Long corpID){
        Optional<ContactPerson> contactPerson_ = contactPersonService.findbyId(contactID);
        Optional<Corporation> corporation_ = corporationService.findbyId(corpID);
        if (contactPerson_.isPresent()){
            if (corporation_.isPresent()){
                Corporation corporation = corporation_.get();

                ContactPerson contactPerson = contactPerson_.get();

                contactPerson.setCorporation(corporation);

                contactPersonService.save(contactPerson);

                return new ResponseEntity<>("Tilføjet kontaktperson:" + contactPerson.getName() + " Til virksomhed: " + corporation.getName(), HttpStatus.OK);
            }
            return new ResponseEntity<>("Kunne ikke finde virksomhed med id: " + corpID, HttpStatus.OK);
        }
        return new ResponseEntity<>("Kunne ikke finde kontaktperson med id: " + contactID, HttpStatus.OK);
    }

    @PutMapping("/updateContactperson")
    public ResponseEntity<Map> updateContactperson(@RequestBody UpdateEntity updateEntity){

        List<ContactPerson> contactpersonList = contactPersonService.findByName(updateEntity.getContactPersonName());
        ContactPerson contactPersonToUpdate = contactpersonList.get(0);
        contactPersonToUpdate.setPhonenumber(updateEntity.getContactPersonPhonenumberToUpdate());
        contactPersonToUpdate.setName(updateEntity.getContactPersonNameToUpdate());
        contactPersonService.save(contactPersonToUpdate);
        Map<String,String > map = new HashMap<>();
        map.put("message","Contactperson updatet, if found " + updateEntity.getContactPersonName());
        return ResponseEntity.ok(map);
    }

}
