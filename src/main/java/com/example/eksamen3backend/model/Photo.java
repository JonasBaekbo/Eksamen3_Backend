package com.example.eksamen3backend.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob()
    @Column(name = "image")
    private String imageString;

    private Date created;


    public Photo() {

    }
}
