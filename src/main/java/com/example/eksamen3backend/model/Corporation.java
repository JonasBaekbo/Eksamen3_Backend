package com.example.eksamen3backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "corporation")
public class Corporation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "Corporation_name")
    private String name;

    @Column(name = "corporation_address")
    private String address;

    @Column(name = "corporation_logo")
    private String logo;

    @Column(name ="corporation_city")
    private String city;

    @Column(name = "corporation_country")
    private String country;


    @OneToMany(mappedBy = "corporation")
    @JsonBackReference("contactPerson")
    @JsonManagedReference
    private Set<ContactPerson> contactPerson = new HashSet<>();

    @OneToMany(mappedBy = "corporation")
    @JsonBackReference("conversation")
    private Set<Conversation> conversations = new HashSet<>();

}
