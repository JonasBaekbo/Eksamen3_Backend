package com.example.eksamen3backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Corporation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;


    @OneToMany
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private Set<ContactPerson> contactPerson = new HashSet<>();

    //@ManyToOne(mappedBy = "corporation")
    //private Set<ContactPerson> contactPerson = new HashSet<>();

    /*@OneToMany(mappedBy = "corporation")
    private Set<Conversation> conversations = new HashSet<>();*/


}
