package com.example.eksamen3backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@JsonIgnoreProperties(value = { "contactID", "corporationName" })
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String subject;
    private String summary;
    private Date date;
    private String employee;

    @ManyToOne
    @JoinColumn(name = "contactPerson_id")
    private ContactPerson contactPerson;

    @ManyToOne
    @JoinColumn(name = "corporation_id")
    private Corporation corporation;

    public Conversation(){
      super();
    }

    @JsonManagedReference("currentContactPerson")
    public String currentContactPerson() {
        return contactPerson.getName();
    }

    @JsonManagedReference("currentCorporation")
    public String currentCorporation() {
        return corporation.getName();
    }

}
