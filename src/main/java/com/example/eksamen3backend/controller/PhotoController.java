package com.example.eksamen3backend.controller;

import com.example.eksamen3backend.model.Photo;
import com.example.eksamen3backend.service.PhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Set;


@RestController
public class PhotoController {
// private PhotoService PhotoService;
//
//    public PhotoController(PhotoService PhotoService) {
//        this.PhotoService = PhotoService;
//    }
//
//
//    @GetMapping("/getAllImage")
//    public ResponseEntity<Set<Photo>> findAll(){
//        return ResponseEntity.ok(PhotoService.findall());
//    }
//
//    @PostMapping("/createImage")
//    public ResponseEntity<Photo> create(@RequestBody Photo photo){
//        photo.setCreated(new Date());
//        PhotoService.save(photo);
//        return ResponseEntity.ok().body(photo);
//    }
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Photo> delete(@PathVariable("id") Integer id){
//        Photo deleteImage = images.stream().filter(element -> element.getId().equals(id)).findFirst().get();
//        images.remove(deleteImage);
//        return ResponseEntity.ok().body(deleteImage);
//    }
}
