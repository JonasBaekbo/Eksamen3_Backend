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
    private String corpAddress;
    private String corpAddressToUpdate;
    private String corpLogo;
    private String corpLogoToUpdate;
    private String corpCity;
    private String corpCityToUpdate;
    private String corpCountry;
    private String corpCountryToUpdate;
}
