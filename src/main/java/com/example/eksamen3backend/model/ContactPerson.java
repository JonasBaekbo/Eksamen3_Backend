package com.example.eksamen3backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "contact_person")
public class ContactPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "contactperson_name")
    private String name;
    @Column(name = "phone_number")
    private int phonenumber;

    @ManyToOne
    @JoinColumn(name = "corporation_id" , nullable = false)
    private Corporation corporation;


    public ContactPerson() {
        super();
    }
}
