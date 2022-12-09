package com.example.eksamen3backend.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Data
public class Image {

    Integer id;
    Long size;
    String user;
    String name;
    String title;
    String description;
    String image;
    Date created;
}
