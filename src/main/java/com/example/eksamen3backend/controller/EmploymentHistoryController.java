package com.example.eksamen3backend.controller;

import com.example.eksamen3backend.service.EmploymentHistoryService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmploymentHistoryController {


    private EmploymentHistoryService employmentHistoryService;

    public EmploymentHistoryController(EmploymentHistoryService employmentHistoryService){
        this.employmentHistoryService = employmentHistoryService;
    }
}
