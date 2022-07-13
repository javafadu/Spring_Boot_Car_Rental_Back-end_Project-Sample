package com.greenrent.service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import com.greenrent.domain.Car;
import com.greenrent.domain.ImageFile;
import com.greenrent.dto.CarDTO;
import com.greenrent.dto.mapper.CarMapper;
import com.greenrent.exception.ResourceNotFoundException;
import com.greenrent.exception.message.ErrorMessage;
import com.greenrent.repository.CarRepository;
import com.greenrent.repository.ImageFileRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class CarService {

    // injection with @AllArgConstructor
    private CarRepository carRepository;

    private ImageFileRepository imageFileRepository;

    private CarMapper carMapper;

    @Transactional
    // lazy bir fetch type da image ler lazy olarak gelecekti
    // transactional annotasyonu lazy bir ilişki var servis methodu
    // bitene kadar
    public List<CarDTO> getAllCars(){
        List<Car> carList= carRepository.findAll();
        return carMapper.map(carList);
    }


    public void saveCar(CarDTO carDTO, String imageId) {
        ImageFile imFile=    imageFileRepository.findById(imageId).
                orElseThrow(()->new ResourceNotFoundException
                        (String.format(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE, imageId)));

       //  Car car= CarMapper.INSTANCE.carDTOToCar(carDTO);

        Car car = carMapper.carDTOToCar(carDTO);

        Set<ImageFile> imFiles=new HashSet<>();
        imFiles.add(imFile);
        car.setImage(imFiles);

        carRepository.save(car);
    }

}
