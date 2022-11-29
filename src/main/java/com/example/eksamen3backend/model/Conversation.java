package com.example.eksamen3backend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String subject;
    private String summary;

    @ManyToOne
    @JoinColumn(name = "contactPerson_id")
    private ContactPerson contactPerson;

    @ManyToOne
    @JoinColumn(name = "corporation_id")
    private Corporation corporation;

    public Conversation(){
      super();
    }
}