package com.example.eksamen3backend.controller;

import com.example.eksamen3backend.model.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/image")
public class ImageController {

    int counter;
    List<Image> images;


    public ImageController(){
        images = new ArrayList<>();
    }
    @GetMapping
    public ResponseEntity<List<Image>> findAll(){
        return ResponseEntity.ok().body(images);
    }

    @PostMapping()
    public ResponseEntity<Image> create(@RequestBody Image image){
        image.setId(counter++);
        image.setCreated(new Date());
        images.add(image);
        return ResponseEntity.ok().body(image);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Image> delete(@PathVariable("id") Integer id){
        Image deleteImage = images.stream().filter(element -> element.getId().equals(id)).findFirst().get();
        images.remove(deleteImage);
        return ResponseEntity.ok().body(deleteImage);
    }
}
