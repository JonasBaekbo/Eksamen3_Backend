package com.example.eksamen3backend.service;

import com.example.eksamen3backend.model.Image;
import com.example.eksamen3backend.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ImageService implements IImage {
    private ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Set<Image> findall() {
        Set<Image> set = new HashSet<>();
        imageRepository.findAll().forEach(set::add);
        return set;
    }

    @Override
    public Image save(Image object) {
       return  imageRepository.save(object);
    }

    @Override
    public void delete(Image object) {
        imageRepository.delete(object);
    }

    @Override
    public void deleteByID(Long aLong) {
        imageRepository.deleteById(aLong);

    }

    @Override
    public Optional<Image> findbyId(Long aLong) {

            return imageRepository.findById(aLong);

    }
}
