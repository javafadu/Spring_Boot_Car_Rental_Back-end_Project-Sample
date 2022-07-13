package com.greenrent.controller;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.greenrent.dto.CarDTO;
import com.greenrent.dto.response.GRResponse;
import com.greenrent.service.CarService;
import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/car")
@AllArgsConstructor
public class CarController {

    private CarService carService;

    // http://localhost:8080/car/admin/85994692-6551-45e7-97eb-7e8573c92413/add

    @PostMapping("/admin/{imageId}/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GRResponse> saveCar(@PathVariable String imageId,
                                              @Valid @RequestBody CarDTO carDTO){

        carService.saveCar(carDTO, imageId);

        GRResponse response=new GRResponse();
        response.setMessage("Car successfully saved");
        response.setSuccess(true);

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }


    @GetMapping("/visitors/all")
    public ResponseEntity<List<CarDTO>> getAllCars() {
        List<CarDTO> carDTOs = carService.getAllCars();
        return ResponseEntity.ok(carDTOs);
    }



}