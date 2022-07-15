package com.greenrent.service;

import java.time.LocalDateTime;
import java.util.List;

import com.greenrent.domain.Reservation;
import com.greenrent.domain.User;
import com.greenrent.dto.ReservationDTO;
import com.greenrent.dto.mapper.ReservationMapper;
import com.greenrent.dto.request.ReservationUpdateRequest;
import com.greenrent.repository.UserRepository;
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
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class ReservationService {

    private ReservationRepository reservationRepository;

    private CarRepository carRepository;

    private UserRepository userRepository;

    private ReservationMapper reservationMapper;



    // Reservation :
    /*
    Below information needed
    1- authenticated user : userId
    2- which car: carId
    3- reservation information (pick up - drop off) : reservationRequest

     */

    public void createReservation
            (ReservationRequest reservationRequest, Long userId, Long carId) {


        // Control1: date time control
        checkReservationTimeIsCorrect(reservationRequest.getPickUpTime(), reservationRequest.getDropOffTime());

        // Control2: is car exist or not
        Car car = carRepository.findById(carId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, carId)));

        // Control3: is car available at that period of date
        boolean carStatus = checkCarAvailability(carId, reservationRequest.getPickUpTime(), reservationRequest.getDropOffTime());

        // Control4: check user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, userId)));


        Reservation reservation = reservationMapper.reservationRequestToReservation(reservationRequest);

        if (!carStatus) {
            reservation.setStatus(ReservationStatus.CREATED); // arac musaitse aracin statusunu set ettik
        } else {
            throw new BadRequestException(ErrorMessage.CAR_NOT_AVAILABLE_MESSAGE);
        }

        reservation.setCarId(car);
        reservation.setUserId(user);
        Double totalPrice = getTotalPrice(car, reservation.getPickUpTime(), reservation.getDropOffTime());

        reservationRepository.save(reservation);

    }

    private Double getTotalPrice(Car car, LocalDateTime pickUpTime, LocalDateTime dropffTime) {
        Long hours = (new Reservation()).getTotalHours(pickUpTime, dropffTime);
        return car.getPricePerHour() * hours;
    }

    public boolean checkCarAvailability(Long carId, LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
        ReservationStatus[] status = {ReservationStatus.CANCELLED, ReservationStatus.DONE};

        List<Reservation> existReservations = reservationRepository.checkCarStatus(carId, pickUpTime, dropOffTime, status);
        return !existReservations.isEmpty();
    }

    private void checkReservationTimeIsCorrect(LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
        LocalDateTime now = LocalDateTime.now();

        if (pickUpTime.isBefore(now)) {
            throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);
        }

        boolean isEqual = pickUpTime.isEqual(dropOffTime) ? true : false;
        boolean isBefore = pickUpTime.isBefore(dropOffTime) ? true : false;

        if (isEqual || !isBefore) {
            throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);
        }
    }

    @Transactional(readOnly=true)
    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAllBy();
    }

    @Transactional(readOnly=true)
    public ReservationDTO findById(Long id) {
       return reservationRepository.findDTOById(id).orElseThrow(()-> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,id)));
    }

    public void updateReservation(Long reservationId, Long carId, ReservationUpdateRequest reservationUpdateRequest) {

        Reservation reservation= reservationRepository.findById(reservationId).orElseThrow(()->new
                ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, reservationId)));

        checkReservationTimeIsCorrect(reservationUpdateRequest.getPickUpTime(), reservationUpdateRequest.getDropOffTime());

        Car car=carRepository.findById(carId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, carId)));


        boolean carStatus=checkCarAvailability(carId, reservationUpdateRequest.getPickUpTime(), reservationUpdateRequest.getDropOffTime());

        if(reservationUpdateRequest.getPickUpTime().compareTo(reservation.getPickUpTime())==0&&
                reservationUpdateRequest.getDropOffTime().compareTo(reservation.getDropOffTime())==0&&
                car.getId().equals(reservation.getCarId().getId())){
            reservation.setStatus(reservationUpdateRequest.getStatus());
        }else if(carStatus) {
            throw new BadRequestException(ErrorMessage.CAR_NOT_AVAILABLE_MESSAGE);
        }


        Double totalPrice = getTotalPrice(car, reservationUpdateRequest.getPickUpTime(), reservationUpdateRequest.getDropOffTime());

        reservation.setTotalPrice(totalPrice);

        reservation.setCarId(car);
        reservation.setPickUpTime(reservationUpdateRequest.getPickUpTime());
        reservation.setDropOffTime(reservationUpdateRequest.getDropOffTime());
        reservation.setPickUpLocation(reservationUpdateRequest.getPickUpLocation());
        reservation.setDropOffLocation(reservationUpdateRequest.getDropOffLocation());

        reservationRepository.save(reservation);
    }

}