package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.Photo;
import com.example.eksamen3backend.repository.PhotoRepository;
import com.example.eksamen3backend.service.IPhoto;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class PhotoService implements IPhoto {
    private PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    public Set<Photo> findall() {
        Set<Photo> set = new HashSet<>();
        photoRepository.findAll().forEach(set::add);
        return set;
    }

    @Override
    public Photo save(Photo object) {
        return photoRepository.save(object);
    }

    @Override
    public void delete(Photo object) {
        photoRepository.delete(object);
    }

    @Override
    public void deleteByID(Long aLong) {
        photoRepository.deleteById(aLong);

    }

    @Override
    public Optional<Photo> findbyId(Long aLong) {
        return photoRepository.findById(aLong);
    }

    public Photo createPhoto(String imageAsString) {
        Photo photo = new Photo();
        photo.setImageString(imageAsString);
        photo.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        photoRepository.save(photo);
        return photo;
    }
}
