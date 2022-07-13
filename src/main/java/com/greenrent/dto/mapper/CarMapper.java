package com.greenrent.dto.mapper;

import com.greenrent.domain.Car;
import com.greenrent.dto.CarDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

// @Mapper (componentModel = "spring")
@Mapper
public interface CarMapper {

    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    @Mapping(target = "image", ignore = true)
    public Car carDTOToCar(CarDTO carDTO);

}
