package com.example.eksamen3backend.controller;

import com.example.eksamen3backend.model.Image;
import com.example.eksamen3backend.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Set;


@RestController
public class ImageController {
 private ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @GetMapping("/getAllImage")
    public ResponseEntity<Set<Image>> findAll(){
        return ResponseEntity.ok(imageService.findall());
    }

    @PostMapping("/createImage")
    public ResponseEntity<Image> create(@RequestBody Image image){
        image.setCreated(new Date());
        imageService.save(image);
        return ResponseEntity.ok().body(image);
    }
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Image> delete(@PathVariable("id") Integer id){
//        Image deleteImage = images.stream().filter(element -> element.getId().equals(id)).findFirst().get();
//        images.remove(deleteImage);
//        return ResponseEntity.ok().body(deleteImage);
//    }
}
