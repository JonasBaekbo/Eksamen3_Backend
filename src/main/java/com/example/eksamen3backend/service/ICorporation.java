package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.Corporation;

import java.util.List;
import java.util.Set;

public interface ICorporation extends CrudService<Corporation, Long> {

    List<Corporation> findByName(String name);
}
