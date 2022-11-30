package com.example.eksamen3backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.sql.Timestamp;


@Getter
@Setter
@Entity
@JsonIgnoreProperties(value = { "id", "contactPerson","name" })
public class Employment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "corporation_id")
    @JsonBackReference("corporation")
    @EqualsAndHashCode.Exclude
    private Corporation corporation;

    @ManyToOne
    @JoinColumn(name = "contact_person_id")
    private ContactPerson contactPerson;

    private Timestamp addedToCorporation;

    private Timestamp movedFromCorporation;

    @Column(name = "phone_number")
    private int phonenumber;

    @Column(name = "contactperson_email")
    private String email;

    @Column(name = "contactperson_position")
    private String position;

    public String getContactPersonName() {
        if (this.contactPerson != null) {
            return this.contactPerson.getName();
        } else {
            return null;
        }
    }
}
