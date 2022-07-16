package com.greenrent.controller;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.greenrent.dto.ReservationDTO;
import com.greenrent.dto.request.ReservationRequest;
import com.greenrent.dto.request.ReservationUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.greenrent.dto.response.GRResponse;
import com.greenrent.dto.response.ResponseMessage;
import com.greenrent.service.ReservationService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private ReservationService reservationService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<GRResponse> makeReservation(HttpServletRequest request, @RequestParam(value="carId") Long carId,
                                                      @Valid @RequestBody ReservationRequest reservationRequest){
        Long userId= (Long) request.getAttribute("id");
        reservationService.createReservation(reservationRequest, userId, carId);

        GRResponse response=new GRResponse();
        response.setMessage(ResponseMessage.RESERVATION_SAVED_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // admin make reservation
    @PostMapping("/add/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GRResponse> addReservation(@RequestParam(value="userId") Long userId, @RequestParam(value="carId") Long carId,
                                                     @Valid @RequestBody ReservationRequest reservationRequest){

        reservationService.createReservation(reservationRequest, userId, carId);

        GRResponse response=new GRResponse();
        response.setMessage(ResponseMessage.RESERVATION_SAVED_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReservationDTO>>  getAllReservations() {
       List<ReservationDTO> reservations = reservationService.getAllReservations();
       return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id){
        ReservationDTO reservationDTO= reservationService.findById(id);
        return ResponseEntity.ok(reservationDTO);
    }

    @PutMapping("/admin/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GRResponse> updateReservation(@RequestParam(value="carId") Long carId,
                                                        @RequestParam(value="reservationId") Long reservationId,
                                                        @Valid @RequestBody ReservationUpdateRequest reservationUpdateRequest){

        reservationService.updateReservation(reservationId,carId,reservationUpdateRequest);

        GRResponse response=new GRResponse();
        response.setMessage(ResponseMessage.RESERVATION_UPDATED_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}