package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.Corporation;

import java.util.List;

public interface ICorporation extends CrudService<Corporation, Long> {

    List<Corporation> findByName(String name);
}
