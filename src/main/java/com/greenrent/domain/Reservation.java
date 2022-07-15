package com.greenrent.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.greenrent.domain.enums.ReservationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "tbl_reservation")
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car carId;


    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;

    @Column(nullable = false)
    private LocalDateTime pickUpTime;

    @Column(nullable = false)
    private LocalDateTime dropOffTime;

    @Column(length = 150, nullable = false)
    private String pickUpLocation;

    @Column(length = 150, nullable = false)
    private String dropOffLocation;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private ReservationStatus status;

    @Column(nullable = false)
    private Double totalPrice;

    public Long getTotalHours(LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
        return ChronoUnit.HOURS.between(pickUpTime, dropOffTime);
    }


}