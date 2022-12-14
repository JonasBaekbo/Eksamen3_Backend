package com.example.eksamen3backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Optional;


@Getter
@Setter
@Entity
@JsonIgnoreProperties(value = { "id", "contactPerson","name", "corpID" ,"CPimage"})
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

    public Employment(){

    }

    public Employment(String emailEmployment, int phonenumberEmployment, String positionEmployment, Timestamp addedToCorporationEmployment) {
        this.email = emailEmployment;
        this.phonenumber = phonenumberEmployment;
        this.position = positionEmployment;
        this.addedToCorporation = addedToCorporationEmployment;
    }
    @JsonManagedReference
    public String getContactPersonName() {
        if (this.contactPerson != null) {
            return this.contactPerson.getName();
        } else {
            return null;
        }
    }

    @JsonManagedReference
    public String getCorporationName() {
        if (this.corporation != null) {
            return this.corporation.getName();
        } else {
            return null;
        }
    }

    @JsonManagedReference
    public long getContactPersonID() {
        return this.contactPerson.getId();
    }




}
