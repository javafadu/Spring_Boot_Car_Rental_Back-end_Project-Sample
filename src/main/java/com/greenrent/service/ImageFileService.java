package com.greenrent.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.greenrent.dto.ImageFileDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.greenrent.domain.ImageFile;
import com.greenrent.exception.ImageFileException;
import com.greenrent.exception.ResourceNotFoundException;
import com.greenrent.exception.message.ErrorMessage;
import com.greenrent.repository.ImageFileRepository;

import lombok.AllArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@AllArgsConstructor
public class ImageFileService {

    // ImageFileRepositor injection yapalim, yukariya da AllArg le birlike
    private ImageFileRepository imageFileRepository;

    // image file in Id sini dondurecegimizden asagidaki method String deger dondurecek.

    public String saveImage(MultipartFile file) {
        // gelen image dosyasinin filename ini alalim
        String fileName= StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // cleanPath : gelen path in icinde nokta gibi seyler varsa icindeki karakterleri normalize ediyor.

        ImageFile imageFile=null;
        try {
            imageFile = new ImageFile(fileName, file.getContentType(), file.getBytes());
        } catch (IOException e) {
            // ozel bir exception olusuturalim.
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

    // tum resimleri getirmek icin servis methodu
    public List<ImageFileDTO> getAllImages(){
        List<ImageFile> imageList= imageFileRepository.findAll();

        List<ImageFileDTO> files= imageList.stream().map(imFile->{
            String imageUri= ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/files/download/")
                    .path(imFile.getId())
                    .toUriString();

            return new ImageFileDTO(imFile.getName(),imageUri,imFile.getType(),imFile.getData().length);
        }).collect(Collectors.toList());

        return files;

    }

}