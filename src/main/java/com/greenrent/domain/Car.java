package com.greenrent.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


@Table(name="tbl_car")
@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer doors;

    @Column(nullable = false)
    private Integer seats;

    @Column(nullable = false)
    private Integer luggage;

    @Column(nullable = false)
    private String transmission;

    @Column(nullable = false)
    private Boolean airConditioning;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Double pricePerHour;

    @Column(length = 30, nullable = false)
    private String fuelType;


    private Boolean builtIn=false;

    // car ile image arasinda iliskiyi olusturalim
    // bir arac icin many tane image i olabilir
    // manytomany oldugu icin de ucuncu bir tablo olusacak
    @ManyToMany(fetch = FetchType.LAZY) // car bilgimi cekerken image bilgileri de lazy olarak gelsin
    @JoinTable(name="tbl_car_image",joinColumns = @JoinColumn(name="car_id"),
    inverseJoinColumns = @JoinColumn(name="imfile_id"))
    private Set<ImageFile> image;





}
