package com.greenrent.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name="tbl_car")
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(length=30, nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer doors;

    @Column(nullable = false)
    private Integer seats;

    @Column(nullable = false)
    private Integer luggage;

    @Column(length=30, nullable = false)
    private String transmission;

    @Column(nullable = false)
    private Boolean airConditioning;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Double pricePerHour;

    @Column(length=30, nullable = false)
    private String fuelType;

    private Boolean builtIn=false;


    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="tbl_car_image", joinColumns = @JoinColumn(name="car_id"),
            inverseJoinColumns = @JoinColumn(name="imfile_id"))
    private Set<ImageFile> image;

}