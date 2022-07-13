package com.greenrent.controller;

import com.greenrent.dto.ImageFileDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.greenrent.domain.ImageFile;
import com.greenrent.dto.response.ImageSavedResponse;
import com.greenrent.dto.response.ResponseMessage;
import com.greenrent.service.ImageFileService;

import lombok.AllArgsConstructor;

import java.util.List;

@RestController // Rest api lerimi yerlestirecegim
@RequestMapping("/files") // base path belirledik
@AllArgsConstructor
public class ImageFileController {

    private ImageFileService imageFileService;
    // ImageFileService injection, bunu yapinca yukariya @AllArgsConstructor da eklememiz gerekiyor

    // image i upload eden bir method yazalim
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageSavedResponse> uploadFile(@RequestParam("file") MultipartFile file ){

        // MultipartFile : bize body degil, multipart yapi gelecek
        // icinde image in ismi, data nin oldugu, byte olarak yapi


        String imageId= imageFileService.saveImage(file);

        ImageSavedResponse response=new ImageSavedResponse();

        response.setImageId(imageId);
        response.setMessage(ResponseMessage.IMAGE_SAVED_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return ResponseEntity.ok(response);

    }

    // image leri getirebilmek icin arac bilgileri gelirken image bilgileri de gelmesi gerekiyor.
    @GetMapping("/download/{id}")
    public ResponseEntity<byte []> getImageFile(@PathVariable String id){
        ImageFile imageFile= imageFileService.getImageById(id);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+
                imageFile.getName()).body(imageFile.getData());

        // download eden taraf filename olarak download etsin, header bilgisinden okudu
        // CONTENT_DISPOSITIO ile download edebiliyoruz ya da byte kodlarını görebiliyoruz.
    }

    // image display
    @GetMapping("/display/{id}")
    public ResponseEntity<byte []> displayFile(@PathVariable String id){
        ImageFile imageFile= imageFileService.getImageById(id);

        HttpHeaders header=new HttpHeaders();
        header.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(imageFile.getData(),header,HttpStatus.OK);
    }

    // butun image dosyalarini almak icin method
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // admin tarafindan getirilebilsin
    public ResponseEntity<List<ImageFileDTO>> getAllImages() {
        List<ImageFileDTO> imageList = imageFileService.getAllImages();

        // return ResponseEntity.ok(imageList);

        return ResponseEntity.status(HttpStatus.OK).body(imageList);
    }






}