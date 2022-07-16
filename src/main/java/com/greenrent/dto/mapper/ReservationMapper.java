
    // elle set etmektense herhangi bir DTO ile Entity class objesi olusturabiliyoruz.
    package com.greenrent.dto.mapper;

    import com.greenrent.dto.request.ReservationRequest;
    import org.mapstruct.Mapper;

    import com.greenrent.domain.Reservation;

    @Mapper(componentModel="spring")
    public interface ReservationMapper {
        Reservation reservationRequestToReservation(ReservationRequest reservationRequest);
    }


