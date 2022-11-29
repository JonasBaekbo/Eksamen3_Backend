package com.example.eksamen3backend.model;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateEntity {

    private String contactPersonName;
    private String contactPersonNameToUpdate;
    private int contactPersonPhonenumber;
    private int contactPersonPhonenumberToUpdate;
    private String corpName;
    private String corpNameToUpdate;
}
