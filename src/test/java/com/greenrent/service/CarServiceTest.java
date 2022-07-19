package com.greenrent.service;

import com.greenrent.domain.Car;
import com.greenrent.dto.CarDTO;
import com.greenrent.dto.mapper.CarMapper;
import com.greenrent.repository.CarRepository;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    // Mock layalim testi yazarken underTest objesinin icine enjecte edilsin
    @Mock
    CarRepository carRepository;

    @Mock
    CarMapper carMapper;


    // CarService injection edelim
    @InjectMocks
    CarService underTest;

    @Test
    public void getAllCarTest() {

        List<Car> list = new ArrayList<>();
        Car car = new Car();
        car.setId(1L);
        car.setModel("Mercedes");

        Car car2 = new Car();
        car2.setId(2L);
        car2.setModel("Toyota");

        Car car3 = new Car();
        car3.setId(3L);
        car3.setModel("BMW");

        list.add(car);
        list.add(car2);
        list.add(car3);


        List<CarDTO> listDTO = new ArrayList<>();
        CarDTO carDTO = new CarDTO();
        carDTO.setId(1L);
        carDTO.setModel("Mercedes");

        CarDTO carDTO2 = new CarDTO();
        carDTO2.setId(2L);
        carDTO2.setModel("Toyota");

        CarDTO carDTO3 = new CarDTO();
        carDTO3.setId(3L);
        carDTO3.setModel("BMW");

        listDTO.add(carDTO);
        listDTO.add(carDTO2);
        listDTO.add(carDTO3);

        when(carRepository.findAll()).thenReturn(list);
        when(carMapper.map(list)).thenReturn(listDTO);

        List<CarDTO> listActual =  underTest.getAllCars();

        assertEquals(3,listActual.size());

        verify(carRepository,times(1)).findAll();


    }


}
