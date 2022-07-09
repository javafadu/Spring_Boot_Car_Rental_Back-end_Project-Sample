package com.greenrent.service;

import java.io.IOException;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.greenrent.domain.ImageFile;
import com.greenrent.exception.ImageFileException;
import com.greenrent.exception.ResourceNotFoundException;
import com.greenrent.exception.message.ErrorMessage;
import com.greenrent.repository.ImageFileRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ImageFileService {

    private ImageFileRepository imageFileRepository;


    public String saveImage(MultipartFile file) {
        String fileName= StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        ImageFile imageFile=null;
        try {
            imageFile = new ImageFile(fileName, file.getContentType(), file.getBytes());
        } catch (IOException e) {

            throw new ImageFileException(e.getMessage());
        }
        imageFileRepository.save(imageFile);
        return imageFile.getId();
    }


    public ImageFile getImageById(String id) {
        ImageFile imageFile=  imageFileRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE, id)));

        return imageFile;
    }


}