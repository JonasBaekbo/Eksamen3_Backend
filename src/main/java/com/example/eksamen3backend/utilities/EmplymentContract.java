package com.example.eksamen3backend.utilities;


import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class EmplymentContract {


    //Employment infor
    private Timestamp addedToCorporationEmployment;

    private Timestamp movedFromCorporationEmployment;

    private int phonenumberEmployment;

    private String emailEmployment;

    private String positionEmployment;

    //ContactPerson
    private String nameContactPerson;
}
