package com.greenrent.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.greenrent.domain.Car;
import com.greenrent.domain.enums.ReservationStatus;
import com.greenrent.dto.request.ReservationRequest;
import com.greenrent.exception.BadRequestException;
import com.greenrent.exception.ResourceNotFoundException;
import com.greenrent.exception.message.ErrorMessage;
import com.greenrent.repository.CarRepository;
import com.greenrent.repository.ReservationRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ReservationService {

    private ReservationRepository reservationRepository;

    private CarRepository carRepository;

    // Reservation :
    /*
    Below information needed
    1- authenticated user
    2- which car
    3- reservation information (pick up - drop off)

     */

    public void createReservation
            (ReservationRequest reservationRequest, Long userId,Long carId) {

        // Control1: date time control
        checkReservationTimeIsCorrect(reservationRequest.getPickUpTime(), reservationRequest.getDropOffTime());

        // Control2: is car exist or not
        Car car=carRepository.findById(carId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, carId)));


    }


    // Control2 : Is car available or not between pickupdate and dropoffdate
    public boolean checkCarAvailability(Long carId, LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
        ReservationStatus  status[]= {ReservationStatus.CANCELLED,ReservationStatus.DONE};

        //TODO: checkStatus metodu repositoryda yazılacak ve burada kullanılacak.
        return false;
    }

    // Control1 : pickup date > now | dropoff date > now && > pickup date
    private void checkReservationTimeIsCorrect(LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
        LocalDateTime now= LocalDateTime.now();

        if(pickUpTime.isBefore(now)) {
            throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);
        }

        boolean isEqual= pickUpTime.isEqual(dropOffTime)?true:false;
        boolean isBefore=pickUpTime.isBefore(dropOffTime)?true:false;

        if(isEqual||!isBefore) {
            throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);
        }
    }

}
