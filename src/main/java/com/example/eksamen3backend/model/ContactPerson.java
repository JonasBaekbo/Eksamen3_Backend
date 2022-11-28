package com.example.eksamen3backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class ContactPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private int phonenumber;

    @ManyToOne
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private Corporation corporation;


    public ContactPerson() {
        super();
    }
}
