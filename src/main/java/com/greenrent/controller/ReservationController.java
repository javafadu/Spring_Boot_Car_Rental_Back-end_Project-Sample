package com.greenrent.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.greenrent.dto.CarDTO;
import com.greenrent.dto.ReservationDTO;
import com.greenrent.dto.request.ReservationRequest;
import com.greenrent.dto.request.ReservationUpdateRequest;
import com.greenrent.dto.response.CarAvailabilityResponse;
import com.greenrent.service.CarService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.greenrent.dto.response.GRResponse;
import com.greenrent.dto.response.ResponseMessage;
import com.greenrent.service.ReservationService;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private ReservationService reservationService;


    // make a reservation by authenticated user (admin or customer)

    // http://localhost:8080/reservations/add?carId=3
    /*

    {
    "pickUpTime":"10/10/2022 10:00:00",
    "dropOffTime":"10/10/2022 11:00:00",
    "pickUpLocation":"Istanbul Airport",
    "dropOffLocation":"Eseboga Airport"
}
     */

    @PostMapping("/add")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<GRResponse> makeReservation(HttpServletRequest request, @RequestParam(value = "carId") Long carId,
                                                      @Valid @RequestBody ReservationRequest reservationRequest) {
        Long userId = (Long) request.getAttribute("id");
        reservationService.createReservation(reservationRequest, userId, carId);

        GRResponse response = new GRResponse();
        response.setMessage(ResponseMessage.RESERVATION_SAVED_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // make a reservation by authenticated admin user (with using userId and carId)

    // http://localhost:8080/reservations/add/auth?userId=4&carId=6
    /*

    {
    "pickUpTime":"10/18/2022 08:00:00",
    "dropOffTime":"10/18/2022 11:00:00",
    "pickUpLocation":"Istanbul",
    "dropOffLocation":"Ankara"
}
     */
    @PostMapping("/add/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GRResponse> addReservation(@RequestParam(value = "userId") Long userId, @RequestParam(value = "carId") Long carId,
                                                     @Valid @RequestBody ReservationRequest reservationRequest) {

        reservationService.createReservation(reservationRequest, userId, carId);

        GRResponse response = new GRResponse();
        response.setMessage(ResponseMessage.RESERVATION_SAVED_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    // List all reservations by admin
    // http://localhost:8080/reservations/admin/all

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    // See a reservation with id by admin
    // http://localhost:8080/reservations/20/admin

    @GetMapping("/{id}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        ReservationDTO reservationDTO = reservationService.findById(id);
        return ResponseEntity.ok(reservationDTO);
    }

    // Update a reservation with a carId and reservationId by Admin

    // http://localhost:8080/reservations/admin/auth?carId=2&reservationId=20
    /*

    {
    "pickUpTime":"10/10/2022 10:00:00",
    "dropOffTime":"10/10/2022 11:00:00",
    "pickUpLocation":"Istanbul",
    "dropOffLocation":"Ankara",
    "status": "CREATED"
}
     */
    @PutMapping("/admin/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GRResponse> updateReservation(@RequestParam(value = "carId") Long carId,
                                                        @RequestParam(value = "reservationId") Long reservationId,
                                                        @Valid @RequestBody ReservationUpdateRequest reservationUpdateRequest) {

        reservationService.updateReservation(reservationId, carId, reservationUpdateRequest);

        GRResponse response = new GRResponse();
        response.setMessage(ResponseMessage.RESERVATION_UPDATED_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // check availibility of a car between given 2 dates
    // http://localhost:8080/reservations/auth?carId=2&pickUpDateTime=10/10/2022 10:05:00&dropOffDateTime=10/11/2022 11:55:00
    @GetMapping("/auth")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<GRResponse> checkCarIsAvailable(@RequestParam(value = "carId") Long carId,
                                                          @RequestParam(value = "pickUpDateTime") @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm:ss") LocalDateTime pickUpTime,
                                                          @RequestParam(value = "dropOffDateTime") @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm:ss") LocalDateTime dropOffTime) {

        // available mi degil mi
        boolean isNotAvailable = reservationService.checkCarAvailability(carId, pickUpTime, dropOffTime);

        // get totalPrice
        Double totalPrice = reservationService.getTotalPrice(carId, pickUpTime, dropOffTime);

        // isNotAvailable -> brings true but we can return it as !isNotAvailable
        CarAvailabilityResponse response = new CarAvailabilityResponse(!isNotAvailable, totalPrice, ResponseMessage.CAR_AVAILABLE_MESSAGE, true);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    // delete reservation
    // http://localhost:8080/reservations/admin/20/auth

    @DeleteMapping("/admin/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GRResponse> deleteReservation(@PathVariable Long id) {
        reservationService.removeById(id);
        GRResponse response = new GRResponse();

        response.setMessage(ResponseMessage.RESERVATION_DELETED_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    // return authenticated user own reservation with id
    // http://localhost:8080/reservations/24/auth

    @GetMapping("/{id}/auth")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<ReservationDTO> getUserReservationById(HttpServletRequest request, @PathVariable Long id) {
        //  @PathVariable ile reservasyon id li path i aliyoruz
        // HttpServletRequest request: logged olan user bilgisinden id sini alabilmek icin
        Long userId = (Long) request.getAttribute("id");
        ReservationDTO reservationDTO = reservationService.findByIdAndUserId(id, userId);
        return ResponseEntity.ok(reservationDTO);

    }

    // return authenticated user own all reservations
    // http://localhost:8080/reservations/auth/all

    @GetMapping("/auth/all")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<List<ReservationDTO>> getUserReservationsByUserId (HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("id");
        List<ReservationDTO> reservations = reservationService.findAllByUserId(userId);
        return ResponseEntity.ok(reservations);
    }


}