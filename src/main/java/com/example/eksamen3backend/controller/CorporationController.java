package com.example.eksamen3backend.controller;

import com.example.eksamen3backend.service.CorporationService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CorporationController {

    private CorporationService corporationService;

    public CorporationController(CorporationService corporationService){
        this.corporationService = corporationService;
    }

}
