package com.example.eksamen3backend.controller;

import com.example.eksamen3backend.model.Corporation;
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
    public ResponseEntity<List<Corporation>> createCorporation(@RequestBody Corporation corporation) {

        if(corporationService.findByName(corporation.getName()).isEmpty()) {

            corporation.setIsActive(1);
            corporationService.save(corporation);

            if (corporationService.save(corporation) != null) {
                System.out.println("Virksomhed oprettet: " + corporation.getName());
            } else {
                System.out.println("Fejl i oprettelse af: " + corporation.getName());
            }
        }else{
            System.out.println("Virksohed med dette name "+corporation.getName() + " findes allreded");
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
    public ResponseEntity<Corporation> findContactPersonById(@RequestParam long corId) {
        Optional<Corporation> corporation_ = corporationService.findbyId(corId);
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
    public ResponseEntity<Map> updateContactperson(@RequestBody Corporation updateEntity, @RequestParam long corpId) {
        Optional<Corporation> corporation_ = corporationService.findbyId(corpId);

//        List<Corporation> corpList = corporationService.findByName(updateEntity.getCorpName());
//        Corporation corporationToUpdate = corpList.get(0);
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
    @PutMapping("/archiveCorporation")
    public ResponseEntity<Map> archiveCorporation(@RequestParam long corpId) {

        Optional<Corporation> corporation_ = corporationService.findbyId(corpId);

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

