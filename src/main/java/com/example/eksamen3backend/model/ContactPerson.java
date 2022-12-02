package com.example.eksamen3backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "contact_person")
@JsonIgnoreProperties(value = {"addedToCorporation","movedFromCorporation","phonenumber","email", "position","corpID"})
public class ContactPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "contactperson_name")
    private String name;


    @OneToMany(mappedBy = "contactPerson")
    @JsonBackReference("contactPersonConversation")
    private Set<Conversation> conversations = new HashSet<>();

    public ContactPerson() {
        super();
    }

}
