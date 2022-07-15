package com.greenrent.repository;

import com.greenrent.domain.Car;
import com.greenrent.dto.CarDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("SELECT new com.greenrent.dto.CarDTO(car)  FROM Car car")
    Page<CarDTO> findAllCarWithPage(Pageable pageable);

}
